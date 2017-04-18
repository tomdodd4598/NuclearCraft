package nc.gui.machine;

import nc.container.machine.ContainerCrusher;
import nc.tile.machine.TileCrusher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiCrusher extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation("nc:textures/gui/crusher.png");
	
	public TileCrusher crusher;

	public GuiCrusher(InventoryPlayer inventoryPlayer, TileCrusher entity) {
		super(new ContainerCrusher(inventoryPlayer, entity));
		
		this.crusher = entity;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.crusherIdle.name");
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(this.crusher.isBurning()) {
			int k = this.crusher.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(guiLeft + 56, guiTop + 36 + 12 - k, 176, 12 - k, 14, k + 2);
		}
		
		int k = this.crusher.getCookProgressScaled(24);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, k + 1, 16);
		
	}
}
