package nc.block.tile;

import static nc.config.NCConfig.wasteland_dimension;

import java.util.Random;

import nc.block.NCBlockPortal;
import nc.init.NCBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockPortalWasteland extends NCBlockPortal {
	
	protected final Random rand = new Random();
	
	public BlockPortalWasteland() {
		super(Material.ROCK, wasteland_dimension, 0);
	}
	
	@Override
	protected ITeleporter getTeleporter(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		return new ITeleporter() {
			
			@Override
			public void placeEntity(World world, Entity entity, float yaw) {
				BlockPos teleportPos = world.getTopSolidOrLiquidBlock(new BlockPos(entity.posX, entity.posY, entity.posZ));
				
				int i = 0;
				while (i < 10) {
					int x = (2 + rand.nextInt(2)) * (rand.nextDouble() > 0.5D ? -1 : 1);
					int y = (2 + rand.nextInt(2)) * (rand.nextDouble() > 0.5D ? -1 : 1);
					int z = (2 + rand.nextInt(2)) * (rand.nextDouble() > 0.5D ? -1 : 1);
					teleportPos = world.getTopSolidOrLiquidBlock(teleportPos.add(x, y, z));
					
					Block block = world.getBlockState(teleportPos).getBlock();
					if (block != NCBlocks.wasteland_portal && !(block instanceof BlockLiquid) && !(block instanceof IFluidBlock)) {
						break;
					}
					
					++i;
				}
				
				if (i >= 10) {
					world.setBlockState(teleportPos, NCBlocks.wasteland_earth.getDefaultState(), 3);
				}
				
				entity.setPositionAndUpdate(teleportPos.getX() + 0.5D, teleportPos.getY() + 1D, teleportPos.getZ() + 0.5D);
			}
		};
	}
}
