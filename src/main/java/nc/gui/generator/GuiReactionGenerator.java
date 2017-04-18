package nc.gui.generator;

import nc.container.generator.ContainerReactionGenerator;
import nc.tile.generator.TileReactionGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiReactionGenerator extends GuiContainer
{
    public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/reactionGenerator.png");
    
    public TileReactionGenerator entity;

    public GuiReactionGenerator(InventoryPlayer inventoryPlayer, TileReactionGenerator entity) {
        super(new ContainerReactionGenerator(inventoryPlayer, entity));
        this.entity = entity;
        this.xSize = 176;
        this.ySize = 166;
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2) {
        String name1 = StatCollector.translateToLocal("gui.reaction");
        String name2 = StatCollector.translateToLocal("gui.generator");
        this.fontRendererObj.drawString(name1, this.xSize / 2 - this.fontRendererObj.getStringWidth(name1) / 2, 5, 4210752);
        this.fontRendererObj.drawString(name2, this.xSize / 2 - this.fontRendererObj.getStringWidth(name2) / 2, 15, 4210752);
        String power = this.entity.energy + " RF";
        this.fontRendererObj.drawString(power, this.xSize / 2 - this.fontRendererObj.getStringWidth(power) / 2, 65, -1);
        String fuel = ((int) this.entity.fuellevel/5000)+"%";
        this.fontRendererObj.drawString(fuel, 69 - this.fontRendererObj.getStringWidth(fuel) / 2, 46, 4210752);
        String reactant = ((int) this.entity.reactantlevel/5000)+"%";
        this.fontRendererObj.drawString(reactant, 107 - this.fontRendererObj.getStringWidth(reactant) / 2, 46, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int k = this.entity.energy * 64 / 100000;
        @SuppressWarnings("unused")
		int j = 64 - k;
        this.drawTexturedModalRect(this.guiLeft + 56, this.guiTop + 57, 176, 153, k, 23);
        int k2 = this.entity.reactantlevel * 74 / 500000;
        this.drawTexturedModalRect(this.guiLeft + 124, this.guiTop + 6 + 74 - k2, 176, 78 + 74 - k2, 44, k2);
        int k3 = this.entity.fuellevel * 74 / 500000;
        this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 6 + 74 - k3, 176, 3 + 74 - k3, 44, k3);
    }
}
