package nc.fluid;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidBase extends Fluid {
	
	private final String fluidName;
	
	public FluidBase(String fluidName, boolean canBeStill) {
		super(fluidName, new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + fluidName + (canBeStill ? "_still" : "")), new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + fluidName + (canBeStill ? "_flow" : "")));
		this.fluidName = fluidName;
	}
	
	public FluidBase(String fluidName, boolean canBeStill, String textureName, int colour) {
		super(fluidName, new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + textureName + (canBeStill ? "_still" : "")), new ResourceLocation(Global.MOD_ID + ":blocks/fluids/" + textureName + (canBeStill ? "_flow" : "")));
		
		int fixedColour = colour;
		if(((fixedColour >> 24) & 0xFF) == 0) fixedColour |= 0xFF << 24;
		setColor(fixedColour);
		
		this.fluidName = fluidName;
	}
	
	public String getFluidName() {
		return fluidName;
	}
}
