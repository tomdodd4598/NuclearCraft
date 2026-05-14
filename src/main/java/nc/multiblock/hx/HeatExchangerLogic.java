package nc.multiblock.hx;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.multiblock.*;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.hx.*;
import nc.tile.hx.TileHeatExchangerInlet.InletProcessorElement;
import nc.tile.internal.fluid.*;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.*;
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
	
	protected boolean isHX() {
		return getID().equals("heat_exchanger");
	}
	
	protected int getShellInputTankDensity() {
		return HeatExchanger.BASE_MAX_INPUT;
	}
	
	protected int getShellOutputTankDensity() {
		return HeatExchanger.BASE_MAX_OUTPUT;
	}
	
	protected int getTubeInputTankDensity() {
		return HeatExchanger.BASE_MAX_INPUT;
	}
	
	protected int getTubeOutputTankDensity() {
		return HeatExchanger.BASE_MAX_OUTPUT;
	}
	
	protected Set<String> getShellValidFluids() {
		return NCRecipes.heat_exchanger.validFluids.get(0);
	}
	
	protected Set<String> getTubeValidFluids() {
		return NCRecipes.heat_exchanger.validFluids.get(0);
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
		multiblock.shellTanks.get(0).setCapacity(getShellInputTankDensity() * volume);
		multiblock.shellTanks.get(1).setCapacity(getShellOutputTankDensity() * volume);
		
		multiblock.shellTanks.get(0).setAllowedFluids(getShellValidFluids());
		
		Long2ObjectMap<TileHeatExchangerTube> tubeMap = getPartMap(TileHeatExchangerTube.class);
		Long2ObjectMap<TileHeatExchangerInlet> inletMap = getPartMap(TileHeatExchangerInlet.class);
		
		Set<String> tubeValidFluids = getTubeValidFluids();
		
		for (HeatExchangerTubeNetwork network : multiblock.networks) {
			for (long inletPosLong : network.inletPosLongSet) {
				network.masterInlet = inletMap.get(inletPosLong);
				network.masterInlet.isMasterInlet = true;
				break;
			}
			
			List<Tank> tanks = network.getTanks();
			int capacityMult = network.tubePosLongSet.size() + 2;
			tanks.get(0).setCapacity(getTubeInputTankDensity() * capacityMult);
			tanks.get(1).setCapacity(getTubeOutputTankDensity() * capacityMult);
			
			tanks.get(0).setAllowedFluids(tubeValidFluids);
			
			network.setFlowStats(tubeMap);
		}
		
		for (TileHeatExchangerInlet inlet : inletMap.values()) {
			inlet.markDirtyAndNotify(true);
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
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.invalid_shell", Collections.emptyList());
			return false;
		}
		
		LongSet shellPosLongSet = new LongOpenHashSet();
		LongList shellPosLongStack = new LongArrayList();
		LongSupplier popShellPosLong = () -> shellPosLongStack.removeLong(shellPosLongStack.size() - 1);
		
		for (long inletPosLong : shellInletPosLongSet) {
			BlockPos inletPos = BlockPos.fromLong(inletPosLong);
			long clampedPosLong = multiblock.getClampedInteriorCoord(inletPos).toLong();
			if (baffleMap.containsKey(clampedPosLong)) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.blocked_inlet", inletPos);
				return false;
			}
			
			shellPosLongSet.add(clampedPosLong);
			shellPosLongStack.add(clampedPosLong);
			
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
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.heat_exchanger.blocked_shell", Collections.emptyList());
			return false;
		}
		
		Long2ObjectMap<ObjectSet<Vec3d>> flowMap = HeatExchangerFlowHelper.getFlowMap(shellInletPosLongSet, shellOutletPosLongSet, x -> LambdaHelper.let(x.toLong(), y -> tubeMap.containsKey(y) ? tubeMap.get(y).settings : null), x -> !x.isBaffle(), (x, y) -> {
			long posLong = x.toLong();
			return shellPosLongSet.contains(posLong) && (!tubeMap.containsKey(posLong) || !tubeMap.get(posLong).getTubeSetting(y.getOpposite()).isBaffle());
		}, x -> shellOutletPosLongSet.contains(x.toLong()));
		
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
		multiblock.refreshFlag = false;
		multiblock.packetFlag = 0;
		
		boolean shouldUpdate = onUpdateServerInternal();
		
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
	
	protected boolean onUpdateServerInternal() {
		multiblock.activeNetworkCount = 0;
		multiblock.activeTubeCount = 0;
		multiblock.activeContactCount = 0;
		multiblock.shellSpeedMultiplier = 0D;
		multiblock.tubeInputRate = 0D;
		multiblock.shellInputRate = 0D;
		multiblock.heatTransferRate = 0D;
		multiblock.totalTempDiff = 0D;
		
		if (isHX()) {
			prepareTransferProposals();
		}
		
		int[] inletUpdates = multiblock.getMasterInlets().mapToInt(x -> x.processor.onTick() ? 1 : 0).toArray();
		boolean shouldUpdate = multiblock.refreshFlag || Arrays.stream(inletUpdates).anyMatch(x -> x != 0);
		
		multiblock.tubeInputRateFP = multiblock.tubeInputRateTracker.update(multiblock.tubeInputRate);
		multiblock.shellInputRateFP = multiblock.shellInputRateTracker.update(multiblock.shellInputRate);
		multiblock.heatTransferRateFP = multiblock.heatTransferRateTracker.update(multiblock.heatTransferRate);
		
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
	
	public void setNetworkTubeFlows(HeatExchangerTubeNetwork network, Long2ObjectMap<TileHeatExchangerTube> tubeMap) {
		Long2ObjectMap<ObjectSet<Vec3d>> flowMap = HeatExchangerFlowHelper.getFlowMap(network.inletPosLongSet, network.outletPosLongSet, x -> LambdaHelper.let(x.toLong(), y -> network.tubePosLongSet.contains(y) ? tubeMap.get(y).settings : null), HeatExchangerTubeSetting::isOpen, (x, y) -> network.tubePosLongSet.contains(x.toLong()), x -> network.outletPosLongSet.contains(x.toLong()));
		
		for (Long2ObjectMap.Entry<ObjectSet<Vec3d>> entry : flowMap.long2ObjectEntrySet()) {
			TileHeatExchangerTube tube = tubeMap.get(entry.getLongKey());
			tube.tubeFlow = entry.getValue().stream().reduce(Vec3d.ZERO, Vec3d::add).normalize();
		}
	}
	
	public void setNetworkFlowStats(HeatExchangerTubeNetwork network, Long2ObjectMap<TileHeatExchangerTube> tubeMap) {
		network.usefulTubeCount = 0;
		network.tubeFlow = Vec3d.ZERO;
		network.shellFlow = Vec3d.ZERO;
		network.flowCosine = 0D;
		network.baseHeatingMultiplier = 0D;
		network.baseCoolingMultiplier = 0D;
		
		for (long tubePosLong : network.tubePosLongSet) {
			TileHeatExchangerTube tube = tubeMap.get(tubePosLong);
			if (tube.tubeFlow != null && (tube.shellFlow != null || !isHX())) {
				++network.usefulTubeCount;
				network.tubeFlow = network.tubeFlow.add(tube.tubeFlow);
				if (tube.shellFlow != null) {
					network.shellFlow = network.shellFlow.add(tube.shellFlow);
					network.flowCosine += tube.tubeFlow.dotProduct(tube.shellFlow);
				}
				network.baseHeatingMultiplier += tube.heatTransferCoefficient * tube.heatRetentionMult;
				network.baseCoolingMultiplier += tube.heatTransferCoefficient;
			}
		}
		
		if (network.usefulTubeCount > 0) {
			network.tubeFlow = network.tubeFlow.scale(1D / network.usefulTubeCount);
			network.shellFlow = network.shellFlow.scale(1D / network.usefulTubeCount);
			network.flowCosine /= network.usefulTubeCount;
		}
	}
	
	public BasicRecipeHandler getInletRecipeHandler(TileHeatExchangerInlet inlet) {
		return NCRecipes.heat_exchanger;
	}
	
	public void setInletRecipeStats(TileHeatExchangerInlet inlet, @Nullable BasicRecipe recipe) {
		if (recipe == null) {
			inlet.processor.baseProcessTime = 1D;
			inlet.inputTemperature = 300;
			inlet.outputTemperature = 300;
			inlet.isHeating = false;
		}
		else {
			inlet.processor.baseProcessTime = recipe.getHeatExchangerHeatDifference();
			inlet.inputTemperature = recipe.getHeatExchangerInputTemperature();
			inlet.outputTemperature = recipe.getHeatExchangerOutputTemperature();
			inlet.isHeating = recipe.getHeatExchangerIsHeating();
		}
	}
	
	public double getInletSpeedMultiplier(TileHeatExchangerInlet inlet) {
		return inlet.speedMultiplierProposal;
	}
	
	public void inletProcess(TileHeatExchangerInlet inlet) {
		InletProcessorElement processor = inlet.processor;
		
		double speedMultiplier = inlet.speedMultiplierProposal;
		double maxProcessCount = processor.baseProcessTime <= 0D ? 0D : speedMultiplier / processor.baseProcessTime;
		
		processor.time += speedMultiplier;
		
		int processCount = 0;
		while (processor.time >= processor.baseProcessTime) {
			processor.finishProcess();
			++processCount;
		}
		
		multiblock.heatTransferRate += inlet.heatTransferRateProposal * (processCount == 0 || maxProcessCount <= 0D ? 1D : processCount / maxProcessCount);
	}
	
	protected void prepareTransferProposals() {
		multiblock.getMasterInlets().forEach(x -> x.speedMultiplierProposal = x.heatTransferRateProposal = 0D);
		
		TileHeatExchangerInlet shellInlet = multiblock.masterShellInlet;
		if (shellInlet == null || shellInlet.processor.recipeInfo == null || !shellInlet.processor.readyToProcess()) {
			return;
		}
		
		List<HeatExchangerTransferProposal> proposals = new ArrayList<>();
		for (HeatExchangerTubeNetwork network : multiblock.networks) {
			TileHeatExchangerInlet tubeInlet = network.masterInlet;
			if (tubeInlet == null || tubeInlet.processor.recipeInfo == null || !tubeInlet.processor.readyToProcess()) {
				continue;
			}
			
			HeatExchangerTransferProposal proposal = getInletTransferProposal(tubeInlet);
			if (proposal == null) {
				continue;
			}
			
			double maxTubeTransferRate = getInletMaxLatentTransferRate(tubeInlet);
			if (maxTubeTransferRate <= 0D || proposal.tubeRecipeRateMultiplier <= 0D) {
				continue;
			}
			
			double cappedSharedTransferRate = Math.min(proposal.sharedTransferRate, maxTubeTransferRate / proposal.tubeRecipeRateMultiplier);
			if (cappedSharedTransferRate <= 0D) {
				continue;
			}
			
			proposals.add(proposal.withSharedTransferRate(cappedSharedTransferRate));
		}
		
		if (proposals.isEmpty()) {
			return;
		}
		
		double maxShellTransferRate = getInletMaxLatentTransferRate(shellInlet);
		if (maxShellTransferRate <= 0D) {
			return;
		}
		
		double totalShellDemandRate = 0D;
		for (HeatExchangerTransferProposal proposal : proposals) {
			totalShellDemandRate += proposal.sharedTransferRate * proposal.shellRecipeRateMultiplier;
		}
		if (totalShellDemandRate <= 0D) {
			return;
		}
		
		double shellRateFactor = Math.min(1D, maxShellTransferRate / totalShellDemandRate);
		for (HeatExchangerTransferProposal proposal : proposals) {
			double sharedTransferProposal = proposal.sharedTransferRate * shellRateFactor;
			if (sharedTransferProposal <= 0D) {
				continue;
			}
			
			proposal.inlet.speedMultiplierProposal = sharedTransferProposal * proposal.tubeRecipeRateMultiplier;
			proposal.inlet.heatTransferRateProposal = sharedTransferProposal * proposal.heatTransferRateMultiplier;
			shellInlet.speedMultiplierProposal += sharedTransferProposal * proposal.shellRecipeRateMultiplier;
			
			multiblock.totalTempDiff += proposal.absMeanTempDiff * proposal.usefulTubeCount;
			multiblock.activeContactCount += proposal.usefulTubeCount;
			++multiblock.activeNetworkCount;
			multiblock.activeTubeCount += proposal.usefulTubeCount;
		}
	}
	
	protected double getInletMaxLatentTransferRate(TileHeatExchangerInlet inlet) {
		InletProcessorElement processor = inlet.processor;
		if (processor.recipeInfo == null || !processor.readyToProcess() || processor.baseProcessTime <= 0D) {
			return 0D;
		}
		
		int maxRecipeRate = getInletMaxRecipeRate(inlet);
		if (maxRecipeRate <= 0) {
			return 0D;
		}
		
		return Math.max(0D, maxRecipeRate * processor.baseProcessTime - processor.time);
	}
	
	protected int getInletMaxRecipeRate(TileHeatExchangerInlet inlet) {
		InletProcessorElement processor = inlet.processor;
		if (processor.recipeInfo == null || !processor.readyToProcess()) {
			return 0;
		}
		
		List<Tank> tanks = processor.getTanks();
		if (processor.getFluidInputSize() != 1 || processor.getFluidOutputSize() != 1 || tanks.size() < 2) {
			return 0;
		}
		
		int inputIndex = processor.getFluidInputTank(0);
		int outputIndex = processor.getFluidOutputTank(0);
		Tank inputTank = tanks.get(inputIndex);
		Tank outputTank = tanks.get(outputIndex);
		
		int recipeInputSize = 0;
		if (processor.hasConsumed && !processor.consumedTanks.isEmpty() && !processor.consumedTanks.get(0).isEmpty()) {
			recipeInputSize = processor.consumedTanks.get(0).getFluidAmount();
		}
		if (recipeInputSize <= 0) {
			IFluidIngredient ingredient = processor.getFluidIngredients().get(0);
			recipeInputSize = ingredient.getMaxStackSize(processor.recipeInfo.getFluidIngredientNumbers().get(0));
		}
		if (recipeInputSize <= 0) {
			return 0;
		}
		
		int consumedInputSize = processor.hasConsumed && !processor.consumedTanks.isEmpty() && !processor.consumedTanks.get(0).isEmpty() ? processor.consumedTanks.get(0).getFluidAmount() : 0;
		int maxInputRecipeRate = (consumedInputSize + inputTank.getFluidAmount()) / recipeInputSize;
		if (maxInputRecipeRate <= 0) {
			return 0;
		}
		
		if (processor.getTankOutputSetting(outputIndex) == TankOutputSetting.VOID) {
			return maxInputRecipeRate;
		}
		
		IFluidIngredient product = processor.getFluidProducts().get(0);
		int recipeOutputSize = product.getMaxStackSize(0);
		if (recipeOutputSize <= 0) {
			return maxInputRecipeRate;
		}
		
		FluidStack productStack = product.getStack();
		if (productStack == null) {
			return 0;
		}
		
		int maxOutputRecipeRate;
		if (outputTank.isEmpty()) {
			maxOutputRecipeRate = processor.getFluidProductCapacity(outputTank, productStack) / recipeOutputSize;
		}
		else if (!outputTank.getFluid().isFluidEqual(productStack)) {
			return 0;
		}
		else {
			maxOutputRecipeRate = (processor.getFluidProductCapacity(outputTank, productStack) - outputTank.getFluidAmount()) / recipeOutputSize;
		}
		
		return Math.min(maxInputRecipeRate, maxOutputRecipeRate);
	}
	
	public @Nullable HeatExchangerTransferProposal getInletTransferProposal(TileHeatExchangerInlet inlet) {
		if (inlet.isMasterShellInlet() || inlet.network == null || inlet.processor.recipeInfo == null || multiblock.masterShellInlet == null || multiblock.masterShellInlet.processor.recipeInfo == null) {
			return null;
		}
		
		BasicRecipe shellRecipe = multiblock.masterShellInlet.processor.recipeInfo.recipe;
		boolean shellIsHeating = shellRecipe.getHeatExchangerIsHeating();
		if (inlet.isHeating == shellIsHeating) {
			return null;
		}
		
		int shellInputTemperature = shellRecipe.getHeatExchangerInputTemperature();
		int shellOutputTemperature = shellRecipe.getHeatExchangerOutputTemperature();
		
		boolean contraflow = inlet.network.isContraflow();
		int inputEndShellTemperature = contraflow ? shellOutputTemperature : shellInputTemperature;
		int outputEndShellTemperature = contraflow ? shellInputTemperature : shellOutputTemperature;
		
		int inletTemperatureDiff = inlet.inputTemperature - inputEndShellTemperature;
		int outletTemperatureDiff = inlet.outputTemperature - outputEndShellTemperature;
		int sumTempDiff = inletTemperatureDiff + outletTemperatureDiff;
		if (sumTempDiff == 0) {
			return null;
		}
		
		boolean heating = sumTempDiff < 0;
		if (inlet.isHeating != heating) {
			return null;
		}
		
		if ((inletTemperatureDiff > 0 && outletTemperatureDiff < 0) || (inletTemperatureDiff < 0 && outletTemperatureDiff > 0)) {
			return null;
		}
		
		double absMeanTempDiff = HeatExchanger.getAbsMeanTempDiff(inletTemperatureDiff, outletTemperatureDiff);
		
		double tubeFlowDirectionMultiplier = inlet.processor.recipeInfo.recipe.getHeatExchangerFlowDirectionMultiplier(inlet.network.tubeFlow);
		double shellFlowDirectionMultiplier = shellRecipe.getHeatExchangerFlowDirectionMultiplier(inlet.network.shellFlow);
		double sharedTransferRate = absMeanTempDiff * tubeFlowDirectionMultiplier * shellFlowDirectionMultiplier;
		if (sharedTransferRate <= 0D) {
			return null;
		}
		
		double tubeRecipeRateMultiplier = heating ? inlet.network.baseHeatingMultiplier : inlet.network.baseCoolingMultiplier;
		double shellRecipeRateMultiplier = heating ? inlet.network.baseCoolingMultiplier : inlet.network.baseHeatingMultiplier;
		double heatTransferRateMultiplier = inlet.network.baseCoolingMultiplier;
		
		return new HeatExchangerTransferProposal(inlet, sharedTransferRate, tubeRecipeRateMultiplier, shellRecipeRateMultiplier, heatTransferRateMultiplier, absMeanTempDiff, inlet.network.usefulTubeCount);
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
		return new HeatExchangerUpdatePacket(multiblock.controller.getTilePos(), multiblock.isExchangerOn, multiblock.totalNetworkCount, multiblock.activeNetworkCount, multiblock.activeTubeCount, multiblock.activeContactCount, multiblock.tubeInputRateFP, multiblock.shellInputRateFP, multiblock.heatTransferRateFP, multiblock.totalTempDiff);
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
