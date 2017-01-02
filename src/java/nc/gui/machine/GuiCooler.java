package nc.gui.machine;

import nc.container.machine.ContainerCooler;
import nc.tile.machine.TileCooler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiCooler extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation("nc:textures/gui/cooler.png");
	
	public TileCooler cooler;

	public GuiCooler(InventoryPlayer inventoryPlayer, TileCooler entity) {
		super(new ContainerCooler(inventoryPlayer, entity));
		
		this.cooler = entity;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.coolerIdle.name");
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(cooler.energy + " RF", 28, this.ySize - 94, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int k = (int) Math.ceil(this.cooler.cookTime * (23 + this.cooler.speedUpgrade/3) / this.cooler.getProcessTime);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, k + 1, 16);
		
		int e = cooler.energy * 74 / 250000;
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 31 + 74 - e, 16, e);
	}
}
