package nc.util;

import java.util.Random;

import nc.config.NCConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHelper {
	
	public static void spawnParticleOnProcessor(IBlockState state, World world, BlockPos pos, Random rand, EnumFacing side, String particleName) {
		if (particleName.equals("") || !NCConfig.processor_particles) return;
		
		double d0 = (double)pos.getX() + 0.5D;
		double d1 = (double)pos.getY() + 0.125D + rand.nextDouble() * 0.75D;
		double d2 = (double)pos.getZ() + 0.5D;
		double d3 = 0.52D;
		double d4 = rand.nextDouble() * 0.6D - 0.3D;
	
		switch (side) {
			case WEST:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				break;
			case EAST:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				break;
			case NORTH:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
				break;
			case SOUTH:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
			default:
				break;
		}
	}
	
	public static void playSoundOnProcessor(World world, BlockPos pos, Random rand, SoundEvent sound) {
		if (sound != null) if (rand.nextDouble() < 0.2D) {
			world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, sound, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
	}
}
