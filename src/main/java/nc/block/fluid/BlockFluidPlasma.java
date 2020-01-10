package nc.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import nc.config.NCConfig;
import nc.fluid.FluidPlasma;
import nc.tile.passive.TilePassive;
import nc.util.DamageSources;
import nc.util.MaterialHelper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidPlasma extends NCBlockFluid {
	
	private static final Material GAS = new MaterialLiquid(MapColor.AIR);
	
	public BlockFluidPlasma(FluidPlasma fluid) {
		super(fluid, GAS);
		setLightLevel(1F);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSources.PLASMA_BURN, 8F);
		entityIn.setFire(10);
	}
	
	@Override
	public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		EnumFacing side = EnumFacing.byIndex(rand.nextInt(6));
		BlockPos offPos = pos.offset(side);
		if (side != EnumFacing.UP && side != EnumFacing.DOWN && NCConfig.fusion_plasma_craziness && rand.nextInt(4) == 0) {
			Material mat = worldIn.getBlockState(offPos).getMaterial();
			if (mat != Material.FIRE && MaterialHelper.isReplaceable(mat) && !mat.isLiquid()) {
				if (worldIn.isSideSolid(offPos.down(), EnumFacing.UP)) {
					worldIn.setBlockState(offPos, Blocks.FIRE.getDefaultState());
					//super.updateTick(worldIn, pos, state, rand);
					return;
				}
			}
		}
		if (rand.nextInt(4) == 0) {
			TileEntity tile = worldIn.getTileEntity(offPos);
			if (tile instanceof TilePassive.FusionElectromagnet) {
				TilePassive.FusionElectromagnet magnet = (TilePassive.FusionElectromagnet) tile;
				if (!magnet.isActive) {
					worldIn.createExplosion(null, offPos.getX(), offPos.getY(), offPos.getZ(), 4F, true);
					return;
				}
			}
		}
		super.updateTick(worldIn, pos, state, rand);
	}
}
