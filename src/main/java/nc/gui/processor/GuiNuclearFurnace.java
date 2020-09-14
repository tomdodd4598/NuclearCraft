package nc.gui.processor;

import nc.Global;
import nc.container.processor.ContainerNuclearFurnace;
import nc.gui.NCGui;
import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;

public class GuiNuclearFurnace extends NCGui {
	
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/nuclear_furnace.png");
	
	private final InventoryPlayer playerInventory;
	private final TileNuclearFurnace furnace;
	
	public GuiNuclearFurnace(EntityPlayer player, TileNuclearFurnace furnace) {
		super(new ContainerNuclearFurnace(player, furnace));
		playerInventory = player.inventory;
		this.furnace = furnace;
	}
	
	/** Draw the foreground layer for the GuiContainer (everything in front of the items) */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = furnace.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	/** Draws the background layer of this container (behind the items) */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (TileNuclearFurnace.isBurning(furnace)) {
			int k = getBurnLeftScaled(13);
			drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		int l = getCookProgressScaled(24);
		drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}
	
	private int getCookProgressScaled(int pixels) {
		int i = furnace.getField(2);
		int j = furnace.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	private int getBurnLeftScaled(int pixels) {
		int i = furnace.getField(1);
		
		if (i == 0) {
			i = furnace.getCookTime();
		}
		
		return furnace.getField(0) * pixels / i;
	}
}
