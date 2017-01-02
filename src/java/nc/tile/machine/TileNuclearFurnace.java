package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockNuclearFurnace;
import nc.item.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileNuclearFurnace extends TileFuelUser {
	
	public TileNuclearFurnace() {
		super(Math.ceil(300/NuclearCraft.nuclearFurnaceCookSpeed), "nuclearFurnace");
	}
	
	public void updateEntity() {
		boolean flag = burnTime > 0;
		boolean flag1 = false;
		super.updateEntity();
		flag1 = burnTime > 0 || isBurning() && canSmelt() && cookTime == furnaceSpeed;
		if(!worldObj.isRemote) {
			if(flag != burnTime > 0) {
				flag1 = true;
				BlockNuclearFurnace.updateNuclearFurnaceBlockState(burnTime > 0, worldObj, xCoord, yCoord, zCoord);
			}
		}
		if(flag1) {
			markDirty();
		}
	}
	
	public int getItemBurnTime(ItemStack itemstack) {
		if (itemstack == null) {
			return 0;
		} else {
			Item item = itemstack.getItem();
			if(item == new ItemStack(NCItems.material, 1, 4).getItem() && item.getDamage(itemstack) == 4) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			} else if(item == new ItemStack(NCItems.material, 1, 5).getItem() && item.getDamage(itemstack) == 5) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			} else if(item == new ItemStack(NCItems.material, 1, 19).getItem() && item.getDamage(itemstack) == 19) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			} else if(item == new ItemStack(NCItems.material, 1, 20).getItem() && item.getDamage(itemstack) == 20) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*32)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			}
			
			if(item == new ItemStack(NCItems.material, 1, 53).getItem() && item.getDamage(itemstack) == 53) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*48)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			} else if(item == new ItemStack(NCItems.material, 1, 54).getItem() && item.getDamage(itemstack) == 54) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*48)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			} else if(item == new ItemStack(NCItems.material, 1, 126).getItem() && item.getDamage(itemstack) == 126) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*48)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			} else if(item == new ItemStack(NCItems.material, 1, 127).getItem() && item.getDamage(itemstack) == 127) {
				return (int) Math.ceil(((NuclearCraft.nuclearFurnaceCookSpeed*48)/NuclearCraft.nuclearFurnaceCookEfficiency)*furnaceSpeed);
			}
		return 0;
		}
	}
}