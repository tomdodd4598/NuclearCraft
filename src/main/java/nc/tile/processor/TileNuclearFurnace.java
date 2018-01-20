package nc.tile.processor;

import javax.annotation.Nullable;

import nc.Global;
import nc.block.tile.processor.BlockNuclearFurnace;
import nc.tile.dummy.IInterfaceable;
import nc.tile.inventory.ITileInventory;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.oredict.OreDictionary;

public class TileNuclearFurnace extends TileEntity implements ITickable, ISidedInventory, ITileInventory, IInterfaceable {
	private static final int[] SLOTS_TOP = new int[] {0};
	private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
	private static final int[] SLOTS_SIDES = new int[] {1};
	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);

	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	@Override
	public int getSizeInventory() {
		return furnaceItemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : furnaceItemStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return furnaceItemStacks.get(slot);
	}

	@Override
	public ItemStack decrStackSize(int index, int amount) {
		return ItemStackHelper.getAndSplit(furnaceItemStacks, index, amount);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(furnaceItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = furnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		furnaceItemStacks.set(index, stack);

		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			//totalCookTime = getCookTime(stack);
			totalCookTime = getCookTime();
			cookTime = 0;
			markDirty();
		}
	}
	
	@Override
	public NonNullList<ItemStack> getInventoryStacks() {
		return furnaceItemStacks;
	}

	@Override
	public String getName() {
		return Global.MOD_ID + ".container." + "nuclear_furnace";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public static void registerFixesFurnace(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileNuclearFurnace.class, new String[] {"Items"}));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		furnaceItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, furnaceItemStacks);
		furnaceBurnTime = nbt.getInteger("BurnTime");
		cookTime = nbt.getInteger("CookTime");
		totalCookTime = nbt.getInteger("CookTimeTotal");
		currentItemBurnTime = getItemBurnTime(furnaceItemStacks.get(1));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("BurnTime", (short) furnaceBurnTime);
		nbt.setInteger("CookTime", (short) cookTime);
		nbt.setInteger("CookTimeTotal", (short) totalCookTime);
		ItemStackHelper.saveAllItems(nbt, furnaceItemStacks);
		
		return nbt;
	}

	@Override
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

	@Override
	public void update() {
		boolean flag = isBurning();
		boolean flag1 = false;

		if (isBurning()) {
			--furnaceBurnTime;
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
				BlockNuclearFurnace.setState(isBurning(), world, pos);
				world.notifyNeighborsOfStateChange(pos, blockType, true);
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

			if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !(furnaceItemStacks.get(1)).isEmpty() && (furnaceItemStacks.get(1)).getItem() == Items.BUCKET) {
				furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			itemstack.shrink(1);
		}
	}

	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			for (ItemStack ore : OreDictionary.getOres("blockThorium")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 3200;
			}
			for (ItemStack ore : OreDictionary.getOres("blockUranium")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 3200;
			}
			for (ItemStack ore : OreDictionary.getOres("ingotThorium")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 320;
			}
			for (ItemStack ore : OreDictionary.getOres("ingotUranium")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 320;
			}
			for (ItemStack ore : OreDictionary.getOres("dustThorium")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 320;
			}
			for (ItemStack ore : OreDictionary.getOres("dustUranium")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 320;
			}
			for (ItemStack ore : OreDictionary.getOres("ingotThoriumOxide")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 480;
			}
			for (ItemStack ore : OreDictionary.getOres("ingotUraniumOxide")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 480;
			}
			for (ItemStack ore : OreDictionary.getOres("dustThoriumOxide")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 480;
			}
			for (ItemStack ore : OreDictionary.getOres("dustUraniumOxide")) {
				if (ItemStack.areItemsEqual(ore, stack)) return 480;
			}
		}
		return 0;
	}

	public static boolean isItemFuel(ItemStack stack) {
		return getItemBurnTime(stack) > 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) != this ? false : player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

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
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
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
	public void clear() {
		furnaceItemStacks.clear();
	}
	
	@Override
	public ITextComponent getDisplayName() {
		if (getBlockType() != null) return new TextComponentTranslation(getBlockType().getLocalizedName()); else return null;
	}

	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
