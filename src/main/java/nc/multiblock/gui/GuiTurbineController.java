package nc.multiblock.gui;

import nc.Global;
import nc.multiblock.gui.element.MultiblockButton;
import nc.multiblock.network.ClearAllMaterialPacket;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.network.PacketHandler;
import nc.util.ColorHelper;
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

public class GuiTurbineController extends GuiMultiblockController<Turbine> {
	
	protected final ResourceLocation gui_texture;
	
	int inputRateWidth = 0;
	
	public GuiTurbineController(Turbine multiblock, BlockPos controllerPos, Container container) {
		super(multiblock, controllerPos, container);
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
		if (NCUtil.isModifierKeyDown()) drawTooltip(clearAllInfo(), mouseX, mouseY, 153, 5, 18, 18);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.nc.container.turbine_controller." + "turbine");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double)width(title)/width("-")));
		fontRenderer.drawString(underline, xSize / 2 - width(underline) / 2, 12, fontColor);
		
		String power = Lang.localise("gui.nc.container.turbine_controller.power") + " " + UnitHelper.prefix(Math.round(multiblock.power), 6, "RF/t");
		fontRenderer.drawString(power, xSize / 2 - width(power) / 2, 24, fontColor);
		
		String coils = NCUtil.isModifierKeyDown() ? Lang.localise("gui.nc.container.turbine_controller.dynamo_coil_count") + " " + (multiblock.getPartMap(TileTurbineRotorBearing.class).size() == 0 ? "0/0, 0/0" : multiblock.dynamoCoilCount + "/" + multiblock.getPartMap(TileTurbineRotorBearing.class).size()/2 + ", " + multiblock.dynamoCoilCountOpposite + "/" + multiblock.getPartMap(TileTurbineRotorBearing.class).size()/2) : Lang.localise("gui.nc.container.turbine_controller.dynamo_efficiency") + " " + NCMath.decimalPlaces(100D*multiblock.conductivity, 1) + "%";
		fontRenderer.drawString(coils, xSize / 2 - width(coils) / 2, 36, fontColor);
		
		String expansionLevel = Lang.localise("gui.nc.container.turbine_controller.expansion_level") + " " + (multiblock.idealTotalExpansionLevel <= 0D ? "0%" : (NCMath.decimalPlaces(100D*multiblock.totalExpansionLevel, 1) + "% [" + NCMath.decimalPlaces(multiblock.idealTotalExpansionLevel, 1) + " x " + NCMath.decimalPlaces(100D*(multiblock.totalExpansionLevel/multiblock.idealTotalExpansionLevel), 1) + "%]"));
		fontRenderer.drawString(expansionLevel, xSize / 2 - width(expansionLevel) / 2, 48, fontColor);
		
		double rateRatio = multiblock.recipeInputRateFP/multiblock.getLogic().getMaxRecipeRateMultiplier();
		String inputRate = Lang.localise("gui.nc.container.turbine_controller.fluid_rate") + " " + UnitHelper.prefix(Math.round(multiblock.recipeInputRateFP), 6, "B/t", -1) + " [" + Math.round(100D*rateRatio) + (rateRatio > 1D ? "%] [!]" : "%]");
		inputRateWidth = inputRateWidth - width(inputRate) > 1 ? width(inputRate) : Math.max(inputRateWidth, width(inputRate));
		fontRenderer.drawString(inputRate, xSize / 2 - inputRateWidth / 2, 60, multiblock.bearingTension <= 0D ? fontColor : multiblock.isTurbineOn ? (0xFFFFFF - (int)(255D*MathHelper.clamp(2D*multiblock.bearingTension, 0D, 1D)) - 256*(int)(255D*MathHelper.clamp(2D*multiblock.bearingTension - 1D, 0D, 1D))) : ColorHelper.blend(15641088, 0xFF0000, (float)multiblock.bearingTension));
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
				PacketHandler.instance.sendToServer(new ClearAllMaterialPacket(controllerPos));
			}
		}
	}
}
