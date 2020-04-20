package nc.multiblock.gui;

import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.MoltenSaltFissionLogic;
import nc.multiblock.gui.element.MultiblockButton;
import nc.multiblock.network.ClearAllMaterialPacket;
import nc.network.PacketHandler;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.NCUtil;
import nc.util.StringHelper;
import nc.util.UnitHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class GuiSaltFissionController extends GuiLogicMultiblockController<FissionReactor, MoltenSaltFissionLogic> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiSaltFissionController(FissionReactor multiblock, BlockPos controllerPos, Container container) {
		super(multiblock, controllerPos, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "salt_fission_controller" + ".png");
		xSize = 176;
		ySize = 114;
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		if (NCUtil.isModifierKeyDown()) drawTooltip(clearAllFluidsInfo(), mouseX, mouseY, 153, 71, 18, 18);
		
		drawEfficiencyTooltip(mouseX, mouseY, 6, 93, 164, 6);
		drawHeatTooltip(mouseX, mouseY, 6, 103, 164, 6);
	}
	
	//TODO
	public List<String> efficiencyInfo() {
		List<String> info = new ArrayList<>();
		info.add(TextFormatting.AQUA + Lang.localise("gui.nc.container.salt_fission_controller.raw_efficiency" + (NCUtil.isModifierKeyDown() ? "_max" : "")) + " " + TextFormatting.WHITE + NCMath.decimalPlaces((NCUtil.isModifierKeyDown() ? multiblock.totalEfficiency : multiblock.meanEfficiency)*100D, 1) + "%");
		info.add(TextFormatting.YELLOW + Lang.localise("gui.nc.container.salt_fission_controller.heat_mult" + (NCUtil.isModifierKeyDown() ? "_max" : "")) + " " + TextFormatting.WHITE + NCMath.decimalPlaces((NCUtil.isModifierKeyDown() ? multiblock.totalHeatMult : multiblock.meanHeatMult)*100D, 1) + "%");
		info.add(TextFormatting.BLUE + Lang.localise("gui.nc.container.salt_fission_controller.cooling_efficiency") + " " + TextFormatting.WHITE + NCMath.decimalPlaces(/*multiblock.coolingEfficiency*/100D, 1) + "%");
		return info;
	}
	
	public void drawEfficiencyTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(efficiencyInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	public List<String> heatInfo() {
		List<String> info = new ArrayList<>();
		info.add(TextFormatting.YELLOW + Lang.localise("gui.nc.container.salt_fission_controller.heat_stored") + " " + TextFormatting.WHITE + UnitHelper.prefix(multiblock.heatBuffer.getHeatStored(), multiblock.heatBuffer.getHeatCapacity(), 6, "H"));
		info.add(TextFormatting.YELLOW + Lang.localise("gui.nc.container.salt_fission_controller.raw_net_heating") + " " + TextFormatting.WHITE + UnitHelper.prefix(logic.getNetClusterHeating(), 6, "H/t"));
		info.add(TextFormatting.BLUE + Lang.localise("gui.nc.container.salt_fission_controller.cooling_rate") + " " + TextFormatting.WHITE + UnitHelper.prefix(-multiblock.cooling, 6, "H/t"));
		return info;
	}
	
	public void drawHeatTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(heatInfo(), mouseX, mouseY, x, y, width, height);
	}
	
	//TODO
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isReactorOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.nc.container.salt_fission_controller.reactor");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double)width(title)/width("-")));
		fontRenderer.drawString(underline, xSize / 2 - width(underline) / 2, 12, fontColor);
		
		String vessels = Lang.localise("gui.nc.container.salt_fission_controller.vessels") + " " + multiblock.fuelComponentCount;
		fontRenderer.drawString(vessels, xSize / 2 - width(vessels) / 2, 24, fontColor);
		
		String heaters = Lang.localise("gui.nc.container.salt_fission_controller.heaters") + " " /*+ multiblock.getHeaters().size()*/;
		fontRenderer.drawString(heaters, xSize / 2 - width(heaters) / 2, 36, fontColor);
		
		String coolingRate = Lang.localise("gui.nc.container.salt_fission_controller.cooling_efficiency") + " " + (int) (/*multiblock.coolingEfficiency*/100D) + "%";
		fontRenderer.drawString(coolingRate, xSize / 2 - width(coolingRate) / 2, 52, fontColor);
		
		//String heat_gen = Lang.localise("gui.nc.container.salt_fission_controller.net_heating") + " " + UnitHelper.prefix(multiblock.netHeating, 6, "H/t");
		//fontRenderer.drawString(heat_gen, xSize / 2 - width(heat_gen) / 2, 64, fontColor);
		
		String cooling = Lang.localise("gui.nc.container.salt_fission_controller.cooling_rate") + " " + UnitHelper.prefix(-multiblock.cooling, 6, "H/t");
		fontRenderer.drawString(cooling, xSize / 2 - width(cooling) / 2, 76, fontColor);
	}
	
	//TODO
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		/*int e = (int)Math.round(multiblock.coolingEfficiency/multiblock.rawEfficiency*164);
		drawTexturedModalRect(guiLeft + 6, guiTop + 92, 3, 114, e, 6);*/
		
		int h = (int)Math.round((double)multiblock.heatBuffer.getHeatStored()/(double)multiblock.heatBuffer.getHeatCapacity()*164);
		drawTexturedModalRect(guiLeft + 6, guiTop + 102, 3, 120, h, 6);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ClearAllMaterial(0, guiLeft + 153, guiTop + 71));
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
