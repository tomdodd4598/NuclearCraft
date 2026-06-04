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

public class GuiDecayPoolController extends GuiLogicMultiblockController<Machine, MachineLogic, IMachinePart, MachineUpdatePacket, TileDecayPoolController, TileContainerInfo<TileDecayPoolController>, DecayPoolLogic> {
	
	protected final ResourceLocation gui_texture;
	
	IntBinaryOperator containerCountText = centeredTracker(() -> Lang.localize("gui.nc.container.decay_pool_controller.containers") + " " + getLogic().getPartCount(TileDecayPoolContainer.class));
	IntBinaryOperator heatingRateText = centeredTracker(() -> Lang.localize("gui.nc.container.decay_pool_controller.heating_rate") + " " + UnitHelper.prefix(multiblock.baseSpeedMultiplier, 5, "H/t"));
	IntBinaryOperator totalDecayRateText = centeredTracker(() -> Lang.localize("gui.nc.container.decay_pool_controller.total_decay_rate") + " " + UnitHelper.prefix(getLogic().totalDecayRate, 5, "R/t"));
	IntBinaryOperator rateText = centeredTracker(() -> Lang.localize("gui.nc.container.machine_controller.rate") + " " + multiblock.recipeUnitInfo.getString(multiblock.readyToProcess ? logic.getProcessTimeFP() : null, 5));
	
	public GuiDecayPoolController(Container inventory, EntityPlayer player, TileDecayPoolController controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
		gui_texture = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "decay_pool_controller" + ".png");
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
		String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize("gui.nc.container.decay_pool_controller.decay_pool");
		fontRenderer.drawString(title, centeredWidth(title), 6, fontColor);
		
		String underline = StringHelper.charLine('-', MathHelper.ceil((double) fontRenderer.getStringWidth(title) / fontRenderer.getStringWidth("-")));
		fontRenderer.drawString(underline, centeredWidth(underline), 12, fontColor);
		
		containerCountText.applyAsInt(22, fontColor);
		
		heatingRateText.applyAsInt(34, fontColor);
		
		totalDecayRateText.applyAsInt(46, fontColor);
		
		rateText.applyAsInt(58, fontColor);
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
