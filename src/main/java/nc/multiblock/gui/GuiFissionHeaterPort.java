package nc.multiblock.gui;

import nc.Global;
import nc.gui.NCGui;
import nc.gui.element.GuiFluidRenderer;
import nc.gui.element.NCButton;
import nc.multiblock.container.ContainerFissionHeaterPort;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import nc.network.PacketHandler;
import nc.network.gui.EmptyFilterTankPacket;
import nc.network.gui.EmptyTankPacket;
import nc.tile.internal.fluid.Tank;
import nc.util.NCUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFissionHeaterPort extends NCGui {
	
	protected final TileFissionHeaterPort port;
	protected final ResourceLocation gui_textures;

	public GuiFissionHeaterPort(EntityPlayer player, TileFissionHeaterPort port) {
		super(new ContainerFissionHeaterPort(player, port));
		this.port = port;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fission_heater_port" + ".png");
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		drawFilteredFluidTooltip(port.getTanks().get(0), port.getFilterTanks().get(0), mouseX, mouseY, 40, 31, 24, 24);
		drawFilteredFluidTooltip(port.getTanks().get(1), port.getFilterTanks().get(1), mouseX, mouseY, 112, 31, 24, 24);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = port.getMultiblock() != null && port.getMultiblock().isReactorOn ? -1 : 15641088;
		String s = port.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		Tank filterTank = port.getFilterTanks().get(0);
		if (!filterTank.isEmpty()) {
			GuiFluidRenderer.renderGuiTank(filterTank.getFluid(), 1000, 1000, guiLeft + 40, guiTop + 31, zLevel, 24, 24, 127);
		}
		mc.getTextureManager().bindTexture(gui_textures);
		
		GuiFluidRenderer.renderGuiTank(port.getTanks().get(0), guiLeft + 40, guiTop + 31, zLevel, 24, 24);
		GuiFluidRenderer.renderGuiTank(port.getTanks().get(1), guiLeft + 112, guiTop + 31, zLevel, 24, 24);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		buttonList.add(new NCButton.EmptyTank(0, guiLeft + 40, guiTop + 31, 24, 24));
		buttonList.add(new NCButton.EmptyTank(1, guiLeft + 112, guiTop + 31, 24, 24));
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (port.getWorld().isRemote) {
			for (int i = 0; i < 2; i++) if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
				PacketHandler.instance.sendToServer(port.getTanks().get(i).isEmpty() ? new EmptyFilterTankPacket(port, i) : new EmptyTankPacket(port, i));
				return;
			}
		}
	}
}
