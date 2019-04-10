package nc.gui.element;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import nc.Global;
import nc.gui.element.NCGuiButton.Button;
import nc.tile.ITile;
import nc.tile.fluid.ITileFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class NCGuiToggleButton {
	
	@SideOnly(Side.CLIENT)
	public static class ToggleButton extends Button {
		
		public final String unpressedText;
		public final String pressedText;
		
		public ToggleButton(int id, int x, int y, int width, int height, String unpressedText, String pressedText, boolean pressed) {
			super(id, x, y, width, height, unpressedText);
			this.unpressedText = unpressedText;
			this.pressedText = pressedText;
			isButtonPressed = pressed;
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
			boolean clicked = enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if (clicked) togglePressed();
			return clicked;
		}
		
		@Override
		public void mouseReleased(int mouseX, int mouseY) {}
		
		public ToggleButton setPressed(boolean pressed) {
			isButtonPressed = pressed;
			if (pressed) displayString = pressedText;
			else displayString = unpressedText;
			return this;
		}
		
		public boolean getPressed() {
			return isButtonPressed;
		}
		
		public boolean togglePressed() {
			setPressed(!isButtonPressed);
			return true;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ImageToggleButton extends ToggleButton {
		
		public final ResourceLocation unpressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/off.png");
		public final ResourceLocation pressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/on.png");
		protected int textureX;
		protected int textureY;
		protected int textureWidth;
		protected int textureHeight;
		
		public ImageToggleButton(int id, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, boolean pressed) {
			super(id, x, y, textureWidth, textureHeight, "", "", pressed);
			this.textureX = textureX;
			this.textureY = textureY;
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
		}
		
		public ResourceLocation getTexture() {
			if (isButtonPressed) return pressedTexture; else return unpressedTexture;
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {
			if (this.visible) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				hovered = x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
				minecraft.getTextureManager().bindTexture(getTexture());
				drawTexturedModalRect(this.x, this.y, textureX, textureY, textureWidth, textureHeight);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ItemToggleButton extends ToggleButton {
		
		protected int width;
		protected int height;
		protected GuiItemRenderer unpressedItemRenderer;
		protected GuiItemRenderer pressedItemRenderer;
		
		public ItemToggleButton(int id, int x, int y, int width, int height, float unpressedAlph, @Nonnull Item unpressedItem, int unpressedItemMeta, float pressedAlph, @Nonnull Item pressedItem, int pressedItemMeta, boolean pressed) {
			super(id, x, y, width, height, "", "", pressed);
			this.width = width;
			this.height = height;
			unpressedItemRenderer = new GuiItemRenderer(x + (width - 16)/2, y + (height - 16)/2, unpressedAlph, unpressedItem, unpressedItemMeta);
			pressedItemRenderer = new GuiItemRenderer(x + (width - 16)/2, y + (height - 16)/2, pressedAlph, pressedItem, pressedItemMeta);
		}
		
		public GuiItemRenderer getItemRenderer() {
			if (isButtonPressed) return pressedItemRenderer; else return unpressedItemRenderer;
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {
			if (this.visible) {
				hovered = x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
				getItemRenderer().draw();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ToggleInputTanksSeparatedButton extends ImageToggleButton {
		
		public ToggleInputTanksSeparatedButton(int id, int x, int y, ITileFluid machine) {
			super(id, x, y, 0, 0, 18, 18, machine.getInputTanksSeparated());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ToggleVoidUnusableFluidInputButton extends ImageToggleButton {
		
		public ToggleVoidUnusableFluidInputButton(int id, int x, int y, ITileFluid machine, int tankNumber) {
			super(id, x, y, 18, 0, 18, 18, machine.getVoidUnusableFluidInput(tankNumber));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ToggleVoidExcessFluidOutputButton extends ImageToggleButton {
		
		public ToggleVoidExcessFluidOutputButton(int id, int x, int y, ITileFluid machine, int tankNumber) {
			super(id, x, y, 36, 0, 18, 18, machine.getVoidExcessFluidOutput(tankNumber));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ToggleAlternateComparatorButton extends ImageToggleButton {
		
		public ToggleAlternateComparatorButton(int id, int x, int y, ITile machine) {
			super(id, x, y, 54, 0, 18, 18, machine.getAlternateComparator());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ToggleRedstoneControlButton extends ImageToggleButton {
		
		public ToggleRedstoneControlButton(int id, int x, int y, ITile machine) {
			super(id, x, y, 72, 0, 18, 18, machine.getRedstoneControl());
		}
	}
}
