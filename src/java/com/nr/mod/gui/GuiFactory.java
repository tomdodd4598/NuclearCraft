package com.nr.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.nr.mod.blocks.tileentities.TileEntityFactory;
import com.nr.mod.container.ContainerFactory;

public class GuiFactory extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation("nr:textures/gui/factory.png");
	
	public TileEntityFactory factory;

	public GuiFactory(InventoryPlayer inventoryPlayer, TileEntityFactory entity) {
		super(new ContainerFactory(inventoryPlayer, entity));
		
		this.factory = entity;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.factoryIdle.name");
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(factory.energy + " RF", 28, this.ySize - 94, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int k = (int) Math.ceil(this.factory.cookTime * (23 + this.factory.speedUpgrade/3) / this.factory.getFurnaceSpeed);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, k + 1, 16);
		
		int e = factory.energy * 74 / 25000;
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 31 + 74 - e, 16, e);
	}
}
