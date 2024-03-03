package nc.gui.multiblock.port;

import nc.gui.GuiInfoTile;
import nc.gui.element.GuiItemRenderer;
import nc.network.tile.multiblock.port.ItemPortUpdatePacket;
import nc.tile.fission.port.TileFissionCellPort;
import nc.tile.fission.port.TileFissionCellPort.FissionCellPortContainerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiFissionCellPort extends GuiInfoTile<TileFissionCellPort, ItemPortUpdatePacket, FissionCellPortContainerInfo> {
	
	public GuiFissionCellPort(Container inventory, EntityPlayer player, TileFissionCellPort tile, String textureLocation) {
		super(inventory, player, tile, textureLocation);
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int fontColor = tile.getMultiblock() != null && tile.getMultiblock().isReactorOn ? -1 : 15641088;
		fontRenderer.drawString(guiName.get(), xSize / 2 - nameWidth.get() / 2, 6, fontColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		defaultStateAndBind();
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		new GuiItemRenderer(tile.getFilterStacks().get(0), guiLeft + 44, guiTop + 35, 0.5F).draw();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		
	}
}
