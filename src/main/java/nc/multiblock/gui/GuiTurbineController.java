package nc.multiblock.gui;

import nc.Global;
import nc.multiblock.turbine.Turbine;
import nc.util.Lang;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiTurbineController extends GuiMultiblockController<Turbine> {
	
	protected final String type;
	protected final ResourceLocation gui_texture;
	
	public GuiTurbineController(Turbine multiblock, String type, Container container) {
		super(multiblock, container);
		this.type = type;
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + type + "_turbine_controller" + ".png");
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.container.turbine_controller." + type + "_turbine");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
	}
}
