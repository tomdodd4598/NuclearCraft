package nc.multiblock.gui;

import nc.Global;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.util.Lang;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiHeatExchangerController extends GuiMultiblockController<HeatExchanger> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiHeatExchangerController(HeatExchanger multiblock, Container container) {
		super(multiblock, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "heat_exchanger_controller" + ".png");
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isHeatExchangerOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.container.heat_exchanger_controller.heat_exchanger");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
	}
}
