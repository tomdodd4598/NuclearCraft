package nc.block.fluid;

import nc.Global;
import nc.NuclearCraft;
import nc.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidSteam extends BlockFluid {
	
	public static DamageSource steam_burn = (new DamageSource("steam_burn")).setDamageBypassesArmor();

	public BlockFluidSteam(Fluid fluid, String name) {
		super(fluid, name, Material.AIR);
		setQuantaPerBlock(16);
	}
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote) {
			entityIn.attackEntityFrom(steam_burn, 3.0F);
			entityIn.setFire(1);
		}
	}
}
