package nc.render.tile;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.init.NCBlocks;
import nc.multiblock.machine.*;
import nc.render.IWorldRender;
import nc.tile.internal.fluid.Tank;
import nc.tile.machine.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

@SideOnly(Side.CLIENT)
public class RenderMultiblockDistiller extends TileEntitySpecialRenderer<TileDistillerController> implements IWorldRender {
	
	@Override
	public boolean isGlobalRenderer(TileDistillerController controller) {
		return controller.isRenderer() && controller.isMultiblockAssembled();
	}
	
	@Override
	public void render(TileDistillerController controller, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
		if (!controller.isRenderer() || !controller.isMultiblockAssembled()) {
			return;
		}
		
		Machine machine = controller.getMultiblock();
		if (machine == null) {
			return;
		}
		
		MachineLogic logic = machine.getLogic();
		if (!(logic instanceof DistillerLogic distillerLogic)) {
			return;
		}
		
		GlStateManager.pushMatrix();
		
		BlockPos cornerPos = machine.getExtremeInteriorCoord(false, false, false);
		BlockPos offsetPos = cornerPos.subtract(controller.getPos());
		GlStateManager.translate(posX + offsetPos.getX(), posY + offsetPos.getY(), posZ + offsetPos.getZ());
		
		BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		
		float brightness = controller.nextRenderBrightness();
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0F, 240F);
		
		IntList trayLevels = distillerLogic.trayLevels;
		int trayCount = trayLevels.size();
		int xSize = machine.getInteriorLengthX(), ySize = machine.getInteriorLengthY(), zSize = machine.getInteriorLengthZ();
		IBlockState trayState = NCBlocks.distiller_sieve_tray.getDefaultState();
		for (int i = 0; i < trayCount; ++i) {
			int y = trayLevels.getInt(i);
			double offset = (i & 1) == 0 ? 0.9375D * PIXEL : -0.9375D * PIXEL;
			for (int x = 0; x < xSize; ++x) {
				for (int z = 0; z < zSize; ++z) {
					GlStateManager.pushMatrix();
					GlStateManager.translate(x + offset, y + 0.5D, z + offset);
					GlStateManager.rotate(-90F, 0F, 1F, 0F);
					renderer.renderBlockBrightness(trayState, brightness);
					GlStateManager.popMatrix();
				}
			}
		}
		
		GlStateManager.enableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		
		List<Tank> liquids = new ArrayList<>();
		List<Tank> gasses = new ArrayList<>();
		
		for (Tank tank : machine.processor.getFluidInputs(false)) {
			FluidStack stack = tank.getFluid();
			if (stack == null || stack.amount <= 0) {
				continue;
			}
			
			Fluid fluid = stack.getFluid();
			if (fluid == null) {
				continue;
			}
			
			(fluid.isGaseous(stack) ? gasses : liquids).add(tank);
		}
		
