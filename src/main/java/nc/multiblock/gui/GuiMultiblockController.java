package nc.multiblock.gui;

import nc.gui.NCGui;
import nc.multiblock.MultiblockBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiMultiblockController<MULTIBLOCK extends MultiblockBase> extends NCGui {
	
	protected MULTIBLOCK multiblock;
	
	public GuiMultiblockController(MULTIBLOCK multiblock, Container container) {
		super(container);
		this.multiblock = multiblock;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(getGuiTexture());
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	protected abstract ResourceLocation getGuiTexture();
}
