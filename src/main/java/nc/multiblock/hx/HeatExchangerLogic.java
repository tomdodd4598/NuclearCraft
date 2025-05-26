package nc.multiblock.hx;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.multiblock.*;
import nc.network.multiblock.*;
import nc.tile.hx.*;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

import static nc.config.NCConfig.*;

public class HeatExchangerLogic extends MultiblockLogic<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart> implements IPacketMultiblockLogic<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart, HeatExchangerUpdatePacket> {
	
	public HeatExchangerLogic(HeatExchanger exchanger) {
		super(exchanger);
	}
	
	public HeatExchangerLogic(HeatExchangerLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "heat_exchanger";
	}
	
	public boolean isCondenser() {
		return false;
	}
	
	// Multiblock Size Limits
	
	@Override
	public int getMinimumInteriorLength() {
		return heat_exchanger_min_size;
	}
	
	@Override
	public int getMaximumInteriorLength() {
		return heat_exchanger_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onMachineAssembled() {
		onExchangerFormed();
	}
	
	@Override
	public void onMachineRestored() {
		onExchangerFormed();
	}
	
	protected void onExchangerFormed() {
		for (IHeatExchangerController<?> contr : getParts(IHeatExchangerController.class)) {
			multiblock.controller = contr;
			break;
		}
		
		if (!getWorld().isRemote) {
			setupExchanger();
			refreshAll();
			setIsExchangerOn();
		}
	}
	
	protected void setupExchanger() {
		int volume = multiblock.getExteriorVolume();
		multiblock.shellTanks.get(0).setCapacity(HeatExchanger.BASE_MAX_INPUT * volume);
		multiblock.shellTanks.get(1).setCapacity(HeatExchanger.BASE_MAX_OUTPUT * volume);
		
		Long2ObjectMap<TileHeatExchangerTube> tubeMap = getPartMap(TileHeatExchangerTube.class);
		Long2ObjectMap<TileHeatExchangerInlet> inletMap = getPartMap(TileHeatExchangerInlet.class);
		
		for (HeatExchangerTubeNetwork network : multiblock.networks) {
			for (long inletPosLong : network.inletPosLongSet) {
				network.masterInlet = inletMap.get(inletPosLong);
				network.masterInlet.isMasterInlet = true;
				break;
			}
			
			List<Tank> tanks = network.getTanks();
			int capacityMult = network.tubePosLongSet.size() + 2;
			tanks.get(0).setCapacity(HeatExchanger.BASE_MAX_INPUT * capacityMult);
			tanks.get(1).setCapacity(HeatExchanger.BASE_MAX_OUTPUT * capacityMult);
			
			network.setFlowStats(tubeMap);
		}
		
		multiblock.totalNetworkCount = multiblock.networks.size();
	}
	
	@Override
	public void onMachinePaused() {
		onExchangerBroken();
	}
	
	@Override
	public void onMachineDisassembled() {
		onExchangerBroken();
	}
	
	public void onExchangerBroken() {
		multiblock.masterShellInlet = null;
		multiblock.networks.clear();
		
		multiblock.shellRecipe = null;
		
		multiblock.totalNetworkCount = multiblock.activeNetworkCount = 0;
		multiblock.activeTubeCount = multiblock.activeContactCount = 0;
		multiblock.shellSpeedMultiplier = 0D;
		multiblock.tubeInputRate = multiblock.tubeInputRateFP = 0D;
		multiblock.shellInputRate = multiblock.shellInputRateFP = 0D;
		multiblock.heatTransferRate = multiblock.heatTransferRateFP = 0D;
		multiblock.totalTempDiff = 0D;
		
		setIsExchangerOn();
	}
	
	@Override
	public boolean isMachineWhole() {
		if (containsBlacklistedPart()) {
			return false;
		}
		
		multiblock.masterShellInlet = null;
		multiblock.networks.clear();
		
		multiblock.shellRecipe = null;
		
		multiblock.totalNetworkCount = 0;
		
		Long2ObjectMap<TileHeatExchangerTube> tubeMap = getPartMap(TileHeatExchangerTube.class);
		Long2ObjectMap<TileHeatExchangerBaffle> baffleMap = getPartMap(TileHeatExchangerBaffle.class);
		
		for (TileHeatExchangerTube tube : tubeMap.values()) {
			tube.tubeFlow = null;
			tube.shellFlow = null;
		}
		
		Long2ObjectMap<TileHeatExchangerInlet> inletMap = getPartMap(TileHeatExchangerInlet.class);
		Long2ObjectMap<TileHeatExchangerOutlet> outletMap = getPartMap(TileHeatExchangerOutlet.class);
		
		for (TileHeatExchangerInlet inlet : inletMap.values()) {
			inlet.isMasterInlet = false;
			inlet.network = null;
		}
		
		for (TileHeatExchangerOutlet outlet : outletMap.values()) {
			outlet.network = null;
		}
		
		LongSet tubeInletPosLongSet = new LongOpenHashSet();
		LongSet tubeOutletPosLongSet = new LongOpenHashSet();
		
		LongSet visitedTubePosLongSet = new LongOpenHashSet();
		
		for (long tubePosLong : tubeMap.keySet()) {
			if (!visitedTubePosLongSet.contains(tubePosLong)) {
				HeatExchangerTubeNetwork network = new HeatExchangerTubeNetwork(this);
				
				LongList tubePosLongStack = new LongArrayList();
				LongSupplier popTubePosLong = () -> tubePosLongStack.removeLong(tubePosLongStack.size() - 1);
				
				visitedTubePosLongSet.add(tubePosLong);
				tubePosLongStack.add(tubePosLong);
				
				while (!tubePosLongStack.isEmpty()) {
					long nextPosLong = popTubePosLong.getAsLong();
					BlockPos nextPos = BlockPos.fromLong(nextPosLong);
					
					TileHeatExchangerTube tube = tubeMap.get(nextPosLong);
					HeatExchangerTubeSetting[] tubeSettings = tube.settings;
					
					for (int i = 0; i < 6; ++i) {
						if (tubeSettings[i].isOpen()) {
							EnumFacing dir = EnumFacing.VALUES[i];
							long offsetPosLong = nextPos.offset(dir).toLong();
							
							TileHeatExchangerInlet inlet = inletMap.get(offsetPosLong);
							if (inlet != null) {
								tubeInletPosLongSet.add(offsetPosLong);
								network.inletPosLongSet.add(offsetPosLong);
								inlet.network = network;
								continue;
							}
							
							TileHeatExchangerOutlet outlet = outletMap.get(offsetPosLong);
							if (outlet != null) {
								tubeOutletPosLongSet.add(offsetPosLong);
								network.outletPosLongSet.add(offsetPosLong);
								outlet.network = network;
								continue;
							}
							
							TileHeatExchangerTube other = tubeMap.get(offsetPosLong);
							if (other != null && other.getTubeSetting(dir.getOpposite()).isOpen()) {
								if (!visitedTubePosLongSet.contains(offsetPosLong)) {
									visitedTubePosLongSet.add(offsetPosLong);
									tubePosLongStack.add(offsetPosLong);
								}
								continue;
							}
							
							multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.dangling_tube", nextPos);
							return false;
						}
					}
					
					network.tubePosLongSet.add(nextPosLong);
				}
				
				multiblock.networks.add(network);
			}
		}
		
		for (HeatExchangerTubeNetwork network : multiblock.networks) {
			if (network.inletPosLongSet.isEmpty() || network.outletPosLongSet.isEmpty()) {
				for (long posLong : network.tubePosLongSet) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.invalid_network", BlockPos.fromLong(posLong));
					return false;
				}
			}
		}
		
		LongSet shellInletPosLongSet = new LongRBTreeSet(), shellOutletPosLongSet = new LongOpenHashSet();
		
		for (long inletPosLong : inletMap.keySet()) {
			if (!tubeInletPosLongSet.contains(inletPosLong)) {
				shellInletPosLongSet.add(inletPosLong);
			}
		}
		
		for (long outletPosLong : outletMap.keySet()) {
			if (!tubeOutletPosLongSet.contains(outletPosLong)) {
				shellOutletPosLongSet.add(outletPosLong);
			}
		}
		
		for (HeatExchangerTubeNetwork network : multiblock.networks) {
			network.setTubeFlows(tubeMap);
		}
		
		if (shellInletPosLongSet.isEmpty() || shellOutletPosLongSet.isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.invalid_shell", null);
			return false;
		}
		
		Long shellStartPosLong = null;
		
		for (long inletPosLong : shellInletPosLongSet) {
			BlockPos inletPos = BlockPos.fromLong(inletPosLong);
			long clampedPosLong = multiblock.getClampedInteriorCoord(inletPos).toLong();
			if (baffleMap.containsKey(clampedPosLong)) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.blocked_inlet", inletPos);
				return false;
			}
			
			if (shellStartPosLong == null) {
				shellStartPosLong = clampedPosLong;
			}
			
			if (multiblock.masterShellInlet == null) {
				multiblock.masterShellInlet = inletMap.get(inletPosLong);
				multiblock.masterShellInlet.isMasterInlet = true;
			}
		}
		
