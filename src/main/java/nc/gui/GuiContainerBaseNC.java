package nc.gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiContainerBaseNC extends GuiContainer {

	private static final String TEXTURE_PATH = ":textures/gui/container/";
	private static final String TEXTURE_EXT = ".png";
	private final List<Rectangle> tabAreas = new ArrayList<Rectangle>();

	private final List<ResourceLocation> guiTextures = new ArrayList<ResourceLocation>();

	public GuiContainerBaseNC(Container par1Container, String... guiTexture) {
		super(par1Container);
		for (String string : guiTexture) {
			guiTextures.add(getGuiTexture(string));
		}
	}

	public void bindGuiTexture() {
		bindGuiTexture(0);
	}

	public void bindGuiTexture(int id) {
		Minecraft.getMinecraft().renderEngine.bindTexture(getGuiTexture(id));
	}

	protected ResourceLocation getGuiTexture(int id) {
		return guiTextures.size() > id ? guiTextures.get(id) : null;
	}

	public static ResourceLocation getGuiTexture(String name) {
		return new ResourceLocation(Global.MOD_ID + TEXTURE_PATH + name + TEXTURE_EXT);
	}
	
	public List<Rectangle> getBlockingAreas() {
		// return a new object every time so equals() actually checks the contents
		return new ArrayList<Rectangle>(tabAreas);
	}
	
	public int getGuiLeft() {
		return guiLeft;
	}
	
	public int getGuiTop() {
		return guiTop;
	}
	
	@Nullable
	public Object getIngredientUnderMouse(int mouseX, int mouseY) {
		return null;
	}
}
