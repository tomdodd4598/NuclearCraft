package nc.gui.generator;

import nc.container.generator.ContainerFusionReactorSteam;
import nc.tile.generator.TileFusionReactorSteam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiFusionReactorSteam extends GuiContainer
{
    public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/fusionReactorSteam.png");
    
    public TileFusionReactorSteam entity;

    public GuiFusionReactorSteam(InventoryPlayer inventoryPlayer, TileFusionReactorSteam entity)
    {
        super(new ContainerFusionReactorSteam(inventoryPlayer, entity));
        this.entity = entity;
        this.xSize = 244;
        this.ySize = 255;
    }
    
    public String f1() {
    	if (this.entity.HLevel > 0) {return "Hydrogen";}
    	else if (this.entity.DLevel > 0) {return "Deuterium";}
    	else if (this.entity.TLevel > 0) {return "Tritium";}
    	else if (this.entity.HeLevel > 0) {return "Helium-3";}
    	else if (this.entity.BLevel > 0) {return "Boron-11";}
    	else if (this.entity.Li6Level > 0) {return "Lithium-6";}
    	else if (this.entity.Li7Level > 0) {return "Lithium-7";}
    	else {return "Fuel";}
    }
    
    public String f2() {
    	if (this.entity.HLevel2 > 0) {return "Hydrogen";}
    	else if (this.entity.DLevel2 > 0) {return "Deuterium";}
    	else if (this.entity.TLevel2 > 0) {return "Tritium";}
    	else if (this.entity.HeLevel2 > 0) {return "Helium-3";}
    	else if (this.entity.BLevel2 > 0) {return "Boron-11";}
    	else if (this.entity.Li6Level2 > 0) {return "Lithium-6";}
    	else if (this.entity.Li7Level2 > 0) {return "Lithium-7";}
    	else {return "Fuel";}
    }
    
    public int level1() {
    	if (this.entity.HLevel > 0) {return this.entity.HLevel;}
    	else if (this.entity.DLevel > 0) {return this.entity.DLevel;}
    	else if (this.entity.TLevel > 0) {return this.entity.TLevel;}
    	else if (this.entity.HeLevel > 0) {return this.entity.HeLevel;}
    	else if (this.entity.BLevel > 0) {return this.entity.BLevel;}
    	else if (this.entity.Li6Level > 0) {return this.entity.Li6Level;}
    	else if (this.entity.Li7Level > 0) {return this.entity.Li7Level;}
    	else {return 0;}
    }
    
    public int level2() {
    	if (this.entity.HLevel2 > 0) {return this.entity.HLevel2;}
    	else if (this.entity.DLevel2 > 0) {return this.entity.DLevel2;}
    	else if (this.entity.TLevel2 > 0) {return this.entity.TLevel2;}
    	else if (this.entity.HeLevel2 > 0) {return this.entity.HeLevel2;}
    	else if (this.entity.BLevel2 > 0) {return this.entity.BLevel2;}
    	else if (this.entity.Li6Level2 > 0) {return this.entity.Li6Level2;}
    	else if (this.entity.Li7Level2 > 0) {return this.entity.Li7Level2;}
    	else {return 0;}
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String name1 = StatCollector.translateToLocal("tile.fusionReactor.name");
        String name2 = this.entity.problem;
        this.fontRendererObj.drawString((this.entity.complete == 1 ? name1 : name2), 11, 10, (this.entity.complete == 1 ? -1 : 15597568));
        String radius = StatCollector.translateToLocal("gui.ringRadius") + ": " + entity.size;
        this.fontRendererObj.drawString(radius, 11, 20, (this.entity.complete == 1 ? -1 : 15597568));
        String power = StatCollector.translateToLocal("gui.steamStored") + ": " + this.entity.fluid + " mB" + (this.entity.steamType <= 1 ? "" : (this.entity.steamType == 2 ? " (" + StatCollector.translateToLocal("gui.dense") + ")" : " (" + StatCollector.translateToLocal("gui.superdense") + ")"));
        this.fontRendererObj.drawString(power, 11, 30, (this.entity.complete == 1 ? -1 : 15597568));
        String fuel1 = f1() + " " + StatCollector.translateToLocal("gui.level1") + ": " + Math.round(level1() / 64000) + "%";
        this.fontRendererObj.drawString(fuel1, 11, 40, (this.entity.complete == 1 ? -1 : 15597568));
        String fuel2 = f2() + " " + StatCollector.translateToLocal("gui.level2") + ": " + Math.round(level2() / 64000) + "%";
        this.fontRendererObj.drawString(fuel2, 11, 50, (this.entity.complete == 1 ? -1 : 15597568));
        String egen = StatCollector.translateToLocal("gui.steamProduction") + ": " + ((this.entity.HLevel + this.entity.DLevel + this.entity.TLevel + this.entity.HeLevel + this.entity.BLevel + this.entity.Li6Level + this.entity.Li7Level == 0 || this.entity.HLevel2 + this.entity.DLevel2 + this.entity.TLevel2 + this.entity.HeLevel2 + this.entity.BLevel2 + this.entity.Li6Level2 + this.entity.Li7Level2 == 0) ? 0 : this.entity.SShown) + " mB/t";
        this.fontRendererObj.drawString(egen, 11, 60, (this.entity.complete == 1 ? -1 : 15597568));
        String efficiency = StatCollector.translateToLocal("gui.efficiency") + ": " + Math.round(entity.efficiency) + "%";
        this.fontRendererObj.drawString(efficiency, 11, 70, (this.entity.complete == 1 ? -1 : 15597568));
        String heat = StatCollector.translateToLocal("gui.temperature") + ": " + (int) entity.heat + " MK";
        this.fontRendererObj.drawString(heat, 11, 80, (this.entity.complete == 1 ? -1 : 15597568));
        //String heatVar = StatCollector.translateToLocal("gui.heatVar") + ": " + entity.heatVar;
        //this.fontRendererObj.drawString(heatVar, 11, 90, (this.entity.complete == 1 ? -1 : 15597568));
        String input = StatCollector.translateToLocal("gui.input");
        this.fontRendererObj.drawString(input, 49, 157, (this.entity.complete == 1 ? -1 : 15597568));
        //String cells = StatCollector.translateToLocal("gui.cells");
        //this.fontRendererObj.drawString(cells, 166-this.fontRendererObj.getStringWidth(cells), 157, (this.entity.complete == 1 ? -1 : 15597568));
        String out = StatCollector.translateToLocal("gui.output");
        this.fontRendererObj.drawString(out, 172+32-this.fontRendererObj.getStringWidth(out)/2, 235, (this.entity.complete == 1 ? -1 : 15597568));
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int k = (int) (this.entity.fluid * 162 / 10000000);
        for (int texi = 0; texi < 5; ++texi) {
        	this.drawTexturedModalRect(this.guiLeft + 213 + texi, this.guiTop + 7 + 162 - k, 244, 3 + 162 - k, 1, k);
        }
        int heat1 = (int) (this.entity.heat * 162 / 20000);
        for (int texi2 = 0; texi2 < 5; ++texi2) {
        	this.drawTexturedModalRect(this.guiLeft + 203 + texi2, this.guiTop + 7 + 162 - heat1, 245, 3 + 162 - heat1, 1, heat1);
        }
        int ef = (int) (this.entity.efficiency * 162 / 100);
        for (int texi3 = 0; texi3 < 5; ++texi3) {
        	this.drawTexturedModalRect(this.guiLeft + 193 + texi3, this.guiTop + 7 + 162 - ef, 246, 3 + 162 - ef, 1, ef);
        }
        int k2 = level1() * 162 / 6400000;
        this.drawTexturedModalRect(this.guiLeft + 222, this.guiTop + 7 + 162 - k2, 249, 3 + 162 - k2, 5, k2);
        int k3 = level2() * 162 / 6400000;
        this.drawTexturedModalRect(this.guiLeft + 231, this.guiTop + 7 + 162 - k3, 249, 3 + 162 - k3, 5, k3);
        
        int HOut = (int) Math.round(this.entity.HOut * 14 / 100000);
        if (this.entity.HOut <= 100000) drawTexturedModalRect(guiLeft + 197, guiTop + 198, 241, 255, HOut, 1); else drawTexturedModalRect(guiLeft + 197, guiTop + 198, 241, 255, 14, 1);
        int DOut = (int) Math.round(this.entity.DOut * 14 / 100000);
        if (this.entity.DOut <= 100000) drawTexturedModalRect(guiLeft + 221, guiTop + 198, 241, 255, DOut, 1); else drawTexturedModalRect(guiLeft + 221, guiTop + 198, 241, 255, 14, 1);
        int TOut = (int) Math.round(this.entity.TOut * 14 / 100000);
        if (this.entity.TOut <= 100000) drawTexturedModalRect(guiLeft + 173, guiTop + 222, 241, 255, TOut, 1); else drawTexturedModalRect(guiLeft + 173, guiTop + 222, 241, 255, 14, 1);
        int HE3Out = (int) Math.round(this.entity.HE3Out * 14 / 100000);
        if (this.entity.HE3Out <= 100000) drawTexturedModalRect(guiLeft + 197, guiTop + 222, 241, 255, HE3Out, 1); else drawTexturedModalRect(guiLeft + 197, guiTop + 222, 241, 255, 14, 1);
        int HE4Out = (int) Math.round(this.entity.HE4Out * 14 / 100000);
        if (this.entity.HE4Out <= 100000) drawTexturedModalRect(guiLeft + 221, guiTop + 222, 241, 255, HE4Out, 1); else drawTexturedModalRect(guiLeft + 221, guiTop + 222, 241, 255, 14, 1);
        int nOut = (int) Math.round(this.entity.nOut * 14 / 100000);
        if (this.entity.nOut <= 100000) drawTexturedModalRect(guiLeft + 173, guiTop + 198, 241, 255, nOut, 1); else drawTexturedModalRect(guiLeft + 173, guiTop + 198, 241, 255, 14, 1);
    }
}
