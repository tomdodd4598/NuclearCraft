package nc.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.fluid.Tank;
import nc.tile.energy.ITileEnergy;
import nc.util.NCMath;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;

public abstract class GuiNC extends GuiContainer {

	public GuiNC(Container inventory) {
		super(inventory);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	public void drawTooltip(List<String> text, int mouseX, int mouseY, int x, int y, int width, int height) {
		int xPos = x + guiLeft; int yPos = y + guiTop;
		if (mouseX >= xPos && mouseY >= yPos && mouseX < xPos + width && mouseY < yPos + height) {
			drawHoveringText(text, mouseX - guiLeft, mouseY - guiTop);
		}
	}
	
	public void drawTooltip(String text, int mouseX, int mouseY, int x, int y, int width, int height) {
		List<String> stringList = Lists.newArrayList(text);
		drawTooltip(stringList, mouseX, mouseY, x, y, width, height);
	}
	
	public List<String> fluidInfo(FluidStack fluid, Tank tank) {
		String fluidName = fluid.getLocalizedName();
		String fluidAmount = NCMath.prefix(fluid.amount, tank.getCapacity(), 5, "B", -1);
		return Lists.newArrayList(TextFormatting.GREEN + fluidName + TextFormatting.WHITE + " [" + fluidAmount + "]", TextFormatting.ITALIC + I18n.translateToLocalFormatted("gui.container.shift_clear_tank"));
	}
	
	public void drawFluidTooltip(FluidStack fluid, Tank tank, int mouseX, int mouseY, int x, int y, int width, int height) {
		if (fluid == null) return;
		if (fluid.amount <= 0) return;
		drawTooltip(fluidInfo(fluid, tank), mouseX, mouseY, x, y, width, height + 1);
	}
	
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = NCMath.prefix(tile.getStorage().getEnergyStored(), tile.getStorage().getMaxEnergyStored(), 5, "RF");
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + I18n.translateToLocalFormatted("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy);
	}
	
	public List<String> noEnergyInfo() {
		return Lists.newArrayList(TextFormatting.RED + I18n.translateToLocalFormatted("gui.container.no_energy"));
	}
	
	public void drawEnergyTooltip(ITileEnergy tile, int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(energyInfo(tile), mouseX, mouseY, x, y, width, height);
	}
	
	public void drawNoEnergyTooltip(int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(noEnergyInfo(), mouseX, mouseY, x, y, width, height);
	}
}
