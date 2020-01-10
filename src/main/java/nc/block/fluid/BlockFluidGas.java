package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import nc.fluid.FluidGas;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidGas extends NCBlockFluid {
	
	private static final Material GAS = new MaterialLiquid(MapColor.AIR);
	
	public BlockFluidGas(FluidGas fluid) {
		super(fluid, GAS);
	}
	
	@Override
	public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		if (isSourceBlock(worldIn, pos)) {
			worldIn.setBlockToAir(pos);
		}
	}
}
