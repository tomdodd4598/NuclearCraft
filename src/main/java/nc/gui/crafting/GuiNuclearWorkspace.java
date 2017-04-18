package nc.gui.crafting;

import nc.container.crafting.ContainerNuclearWorkspace;
import nc.tile.crafting.TileNuclearWorkspace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class GuiNuclearWorkspace extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("nc:textures/gui/nuclearWorkspace.png");
	
	public GuiNuclearWorkspace(InventoryPlayer inventoryPlayer, World world, int x, int y, int z, TileNuclearWorkspace tile) {
		super(new ContainerNuclearWorkspace(inventoryPlayer, world, x, y, z, tile));
		
		this.xSize = 176;
		this.ySize = 193;
	}
	
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("gui.heavyDutyWorkspace");
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}