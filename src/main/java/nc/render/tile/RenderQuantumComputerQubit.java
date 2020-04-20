package nc.render.tile;

import nc.multiblock.qComputer.tile.TileQuantumComputerQubit;
import nc.util.NCMath;
import nc.util.NCRenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderQuantumComputerQubit extends TileEntitySpecialRenderer<TileQuantumComputerQubit> {
	
	@Override
	public void render(TileQuantumComputerQubit qubit, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
		long time = System.currentTimeMillis();
		boolean up = qubit.measureColor > 0F, down = qubit.measureColor < 0F;
		float r = (float) NCMath.trapezoidalWave(time*0.12D, 0D) + 6F*(down ? -qubit.measureColor : 0F);
		float g = (float) NCMath.trapezoidalWave(time*0.12D, 120D);
		float b = (float) NCMath.trapezoidalWave(time*0.12D, 240D) + 6F*(up ? qubit.measureColor : 0F);
		
		if (up) {
			qubit.measureColor = Math.max(0F, qubit.measureColor - 0.0005F);
		}
		else if (down) {
			qubit.measureColor = Math.min(0F, qubit.measureColor + 0.0005F);
		}
		
		float d = Math.max(r, Math.max(g, b));
		r /= d;
		g /= d;
		b /= d;
		
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		
		BlockPos pos = qubit.getPos();
		GlStateManager.translate(posX - pos.getX(), posY - pos.getY(), posZ - pos.getZ());
		
		GlStateManager.color(r, g, b);
		NCRenderHelper.renderBlockFaces(pos, 8F, r, g, b, 1F);
		Tessellator.getInstance().draw();
		
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}
}