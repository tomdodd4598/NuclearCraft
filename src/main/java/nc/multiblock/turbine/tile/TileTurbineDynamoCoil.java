package nc.multiblock.turbine.tile;

import nc.multiblock.turbine.TurbineDynamoCoilType;

public class TileTurbineDynamoCoil extends TileTurbineDynamoPart {
	
	/** Don't use this constructor! */
	public TileTurbineDynamoCoil() {
		super();
	}
	
	public TileTurbineDynamoCoil(String coilName, Double conductivity, String ruleID) {
		super(coilName, conductivity, ruleID);
	}
	
	private TileTurbineDynamoCoil(TurbineDynamoCoilType coilType) {
		this(coilType.getName(), coilType.getConductivity(), coilType.getName() + "_coil");
	}
	
	public static class Magnesium extends TileTurbineDynamoCoil {
		
		public Magnesium() {
			super(TurbineDynamoCoilType.MAGNESIUM);
		}
	}
	
	public static class Beryllium extends TileTurbineDynamoCoil {
		
		public Beryllium() {
			super(TurbineDynamoCoilType.BERYLLIUM);
		}
	}
	
	public static class Aluminum extends TileTurbineDynamoCoil {
		
		public Aluminum() {
			super(TurbineDynamoCoilType.ALUMINUM);
		}
	}
	
	public static class Gold extends TileTurbineDynamoCoil {
		
		public Gold() {
			super(TurbineDynamoCoilType.GOLD);
		}
	}
	
	public static class Copper extends TileTurbineDynamoCoil {
		
		public Copper() {
			super(TurbineDynamoCoilType.COPPER);
		}
	}
	
	public static class Silver extends TileTurbineDynamoCoil {
		
		public Silver() {
			super(TurbineDynamoCoilType.SILVER);
		}
	}
}
