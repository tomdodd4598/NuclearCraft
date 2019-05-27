package nc.block;

import java.util.Random;

import nc.Global;
import nc.config.NCConfig;
import nc.tab.NCTabs;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NCBlockMushroom extends BlockMushroom {
	
	public NCBlockMushroom(String name) {
		super();
		setTranslationKey(Global.MOD_ID + "." + name);
		setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		setCreativeTab(NCTabs.BASE_BLOCK_MATERIALS);
		setLightLevel(1F);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (NCConfig.mushroom_spread_rate <= 0) return;
		
		int spreadTime = 400/NCConfig.mushroom_spread_rate;
		if (spreadTime <= 0) spreadTime = 1;
		
		if (rand.nextInt(spreadTime) == 0) {
			int shroomCheck = 5;

			for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
				if (world.getBlockState(blockpos).getBlock() == this) {
					shroomCheck--;
					if (shroomCheck <= 0) return;
				}
			}

			BlockPos newPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

			for (int k = 0; k < 4; ++k) {
				if (world.isAirBlock(newPos) && canBlockStay(world, newPos, this.getDefaultState())) pos = newPos;
				newPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
			}

			if (world.isAirBlock(newPos) && canBlockStay(world, newPos, this.getDefaultState())) {
				world.setBlockState(newPos, getDefaultState(), 2);
			}
		}
	}
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {}
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState iblockstate = worldIn.getBlockState(pos.down());
			return worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(iblockstate, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
		}
		return false;
	}
}
