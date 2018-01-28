package nc.block.fluid;

import nc.fluid.FluidBase;
import net.minecraft.block.material.Material;

public class BlockFluid extends BlockFluidBase {

	public BlockFluid(FluidBase fluid, Material material) {
		super(fluid, material);
	}
	
	public BlockFluid(FluidBase fluid) {
		super(fluid, Material.WATER);
	}
}
