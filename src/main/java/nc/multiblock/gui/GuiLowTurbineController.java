package nc.multiblock.gui;

import nc.Global;
import nc.multiblock.lowTurbine.LowTurbine;
import nc.util.Lang;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiLowTurbineController extends GuiMultiblockController<LowTurbine> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiLowTurbineController(LowTurbine multiblock, Container container) {
		super(multiblock, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "low_turbine_controller" + ".png");
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isLowTurbineOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.container.low_turbine_controller.low_turbine");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
	}
}
