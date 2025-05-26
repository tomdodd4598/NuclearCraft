package nc.tile.turbine;

import it.unimi.dsi.fastutil.objects.*;
import nc.block.turbine.BlockTurbineRotorStator;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.*;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

public class TileTurbineRotorStator extends TileTurbinePart implements ITurbineRotorBlade<TileTurbineRotorStator> {
	
	public static final Object2ObjectMap<String, IRotorStatorType> DYN_STATOR_TYPE_MAP = new Object2ObjectOpenHashMap<>();
	
	public IRotorStatorType statorType = null;
	protected TurbinePartDir dir = TurbinePartDir.Y;
	
	/**
	 * Don't use this constructor!
	 */
	public TileTurbineRotorStator() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	public static class Variant extends TileTurbineRotorStator {
		
		protected Variant(TurbineRotorStatorType statorType) {
			super(statorType);
		}
	}
	
	public static class Standard extends Variant {
		
		public Standard() {
			super(TurbineRotorStatorType.STANDARD);
		}
	}
	
	public TileTurbineRotorStator(IRotorStatorType statorType) {
		this();
		this.statorType = statorType;
	}
	
	@Override
	public void onMachineAssembled(Turbine multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
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
		}
		
		nbt.setInteger("bladeDir", dir.ordinal());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("statorName")) {
			String statorName = nbt.getString("statorName");
			if (DYN_STATOR_TYPE_MAP.containsKey(statorName)) {
				statorType = DYN_STATOR_TYPE_MAP.get(statorName);
			}
		}
		
		dir = TurbinePartDir.values()[nbt.getInteger("bladeDir")];
	}
}
