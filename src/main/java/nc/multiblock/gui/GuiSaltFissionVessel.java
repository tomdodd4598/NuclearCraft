package nc.multiblock.gui;

import java.util.List;

import com.google.common.collect.Lists;

import nc.Global;
import nc.gui.NCGui;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.NCButton;
import nc.multiblock.container.ContainerSaltFissionVessel;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
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

public class GuiSaltFissionVessel extends NCGui {
	
	protected final TileSaltFissionVessel vessel;
	protected final ResourceLocation gui_textures;

	public GuiSaltFissionVessel(EntityPlayer player, TileSaltFissionVessel vessel) {
		super(new ContainerSaltFissionVessel(player, vessel));
		this.vessel = vessel;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "salt_fission_vessel" + ".png");
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawTooltip(vessel.clusterHeatCapacity >= 0L ? heatInfo() : noClusterInfo(), mouseX, mouseY, 8, 6, 16, 74);
		
		drawFilteredFluidTooltip(vessel.getTanks().get(0), vessel.getFilterTanks().get(0), mouseX, mouseY, 56, 35, 16, 16);
		drawFilteredFluidTooltip(vessel.getTanks().get(1), vessel.getFilterTanks().get(1), mouseX, mouseY, 112, 31, 24, 24);
	}
	
	public List<String> heatInfo() {
		String heat = UnitHelper.prefix(vessel.clusterHeatStored, vessel.clusterHeatCapacity, 5, "H");
		return Lists.newArrayList(TextFormatting.YELLOW + Lang.localise("gui.nc.container.salt_fission_vessel.heat_stored") + TextFormatting.WHITE + " " + heat);
	}
	
	public List<String> noClusterInfo() {
		return Lists.newArrayList(TextFormatting.RED + Lang.localise("gui.nc.container.no_cluster"));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = vessel.getMultiblock() != null && vessel.getMultiblock().isReactorOn ? -1 : 15641088;
		String s = Lang.localise("gui.nc.container.salt_fission_vessel.vessel");
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		Tank filterTank = vessel.getFilterTanks().get(0);
		if (!filterTank.isEmpty()) {
			GuiFluidRenderer.renderGuiTank(filterTank.getFluid(), 1000, 1000, guiLeft + 56, guiTop + 35, zLevel, 16, 16, 127);
		}
		mc.getTextureManager().bindTexture(gui_textures);
		
		if (vessel.clusterHeatCapacity >= 0L) {
			int e = (int) Math.round(74D*vessel.clusterHeatStored/vessel.clusterHeatCapacity);
			drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
		}
		else drawGradientRect(guiLeft + 8, guiTop + 6, guiLeft + 8 + 16, guiTop + 6 + 74, 0xFF777777, 0xFF535353);
		
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, getCookProgressScaled(37), 16);
		
		GuiFluidRenderer.renderGuiTank(vessel.getTanks().get(0), guiLeft + 56, guiTop + 35, zLevel, 16, 16);
		GuiFluidRenderer.renderGuiTank(vessel.getTanks().get(1), guiLeft + 112, guiTop + 31, zLevel, 24, 24);
	}
	
	protected int getCookProgressScaled(int pixels) {
		if (vessel.baseProcessTime/vessel.getSpeedMultiplier() < 4D) {
			return vessel.isProcessing ? pixels : 0;
		}
		double i = vessel.time, j = vessel.baseProcessTime;
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
		if (vessel.getWorld().isRemote) {
			for (int i = 0; i < 2; i++) if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
				PacketHandler.instance.sendToServer(vessel.getTanks().get(i).isEmpty() ? new EmptyFilterTankPacket(vessel, i) : new EmptyTankPacket(vessel, i));
				return;
			}
		}
	}
}
