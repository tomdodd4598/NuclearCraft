package nc.multiblock.fission.moltensalt.tile;

import java.util.HashSet;
import java.util.Set;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockControllerBase;
import nc.multiblock.BeefyTileBase.SyncReason;
import nc.multiblock.cuboidal.CuboidalMultiblockControllerBase;
import nc.multiblock.fission.moltensalt.container.ContainerSaltFissionController;
import nc.multiblock.fission.moltensalt.network.SaltFissionHeatBufferUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import nc.network.PacketHandler;
import nc.tile.internal.HeatBuffer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaltFissionController extends CuboidalMultiblockControllerBase {
	
	private Set<TileSaltFissionController> controllers;
	private Set<TileSaltFissionVent> vents;
	private Set<TileSaltFissionVessel> vessels;
	private Set<TileSaltFissionHeater> heaters;
	
	private Set<EntityPlayer> update_players;
	private TileSaltFissionController controller;
	
	private int redstone_signal = 0;
	private int update_count = 0;
	
	private int rate_multiplier, process_time;
	private int cooling, heating, cell_count, efficiency, heat_multiplier;
	private final HeatBuffer heatBuffer;
	private static final int BASE_MAX_HEAT = 25000;

	public SaltFissionController(World world) {
		super(world);
		
		controllers = new HashSet<TileSaltFissionController>();
		vents = new HashSet<TileSaltFissionVent>();
		vessels = new HashSet<TileSaltFissionVessel>();
		heaters = new HashSet<TileSaltFissionHeater>();
		
		update_players = new HashSet<EntityPlayer>();
		
		heatBuffer = new HeatBuffer(BASE_MAX_HEAT);
	}
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return (int) (Math.pow(NCConfig.salt_fission_min_size + 2, 3) - Math.pow(NCConfig.salt_fission_min_size, 3));
	}

	@Override
	protected int getMaximumXSize() {
		return NCConfig.salt_fission_max_size;
	}

	@Override
	protected int getMaximumZSize() {
		return NCConfig.salt_fission_max_size;
	}

	@Override
	protected int getMaximumYSize() {
		return NCConfig.salt_fission_max_size;
	}
	
	public HeatBuffer getHeatBuffer() {
		return heatBuffer;
	}
	
	public int getRedstoneSignal() {
		return redstone_signal;
	}

	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		this.syncDataFrom(data, SyncReason.FullSync);
	}

	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof TileSaltFissionController) controllers.add((TileSaltFissionController) newPart);
		if (newPart instanceof TileSaltFissionVent) vents.add((TileSaltFissionVent) newPart);
		if (newPart instanceof TileSaltFissionVessel) vessels.add((TileSaltFissionVessel) newPart);
		if (newPart instanceof TileSaltFissionHeater) heaters.add((TileSaltFissionHeater) newPart);
	}

	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileSaltFissionController) controllers.remove(oldPart);
		if (oldPart instanceof TileSaltFissionVent) vents.remove(oldPart);
		if (oldPart instanceof TileSaltFissionVessel) vessels.remove(oldPart);
		if (oldPart instanceof TileSaltFissionHeater) heaters.remove(oldPart);
	}
	
	public Set<TileSaltFissionController> getControllers() {
		return controllers;
	}
	
	public Set<TileSaltFissionVent> getVents() {
		return vents;
	}
	
	public Set<TileSaltFissionVessel> getVessels() {
		return vessels;
	}
	
	public Set<TileSaltFissionHeater> getHeaters() {
		return heaters;
	}
	
	public int getNoControllers() {
		return getControllers().size();
	}
	
	public int getNoVents() {
		return getVents().size();
	}
	
	public int getNoVessels() {
		return getVessels().size();
	}
	
	public int getNoHeaters() {
		return getHeaters().size();
	}

	@Override
	protected void onMachineAssembled() {
		for (TileSaltFissionController contr : controllers) controller = contr;
		calculate();
	}

	@Override
	protected void onMachineRestored() {
		calculate();
	}

	@Override
	protected void onMachinePaused() {
		
	}

	@Override
	protected void onMachineDisassembled() {
		
	}
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return true;
	}
	
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
		if (controllers.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.no_controller");
			return false;
		}
		if (controllers.size() > 1) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.too_many_controllers");
			return false;
		}
		return super.isMachineWhole(validatorCallback);
	}

	@Override
	protected void onAssimilate(MultiblockControllerBase assimilated) {
		SaltFissionController newController = (SaltFissionController) assimilated;
		heatBuffer.mergeHeatBuffers(newController.getHeatBuffer());
	}

	@Override
	protected void onAssimilated(MultiblockControllerBase assimilator) {
		
	}

	@Override
	protected boolean updateServer() {
		// TODO Auto-generated method stub
		
		if (update_count % 10 == 0) {
			SaltFissionHeatBufferUpdatePacket packet = getHeatBufferUpdatePacket();
			for (EntityPlayer player : update_players) {
				PacketHandler.instance.sendTo(packet, (EntityPlayerMP) player);
			}
			
			update_count = 0;
		}
		update_count++;
		
		return true;
	}

	@Override
	protected void updateClient() {
		
	}
	
	public void calculate() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.readFromNBT(data);
	}

	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.writeToNBT(data);
	}
	
	protected void sendIndividualUpdate(EntityPlayer player) {
		if (this.WORLD.isRemote) {
			return;
		}
		PacketHandler.instance.sendTo(getHeatBufferUpdatePacket(), (EntityPlayerMP) player);
	}
	
	protected SaltFissionHeatBufferUpdatePacket getHeatBufferUpdatePacket() {
		return new SaltFissionHeatBufferUpdatePacket(controller, heatBuffer.getHeatStored(), heatBuffer.getHeatCapacity());
	}
	
	public void beginUpdatingPlayer(EntityPlayer playerToUpdate) {
		update_players.add(playerToUpdate);
		sendIndividualUpdate(playerToUpdate);
	}
	
	public void onPacket(long capacity, long stored) {
		getHeatBuffer().setHeatCapacity(capacity);
		getHeatBuffer().setHeatStored(stored);
	}
	
	public void stopUpdatingPlayer(EntityPlayer playerToRemove) {
		update_players.remove(playerToRemove);
	}
	
	public Container getContainer(EntityPlayer player) {
		return new ContainerSaltFissionController(player, controller);
	}
	
	Block getBlock(int x, int y, int z) {
		return this.WORLD.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	@Override
	protected boolean isBlockGoodForBottom(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_casing", x, y, z, getBlock(x, y, z).getLocalizedName());
		return false;
	}

	@Override
	protected boolean isBlockGoodForFrame(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_casing", x, y, z, getBlock(x, y, z).getLocalizedName());
		return false;
	}

	@Override
	protected boolean isBlockGoodForSides(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_casing", x, y, z, getBlock(x, y, z).getLocalizedName());
		return false;
	}

	@Override
	protected boolean isBlockGoodForTop(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_casing", x, y, z, getBlock(x, y, z).getLocalizedName());
		return false;
	}

}
