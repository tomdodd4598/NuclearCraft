 package nc.gui.machine;
 
 import nc.container.machine.ContainerCollector;
import nc.tile.machine.TileCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
 
 public class GuiCollector extends GuiContainer {
   public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/collector.png");
   
   public TileCollector collector;
 
   public GuiCollector(InventoryPlayer inventoryPlayer, TileCollector entity) {
     super(new ContainerCollector(inventoryPlayer, entity)); 
 
     this.collector = entity;
 
     this.xSize = 176;
     this.ySize = 174;
   }
 
   public void drawGuiContainerForegroundLayer(int par1, int par2) {
     String name = StatCollector.translateToLocal("tile.collectorIdle.name");
     this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
   }
 
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
     
     Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
     drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
     
     double k2 = 114*this.collector.cookTime/this.collector.ProcessTime();
     int k = (int) Math.ceil(k2);
     drawTexturedModalRect(guiLeft + 30, guiTop + 8, 3, 174, k, 78);
   }
 }
