package nc.multiblock.rtg;

import nc.radiation.RadSources;
import nc.tile.rtg.TileRTG;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;

import static nc.config.NCConfig.rtg_power;

public enum RTGType implements IStringSerializable {
	
	URANIUM(0, "uranium", RadSources.URANIUM_238),
	PLUTONIUM(1, "plutonium", RadSources.PLUTONIUM_238),
	AMERICIUM(2, "americium", RadSources.AMERICIUM_241),
	CALIFORNIUM(3, "californium", RadSources.CALIFORNIUM_250);
	
	private final int id;
	private final String name;
	private final double radiation;
	
	RTGType(int id, String name, double radiation) {
		this.id = id;
		this.name = name;
		this.radiation = radiation;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
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
