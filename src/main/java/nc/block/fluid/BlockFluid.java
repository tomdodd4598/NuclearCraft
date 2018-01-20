package nc.block.fluid;

import nc.Global;
import nc.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class BlockFluid extends BlockFluidBase {

	public BlockFluid(Fluid fluid, String name, Material material) {
		super(fluid, name, material);
	}
}
