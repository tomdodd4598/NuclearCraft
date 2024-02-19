package nc.gui.multiblock;

import java.util.List;

import com.google.common.collect.Lists;

import nc.gui.processor.GuiFilteredProcessor;
import nc.network.tile.multiblock.FissionIrradiatorUpdatePacket;
import nc.tile.fission.TileFissionIrradiator;
import nc.tile.fission.TileFissionIrradiator.FissionIrradiatorContainerInfo;
import nc.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.TextFormatting;

public class GuiFissionIrradiator extends GuiFilteredProcessor<TileFissionIrradiator, FissionIrradiatorUpdatePacket, FissionIrradiatorContainerInfo> {
	
	public GuiFissionIrradiator(Container inventory, EntityPlayer player, TileFissionIrradiator tile, String textureLocation) {
		super(inventory, player, tile, textureLocation);
	}
	
	@Override
	protected void initConfigButtons() {}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.getMultiblock() != null && tile.getMultiblock().isReactorOn ? -1 : 15641088;
		fontRenderer.drawString(guiName.get(), xSize / 2 - nameWidth.get() / 2, 6, fontColor);
	}
	
	@Override
	protected void drawBars() {
		drawProgressBar();
		
		if (tile.clusterHeatCapacity >= 0L) {
			int e = (int) Math.round(74D * tile.clusterHeatStored / tile.clusterHeatCapacity);
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
		}
		else {
			drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 74, 0xFF777777, 0xFF535353);
		}
	}
	
	@Override
	protected boolean configButtonActionPerformed(GuiButton button) {
		return false;
	}
	
	@Override
	protected void renderBarTooltips(int mouseX, int mouseY) {
		drawTooltip(tile.clusterHeatCapacity >= 0L ? heatInfo() : noClusterInfo(), mouseX, mouseY, 8, 6, 16, 74);
	}
	
	public List<String> heatInfo() {
		String heat = UnitHelper.prefix(tile.clusterHeatStored, tile.clusterHeatCapacity, 5, "H");
		return Lists.newArrayList(TextFormatting.YELLOW + Lang.localize("gui.nc.container.fission_tile.heat_stored") + TextFormatting.WHITE + " " + heat);
	}
	
	@Override
	protected void renderConfigButtonTooltips(int mouseX, int mouseY) {}
}
