package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import nc.capability.radiation.source.IRadiationSource;
import nc.fluid.FluidCorium;
import nc.radiation.RadSources;
import nc.radiation.RadiationHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidCorium extends BlockFluidFission {
	
	protected static DamageSource corium_burn = new DamageSource("corium_burn");
	
	public BlockFluidCorium(Fluid fluid) {
		super(fluid);
	}
	
	public BlockFluidCorium(FluidCorium fluid) {
		super(fluid);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(corium_burn, 4.0F);
		super.onEntityCollision(worldIn, pos, state, entityIn);
	}
	
	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		Chunk chunk = world.getChunk(pos);
		if (chunk.isLoaded()) {
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
			if (chunkSource != null) {
				RadiationHelper.addToSourceRadiation(chunkSource, RadSources.CORIUM*getQuantaValue(world, pos));
			}
		}
		super.updateTick(world, pos, state, rand);
	}
}
