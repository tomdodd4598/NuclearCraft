package nc.gui.multiblock.controller;

import nc.Global;
import nc.gui.element.MultiblockButton;
import nc.multiblock.turbine.Turbine;
import nc.network.multiblock.*;
import nc.tile.TileContainerInfo;
import nc.tile.turbine.*;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiTurbineController extends GuiMultiblockController<Turbine, ITurbinePart, TurbineUpdatePacket, TileTurbineController, TileContainerInfo<TileTurbineController>> {
	
	protected final ResourceLocation gui_texture;
	
	int inputRateWidth = 0;
	
	public GuiTurbineController(Container inventory, EntityPlayer player, TileTurbineController controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "turbine_controller" + ".png");
		xSize = 176;
		ySize = 75;
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
		int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize("gui.nc.container.turbine_controller." + "turbine");
		fontRenderer.drawString(title, xSize / 2 - fontRenderer.getStringWidth(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, xSize / 2 - fontRenderer.getStringWidth(underline) / 2, 12, fontColor);
		
		String power = Lang.localize("gui.nc.container.turbine_controller.power") + " " + UnitHelper.prefix(Math.round(multiblock.power), 5, "RF/t");
		fontRenderer.drawString(power, xSize / 2 - fontRenderer.getStringWidth(power) / 2, 22, fontColor);
		
		int bearingCount = multiblock.getPartCount(TileTurbineRotorBearing.class);
		String coils = NCUtil.isModifierKeyDown() ? Lang.localize("gui.nc.container.turbine_controller.dynamo_coil_count") + " " + (bearingCount == 0 ? "0/0, 0/0" : multiblock.dynamoCoilCount + "/" + bearingCount / 2 + ", " + multiblock.dynamoCoilCountOpposite + "/" + bearingCount / 2) : Lang.localize("gui.nc.container.turbine_controller.dynamo_efficiency") + " " + NCMath.pcDecimalPlaces(multiblock.conductivity, 1);
		fontRenderer.drawString(coils, xSize / 2 - fontRenderer.getStringWidth(coils) / 2, 34, fontColor);
		
		String rotor = NCUtil.isModifierKeyDown() ? Lang.localize("gui.nc.container.turbine_controller.expansion_level") + " " + (multiblock.idealTotalExpansionLevel <= 0D ? "0%" : NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel, 1) + " [" + NCMath.decimalPlaces(multiblock.idealTotalExpansionLevel, 1) + " x " + NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel / multiblock.idealTotalExpansionLevel, 1) + "]") : Lang.localize("gui.nc.container.turbine_controller.rotor_efficiency") + " " + NCMath.pcDecimalPlaces(multiblock.rotorEfficiency, 1);
		fontRenderer.drawString(rotor, xSize / 2 - fontRenderer.getStringWidth(rotor) / 2, 46, fontColor);
		
		String inputRate;
		if (NCUtil.isModifierKeyDown()) {
			inputRate = Lang.localize("gui.nc.container.turbine_controller.power_bonus") + " " + NCMath.pcDecimalPlaces(multiblock.powerBonus, 1);
		}
		else {
			double rateRatio = multiblock.recipeInputRate / (double) multiblock.getLogic().getMaxRecipeRateMultiplier();
			double rateRatioFP = multiblock.recipeInputRateFP / multiblock.getLogic().getMaxRecipeRateMultiplier();
			inputRate = Lang.localize("gui.nc.container.turbine_controller.fluid_rate") + " " + UnitHelper.prefix(NCMath.roundTo(multiblock.recipeInputRateFP, 0.1D), 5, "B/t", -1) + " [" + NCMath.pcDecimalPlaces(rateRatioFP, 1) + (rateRatio > 1D ? "] [!]" : "]");
			inputRateWidth = inputRateWidth - fontRenderer.getStringWidth(inputRate) > 1 ? fontRenderer.getStringWidth(inputRate) : Math.max(inputRateWidth, fontRenderer.getStringWidth(inputRate));
		}
		fontRenderer.drawString(inputRate, xSize / 2 - (NCUtil.isModifierKeyDown() ? fontRenderer.getStringWidth(inputRate) : inputRateWidth) / 2, 58, multiblock.bearingTension <= 0D ? fontColor : multiblock.isTurbineOn ? 0xFFFFFF - NCMath.toInt((255D * MathHelper.clamp(2D * multiblock.bearingTension, 0D, 1D))) - 256 * NCMath.toInt((255D * MathHelper.clamp(2D * multiblock.bearingTension - 1D, 0D, 1D))) : ColorHelper.blend(15641088, 0xFF0000, (float) multiblock.bearingTension));
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
