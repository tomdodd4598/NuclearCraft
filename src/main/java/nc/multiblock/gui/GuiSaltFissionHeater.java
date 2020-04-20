package nc.multiblock.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.gui.NCGui;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.NCButton;
import nc.multiblock.container.ContainerSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.network.PacketHandler;
import nc.network.gui.EmptyFilterTankPacket;
import nc.network.gui.EmptyTankPacket;
import nc.tile.internal.fluid.Tank;
import nc.util.Lang;
import nc.util.NCUtil;
import nc.util.UnitHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiSaltFissionHeater extends NCGui {
	
	protected final TileSaltFissionHeater heater;
	protected final ResourceLocation gui_textures;

	public GuiSaltFissionHeater(EntityPlayer player, TileSaltFissionHeater heater) {
		super(new ContainerSaltFissionHeater(player, heater));
		this.heater = heater;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "salt_fission_heater" + ".png");
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawTooltip(heater.clusterHeatCapacity >= 0L ? heatInfo() : noClusterInfo(), mouseX, mouseY, 8, 6, 16, 74);
		
		drawFilteredFluidTooltip(heater.getTanks().get(0), heater.getFilterTanks().get(0), mouseX, mouseY, 56, 35, 16, 16);
		drawFilteredFluidTooltip(heater.getTanks().get(1), heater.getFilterTanks().get(1), mouseX, mouseY, 112, 31, 24, 24);
	}
	
	public List<String> heatInfo() {
		String heat = UnitHelper.prefix(heater.clusterHeatStored, heater.clusterHeatCapacity, 5, "H");
		return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.nc.container.salt_fission_heater.heat_stored") + TextFormatting.WHITE + " " + heat);
	}
	
	public List<String> noClusterInfo() {
		return Lists.newArrayList(TextFormatting.RED + Lang.localise("gui.nc.container.no_cluster"));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = heater.getMultiblock() != null && heater.getMultiblock().isReactorOn ? -1 : 15641088;
		String s = Lang.localise("gui.nc.container.salt_fission_heater.heater");
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		Tank filterTank = heater.getFilterTanks().get(0);
		if (!filterTank.isEmpty()) {
			GuiFluidRenderer.renderGuiTank(filterTank.getFluid(), 1000, 1000, guiLeft + 56, guiTop + 35, zLevel, 16, 16, 127);
		}
		mc.getTextureManager().bindTexture(gui_textures);
		
		if (heater.clusterHeatCapacity >= 0L) {
			int e = (int) Math.round(74D*heater.clusterHeatStored/heater.clusterHeatCapacity);
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
		}
		else drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 74, 0xFF777777, 0xFF535353);
		
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, getCookProgressScaled(37), 16);
		
		GuiFluidRenderer.renderGuiTank(heater.getTanks().get(0), guiLeft + 56, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(heater.getTanks().get(1), guiLeft + 112, guiTop + 31, zLevel, 24, 24);
	}
	
	protected int getCookProgressScaled(int pixels) {
		if (1D/heater.getSpeedMultiplier() < 4D) {
			return heater.isProcessing ? pixels : 0;
		}
		double i = heater.time, j = 1D;
		return j != 0D ? (int) Math.round(i * pixels / j) : 0;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		buttonList.add(new NCButton.EmptyTank(0, guiLeft + 56, guiTop + 35, 16, 16));
		buttonList.add(new NCButton.EmptyTank(1, guiLeft + 112, guiTop + 31, 24, 24));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (heater.getWorld().isRemote) {
			for (int i = 0; i < 2; i++) if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
				PacketHandler.instance.sendToServer(heater.getTanks().get(i).isEmpty() ? new EmptyFilterTankPacket(heater, i) : new EmptyTankPacket(heater, i));
				return;
			}
		}
	}
}
