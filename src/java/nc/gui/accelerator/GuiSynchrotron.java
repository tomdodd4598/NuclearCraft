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
        this.ySize = 166;
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2) {
        String name1 = (this.entity.length+2) + "x" + (this.entity.length+2) + " " + StatCollector.translateToLocal("gui.synchrotron");
        String name2 = this.entity.problem;
        this.fontRendererObj.drawString((this.entity.multiblockstring() ? name1 : name2), 67, 5, (this.entity.multiblockstring() ? -1 : 15597568));
        String power = this.entity.energy + " RF";
        this.fontRendererObj.drawString(power, 67, 16, (this.entity.multiblockstring() ? -1 : 15597568));
        String fuel =StatCollector.translateToLocal("gui.fuel") + ": " + (this.entity.fuel) + "%";
        this.fontRendererObj.drawString(fuel, 67, 27, (this.entity.multiblockstring() ? -1 : 15597568));
        String efficiency = StatCollector.translateToLocal("gui.efficiency") + ": " + (this.entity.efficiency) + "%";
        this.fontRendererObj.drawString(efficiency, 67, 38, (this.entity.multiblockstring() ? -1 : 15597568));
        String particleEnergy = StatCollector.translateToLocal("gui.electronEnergy")+": "+(this.entity.particleEnergy) + " GeV";
        this.fontRendererObj.drawString(particleEnergy, 67, 49, (this.entity.multiblockstring() ? -1 : 15597568));
        String percentageOn = StatCollector.translateToLocal("gui.percentageOn")+": "+(this.entity.percentageOn) + "%";
        this.fontRendererObj.drawString(percentageOn, 67, 60, (this.entity.multiblockstring() ? -1 : 15597568));
        
        String rgen = this.entity.radiationPower < 100000 ? (this.entity.radiationPower)+" kW" : ((int)(Math.round(this.entity.radiationPower/1000))) + " MW";
        this.fontRendererObj.drawString(rgen, 7, 74, (this.entity.multiblockstring() ? -1 : 15597568));
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int k = this.entity.energy * 55 / 25000000;
        this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 6 + 55 - k, 176, 3 + 55 - k + (this.entity.multiblockstring() ? 0 : 55), 16, k);
        int k2 = (int) (this.entity.fuel * 55 / 7200000);
        this.drawTexturedModalRect(this.guiLeft + 27, this.guiTop + 6 + 55 - k2, 192, 3 + 55 - k2 + (this.entity.multiblockstring() ? 0 : 55), 16, k2);
        int k3 = (int) (this.entity.efficiency * 55 / 1000000);
        this.drawTexturedModalRect(this.guiLeft + 46, this.guiTop + 6 + 55 - k3, 208, 3 + 55 - k3 + (this.entity.multiblockstring() ? 0 : 55), 16, k3);
        int k4 = (int) ((this.entity.percentageOn != 0) ? (56 - (this.entity.percentageOn * 56 / 7200000)) : 0);
		drawTexturedModalRect(guiLeft + 86, guiTop + 54, 3 + (this.entity.multiblockstring() ? 0 : 56), 166, k4, 27);
    }
}
