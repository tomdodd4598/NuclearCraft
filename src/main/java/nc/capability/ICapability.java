package nc.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public interface ICapability<T extends ICapability<T>> {
	
	public NBTTagCompound writeNBT(T instance, EnumFacing side, NBTTagCompound nbt);
	
	public void readNBT(T instance, EnumFacing side, NBTTagCompound nbt);
}
