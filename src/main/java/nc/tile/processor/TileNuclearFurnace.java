package nc.tile.processor;

import com.google.common.collect.Lists;
import nc.Global;
import nc.capability.radiation.source.*;
import nc.handler.TileInfoHandler;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.radiation.RadSources;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.datafix.*;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.*;
import java.util.*;

public class TileNuclearFurnace extends TileEntity implements IBasicProcessor<TileNuclearFurnace, ProcessorUpdatePacket> {
	
	private final @Nonnull NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
	
	private final InventoryConnection outputConnection = new InventoryConnection(Lists.newArrayList(ItemSorption.NON, ItemSorption.IN, ItemSorption.OUT));
	private final InventoryConnection inputConnection = new InventoryConnection(Lists.newArrayList(ItemSorption.IN, ItemSorption.NON, ItemSorption.NON));
	
	private @Nonnull InventoryConnection[] inventoryConnections = new InventoryConnection[] {outputConnection, inputConnection, outputConnection, outputConnection, outputConnection, outputConnection};
	
	private final List<ItemOutputSetting> itemOutputSettings = Lists.newArrayList(ItemOutputSetting.DEFAULT, ItemOutputSetting.DEFAULT, ItemOutputSetting.DEFAULT);
	
	private int furnaceBurnTime, currentItemBurnTime, cookTime, totalCookTime;
	
	private final IRadiationSource radiation;
	
	public TileNuclearFurnace() {
		super();
		radiation = new RadiationSource(0D);
	}
	
	@Override
	public TileEntity getTile() {
		return this;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = furnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && nc.util.StackHelper.areItemStackTagsEqual(stack, itemstack);
		
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		
		furnaceItemStacks.set(index, stack);
		
		if (!flag) {
			totalCookTime = getCookTime();
			cookTime = 0;
			markDirty();
		}
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		return furnaceItemStacks;
	}
	
	@Override
	public IRadiationSource getRadiationSource() {
		return radiation;
	}
	
	@Override
	public String getName() {
		return Global.MOD_ID + ".container." + "nuclear_furnace";
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		return inventoryConnections;
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		inventoryConnections = connections;
	}
	
