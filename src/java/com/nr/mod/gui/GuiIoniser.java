package com.nr.mod.gui;
 
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.nr.mod.blocks.tileentities.TileEntityIoniser;
import com.nr.mod.container.ContainerIoniser;
 
public class GuiIoniser extends GuiContainer {
	public static final ResourceLocation bground = new ResourceLocation("nr:textures/gui/ioniser.png");
   
	public TileEntityIoniser ioniser;
   
	public GuiIoniser(InventoryPlayer inventoryPlayer, TileEntityIoniser entity) {
		super(new ContainerIoniser(inventoryPlayer, entity));
			this.ioniser = entity;
			this.xSize = 176;
			this.ySize = 174;
	}
   
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.ioniserIdle.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(ioniser.energy + " RF", 28, this.ySize - 94, 4210752);
	}
   
 
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
     
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
     
		int e = ioniser.energy * 82 / 40000;
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 82 - e, 176, 3 + 82 - e, 16, e);
     
		int k = (int) Math.ceil(this.ioniser.cookTime * 70 / this.ioniser.getFurnaceSpeed);
		drawTexturedModalRect(guiLeft + 59, guiTop + 37, 3, 174, k, 37);
	}
}
