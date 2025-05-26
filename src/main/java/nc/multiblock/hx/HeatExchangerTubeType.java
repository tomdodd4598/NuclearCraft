package nc.multiblock.hx;

import nc.enumm.ITileEnum;
import nc.tile.hx.*;
import net.minecraft.util.IStringSerializable;

import static nc.config.NCConfig.*;

public enum HeatExchangerTubeType implements IStringSerializable, ITileEnum<TileHeatExchangerTube.Variant> {
	
	COPPER("copper", heat_exchanger_heat_transfer_coefficient[0], heat_exchanger_heat_retention_mult[0], TileHeatExchangerTube.Copper.class),
	HARD_CARBON("hard_carbon", heat_exchanger_heat_transfer_coefficient[1], heat_exchanger_heat_retention_mult[1], TileHeatExchangerTube.HardCarbon.class),
	THERMOCONDUCTING("thermoconducting", heat_exchanger_heat_transfer_coefficient[2], heat_exchanger_heat_retention_mult[2], TileHeatExchangerTube.Thermoconducting.class);
	
	private final String name;
	private final double heatTransferCoefficient;
	private final double heatRetentionMult;
	private final Class<? extends TileHeatExchangerTube.Variant> tileClass;
	
	HeatExchangerTubeType(String name, double heatTransferCoefficient, double heatRetentionMult, Class<? extends TileHeatExchangerTube.Variant> tileClass) {
		this.name = name;
		this.heatTransferCoefficient = heatTransferCoefficient;
		this.heatRetentionMult = heatRetentionMult;
		this.tileClass = tileClass;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public double getHeatTransferCoefficient() {
		return heatTransferCoefficient;
	}
	
	public double getHeatRetentionMult() {
		return heatRetentionMult;
	}
	
	@Override
	public Class<? extends TileHeatExchangerTube.Variant> getTileClass() {
		return tileClass;
	}
}
