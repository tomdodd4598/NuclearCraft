package nc.tile.generator;
 
import nc.NuclearCraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileContinuousBase extends TileGenerator {
	public int power = NuclearCraft.RTGRF;

	public TileContinuousBase(String localName, int energyMax, int slotsNumber) {
		super(localName, energyMax, slotsNumber);
	}
	
	public void updateEntity() {
		super.updateEntity();
		if(!this.worldObj.isRemote) {
		 	energy();
		 	addEnergy();
		 }
		markDirty();
	}
	
	public void energy() {
		 if (this.storage.getEnergyStored() == 0)
		 {
		 	this.storage.receiveEnergy(power, false);
		 }
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		 super.readFromNBT(nbt);
		 if (nbt.hasKey("storage")) {
			 this.storage.readFromNBT(nbt.getCompoundTag("storage"));
		 }
		 power = nbt.getInteger("Power");
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound energyTag = new NBTTagCompound();
		this.storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
		nbt.setInteger("Power", power);
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}
}