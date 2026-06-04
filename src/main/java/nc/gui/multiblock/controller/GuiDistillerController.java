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

public class GuiDistillerController extends GuiLogicMultiblockController<Machine, MachineLogic, IMachinePart, MachineUpdatePacket, TileDistillerController, TileContainerInfo<TileDistillerController>, DistillerLogic> {
	
	protected final ResourceLocation gui_texture;
	
	IntBinaryOperator refluxBonusText = centeredTracker(() -> Lang.localize("gui.nc.container.distiller_controller.reflux_bonus") + " " + NCMath.pcDecimalPlaces(getLogic().refluxUnitBonus, 1));
	IntBinaryOperator reboilingBonusText = centeredTracker(() -> Lang.localize("gui.nc.container.distiller_controller.reboiling_bonus") + " " + NCMath.pcDecimalPlaces(getLogic().reboilingUnitBonus, 1));
	IntBinaryOperator distributionBonusText = centeredTracker(() -> Lang.localize("gui.nc.container.distiller_controller.distribution_bonus") + " " + NCMath.pcDecimalPlaces(getLogic().liquidDistributorBonus, 1));
	IntBinaryOperator rateText = centeredTracker(() -> Lang.localize("gui.nc.container.machine_controller.rate") + " " + multiblock.recipeUnitInfo.getString(multiblock.readyToProcess ? logic.getProcessTimeFP() : null, 5));
	IntBinaryOperator powerText = centeredTracker(() -> Lang.localize("gui.nc.container.machine_controller.power") + " " + UnitHelper.prefix(logic.getProcessPower(), 5, "RF/t"));
	
	public GuiDistillerController(Container inventory, EntityPlayer player, TileDistillerController controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "distiller_controller" + ".png");
		xSize = 176;
		ySize = 88;
	}
	
	@Override
	protected ResourceLocation getGuiTexture() {
		return gui_texture;
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		if (NCUtil.isModifierKeyDown()) {
			drawTooltip(clearAllInfo(), mouseX, mouseY, 153, 65, 18, 18);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = multiblock.isMachineOn ? 4210752 : 15641088;
		String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize("gui.nc.container.distiller_controller.distiller");
		fontRenderer.drawString(title, centeredWidth(title), 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, centeredWidth(underline), 12, fontColor);
		
		refluxBonusText.applyAsInt(22, fontColor);
		
		reboilingBonusText.applyAsInt(34, fontColor);
		
		distributionBonusText.applyAsInt(46, fontColor);
		
		rateText.applyAsInt(58, fontColor);
		
		powerText.applyAsInt(70, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new MultiblockButton.ClearAllMaterial(0, guiLeft + 153, guiTop + 65));
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
