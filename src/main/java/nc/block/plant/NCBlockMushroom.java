package nc.block.plant;

import static nc.config.NCConfig.mushroom_spread_rate;

import java.util.Random;

import nc.util.NCMath;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;

public abstract class NCBlockMushroom extends BlockMushroom {
	
	public NCBlockMushroom() {
		super();
		setHardness(0F);
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (mushroom_spread_rate <= 0) {
			return;
		}
		
		if (rand.nextInt(Math.max(1, NCMath.toInt(400D / mushroom_spread_rate))) == 0) {
			
			int count = 0;
			for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
				if (world.getBlockState(blockpos).getBlock() == this) {
					++count;
					if (count > 4) {
						return;
					}
				}
			}
			
			BlockPos spreadPos;
			for (int i = 0; i < 4; ++i) {
				spreadPos = pos.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4));
				if (world.isAirBlock(spreadPos) && canBlockStay(world, spreadPos, getDefaultState())) {
					world.setBlockState(spreadPos, getDefaultState(), 2);
					return;
				}
			}
		}
	}
	
	@Override
	public EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Cave;
	}
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (pos.getY() > 0 && pos.getY() < worldIn.getHeight()) {
			return canGroundSustainPlant(worldIn, pos.down());
		}
		return false;
	}
	
	protected boolean canGroundSustainPlant(World worldIn, BlockPos groundPos) {
		IBlockState ground = worldIn.getBlockState(groundPos);
		return ground.getBlock().canSustainPlant(worldIn.getBlockState(groundPos), worldIn, groundPos, EnumFacing.UP, this);
	}
	
	@Override
	public abstract boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient);
	
	protected abstract HugeMushroomGenerator getHugeMushroomGenerator(World worldIn, Random rand, BlockPos pos);
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		if (canGrow(worldIn, pos, state, false)) {
			worldIn.setBlockToAir(pos);
			if (!getHugeMushroomGenerator(worldIn, rand, pos).generate(worldIn, rand, pos)) {
				worldIn.setBlockState(pos, state, 3);
			}
		}
	}
	
	protected class HugeMushroomGenerator extends WorldGenerator {
		
		protected final Block hugeBlock;
		protected final boolean flatCap;
		
		public HugeMushroomGenerator(Block hugeBlock, boolean flatCap) {
			super(true);
			this.hugeBlock = hugeBlock;
			this.flatCap = flatCap;
		}
		
		@Override
		public boolean generate(World worldIn, Random rand, BlockPos pos) {
			int i = rand.nextInt(3) + 4;
			
			if (rand.nextInt(12) == 0) {
				i *= 2;
			}
			
			boolean flag = true;
			
			if (pos.getY() >= 1 && pos.getY() + i + 1 < worldIn.getHeight()) {
				for (int j = pos.getY(); j <= pos.getY() + 1 + i; ++j) {
					int k = 3;
					
					if (j <= pos.getY() + 3) {
						k = 0;
					}
					
					BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
					
					for (int l = pos.getX() - k; l <= pos.getX() + k && flag; ++l) {
						for (int i1 = pos.getZ() - k; i1 <= pos.getZ() + k && flag; ++i1) {
							if (j >= 0 && j < worldIn.getHeight()) {
								IBlockState state = worldIn.getBlockState(mutablePos.setPos(l, j, i1));
								
								if (!state.getBlock().isAir(state, worldIn, mutablePos) && !state.getBlock().isLeaves(state, worldIn, mutablePos)) {
									flag = false;
								}
							}
							else {
								flag = false;
							}
						}
					}
				}
				
				if (!flag) {
					return false;
				}
				else {
					if (!canGroundSustainPlant(worldIn, pos.down())) {
						return false;
					}
					else {
						int k2 = pos.getY() + i;
						
						if (!flatCap) {
							k2 = pos.getY() + i - 3;
						}
						
						for (int l2 = k2; l2 <= pos.getY() + i; ++l2) {
							int j3 = 1;
							
							if (l2 < pos.getY() + i) {
								++j3;
							}
							
							if (flatCap) {
								j3 = 3;
							}
							
							int k3 = pos.getX() - j3;
							int l3 = pos.getX() + j3;
							int j1 = pos.getZ() - j3;
							int k1 = pos.getZ() + j3;
							
							for (int l1 = k3; l1 <= l3; ++l1) {
								for (int i2 = j1; i2 <= k1; ++i2) {
									int j2 = 5;
									
									if (l1 == k3) {
										--j2;
									}
									else if (l1 == l3) {
										++j2;
									}
									
									if (i2 == j1) {
										j2 -= 3;
									}
									else if (i2 == k1) {
										j2 += 3;
									}
									
									BlockHugeMushroom.EnumType type = BlockHugeMushroom.EnumType.byMetadata(j2);
									
									if (flatCap || l2 < pos.getY() + i) {
										if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1)) {
											continue;
										}
										
										if (l1 == pos.getX() - (j3 - 1) && i2 == j1) {
											type = BlockHugeMushroom.EnumType.NORTH_WEST;
										}
										
										if (l1 == k3 && i2 == pos.getZ() - (j3 - 1)) {
											type = BlockHugeMushroom.EnumType.NORTH_WEST;
										}
										
										if (l1 == pos.getX() + (j3 - 1) && i2 == j1) {
											type = BlockHugeMushroom.EnumType.NORTH_EAST;
										}
										
										if (l1 == l3 && i2 == pos.getZ() - (j3 - 1)) {
											type = BlockHugeMushroom.EnumType.NORTH_EAST;
										}
										
										if (l1 == pos.getX() - (j3 - 1) && i2 == k1) {
											type = BlockHugeMushroom.EnumType.SOUTH_WEST;
										}
										
										if (l1 == k3 && i2 == pos.getZ() + (j3 - 1)) {
											type = BlockHugeMushroom.EnumType.SOUTH_WEST;
										}
										
										if (l1 == pos.getX() + (j3 - 1) && i2 == k1) {
											type = BlockHugeMushroom.EnumType.SOUTH_EAST;
										}
										
										if (l1 == l3 && i2 == pos.getZ() + (j3 - 1)) {
											type = BlockHugeMushroom.EnumType.SOUTH_EAST;
										}
									}
									
									if (type == BlockHugeMushroom.EnumType.CENTER && l2 < pos.getY() + i) {
										type = BlockHugeMushroom.EnumType.ALL_INSIDE;
									}
									
									if (pos.getY() >= pos.getY() + i - 1 || type != BlockHugeMushroom.EnumType.ALL_INSIDE) {
										BlockPos blockpos = new BlockPos(l1, l2, i2);
										IBlockState state = worldIn.getBlockState(blockpos);
										
										if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos)) {
											this.setBlockAndNotifyAdequately(worldIn, blockpos, hugeBlock.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, type));
										}
									}
								}
							}
						}
						
						for (int j = 0; j < i; ++j) {
							IBlockState iblockstate = worldIn.getBlockState(pos.up(j));
							
							if (iblockstate.getBlock().canBeReplacedByLeaves(iblockstate, worldIn, pos.up(j))) {
								this.setBlockAndNotifyAdequately(worldIn, pos.up(j), hugeBlock.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
							}
						}
						
						return true;
					}
				}
			}
			else {
				return false;
			}
		}
	}
}
