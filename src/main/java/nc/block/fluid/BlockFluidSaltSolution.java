package nc.block.fluid;

import nc.fluid.FluidSaltSolution;
import net.minecraft.block.material.Material;

public class BlockFluidSaltSolution extends NCBlockFluid {
	
	public BlockFluidSaltSolution(FluidSaltSolution fluid) {
		super(fluid, Material.WATER);
	}
}
