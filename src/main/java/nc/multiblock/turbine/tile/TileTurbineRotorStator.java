package nc.multiblock.turbine.tile;

import nc.init.NCBlocks;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.*;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileTurbineRotorStator extends TileTurbinePart implements ITurbineRotorBlade {
	
	public IRotorStatorType statorType = null;
	protected TurbinePartDir dir = TurbinePartDir.Y;
	
	public TileTurbineRotorStator() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	public TileTurbineRotorStator(IRotorStatorType statorType) {
		this();
		this.statorType = statorType;
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		// if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		// if (getWorld().isRemote) return;
		// getWorld().setBlockState(getPos(),
		// getWorld().getBlockState(getPos()), 2);
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
		return TurbineRotorStatorType.STATOR;
	}
	
	@Override
	public IBlockState getRenderState() {
		return NCBlocks.turbine_rotor_stator.getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
	}
	
	@Override
	public void onBearingFailure(Turbine turbine) {
		if (turbine.rand.nextDouble() < 0.04D) {
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
				
				final String name = nbt.getString("bladeName");
				final double expansionCoefficient = nbt.getDouble("bladeExpansionCoefficient");
				
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
