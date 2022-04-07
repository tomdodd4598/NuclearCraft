package nc.multiblock.turbine.tile;

import nc.multiblock.turbine.TurbineDynamoCoilType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileTurbineDynamoCoil extends TileTurbineDynamoPart {
	
	/** Don't use this constructor! */
	public TileTurbineDynamoCoil() {
		super();
	}
	
	public TileTurbineDynamoCoil(String coilName, Double conductivity, String ruleID) {
		super(coilName, conductivity, ruleID);
	}
	
	protected static class Meta extends TileTurbineDynamoCoil {
		
		protected Meta(TurbineDynamoCoilType coilType) {
			super(coilType.getName(), coilType.getConductivity(), coilType.getName() + "_coil");
		}
		
		@Override
		public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
			return oldState != newState;
		}
	}
	
	public static class Magnesium extends Meta {
		
		public Magnesium() {
			super(TurbineDynamoCoilType.MAGNESIUM);
		}
	}
	
	public static class Beryllium extends Meta {
		
		public Beryllium() {
			super(TurbineDynamoCoilType.BERYLLIUM);
		}
	}
	
	public static class Aluminum extends Meta {
		
		public Aluminum() {
			super(TurbineDynamoCoilType.ALUMINUM);
		}
	}
	
	public static class Gold extends Meta {
		
		public Gold() {
			super(TurbineDynamoCoilType.GOLD);
		}
	}
	
	public static class Copper extends Meta {
		
		public Copper() {
			super(TurbineDynamoCoilType.COPPER);
		}
	}
	
	public static class Silver extends Meta {
		
		public Silver() {
			super(TurbineDynamoCoilType.SILVER);
		}
	}
}
