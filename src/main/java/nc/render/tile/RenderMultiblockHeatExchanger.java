package nc.render.tile;

import nc.multiblock.hx.HeatExchanger;
import nc.render.IWorldRender;
import nc.tile.hx.TileHeatExchangerController;
import nc.tile.internal.fluid.Tank;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderMultiblockHeatExchanger extends TileEntitySpecialRenderer<TileHeatExchangerController> implements IWorldRender {
	
	@Override
	public boolean isGlobalRenderer(TileHeatExchangerController controller) {
		return controller.isRenderer() && controller.isMultiblockAssembled();
	}
	
	@Override
	public void render(TileHeatExchangerController controller, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
		if (!controller.isRenderer() || !controller.isMultiblockAssembled()) {
			return;
		}
		
		HeatExchanger hx = controller.getMultiblock();
		if (hx == null) {
			return;
		}
		
		GlStateManager.pushMatrix();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0F, 240F);
		
		GlStateManager.enableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		
		BlockPos posOffset = hx.getExtremeInteriorCoord(false, false, false).subtract(controller.getPos());
		GlStateManager.translate(posX + posOffset.getX(), posY + posOffset.getY(), posZ + posOffset.getZ());
		
		int xSize = hx.getInteriorLengthX(), ySize = hx.getInteriorLengthY(), zSize = hx.getInteriorLengthZ();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-PIXEL, -PIXEL, -PIXEL);
		for (Tank tank : hx.shellTanks) {
			IWorldRender.renderFluid(tank, xSize + 2D * PIXEL, ySize + 2D * PIXEL, zSize + 2D * PIXEL, EnumFacing.UP, x -> true);
		}
		GlStateManager.popMatrix();
		
		GlStateManager.disableBlend();
		GlStateManager.disableCull();
		
		GlStateManager.popMatrix();
	}
}
