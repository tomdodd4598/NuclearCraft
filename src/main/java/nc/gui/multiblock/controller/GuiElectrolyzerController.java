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

public class GuiElectrolyzerController extends GuiLogicMultiblockController<Machine, MachineLogic, IMachinePart, MachineUpdatePacket, TileElectrolyzerController, TileContainerInfo<TileElectrolyzerController>, ElectrolyzerLogic> {
	
	protected final ResourceLocation gui_texture;
	
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
		fontRenderer.drawString(title, xSize / 2 - fontRenderer.getStringWidth(title) / 2, 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, xSize / 2 - fontRenderer.getStringWidth(underline) / 2, 12, fontColor);
		
		String electrodeEfficiency = Lang.localize("gui.nc.container.electrolyzer_controller.electrode_efficiency") + " " + NCMath.pcDecimalPlaces(logic.basePowerMultiplier <= 0D ? 0D : 8D * logic.baseSpeedMultiplier / logic.basePowerMultiplier, 1);
		fontRenderer.drawString(electrodeEfficiency, xSize / 2 - fontRenderer.getStringWidth(electrodeEfficiency) / 2, 22, fontColor);
		
		String electrolyteEfficiency = Lang.localize("gui.nc.container.electrolyzer_controller.electrolyte_efficiency") + " " + NCMath.pcDecimalPlaces(getLogic().electrolyteEfficiency, 1);
		fontRenderer.drawString(electrolyteEfficiency, xSize / 2 - fontRenderer.getStringWidth(electrolyteEfficiency) / 2, 34, fontColor);
		
		String rate = Lang.localize("gui.nc.container.machine_controller.rate") + " " + logic.recipeUnitInfo.getString(logic.getProcessTimeFP(), 5);
		fontRenderer.drawString(rate, xSize / 2 - fontRenderer.getStringWidth(rate) / 2, 46, fontColor);
		
		String power = Lang.localize("gui.nc.container.machine_controller.power") + " " + UnitHelper.prefix(logic.getProcessPower(), 5, "RF/t");
		fontRenderer.drawString(power, xSize / 2 - fontRenderer.getStringWidth(power) / 2, 58, fontColor);
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
