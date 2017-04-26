package nc.block.fluid;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidBase extends Fluid {
	
	public FluidBase(String fluidName, boolean canBeStill) {
		super(fluidName, new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + fluidName + (canBeStill ? "_still" : "")), new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + fluidName + (canBeStill ? "_flow" : "")));
	}
}
