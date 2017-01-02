package nc.gui.accelerator;

import nc.container.accelerator.ContainerSynchrotron;
import nc.tile.accelerator.TileSynchrotron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiSynchrotron extends GuiContainer {
    public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/synchrotron.png");
    
    public TileSynchrotron entity;

    public GuiSynchrotron(InventoryPlayer inventoryPlayer, TileSynchrotron entity) {
        super(new ContainerSynchrotron(inventoryPlayer, entity));
        this.entity = entity;
        this.xSize = 176;
        this.ySize = 203;
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2) {
    	double eff = this.entity.efficiency*0.000001;
        String name1 = (this.entity.length+2) + "x" + (this.entity.length+2) + " " + StatCollector.translateToLocal("gui.synchrotron");
        String name2 = this.entity.problem;
        this.fontRendererObj.drawString((this.entity.complete == 1 ? name1 : name2), 47, 5, (this.entity.complete == 1 ? -1 : 15597568));
        String power = this.entity.energy + " RF";
        this.fontRendererObj.drawString(power, 47, 16, (this.entity.complete == 1 ? -1 : 15597568));
        String fuel =StatCollector.translateToLocal("gui.fuel") + ": " + Math.round(this.entity.fuel/1000) + "%";
        this.fontRendererObj.drawString(fuel, 47, 27, (this.entity.complete == 1 ? -1 : 15597568));
        String efficiency = StatCollector.translateToLocal("gui.efficiency") + ": " + Math.round(this.entity.efficiency*0.0001) + "%";
        this.fontRendererObj.drawString(efficiency, 47, 38, (this.entity.complete == 1 ? -1 : 15597568));
        String particleEnergy = this.entity.particleEnergy*eff < 10000 ? (this.entity.particleEnergy*eff < 10 ? StatCollector.translateToLocal("gui.electronEnergy")+": "+Math.round(this.entity.particleEnergy*eff) + " keV" : StatCollector.translateToLocal("gui.electronEnergy")+": "+Math.round(this.entity.particleEnergy*eff) + " MeV") : StatCollector.translateToLocal("gui.electronEnergy")+": "+Math.round(this.entity.particleEnergy*eff/1000) + " GeV";
        this.fontRendererObj.drawString(particleEnergy, 47, 49, (this.entity.complete == 1 ? -1 : 15597568));
        String percentageOn = StatCollector.translateToLocal("gui.percentageOn")+": "+Math.round(this.entity.percentageOn) + "%";
        this.fontRendererObj.drawString(percentageOn, 47, 60, (this.entity.complete == 1 ? -1 : 15597568));
        
        String rgen = this.entity.radiationPower < 10000 ? StatCollector.translateToLocal("gui.radiationPower")+": "+Math.round(this.entity.radiationPower)+" kW" : (this.entity.radiationPower > 10000000 ? StatCollector.translateToLocal("gui.radiationPower")+": "+((int)(Math.round(this.entity.radiationPower/1000000))) + " GW" : StatCollector.translateToLocal("gui.radiationPower")+": "+((int)(Math.round(this.entity.radiationPower/1000))) + " MW");
        this.fontRendererObj.drawString(rgen, 47, 71, (this.entity.complete == 1 ? -1 : 15597568));
        
        String anti = StatCollector.translateToLocal("gui.antimatterProgress") + ": " + (int) ((this.entity.antimatter)/64) + " ppm";
        this.fontRendererObj.drawString(anti, 47, 82, (this.entity.complete == 1 ? -1 : 15597568));
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int k = this.entity.energy * 109 / 1000000;
        this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 6 + 109 - k, 176, 3 + 109 - k + (this.entity.complete == 1 ? 0 : 109), 5, k);
        int k2 = (int) (this.entity.fuel * 109 / 100000);
        this.drawTexturedModalRect(this.guiLeft + 18, this.guiTop + 6 + 109 - k2, 181, 3 + 109 - k2 + (this.entity.complete == 1 ? 0 : 109), 5, k2);
        int k3 = (int) (this.entity.efficiency/1000 * 109 / 1000);
        this.drawTexturedModalRect(this.guiLeft + 28, this.guiTop + 6 + 109 - k3, 186, 3 + 109 - k3 + (this.entity.complete == 1 ? 0 : 109), 5, k3);
        int k4 = (int) (this.entity.percentageOn * 109 / 100);
        this.drawTexturedModalRect(this.guiLeft + 38, this.guiTop + 6 + 109 - k4, 191, 3 + 109 - k4 + (this.entity.complete == 1 ? 0 : 109), 5, k4);
        
        int antimatterGUI = (int) Math.round(this.entity.antimatter * 14 / 64000000);
        if (this.entity.antimatter <= 64000000) drawTexturedModalRect(guiLeft + 153, guiTop + 117, 176, 2, antimatterGUI, 1); else drawTexturedModalRect(guiLeft + 153, guiTop + 117, 176, 2, 14, 1);
    }
}
