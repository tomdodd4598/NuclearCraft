package nc.block.fluid;

import nc.fluid.FluidLiquid;
import net.minecraft.block.material.Material;

public class BlockFluidLiquid extends BlockFluid {

	public BlockFluidLiquid(FluidLiquid fluid) {
		super(fluid, Material.WATER);
	}
}
