 package nc.gui.machine;
 
import nc.container.machine.ContainerAutoWorkspace;
import nc.tile.machine.TileAutoWorkspace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
 
 public class GuiAutoWorkspace extends GuiContainer {
   public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/autoWorkspace.png");
   
   public TileAutoWorkspace autoWorkspace;
 
   public GuiAutoWorkspace(InventoryPlayer inventoryPlayer, TileAutoWorkspace entity) {
     super(new ContainerAutoWorkspace(inventoryPlayer, entity)); 
 
     this.autoWorkspace = entity;
 
     this.xSize = 176;
     this.ySize = 174;
   }
 
   public void drawGuiContainerForegroundLayer(int par1, int par2) {
     String name = StatCollector.translateToLocal("tile.autoWorkspaceIdle.name");
     this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
   }
 
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
     
     Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
     drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
     
     /*double k2 = 114*this.autoWorkspace.cookTime/this.autoWorkspace.FurnaceSpeed();
     int k = (int) Math.ceil(k2);
     drawTexturedModalRect(guiLeft + 30, guiTop + 8, 3, 174, k, 78);*/
   }
 }
