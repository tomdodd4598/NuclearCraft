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

import java.util.function.IntBinaryOperator;

public class GuiCondenserController extends GuiMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController, TileContainerInfo<TileCondenserController>> {
	
	protected final ResourceLocation gui_texture;
	
	IntBinaryOperator networkCountText = centeredTracker(() -> Lang.localize("gui.nc.container.heat_exchanger_controller.active_network_count") + " " + multiblock.activeNetworkCount + "/" + multiblock.totalNetworkCount);
	IntBinaryOperator tubeCountText = centeredTracker(() -> Lang.localize("gui.nc.container.heat_exchanger_controller.active_tube_count") + " " + multiblock.activeTubeCount + "/" + multiblock.getPartCount(TileHeatExchangerTube.class));
	IntBinaryOperator tubeInputRateText = centeredTracker(() -> Lang.localize("gui.nc.container.heat_exchanger_controller.tube_input") + " " + UnitHelper.prefix(multiblock.tubeInputRateFP, 5, "B/t", -1));
	IntBinaryOperator heatDissipationRateText = centeredTracker(() -> Lang.localize("gui.nc.container.heat_exchanger_controller.heat_dissipation_rate") + " " + UnitHelper.prefix(multiblock.heatTransferRateFP, 5, "H/t"));
	IntBinaryOperator meanTempDiffText = centeredTracker(() -> Lang.localize("gui.nc.container.heat_exchanger_controller.mean_temp_diff") + " " + UnitHelper.prefix(multiblock.activeContactCount == 0 ? 0D : multiblock.totalTempDiff / multiblock.activeContactCount, 5, "K"));
	
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
		fontRenderer.drawString(title, centeredWidth(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, centeredWidth(underline) / 2, 12, fontColor);
		
		if (NCUtil.isModifierKeyDown()) {
			networkCountText.applyAsInt(22, fontColor);
		}
		else {
			tubeCountText.applyAsInt(22, fontColor);
		}
		
		tubeInputRateText.applyAsInt(34, fontColor);
		
		heatDissipationRateText.applyAsInt(46, fontColor);
		
		meanTempDiffText.applyAsInt(58, fontColor);
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
