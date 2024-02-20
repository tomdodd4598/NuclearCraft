package nc.multiblock.rtg;

import static nc.config.NCConfig.rtg_power;

import nc.radiation.RadSources;
import nc.tile.rtg.TileRTG;
import net.minecraft.tileentity.TileEntity;

public enum RTGType {
	
	URANIUM(0, RadSources.URANIUM_238),
	PLUTONIUM(1, RadSources.PLUTONIUM_238),
	AMERICIUM(2, RadSources.AMERICIUM_241),
	CALIFORNIUM(3, RadSources.CALIFORNIUM_250);
	
	private final int id;
	private final double radiation;
	
	RTGType(int id, double radiation) {
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
        return switch (this) {
            case URANIUM -> new TileRTG.Uranium();
            case PLUTONIUM -> new TileRTG.Plutonium();
            case AMERICIUM -> new TileRTG.Americium();
            case CALIFORNIUM -> new TileRTG.Californium();
        };
	}
}
