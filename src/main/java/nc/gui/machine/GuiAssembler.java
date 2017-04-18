package nc.gui.machine;
 
import nc.container.machine.ContainerAssembler;
import nc.packet.PacketAssemblerState;
import nc.packet.PacketHandler;
import nc.tile.machine.TileAssembler;
import nc.tile.machine.TileAssembler.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
 
public class GuiAssembler extends GuiContainer {
	public static final ResourceLocation bground = new ResourceLocation("nc:textures/gui/assembler.png");
	public TileAssembler assembler;
	
	private GuiButton modeButton;
	
	public GuiAssembler(InventoryPlayer inventoryPlayer, TileAssembler entity) {
		super(new ContainerAssembler(inventoryPlayer, entity));
		
		assembler = entity;
		
		modeButton = new GuiButton(0, 129, 14, 38, 20, assembler.getMode() == Mode.USE ? "USE" : "KEEP");

		this.xSize = 176;
		this.ySize = 174;
	}
	
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();

		modeButton.xPosition = guiLeft + 129;
		modeButton.yPosition = guiTop + 14;
		
		buttonList.add(modeButton);
		if (assembler.getMode() == Mode.USE) modeButton.displayString = "USE"; else modeButton.displayString = "KEEP";
		PacketHandler.INSTANCE.sendToServer(new PacketAssemblerState(assembler));
	}

	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String name = StatCollector.translateToLocal("tile.assemblerIdle.name");
		this.fontRendererObj.drawString(name, xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(assembler.energy + " RF", 28, ySize - 94, 4210752);
	}
   
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
     
		Minecraft.getMinecraft().getTextureManager().bindTexture(bground);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
     
		int e = assembler.energy * 82 / 250000;
		drawTexturedModalRect(guiLeft + 8, guiTop + 6 + 82 - e, 176, 3 + 82 - e, 16, e);
     
		int k = (int) Math.ceil(assembler.cookTime * 64 / assembler.getProcessTime);
		drawTexturedModalRect(guiLeft + 68, guiTop + 37, 3, 174, k, 37);
	}
	
	protected void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			assembler.setMode(assembler.getMode().next());
			if (assembler.getMode() == Mode.USE) modeButton.displayString = "USE"; else modeButton.displayString = "KEEP";
			PacketHandler.INSTANCE.sendToServer(new PacketAssemblerState(assembler));
		}
		
		else super.actionPerformed(button);
	}
}
