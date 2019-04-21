package nc.block.fluid;

import nc.fluid.FluidLiquid;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidLiquid extends NCBlockFluid {

	public BlockFluidLiquid(Fluid fluid) {
		super(fluid, Material.WATER);
	}
	
	public BlockFluidLiquid(FluidLiquid fluid) {
		super(fluid, Material.WATER);
	}
}
