package nc.util;

import static nc.config.NCConfig.processor_particles;

import java.util.Random;

import javax.annotation.Nullable;

import nc.block.property.BlockProperties;
import nc.tile.fluid.ITileFluid;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.*;

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
			}
			else if (facing == EnumFacing.SOUTH && s && !n) {
				facing = EnumFacing.NORTH;
			}
			else {
				boolean w = world.getBlockState(pos.west()).isFullBlock();
				boolean e = world.getBlockState(pos.east()).isFullBlock();
				
				if (facing == EnumFacing.WEST && w && !e) {
					facing = EnumFacing.EAST;
				}
				else if (facing == EnumFacing.EAST && e && !w) {
					facing = EnumFacing.WEST;
				}
				else if (property == BlockProperties.FACING_ALL) {
					boolean u = world.getBlockState(pos.up()).isFullBlock();
					boolean d = world.getBlockState(pos.down()).isFullBlock();
					
					if (facing == EnumFacing.UP && u && !d) {
						facing = EnumFacing.DOWN;
					}
					else if (facing == EnumFacing.DOWN && d && !u) {
						facing = EnumFacing.UP;
					}
				}
			}
		}
		return facing;
	}
	
	public static void spawnParticleOnProcessor(IBlockState state, World world, BlockPos pos, Random rand, EnumFacing side, String particleName) {
		if (particleName.equals("") || !processor_particles) {
			return;
		}
		
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
				break;
			default:
				break;
		}
	}
	
	// Accessing machine tanks - taken from net.minecraftforge.fluids.FluidUtil and modified to correctly handle ITileFluids
	public static boolean accessTanks(EntityPlayer player, EnumHand hand, EnumFacing facing, ITileFluid tile) {
		if (player == null || tile == null) {
			return false;
		}
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()) {
			IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (playerInventory != null) {
				IFluidHandlerItem container = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldItem, 1));
				if (container == null) {
					return false;
				}
				for (int i = 0; i < tile.getTanks().size(); ++i) {
					FluidActionResult fluidActionResult = !tile.getTankSorption(facing, i).canDrain() ? FluidActionResult.FAILURE : FluidUtil.tryFillContainerAndStow(heldItem, tile.getTanks().get(i), playerInventory, Integer.MAX_VALUE, player, true);
					if (!fluidActionResult.isSuccess()) {
						if (tile.getTankSorption(facing, i).canFill() && tile.isNextToFill(facing, i, container.drain(Integer.MAX_VALUE, false))) {
							fluidActionResult = FluidUtil.tryEmptyContainerAndStow(heldItem, tile.getTanks().get(i), playerInventory, Integer.MAX_VALUE, player, true);
						}
					}
					if (fluidActionResult.isSuccess()) {
						player.setHeldItem(hand, fluidActionResult.getResult());
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static final AxisAlignedBB REDUCED_BLOCK_AABB = new AxisAlignedBB(0.002D, 0.002D, 0.002D, 0.998D, 0.998D, 0.998D);
	
	// Taken from cofh.core.util.helpers.BlockHelper. Good idea Lemming!
	
	private static final byte[] BOTTOM = {2, 3, 0, 0, 0, 0};
	private static final byte[] TOP = {3, 2, 1, 1, 1, 1};
	private static final byte[] LEFT = {4, 5, 5, 4, 2, 3};
	private static final byte[] RIGHT = {5, 4, 4, 5, 3, 2};
	private static final byte[] FRONT = {0, 1, 2, 3, 4, 5};
	private static final byte[] BACK = {1, 0, 3, 2, 5, 4};
	
	public static EnumFacing bottom(@Nullable EnumFacing facing) {
		return facing == null ? EnumFacing.DOWN : EnumFacing.byIndex(BOTTOM[facing.getIndex()]);
	}
	
	public static EnumFacing top(@Nullable EnumFacing facing) {
		return facing == null ? EnumFacing.UP : EnumFacing.byIndex(TOP[facing.getIndex()]);
	}
	
	public static EnumFacing left(@Nullable EnumFacing facing) {
		return facing == null ? EnumFacing.NORTH : EnumFacing.byIndex(LEFT[facing.getIndex()]);
	}
	
	public static EnumFacing right(@Nullable EnumFacing facing) {
		return facing == null ? EnumFacing.SOUTH : EnumFacing.byIndex(RIGHT[facing.getIndex()]);
	}
	
	public static EnumFacing front(@Nullable EnumFacing facing) {
		return facing == null ? EnumFacing.WEST : EnumFacing.byIndex(FRONT[facing.getIndex()]);
	}
	
	public static EnumFacing back(@Nullable EnumFacing facing) {
		return facing == null ? EnumFacing.EAST : EnumFacing.byIndex(BACK[facing.getIndex()]);
	}
}