		for (long outletPosLong : shellOutletPosLongSet) {
			BlockPos outletPos = BlockPos.fromLong(outletPosLong);
			if (baffleMap.containsKey(multiblock.getClampedInteriorCoord(outletPos).toLong())) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.blocked_outlet", outletPos);
				return false;
			}
		}
		
		LongSet shellPosLongSet = new LongOpenHashSet();
		LongList shellPosLongStack = new LongArrayList();
		LongSupplier popShellPosLong = () -> shellPosLongStack.removeLong(shellPosLongStack.size() - 1);
		
		shellPosLongSet.add(shellStartPosLong);
		shellPosLongStack.add(shellStartPosLong);
		
		while (!shellPosLongStack.isEmpty()) {
			long nextPosLong = popShellPosLong.getAsLong();
			BlockPos nextPos = BlockPos.fromLong(nextPosLong);
			
			if (!tubeMap.containsKey(nextPosLong) && !MaterialHelper.isEmpty(getWorld().getBlockState(nextPos).getMaterial())) {
				multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_interior", nextPos, nextPos.getX(), nextPos.getY(), nextPos.getZ());
				return false;
			}
			
			HeatExchangerTubeSetting[] tubeSettings = tubeMap.containsKey(nextPosLong) ? tubeMap.get(nextPosLong).settings : null;
			
			for (int i = 0; i < 6; ++i) {
				if (tubeSettings == null || !tubeSettings[i].isBaffle()) {
					EnumFacing dir = EnumFacing.VALUES[i];
					BlockPos offsetPos = nextPos.offset(dir);
					long offsetPosLong = offsetPos.toLong();
					
					if (multiblock.isInInterior(offsetPos) && !shellPosLongSet.contains(offsetPosLong) && (!baffleMap.containsKey(offsetPosLong) || (tubeMap.containsKey(offsetPosLong) && !tubeMap.get(offsetPosLong).getTubeSetting(dir.getOpposite()).isBaffle()))) {
						shellPosLongSet.add(offsetPosLong);
						shellPosLongStack.add(offsetPosLong);
					}
				}
			}
		}
		
		if (shellPosLongSet.size() + baffleMap.size() != multiblock.getInteriorVolume()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.blocked_shell", null);
			return false;
		}
		
		Long2ObjectMap<ObjectSet<Vec3d>> flowMap = HeatExchangerFlowHelper.getFlowMap(
				shellInletPosLongSet,
				shellOutletPosLongSet,
				x -> LambdaHelper.let(x.toLong(), y -> tubeMap.containsKey(y) ? tubeMap.get(y).settings : null),
				x -> !x.isBaffle(),
				(x, y) -> {
					long posLong = x.toLong();
					return shellPosLongSet.contains(posLong) && (!tubeMap.containsKey(posLong) || !tubeMap.get(posLong).getTubeSetting(y.getOpposite()).isBaffle());
				},
				x -> shellOutletPosLongSet.contains(x.toLong())
		);
		
		for (Long2ObjectMap.Entry<ObjectSet<Vec3d>> entry : flowMap.long2ObjectEntrySet()) {
			long posLong = entry.getLongKey();
			if (tubeMap.containsKey(posLong)) {
				TileHeatExchangerTube tube = tubeMap.get(posLong);
				if (tube.tubeFlow != null) {
					tube.shellFlow = entry.getValue().stream().reduce(Vec3d.ZERO, Vec3d::add).normalize();
				}
			}
		}
		
		for (IHeatExchangerController<?> controller : getParts(IHeatExchangerController.class)) {
			controller.setIsRenderer(false);
		}
		for (IHeatExchangerController<?> controller : getParts(IHeatExchangerController.class)) {
			controller.setIsRenderer(true);
			break;
		}
		
		return true;
	}
	
	@Override
	public List<Pair<Class<? extends IHeatExchangerPart>, String>> getPartBlacklist() {
		return Collections.emptyList();
	}
	
	@Override
	public void onAssimilate(HeatExchanger assimilated) {}
	
	@Override
	public void onAssimilated(HeatExchanger assimilator) {}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		double prevTubeInputRate = multiblock.tubeInputRate;
		double prevShellInputRate = multiblock.shellInputRate;
		double prevHeatTransferRate = multiblock.heatTransferRate;
		double prevHeatDissipationRate = multiblock.heatDissipationRate;
		
		multiblock.refreshFlag = false;
		multiblock.packetFlag = 0;
		
		multiblock.activeNetworkCount = 0;
		multiblock.activeTubeCount = 0;
		multiblock.activeContactCount = 0;
		multiblock.shellSpeedMultiplier = 0D;
		multiblock.tubeInputRate = 0D;
		multiblock.shellInputRate = 0D;
		multiblock.heatTransferRate = 0D;
		multiblock.heatDissipationRate = 0D;
		multiblock.totalTempDiff = 0D;
		
		int[] inletUpdates = multiblock.getMasterInlets().mapToInt(x -> x.processor.onTick() ? 1 : 0).toArray();
		boolean shouldUpdate = multiblock.refreshFlag || Arrays.stream(inletUpdates).anyMatch(x -> x != 0);
		
		double tubeInputRateDiff = Math.abs(prevTubeInputRate - multiblock.tubeInputRate);
		double tubeInputRateRoundingFactor = Math.max(0D, 1.5D * Math.log1p(multiblock.tubeInputRate / (1D + prevTubeInputRate)));
		multiblock.tubeInputRateFP = (tubeInputRateRoundingFactor * multiblock.tubeInputRateFP + multiblock.tubeInputRate) / (1D + tubeInputRateRoundingFactor);
		
		double shellInputRateDiff = Math.abs(prevShellInputRate - multiblock.shellInputRate);
		double shellInputRateRoundingFactor = Math.max(0D, 1.5D * Math.log1p(multiblock.shellInputRate / (1D + prevShellInputRate)));
		multiblock.shellInputRateFP = (shellInputRateRoundingFactor * multiblock.shellInputRateFP + multiblock.shellInputRate) / (1D + shellInputRateRoundingFactor);
		
		double heatTransferRateDiff = Math.abs(prevHeatTransferRate - multiblock.heatTransferRate);
		double heatTransferRateRoundingFactor = Math.max(0D, 1.5D * Math.log1p(multiblock.heatTransferRate / (1D + prevHeatTransferRate)));
		multiblock.heatTransferRateFP = (heatTransferRateRoundingFactor * multiblock.heatTransferRateFP + multiblock.heatTransferRate) / (1D + heatTransferRateRoundingFactor);
		
		double heatDissipationRateDiff = Math.abs(prevHeatDissipationRate - multiblock.heatDissipationRate);
		double heatDissipationRateRoundingFactor = Math.max(0D, 1.5D * Math.log1p(multiblock.heatDissipationRate / (1D + prevHeatDissipationRate)));
		multiblock.heatDissipationRateFP = (heatDissipationRateRoundingFactor * multiblock.heatDissipationRateFP + multiblock.heatDissipationRate) / (1D + heatDissipationRateRoundingFactor);
		
		if (shouldUpdate) {
			refreshAll();
		}
		
		if (multiblock.packetFlag > 1) {
			multiblock.sendMultiblockUpdatePacketToAll();
		}
		else if (multiblock.packetFlag > 0) {
			multiblock.sendMultiblockUpdatePacketToListeners();
		}
		
		if (multiblock.controller != null) {
			multiblock.sendRenderPacketToAll();
		}
		
		return shouldUpdate;
	}
	
	public void setActivity(boolean isExchangerOn) {
		multiblock.controller.setActivity(isExchangerOn);
	}
	
	public void setIsExchangerOn() {
		boolean oldIsExchangerOn = multiblock.isExchangerOn;
		multiblock.isExchangerOn = (isRedstonePowered() || multiblock.computerActivated) && multiblock.isAssembled();
		if (multiblock.isExchangerOn != oldIsExchangerOn) {
			if (multiblock.controller != null) {
				setActivity(multiblock.isExchangerOn);
				multiblock.sendMultiblockUpdatePacketToAll();
			}
		}
	}
	
	protected boolean isRedstonePowered() {
		return Stream.concat(Stream.of(multiblock.controller), getParts(TileHeatExchangerRedstonePort.class).stream()).anyMatch(x -> x != null && x.getIsRedstonePowered());
	}
	
	public void refreshRecipe() {
		multiblock.getMasterInlets().forEach(x -> x.processor.refreshRecipe());
	}
	
	public void refreshActivity() {
		multiblock.getMasterInlets().forEach(x -> x.processor.refreshActivity());
	}
	
	public void refreshAll() {
		multiblock.getMasterInlets().forEach(x -> x.processor.refreshAll());
	}
	
	public @Nonnull List<Tank> getInletTanks(HeatExchangerTubeNetwork network) {
		List<Tank> tanks = network != null ? network.getTanks() : (multiblock.isAssembled() ? multiblock.shellTanks : Collections.emptyList());
		return tanks.isEmpty() ? Collections.emptyList() : tanks.subList(0, 1);
	}
	
	public @Nonnull List<Tank> getOutletTanks(HeatExchangerTubeNetwork network) {
		List<Tank> tanks = network != null ? network.getTanks() : (multiblock.isAssembled() ? multiblock.shellTanks : Collections.emptyList());
		return tanks.size() < 2 ? Collections.emptyList() : tanks.subList(1, 2);
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound data, SyncReason syncReason) {
	
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound data, SyncReason syncReason) {
	
	}
	
	// Packets
	
	@Override
	public HeatExchangerUpdatePacket getMultiblockUpdatePacket() {
		return new HeatExchangerUpdatePacket(multiblock.controller.getTilePos(), multiblock.isExchangerOn, multiblock.totalNetworkCount, multiblock.activeNetworkCount, multiblock.activeTubeCount, multiblock.activeContactCount, multiblock.tubeInputRateFP, multiblock.shellInputRateFP, multiblock.heatTransferRateFP, multiblock.heatDissipationRateFP, multiblock.totalTempDiff);
	}
	
	@Override
	public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
		multiblock.isExchangerOn = message.isExchangerOn;
		multiblock.totalNetworkCount = message.totalNetworkCount;
		multiblock.activeNetworkCount = message.activeNetworkCount;
		multiblock.activeTubeCount = message.activeTubeCount;
		multiblock.activeContactCount = message.activeContactCount;
		multiblock.tubeInputRateFP = message.tubeInputRateFP;
		multiblock.shellInputRateFP = message.shellInputRateFP;
		multiblock.heatTransferRateFP = message.heatTransferRateFP;
		multiblock.heatDissipationRateFP = message.heatDissipationRateFP;
		multiblock.totalTempDiff = message.totalTempDiff;
	}
	
	public HeatExchangerRenderPacket getRenderPacket() {
		return new HeatExchangerRenderPacket(multiblock.controller.getTilePos(), multiblock.shellTanks);
	}
	
	public void onRenderPacket(HeatExchangerRenderPacket message) {
		TankInfo.readInfoList(message.shellTankInfos, multiblock.shellTanks);
	}
	
	// Multiblock Validators
	
	@Override
	public boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return true;
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {}
}
