package nc.multiblock.turbine.tile;

import nc.init.NCBlocks;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.*;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileTurbineRotorBlade extends TileTurbinePart implements ITurbineRotorBlade {
	
	public IRotorBladeType bladeType = null;
	protected TurbinePartDir dir = TurbinePartDir.Y;
	
	/** Don't use this constructor! */
	public TileTurbineRotorBlade() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
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
	
	public TileTurbineRotorBlade(IRotorBladeType bladeType) {
		this();
		this.bladeType = bladeType;
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
		return bladeType;
	}
	
	@Override
	public void onBearingFailure(Turbine turbine) {
		if (turbine.rand.nextDouble() < 0.18D) {
			world.removeTileEntity(pos);
			world.createExplosion(null, pos.getX() + turbine.rand.nextDouble() - 0.5D, pos.getY() + turbine.rand.nextDouble() - 0.5D, pos.getZ() + turbine.rand.nextDouble() - 0.5D, 4F, false);
			world.setBlockToAir(pos);
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (bladeType != null) {
			nbt.setString("bladeName", bladeType.getName());
			nbt.setDouble("bladeEfficiency", bladeType.getEfficiency());
			nbt.setDouble("bladeExpansionCoefficient", bladeType.getExpansionCoefficient());
		}
		
		nbt.setInteger("bladeDir", dir.ordinal());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (bladeType == null && nbt.hasKey("bladeName") && nbt.hasKey("bladeEfficiency") && nbt.hasKey("bladeExpansionCoefficient")) {
			bladeType = new IRotorBladeType() {
				
				final String name = nbt.getString("bladeName");
				final double efficiency = nbt.getDouble("bladeEfficiency"), expansionCoefficient = nbt.getDouble("bladeExpansionCoefficient");
				
				@Override
				public String getName() {
					return name;
				}
				
				@Override
				public double getEfficiency() {
					return efficiency;
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