		if (!liquids.isEmpty()) {
			liquids.sort(Comparator.comparingInt(x -> {
				FluidStack stack = x.getFluid();
				return -stack.getFluid().getDensity(stack);
			}));
			
			int liquidCount = liquids.size();
			for (int i = 0; i < trayCount; ++i) {
				boolean offsetMin = (i & 1) == 0;
				EnumFacing xFillDir = offsetMin ? EnumFacing.WEST : EnumFacing.EAST, zFillDir = offsetMin ? EnumFacing.NORTH : EnumFacing.SOUTH;
				
				Tank tank = liquids.get((liquidCount * i) / trayCount);
				double fraction = (double) tank.getFluidAmount() / (double) tank.getCapacity();
				
				double trayHeight = trayLevels.getInt(i) + 1.5D - 0.25D * PIXEL;
				double fallHeight = trayLevels.getInt(i) - (i > 0 ? trayLevels.getInt(i - 1) : -1.5D + 0.25D * PIXEL);
				
				if (fraction > 0.5D) {
					double widthMult = Math.min(1D - 0.25 * PIXEL, 2D * (fraction - 0.5D));
					
					GlStateManager.pushMatrix();
					double zWidth = zSize - 0.5D * PIXEL;
					GlStateManager.translate(offsetMin ? 0.5D * PIXEL : xSize - 1.5D * PIXEL, trayHeight - fallHeight, 0.5D * (1D - widthMult) * zWidth + (offsetMin ? 0.5D * PIXEL : 0D));
					IWorldRender.renderFluid(tank, PIXEL, fallHeight, widthMult * zWidth, xFillDir);
					GlStateManager.popMatrix();
					
					GlStateManager.pushMatrix();
					double xWidth = xSize - 0.5D * PIXEL;
					GlStateManager.translate(0.5D * (1D - widthMult) * xWidth + (offsetMin ? 0.5D * PIXEL : 0D), trayHeight - fallHeight, offsetMin ? 0.5D * PIXEL : zSize - 1.5D * PIXEL);
					IWorldRender.renderFluid(tank, widthMult * xWidth, fallHeight, PIXEL, zFillDir);
					GlStateManager.popMatrix();
				}
				
				GlStateManager.pushMatrix();
				double widthMult = Math.min(1D, 2D * fraction);
				double xWidth = xSize - PIXEL, zWidth = zSize - PIXEL;
				GlStateManager.translate(0.5D * PIXEL + 0.5D * (1D - widthMult) * xWidth, trayHeight - 0.5D * PIXEL, 0.5D * PIXEL + 0.5D * (1D - widthMult) * zWidth);
				IWorldRender.renderFluid(tank, widthMult * xWidth, 0.5D - 0.5D * PIXEL, widthMult * zWidth, EnumFacing.UP);
				GlStateManager.popMatrix();
			}
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5D * PIXEL, -0.5D * PIXEL, -0.5D * PIXEL);
			IWorldRender.renderFluid(liquids.get(0), xSize + PIXEL, 0.25D + 0.5D * PIXEL, zSize + PIXEL, EnumFacing.UP);
			GlStateManager.popMatrix();
			
			Tank topLiquid = liquids.get(liquidCount - 1);
			FluidStack topStack = topLiquid.getFluid();
			int topStackSize = topLiquid.getFluidAmount();
			double topStackFraction = (double) topStackSize / (double) topLiquid.getCapacity();
			
			if (topStackFraction > 0.5D) {
				double width = 1D - 4D * PIXEL, widthMult = 2D * (topStackFraction - 0.5D), offset = 2D * PIXEL + 0.5D * (1D - widthMult) * width;
				double fallTop = ySize + 0.5D * PIXEL;
				double fallBottom = trayCount == 0 ? -0.5D * PIXEL : trayLevels.getInt(trayCount - 1) + 0.5D + PIXEL;
				
				for (TileDistillerLiquidDistributor ld : machine.getParts(TileDistillerLiquidDistributor.class)) {
					BlockPos ldPos = ld.getPos();
					int ldX = ldPos.getX() - cornerPos.getX(), ldZ = ldPos.getZ() - cornerPos.getZ();
					
					GlStateManager.pushMatrix();
					GlStateManager.translate(ldX + offset, fallBottom, ldZ + offset);
					IWorldRender.renderFluid(topStack, topStackSize, widthMult * width, fallTop - fallBottom, widthMult * width, EnumFacing.UP);
					GlStateManager.popMatrix();
				}
			}
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-PIXEL, -PIXEL, -PIXEL);
		for (Tank tank : gasses) {
			IWorldRender.renderFluid(tank, xSize + 2D * PIXEL, ySize + 2D * PIXEL, zSize + 2D * PIXEL, EnumFacing.UP);
		}
		GlStateManager.popMatrix();
		
		GlStateManager.disableBlend();
		GlStateManager.disableCull();
		
		GlStateManager.popMatrix();
	}
}
