package nc.gui.processor;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.container.processor.ContainerEnergyItemFluidProcessor;
import nc.gui.NCGui;
import nc.gui.NCGuiButton.Button;
import nc.tile.energy.ITileEnergy;
import nc.tile.processor.TileEnergyItemFluidProcessor;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public abstract class GuiEnergyItemFluidProcessor extends NCGui {
	
	public static int tick;
	
	private final InventoryPlayer playerInventory;
	protected TileEnergyItemFluidProcessor tile;
	protected final ResourceLocation gui_textures;

	public GuiEnergyItemFluidProcessor(String name, EntityPlayer player, ContainerEnergyItemFluidProcessor inv) {
		super(inv);
		playerInventory = player.inventory;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + name + ".png");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tile.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
	}
	
	@Override
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
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (tile.getWorld().isRemote) {
			if (guiButton != null) if (guiButton instanceof Button) {
				
			}
		}
	}
	
	@Override
	public void drawEnergyTooltip(ITileEnergy tile, int mouseX, int mouseY, int x, int y, int width, int height) {
		if (this.tile.baseProcessPower != 0) super.drawEnergyTooltip(tile, mouseX, mouseY, x, y, width, height);
		else drawNoEnergyTooltip(mouseX, mouseY, x, y, width, height);
	}
	
	@Override
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = UnitHelper.prefix(tile.getStorage().getEnergyStored(), tile.getStorage().getMaxEnergyStored(), 5, "RF");
		String power = UnitHelper.prefix(this.tile.getProcessPower(), 5, "RF/t");
		String speedMultiplier = this.tile.getSpeedMultiplier()*100 + "%";
		String powerMultiplier = this.tile.getSpeedMultiplier()*(this.tile.getSpeedMultiplier() + 1)*50 + "%";
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy, TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.process_power") + TextFormatting.WHITE + " " + power, TextFormatting.AQUA + Lang.localise("gui.container.speed_multiplier") + TextFormatting.WHITE + " " + speedMultiplier, TextFormatting.AQUA + Lang.localise("gui.container.power_multiplier") + TextFormatting.WHITE + " " + powerMultiplier);
	}
}
