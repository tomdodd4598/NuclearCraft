package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.*;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.fluid.FluidCorium;
import nc.init.NCBlocks;
import nc.radiation.*;
import nc.util.DamageSources;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockFluidCorium extends BlockFluidFission {
	
	protected static IntSet solidification_dim_set;
	
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
				RadiationHelper.addToSourceRadiation(chunkSource, RadSources.CORIUM * getQuantaValue(world, pos));
			}
		}
		
		super.updateTick(world, pos, state, rand);
		
		if (solidification_dim_set == null) {
			solidification_dim_set = new IntOpenHashSet(NCConfig.corium_solidification);
		}
		
		if (state.getValue(LEVEL) == 0 && solidification_dim_set.contains(world.provider.getDimension()) != NCConfig.corium_solidification_list_type) {
			int count = 0;
			for (EnumFacing side : EnumFacing.VALUES) {
				if (isSourceBlock(world, pos.offset(side))) {
					++count;
					if (count > 3) {
						return;
					}
				}
			}
			
			if (rand.nextInt(2 + 4 * count) == 0) {
				world.setBlockState(pos, NCBlocks.solidified_corium.getDefaultState());
			}
		}
	}
}
