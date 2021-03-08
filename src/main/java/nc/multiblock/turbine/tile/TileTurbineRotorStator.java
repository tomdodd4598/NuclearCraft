package nc.multiblock.turbine.tile;

import java.util.Iterator;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.*;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import nc.multiblock.turbine.block.BlockTurbineRotorStator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TileTurbineRotorStator extends TileTurbinePart implements ITurbineRotorBlade<TileTurbineRotorStator> {
	
	public IRotorStatorType statorType = null;
	protected TurbinePartDir dir = TurbinePartDir.Y;
	
	/** Don't use this constructor! */
	public TileTurbineRotorStator() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	public static class Standard extends TileTurbineRotorStator {
		
		public Standard() {
			super(TurbineRotorStatorType.STATOR);
		}
	}
	
	public TileTurbineRotorStator(IRotorStatorType statorType) {
		this();
		this.statorType = statorType;
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	@Override
	public BlockPos bladePos() {
		return pos;
	}
	
	@Override
	public TurbinePartDir getDir() {
		return dir;
	}
	
	@Override
	public void setDir(TurbinePartDir newDir) {
		dir = newDir;
	}
	
	@Override
	public IRotorBladeType getBladeType() {
		return statorType;
	}
	
	@Override
	public IBlockState getRenderState() {
		if (getBlockType() instanceof BlockTurbineRotorStator) {
			return getBlockType().getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
		}
		return getBlockType().getDefaultState();
	}
	
	@Override
	public void onBearingFailure(Iterator<TileTurbineRotorStator> statorIterator) {
		Turbine turbine = getMultiblock();
		if (turbine != null && turbine.rand.nextDouble() < 0.04D) {
			statorIterator.remove();
			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (statorType != null) {
			nbt.setString("statorName", statorType.getName());
			nbt.setDouble("statorExpansionCoefficient", statorType.getExpansionCoefficient());
		}
		
		nbt.setInteger("bladeDir", dir.ordinal());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (statorType == null && nbt.hasKey("statorName") && nbt.hasKey("statorExpansionCoefficient")) {
			statorType = new IRotorStatorType() {
				
				final String name = nbt.getString("statorName");
				final double expansionCoefficient = nbt.getDouble("statorExpansionCoefficient");
				
				@Override
				public String getName() {
					return name;
				}
				
				@Override
				public double getExpansionCoefficient() {
					return expansionCoefficient;
				}
				
			};
		}
		
		dir = TurbinePartDir.values()[nbt.getInteger("bladeDir")];
	}
}
