package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import nc.capability.radiation.source.IRadiationSource;
import nc.fluid.FluidCorium;
import nc.radiation.RadSources;
import nc.radiation.RadiationHelper;
import nc.util.DamageSources;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockFluidCorium extends BlockFluidFission {
	
	public BlockFluidCorium(FluidCorium fluid) {
		super(fluid);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.CORIUM_BURN, 4F);
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
