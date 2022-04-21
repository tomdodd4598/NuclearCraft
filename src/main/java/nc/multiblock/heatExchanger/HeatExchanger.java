package nc.multiblock.heatExchanger;

import static nc.config.NCConfig.*;

import java.lang.reflect.Constructor;
import java.util.Set;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.*;
import nc.multiblock.cuboidal.CuboidalMultiblock;
import nc.multiblock.heatExchanger.tile.*;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import nc.util.NCMath;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeatExchanger extends CuboidalMultiblock<HeatExchanger, IHeatExchangerPart> implements ILogicMultiblock<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart>, IPacketMultiblock<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket> {
	
	public static final ObjectSet<Class<? extends IHeatExchangerPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	public static final Object2ObjectMap<String, Constructor<? extends HeatExchangerLogic>> LOGIC_MAP = new Object2ObjectOpenHashMap<>();
	
	protected final ObjectSet<IHeatExchangerController<?>> controllers = new ObjectOpenHashSet<>();
	protected final ObjectSet<TileHeatExchangerVent> vents = new ObjectOpenHashSet<>();
	protected final ObjectSet<TileHeatExchangerTube> tubes = new ObjectOpenHashSet<>();
	protected final ObjectSet<TileCondenserTube> condenserTubes = new ObjectOpenHashSet<>();
	
	protected @Nonnull HeatExchangerLogic logic = new HeatExchangerLogic(this);
	
	protected final PartSuperMap<HeatExchanger, IHeatExchangerPart> partSuperMap = new PartSuperMap<>();
	
	protected IHeatExchangerController<?> controller;
	
	public boolean isHeatExchangerOn, computerActivated;
	public double fractionOfTubesActive, efficiency, maxEfficiency;
	
	protected final Set<EntityPlayer> updatePacketListeners;
	
	public HeatExchanger(World world) {
		super(world, HeatExchanger.class, IHeatExchangerPart.class);
		updatePacketListeners = new ObjectOpenHashSet<>();
	}
	
	@Override
	public @Nonnull HeatExchangerLogic getLogic() {
		return logic;
	}
	
	@Override
	public void setLogic(String logicID) {
		if (logicID.equals(logic.getID())) {
			return;
		}
		logic = getNewLogic(LOGIC_MAP.get(logicID));
	}
	
	// Multiblock Part Getters
	
	@Override
	public PartSuperMap<HeatExchanger, IHeatExchangerPart> getPartSuperMap() {
		return partSuperMap;
	}
	
	public ObjectSet<IHeatExchangerController<?>> getControllers() {
		return controllers;
	}
	
	public ObjectSet<TileHeatExchangerVent> getVents() {
		return vents;
	}
	
	public ObjectSet<TileHeatExchangerTube> getTubes() {
		return tubes;
	}
	
	public ObjectSet<TileCondenserTube> getCondenserTubes() {
		return condenserTubes;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return heat_exchanger_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return heat_exchanger_max_size;
	}
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return NCMath.hollowCuboid(Math.max(4, getMinimumInteriorLength() + 2), getMinimumInteriorLength() + 2, getMinimumInteriorLength() + 2);
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IHeatExchangerPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IHeatExchangerPart newPart) {
		if (newPart instanceof IHeatExchangerController) {
			controllers.add((IHeatExchangerController<?>) newPart);
		}
		if (newPart instanceof TileHeatExchangerVent) {
			vents.add((TileHeatExchangerVent) newPart);
		}
		if (newPart instanceof TileHeatExchangerTube) {
			tubes.add((TileHeatExchangerTube) newPart);
		}
		if (newPart instanceof TileCondenserTube) {
			condenserTubes.add((TileCondenserTube) newPart);
		}
	}
	
	@Override
	protected void onBlockRemoved(IHeatExchangerPart oldPart) {
		if (oldPart instanceof IHeatExchangerController) {
			controllers.remove((IHeatExchangerController<?>) oldPart);
		}
		if (oldPart instanceof TileHeatExchangerVent) {
			vents.remove((TileHeatExchangerVent) oldPart);
		}
		if (oldPart instanceof TileHeatExchangerTube) {
			tubes.remove((TileHeatExchangerTube) oldPart);
		}
		if (oldPart instanceof TileCondenserTube) {
			condenserTubes.remove((TileCondenserTube) oldPart);
		}
	}
	
	@Override
	protected void onMachineAssembled() {
		onHeatExchangerFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onHeatExchangerFormed();
	}
	
	protected void onHeatExchangerFormed() {
		for (IHeatExchangerController<?> contr : controllers) {
			controller = contr;
		}
		setIsHeatExchangerOn();
		
		if (!WORLD.isRemote) {
			for (TileHeatExchangerTube tube : tubes) {
				tube.updateFlowDir();
			}
			for (TileCondenserTube condenserTube : condenserTubes) {
				condenserTube.updateAdjacentTemperatures();
			}
			
			updateHeatExchangerStats();
		}
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isHeatExchangerOn = false;
		if (controller != null) {
			controller.setActivity(false);
		}
		fractionOfTubesActive = efficiency = maxEfficiency = 0D;
	}
	
	@Override
	protected boolean isMachineWhole() {
		
		// Only one controller
		
		if (controllers.size() == 0) {
			setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (controllers.size() > 1) {
			setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		return super.isMachineWhole();
	}
	
	@Override
	protected void onAssimilate(HeatExchanger assimilated) {
		logic.onAssimilate(assimilated);
	}
	
	@Override
	protected void onAssimilated(HeatExchanger assimilator) {
		logic.onAssimilated(assimilator);
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		boolean flag = false;
		// setIsHeatExchangerOn();
		updateHeatExchangerStats();
		if (logic.onUpdateServer()) {
			flag = true;
		}
		if (controller != null) {
			sendMultiblockUpdatePacketToListeners();
		}
		return flag;
	}
	
	public void setIsHeatExchangerOn() {
		boolean oldIsHeatExchangerOn = isHeatExchangerOn;
		isHeatExchangerOn = (isRedstonePowered() || computerActivated) && isAssembled();
		if (isHeatExchangerOn != oldIsHeatExchangerOn) {
			if (controller != null) {
				controller.setActivity(isHeatExchangerOn);
				sendMultiblockUpdatePacketToAll();
			}
		}
	}
	
	protected boolean isRedstonePowered() {
		if (controller != null && controller.checkIsRedstonePowered(WORLD, controller.getTilePos())) {
			return true;
		}
		return false;
	}
	
	protected void updateHeatExchangerStats() {
		int numberOfTubes = tubes.size() + condenserTubes.size();
		if (numberOfTubes < 1) {
			fractionOfTubesActive = efficiency = maxEfficiency = 0D;
			return;
		}
		int activeCount = 0, efficiencyCount = 0, maxEfficiencyCount = 0;
		
		for (TileHeatExchangerTube tube : tubes) {
			int[] eff = tube.checkPosition();
			if (eff[0] > 0) {
				++activeCount;
			}
			efficiencyCount += eff[0];
			maxEfficiencyCount += eff[1];
		}
		
		for (TileCondenserTube condenserTube : condenserTubes) {
			int eff = condenserTube.checkPosition();
			if (eff > 0) {
				++activeCount;
			}
			efficiencyCount += eff;
			maxEfficiencyCount += eff;
		}
		
		fractionOfTubesActive = (double) activeCount / numberOfTubes;
		efficiency = activeCount == 0 ? 0D : (double) efficiencyCount / activeCount;
		maxEfficiency = (double) maxEfficiencyCount / numberOfTubes;
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		logic.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		data.setBoolean("isHeatExchangerOn", isHeatExchangerOn);
		data.setBoolean("computerActivated", computerActivated);
		data.setDouble("fractionOfTubesActive", fractionOfTubesActive);
		data.setDouble("efficiency", efficiency);
		data.setDouble("maxEfficiency", maxEfficiency);
		
		writeLogicNBT(data, syncReason);
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		isHeatExchangerOn = data.getBoolean("isHeatExchangerOn");
		computerActivated = data.getBoolean("computerActivated");
		fractionOfTubesActive = data.getDouble("fractionOfTubesActive");
		efficiency = data.getDouble("efficiency");
		maxEfficiency = data.getDouble("maxEfficiency");
		
		readLogicNBT(data, syncReason);
	}
	
	// Packets
	
	@Override
	public Set<EntityPlayer> getMultiblockUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public HeatExchangerUpdatePacket getMultiblockUpdatePacket() {
		return new HeatExchangerUpdatePacket(controller.getTilePos(), isHeatExchangerOn, fractionOfTubesActive, efficiency, maxEfficiency);
	}
	
	@Override
	public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
		isHeatExchangerOn = message.isHeatExchangerOn;
		fractionOfTubesActive = message.fractionOfTubesActive;
		efficiency = message.efficiency;
		maxEfficiency = message.maxEfficiency;
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return true;
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		logic.clearAllMaterial();
		super.clearAllMaterial();
	}
}
