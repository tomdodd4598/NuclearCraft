package nc.block.fluid;

import nc.fluid.FluidSaltSolution;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidSaltSolution extends NCBlockFluid {

	public BlockFluidSaltSolution(Fluid fluid) {
		super(fluid, Material.WATER);
	}
	
	public BlockFluidSaltSolution(FluidSaltSolution fluid) {
		super(fluid, Material.WATER);
	}
}
