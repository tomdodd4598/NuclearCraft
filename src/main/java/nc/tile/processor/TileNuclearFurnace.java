package nc.tile.processor;

import nc.block.tile.processor.BlockNuclearFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class TileNuclearFurnace extends TileEntity implements ITickable, ISidedInventory {
	private static final int[] SLOTS_TOP = new int[] {0};
	private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
	private static final int[] SLOTS_SIDES = new int[] {1};
	private ItemStack[] furnaceItemStacks = new ItemStack[3];

	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	public int getSizeInventory() {
		return furnaceItemStacks.length;
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : furnaceItemStacks) {
			if (!(itemstack == null)) {
				return false;
			}
		}
		return true;
	}

	public ItemStack getStackInSlot(int slot) {
		return furnaceItemStacks[slot];
	}

	public ItemStack decrStackSize(int index, int amount) {
		return ItemStackHelper.getAndSplit(furnaceItemStacks, index, amount);
	}

	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(furnaceItemStacks, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = furnaceItemStacks[index];
		boolean flag = !(stack == null) && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		furnaceItemStacks[index] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		if (index == 0 && !flag) {
			//totalCookTime = getCookTime(stack);
			totalCookTime = getCookTime();
			cookTime = 0;
			markDirty();
		}
	}

	public String getName() {
		return "Nuclear Furnace";
	}

	public boolean hasCustomName() {
		return false;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		
		furnaceItemStacks = new ItemStack[getSizeInventory()];
		
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot");
			
			if (j >= 0 && j < furnaceItemStacks.length) {
				furnaceItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
			}
		}
		
		furnaceBurnTime = nbt.getInteger("BurnTime");
		cookTime = nbt.getInteger("CookTime");
		totalCookTime = nbt.getInteger("CookTimeTotal");
		currentItemBurnTime = getItemBurnTime(furnaceItemStacks[1]);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("BurnTime", (short) furnaceBurnTime);
		nbt.setInteger("CookTime", (short) cookTime);
		nbt.setInteger("CookTimeTotal", (short) totalCookTime);
		
		NBTTagList nbttaglist = new NBTTagList();
		
		for (int i = 0; i < furnaceItemStacks.length; ++i) {
			if (furnaceItemStacks[i] != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte)i);
				furnaceItemStacks[i].writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}
		
		nbt.setTag("Items", nbttaglist);
		return nbt;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	public void update() {
		boolean flag = isBurning();
		boolean flag1 = false;

		if (isBurning()) {
			--furnaceBurnTime;
		}

		if (!worldObj.isRemote) {
			if (isBurning() || furnaceItemStacks[1] != null && furnaceItemStacks[0] != null) {
				if (!isBurning() && canSmelt()) {
					furnaceBurnTime = getItemBurnTime(furnaceItemStacks[1]);
					currentItemBurnTime = furnaceBurnTime;

					if (isBurning()) {
						flag1 = true;

						if (furnaceItemStacks[1] != null) {
							furnaceItemStacks[1].stackSize--;

							if (furnaceItemStacks[1].stackSize <= 0) {
								furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
							}
						}
					}
				}

				if (isBurning() && canSmelt()) {
					++cookTime;

					if (cookTime >= totalCookTime) {
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
				cookTime = MathHelper.clamp_int(cookTime - 2, 0, totalCookTime);
			}

			if (flag != isBurning()) {
				flag1 = true;
				BlockNuclearFurnace.setState(isBurning(), worldObj, pos);
			}
		}

		if (flag1) {
			markDirty();
		}
	}

	public int getCookTime() {
		return 10;
	}

	private boolean canSmelt() {
		if ((furnaceItemStacks[0]) == null) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(furnaceItemStacks[0]);

			if (itemstack == null) {
				return false;
			} else {
				if (furnaceItemStacks[2] == null) return true;
				if (!furnaceItemStacks[2].isItemEqual(itemstack)) return false;
				int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
				return result <= getInventoryStackLimit() && result <= furnaceItemStacks[2].getMaxStackSize();
			}
		}
	}

	public void smeltItem() {
		if (canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(furnaceItemStacks[0]);

			if (furnaceItemStacks[2] == null) {
				furnaceItemStacks[2] = itemstack.copy();
			} else if (furnaceItemStacks[2].getItem() == itemstack.getItem()) {
				furnaceItemStacks[2].stackSize += itemstack.stackSize;
			}

			if (furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.SPONGE) && furnaceItemStacks[0].getMetadata() == 1 && furnaceItemStacks[1] != null && furnaceItemStacks[1].getItem() == Items.BUCKET) {
				furnaceItemStacks[1] = new ItemStack(Items.WATER_BUCKET);
			}

			furnaceItemStacks[0].stackSize--;
			
			if (this.furnaceItemStacks[0].stackSize <= 0) {
				this.furnaceItemStacks[0] = null;
			}
		}
	}

	public static int getItemBurnTime(ItemStack stack) {
		if (stack == null) {
			return 0;
		} else {
			for (int i = 0; i < OreDictionary.getOres("blockThorium").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("blockThorium").get(i), stack)) return 3200;
			}
			for (int i = 0; i < OreDictionary.getOres("blockUranium").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("blockUranium").get(i), stack)) return 3200;
			}
			for (int i = 0; i < OreDictionary.getOres("ingotThorium").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("ingotThorium").get(i), stack)) return 320;
			}
			for (int i = 0; i < OreDictionary.getOres("ingotUranium").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("ingotUranium").get(i), stack)) return 320;
			}
			for (int i = 0; i < OreDictionary.getOres("dustThorium").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("dustThorium").get(i), stack)) return 320;
			}
			for (int i = 0; i < OreDictionary.getOres("dustUranium").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("dustUranium").get(i), stack)) return 320;
			}
			for (int i = 0; i < OreDictionary.getOres("ingotThoriumOxide").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("ingotThoriumOxide").get(i), stack)) return 480;
			}
			for (int i = 0; i < OreDictionary.getOres("ingotUraniumOxide").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("ingotUraniumOxide").get(i), stack)) return 480;
			}
			for (int i = 0; i < OreDictionary.getOres("dustThoriumOxide").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("dustThoriumOxide").get(i), stack)) return 480;
			}
			for (int i = 0; i < OreDictionary.getOres("dustUraniumOxide").size(); i++) {
				if (ItemStack.areItemsEqual(OreDictionary.getOres("dustUraniumOxide").get(i), stack)) return 480;
			}
		}
		return 0;
	}

	public static boolean isItemFuel(ItemStack stack) {
		return getItemBurnTime(stack) > 0;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(pos) != this ? false : player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 1) {
			return true;
		} else {
			return isItemFuel(stack);
		}
	}

	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
	}

	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(index, stack);
	}

	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
	}
	
	public int getFieldCount() {
		return 4;
	}

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

	public void clear() {
		for (int i = 0; i < furnaceItemStacks.length; ++i) {
			furnaceItemStacks[i] = null;
		}
	}
	
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getName());
	}

	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return super.getCapability(capability, facing);
	}
}
