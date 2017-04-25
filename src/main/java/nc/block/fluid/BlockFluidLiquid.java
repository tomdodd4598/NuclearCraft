package nc.block.fluid;

import nc.Global;
import nc.NuclearCraft;
import nc.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidLiquid extends BlockFluid {

	public BlockFluidLiquid(Fluid fluid, String name) {
		super(fluid, name, Material.WATER);
	}
}
