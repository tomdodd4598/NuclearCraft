package nc.tile.internal.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class FluidConnection {
	
	private @Nonnull List<TankSorption> sorptionList;
	
	public FluidConnection(@Nonnull List<TankSorption> sorptionList) {
		this.sorptionList = new ArrayList<TankSorption>(sorptionList);
	}
	
	public FluidConnection copy() {
		return new FluidConnection(sorptionList);
	}
	
	public TankSorption getTankSorption(int tankNumber) {
		return sorptionList.get(tankNumber);
	}
	
	public void setTankSorption(int tankNumber, TankSorption sorption) {
		sorptionList.set(tankNumber, sorption);
	}
	
	public boolean canConnect() {
		for (TankSorption sorption : sorptionList) {
			if (sorption.canConnect()) {
				return true;
			}
		}
		return false;
	}
	
	public void toggleTankSorption(int tankNumber) {
		setTankSorption(tankNumber, getTankSorption(tankNumber).next());
	}
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, @Nonnull EnumFacing side) {
		NBTTagCompound connectionTag = new NBTTagCompound();
		for (int i = 0; i < sorptionList.size(); i++) {
			connectionTag.setInteger("sorption" + i, getTankSorption(i).ordinal());
		}
		nbt.setTag("fluidConnection" + side.getIndex(), connectionTag);
		return nbt;

	}
	
	public final FluidConnection readFromNBT(NBTTagCompound nbt, @Nonnull EnumFacing side) {
		if (nbt.hasKey("fluidConnection" + side.getIndex())) {
			NBTTagCompound connectionTag = nbt.getCompoundTag("fluidConnection" + side.getIndex());
			for (int i = 0; i < sorptionList.size(); i++) {
				if (connectionTag.hasKey("sorption" + i)) {
					setTankSorption(i, TankSorption.values()[connectionTag.getInteger("sorption" + i)]);
				}
			}
			
		}
		return this;
	}
}
