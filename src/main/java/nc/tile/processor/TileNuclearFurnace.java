package nc.tile.processor;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.Global;
import nc.block.tile.IActivatable;
import nc.capability.radiation.source.IRadiationSource;
import nc.capability.radiation.source.RadiationSource;
import nc.network.tile.TileUpdatePacket;
import nc.radiation.RadSources;
import nc.tile.ITileGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.InventoryTileWrapper;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.ItemStackHelper;
import nc.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileNuclearFurnace extends TileEntity implements ITickable, ITileInventory, ITileGui, IInterfaceable, IBufferable {
	
	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
	
	private final InventoryConnection outputConnection = new InventoryConnection(Lists.newArrayList(ItemSorption.NON, ItemSorption.IN, ItemSorption.OUT));
	private final InventoryConnection inputConnection = new InventoryConnection(Lists.newArrayList(ItemSorption.IN, ItemSorption.NON, ItemSorption.NON));
	
	private InventoryConnection[] inventoryConnections = new InventoryConnection[] {outputConnection, inputConnection, outputConnection, outputConnection, outputConnection, outputConnection};
	
	private final InventoryTileWrapper invWrapper = new InventoryTileWrapper(this);
	
	private final List<ItemOutputSetting> itemOutputSettings = Lists.newArrayList(ItemOutputSetting.DEFAULT, ItemOutputSetting.DEFAULT, ItemOutputSetting.DEFAULT);
	
	private int furnaceBurnTime, currentItemBurnTime, cookTime, totalCookTime;
	
	private IRadiationSource radiation;
	
	public TileNuclearFurnace() {
		super();
		radiation = new RadiationSource(0D);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = furnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && nc.util.ItemStackHelper.areItemStackTagsEqual(stack, itemstack);
		
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		
		furnaceItemStacks.set(index, stack);
		
		if (!flag) {
			//totalCookTime = getCookTime(stack);
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
	
	@Override
	public @Nonnull InventoryTileWrapper getInventory() {
		return invWrapper;
	}
	
	public static void registerFixesFurnace(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileNuclearFurnace.class, new String[] {"Items"}));
	}
	
	public void readRadiation(NBTTagCompound nbt) {
		if (nbt.hasKey("radiationLevel")) getRadiationSource().setRadiationLevel(nbt.getDouble("radiationLevel"));
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
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}
	
	@Override
	public void update() {
		boolean flag = isBurning();
		boolean flag1 = false;
		
		if (isBurning()) {
			--furnaceBurnTime;
			getRadiationSource().setRadiationLevel(RadSources.LEU_235_FISSION);
		} else {
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
				} else {
					cookTime = 0;
				}
			} else if (!isBurning() && cookTime > 0) {
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
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(furnaceItemStacks.get(0));

			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = furnaceItemStacks.get(2);
				if (itemstack1.isEmpty()) return true;
				if (!itemstack1.isItemEqual(itemstack)) return false;
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
			} else if (itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}
			if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && ItemStackHelper.getMetadata(itemstack) == 1 && !(furnaceItemStacks.get(1)).isEmpty() && (furnaceItemStacks.get(1)).getItem() == Items.BUCKET) {
				furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
			}
			itemstack.shrink(1);
		}
	}
	
	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) return 0;
		else if (OreDictHelper.isOreMember(stack, "blockUranium")) return 3200;
		else if (OreDictHelper.isOreMember(stack, "ingotUranium")) return 320;
		else if (OreDictHelper.isOreMember(stack, "dustUranium")) return 320;
		else return 0;
	}
	
	public static boolean isItemFuel(ItemStack stack) {
		return getItemBurnTime(stack) > 0;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 1) {
			return true;
		} else {
			return isItemFuel(stack);
		}
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return ITileInventory.super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		if (side == EnumFacing.DOWN && slot == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}
		return ITileInventory.super.canExtractItem(slot, stack, side);
	}
	
	@Override
	public int getFieldCount() {
		return 4;
	}
	
	@Override
	public int getField(int id) {
		switch (id) {
			case 0:
				return furnaceBurnTime;
			case 1:
				return currentItemBurnTime;
			case 2:
				return cookTime;
			case 3:
				return totalCookTime;
			default:
				return 0;
		}
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
		}
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return getBlockType() == null ? null : new TextComponentTranslation(getBlockType().getLocalizedName());
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
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
			return (T) radiation;
		}
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) getItemHandlerCapability(side);
		}
		return super.getCapability(capability, side);
	}
	
	//ITile
	
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
	public void setState(boolean isActive, TileEntity tile) {
		if (getBlockType() instanceof IActivatable) {
			((IActivatable)getBlockType()).setState(isActive, tile);
		}
	}
	
	@Override
	public void markTileDirty() {
		markDirty();
	}
	
	@Override
	public void markDirtyAndNotify() {
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		
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
	
	@Override
	public int getGuiID() {
		return 0;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return null;
	}
	
	@Override
	public TileUpdatePacket getGuiUpdatePacket() {
		return null;
	}
	
	@Override
	public void onGuiPacket(TileUpdatePacket message) {}
	
	@Override
	public void beginUpdatingPlayer(EntityPlayer playerToUpdate) {}
	
	@Override
	public void stopUpdatingPlayer(EntityPlayer playerToRemove) {}
	
	@Override
	public void sendUpdateToListeningPlayers() {}
	
	@Override
	public void sendIndividualUpdate(EntityPlayer player) {}
	
	@Override
	public void sendUpdateToAllPlayers() {}
}
