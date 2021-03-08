package nc.multiblock.heatExchanger;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import nc.Global;
import nc.multiblock.heatExchanger.tile.*;

public class CondenserLogic extends HeatExchangerLogic {
	
	public CondenserLogic(HeatExchangerLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "condenser";
	}
	
	@Override
	public boolean isMachineWhole() {
		return !containsBlacklistedPart();
	}
	
	public static final List<Pair<Class<? extends IHeatExchangerPart>, String>> CONDENSER_PART_BLACKLIST = Lists.newArrayList(Pair.of(TileHeatExchangerTube.class, Global.MOD_ID + ".multiblock_validation.heat_exchanger.prohibit_exchanger_tubes"));
	
	@Override
	public List<Pair<Class<? extends IHeatExchangerPart>, String>> getPartBlacklist() {
		return CONDENSER_PART_BLACKLIST;
	}
}
