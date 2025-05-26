package nc.multiblock.hx;

import it.unimi.dsi.fastutil.longs.*;
import nc.Global;
import nc.network.multiblock.*;
import nc.recipe.NCRecipes;
import nc.tile.hx.*;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.MaterialHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.LongSupplier;

public class CondenserLogic extends HeatExchangerLogic {
	
	public CondenserLogic(HeatExchanger exchanger) {
		super(exchanger);
	}
	
	public CondenserLogic(HeatExchangerLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "condenser";
	}
	
	public boolean isCondenser() {
		return true;
	}
	
	// Multiblock Methods
	
	protected void onExchangerFormed() {
		super.onExchangerFormed();
	}
	
	protected void setupExchanger() {
		super.setupExchanger();
	}
	
	public void onExchangerBroken() {
		super.onExchangerBroken();
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
		
		for (int x = multiblock.getMinInteriorX(), maxX = multiblock.getMaxInteriorX(); x <= maxX; ++x) {
			for (int y = multiblock.getMinInteriorY(), maxY = multiblock.getMaxInteriorY(); y <= maxY; ++y) {
				for (int z = multiblock.getMinInteriorX(), maxZ = multiblock.getMaxInteriorZ(); z <= maxZ; ++z) {
					BlockPos pos = new BlockPos(x, y, z);
					if (!tubeMap.containsKey(pos.toLong()) && !MaterialHelper.isEmpty(getWorld().getBlockState(pos).getMaterial())) {
						multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.condenser.blocked_shell", pos);
						return false;
					}
				}
			}
		}
		
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
								network.inletPosLongSet.add(offsetPosLong);
								inlet.network = network;
								continue;
							}
							
							TileHeatExchangerOutlet outlet = outletMap.get(offsetPosLong);
							if (outlet != null) {
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
							
							multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.condenser.dangling_tube", nextPos);
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
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.condenser.invalid_network", BlockPos.fromLong(posLong));
					return false;
				}
			}
		}
		
		for (HeatExchangerTubeNetwork network : multiblock.networks) {
			network.setTubeFlows(tubeMap);
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
	public void onAssimilate(HeatExchanger assimilated) {
		super.onAssimilate(assimilated);
	}
	
	@Override
	public void onAssimilated(HeatExchanger assimilator) {
		super.onAssimilated(assimilator);
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		multiblock.shellRecipe = NCRecipes.heat_exchanger.getRecipeInfoFromInputs(Collections.emptyList(), multiblock.shellTanks.subList(0, 1));
		
		return super.onUpdateServer();
	}
	
	@Override
	public @Nonnull List<Tank> getOutletTanks(HeatExchangerTubeNetwork network) {
		return network == null ? getInletTanks(network) : super.getOutletTanks(network);
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		super.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound data, SyncReason syncReason) {
		super.writeToLogicTag(data, syncReason);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound data, SyncReason syncReason) {
		super.readFromLogicTag(data, syncReason);
	}
	
	// Packets
	
	@Override
	public CondenserUpdatePacket getMultiblockUpdatePacket() {
		return new CondenserUpdatePacket(multiblock.controller.getTilePos(), multiblock.isExchangerOn, multiblock.totalNetworkCount, multiblock.activeNetworkCount, multiblock.activeTubeCount, multiblock.activeContactCount, multiblock.tubeInputRateFP, multiblock.shellInputRateFP, multiblock.heatTransferRateFP, multiblock.heatDissipationRateFP, multiblock.totalTempDiff);
	}
	
	@Override
	public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
	}
	
	public CondenserRenderPacket getRenderPacket() {
		return new CondenserRenderPacket(multiblock.controller.getTilePos(), multiblock.shellTanks);
	}
	
	public void onRenderPacket(HeatExchangerRenderPacket message) {
		super.onRenderPacket(message);
	}
	
	// Multiblock Validators
	
	@Override
	public boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return super.isBlockGoodForInterior(world, pos);
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		super.clearAllMaterial();
	}
}
