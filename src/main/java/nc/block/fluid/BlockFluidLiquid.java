package nc.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidLiquid extends BlockFluid {

	public BlockFluidLiquid(Fluid fluid, String name) {
		super(fluid, name, Material.WATER);
	}
}
