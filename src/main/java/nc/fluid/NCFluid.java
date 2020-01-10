package nc.fluid;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class NCFluid extends Fluid {
	
	public NCFluid(String fluidName, boolean canBeStill) {
		super(fluidName, stillTextureLocation(fluidName, canBeStill), flowingTextureLocation(fluidName, canBeStill));
		
		if (FluidRegistry.registerFluid(this)) {
			FluidRegistry.addBucketForFluid(this);
		}
	}
	
	public NCFluid(String fluidName, boolean canBeStill, String textureName, Integer color) {
		super(fluidName, stillTextureLocation(textureName, canBeStill), flowingTextureLocation(textureName, canBeStill));
		
		int fixedColor = color.intValue();
		if (((fixedColor >> 24) & 0xFF) == 0) {
			fixedColor |= 0xFF << 24;
		}
		setColor(fixedColor);
		
		if (FluidRegistry.registerFluid(this)) {
			FluidRegistry.addBucketForFluid(this);
		}
	}
	
	public NCFluid(String fluidName, Integer colour) {
		this(fluidName, true, "liquid", colour);
	}
	
	private static ResourceLocation stillTextureLocation(String textureName, boolean canBeStill) {
		return new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + textureName + (canBeStill ? "_still" : ""));
	}
	
	private static ResourceLocation flowingTextureLocation(String textureName, boolean canBeStill) {
		return new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + textureName + (canBeStill ? "_flow" : ""));
	}
}
