package nc.block.fluid;

import java.util.Random;

import nc.fluid.FluidPlasma;
import nc.tile.passive.TilePassive;
import nc.util.MaterialHelper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
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
			if (mat != Material.FIRE && MaterialHelper.isReplaceable(mat) && !mat.isLiquid()) {
				if (worldIn.isSideSolid(offPos.down(), EnumFacing.UP)) {
					worldIn.setBlockState(offPos, Blocks.FIRE.getDefaultState());
					break;
				}
			}
		}
		int free = 0;
		for (EnumFacing side : EnumFacing.values()) {
			TileEntity tile = worldIn.getTileEntity(pos.offset(side));
			if (!(tile instanceof TilePassive.FusionElectromagnet)) {
				free++;
			} else if (rand.nextInt(100) < 1) {
				TilePassive.FusionElectromagnet magnet = (TilePassive.FusionElectromagnet) tile;
				if (!magnet.isRunning) {
					worldIn.createExplosion(null, pos.offset(side).getX(), pos.offset(side).getY(), pos.offset(side).getZ(), 4F, true);
					return;
				}
			}
		}
		if (free >= 6 && isSourceBlock(worldIn, pos)) {
			worldIn.setBlockToAir(pos);
			return;
		}
		super.updateTick(worldIn, pos, state, rand);
	}
}
