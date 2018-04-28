package nc.multiblock.fission.moltensalt.gui;

import nc.Global;
import nc.gui.NCGui;
import nc.multiblock.fission.moltensalt.tile.SaltFissionReactor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiSaltFissionController extends NCGui {
	
	SaltFissionReactor controller;
	
	private final ResourceLocation gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "salt_fission_controller" + ".png");
	
	public GuiSaltFissionController(SaltFissionReactor controller, Container container) {
		super(container);
		this.controller = controller;
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
