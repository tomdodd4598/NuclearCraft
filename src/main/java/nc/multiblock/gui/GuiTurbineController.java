package nc.multiblock.gui;

import nc.Global;
import nc.multiblock.turbine.Turbine;
import nc.util.Lang;
import nc.util.StringHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiTurbineController extends GuiMultiblockController<Turbine> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiTurbineController(Turbine multiblock, Container container) {
		super(multiblock, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "turbine_controller" + ".png");
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.container.turbine_controller." + "turbine");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double)width(title)/width("-")));
		fontRenderer.drawString(underline, xSize / 2 - width(underline) / 2, 12, fontColor);
	}
}
