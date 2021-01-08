package nc.gui.element;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import nc.Global;
import nc.tile.ITile;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankOutputSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class NCToggleButton extends NCButton {
	
	public NCToggleButton(int id, int x, int y, int width, int height, boolean pressed) {
		super(id, x, y, width, height);
		isButtonPressed = pressed;
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		boolean clicked = mouseButton == 0 && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		if (clicked) {
			isButtonPressed = !isButtonPressed;
		}
		return clicked;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {}
	
	@SideOnly(Side.CLIENT)
	public static abstract class Image extends NCToggleButton {
		
		public final ResourceLocation unpressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/off.png");
		public final ResourceLocation pressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/on.png");
		protected int textureX, textureY;
		protected int textureWidth, textureHeight;
		
		public Image(int id, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, boolean pressed) {
			super(id, x, y, textureWidth, textureHeight, pressed);
			this.textureX = textureX;
			this.textureY = textureY;
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {
			if (visible) {
				GL11.glColor4f(1F, 1F, 1F, 1F);
				hovered = x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
				minecraft.getTextureManager().bindTexture(getTexture());
				drawTexturedModalRect(this.x, this.y, textureX, textureY, textureWidth, textureHeight);
			}
		}
		
		public ResourceLocation getTexture() {
			if (isButtonPressed) {
				return pressedTexture;
			}
			else {
				return unpressedTexture;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class Item extends NCToggleButton {
		
		protected GuiItemRenderer unpressedItemRenderer, pressedItemRenderer;
		
		public Item(int id, int x, int y, int width, int height, float unpressedAlpha, @Nonnull net.minecraft.item.Item unpressedItem, int unpressedMeta, float pressedAlpha, @Nonnull net.minecraft.item.Item pressedItem, int pressedMeta, boolean pressed) {
			super(id, x, y, width, height, pressed);
			this.width = width;
			this.height = height;
			unpressedItemRenderer = new GuiItemRenderer(unpressedItem, unpressedMeta, x + (width - 16) / 2, y + (height - 16) / 2, unpressedAlpha);
			pressedItemRenderer = new GuiItemRenderer(pressedItem, pressedMeta, x + (width - 16) / 2, y + (height - 16) / 2, pressedAlpha);
		}
		
		public GuiItemRenderer getItemRenderer() {
			if (isButtonPressed) {
				return pressedItemRenderer;
			}
			else {
				return unpressedItemRenderer;
			}
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {
			if (visible) {
				hovered = x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
				getItemRenderer().draw();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class InputTanksSeparated extends Image {
		
		public InputTanksSeparated(int id, int x, int y, ITileFluid machine) {
			super(id, x, y, 0, 0, 18, 18, machine.getInputTanksSeparated());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class VoidUnusableFluidInput extends Image {
		
		public VoidUnusableFluidInput(int id, int x, int y, ITileFluid machine, int tankNumber) {
			super(id, x, y, 18, 0, 18, 18, machine.getVoidUnusableFluidInput(tankNumber));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class VoidExcessFluidOutput extends Image {
		
		public VoidExcessFluidOutput(int id, int x, int y, ITileFluid machine, int tankNumber) {
			super(id, x, y, 36, 0, 18, 18, machine.getTankOutputSetting(tankNumber) != TankOutputSetting.DEFAULT);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class AlternateComparator extends Image {
		
		public AlternateComparator(int id, int x, int y, ITile machine) {
			super(id, x, y, 54, 0, 18, 18, machine.getAlternateComparator());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class RedstoneControl extends Image {
		
		public RedstoneControl(int id, int x, int y, ITile machine) {
			super(id, x, y, 72, 0, 18, 18, machine.getRedstoneControl());
		}
	}
}
