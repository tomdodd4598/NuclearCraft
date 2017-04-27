package nc.render;

import nc.block.tile.energy.generator.BlockFusionCore;
import nc.tile.generator.TileFusionCore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FusionCoreTESR extends TileEntitySpecialRenderer<TileFusionCore> {
	
	public void renderTileEntityAt(TileFusionCore tileEntity, double posX, double posY, double posZ, float partialTicks, int destroyStage) {
		
		if(!(tileEntity.getBlockType() instanceof BlockFusionCore)) return;
		
		GlStateManager.pushMatrix(); {
			GlStateManager.translate(posX, posY, posZ);
			GlStateManager.translate(-0.75, 0, -0.75);
			GlStateManager.scale(2.0, 2.0, 2.0);
		}
		GlStateManager.popMatrix();
	}
}