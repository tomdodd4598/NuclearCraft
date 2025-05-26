package nc.multiblock.machine;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.capability.radiation.source.*;
import nc.multiblock.*;
import nc.multiblock.cuboidal.CuboidalMultiblock;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.processor.AbstractProcessorElement;
import nc.tile.machine.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.InventoryStackList;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.UnaryOperator;

public class Machine extends CuboidalMultiblock<Machine, IMachinePart> implements ILogicMultiblock<Machine, MachineLogic, IMachinePart>, IPacketMultiblock<Machine, IMachinePart, MachineUpdatePacket> {
	
	public static final ObjectSet<Class<? extends IMachinePart>> PART_CLASSES = new ObjectOpenHashSet<>();
	public static final Object2ObjectMap<String, UnaryOperator<MachineLogic>> LOGIC_MAP = new Object2ObjectOpenHashMap<>();
	
	protected final PartSuperMap<Machine, IMachinePart> partSuperMap = new PartSuperMap<>();
	
	protected IMachineController<?> controller;
	
	public final IRadiationSource radiation = new RadiationSource(0D);
	
	public @Nonnull EnergyStorage energyStorage = new EnergyStorage(1);
	
	public @Nonnull List<Tank> reservoirTanks = Collections.emptyList();
	
	public BasicRecipeHandler recipeHandler;
	
	public int itemInputSize, itemOutputSize, fluidInputSize, fluidOutputSize;
	
	public @Nonnull InventoryStackList inventoryStacks = new InventoryStackList(new ArrayList<>());
	public @Nonnull List<Tank> tanks = Collections.emptyList();
	
	public @Nonnull List<InventoryConnection[]> inventoryConnections = Collections.emptyList();
	public @Nonnull List<FluidConnection[]> fluidConnections = Collections.emptyList();
	
	public double baseProcessPower, baseProcessRadiation;
	
	public int productionCount;
	
	public double baseSpeedMultiplier, basePowerMultiplier;
	
	public RecipeUnitInfo recipeUnitInfo = RecipeUnitInfo.DEFAULT;
	
	public boolean isMachineOn, fullHalt;
	
	@SideOnly(Side.CLIENT)
	protected Object2ObjectMap<BlockPos, ISound> soundMap;
	public boolean refreshSounds = true;
	
	protected double prevSpeedMultiplier = 0D;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public AbstractProcessorElement processor = new AbstractProcessorElement(false) {
		
		@Override
		public World getWorld() {
			return WORLD;
		}
		
		@Override
		public BasicRecipeHandler getRecipeHandler() {
			return recipeHandler;
		}
		
		@Override
		public boolean setRecipeStats() {
			boolean recipeNonNull = super.setRecipeStats();
			
			if (recipeNonNull) {
				recipeUnitInfo = recipeInfo.getRecipeUnitInfo(1D);
			}
			else {
				if (productionCount > 0) {
					recipeUnitInfo = recipeUnitInfo.withRateMultiplier(recipeUnitInfo.rateMultiplier / (1D + 1D / productionCount));
				}
				else {
					recipeUnitInfo = RecipeUnitInfo.DEFAULT;
				}
			}
			
			return recipeNonNull;
		}
		
		@Override
		public void setRecipeStats(@Nullable BasicRecipe recipe) {
			logic.setRecipeStats(recipe);
		}
		
		@Override
		public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
			return inventoryStacks;
		}
		
		@Override
		public @Nonnull List<Tank> getTanks() {
			return tanks;
		}
		
		@Override
		public boolean getConsumesInputs() {
			return logic.getConsumesInputs();
		}
		
		@Override
		public boolean getLosesProgress() {
			return logic.getLosesProgress();
		}
		
		@Override
		public int getItemInputSize() {
			return itemInputSize;
		}
		
		@Override
		public int getFluidInputSize() {
			return fluidInputSize;
		}
		
		@Override
		public int getItemOutputSize() {
			return itemOutputSize;
		}
		
		@Override
		public int getFluidOutputSize() {
			return fluidOutputSize;
		}
		
		@Override
		public int getItemInputSlot(int index) {
			return index;
		}
		
		@Override
		public int getFluidInputTank(int index) {
			return index;
		}
		
		@Override
		public int getItemOutputSlot(int index) {
			return itemInputSize + index;
		}
		
		@Override
		public int getFluidOutputTank(int index) {
			return fluidInputSize + index;
		}
		
		@Override
		public double getSpeedMultiplier() {
			return logic.getSpeedMultiplier();
		}
		
		@Override
		public boolean isHalted() {
			return logic.isHalted();
		}
		
		@Override
		public boolean readyToProcess() {
			return logic.readyToProcess();
		}
		
