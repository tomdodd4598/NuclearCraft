package nc.multiblock.gui;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.multiblock.gui.element.MultiblockButton;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.network.ClearAllMaterialPacket;
import nc.network.PacketHandler;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.NCUtil;
import nc.util.StringHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class GuiHeatExchangerController extends GuiMultiblockController<HeatExchanger> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiHeatExchangerController(HeatExchanger multiblock, BlockPos controllerPos, Container container) {
		super(multiblock, controllerPos, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "heat_exchanger_controller" + ".png");
		xSize = 176;
		ySize = 68;
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		if (NCUtil.isModifierKeyDown()) drawTooltip(clearAllInfo(), mouseX, mouseY, 153, 35, 18, 18);
		
		drawEfficiencyTooltip(mouseX, mouseY, 6, 57, 164, 6);
	}
	
	public List<String> efficiencyInfo() {
		List<String> info = new ArrayList<>();
		info.add(TextFormatting.LIGHT_PURPLE + Lang.localise("gui.nc.container.heat_exchanger_controller.active_percent") + " " + TextFormatting.WHITE + NCMath.decimalPlaces(multiblock.fractionOfTubesActive*100D, 1) + "%");
		info.add(TextFormatting.AQUA + Lang.localise("gui.nc.container.heat_exchanger_controller.efficiency" + (NCUtil.isModifierKeyDown() ? "_max" : "")) + " " + TextFormatting.WHITE + NCMath.decimalPlaces((NCUtil.isModifierKeyDown() ? multiblock.maxEfficiency : multiblock.efficiency)*100D, 1) + "%");
		return info;
	}
	
	public void drawEfficiencyTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(efficiencyInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isHeatExchangerOn ? 4210752 : 15619328;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.nc.container.heat_exchanger_controller.heat_exchanger");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double)width(title)/width("-")));
		fontRenderer.drawString(underline, xSize / 2 - width(underline) / 2, 12, fontColor);
		
		String tubes = Lang.localise("gui.nc.container.heat_exchanger_controller.tubes") + " " + (multiblock.getTubes().size() + multiblock.getCondenserTubes().size());
		fontRenderer.drawString(tubes, xSize / 2 - width(tubes) / 2, 24, fontColor);
		
		String efficiency = Lang.localise("gui.nc.container.heat_exchanger_controller.efficiency") + " " + (int) (multiblock.efficiency*100D) + "%";
		fontRenderer.drawString(efficiency, xSize / 2 - width(efficiency) / 2, 40, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int f = (int)Math.round(multiblock.fractionOfTubesActive*164);
		drawTexturedModalRect(guiLeft + 6, guiTop + 56, 3, 68, f, 6);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ClearAllMaterial(0, guiLeft + 153, guiTop + 35));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (multiblock.WORLD.isRemote) {
			if (guiButton.id == 0 && NCUtil.isModifierKeyDown()) {
				PacketHandler.instance.sendToServer(new ClearAllMaterialPacket(controllerPos));
			}
		}
	}
}
