package nc.multiblock.turbine.tile;

import nc.multiblock.PlacementRule;
import nc.multiblock.turbine.*;

public abstract class TileTurbineDynamoCoil extends TileTurbineDynamoPart {
	
	public TileTurbineDynamoCoil(String coilName, Double conductivity, PlacementRule<ITurbinePart> placementRule) {
		super(coilName, conductivity, placementRule);
	}
	
	private TileTurbineDynamoCoil(TurbineDynamoCoilType coilType) {
		this(coilType.getName(), coilType.getConductivity(), TurbinePlacement.RULE_MAP.get(coilType.getName() + "_coil"));
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
