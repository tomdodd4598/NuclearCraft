package nc.gui.machine;

import nc.container.machine.ContainerHeliumExtractor;
import nc.tile.machine.TileHeliumExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiHeliumExtractor extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation("nc:textures/gui/heliumExtractor.png");
	
	public TileHeliumExtractor heliumExtractor;

	public GuiHeliumExtractor(InventoryPlayer inventoryPlayer, TileHeliumExtractor entity) {
		super(new ContainerHeliumExtractor(inventoryPlayer, entity));
		
		this.heliumExtractor = entity;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.heliumExtractorIdle.name");
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(heliumExtractor.energy + " RF", 28, this.ySize - 94, 4210752);
		this.fontRendererObj.drawString(heliumExtractor.fluid + " mB", 149 - this.fontRendererObj.getStringWidth(heliumExtractor.fluid + " mB"), this.ySize - 94, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int k = (int) Math.ceil(this.heliumExtractor.cookTime * (23 + this.heliumExtractor.speedUpgrade/3) / this.heliumExtractor.getProcessTime);
		drawTexturedModalRect(guiLeft + 76, guiTop + 34, 176, 14, k + 1, 16);
		
		int e = heliumExtractor.energy * 74 / 250000;
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 31 + 74 - e, 16, e);
		
		int fl = heliumExtractor.fluid * 74 / 16000;
		drawTexturedModalRect(guiLeft + 152, guiTop + 6 + 74 - fl, 192, 31 + 74 - fl, 16, fl);
	}
}