	public static void registerFixesFurnace(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileNuclearFurnace.class, "Items"));
	}
	
	public void readRadiation(NBTTagCompound nbt) {
		if (nbt.hasKey("radiationLevel")) {
			getRadiationSource().setRadiationLevel(nbt.getDouble("radiationLevel"));
		}
	}
	
	public NBTTagCompound writeRadiation(NBTTagCompound nbt) {
		nbt.setDouble("radiationLevel", getRadiationSource().getRadiationLevel());
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		furnaceBurnTime = nbt.getInteger("furnaceBurnTime");
		cookTime = nbt.getInteger("cookTime");
		totalCookTime = nbt.getInteger("totalCookTime");
		currentItemBurnTime = nbt.getInteger("currentItemBurnTime");
		readInventory(nbt);
		readInventoryConnections(nbt);
		readSlotSettings(nbt);
		readRadiation(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("furnaceBurnTime", furnaceBurnTime);
		nbt.setInteger("cookTime", cookTime);
		nbt.setInteger("totalCookTime", totalCookTime);
		nbt.setInteger("currentItemBurnTime", currentItemBurnTime);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		writeSlotSettings(nbt);
		writeRadiation(nbt);
		return nbt;
	}
	
	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isBurningClient() {
		return getField(0) > 0;
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			world.markBlockRangeForRenderUpdate(pos, pos);
			refreshIsRedstonePowered(world, pos);
			markDirty();
			updateComparatorOutputLevel();
		}
	}
	
	@Override
	public void update() {
		boolean flag = isBurning();
		boolean flag1 = false;
		
		if (isBurning()) {
			--furnaceBurnTime;
			getRadiationSource().setRadiationLevel(RadSources.LEU_235_FISSION);
		}
		else {
			getRadiationSource().setRadiationLevel(0D);
		}
		
		if (!world.isRemote) {
			ItemStack itemstack = furnaceItemStacks.get(1);
			
			if (isBurning() || !itemstack.isEmpty() && !(furnaceItemStacks.get(0)).isEmpty()) {
				if (!isBurning() && canSmelt()) {
					furnaceBurnTime = getItemBurnTime(itemstack);
					currentItemBurnTime = furnaceBurnTime;
					
					if (isBurning()) {
						flag1 = true;
						
						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);
							
							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								furnaceItemStacks.set(1, item1);
							}
						}
					}
				}
				
				if (isBurning() && canSmelt()) {
					++cookTime;
					
					if (cookTime == totalCookTime) {
						cookTime = 0;
						//totalCookTime = getCookTime(furnaceItemStacks.get(0));
						totalCookTime = getCookTime();
						smeltItem();
						flag1 = true;
					}
				}
				else {
					cookTime = 0;
				}
			}
			else if (!isBurning() && cookTime > 0) {
				cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);
			}
			
			if (flag != isBurning()) {
				flag1 = true;
				setState(isBurning(), this);
				world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
			}
			
			if (flag1) {
				markDirty();
			}
		}
	}
	
	public int getCookTime() {
		return 10;
	}
	
	private boolean canSmelt() {
		if ((furnaceItemStacks.get(0)).isEmpty()) {
			return false;
		}
		else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(furnaceItemStacks.get(0));
			
			if (itemstack.isEmpty()) {
				return false;
			}
			else {
				ItemStack itemstack1 = furnaceItemStacks.get(2);
				if (itemstack1.isEmpty()) {
					return true;
				}
				if (!itemstack1.isItemEqual(itemstack)) {
					return false;
				}
				int result = itemstack1.getCount() + itemstack.getCount();
				return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
			}
		}
	}
	
	public void smeltItem() {
		if (canSmelt()) {
			ItemStack itemstack = furnaceItemStacks.get(0);
			ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
			ItemStack itemstack2 = furnaceItemStacks.get(2);
			
			if (itemstack2.isEmpty()) {
				furnaceItemStacks.set(2, itemstack1.copy());
			}
			else if (itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}
			if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && StackHelper.getMetadata(itemstack) == 1 && !(furnaceItemStacks.get(1)).isEmpty() && (furnaceItemStacks.get(1)).getItem() == Items.BUCKET) {
				furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
			}
			itemstack.shrink(1);
		}
	}
	
	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		else if (OreDictHelper.isOreMember(stack, "blockUranium")) {
			return 3200;
		}
		else if (OreDictHelper.isOreMember(stack, "ingotUranium")) {
			return 320;
		}
		else if (OreDictHelper.isOreMember(stack, "dustUranium")) {
			return 320;
		}
		else {
			return 0;
		}
	}
	
	public static boolean isItemFuel(ItemStack stack) {
		return getItemBurnTime(stack) > 0;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		}
		else if (index != 1) {
			return true;
		}
		else {
			return isItemFuel(stack);
		}
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return IBasicProcessor.super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		if (side == EnumFacing.DOWN && slot == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}
		return IBasicProcessor.super.canExtractItem(slot, stack, side);
	}
	
	@Override
	public int getFieldCount() {
		return 4;
	}
	
	@Override
	public int getField(int id) {
		return switch (id) {
			case 0 -> furnaceBurnTime;
			case 1 -> currentItemBurnTime;
			case 2 -> cookTime;
			case 3 -> totalCookTime;
			default -> 0;
		};
	}
	
	@Override
	public void setField(int id, int value) {
		switch (id) {
			case 0:
				furnaceBurnTime = value;
				break;
			case 1:
				currentItemBurnTime = value;
				break;
			case 2:
				cookTime = value;
				break;
			case 3:
				totalCookTime = value;
				break;
			default:
				break;
		}
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return getBlockType() == null ? null : new TextComponentTranslation(getBlockType().getLocalizedName());
	}
	
	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return radiation != null;
		}
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return IRadiationSource.CAPABILITY_RADIATION_SOURCE.cast(radiation);
		}
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
		}
		return super.getCapability(capability, side);
	}
	
	// ITile
	
	@Override
	public World getTileWorld() {
		return world;
	}
	
	@Override
	public BlockPos getTilePos() {
		return pos;
	}
	
	@Override
	public Block getTileBlockType() {
		return getBlockType();
	}
	
	@Override
	public int getTileBlockMeta() {
		return getBlockMetadata();
	}
	
	@Override
	public void markTileDirty() {
		markDirty();
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
	
	}
	
	@Override
	public boolean getIsRedstonePowered() {
		return false;
	}
	
	@Override
	public void setIsRedstonePowered(boolean isRedstonePowered) {}
	
	@Override
	public boolean getAlternateComparator() {
		return false;
	}
	
	@Override
	public void setAlternateComparator(boolean alternate) {}
	
	@Override
	public boolean getRedstoneControl() {
		return false;
	}
	
	@Override
	public void setRedstoneControl(boolean redstoneControl) {}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return itemOutputSettings.get(slot);
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {
		itemOutputSettings.set(slot, setting);
	}
	
	// ITileGui
	
	protected final ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TileNuclearFurnace, ProcessorUpdatePacket> info = TileInfoHandler.getProcessorContainerInfo("nuclear_furnace");
	
	@Override
	public ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TileNuclearFurnace, ProcessorUpdatePacket> getContainerInfo() {
		return info;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return null;
	}
	
	@Override
	public ProcessorUpdatePacket getTileUpdatePacket() {
		return null;
	}
	
	@Override
	public void onTileUpdatePacket(ProcessorUpdatePacket message) {}
	
	@Override
	public void sendTileUpdatePacketToPlayer(EntityPlayer player) {}
	
	@Override
	public void sendTileUpdatePacketToAll() {}
	
	// IProcessor
	
	protected final List<Tank> tanks = new ArrayList<>();
	
	@Override
	public List<Tank> getTanks() {
		return tanks;
	}
	
	protected final FluidConnection[] fluidConnections = {};
	
	@Override
	public FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(FluidConnection[] connections) {
	
	}
	
	protected final FluidTileWrapper[] fluidSides = {};
	
	@Override
	public FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	protected final GasTileWrapper gasTileWrapper = new GasTileWrapper(this);
	
	@Override
	public GasTileWrapper getGasWrapper() {
		return gasTileWrapper;
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}
	
	@Override
	public void setInputTanksSeparated(boolean separated) {}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return null;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return null;
	}
	
	@Override
	public double getBaseProcessTime() {
		return 0;
	}
	
	@Override
	public void setBaseProcessTime(double baseProcessTime) {
	
	}
	
	@Override
	public double getBaseProcessPower() {
		return 0;
	}
	
	@Override
	public void setBaseProcessPower(double baseProcessPower) {
	
	}
	
	@Override
	public boolean setRecipeStats() {
		return false;
	}
	
	@Override
	public boolean isProcessing() {
		return false;
	}
	
	@Override
	public boolean isHalted() {
		return false;
	}
	
	@Override
	public boolean readyToProcess() {
		return false;
	}
	
	@Override
	public boolean canProcessInputs() {
		return false;
	}
	
	@Override
	public void process() {
	
	}
	
	@Override
	public void finishProcess() {
	
	}
	
	@Override
	public double getSpeedMultiplier() {
		return 0;
	}
	
	@Override
	public double getPowerMultiplier() {
		return 0;
	}
	
	@Override
	public double getCurrentTime() {
		return 0;
	}
	
	@Override
	public void setCurrentTime(double time) {
	
	}
	
	@Override
	public double getResetTime() {
		return 0;
	}
	
	@Override
	public void setResetTime(double resetTime) {
	
	}
	
	@Override
	public boolean getIsProcessing() {
		return false;
	}
	
	@Override
	public void setIsProcessing(boolean isProcessing) {
	
	}
	
	@Override
	public boolean getCanProcessInputs() {
		return false;
	}
	
	@Override
	public void setCanProcessInputs(boolean canProcessInputs) {
	
	}
	
	@Override
	public boolean getHasConsumed() {
		return false;
	}
	
	@Override
	public void setHasConsumed(boolean hasConsumed) {
	
	}
	
	@Override
	public List<ItemStack> getItemInputs(boolean consumed) {
		return null;
	}
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return null;
	}
	
	@Nonnull
	@Override
	public NonNullList<ItemStack> getConsumedStacks() {
		return InventoryStackList.EMPTY_LIST;
	}
	
	@Nonnull
	@Override
	public List<Tank> getConsumedTanks() {
		return Collections.emptyList();
	}
	
	@Override
	public RecipeInfo<BasicRecipe> getRecipeInfo() {
		return null;
	}
	
	@Override
	public void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo) {
	
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return null;
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return null;
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return null;
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return null;
	}
	
	@Override
	public void refreshAll() {}
	
	@Override
	public void refreshRecipe() {}
	
	@Override
	public void refreshActivity() {}
	
	@Override
	public void refreshActivityOnProduction() {}
}
