package nc.block.fluid;

import nc.Global;
import nc.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidBase extends BlockFluidClassic {
	
	String name;
	public final Fluid fluid;
	
	public BlockFluidBase(Fluid fluid, String name, Material material) {
		super(fluid, material);
		this.name = name;
		setUnlocalizedName(Global.MOD_ID + "." + name);
		setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		//NuclearCraft.proxy.registerFluidBlockRendering(this, name);
		setCreativeTab(CommonProxy.TAB_FLUIDS);
		this.fluid = fluid;
	}

	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid())
			return false;
		return super.canDisplace(world, pos);
	}

	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid())
			return false;
		return super.displaceIfPossible(world, pos);
	}
	
	public String getName() {
		return name;
	}
}
