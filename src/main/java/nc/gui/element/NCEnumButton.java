package nc.gui.element;

import org.lwjgl.opengl.GL11;

import nc.Global;
import nc.gui.IGuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class NCEnumButton<T extends Enum<T>> extends NCButton {
	
	protected T value;
	
	public NCEnumButton(int id, int x, int y, int width, int height, T startingValue) {
		super(id, x, y, width, height);
		value = startingValue;
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		boolean clicked = mouseButton == 0 && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		if (clicked) {
			toggle(mouseButton);
		}
		return clicked;
	}
	
	public abstract void toggle(int mouseButton);
	
	@SideOnly(Side.CLIENT)
	public static abstract class Image<T extends Enum<T> & IGuiButton> extends NCEnumButton<T> {
		
		protected int textureX, textureY;
		protected int textureWidth, textureHeight;
		public final ResourceLocation unpressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/off.png");
		public final ResourceLocation pressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/on.png");
		
		public Image(int id, int x, int y, T startingValue) {
			super(id, x, y, startingValue.getTextureWidth(), startingValue.getTextureHeight(), startingValue);
			this.textureX = startingValue.getTextureX();
			this.textureY = startingValue.getTextureY();
			this.textureWidth = startingValue.getTextureWidth();
			this.textureHeight = startingValue.getTextureHeight();
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int drawX, int drawY, float partialTicks) {
			if (visible) {
				GL11.glColor4f(1F, 1F, 1F, 1F);
				hovered = drawX >= x && drawY >= y && drawX < x + width && drawY < y + height;
				minecraft.getTextureManager().bindTexture(getTexture());
				setTexturePosition();
				drawTexturedModalRect(x, y, textureX, textureY, textureWidth, textureHeight);
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
		
		public void setTexturePosition() {
			textureX = value.getTextureX();
			textureY = value.getTextureY();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ItemSorption extends Image<nc.tile.internal.inventory.ItemSorption> {
		
		private final nc.tile.internal.inventory.ItemSorption.Type sorptionType;
		
		public ItemSorption(int id, int x, int y, nc.tile.internal.inventory.ItemSorption startingValue, nc.tile.internal.inventory.ItemSorption.Type sorptionType) {
			super(id, x, y, startingValue);
			this.sorptionType = sorptionType;
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			boolean clicked = (mouseButton == 0 || mouseButton == 1) && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if (clicked) {
				toggle(mouseButton);
			}
			return clicked;
		}
		
		@Override
		public void toggle(int mouseButton) {
			value = value.next(sorptionType, mouseButton == 1);
		}
		
		public void set(nc.tile.internal.inventory.ItemSorption sorption) {
			value = sorption;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class TankSorption extends Image<nc.tile.internal.fluid.TankSorption> {
		
		private final nc.tile.internal.fluid.TankSorption.Type sorptionType;
		
		public TankSorption(int id, int x, int y, nc.tile.internal.fluid.TankSorption startingValue, nc.tile.internal.fluid.TankSorption.Type sorptionType) {
			super(id, x, y, startingValue);
			this.sorptionType = sorptionType;
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			boolean clicked = (mouseButton == 0 || mouseButton == 1) && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if (clicked) {
				toggle(mouseButton);
			}
			return clicked;
		}
		
		@Override
		public void toggle(int mouseButton) {
			value = value.next(sorptionType, mouseButton == 1);
		}
		
		public void set(nc.tile.internal.fluid.TankSorption sorption) {
			value = sorption;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ItemOutputSetting extends Image<nc.tile.internal.inventory.ItemOutputSetting> {
		
		public ItemOutputSetting(int id, int x, int y, nc.tile.internal.inventory.ItemOutputSetting startingValue) {
			super(id, x, y, startingValue);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			boolean clicked = (mouseButton == 0 || mouseButton == 1) && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if (clicked) {
				toggle(mouseButton);
			}
			return clicked;
		}
		
		@Override
		public void toggle(int mouseButton) {
			value = value.next(mouseButton == 1);
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	public static class TankOutputSetting extends Image<nc.tile.internal.fluid.TankOutputSetting> {
		
		public TankOutputSetting(int id, int x, int y, nc.tile.internal.fluid.TankOutputSetting startingValue) {
			super(id, x, y, startingValue);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			boolean clicked = (mouseButton == 0 || mouseButton == 1) && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if (clicked) {
				toggle(mouseButton);
			}
			return clicked;
		}
		
		@Override
		public void toggle(int mouseButton) {
			value = value.next(mouseButton == 1);
		}
		
	}
}
