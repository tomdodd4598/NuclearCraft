package com.nr.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.nr.mod.blocks.tileentities.TileEntityFissionReactorGraphite;
import com.nr.mod.container.ContainerFissionReactorGraphite;

public class GuiFissionReactorGraphite extends GuiContainer {
    public static final ResourceLocation bground = new ResourceLocation("nr:textures/gui/fissionReactorGraphite.png");
    
    public TileEntityFissionReactorGraphite entity;

    public GuiFissionReactorGraphite(InventoryPlayer inventoryPlayer, TileEntityFissionReactorGraphite entity) {
        super(new ContainerFissionReactorGraphite(inventoryPlayer, entity));
        this.entity = entity;
        this.xSize = 176;
        this.ySize = 166;
    }

    public void drawGuiContainerForegroundLayer(int par1, int par2) {
        String name1 = (this.entity.D-2) + "x" + (this.entity.D-2) + "x" + (this.entity.D-2) + " " + StatCollector.translateToLocal("gui.reactor");
        String name2 = this.entity.problem;
        this.fontRendererObj.drawString((this.entity.multiblockstring() ? name1 : name2), 67, 5, (this.entity.multiblockstring() ? -1 : 15597568));
        String power = this.entity.energy + " RF";
        this.fontRendererObj.drawString(power, 67, 16, (this.entity.multiblockstring() ? -1 : 15597568));
        String fueltime = ((int) this.entity.fueltime*100/72)+" "+StatCollector.translateToLocal("gui.fuel");
        this.fontRendererObj.drawString(fueltime, 67, 27, (this.entity.multiblockstring() ? -1 : 15597568));
        String heat = (this.entity.heat)+" "+StatCollector.translateToLocal("gui.heat");
        this.fontRendererObj.drawString(heat, 67, 38, (this.entity.multiblockstring() ? -1 : 15597568));
        String fueltype = (this.entity.fueltime == 0 ? StatCollector.translateToLocal("gui.noFuel") : this.entity.typeoffuel)+"";
        this.fontRendererObj.drawString(fueltype, 67, 49, (this.entity.multiblockstring() ? -1 : 15597568));
        String egen = this.entity.fueltime == 0 ? 0 + " RF/t" : (this.entity.E < 100000 ? (this.entity.E)+" RF/t" : ((int)(Math.round(this.entity.E/1000))) + " kRF/t");
        this.fontRendererObj.drawString(egen, 7, 63, (!this.entity.multiblockstring() ? 15597568 : (this.entity.off==1 ? 15641088 : -1)));
        String hgen = this.entity.H < 100000 ? (this.entity.H)+" H/t" : ((int)(Math.round(this.entity.H/1000))) + " kH/t";
        this.fontRendererObj.drawString(hgen, 7, 74, (!this.entity.multiblockstring() ? 15597568 : (this.entity.off==1 ? 15641088 : -1)));
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int k = this.entity.energy * 55 / 1000000;
        this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 6 + 55 - k, 176, 3 + 55 - k + (this.entity.multiblockstring() ? 0 : 55), 16, k);
        int k2 = this.entity.fueltime * 55 / 7200000;
        this.drawTexturedModalRect(this.guiLeft + 27, this.guiTop + 6 + 55 - k2, 192, 3 + 55 - k2 + (this.entity.multiblockstring() ? 0 : 55), 16, k2);
        int k3 = this.entity.heat * 55 / 1000000;
        this.drawTexturedModalRect(this.guiLeft + 46, this.guiTop + 6 + 55 - k3, 208, 3 + 55 - k3 + (this.entity.multiblockstring() ? 0 : 55), 16, k3);
        int k4 = ((this.entity.fueltime != 0) ? (56 - (this.entity.fueltime * 56 / 7200000)) : 0);
		drawTexturedModalRect(guiLeft + 86, guiTop + 54, 3 + (this.entity.multiblockstring() ? 0 : 56), 166, k4, 27);
    }
}
