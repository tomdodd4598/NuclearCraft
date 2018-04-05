package nc.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluid extends BlockFluidBase {

	public BlockFluid(Fluid fluid, Material material) {
		super(fluid, material);
	}
	
	public BlockFluid(Fluid fluid) {
		super(fluid, Material.WATER);
	}
}
