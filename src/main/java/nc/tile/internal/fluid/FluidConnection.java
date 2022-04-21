package nc.tile.internal.fluid;

import java.util.*;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class FluidConnection {
	
	private final @Nonnull List<TankSorption> sorptionList;
	private final @Nonnull List<TankSorption> defaultSorptions;
	
	public FluidConnection(@Nonnull List<TankSorption> sorptionList) {
		this.sorptionList = new ArrayList<>(sorptionList);
		defaultSorptions = new ArrayList<>(sorptionList);
	}
	
	private FluidConnection(@Nonnull FluidConnection connection) {
		sorptionList = new ArrayList<>(connection.sorptionList);
		defaultSorptions = new ArrayList<>(connection.defaultSorptions);
	}
	
	private FluidConnection copy() {
		return new FluidConnection(this);
	}
	
	public static FluidConnection[] cloneArray(@Nonnull FluidConnection[] connections) {
		FluidConnection[] clone = new FluidConnection[6];
		for (int i = 0; i < 6; ++i) {
			clone[i] = connections[i].copy();
		}
		return clone;
	}
	
	public TankSorption getTankSorption(int tankNumber) {
		return sorptionList.get(tankNumber);
	}
	
	public void setTankSorption(int tankNumber, TankSorption sorption) {
		sorptionList.set(tankNumber, sorption);
	}
	
	public TankSorption getDefaultTankSorption(int slot) {
		return defaultSorptions.get(slot);
	}
	
	public boolean canConnect() {
		for (TankSorption sorption : sorptionList) {
			if (sorption.canConnect()) {
				return true;
			}
		}
		return false;
	}
	
	public void toggleTankSorption(int tankNumber, TankSorption.Type type, boolean reverse) {
		setTankSorption(tankNumber, getTankSorption(tankNumber).next(type, reverse));
	}
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, @Nonnull EnumFacing side) {
		NBTTagCompound connectionTag = new NBTTagCompound();
		for (int i = 0; i < sorptionList.size(); ++i) {
			connectionTag.setInteger("sorption" + i, getTankSorption(i).ordinal());
		}
		nbt.setTag("fluidConnection" + side.getIndex(), connectionTag);
		return nbt;
		
	}
	
	public final FluidConnection readFromNBT(NBTTagCompound nbt, @Nonnull EnumFacing side) {
		if (nbt.hasKey("fluidConnection" + side.getIndex())) {
			NBTTagCompound connectionTag = nbt.getCompoundTag("fluidConnection" + side.getIndex());
			for (int i = 0; i < sorptionList.size(); ++i) {
				if (connectionTag.hasKey("sorption" + i)) {
					setTankSorption(i, TankSorption.values()[connectionTag.getInteger("sorption" + i)]);
				}
			}
			
		}
		return this;
	}
}
