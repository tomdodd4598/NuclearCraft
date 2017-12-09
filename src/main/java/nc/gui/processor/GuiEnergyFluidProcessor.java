package nc.gui.processor;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.container.processor.ContainerEnergyFluidProcessor;
import nc.gui.GuiNC;
import nc.gui.NCGuiButton.Button;
import nc.tile.energy.ITileEnergy;
import nc.tile.processor.TileEnergyFluidProcessor;
import nc.util.NCMath;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public abstract class GuiEnergyFluidProcessor extends GuiNC {
	
	public static int tick;
	
	private final InventoryPlayer playerInventory;
	protected TileEnergyFluidProcessor tile;
	protected final ResourceLocation gui_textures;

	public GuiEnergyFluidProcessor(String name, EntityPlayer player, ContainerEnergyFluidProcessor inv) {
		super(inv);
		playerInventory = player.inventory;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + name + ".png");
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tile.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		//fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		tick++;
		tick %= 10;
	}
	
	protected int getCookProgressScaled(double pixels) {
		double i = tile.getField(0);
		double j = tile.baseProcessTime;
		return j != 0D ? (int) Math.round(i * pixels / j) : 0;
	}
	
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			if (guiButton != null) if (guiButton instanceof Button) {
				
			}
		}
	}
	
	public void drawEnergyTooltip(ITileEnergy tile, int mouseX, int mouseY, int x, int y, int width, int height) {
		if (this.tile.baseProcessPower != 0) super.drawEnergyTooltip(tile, mouseX, mouseY, x, y, width, height);
		else drawNoEnergyTooltip(mouseX, mouseY, x, y, width, height);
	}
	
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = NCMath.prefix(tile.getStorage().getEnergyStored(), tile.getStorage().getMaxEnergyStored(), 5, "RF");
		String power = NCMath.prefix(this.tile.getProcessPower(), 5, "RF/t");
		String speedMultiplier = this.tile.getSpeedMultiplier()*100 + "%";
		String powerMultiplier = this.tile.getSpeedMultiplier()*(this.tile.getSpeedMultiplier() + 1)*50 + "%";
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + I18n.translateToLocalFormatted("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy, TextFormatting.LIGHT_PURPLE + I18n.translateToLocalFormatted("gui.container.process_power") + TextFormatting.WHITE + " " + power, TextFormatting.AQUA + I18n.translateToLocalFormatted("gui.container.speed_multiplier") + TextFormatting.WHITE + " " + speedMultiplier, TextFormatting.AQUA + I18n.translateToLocalFormatted("gui.container.power_multiplier") + TextFormatting.WHITE + " " + powerMultiplier);
	}
}
