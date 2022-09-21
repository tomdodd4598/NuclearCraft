package nc.gui.processor;

import java.util.List;

import nc.gui.NCGui;
import nc.gui.element.*;
import nc.network.PacketHandler;
import nc.network.gui.EmptyTankPacket;
import nc.tile.internal.fluid.Tank;
import nc.tile.processor.*;
import nc.util.NCUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class GuiProcessor<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> extends NCGui {
	
	protected final EntityPlayer player;
	protected final TILE tile;
	protected final INFO info;
	protected final ResourceLocation guiTextures;
	
	protected String guiName = null;
	
	public GuiProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
		super(inventory);
		this.player = player;
		this.tile = tile;
		info = tile.getContainerInfo();
		guiTextures = new ResourceLocation(textureLocation);
		
		xSize = info.guiSizeX;
		ySize = info.guiSizeY;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initButtons();
	}
	
	public void initButtons() {
		for (int i = 0; i < info.fluidInputSize; ++i) {
			int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
			buttonList.add(new NCButton.EmptyTank(i, guiLeft + tankXYWH[0], guiTop + tankXYWH[1], tankXYWH[2], tankXYWH[3]));
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
			buttonList.add(new NCButton.EmptyTank(i + info.fluidInputSize, guiLeft + tankXYWH[0], guiTop + tankXYWH[1], tankXYWH[2], tankXYWH[3]));
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(guiTextures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		List<Tank> tanks = tile.getTanks();
		for (int i = 0; i < info.fluidInputSize; ++i) {
			int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
			GuiFluidRenderer.renderGuiTank(tanks.get(i), guiLeft + tankXYWH[0], guiTop + tankXYWH[1], zLevel, tankXYWH[2], tankXYWH[3]);
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
			GuiFluidRenderer.renderGuiTank(tanks.get(i + info.fluidInputSize), guiLeft + tankXYWH[0], guiTop + tankXYWH[1], zLevel, tankXYWH[2], tankXYWH[3]);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (tile.getWorld().isRemote && NCUtil.isModifierKeyDown()) {
			for (int i = 0; i < info.fluidInputSize + info.fluidOutputSize; ++i) {
				if (button.id == i) {
					PacketHandler.instance.sendToServer(new EmptyTankPacket(tile, i));
					return;
				}
			}
		}
	}
	
	@Override
	public void renderTooltips(int mouseX, int mouseY) {
		renderButtonTooltips(mouseX, mouseY);
	}
	
	public void renderButtonTooltips(int mouseX, int mouseY) {
		List<Tank> tanks = tile.getTanks();
		for (int i = 0; i < info.fluidInputSize; ++i) {
			int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
			drawFluidTooltip(tanks.get(i), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
		}
		
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
			drawFluidTooltip(tanks.get(i + info.fluidInputSize), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
		}
	}
}
