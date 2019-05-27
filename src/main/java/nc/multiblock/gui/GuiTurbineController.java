package nc.multiblock.gui;

import com.google.common.math.DoubleMath;

import nc.Global;
import nc.multiblock.gui.element.MultiblockButton;
import nc.multiblock.network.ClearAllFluidsPacket;
import nc.multiblock.turbine.Turbine;
import nc.network.PacketHandler;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.StringHelper;
import nc.util.UnitHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class GuiTurbineController extends GuiMultiblockController<Turbine> {
	
	protected final ResourceLocation gui_texture;
	
	public GuiTurbineController(Turbine multiblock, BlockPos controllerPos, Container container) {
		super(multiblock, controllerPos, container);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "turbine_controller" + ".png");
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		if (isShiftKeyDown()) drawTooltip(clearAllFluidsInfo(), mouseX, mouseY, 162, 152, 9, 9);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" +  multiblock.getInteriorLengthY() + "*" +  multiblock.getInteriorLengthZ() + " " + Lang.localise("gui.container.turbine_controller." + "turbine");
		fontRenderer.drawString(title, xSize / 2 - width(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double)width(title)/width("-")));
		fontRenderer.drawString(underline, xSize / 2 - width(underline) / 2, 12, fontColor);
		
		String power = Lang.localise("gui.container.turbine_controller.power") + " " + UnitHelper.prefix(Math.round(multiblock.power), 6, "RF/t");
		fontRenderer.drawString(power, xSize / 2 - width(power) / 2, 24, fontColor);
		
		String coils = Lang.localise("gui.container.turbine_controller.dynamo_coil_count") + " " + (multiblock.getRotorBearings().size() == 0 ? "0/0 [0%]" : multiblock.getDynamoCoils().size() + "/" + multiblock.getRotorBearings().size() + " [" + Math.min(100, Math.round((100D*multiblock.getDynamoCoils().size()/multiblock.getRotorBearings().size()))) + "%]");
		fontRenderer.drawString(coils, xSize / 2 - width(coils) / 2, 36, fontColor);
		
		String expansion_level = Lang.localise("gui.container.turbine_controller.expansion_level") + " " + (multiblock.idealTotalExpansionLevel <= 0D ? "0%" : (NCMath.round(100D*multiblock.totalExpansionLevel, 1) + "% [" + (DoubleMath.isMathematicalInteger(multiblock.idealTotalExpansionLevel) ? new Integer((int)multiblock.idealTotalExpansionLevel).toString() : NCMath.round(multiblock.idealTotalExpansionLevel, 1)) + " x " + NCMath.round(100D*(multiblock.totalExpansionLevel/multiblock.idealTotalExpansionLevel), 1) + "%]"));
		fontRenderer.drawString(expansion_level, xSize / 2 - width(expansion_level) / 2, 48, fontColor);
		
		String fluid_rate = Lang.localise("gui.container.turbine_controller.fluid_rate") + " " + UnitHelper.prefix(Math.round(multiblock.getActualInputRate()), 6, "B/t", -1) + " [" + Math.round(100D*multiblock.getActualInputRate()/multiblock.getMaxRecipeRateMultiplier()) + "%]";
		fontRenderer.drawString(fluid_rate, xSize / 2 - width(fluid_rate) / 2, 60, fontColor);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ButtonClearAllFluids(0, guiLeft + 162, guiTop + 152));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (multiblock.WORLD.isRemote) {
			if (guiButton.id == 0 && isShiftKeyDown()) {
				PacketHandler.instance.sendToServer(new ClearAllFluidsPacket(controllerPos));
			}
		}
	}
}
