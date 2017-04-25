package nc.tile.generator;

import nc.energy.EnumStorage.EnergyConnection;
import nc.tile.energy.TileEnergySidedInventory;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEnergyGenerator extends TileEnergySidedInventory {
	
	public boolean isGenerating;
	
	public TileEnergyGenerator(String name, int size, int capacity, int maxTransfer) {
		super(name, size, capacity, maxTransfer, EnergyConnection.OUT);
	}
	
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) isGenerating = isGenerating();
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
		}
	}
	
	public abstract boolean isGenerating();
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isGenerating", isGenerating);
		return nbt;
	}
		
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isGenerating = nbt.getBoolean("isGenerating");
	}
}
