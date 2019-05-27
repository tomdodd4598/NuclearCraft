package nc.gui.element;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import nc.Global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class NCGuiButton {
	
	@SideOnly(Side.CLIENT)
	public static class Button extends GuiButton {
		
		public boolean isButtonPressed;

		public Button(int id, int x, int y, int width, int height, String text) {
			super(id, x, y, width, height, text);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
			isButtonPressed = enabled && visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + width && mouseY < this.y + height;
			return isButtonPressed;
		}
		
		@Override
		public void mouseReleased(int mouseX, int mouseY) {
			isButtonPressed = false;
		}
		
		public static boolean isShiftKeyDown() {
			return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		}
		
		public static boolean isCtrlKeyDown() {
			return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		}
		
		//public void onPressed() {}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ImageButton extends Button {
		
		public final ResourceLocation unpressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/off.png");
		public final ResourceLocation pressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/on.png");
		protected int textureX;
		protected int textureY;
		protected int textureWidth;
		protected int textureHeight;
		
		public ImageButton(int id, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight) {
			super(id, x, y, textureWidth, textureHeight, "");
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
			if (visible) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				hovered = x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
				minecraft.getTextureManager().bindTexture(getTexture());
				drawTexturedModalRect(this.x, this.y, textureX, textureY, textureWidth, textureHeight);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ItemButton extends Button {
		
		protected int width;
		protected int height;
		protected GuiItemRenderer itemRenderer;
		
		public ItemButton(int id, int x, int y, int width, int height, float alph, @Nonnull Item item, int itemMeta) {
			super(id, x, y, width, height, "");
			this.width = width;
			this.height = height;
			itemRenderer = new GuiItemRenderer(x + (width - 16)/2, y + (height - 16)/2, alph, item, itemMeta);
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {
			if (visible) {
				hovered = x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
				itemRenderer.draw();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class BlankButton extends Button {
		
		public BlankButton(int id, int x, int y, int width, int height) {
			super(id, x, y, width, height, "");
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {}
	}
	
	@SideOnly(Side.CLIENT)
	public static class EmptyTankButton extends BlankButton {
		
		public EmptyTankButton(int id, int x, int y, int width, int height) {
			super(id, x, y, width, height);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
			isButtonPressed = isShiftKeyDown() && enabled && visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + width && mouseY < this.y + height;
			return isButtonPressed;
		}
	}
}
