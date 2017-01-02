package nc.tile.machine;

import nc.NuclearCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class TileFuelUser extends TileInventory {

	private static final int[] slots_top = new int[]{0};
	private static final int[] slots_sides = new int[]{1};
	private static final int[] slots_bottom = new int[]{2};
	public double furnaceSpeed = 8000/NuclearCraft.metalFurnaceCookSpeed;
	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;

	public TileFuelUser(double processTime, String name) {
		localizedName = name;
		furnaceSpeed = processTime;
		slots = new ItemStack[3];
	}
	
	public String getInvName() {
		return isInvNameLocalized() ? localizedName : "fuelUser";
	}
	
	public boolean isInvNameLocalized() {
		return localizedName != null && localizedName.length() > 0;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Items", 10);
		slots = new ItemStack[getSizeInventory()];
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");
			if(b >= 0 && b < slots.length) {
				slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		burnTime = (int) nbt.getShort("BurnTime");
		cookTime = (int) nbt.getShort("CookTime");
		currentItemBurnTime = (int) nbt.getShort("CurrentBurnTime");
		
		if(nbt.hasKey("CustomName", 8))
		{
			localizedName = nbt.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("BurnTime", (short) burnTime);
		nbt.setShort("CookTime", (short) cookTime);
		nbt.setShort("CurrentBurnTime", (short) currentItemBurnTime);
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < slots.length; i++) {
			if(slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
				slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}
		nbt.setTag("Items", list);
		if(isInvNameLocalized()) {
			nbt.setString("CustomName", localizedName);
		}
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : 
		entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64;
	}

	public boolean isBurning() {
		return burnTime > 0;
	}
	
	public void updateEntity() {
		if(burnTime > 0) {
			burnTime--;
		}
		if(!worldObj.isRemote) {
			if (burnTime != 0 || slots[1] != null && slots[0] != null) {
				if(burnTime == 0 && canSmelt()) {
					currentItemBurnTime = burnTime = getItemBurnTime(slots[1]);
					if(burnTime > 0) {
						if(slots[1] != null) {
							slots[1].stackSize--;
							if(slots[1].stackSize == 0) {
								slots[1] = slots[1].getItem().getContainerItem(slots[1]);
							}
						}
					}
				}
				if(isBurning() && canSmelt()) {
					cookTime++;
					if(cookTime == furnaceSpeed) {
						cookTime = 0;
						smeltItem();
					}
				} else {
					cookTime = 0;
				}
            }
		}
	}
	
	protected boolean canSmelt() {
		if(slots[0] == null) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(slots[0]);
			if(itemstack == null) return false;
			if(slots[2] == null) return true;
			if(!slots[2].isItemEqual(itemstack)) return false;
			int result = slots[2].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= slots[2].getMaxStackSize();
		}
	}
	
	public void smeltItem() {
		if(canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(slots[0]);
			if(slots[2] == null) {
				slots[2] = itemstack.copy();
			} else if (slots[2].getItem() == itemstack.getItem()) {
                slots[2].stackSize += itemstack.stackSize;
            }
		}
			slots[0].stackSize--;
			if(slots[0].stackSize <= 0) {
				slots[0] = null;
			}
	}
	
	public int getItemBurnTime(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            Item item = itemstack.getItem();
            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
                Block block = Block.getBlockFromItem(item);
                if (block == Blocks.wooden_slab) {
                    return 8000/NuclearCraft.metalFurnaceCookEfficiency;
                }
                if (block.getMaterial() == Material.wood) {
                	return 16000/NuclearCraft.metalFurnaceCookEfficiency;
                }
                if (block == Blocks.coal_block) {
                	return 960000/NuclearCraft.metalFurnaceCookEfficiency;
                }
            }
            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD"))
            	return 16000/NuclearCraft.metalFurnaceCookEfficiency;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD"))
            	return 16000/NuclearCraft.metalFurnaceCookEfficiency;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD"))
            	return 16000/NuclearCraft.metalFurnaceCookEfficiency;
            if (item == Items.stick) return 4000/NuclearCraft.metalFurnaceCookEfficiency;
            if (item == Items.coal) return 96000/NuclearCraft.metalFurnaceCookEfficiency;
            if (item == Items.lava_bucket) return 1200000/NuclearCraft.metalFurnaceCookEfficiency;
            if (item == Item.getItemFromBlock(Blocks.sapling))
            	return 4000/NuclearCraft.metalFurnaceCookEfficiency;
            if (item == Items.blaze_rod) return 144000/NuclearCraft.metalFurnaceCookEfficiency;
            return (GameRegistry.getFuelValue(itemstack)*64)/NuclearCraft.metalFurnaceCookEfficiency;
        }
    }

	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if (slot == 2) return false;
		else if (slot == 1) {
			if (getItemBurnTime(itemstack) > 0) return true;
			else return false;
		} else if (slot == 0) {
			return true;
		}
		else return false;
	}

	public int[] getAccessibleSlotsFromSide(int var1) {
		return var1 == 0 ? slots_bottom : (var1 == 1 ? slots_top : slots_sides);
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return isItemValidForSlot(i, itemstack);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == 2;
	}

	public int getBurnTimeRemainingScaled(int i) {
		if(currentItemBurnTime == 0) {
			currentItemBurnTime = (int) furnaceSpeed;
		}
		return burnTime * i / currentItemBurnTime;
	}
	
	public int getCookProgressScaled(int i) {
		return cookTime * i / (int) furnaceSpeed;
	}	
}