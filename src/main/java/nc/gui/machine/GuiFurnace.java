package nc.gui.machine;

import nc.container.machine.ContainerFurnace;
import nc.tile.machine.TileFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiFurnace extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation("nc:textures/gui/furnace.png");
	
	public TileFurnace furnace;

	public GuiFurnace(InventoryPlayer inventoryPlayer, TileFurnace entity) {
		super(new ContainerFurnace(inventoryPlayer, entity));
		
		this.furnace = entity;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.furnaceIdle.name");
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(this.furnace.isBurning()) {
			int k = this.furnace.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(guiLeft + 56, guiTop + 36 + 12 - k, 176, 12 - k, 14, k + 2);
		}
		
		int k = this.furnace.getCookProgressScaled(24);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, k + 1, 16);	
	}
}
