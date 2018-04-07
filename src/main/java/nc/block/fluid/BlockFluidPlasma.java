package nc.block.fluid;

import java.util.Random;

import nc.block.tile.BlockActivatable;
import nc.fluid.FluidPlasma;
import nc.init.NCBlocks;
import nc.util.MaterialHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidPlasma extends BlockFluid {
	
	private static final Material GAS = new MaterialLiquid(MapColor.AIR);
	
	public static DamageSource plasma_burn = (new DamageSource("plasma_burn")).setDamageBypassesArmor();

	public BlockFluidPlasma(Fluid fluid) {
		super(fluid, GAS);
		setQuantaPerBlock(16);
	}
	
	public BlockFluidPlasma(FluidPlasma fluid) {
		super(fluid, GAS);
		setQuantaPerBlock(16);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(plasma_burn, 8.0F);
		entityIn.setFire(10);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			BlockPos offPos = pos.offset(side);
			Material mat = worldIn.getBlockState(offPos).getMaterial();
			if (MaterialHelper.isReplaceable(mat) && !mat.isLiquid() && mat != Material.FIRE) {
				if (worldIn.isSideSolid(offPos.down(), EnumFacing.UP)) {
					worldIn.setBlockState(offPos, Blocks.FIRE.getDefaultState());
					break;
				}
			}
		}
		int free = 0;
		for (EnumFacing side : EnumFacing.values()) {
			Block offBlock = worldIn.getBlockState(pos.offset(side)).getBlock();
			if (!(offBlock instanceof BlockActivatable || offBlock == NCBlocks.fusion_connector)) {
				free++;
				continue;
			} if (rand.nextInt(200) < 2) {
				if (offBlock == NCBlocks.fusion_electromagnet_idle || offBlock == NCBlocks.fusion_electromagnet_transparent_idle) {
					worldIn.createExplosion(null, pos.offset(side).getX(), pos.offset(side).getY(), pos.offset(side).getZ(), 4F, true);
					return;
				} else if (offBlock == NCBlocks.fusion_core || offBlock == NCBlocks.fusion_dummy_side || offBlock == NCBlocks.fusion_dummy_top) {
					worldIn.createExplosion(null, pos.offset(side).getX(), pos.offset(side).getY(), pos.offset(side).getZ(), 7F, true);
					return;
				}
			}
		}
		if (free == 6 && isSourceBlock(worldIn, pos)) {
			worldIn.setBlockToAir(pos);
			return;
		}
		super.updateTick(worldIn, pos, state, rand);
	}
}
