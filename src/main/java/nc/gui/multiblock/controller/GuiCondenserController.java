package nc.gui.multiblock.controller;

import nc.Global;
import nc.gui.element.MultiblockButton;
import nc.multiblock.hx.HeatExchanger;
import nc.network.multiblock.*;
import nc.tile.TileContainerInfo;
import nc.tile.hx.*;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiCondenserController extends GuiMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController, TileContainerInfo<TileCondenserController>> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiCondenserController(Container inventory, EntityPlayer player, TileCondenserController controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "heat_exchanger_controller" + ".png");
		xSize = 176;
		ySize = 76;
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		if (NCUtil.isModifierKeyDown()) {
			drawTooltip(clearAllInfo(), mouseX, mouseY, 153, 5, 18, 18);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isExchangerOn ? 4210752 : 15619328;
		String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize("gui.nc.container.condenser_controller.condenser");
		fontRenderer.drawString(title, xSize / 2 - fontRenderer.getStringWidth(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, xSize / 2 - fontRenderer.getStringWidth(underline) / 2, 12, fontColor);
		
		if (NCUtil.isModifierKeyDown()) {
			String networkCount = Lang.localize("gui.nc.container.heat_exchanger_controller.active_network_count") + " " + multiblock.activeNetworkCount + "/" + multiblock.totalNetworkCount;
			fontRenderer.drawString(networkCount, xSize / 2 - fontRenderer.getStringWidth(networkCount) / 2, 22, fontColor);
		}
		else {
			String tubeCount = Lang.localize("gui.nc.container.heat_exchanger_controller.active_tube_count") + " " + multiblock.activeTubeCount + "/" + multiblock.getPartCount(TileHeatExchangerTube.class);
			fontRenderer.drawString(tubeCount, xSize / 2 - fontRenderer.getStringWidth(tubeCount) / 2, 22, fontColor);
		}
		
		String tubeInputRate = Lang.localize("gui.nc.container.heat_exchanger_controller.tube_input") + " " + UnitHelper.prefix(Math.round(multiblock.tubeInputRateFP), 5, "B/t", -1);
		fontRenderer.drawString(tubeInputRate, xSize / 2 - fontRenderer.getStringWidth(tubeInputRate) / 2, 34, fontColor);
		
		String heatDissipationRate = Lang.localize("gui.nc.container.heat_exchanger_controller.heat_dissipation_rate") + " " + UnitHelper.prefix(Math.round(multiblock.heatDissipationRateFP), 5, "H/t");
		fontRenderer.drawString(heatDissipationRate, xSize / 2 - fontRenderer.getStringWidth(heatDissipationRate) / 2, 46, fontColor);
		
		String meanTempDiff = Lang.localize("gui.nc.container.heat_exchanger_controller.mean_temp_diff") + " " + UnitHelper.prefix(multiblock.activeContactCount == 0 ? 0D : Math.round(multiblock.totalTempDiff / multiblock.activeContactCount), 5, "K");
		fontRenderer.drawString(meanTempDiff, xSize / 2 - fontRenderer.getStringWidth(meanTempDiff) / 2, 58, fontColor);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ClearAllMaterial(0, guiLeft + 153, guiTop + 5));
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
