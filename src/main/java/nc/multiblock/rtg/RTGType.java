package nc.multiblock.rtg;

import static nc.config.NCConfig.rtg_power;

import nc.multiblock.rtg.tile.TileRTG;
import nc.radiation.RadSources;
import net.minecraft.tileentity.TileEntity;

public enum RTGType {
	
	URANIUM(0, RadSources.URANIUM_238),
	PLUTONIUM(1, RadSources.PLUTONIUM_238),
	AMERICIUM(2, RadSources.AMERICIUM_241),
	CALIFORNIUM(3, RadSources.CALIFORNIUM_250);
	
	private int id;
	private double radiation;
	
	private RTGType(int id, double radiation) {
		this.id = id;
		this.radiation = radiation;
	}
	
	public int getPower() {
		return rtg_power[id];
	}
	
	public double getRadiation() {
		return radiation / 8D;
	}
	
	public TileEntity getTile() {
		switch (this) {
			case URANIUM:
				return new TileRTG.Uranium();
			case PLUTONIUM:
				return new TileRTG.Plutonium();
			case AMERICIUM:
				return new TileRTG.Americium();
			case CALIFORNIUM:
				return new TileRTG.Californium();
			default:
				return null;
		}
	}
}
