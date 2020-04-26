package nc.multiblock.turbine.tile;

import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.util.BlockPosHelper;
import net.minecraft.util.EnumFacing;

public abstract class TileTurbineDynamoCoil extends TileTurbineDynamoPart {
	
	public static class Magnesium extends TileTurbineDynamoCoil {
		
		public Magnesium() {
			super(TurbineDynamoCoilType.MAGNESIUM);
		}
		
		@Override
		protected boolean checkDynamoCoilValid() {
			for (EnumFacing dir : BlockPosHelper.getHorizontals(getMultiblock().flowDir)) {
				if (isRotorBearing(pos.offset(dir)) || isCoilConnector(pos.offset(dir))) return true;
			}
			return false;
		}
		
		@Override
		public boolean isSearchRoot() {
			return true;
		}
	}
	
	public static class Beryllium extends TileTurbineDynamoCoil {
		
		public Beryllium() {
			super(TurbineDynamoCoilType.BERYLLIUM);
		}
		
		@Override
		protected boolean checkDynamoCoilValid() {
			for (EnumFacing dir : BlockPosHelper.getHorizontals(getMultiblock().flowDir)) {
				if (isDynamoCoil(pos.offset(dir), "magnesium")) return true;
			}
			return false;
		}
	}
	
	public static class Aluminum extends TileTurbineDynamoCoil {
		
		public Aluminum() {
			super(TurbineDynamoCoilType.ALUMINUM);
		}
		
		@Override
		protected boolean checkDynamoCoilValid() {
			byte magnesium = 0;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(getMultiblock().flowDir)) {
				if (isDynamoCoil(pos.offset(dir), "magnesium")) magnesium++;
				if (magnesium >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Gold extends TileTurbineDynamoCoil {
		
		public Gold() {
			super(TurbineDynamoCoilType.GOLD);
		}
		
		@Override
		protected boolean checkDynamoCoilValid() {
			for (EnumFacing dir : BlockPosHelper.getHorizontals(getMultiblock().flowDir)) {
				if (isDynamoCoil(pos.offset(dir), "aluminum")) return true;
			}
			return false;
		}
	}
	
	public static class Copper extends TileTurbineDynamoCoil {
		
		public Copper() {
			super(TurbineDynamoCoilType.COPPER);
		}
		
		@Override
		protected boolean checkDynamoCoilValid() {
			for (EnumFacing dir : BlockPosHelper.getHorizontals(getMultiblock().flowDir)) {
				if (isDynamoCoil(pos.offset(dir), "beryllium")) return true;
			}
			return false;
		}
	}
	
	public static class Silver extends TileTurbineDynamoCoil {
		
		public Silver() {
			super(TurbineDynamoCoilType.SILVER);
		}
		
		@Override
		protected boolean checkDynamoCoilValid() {
			boolean gold = false, copper = false;
			for (EnumFacing dir : BlockPosHelper.getHorizontals(getMultiblock().flowDir)) {
				if (!gold && isDynamoCoil(pos.offset(dir), "gold")) gold = true;
				if (!copper && isDynamoCoil(pos.offset(dir), "copper")) copper = true;
				if (gold && copper) return true;
			}
			return false;
		}
	}
	
	public TileTurbineDynamoCoil(String coilType, Double conductivity) {
		super(coilType, conductivity);
	}
	
	private TileTurbineDynamoCoil(TurbineDynamoCoilType coilType) {
		this(coilType.getName(), coilType.getConductivity());
	}
}
