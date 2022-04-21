package nc.gui;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import nc.container.slot.SlotFiltered;
import nc.gui.element.NCButton;
import nc.tile.energy.ITileEnergy;
import nc.tile.internal.fluid.Tank;
import nc.util.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public abstract class NCGui extends GuiContainer {
	
	public NCGui(Container inventory) {
		super(inventory);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		renderTooltips(mouseX, mouseY);
	}
	
	@Override
	protected void renderHoveredToolTip(int x, int y) {
		Slot slot = getSlotUnderMouse();
		if (slot != null && mc.player.inventory.getItemStack().isEmpty()) {
			if (slot instanceof SlotFiltered && ((SlotFiltered) slot).hasStackForRender()) {
				renderToolTip(((SlotFiltered) slot).getStackForRender(), x, y);
			}
			else if (slot.getHasStack()) {
				renderToolTip(slot.getStack(), x, y);
			}
		}
	}
	
	public void renderTooltips(int mouseX, int mouseY) {}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 1 || mouseButton == 2) {
			for (int i = 0; i < buttonList.size(); ++i) {
				GuiButton guibutton = buttonList.get(i);
				boolean mousePressed = guibutton instanceof NCButton ? ((NCButton) guibutton).mousePressed(mc, mouseX, mouseY, mouseButton) : false;
				if (mousePressed) {
					GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, buttonList);
					if (MinecraftForge.EVENT_BUS.post(event)) {
						break;
					}
					guibutton = event.getButton();
					selectedButton = guibutton;
					float soundPitch = 1F;
					if (mouseButton == 1) {
						actionPerformedRight(guibutton);
						soundPitch = SoundHelper.getPitch(1D);
					}
					else if (mouseButton == 2) {
						actionPerformedMiddle(guibutton);
					}
					mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, soundPitch));
					if (equals(mc.currentScreen)) {
						MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), buttonList));
					}
				}
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	protected void actionPerformedRight(GuiButton guiButton) {}
	
	protected void actionPerformedMiddle(GuiButton guiButton) {}
	
	protected boolean isEscapeKeyDown(int keyCode) {
		return keyCode == 1 || mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode);
	}
	
	protected void drawTooltip(List<String> text, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
		int xPos = x + guiLeft;
		int yPos = y + guiTop;
		if (mouseX >= xPos && mouseY >= yPos && mouseX < xPos + tooltipWidth && mouseY < yPos + tooltipHeight) {
			drawHoveringText(text, mouseX, mouseY);
		}
	}
	
	protected void drawTooltip(String text, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
		drawTooltip(Lists.newArrayList(text), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
	}
	
	protected List<String> fluidInfo(Tank tank) {
		String fluidName = tank.getFluidLocalizedName();
		String fluidAmount = UnitHelper.prefix(tank.getFluidAmount(), tank.getCapacity(), 5, "B", -1);
		return Lists.newArrayList(TextFormatting.GREEN + fluidName + TextFormatting.WHITE + " [" + fluidAmount + "]", TextFormatting.ITALIC + Lang.localise("gui.nc.container.shift_clear_tank"));
	}
	
	protected List<String> fluidFilterInfo(Tank tank) {
		String fluidName = tank.getFluidLocalizedName();
		return Lists.newArrayList(TextFormatting.GREEN + fluidName + TextFormatting.WHITE, TextFormatting.ITALIC + Lang.localise("gui.nc.container.shift_clear_filter"));
	}
	
	protected void drawFluidTooltip(Tank tank, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
		if (!tank.isEmpty()) {
			drawTooltip(fluidInfo(tank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
		}
	}
	
	protected void drawFilteredFluidTooltip(Tank tank, Tank filterTank, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
		if (tank.isEmpty()) {
			if (!filterTank.isEmpty()) {
				drawTooltip(fluidFilterInfo(filterTank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
			}
		}
		else {
			drawTooltip(fluidInfo(tank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
		}
	}
	
	protected List<String> energyInfo(ITileEnergy tile) {
		String energy = UnitHelper.prefix(tile.getEnergyStorage().getEnergyStored(), tile.getEnergyStorage().getMaxEnergyStored(), 5, "RF");
		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + Lang.localise("gui.container.energy_stored") + TextFormatting.WHITE + " " + energy);
	}
	
	protected List<String> noEnergyInfo() {
		return Lists.newArrayList(TextFormatting.RED + Lang.localise("gui.nc.container.no_energy"));
	}
	
	protected void drawEnergyTooltip(ITileEnergy tile, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
		drawTooltip(energyInfo(tile), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
	}
	
	protected void drawNoEnergyTooltip(int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
		drawTooltip(noEnergyInfo(), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
	}
	
	protected int width(String string) {
		return fontRenderer.getStringWidth(string);
	}
}
