package nc.multiblock.turbine.tile;

import nc.init.NCBlocks;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.TurbineRotorBladeUtil;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IRotorBladeType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.ITurbineRotorBlade;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileTurbineRotorBlade extends TileTurbinePartBase implements ITurbineRotorBlade {
	
	public final TurbineRotorBladeType bladeType;
	protected TurbinePartDir dir = TurbinePartDir.Y;
	protected int depth = 0;
	protected float rotation = 0F;
	
	public static class Steel extends TileTurbineRotorBlade {
		
		public Steel() {
			super(TurbineRotorBladeType.STEEL);
		}
		
		@Override
		public IBlockState getRenderState() {
			return NCBlocks.turbine_rotor_blade_steel.getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
		}
	}
	
	public static class Extreme extends TileTurbineRotorBlade {
		
		public Extreme() {
			super(TurbineRotorBladeType.EXTREME);
		}
		
		@Override
		public IBlockState getRenderState() {
			return NCBlocks.turbine_rotor_blade_extreme.getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
		}
	}
	
	public static class SicSicCMC extends TileTurbineRotorBlade {
		
		public SicSicCMC() {
			super(TurbineRotorBladeType.SIC_SIC_CMC);
		}
		
		@Override
		public IBlockState getRenderState() {
			return NCBlocks.turbine_rotor_blade_sic_sic_cmc.getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
		}
	}
	
	private TileTurbineRotorBlade(TurbineRotorBladeType bladeType) {
		super(CuboidalPartPositionType.INTERIOR);
		
		this.bladeType = bladeType;
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
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
		return bladeType;
	}
	
	@Override
	public int getDepth() {
		return depth;
	}
	
	@Override
	public void setDepth(int newDepth) {
		depth = newDepth;
	}
	
	@Override
	public float getRenderRotation() {
		return rotation;
	}
	
	@Override
	public void setRenderRotation(float newRotation) {
		rotation = newRotation;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("bladeDir", dir.ordinal());
		nbt.setInteger("posDepth", depth);
		nbt.setFloat("renderRotation", rotation);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		dir = TurbinePartDir.values()[nbt.getInteger("bladeDir")];
		depth = nbt.getInteger("posDepth");
		rotation = nbt.getFloat("renderRotation");
	}
}
