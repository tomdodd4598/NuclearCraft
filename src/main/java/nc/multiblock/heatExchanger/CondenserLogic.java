package nc.multiblock.heatExchanger;

public class CondenserLogic extends HeatExchangerLogic {
	
	public CondenserLogic(HeatExchangerLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "condenser";
	}
}
