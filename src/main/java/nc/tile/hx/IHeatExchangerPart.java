package nc.tile.hx;

import nc.multiblock.cuboidal.ITileCuboidalLogicMultiblockPart;
import nc.multiblock.hx.*;

public interface IHeatExchangerPart extends ITileCuboidalLogicMultiblockPart<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart> {
	
	default void refreshHeatExchangerRecipe() {
		HeatExchangerLogic logic = getLogic();
		if (logic != null) {
			logic.refreshRecipe();
		}
	}
	
	default void refreshHeatExchangerActivity() {
		HeatExchangerLogic logic = getLogic();
		if (logic != null) {
			logic.refreshActivity();
		}
	}
}
