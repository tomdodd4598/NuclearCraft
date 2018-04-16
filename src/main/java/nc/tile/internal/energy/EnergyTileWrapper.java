package nc.tile.internal.energy;

import nc.tile.energy.ITileEnergy;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyTileWrapper implements IEnergyStorage {
	
	public final ITileEnergy tile;
	public final EnumFacing side;
	
	public EnergyTileWrapper(ITileEnergy tile, EnumFacing side) {
		this.tile = tile;
		this.side = side;
	}
	
	@Override
	public int getEnergyStored() {
		return tile.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored() {
		return tile.getMaxEnergyStored();
	}
	
	@Override
	public boolean canReceive() {
		return tile.canReceiveEnergy(side);
	}
	
	@Override
	public boolean canExtract() {
		return tile.canExtractEnergy(side);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return tile.receiveEnergy(maxReceive, side, simulate);
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return tile.extractEnergy(maxExtract, side, simulate);
	}
}
