package nc.gui.multiblock.controller;

import nc.Global;
import nc.gui.element.MultiblockButton;
import nc.multiblock.machine.*;
import nc.network.multiblock.*;
import nc.tile.TileContainerInfo;
import nc.tile.machine.*;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.function.IntBinaryOperator;

public class GuiElectrolyzerController extends GuiLogicMultiblockController<Machine, MachineLogic, IMachinePart, MachineUpdatePacket, TileElectrolyzerController, TileContainerInfo<TileElectrolyzerController>, ElectrolyzerLogic> {
	
	protected final ResourceLocation gui_texture;
	
	IntBinaryOperator electrodeEfficiencyText = centeredTracker(() -> Lang.localize("gui.nc.container.electrolyzer_controller.electrode_efficiency") + " " + NCMath.pcDecimalPlaces(multiblock.basePowerMultiplier <= 0D ? 0D : 8D * multiblock.baseSpeedMultiplier / multiblock.basePowerMultiplier, 1));
	IntBinaryOperator electrolyteEfficiencyText = centeredTracker(() -> Lang.localize("gui.nc.container.electrolyzer_controller.electrolyte_efficiency") + " " + NCMath.pcDecimalPlaces(getLogic().electrolyteEfficiency, 1));
	IntBinaryOperator rateText = centeredTracker(() -> Lang.localize("gui.nc.container.machine_controller.rate") + " " + multiblock.recipeUnitInfo.getString(multiblock.readyToProcess ? logic.getProcessTimeFP() : null, 5));
	IntBinaryOperator powerText = centeredTracker(() -> Lang.localize("gui.nc.container.machine_controller.power") + " " + UnitHelper.prefix(logic.getProcessPower(), 5, "RF/t"));
	
	public GuiElectrolyzerController(Container inventory, EntityPlayer player, TileElectrolyzerController controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "electrolyzer_controller" + ".png");
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
			drawTooltip(clearAllInfo(), mouseX, mouseY, 153, 53, 18, 18);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isMachineOn ? 4210752 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize("gui.nc.container.electrolyzer_controller.electrolyzer");
		fontRenderer.drawString(title, centeredWidth(title), 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, centeredWidth(underline), 12, fontColor);
		
		electrodeEfficiencyText.applyAsInt(22, fontColor);
		
		electrolyteEfficiencyText.applyAsInt(34, fontColor);
		
		rateText.applyAsInt(46, fontColor);
		
		powerText.applyAsInt(58, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ClearAllMaterial(0, guiLeft + 153, guiTop + 53));
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
