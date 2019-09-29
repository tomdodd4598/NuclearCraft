package nc.multiblock.turbine;

import nc.config.NCConfig;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public class TurbineRotorBladeUtil {
	
	public enum TurbineRotorBladeType implements IStringSerializable, IRotorBladeType {
		STEEL("steel", NCConfig.turbine_blade_efficiency[0], NCConfig.turbine_blade_expansion[0]),
		EXTREME("extreme", NCConfig.turbine_blade_efficiency[1], NCConfig.turbine_blade_expansion[1]),
		SIC_SIC_CMC("sic_sic_cmc", NCConfig.turbine_blade_efficiency[2], NCConfig.turbine_blade_expansion[2]);
		
		private String name;
		private double efficiency;
		private double expansion;
		
		private TurbineRotorBladeType(String name, double efficiency, double expansion) {
			this.name = name;
			this.efficiency = efficiency;
			this.expansion = expansion;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public double getEfficiency() {
			return efficiency;
		}
		
		public double getExpansionCoefficient() {
			return expansion;
		}
	}
	
	public enum TurbineRotorStatorType implements IRotorBladeType {
		STATOR;
	}
	
	public interface IRotorBladeType {
		
	}
	
	public interface ITurbineRotorBlade {
		
		public BlockPos bladePos();
		
		public TurbinePartDir getDir();
		
		public void setDir(TurbinePartDir newDir);
		
		public IRotorBladeType getBladeType();
		
		public IBlockState getRenderState();
		
		public int getDepth();
		
		public void setDepth(int newDepth);
		
		public float getRenderRotation();
		
		public void setRenderRotation(float newRotation);
	}
	
	public interface IBlockRotorBlade {
		
	}
	
	public enum TurbinePartDir implements IStringSerializable {
		INVISIBLE("invisible"),
		X("x"),
		Y("y"),
		Z("z");
		
		private String name;
		
		private TurbinePartDir(String name) {
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public static TurbinePartDir fromFacingAxis(EnumFacing.Axis axis) {
			switch (axis) {
				case X:
					return X;
				case Y:
					return Y;
				case Z:
					return Z;
				default:
					return INVISIBLE;
			}
		}
	}
	
	public static final PropertyEnum<TurbinePartDir> DIR = PropertyEnum.create("dir", TurbinePartDir.class);
}
