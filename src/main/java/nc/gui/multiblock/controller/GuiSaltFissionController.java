package nc.gui.multiblock.controller;

import nc.Global;
import nc.gui.element.MultiblockButton;
import nc.multiblock.fission.*;
import nc.network.multiblock.*;
import nc.tile.TileContainerInfo;
import nc.tile.fission.*;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.function.IntBinaryOperator;

public class GuiSaltFissionController extends GuiLogicMultiblockController<FissionReactor, FissionReactorLogic, IFissionPart, FissionUpdatePacket, TileSaltFissionController, TileContainerInfo<TileSaltFissionController>, MoltenSaltFissionLogic> {
	
	protected final ResourceLocation gui_texture;
	
	IntBinaryOperator heatMultText = centeredTracker(() -> Lang.localize("gui.nc.container.fission_controller.heat_mult") + " " + NCMath.pcDecimalPlaces(multiblock.meanHeatMult, 1));
	IntBinaryOperator efficiencyText = centeredTracker(() -> Lang.localize("gui.nc.container.fission_controller.efficiency") + " " + NCMath.pcDecimalPlaces(multiblock.meanEfficiency, 1));
	IntBinaryOperator speedMultText = centeredTracker(() -> Lang.localize("gui.nc.container.salt_fission_controller.heating_speed_multiplier") + " " + NCMath.pcDecimalPlaces(getLogic().meanHeatingSpeedMultiplier, 1));
	IntBinaryOperator sparsityText = centeredTracker(() -> Lang.localize("gui.nc.container.fission_controller.sparsity") + " " + NCMath.pcDecimalPlaces(multiblock.sparsityEfficiencyMult, 1));
	IntBinaryOperator usefulPartCountText = centeredTracker(() -> Lang.localize("gui.nc.container.fission_controller.useful_parts") + " " + multiblock.usefulPartCount + "/" + multiblock.getInteriorVolume());
	IntBinaryOperator netClusterHeatingText = centeredTracker(() -> Lang.localize("gui.nc.container.fission_controller.net_cluster_heating") + " " + UnitHelper.prefix(getLogic().getNetClusterHeating(), 5, "H/t"));
	
	public GuiSaltFissionController(Container inventory, EntityPlayer player, TileSaltFissionController controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
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
		if (NCUtil.isModifierKeyDown()) {
			drawTooltip(clearAllInfo(), mouseX, mouseY, 153, 71, 18, 18);
		}
		
		drawHeatTooltip(mouseX, mouseY, 6, 103, 164, 6);
	}
	
	public List<String> heatInfo() {
		List<String> info = new ArrayList<>();
		info.add(TextFormatting.YELLOW + Lang.localize("gui.nc.container.fission_controller.heat_stored") + " " + TextFormatting.WHITE + UnitHelper.prefix(logic.heatBuffer.getHeatStored(), logic.heatBuffer.getHeatCapacity(), 5, "H"));
		info.add(TextFormatting.YELLOW + Lang.localize("gui.nc.container.fission_controller.net_cluster_heating") + " " + TextFormatting.WHITE + UnitHelper.prefix(getLogic().getNetClusterHeating(), 5, "H/t"));
		info.add(TextFormatting.BLUE + Lang.localize("gui.nc.container.fission_controller.total_cluster_cooling") + " " + TextFormatting.WHITE + UnitHelper.prefix(-multiblock.cooling, 5, "H/t"));
		return info;
	}
	
	public void drawHeatTooltip(int mouseX, int mouseY, int x, int y, int drawWidth, int drawHeight) {
		drawTooltip(heatInfo(), mouseX, mouseY, x, y, drawWidth, drawHeight);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isReactorOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize("gui.nc.container.salt_fission_controller.reactor");
		fontRenderer.drawString(title, centeredWidth(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, centeredWidth(underline) / 2, 12, fontColor);
		
		String clusters = Lang.localize("gui.nc.container.fission_controller.clusters") + " " + multiblock.clusterCount;
		fontRenderer.drawString(clusters, centeredWidth(clusters) / 2, 22, fontColor);
		
		if (NCUtil.isModifierKeyDown()) {
			heatMultText.applyAsInt(34, fontColor);
		}
		else {
			efficiencyText.applyAsInt(34, fontColor);
		}
		
		speedMultText.applyAsInt(46, fontColor);
		
		if (NCUtil.isModifierKeyDown()) {
			sparsityText.applyAsInt(58, fontColor);
		}
		else {
			usefulPartCountText.applyAsInt(58, fontColor);
		}
		
		String temperature = Lang.localize("gui.nc.container.fission_controller.temperature") + " " + (NCUtil.isModifierKeyDown() ? Math.round(logic.getTemperature() - 273.15D) + " C" : Math.round(logic.getTemperature()) + " K");
		fontRenderer.drawString(temperature, centeredWidth(temperature) / 2, NCUtil.isModifierKeyDown() ? 70 : 76, fontColor);
		
		if (!NCUtil.isModifierKeyDown()) {
			netClusterHeatingText.applyAsInt(88, fontColor);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int h = NCMath.toInt(Math.round((double) logic.heatBuffer.getHeatStored() / (double) logic.heatBuffer.getHeatCapacity() * 164));
		drawTexturedModalRect(guiLeft + 6, guiTop + 102, 3, 114, h, 6);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ClearAllMaterial(0, guiLeft + 153, guiTop + 81));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (multiblock.WORLD.isRemote) {
			if (guiButton.id == 0 && NCUtil.isModifierKeyDown()) {
				new ClearAllMaterialPacket(tile.getTilePos()).sendToServer();
			}
		}
	}
}
