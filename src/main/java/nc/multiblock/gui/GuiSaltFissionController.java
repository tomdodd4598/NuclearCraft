package nc.multiblock.gui;

import nc.Global;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiSaltFissionController extends GuiMultiblockController<SaltFissionReactor> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiSaltFissionController(SaltFissionReactor multiblock, Container container) {
		super(multiblock, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "salt_fission_controller" + ".png");
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isReactorOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.container.salt_fission_controller.reactor");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
		
		String vessels = Lang.localise("gui.container.salt_fission_controller.vessels") + " " + multiblock.getVessels().size();
		fontRenderer.drawString(vessels, xSize / 2 - width(vessels) / 2, 30, fontColor);
		
		String heaters = Lang.localise("gui.container.salt_fission_controller.heaters") + " " + multiblock.getHeaters().size();
		fontRenderer.drawString(heaters, xSize / 2 - width(heaters) / 2, 42, fontColor);
		
		String efficiency = Lang.localise("gui.container.salt_fission_controller.efficiency") + " " + (int) (multiblock.efficiency*100D) + "%";
		fontRenderer.drawString(efficiency, xSize / 2 - width(efficiency) / 2, 54, fontColor);
		
		String heat_mult = Lang.localise("gui.container.salt_fission_controller.heat_mult") + " " + (int) (multiblock.heatMult*100D) + "%";
		fontRenderer.drawString(heat_mult, xSize / 2 - width(heat_mult) / 2, 66, fontColor);
		
		String coolingRate = Lang.localise("gui.container.salt_fission_controller.cooling_rate") + " " + (int) (multiblock.coolingRate*100D) + "%";
		fontRenderer.drawString(coolingRate, xSize / 2 - width(coolingRate) / 2, 78, fontColor);
		
		String heat = Lang.localise("gui.container.salt_fission_controller.heat") + " " + UnitHelper.prefix((int) multiblock.heatBuffer.heatStored, (int) multiblock.heatBuffer.heatCapacity, 6, "H");
		fontRenderer.drawString(heat, xSize / 2 - width(heat) / 2, 90, fontColor);
		
		String heat_gen = Lang.localise("gui.container.salt_fission_controller.heat_gen") + " " + UnitHelper.prefix((int) multiblock.getHeatChange(false), 6, "H/t");
		fontRenderer.drawString(heat_gen, xSize / 2 - width(heat_gen) / 2, 102, fontColor);
		
		String cooling = Lang.localise("gui.container.salt_fission_controller.cooling") + " " + UnitHelper.prefix((int) -multiblock.cooling, 6, "H/t");
		fontRenderer.drawString(cooling, xSize / 2 - width(cooling) / 2, 114, fontColor);
		
		String needtosortoutthis = "Need to sort out this";
		fontRenderer.drawString(needtosortoutthis, xSize / 2 - width(needtosortoutthis) / 2, 138, fontColor);
		
		String guiatsomepoint = "GUI at some point...!";
		fontRenderer.drawString(guiatsomepoint, xSize / 2 - width(guiatsomepoint) / 2, 150, fontColor);
	}
}
