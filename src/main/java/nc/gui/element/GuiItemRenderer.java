/** Massive thanks to CrazyPants, maker of EnderIO and related mods, for letting me use this code! */

package nc.gui.element;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class GuiItemRenderer extends Gui {
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public static final int DEFAULT_WIDTH = 16;
	public static final int HWIDTH = DEFAULT_WIDTH / 2;
	public static final int DEFAULT_HEIGHT = 16;
	public static final int HHEIGHT = DEFAULT_HEIGHT / 2;
	
	protected int hwidth = HWIDTH;
	protected int hheight = HHEIGHT;
	protected int width = DEFAULT_WIDTH;
	protected int height = DEFAULT_HEIGHT;
	
	protected @Nonnull TextureAtlasSprite icon;
	protected @Nonnull ResourceLocation texture;
	
	private int yPosition;
	private int xPosition;
	
	private float alpha = 1F;
	
	public GuiItemRenderer(int x, int y, float alph, @Nonnull Item item, int itemMeta) {
		xPosition = x;
		yPosition = y;
		setAlpha(alph);
		icon = getIconForItem(item, itemMeta);
		texture = TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
	public GuiItemRenderer(int x, int y, float alph, @Nonnull TextureAtlasSprite icon, @Nonnull ResourceLocation texture) {
		xPosition = x;
		yPosition = y;
		setAlpha(alph);
		this.icon = icon;
		this.texture = texture;
	}
	
	public static @Nonnull TextureAtlasSprite getIconForItem(@Nonnull Item item, int meta) {
		final TextureAtlasSprite icon = MC.getRenderItem().getItemModelMesher().getParticleIcon(item, meta);
		return icon != null ? icon : MC.getTextureMapBlocks().getMissingSprite();
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		hwidth = width / 2;
		hheight = height / 2;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public @Nonnull TextureAtlasSprite getIcon() {
		return icon;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void setIcon(@Nonnull TextureAtlasSprite icon) {
		this.icon = icon;
	}
	
	public @Nonnull ResourceLocation getTexture() {
		return texture;
	}
	
	public void setTexture(@Nonnull ResourceLocation textureName) {
		this.texture = textureName;
	}
	
	public static void bindTexture(String string) {
		MC.renderEngine.bindTexture(new ResourceLocation(string));
	}
	
	public static void bindTexture(@Nonnull ResourceLocation tex) {
		MC.renderEngine.bindTexture(tex);
	}
	
	public void draw() {
		GlStateManager.color(1F, 1F, 1F, alpha);
		GlStateManager.pushAttrib();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		bindTexture(texture);
		drawTexturedModalRect(xPosition, yPosition, icon, width, height);
		
		GlStateManager.popAttrib();
		GlStateManager.disableBlend();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
	}
}
