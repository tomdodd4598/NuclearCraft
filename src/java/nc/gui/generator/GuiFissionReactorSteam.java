package nc.gui.generator;

import nc.container.generator.ContainerFissionReactorSteam;
import nc.tile.generator.TileFissionReactorSteam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiFissionReactorSteam extends GuiContainer {
    public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/fissionReactorSteam.png");
    
    public TileFissionReactorSteam entity;

    public GuiFissionReactorSteam(InventoryPlayer inventoryPlayer, TileFissionReactorSteam entity) {
        super(new ContainerFissionReactorSteam(inventoryPlayer, entity));
        this.entity = entity;
        this.xSize = 176;
        this.ySize = 177;
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2) {
        String name1 = (this.entity.lx-2) + "x" + (this.entity.ly-2) + "x" + (this.entity.lz-2) + " " + StatCollector.translateToLocal("gui.reactor");
        String name2 = this.entity.problem;
        this.fontRendererObj.drawString((this.entity.complete == 1 ? name1 : name2), 67, 5, (this.entity.complete == 1 ? -1 : 15597568));
        String power = this.entity.fluid + " mB" + (this.entity.S < 200 ? "" : (this.entity.S < 40000 ? " (" + StatCollector.translateToLocal("gui.dense") + ")" : " (" + StatCollector.translateToLocal("gui.superdense") + ")"));
        this.fontRendererObj.drawString(power, 67, 16, (this.entity.complete == 1 ? -1 : 15597568));
        String fueltime = (this.entity.fueltime/100000) + "% " + StatCollector.translateToLocal("gui.fuel");
        this.fontRendererObj.drawString(fueltime, 67, 27, (this.entity.complete == 1 ? -1 : 15597568));
        String heat = (this.entity.heat) + " "+ StatCollector.translateToLocal("gui.heat");
        this.fontRendererObj.drawString(heat, 67, 38, (this.entity.complete == 1 ? -1 : 15597568));
        
        String efficiency = (this.entity.efficiency + "% " + StatCollector.translateToLocal("gui.efficiency"))+"";
        this.fontRendererObj.drawString(efficiency, 67, 49, (this.entity.complete == 1 ? -1 : 15597568));
        
        String fueltype = (this.entity.fueltime == 0 ? StatCollector.translateToLocal("gui.noFuel") : this.entity.typeoffuel)+"";
        this.fontRendererObj.drawString(fueltype, 67, 60, (this.entity.complete == 1 ? -1 : 15597568));
        
        String egen = this.entity.fueltime == 0 ? 0 + " mB/t" : (this.entity.S < 10000 ? (this.entity.S)+" mB/t" : (this.entity.S < 10000000 ? ((int)(Math.round(this.entity.S/1000))) + " B/t" : ((int)(Math.round(this.entity.S/1000000))) + " kB/t"));
        this.fontRendererObj.drawString(egen, 7, 63, (this.entity.complete == 0 ? 15597568 : (this.entity.off==1 ? 15641088 : -1)));
        int heatGen = this.entity.H >= 0 ? this.entity.H : -this.entity.MinusH;
        String hgen = Math.abs(heatGen) < 100000 ? (heatGen)+" H/t" : (Math.abs(heatGen) < 10000000 ? ((int)(Math.round(heatGen/1000))) + " kH/t" : ((int)(Math.round(heatGen/1000000))) + " MH/t");
        this.fontRendererObj.drawString(hgen, 7, 74, (this.entity.complete == 0 ? 15597568 : (this.entity.off==1 ? 15641088 : -1)));
        
        String numberCells = /*StatCollector.translateToLocal("gui.cells")*/ "Cells" + ": " + this.entity.numberOfCells;
        this.fontRendererObj.drawString(numberCells, 7, 85, (this.entity.complete == 1 ? -1 : 15597568));
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int k = this.entity.fluid * 55 / 100000;
        this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 6 + 55 - k, 176, 3 + 55 - k + (this.entity.complete == 1 ? 0 : 55), 16, k);
        int k2 = this.entity.fueltime * 55 / 10000000;
        this.drawTexturedModalRect(this.guiLeft + 27, this.guiTop + 6 + 55 - k2, 192, 3 + 55 - k2 + (this.entity.complete == 1 ? 0 : 55), 16, k2);
        int k3 = this.entity.heat * 55 / 1000000;
        this.drawTexturedModalRect(this.guiLeft + 46, this.guiTop + 6 + 55 - k3, 208, 3 + 55 - k3 + (this.entity.complete == 1 ? 0 : 55), 16, k3);
        int k4 = ((this.entity.fueltime != 0) ? (56 - (this.entity.fueltime * 56 / 10000000)) : 0);
		drawTexturedModalRect(guiLeft + 86, guiTop + 65, 3 + (this.entity.complete == 1 ? 0 : 56), 177, k4, 27);
    }
}
