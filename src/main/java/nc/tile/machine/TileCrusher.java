package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockCrusher;
import nc.crafting.machine.CrusherRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileCrusher extends TileFuelUser {
	
	public TileCrusher() {
		super(16000/NuclearCraft.crusherCrushSpeed, "crusher");
	}
	
	public void updateEntity() {
		boolean flag = burnTime > 0;
		boolean flag1 = false;
		super.updateEntity();
		flag1 = burnTime > 0 || isBurning() && canSmelt() && cookTime == furnaceSpeed;
		if(!worldObj.isRemote) {
			if(flag != burnTime > 0) {
				flag1 = true;
				BlockCrusher.updateCrusherBlockState(burnTime > 0, worldObj, xCoord, yCoord, zCoord);
			}
		}
		if(flag1) {
			markDirty();
		}
	}
	
	public ItemStack getOutput(ItemStack stack) {
		   ItemStack[] outputs = CrusherRecipes.instance().getOutput(stack);
		   if(outputs!=null) {
			   return outputs[0];
		   }
		   return null;
	   }
	
	protected boolean canSmelt() {
		if(slots[0] == null) {
			return false;
		} else {
			ItemStack itemstack = getOutput(slots[0]);
			if(itemstack == null) return false;
			if(slots[2] == null) return true;
			if(!slots[2].isItemEqual(itemstack)) return false;
			int result = slots[2].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= slots[2].getMaxStackSize();
		}
	}
	
	public int getInputSize(ItemStack stack, int slot) {
		   ItemStack[] outputs = CrusherRecipes.instance().getOutput(stack);
		   if(outputs!=null) {
			   return CrusherRecipes.instance().getInputSize(slot, outputs);
		   }
		   return 1;
	}
	
	public void smeltItem() {
		if(canSmelt()) {
			ItemStack itemstack = getOutput(slots[0]);
			if(slots[2] == null) {
				slots[2] = itemstack.copy();
			} else if (slots[2].getItem() == itemstack.getItem()) {
                slots[2].stackSize += itemstack.stackSize;
            }
		}
		slots[0].stackSize -= getInputSize(slots[0], 0);
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
                	return 8000/NuclearCraft.crusherCrushEfficiency;
                }
                if (block.getMaterial() == Material.wood) {
                	return 16000/NuclearCraft.crusherCrushEfficiency;
                }
                if (block == Blocks.coal_block) {
                	return 960000/NuclearCraft.crusherCrushEfficiency;
                }
            }
            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD"))
            	return 8000/NuclearCraft.crusherCrushEfficiency;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD"))
            	return 8000/NuclearCraft.crusherCrushEfficiency;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD"))
            	return 8000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.stick) return 4000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.coal) return 96000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.lava_bucket) return 1200000/NuclearCraft.crusherCrushEfficiency;
            if (item == Item.getItemFromBlock(Blocks.sapling))
            	return 4000/NuclearCraft.crusherCrushEfficiency;
            if (item == Items.blaze_rod) return 144000/NuclearCraft.crusherCrushEfficiency;
            return (GameRegistry.getFuelValue(itemstack)*48)/NuclearCraft.crusherCrushEfficiency;
        }
    }
}