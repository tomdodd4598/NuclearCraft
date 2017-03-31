package nc.gui.energy.generator;

import nc.Global;
import nc.container.energy.generator.ContainerFissionController;
import nc.tile.energy.generator.TileFissionController;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFissionController extends GuiContainer {
	
	private final InventoryPlayer playerInventory;
	protected TileFissionController tile;
	protected final ResourceLocation gui_textures;

	public GuiFissionController(EntityPlayer player, TileFissionController tile) {
		super(new ContainerFissionController(player, tile));
		playerInventory = player.inventory;
		this.tile = tile;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + "fission_controller" + ".png");
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tile.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		//fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
		fontRendererObj.drawString(tile.storage.getEnergyStored() + " RF", 28, ySize - 94, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		double e = Math.round(((double) tile.storage.getEnergyStored()) / ((double) tile.storage.getMaxEnergyStored()) * 74);
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 74 - (int) e, 176, 90 + 74 - (int) e, 16, (int) e);
		
		int k = getCookProgressScaled(37);
		drawTexturedModalRect(guiLeft + 74, guiTop + 35, 176, 3, k, 16);
	}
	
	protected int getCookProgressScaled(double pixels) {
		double i = tile.getField(0);
		double j = tile.getProcessTime();
		return j != 0D ? (int) Math.round(i * pixels / j) : 0;
	}
}
