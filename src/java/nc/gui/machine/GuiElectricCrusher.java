package nc.gui.machine;

import nc.container.machine.ContainerElectricCrusher;
import nc.tile.machine.TileElectricCrusher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiElectricCrusher extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation("nc:textures/gui/electricCrusher.png");
	
	public TileElectricCrusher electricCrusher;

	public GuiElectricCrusher(InventoryPlayer inventoryPlayer, TileElectricCrusher entity) {
		super(new ContainerElectricCrusher(inventoryPlayer, entity));
		
		this.electricCrusher = entity;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.electricCrusherIdle.name");
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(electricCrusher.energy + " RF", 28, this.ySize - 94, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int k = (int) Math.ceil(this.electricCrusher.cookTime * (23 + this.electricCrusher.speedUpgrade/3) / this.electricCrusher.getProcessTime);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, k + 1, 16);
		
		int e = electricCrusher.energy * 74 / 250000;
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 31 + 74 - e, 16, e);
	}
}
