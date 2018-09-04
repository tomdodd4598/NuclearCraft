package nc.block.fluid;

import nc.Global;
import nc.fluid.FluidBase;
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
	
	public BlockFluidBase(Fluid fluid, Material material) {
		super(fluid, material);
		String fluidBlockName = "fluid_" + fluid.getName();
		this.name = fluidBlockName;
		setUnlocalizedName(Global.MOD_ID + "." + fluidBlockName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, fluidBlockName));
		//NuclearCraft.proxy.registerFluidBlockRendering(this, name);
		this.fluid = fluid;
	}
	
	public BlockFluidBase(FluidBase fluid, Material material) {
		super(fluid, material);
		String fluidBlockName = "fluid_" + fluid.getName();
		this.name = fluidBlockName;
		setUnlocalizedName(Global.MOD_ID + "." + fluidBlockName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, fluidBlockName));
		//NuclearCraft.proxy.registerFluidBlockRendering(this, name);
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
