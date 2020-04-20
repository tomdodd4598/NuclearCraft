package nc.util;

import java.util.Random;

import nc.block.property.BlockProperties;
import nc.config.NCConfig;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHelper {
	
	public static void setDefaultFacing(World world, BlockPos pos, IBlockState state, PropertyEnum<EnumFacing> property) {
		if (!world.isRemote) {
			world.setBlockState(pos, state.withProperty(property, getDefaultFacing(world, pos, state, property)), 2);
		}
	}
	
	public static EnumFacing getDefaultFacing(World world, BlockPos pos, IBlockState state, PropertyEnum<EnumFacing> property) {
		EnumFacing facing = state.getValue(property);
		if (!world.isRemote) {
			boolean n = world.getBlockState(pos.north()).isFullBlock();
			boolean s = world.getBlockState(pos.south()).isFullBlock();
			
			if (facing == EnumFacing.NORTH && n && !s) {
				facing = EnumFacing.SOUTH;
			} else if (facing == EnumFacing.SOUTH && s && !n) {
				facing = EnumFacing.NORTH;
			}
			else {
				boolean w = world.getBlockState(pos.west()).isFullBlock();
				boolean e = world.getBlockState(pos.east()).isFullBlock();
				
				if (facing == EnumFacing.WEST && w && !e) {
					facing = EnumFacing.EAST;
				} else if (facing == EnumFacing.EAST && e && !w) {
					facing = EnumFacing.WEST;
				}
				else if (property == BlockProperties.FACING_ALL) {
					boolean u = world.getBlockState(pos.up()).isFullBlock();
					boolean d = world.getBlockState(pos.down()).isFullBlock();
					
					if (facing == EnumFacing.UP && u && !d) {
						facing = EnumFacing.DOWN;
					} else if (facing == EnumFacing.DOWN && d && !u) {
						facing = EnumFacing.UP;
					}
				}
			}
		}
		return facing;
	}
	
	public static void spawnParticleOnProcessor(IBlockState state, World world, BlockPos pos, Random rand, EnumFacing side, String particleName) {
		if (particleName.equals("") || !NCConfig.processor_particles) return;
		
		double d0 = pos.getX() + 0.5D;
		double d1 = pos.getY() + 0.125D + rand.nextDouble() * 0.75D;
		double d2 = pos.getZ() + 0.5D;
		double d3 = 0.52D;
		double d4 = rand.nextDouble() * 0.6D - 0.3D;
	
		switch (side) {
			case WEST:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 - d3, d1, d2 + d4, 0D, 0D, 0D);
				break;
			case EAST:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 + d3, d1, d2 + d4, 0D, 0D, 0D);
				break;
			case NORTH:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 + d4, d1, d2 - d3, 0D, 0D, 0D);
				break;
			case SOUTH:
				world.spawnParticle(EnumParticleTypes.getByName(particleName), d0 + d4, d1, d2 + d3, 0D, 0D, 0D);
			default:
				break;
		}
	}
	
	public static void playSoundOnProcessor(World world, BlockPos pos, Random rand, SoundEvent sound) {
		if (sound != null) if (rand.nextDouble() < 0.2D) {
			world.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, sound, SoundCategory.BLOCKS, 1F, 1F, false);
		}
	}
	
	public static final AxisAlignedBB REDUCED_BLOCK_AABB = new AxisAlignedBB(0.002D, 0.002D, 0.002D, 0.998D, 0.998D, 0.998D);
	
	// Taken from cofh.core.util.helpers.BlockHelper. Good idea Lemming!
	
	private static final byte[] BOTTOM = {2, 3, 0, 0, 0, 0};
	private static final byte[] TOP = {3, 2, 1, 1, 1, 1};
	private static final byte[] LEFT = {4, 5, 5, 4, 2, 3};
	private static final byte[] RIGHT = {5, 4, 4, 5, 3, 2};
	private static final byte[] FRONT = {0, 1, 2, 3, 4, 5};
	private static final byte[] BACK = {1, 0, 3, 2, 5, 4};
	
	public static EnumFacing bottom(EnumFacing facing) {
		return EnumFacing.byIndex(BOTTOM[facing.getIndex()]);
	}
	
	public static EnumFacing top(EnumFacing facing) {
		return EnumFacing.byIndex(TOP[facing.getIndex()]);
	}
	
	public static EnumFacing left(EnumFacing facing) {
		return EnumFacing.byIndex(LEFT[facing.getIndex()]);
	}
	
	public static EnumFacing right(EnumFacing facing) {
		return EnumFacing.byIndex(RIGHT[facing.getIndex()]);
	}
	
	public static EnumFacing front(EnumFacing facing) {
		return EnumFacing.byIndex(FRONT[facing.getIndex()]);
	}
	
	public static EnumFacing back(EnumFacing facing) {
		return EnumFacing.byIndex(BACK[facing.getIndex()]);
	}
}
