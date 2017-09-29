package nc.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.tile.energy.ITileEnergy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;

public abstract class GuiNC extends GuiContainer {

	public GuiNC(Container inventory) {
		super(inventory);
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
	
	public List<String> fluidInfo(FluidStack fluid) {
		String fluidName = fluid.getLocalizedName();
		return Lists.newArrayList(TextFormatting.GREEN + fluidName, TextFormatting.WHITE + (fluid.amount + " mB"), TextFormatting.ITALIC + I18n.translateToLocalFormatted("gui.container.shift_clear_tank"));
	}
	
	public void drawFluidTooltip(FluidStack fluid, int mouseX, int mouseY, int x, int y, int width, int height) {
		if (fluid == null) return;
		if (fluid.amount <= 0) return;
		drawTooltip(fluidInfo(fluid), mouseX, mouseY, x, y, width, height + 1);
	}
	
	public List<String> energyInfo(ITileEnergy tile) {
		String energy = tile.getStorage().getEnergyStored() + " RF";
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + I18n.translateToLocalFormatted("gui.container.energy_stored"), TextFormatting.WHITE + energy);
	}
	
	public void drawEnergyTooltip(ITileEnergy tile, int mouseX, int mouseY, int x, int y, int width, int height) {
		drawTooltip(energyInfo(tile), mouseX, mouseY, x, y, width, height);
	}
}
