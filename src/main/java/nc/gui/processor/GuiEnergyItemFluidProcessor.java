package nc.gui.processor;

import nc.Global;
import nc.container.processor.ContainerEnergyItemFluidProcessor;
import nc.tile.processor.TileEnergyItemFluidProcessor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public abstract class GuiEnergyItemFluidProcessor extends GuiContainer {
	
	public static int tick;
	
	private final InventoryPlayer playerInventory;
	protected TileEnergyItemFluidProcessor tile;
	protected final ResourceLocation gui_textures;

	public GuiEnergyItemFluidProcessor(String name, EntityPlayer player, ContainerEnergyItemFluidProcessor inv) {
		super(inv);
		playerInventory = player.inventory;
		gui_textures = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + name + ".png");
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tile.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		//fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(gui_textures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		tick++;
		tick %= 10;
	}
	
	protected int getCookProgressScaled(double pixels) {
		double i = tile.getField(0);
		double j = tile.baseProcessTime;
		return j != 0D ? (int) Math.round(i * pixels / j) : 0;
	}
}
