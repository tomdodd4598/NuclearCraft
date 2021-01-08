package nc.gui.element;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import nc.Global;
import nc.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class NCButton extends GuiButton {
	
	protected boolean isButtonPressed;
	
	public NCButton(int id, int x, int y, int width, int height) {
		super(id, x, y, width, height, "");
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		return mousePressed(minecraft, mouseX, mouseY, 0);
	}
	
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		isButtonPressed = mouseButton == 0 && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		return isButtonPressed;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		isButtonPressed = false;
	}
	
	@SideOnly(Side.CLIENT)
	public static abstract class Image extends NCButton {
		
		public final ResourceLocation unpressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/off.png");
		public final ResourceLocation pressedTexture = new ResourceLocation(Global.MOD_ID + ":textures/gui/buttons/on.png");
		protected int textureX, textureY;
		protected int textureWidth, textureHeight;
		
		public Image(int id, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight) {
			super(id, x, y, textureWidth, textureHeight);
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
	public static class Item extends NCButton {
		
		protected GuiItemRenderer itemRenderer;
		
		public Item(int id, int x, int y, int width, int height, float alph, @Nonnull net.minecraft.item.Item item, int meta) {
			super(id, x, y, width, height);
			this.width = width;
			this.height = height;
			itemRenderer = new GuiItemRenderer(item, meta, x + (width - 16) / 2, y + (height - 16) / 2, alph);
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
	public static class Blank extends NCButton {
		
		public Blank(int id, int x, int y, int width, int height) {
			super(id, x, y, width, height);
		}
		
		@Override
		public void drawButton(Minecraft minecraft, int x, int y, float partialTicks) {}
	}
	
	@SideOnly(Side.CLIENT)
	public static class EmptyTank extends Blank {
		
		public EmptyTank(int id, int x, int y, int width, int height) {
			super(id, x, y, width, height);
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			isButtonPressed = mouseButton == 0 && NCUtil.isModifierKeyDown() && enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			return isButtonPressed;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class MachineConfig extends Image {
		
		public MachineConfig(int id, int x, int y) {
			super(id, x, y, 90, 0, 18, 18);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static abstract class SorptionConfig extends Image {
		
		public SorptionConfig(int id, int x, int y, int textureX, int textureY, int width, int height) {
			super(id, x, y, textureX, textureY, width, height);
		}
		
		@SideOnly(Side.CLIENT)
		public static class ItemInput extends SorptionConfig {
			
			public ItemInput(int id, int x, int y) {
				super(id, x, y, 0, 191, 18, 18);
			}
		}
		
		@SideOnly(Side.CLIENT)
		public static class ItemOutputSmall extends SorptionConfig {
			
			public ItemOutputSmall(int id, int x, int y) {
				super(id, x, y, 18, 191, 18, 18);
			}
		}
		
		@SideOnly(Side.CLIENT)
		public static class ItemOutput extends SorptionConfig {
			
			public ItemOutput(int id, int x, int y) {
				super(id, x, y, 0, 209, 26, 26);
			}
		}
		
		@SideOnly(Side.CLIENT)
		public static class FluidInput extends SorptionConfig {
			
			public FluidInput(int id, int x, int y) {
				super(id, x, y, 36, 191, 18, 18);
			}
		}
		
		@SideOnly(Side.CLIENT)
		public static class FluidOutput extends SorptionConfig {
			
			public FluidOutput(int id, int x, int y) {
				super(id, x, y, 26, 209, 26, 26);
			}
		}
		
		@SideOnly(Side.CLIENT)
		public static class FluidOutputSmall extends SorptionConfig {
			
			public FluidOutputSmall(int id, int x, int y) {
				super(id, x, y, 54, 191, 18, 18);
			}
		}
		
		@SideOnly(Side.CLIENT)
		public static class SpeedUpgrade extends SorptionConfig {
			
			public SpeedUpgrade(int id, int x, int y) {
				super(id, x, y, 72, 191, 18, 18);
			}
		}
		
		@SideOnly(Side.CLIENT)
		public static class EnergyUpgrade extends SorptionConfig {
			
			public EnergyUpgrade(int id, int x, int y) {
				super(id, x, y, 90, 191, 18, 18);
			}
		}
	}
}
