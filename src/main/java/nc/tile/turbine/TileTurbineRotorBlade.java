package nc.tile.turbine;

import it.unimi.dsi.fastutil.objects.*;
import nc.block.turbine.BlockTurbineRotorBlade;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.*;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

public class TileTurbineRotorBlade extends TileTurbinePart implements ITurbineRotorBlade<TileTurbineRotorBlade> {
	
	public static final Object2ObjectMap<String, IRotorBladeType> DYN_BLADE_TYPE_MAP = new Object2ObjectOpenHashMap<>();
	
	public IRotorBladeType bladeType = null;
	protected TurbinePartDir dir = TurbinePartDir.Y;
	
	/**
	 * Don't use this constructor!
	 */
	public TileTurbineRotorBlade() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	public static class Variant extends TileTurbineRotorBlade {
		
		protected Variant(TurbineRotorBladeType bladeType) {
			super(bladeType);
		}
	}
	
	public static class Steel extends Variant {
		
		public Steel() {
			super(TurbineRotorBladeType.STEEL);
		}
	}
	
	public static class Extreme extends Variant {
		
		public Extreme() {
			super(TurbineRotorBladeType.EXTREME);
		}
	}
	
	public static class SicSicCMC extends Variant {
		
		public SicSicCMC() {
			super(TurbineRotorBladeType.SIC_SIC_CMC);
		}
	}
	
	public TileTurbineRotorBlade(IRotorBladeType bladeType) {
		this();
		this.bladeType = bladeType;
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
	public IBlockState getRenderState() {
		if (getBlockType() instanceof BlockTurbineRotorBlade) {
			return getBlockType().getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
		}
		return getBlockType().getDefaultState();
	}
	
	@Override
	public IRotorBladeType getBladeType() {
		return bladeType;
	}
	
	@Override
	public void onBearingFailure(Iterator<TileTurbineRotorBlade> bladeIterator) {
		Turbine turbine = getMultiblock();
		if (turbine != null && turbine.rand.nextDouble() < 0.18D) {
			bladeIterator.remove();
			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
			world.createExplosion(null, pos.getX() + turbine.rand.nextDouble() - 0.5D, pos.getY() + turbine.rand.nextDouble() - 0.5D, pos.getZ() + turbine.rand.nextDouble() - 0.5D, 4F, false);
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (bladeType != null) {
			nbt.setString("bladeName", bladeType.getName());
		}
		
		nbt.setInteger("bladeDir", dir.ordinal());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("bladeName")) {
			String bladeName = nbt.getString("bladeName");
			if (DYN_BLADE_TYPE_MAP.containsKey(bladeName)) {
				bladeType = DYN_BLADE_TYPE_MAP.get(bladeName);
			}
		}
		
		dir = TurbinePartDir.values()[nbt.getInteger("bladeDir")];
	}
}
