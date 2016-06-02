package nc.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemRenderFusionReactor implements IItemRenderer {
	
	TileEntitySpecialRenderer render;
	private TileEntity entity;
	
	public ItemRenderFusionReactor(TileEntitySpecialRenderer render, TileEntity entity) {
		this.entity = entity;
		this.render = render;
	}
	

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == IItemRenderer.ItemRenderType.ENTITY) {
			GL11.glTranslatef(-0.25F, 0.0F, -0.25F);
			GL11.glScaled(0.5D, 0.5D, 0.5D);
		}
		if (type == IItemRenderer.ItemRenderType.INVENTORY) {
			GL11.glTranslatef(0.0F, -0.361F, 0.0F);
			GL11.glScaled(0.333D, 0.333D, 0.333D);
		}
		if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(-0.2F, 0.0F, -0.2F);
			GL11.glScaled(0.5D, 0.5D, 0.5D);
		}
		if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
			GL11.glTranslatef(0.8F, 0.0F, 0.8F);
			GL11.glScaled(0.5D, 0.5D, 0.5D);
		}
		this.render.renderTileEntityAt(this.entity, 0.0D, 0.0D, 0.0D, 0.0F);
	}

}
