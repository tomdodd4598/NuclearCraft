package nc.multiblock.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.gui.NCGui;
import nc.multiblock.Multiblock;
import nc.util.Lang;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public abstract class GuiMultiblockController<MULTIBLOCK extends Multiblock> extends NCGui {
	
	protected MULTIBLOCK multiblock;
	protected BlockPos controllerPos;
	
	public GuiMultiblockController(MULTIBLOCK multiblock, BlockPos controllerPos, Container container) {
		super(container);
		this.multiblock = multiblock;
		this.controllerPos = controllerPos;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(getGuiTexture());
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	protected abstract ResourceLocation getGuiTexture();
	
	public List<String> clearAllInfo() {
		return Lists.newArrayList(TextFormatting.ITALIC + Lang.localise("gui.nc.container.shift_clear_multiblock"));
	}
}
