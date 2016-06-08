package nc.gui.machine;
 
import nc.container.machine.ContainerAssembler;
import nc.tile.machine.TileAssembler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
 
public class GuiAssembler extends GuiContainer {
	public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/assembler.png");
	public TileAssembler assembler;
   
	public GuiAssembler(InventoryPlayer inventoryPlayer, TileAssembler entity) {
		super(new ContainerAssembler(inventoryPlayer, entity));
		
		this.assembler = entity;

		this.xSize = 176;
		this.ySize = 174;
	}

	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.assemblerIdle.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(assembler.energy + " RF", 28, this.ySize - 94, 4210752);
	}
   
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
     
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
     
		int e = assembler.energy * 82 / 250000;
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 82 - e, 176, 3 + 82 - e, 16, e);
     
		int k = (int) Math.ceil(this.assembler.cookTime * 64 / this.assembler.getFurnaceSpeed);
		drawTexturedModalRect(guiLeft + 68, guiTop + 37, 3, 174, k, 37);
	}
}
