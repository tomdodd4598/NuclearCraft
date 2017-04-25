package nc.block.fluid;

import nc.Global;
import nc.NuclearCraft;
import nc.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidGas extends BlockFluid {

	public BlockFluidGas(Fluid fluid, String name) {
		super(fluid, name, Material.AIR);
		setQuantaPerBlock(16);
	}
}
