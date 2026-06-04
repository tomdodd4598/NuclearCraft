package nc.render.tile;

import nc.multiblock.machine.*;
import nc.render.IWorldRender;
import nc.tile.internal.fluid.Tank;
import nc.tile.machine.TileDecayPoolController;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderMultiblockDecayPool extends TileEntitySpecialRenderer<TileDecayPoolController> implements IWorldRender {
	
	@Override
	public boolean isGlobalRenderer(TileDecayPoolController controller) {
		return controller.isRenderer() && controller.isMultiblockAssembled();
	}
	
	@Override
	public void render(TileDecayPoolController controller, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
		if (!controller.isRenderer() || !controller.isMultiblockAssembled()) {
			return;
		}
		
		Machine machine = controller.getMultiblock();
		if (machine == null) {
			return;
		}
		
		MachineLogic logic = machine.getLogic();
		if (!(logic instanceof DecayPoolLogic)) {
			return;
		}
		
		GlStateManager.pushMatrix();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0F, 240F);
		
		GlStateManager.enableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		
		BlockPos posOffset = machine.getExtremeInteriorCoord(false, false, false).subtract(controller.getPos());
		GlStateManager.translate(posX + posOffset.getX(), posY + posOffset.getY(), posZ + posOffset.getZ());
		
		int xSize = machine.getInteriorLengthX(), ySize = machine.getInteriorLengthY(), zSize = machine.getInteriorLengthZ();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-PIXEL, -PIXEL, -PIXEL);
		for (Tank tank : machine.tanks) {
			IWorldRender.renderFluid(tank, xSize + 2D * PIXEL, ySize + 2D * PIXEL, zSize + 2D * PIXEL, EnumFacing.UP);
		}
		GlStateManager.popMatrix();
		
		GlStateManager.disableBlend();
		GlStateManager.disableCull();
		
		GlStateManager.popMatrix();
	}
}
