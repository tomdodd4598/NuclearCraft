package nc.gui.processor;

import nc.Global;
import nc.container.processor.ContainerNuclearFurnace;
import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiNuclearFurnace extends GuiContainer {
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/nuclear_furnace.png");
	/** The player inventory bound to this GUI */
	private final InventoryPlayer playerInventory;
	private final IInventory tileFurnace;
	
	public GuiNuclearFurnace(EntityPlayer player, IInventory furnaceInv) {
		super(new ContainerNuclearFurnace(player, furnaceInv));
		playerInventory = player.inventory;
		tileFurnace = furnaceInv;
	}
	
	/** Draw the foreground layer for the GuiContainer (everything in front of the items) */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tileFurnace.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	/** Draws the background layer of this container (behind the items) */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (TileNuclearFurnace.isBurning(tileFurnace)) {
			int k = getBurnLeftScaled(13);
			drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		int l = getCookProgressScaled(24);
		drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}

	private int getCookProgressScaled(int pixels) {
		int i = tileFurnace.getField(2);
		int j = tileFurnace.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getBurnLeftScaled(int pixels) {
		int i = tileFurnace.getField(1);
		
		if (i == 0) {
			i = ((TileNuclearFurnace) tileFurnace).getCookTime();
		}
		
		return tileFurnace.getField(0) * pixels / i;
	}
}
