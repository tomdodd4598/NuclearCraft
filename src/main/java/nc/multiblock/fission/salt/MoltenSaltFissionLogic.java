package nc.multiblock.fission.salt;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.FACING_ALL;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.Multiblock;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerMultiblockController;
import nc.multiblock.container.ContainerSaltFissionController;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.FissionReactorLogic;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.network.FissionUpdatePacket;
import nc.multiblock.network.SaltFissionUpdatePacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MoltenSaltFissionLogic extends FissionReactorLogic {
	
	public MoltenSaltFissionLogic(FissionReactorLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "molten_salt";
	}
	
	@Override
	public void onResetStats() {
		
	}
	
	@Override
	public void onReactorFormed() {
		super.onReactorFormed();
	}
	
	@Override
	public boolean isMachineWhole(Multiblock multiblock) {
		if (getPartMap(TileSolidFissionCell.class).size() != 0) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_cells", null);
			return false;
		}
		if (getPartMap(TileSolidFissionSink.class).size() != 0) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_sinks", null);
			return false;
		}
		if (getPartMap(TileFissionVent.class).size() != 0) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_vents", null);
			return false;
		}
		return true;
	}
	
	@Override
	public void distributeFlux(final ObjectSet<IFissionFuelComponent> primedCache, final ObjectSet<IFissionFuelComponent> primedFailureCache) {
		for (TileFissionSource source : getPartMap(TileFissionSource.class).values()) {
			IBlockState state = getWorld().getBlockState(source.getPos());
			EnumFacing facing = source.getPartPosition().getFacing();
			source.refreshIsRedstonePowered(getWorld(), source.getPos());
			getWorld().setBlockState(source.getPos(), state.withProperty(FACING_ALL, facing != null ? facing : state.getValue(FACING_ALL)).withProperty(ACTIVE, source.getIsRedstonePowered()), 3);
			
			if (!source.getIsRedstonePowered()) continue;
			PrimingTargetInfo targetInfo = source.getPrimingTarget();
			if (targetInfo == null) continue;
			IFissionFuelComponent fuelComponent = targetInfo.fuelComponent;
			if (fuelComponent == null || primedFailureCache.contains(fuelComponent)) continue;
			
			fuelComponent.tryPriming(getReactor());
			if (fuelComponent.isPrimed()) {
				primedCache.add(fuelComponent);
			}
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			iterateFluxSearch(primedComponent);
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			primedComponent.refreshIsProcessing(false);
			primedComponent.refreshLocal();
			primedComponent.unprime();
			
			if (!primedComponent.isFunctional()) {
				primedFailureCache.add(primedComponent);
				getReactor().refreshFlag = true;
			}
		}
	}
	
	@Override
	public void refreshClusters() {
		for (TileSaltFissionVessel vessel : getPartMap(TileSaltFissionVessel.class).values()) {
			vessel.refreshModerators();
		}
		
		getReactor().passiveModeratorCache.removeAll(getReactor().activeModeratorCache);
		
		for (TileSaltFissionVessel vessel : getPartMap(TileSaltFissionVessel.class).values()) {
			iterateClusterSearch(vessel);
		}
		
		for (long posLong : getReactor().activeModeratorCache) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(BlockPos.fromLong(posLong).offset(dir).toLong());
				if (component != null) iterateClusterSearch(component);
			}
		}
		
		for (long posLong : getReactor().activeReflectorCache) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(BlockPos.fromLong(posLong).offset(dir).toLong());
				if (component != null) iterateClusterSearch(component);
			}
		}
		
		super.refreshClusters();
	}
	
	@Override
	public void refreshReactorStats() {
		getReactor().resetStats();
		for (FissionCluster cluster : getReactor().getClusterMap().values()) {
			if (cluster.connectedToWall) {
				getReactor().usefulPartCount += cluster.componentCount;
				getReactor().fuelComponentCount += cluster.fuelComponentCount;
				getReactor().cooling += cluster.cooling;
				getReactor().rawHeating += cluster.rawHeating;
				//effectiveHeating += cluster.effectiveHeating;
				getReactor().totalHeatMult += cluster.totalHeatMult;
				getReactor().totalEfficiency += cluster.totalEfficiency;
			}
		}
		
		getReactor().usefulPartCount += getReactor().passiveModeratorCache.size() + getReactor().activeModeratorCache.size() + getReactor().activeReflectorCache.size();
		double usefulPartRatio = (double)getReactor().usefulPartCount/(double)getReactor().getInteriorVolume();
		getReactor().sparsityEfficiencyMult = usefulPartRatio >= NCConfig.fission_sparsity_penalty_params[1] ? 1D : (1D - NCConfig.fission_sparsity_penalty_params[0])*Math.sin(usefulPartRatio*Math.PI/(2D*NCConfig.fission_sparsity_penalty_params[1])) + NCConfig.fission_sparsity_penalty_params[0];
		//effectiveHeating *= sparsityEfficiencyMult;
		getReactor().totalEfficiency *= getReactor().sparsityEfficiencyMult;
		getReactor().meanHeatMult = getReactor().fuelComponentCount == 0 ? 0D : (double)getReactor().totalHeatMult/(double)getReactor().fuelComponentCount;
		getReactor().meanEfficiency = getReactor().fuelComponentCount == 0 ? 0D : getReactor().totalEfficiency/getReactor().fuelComponentCount;
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		if (getReactor().heatBuffer.isFull() && NCConfig.fission_overheat) {
			getReactor().heatBuffer.setHeatStored(0);
			//reservedEffectiveHeat = 0D;
			doMeltdown();
			return true;
		}
		
		return super.onUpdateServer();
	}
	
	public long getNetClusterHeating() {
		return getReactor().rawHeating - getReactor().cooling;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		if (getReactor().isReactorOn) {
			int i = getPartMap(TileSaltFissionVessel.class).size();
			for (TileSaltFissionVessel vessel : getPartMap(TileSaltFissionVessel.class).values()) {
				if (rand.nextDouble() < 1D/i && playFissionSound(vessel)) return;
				if (vessel.isFunctional()) i--;
			}
		}
	}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.writeToLogicTag(logicTag, syncReason);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
	}
	
	// Packets
	
	@Override
	public SaltFissionUpdatePacket getUpdatePacket() {
		return new SaltFissionUpdatePacket(getReactor().controller.getTilePos(), getReactor().isReactorOn, getReactor().heatBuffer, getReactor().clusterCount, getReactor().cooling, getReactor().rawHeating, getReactor().totalHeatMult, getReactor().meanHeatMult, getReactor().fuelComponentCount, getReactor().usefulPartCount, getReactor().totalEfficiency, getReactor().meanEfficiency, getReactor().sparsityEfficiencyMult);
	}
	
	@Override
	public void onPacket(FissionUpdatePacket message) {
		super.onPacket(message);
		if (message instanceof SaltFissionUpdatePacket) {
			//SaltFissionUpdatePacket packet = (SaltFissionUpdatePacket) message;
		}
	}
	
	@Override
	public ContainerMultiblockController<FissionReactor, IFissionController> getContainer(EntityPlayer player) {
		return new ContainerSaltFissionController(player, getReactor().controller);
	}
	
	@Override
	public void clearAllMaterial() {
		super.clearAllMaterial();
	}
}