		public void process() {
			energyStorage.changeEnergyStored(logic.isGenerator() ? logic.getProcessPower() : -logic.getProcessPower());
			radiation.setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
			super.process();
			productionCount = 0;
		}
		
		@Override
		public void finishProcess() {
			++productionCount;
			super.finishProcess();
		}
		
		@Override
		public boolean onIdle(boolean wasProcessing) {
			radiation.setRadiationLevel(0D);
			return super.onIdle(wasProcessing);
		}
		
		@Override
		public void onResumeProcessingState() {
			sendMultiblockUpdatePacketToListeners();
		}
		
		@Override
		public void onChangeProcessingState() {
			logic.setIsMachineOn(isProcessing);
		}
		
		@Override
		public void refreshRecipe() {
			logic.refreshRecipe();
		}
		
		@Override
		public void refreshActivity() {
			logic.refreshActivity();
		}
	};
	
	// Moved to fix constructor null pointer exceptions
	protected @Nonnull MachineLogic logic = new MachineLogic(this);
	
	public Machine(World world) {
		super(world, Machine.class, IMachinePart.class);
		for (Class<? extends IMachinePart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public @Nonnull MachineLogic getLogic() {
		return logic;
	}
	
	@Override
	public void setLogic(String logicID) {
		if (logicID.equals(logic.getID())) {
			return;
		}
		logic = getNewLogic(LOGIC_MAP.get(logicID));
	}
	
	@Override
	public PartSuperMap<Machine, IMachinePart> getPartSuperMap() {
		return partSuperMap;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return logic.getMinimumInteriorLength();
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return logic.getMaximumInteriorLength();
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMachinePart part, NBTTagCompound data) {
		logic.onAttachedPartWithMultiblockData(part, data);
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMachinePart newPart) {
		onPartAdded(newPart);
		logic.onBlockAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMachinePart oldPart) {
		onPartRemoved(oldPart);
		logic.onBlockRemoved(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		logic.onMachineAssembled();
	}
	
	@Override
	protected void onMachineRestored() {
		logic.onMachineRestored();
	}
	
	@Override
	protected void onMachinePaused() {
		logic.onMachinePaused();
	}
	
	@Override
	protected void onMachineDisassembled() {
		logic.onMachineDisassembled();
	}
	
	@Override
	protected boolean isMachineWhole() {
		return setLogic(this) && super.isMachineWhole() && logic.isMachineWhole();
	}
	
	public boolean setLogic(Machine multiblock) {
		if (getPartMap(IMachineController.class).isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (getPartCount(IMachineController.class) > 1) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		for (IMachineController<?> contr : getParts(IMachineController.class)) {
			controller = contr;
			break;
		}
		
		setLogic(controller.getLogicID());
		
		return true;
	}
	
	@Override
	protected void onAssimilate(Machine assimilated) {
		logic.onAssimilate(assimilated);
	}
	
	@Override
	protected void onAssimilated(Machine assimilator) {
		logic.onAssimilated(assimilator);
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		return logic.onUpdateServer();
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		logic.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		writeLogicNBT(data, syncReason);
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		readLogicNBT(data, syncReason);
	}
	
	// Packets
	
	@Override
	public Set<EntityPlayer> getMultiblockUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return logic.getMultiblockUpdatePacket();
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		isMachineOn = message.isMachineOn;
		logic.onMultiblockUpdatePacket(message);
	}
	
	protected MachineRenderPacket getRenderPacket() {
		return logic.getRenderPacket();
	}
	
	public void onRenderPacket(MachineRenderPacket message) {
		isMachineOn = message.isMachineOn;
		logic.onRenderPacket(message);
	}
	
	public void sendRenderPacketToPlayer(EntityPlayer player) {
		if (WORLD.isRemote) {
			return;
		}
		MachineRenderPacket packet = getRenderPacket();
		if (packet == null) {
			return;
		}
		packet.sendTo(player);
	}
	
	public void sendRenderPacketToAll() {
		if (WORLD.isRemote) {
			return;
		}
		MachineRenderPacket packet = getRenderPacket();
		if (packet == null) {
			return;
		}
		packet.sendToAll();
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return logic.isBlockGoodForInterior(world, pos);
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		logic.clearAllMaterial();
		super.clearAllMaterial();
		
		Collections.fill(inventoryStacks, ItemStack.EMPTY);
		for (Tank tank : tanks) {
			tank.setFluidStored(null);
		}
		
		Collections.fill(processor.consumedStacks, ItemStack.EMPTY);
		for (Tank tank : processor.consumedTanks) {
			tank.setFluidStored(null);
		}
		
		for (Tank reservoirTank : reservoirTanks) {
			reservoirTank.setFluidStored(null);
		}
	}
}
