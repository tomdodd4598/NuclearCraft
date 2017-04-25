package nc.block.fluid;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidBase extends Fluid {
	
	public FluidBase(String fluidName) {
		super(fluidName, new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + fluidName), new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + fluidName));
		FluidRegistry.addBucketForFluid(this);
	}
}
