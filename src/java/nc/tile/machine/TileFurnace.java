package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockFurnace;

public class TileFurnace extends TileFuelUser {
	
	public TileFurnace() {
		super(8000/NuclearCraft.metalFurnaceCookSpeed, "furnace");
	}
	
	public void updateEntity() {
		boolean flag = burnTime > 0;
		boolean flag1 = false;
		super.updateEntity();
		flag1 = burnTime > 0 || isBurning() && canSmelt() && cookTime == furnaceSpeed;
		if(!worldObj.isRemote) {
			if(flag != burnTime > 0) {
				flag1 = true;
				BlockFurnace.updateFurnaceBlockState(burnTime > 0, worldObj, xCoord, yCoord, zCoord);
			}
		}
		if(flag1) {
			markDirty();
		}
	}
}