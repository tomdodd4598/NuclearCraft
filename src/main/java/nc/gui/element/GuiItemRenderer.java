package nc.gui.element;

import nc.util.NCMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeModContainer;

import javax.annotation.Nonnull;
import java.awt.*;

public class GuiItemRenderer {
	
	protected static final Minecraft MC = Minecraft.getMinecraft();
	
	protected final @Nonnull ItemStack stack;
	protected final int x, y;
	protected final float alpha;
	
	protected int width = 16, height = 16;
	
	public GuiItemRenderer(@Nonnull ItemStack stack, int x, int y, float alpha) {
		this.stack = stack;
		this.x = x;
		this.y = y;
		this.alpha = alpha;
	}
	
	public GuiItemRenderer(Item item, int meta, int x, int y, float alpha) {
		this(new ItemStack(item, 1, meta), x, y, alpha);
	}
	
	public GuiItemRenderer size(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
		return this;
	}
	
	public void draw() {
		if (!stack.isEmpty()) {
			GlStateManager.color(1F, 1F, 1F, alpha);
			renderItem();
			renderItemOverlays();
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}
	
	protected void renderItem() {
		MC.getRenderItem().zLevel += 50F;
		GlStateManager.pushMatrix();
		MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, alpha);
		IBakedModel model = MC.getRenderItem().getItemModelWithOverrides(stack, null, null);
		setupGuiTransform(x, y, model.isGui3d());
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
		renderModelAndEffect(stack, model);
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		MC.getRenderItem().zLevel -= 50F;
	}
	
	protected void setupGuiTransform(int x, int y, boolean isGui3d) {
		GlStateManager.translate(x, y, 100F + MC.getRenderItem().zLevel);
		GlStateManager.translate(8F, 8F, 0F);
		GlStateManager.scale(1F, -1F, 1F);
		GlStateManager.scale(width, 16F, height);
		
		if (isGui3d) {
			GlStateManager.enableLighting();
		}
		else {
			GlStateManager.disableLighting();
		}
	}
	
	protected void renderModelAndEffect(ItemStack itemStack, IBakedModel model) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		
		if (model.isBuiltInRenderer()) {
			GlStateManager.color(1F, 1F, 1F, alpha);
			GlStateManager.enableRescaleNormal();
			itemStack.getItem().getTileEntityItemStackRenderer().renderByItem(itemStack);
		}
		else {
			renderModel(model, new Color(1F, 1F, 1F, alpha).getRGB(), itemStack);
			if (itemStack.hasEffect()) {
				renderEffect(model);
			}
		}
		
		GlStateManager.popMatrix();
	}
	
	protected void renderEffect(IBakedModel model) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		MC.getTextureManager().bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8F, 8F, 8F);
		float f = (Minecraft.getSystemTime() % 3000L) / 24000F;
		GlStateManager.translate(f, 0F, 0F);
		GlStateManager.rotate(-50F, 0F, 0F, 1F);
		renderModel(model, -8372020, ItemStack.EMPTY);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8F, 8F, 8F);
		float f1 = (Minecraft.getSystemTime() % 4873L) / 38984F;
		GlStateManager.translate(-f1, 0F, 0F);
		GlStateManager.rotate(10F, 0F, 0F, 1F);
		renderModel(model, -8372020, ItemStack.EMPTY);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
	
	protected void renderModel(IBakedModel model, int color, ItemStack itemStack) {
		if (ForgeModContainer.allowEmissiveItems) {
			ForgeHooksClient.renderLitItem(MC.getRenderItem(), model, color, itemStack);
			return;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
		
		for (EnumFacing facing : EnumFacing.VALUES) {
			MC.getRenderItem().renderQuads(bufferbuilder, model.getQuads(null, facing, 0L), color, itemStack);
		}
		
		MC.getRenderItem().renderQuads(bufferbuilder, model.getQuads(null, null, 0L), color, itemStack);
		tessellator.draw();
	}
	
	protected void renderItemOverlays() {
		if (!stack.isEmpty()) {
			BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			
			if (stack.getItem().showDurabilityBar(stack)) {
				GlStateManager.disableBlend();
				int i = Math.round(13F * (1F - (float) stack.getItem().getDurabilityForDisplay(stack)));
				int j = stack.getItem().getRGBDurabilityForDisplay(stack);
				draw(bufferbuilder, x + 2, y + 13, 13, 2, 0, 0, 0, NCMath.toInt(255D * alpha));
				draw(bufferbuilder, x + 2, y + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, NCMath.toInt(255D * alpha));
				GlStateManager.enableBlend();
			}
			
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			float partialTicks = player == null ? 0F : player.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
			
			if (partialTicks > 0F) {
				draw(bufferbuilder, x, y + MathHelper.floor(16F * (1F - partialTicks)), 16, MathHelper.ceil(16F * partialTicks), 255, 255, 255, NCMath.toInt(127D * alpha));
			}
			
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}
	}
	
	protected void draw(BufferBuilder bufferbuilder, int drawX, int drawY, int drawWidth, int drawHeight, int r, int g, int b, int a) {
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(drawX, drawY, 0D).color(r, g, b, a).endVertex();
		bufferbuilder.pos(drawX, drawY + drawHeight, 0D).color(r, g, b, a).endVertex();
		bufferbuilder.pos(drawX + drawWidth, drawY + drawHeight, 0D).color(r, g, b, a).endVertex();
		bufferbuilder.pos(drawX + drawWidth, drawY, 0D).color(r, g, b, a).endVertex();
		Tessellator.getInstance().draw();
	}
}
