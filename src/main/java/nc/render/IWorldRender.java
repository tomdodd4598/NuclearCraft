package nc.render;

import com.google.common.collect.ImmutableSet;
import nc.tile.internal.fluid.Tank;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.*;

@SideOnly(Side.CLIENT)
public interface IWorldRender {
	
	double PIXEL = 0.0625;
	
	static @Nonnull TextureAtlasSprite getStillTexture(@Nonnull Fluid fluid) {
		ResourceLocation iconKey = fluid.getStill();
		if (iconKey == null) {
			return getMissingSprite();
		}
		final TextureAtlasSprite textureEntry = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(iconKey.toString());
		return textureEntry != null ? textureEntry : getMissingSprite();
	}
	
	static @Nonnull TextureAtlasSprite getMissingSprite() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
	
	static void renderFluid(FluidStack stack, double capacity, double xSize, double ySize, double zSize, EnumFacing fillDir, Predicate<FluidStack> isGaseous) {
		if (stack == null || stack.amount <= 0) {
			return;
		}
		
		Fluid fluid = stack.getFluid();
		if (fluid == null) {
			return;
		}
		
		int luminosity = 16 * fluid.getLuminosity();
		float[] lastBrightness = null;
		
		if (!FMLClientHandler.instance().hasOptifine() && luminosity > 0) {
			lastBrightness = new float[] {OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY};
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, Math.min(luminosity + lastBrightness[0], 240F), Math.min(luminosity + lastBrightness[1], 240F));
		}
		
		boolean gaseous = isGaseous.test(stack);
		int color = fluid.getColor(stack);
		double fraction = (double) stack.amount / capacity;
		
		GlStateManager.color(ColorHelper.getRed(color) / 255F, ColorHelper.getGreen(color) / 255F, ColorHelper.getBlue(color) / 255F, (gaseous ? (float) fraction : 1F) * ColorHelper.getAlpha(color) / 255F);
		
		if (fluid.getStill(stack) != null) {
			GlStateManager.pushMatrix();
			BlockModelCuboid model = new BlockModelCuboid();
			model.setTexture(IWorldRender.getStillTexture(fluid));
			if (gaseous) {
				model.setSize(xSize, ySize, zSize);
			}
			else {
				Axis axis = fillDir.getAxis();
				if (fillDir.getAxisDirection().equals(AxisDirection.NEGATIVE)) {
					switch (axis) {
						case X -> GlStateManager.translate((1D - fraction) * xSize, 0D, 0D);
						case Y -> GlStateManager.translate(0D, (1D - fraction) * ySize, 0D);
						case Z -> GlStateManager.translate(0D, 0D, (1D - fraction) * zSize);
					}
				}
				model.setSize((axis.equals(Axis.X) ? fraction : 1D) * xSize, (axis.equals(Axis.Y) ? fraction : 1D) * ySize, (axis.equals(Axis.Z) ? fraction : 1D) * zSize);
			}
			RenderModelCuboid.render(model);
			GlStateManager.popMatrix();
		}
		
		if (lastBrightness != null) {
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightness[0], lastBrightness[1]);
		}
		
		GlStateManager.color(1F, 1F, 1F, 1F);
	}
	
	static void renderFluid(FluidStack stack, double capacity, double xSize, double ySize, double zSize, EnumFacing fillDir) {
		renderFluid(stack, capacity, xSize, ySize, zSize, fillDir, x -> x.getFluid().isGaseous(x));
	}
	
	static void renderFluid(Tank tank, double xSize, double ySize, double zSize, EnumFacing fillDir, Predicate<FluidStack> isGaseous) {
		if (tank != null) {
			renderFluid(tank.getFluid(), tank.getCapacity(), xSize, ySize, zSize, fillDir, isGaseous);
		}
	}
	
	static void renderFluid(Tank tank, double xSize, double ySize, double zSize, EnumFacing fillDir) {
		renderFluid(tank, xSize, ySize, zSize, fillDir, x -> x.getFluid().isGaseous(x));
	}
	
	/**
	 * Thanks to Buildcraft and Mekanism for these methods!
	 */
	class RenderModelCuboid {
		
		public static final Vec3d VEC_ONE = all(1D);
		public static final Vec3d VEC_ZERO = all(0D);
		public static final Vec3d VEC_HALF = all(0.5D);
		
		private static final int U_MIN = 0;
		private static final int U_MAX = 1;
		private static final int V_MIN = 2;
		private static final int V_MAX = 3;
		
		private static final Map<EnumFacing, Vec3d> AMBIENT_OCCLUSION_MAP = new EnumMap<>(EnumFacing.class);
		
		static {
			AMBIENT_OCCLUSION_MAP.put(EnumFacing.UP, all(1D));
			AMBIENT_OCCLUSION_MAP.put(EnumFacing.DOWN, all(0.5D));
			AMBIENT_OCCLUSION_MAP.put(EnumFacing.NORTH, all(0.8D));
			AMBIENT_OCCLUSION_MAP.put(EnumFacing.SOUTH, all(0.8D));
			AMBIENT_OCCLUSION_MAP.put(EnumFacing.EAST, all(0.6D));
			AMBIENT_OCCLUSION_MAP.put(EnumFacing.WEST, all(0.6D));
		}
		
		public static Vec3d withValue(Vec3d vector, Axis axis, double value) {
			if (axis == Axis.X) {
				return new Vec3d(value, vector.y, vector.z);
			}
			else if (axis == Axis.Y) {
				return new Vec3d(vector.x, value, vector.z);
			}
			else if (axis == Axis.Z) {
				return new Vec3d(vector.x, vector.y, value);
			}
			else {
				return new Vec3d(vector.x, vector.y, vector.z);
			}
		}
		
		public static BlockPos convertFloor(Vec3d vec) {
			return new BlockPos(vec.x, vec.y, vec.z);
		}
		
		public static Vec3d convert(Vec3i vec3i) {
			return new Vec3d(vec3i.getX(), vec3i.getY(), vec3i.getZ());
		}
		
		public static Vec3d convert(EnumFacing face) {
			if (face == null) {
				return VEC_ZERO;
			}
			return new Vec3d(face.getXOffset(), face.getYOffset(), face.getZOffset());
		}
		
		public static EnumFacing[] getNeighbours(EnumFacing face) {
			EnumFacing[] faces = new EnumFacing[4];
			int ordinal = 0;
			for (EnumFacing next : EnumFacing.VALUES) {
				if (next.getAxis() != face.getAxis()) {
					faces[ordinal] = next;
					ordinal++;
				}
			}
			return faces;
		}
		
		public static void setWorldRendererRGB(BufferBuilder builder, Vec3d color) {
			builder.color((float) color.x, (float) color.y, (float) color.z, 1f);
		}
		
		public static Vec3d all(double value) {
			return new Vec3d(value, value, value);
		}
		
		public static Vec3d multiply(Vec3d vec, double multiple) {
			return new Vec3d(vec.x * multiple, vec.y * multiple, vec.z * multiple);
		}
		
		public static Vec3d convertToCenter(Vec3i vec3i) {
			return convert(vec3i).add(VEC_HALF);
		}
		
		public static double getValue(Vec3d vector, Axis axis) {
			if (axis == Axis.X) {
				return vector.x;
			}
			else if (axis == Axis.Y) {
				return vector.y;
			}
			else if (axis == Axis.Z) {
				return vector.z;
			}
			else {
				return 0D;
			}
		}
		
		public static void renderFromCenter(BlockModelCuboid model) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5F * model.sizeX(), -0.5F * model.sizeY(), -0.5F * model.sizeZ());
			render(model, EnumShadeArgument.NONE, null, null, null);
			GlStateManager.popMatrix();
		}
		
		public static void render(BlockModelCuboid model) {
			render(model, EnumShadeArgument.NONE, null, null, null);
		}
		
		public static void render(BlockModelCuboid model, EnumShadeArgument shadeTypes, IBlockLocation formula, IFacingLocation faceFormula, IBlockAccess world) {
			if (faceFormula == null) {
				faceFormula = DefaultFacingLocation.INSTANCE;
			}
			
			TextureAtlasSprite[] sprites = model.textures;
			
			int[] flips = model.textureFlips;
			if (flips == null) {
				flips = new int[6];
			}
			
			Vec3d textureStart = new Vec3d(model.textureStartX / 16D, model.textureStartY / 16D, model.textureStartZ / 16D);
			Vec3d textureSize = new Vec3d(model.textureSizeX / 16D, model.textureSizeY / 16D, model.textureSizeZ / 16D);
			Vec3d textureOffset = new Vec3d(model.textureOffsetX / 16D, model.textureOffsetY / 16D, model.textureOffsetZ / 16D);
			Vec3d size = new Vec3d(model.sizeX(), model.sizeY(), model.sizeZ());
			
			Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder builder = tessellator.getBuffer();
			
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.disableLighting();
			
			builder.begin(GL11.GL_QUADS, shadeTypes.vertexFormat);
			
			for (EnumFacing face : EnumFacing.VALUES) {
				if (model.getRenderSide(face)) {
					renderCuboidFace(builder, face, sprites, flips, textureStart, textureSize, size, textureOffset, shadeTypes, formula, faceFormula, world);
				}
			}
			
			tessellator.draw();
			
			GlStateManager.enableLighting();
			GlStateManager.disableAlpha();
		}
		
		private static void renderCuboidFace(BufferBuilder builder, EnumFacing face, TextureAtlasSprite[] sprites, int[] flips, Vec3d textureStart, Vec3d textureSize, Vec3d size, Vec3d textureOffset, EnumShadeArgument shadeTypes, IBlockLocation locationFormula, IFacingLocation faceFormula, IBlockAccess access) {
			int ordinal = face.ordinal();
			if (sprites[ordinal] == null) {
				return;
			}
			
			Vec3d textureEnd = textureStart.add(textureSize);
			float[] uv = getUVArray(sprites[ordinal], flips[ordinal], face, textureStart, textureEnd);
			List<RenderInfo> renderInfoList = getRenderInfos(uv, face, size, textureSize, textureOffset);
			
			Axis u = face.getAxis() == Axis.X ? Axis.Z : Axis.X;
			Axis v = face.getAxis() == Axis.Y ? Axis.Z : Axis.Y;
			double other = face.getAxisDirection() == AxisDirection.POSITIVE ? getValue(size, face.getAxis()) : 0;
			
			// Swap the face if this is positive: the renderer returns indexes that ALWAYS are for the negative face, so light it properly this way
			face = face.getAxisDirection() == AxisDirection.NEGATIVE ? face : face.getOpposite();
			
			EnumFacing opposite = face.getOpposite();
			
			for (RenderInfo ri : renderInfoList) {
				renderPoint(builder, face, u, v, other, ri, true, false, locationFormula, faceFormula, access, shadeTypes);
				renderPoint(builder, face, u, v, other, ri, true, true, locationFormula, faceFormula, access, shadeTypes);
				renderPoint(builder, face, u, v, other, ri, false, true, locationFormula, faceFormula, access, shadeTypes);
				renderPoint(builder, face, u, v, other, ri, false, false, locationFormula, faceFormula, access, shadeTypes);
				
				renderPoint(builder, opposite, u, v, other, ri, false, false, locationFormula, faceFormula, access, shadeTypes);
				renderPoint(builder, opposite, u, v, other, ri, false, true, locationFormula, faceFormula, access, shadeTypes);
				renderPoint(builder, opposite, u, v, other, ri, true, true, locationFormula, faceFormula, access, shadeTypes);
				renderPoint(builder, opposite, u, v, other, ri, true, false, locationFormula, faceFormula, access, shadeTypes);
			}
		}
		
		private static void renderPoint(BufferBuilder builder, EnumFacing face, Axis u, Axis v, double other, RenderInfo ri, boolean minU, boolean minV, IBlockLocation locationFormula, IFacingLocation faceFormula, IBlockAccess access, EnumShadeArgument shadeTypes) {
			int U_ARRAY = minU ? U_MIN : U_MAX;
			int V_ARRAY = minV ? V_MIN : V_MAX;
			
			Vec3d vertex = withValue(VEC_ZERO, u, ri.xyz[U_ARRAY]);
			vertex = withValue(vertex, v, ri.xyz[V_ARRAY]);
			vertex = withValue(vertex, face.getAxis(), other);
			
			builder.pos(vertex.x, vertex.y, vertex.z);
			builder.tex(ri.uv[U_ARRAY], ri.uv[V_ARRAY]);
			
			if (shadeTypes.isEnabled(EnumShadeType.FACE)) {
				setWorldRendererRGB(builder, AMBIENT_OCCLUSION_MAP.get(faceFormula.transformToWorld(face)));
			}
			
			if (shadeTypes.isEnabled(EnumShadeType.AMBIENT_OCCLUSION)) {
				applyLocalAO(builder, faceFormula.transformToWorld(face), locationFormula, access, shadeTypes, vertex);
			}
			else if (shadeTypes.isEnabled(EnumShadeType.LIGHT)) {
				Vec3d transVertex = locationFormula.transformToWorld(vertex);
				BlockPos pos = convertFloor(transVertex);
				IBlockState block = access.getBlockState(pos);
				int combinedLight = block.getPackedLightmapCoords(access, pos);
				builder.lightmap(combinedLight >> 16 & 65535, combinedLight & 65535);
			}
			
			builder.endVertex();
		}
		
		private static void applyLocalAO(BufferBuilder builder, EnumFacing face, IBlockLocation locationFormula, IBlockAccess access, EnumShadeArgument shadeTypes, Vec3d vertex) {
			boolean allAround = false;
			
			int numPositions = allAround ? 7 : 5;
			int[] skyLight = new int[numPositions];
			int[] blockLight = new int[numPositions];
			float[] colorMultiplier = new float[numPositions];
			double[] distances = new double[numPositions];
			double totalDist = 0;
			Vec3d transVertex = locationFormula.transformToWorld(vertex);
			BlockPos pos = convertFloor(transVertex);
			IBlockState state = access.getBlockState(pos);
			int combinedLight = state.getPackedLightmapCoords(access, pos);
			
			skyLight[0] = combinedLight / 0x10000;
			blockLight[0] = combinedLight % 0x10000;
			colorMultiplier[0] = state.getAmbientOcclusionLightValue();
			distances[0] = transVertex.distanceTo(convertToCenter(pos));
			
			int index = 0;
			EnumFacing[] testArray = allAround ? EnumFacing.VALUES : getNeighbours(face);
			for (EnumFacing otherFace : testArray) {
				Vec3d nearestOther = vertex.add(convert(otherFace));
				pos = convertFloor(locationFormula.transformToWorld(nearestOther));
				state = access.getBlockState(pos);
				combinedLight = state.getPackedLightmapCoords(access, pos);
				
				index++;
				
				skyLight[index] = combinedLight / 0x10000;
				blockLight[index] = combinedLight % 0x10000;
				colorMultiplier[index] = state.getAmbientOcclusionLightValue();
				distances[index] = 1 / (transVertex.distanceTo(convertToCenter(pos)) + 0.1);
				totalDist += distances[index];
			}
			
			double avgBlockLight = 0;
			double avgSkyLight = 0;
			float avgColorMultiplier = 0;
			for (int i = 0; i < numPositions; i++) {
				double part = distances[i] / totalDist;
				avgBlockLight += blockLight[i] * part;
				avgSkyLight += skyLight[i] * part;
				avgColorMultiplier += (float) (colorMultiplier[i] * part);
			}
			
			if (shadeTypes.isEnabled(EnumShadeType.LIGHT)) {
				int capBlockLight = NCMath.toInt(avgBlockLight);
				int capSkyLight = NCMath.toInt(avgSkyLight);
				builder.lightmap(capBlockLight, capSkyLight);
			}
			
			Vec3d color;
			if (shadeTypes.isEnabled(EnumShadeType.FACE)) {
				color = AMBIENT_OCCLUSION_MAP.get(face);
			}
			else {
				color = VEC_ONE;
			}
			color = multiply(color, avgColorMultiplier);
			setWorldRendererRGB(builder, color);
		}
		
		public static void renderStatic(List<BakedQuad> quads, BlockModelCuboid model) {
			TextureAtlasSprite[] sprites = model.textures;
			
			int[] flips = model.textureFlips;
			if (flips == null) {
				flips = new int[6];
			}
			
			double textureStartX = model.textureStartX / 16D;
			double textureStartY = model.textureStartY / 16D;
			double textureStartZ = model.textureStartZ / 16D;
			
			double textureSizeX = model.textureSizeX / 16D;
			double textureSizeY = model.textureSizeY / 16D;
			double textureSizeZ = model.textureSizeZ / 16D;
			
			double textureEndX = textureSizeX + textureStartX;
			double textureEndY = textureSizeY + textureStartY;
			double textureEndZ = textureSizeZ + textureStartZ;
			
			double textureOffsetX = model.textureOffsetX / 16D;
			double textureOffsetY = model.textureOffsetY / 16D;
			double textureOffsetZ = model.textureOffsetZ / 16D;
			
			double sizeX = model.sizeX();
			double sizeY = model.sizeY();
			double sizeZ = model.sizeZ();
			
			if (sprites[0] != null) {
				// Down
				float[] uv = getUVArray(sprites[0], flips[0], textureStartX, textureEndX, textureStartZ, textureEndZ);
				for (RenderInfo ri : getRenderInfos(uv, sizeX, sizeZ, textureSizeX, textureSizeZ, textureOffsetX, textureOffsetZ)) {
					double[][] arr = new double[4][];
					arr[0] = new double[] {ri.xyz[U_MAX], 0D, ri.xyz[V_MIN], -1, ri.uv[U_MAX], ri.uv[V_MIN], 0};
					arr[1] = new double[] {ri.xyz[U_MAX], 0D, ri.xyz[V_MAX], -1, ri.uv[U_MAX], ri.uv[V_MAX], 0};
					arr[2] = new double[] {ri.xyz[U_MIN], 0D, ri.xyz[V_MAX], -1, ri.uv[U_MIN], ri.uv[V_MAX], 0};
					arr[3] = new double[] {ri.xyz[U_MIN], 0D, ri.xyz[V_MIN], -1, ri.uv[U_MIN], ri.uv[V_MIN], 0};
					convertToDoubleQuads(quads, arr, EnumFacing.DOWN, sprites[0]);
				}
			}
			
			if (sprites[1] != null) {
				// Up
				float[] uv = getUVArray(sprites[1], flips[1], textureStartX, textureEndX, textureStartZ, textureEndZ);
				
				for (RenderInfo ri : getRenderInfos(uv, sizeX, sizeZ, textureSizeX, textureSizeZ, textureOffsetX, textureOffsetZ)) {
					double[][] arr = new double[4][];
					arr[0] = new double[] {ri.xyz[U_MAX], sizeY, ri.xyz[V_MIN], -1, ri.uv[U_MAX], ri.uv[V_MIN], 0};
					arr[1] = new double[] {ri.xyz[U_MAX], sizeY, ri.xyz[V_MAX], -1, ri.uv[U_MAX], ri.uv[V_MAX], 0};
					arr[2] = new double[] {ri.xyz[U_MIN], sizeY, ri.xyz[V_MAX], -1, ri.uv[U_MIN], ri.uv[V_MAX], 0};
					arr[3] = new double[] {ri.xyz[U_MIN], sizeY, ri.xyz[V_MIN], -1, ri.uv[U_MIN], ri.uv[V_MIN], 0};
					convertToDoubleQuads(quads, arr, EnumFacing.UP, sprites[1]);
				}
			}
			
			if (sprites[2] != null) {
				// North (-Z)
				float[] uv = getUVArray(sprites[2], flips[2], textureStartX, textureEndX, textureStartY, textureEndY);
				
				for (RenderInfo ri : getRenderInfos(uv, sizeX, sizeY, textureSizeX, textureSizeY, textureOffsetX, textureOffsetY)) {
					double[][] arr = new double[4][];
					arr[0] = new double[] {ri.xyz[U_MAX], ri.xyz[V_MIN], 0D, -1, ri.uv[U_MAX], ri.uv[V_MIN], 0};
					arr[1] = new double[] {ri.xyz[U_MAX], ri.xyz[V_MAX], 0D, -1, ri.uv[U_MAX], ri.uv[V_MAX], 0};
					arr[2] = new double[] {ri.xyz[U_MIN], ri.xyz[V_MAX], 0D, -1, ri.uv[U_MIN], ri.uv[V_MAX], 0};
					arr[3] = new double[] {ri.xyz[U_MIN], ri.xyz[V_MIN], 0D, -1, ri.uv[U_MIN], ri.uv[V_MIN], 0};
					convertToDoubleQuads(quads, arr, EnumFacing.NORTH, sprites[2]);
				}
			}
			
			if (sprites[3] != null) {
				// South (+Z)
				float[] uv = getUVArray(sprites[3], flips[3], textureStartX, textureEndX, textureStartY, textureEndY);
				
				for (RenderInfo ri : getRenderInfos(uv, sizeX, sizeY, textureSizeX, textureSizeY, textureOffsetX, textureOffsetY)) {
					double[][] arr = new double[4][];
					arr[0] = new double[] {ri.xyz[U_MAX], ri.xyz[V_MIN], sizeZ, -1, ri.uv[U_MAX], ri.uv[V_MIN], 0};
					arr[1] = new double[] {ri.xyz[U_MAX], ri.xyz[V_MAX], sizeZ, -1, ri.uv[U_MAX], ri.uv[V_MAX], 0};
					arr[2] = new double[] {ri.xyz[U_MIN], ri.xyz[V_MAX], sizeZ, -1, ri.uv[U_MIN], ri.uv[V_MAX], 0};
					arr[3] = new double[] {ri.xyz[U_MIN], ri.xyz[V_MIN], sizeZ, -1, ri.uv[U_MIN], ri.uv[V_MIN], 0};
					convertToDoubleQuads(quads, arr, EnumFacing.SOUTH, sprites[3]);
				}
			}
			
			if (sprites[4] != null) {
				// West (-X)
				float[] uv = getUVArray(sprites[4], flips[4], textureStartZ, textureEndZ, textureStartY, textureEndY);
				
				for (RenderInfo ri : getRenderInfos(uv, sizeZ, sizeY, textureSizeZ, textureSizeY, textureOffsetZ, textureOffsetY)) {
					double[][] arr = new double[4][];
					arr[0] = new double[] {0D, ri.xyz[V_MIN], ri.xyz[U_MAX], -1, ri.uv[U_MAX], ri.uv[V_MIN], 0};
					arr[1] = new double[] {0D, ri.xyz[V_MAX], ri.xyz[U_MAX], -1, ri.uv[U_MAX], ri.uv[V_MAX], 0};
					arr[2] = new double[] {0D, ri.xyz[V_MAX], ri.xyz[U_MIN], -1, ri.uv[U_MIN], ri.uv[V_MAX], 0};
					arr[3] = new double[] {0D, ri.xyz[V_MIN], ri.xyz[U_MIN], -1, ri.uv[U_MIN], ri.uv[V_MIN], 0};
					convertToDoubleQuads(quads, arr, EnumFacing.WEST, sprites[4]);
				}
			}
			
			if (sprites[5] != null) {
				// East (+X)
				float[] uv = getUVArray(sprites[5], flips[5], textureStartZ, textureEndZ, textureStartY, textureEndY);
				
				for (RenderInfo ri : getRenderInfos(uv, sizeZ, sizeY, textureSizeZ, textureSizeY, textureOffsetZ, textureOffsetY)) {
					double[][] arr = new double[4][];
					arr[0] = new double[] {sizeX, ri.xyz[V_MIN], ri.xyz[U_MAX], -1, ri.uv[U_MAX], ri.uv[V_MIN], 0};
					arr[1] = new double[] {sizeX, ri.xyz[V_MAX], ri.xyz[U_MAX], -1, ri.uv[U_MAX], ri.uv[V_MAX], 0};
					arr[2] = new double[] {sizeX, ri.xyz[V_MAX], ri.xyz[U_MIN], -1, ri.uv[U_MIN], ri.uv[V_MAX], 0};
					arr[3] = new double[] {sizeX, ri.xyz[V_MIN], ri.xyz[U_MIN], -1, ri.uv[U_MIN], ri.uv[V_MIN], 0};
					convertToDoubleQuads(quads, arr, EnumFacing.EAST, sprites[5]);
				}
			}
		}
		
		private static void convertToDoubleQuads(List<BakedQuad> quads, double[][] points, EnumFacing face, TextureAtlasSprite sprite) {
			BakedQuad quad = convertToQuad(points, face, sprite);
			quads.add(quad);
			
			double[][] otherPoints = new double[][] {points[3], points[2], points[1], points[0]};
			quad = convertToQuad(otherPoints, face, sprite);
			quads.add(quad);
		}
		
		private static BakedQuad convertToQuad(double[][] points, EnumFacing face, TextureAtlasSprite sprite) {
			int[] list = new int[points.length * points[0].length];
			for (int i = 0; i < points.length; i++) {
				double[] arr = points[i];
				for (int j = 0; j < arr.length; j++) {
					double d = arr[j];
					int used;
					if (j == 3 || j == 6) {
						used = NCMath.toInt(d);
					}
					else {
						used = Float.floatToRawIntBits((float) d);
					}
					list[i * arr.length + j] = used;
				}
			}
			return new BakedQuad(list, -1, face, sprite, true, DefaultVertexFormats.ITEM);
		}
		
		private static float[] getUVArray(TextureAtlasSprite sprite, int flips, double startU, double endU, double startV, double endV) {
			float minU = sprite.getInterpolatedU(startU * 16);
			float maxU = sprite.getInterpolatedU(endU * 16);
			float minV = sprite.getInterpolatedV(startV * 16);
			float maxV = sprite.getInterpolatedV(endV * 16);
			float[] uvArray = new float[] {minU, maxU, minV, maxV};
			if (flips % 2 == 1) {
				float holder = uvArray[0];
				uvArray[0] = uvArray[1];
				uvArray[1] = holder;
			}
			if (flips >> 1 % 2 == 1) {
				float holder = uvArray[2];
				uvArray[2] = uvArray[3];
				uvArray[3] = holder;
			}
			return uvArray;
		}
		
		private static float[] getUVArray(TextureAtlasSprite sprite, int flips, EnumFacing face, Vec3d start, Vec3d end) {
			Axis u = face.getAxis() == Axis.X ? Axis.Z : Axis.X;
			Axis v = face.getAxis() == Axis.Y ? Axis.Z : Axis.Y;
			
			float minU = sprite.getInterpolatedU(getValue(start, u) * 16);
			float maxU = sprite.getInterpolatedU(getValue(end, u) * 16);
			float minV = sprite.getInterpolatedV(getValue(start, v) * 16);
			float maxV = sprite.getInterpolatedV(getValue(end, v) * 16);
			
			float[] uvArray = new float[] {minU, maxU, minV, maxV};
			if (flips % 2 == 1) {
				float holder = uvArray[0];
				uvArray[0] = uvArray[1];
				uvArray[1] = holder;
			}
			if (flips >> 1 % 2 == 1) {
				float holder = uvArray[2];
				uvArray[2] = uvArray[3];
				uvArray[3] = holder;
			}
			return uvArray;
		}
		
		private static List<RenderInfo> getRenderInfos(float[] uv, EnumFacing face, Vec3d size, Vec3d texSize, Vec3d texOffset) {
			Axis u = face.getAxis() == Axis.X ? Axis.Z : Axis.X;
			Axis v = face.getAxis() == Axis.Y ? Axis.Z : Axis.Y;
			
			double sizeU = getValue(size, u);
			double sizeV = getValue(size, v);
			double textureSizeU = getValue(texSize, u);
			double textureSizeV = getValue(texSize, v);
			double textureOffsetU = getValue(texOffset, u);
			double textureOffsetV = getValue(texOffset, v);
			
			return getRenderInfos(uv, sizeU, sizeV, textureSizeU, textureSizeV, textureOffsetU, textureOffsetV);
		}
		
		private static List<RenderInfo> getRenderInfos(float[] uv, double sizeU, double sizeV, double textureSizeU, double textureSizeV, double textureOffsetU, double textureOffsetV) {
			List<RenderInfo> infos = new ArrayList<>();
			boolean firstU = true;
			for (double u = 0; u < sizeU; u += textureSizeU) {
				float[] uvCu = Arrays.copyOf(uv, 4);
				double addU = textureSizeU;
				boolean lowerU = false;
				
				if (firstU && textureOffsetU != 0) {
					uvCu[U_MIN] = uvCu[U_MIN] + (uvCu[U_MAX] - uvCu[U_MIN]) * (float) textureOffsetU;
					addU -= textureOffsetU;
					// addU = 1 - textureOffsetU;
					lowerU = true;
				}
				
				if (u + addU > sizeU) {
					addU = sizeU - u;
					if (firstU && textureOffsetU != 0) {
						uvCu[U_MAX] = uvCu[U_MIN] + (uvCu[U_MAX] - uvCu[U_MIN]) * (float) (addU / (textureSizeU - textureOffsetU));
					}
					else {
						uvCu[U_MAX] = uvCu[U_MIN] + (uvCu[U_MAX] - uvCu[U_MIN]) * (float) (addU / textureSizeU);
					}
				}
				firstU = false;
				boolean firstV = true;
				for (double v = 0; v < sizeV; v += textureSizeV) {
					float[] uvCv = Arrays.copyOf(uvCu, 4);
					
					double addV = textureSizeV;
					
					boolean lowerV = false;
					
					if (firstV && textureOffsetV != 0) {
						uvCv[V_MIN] = uvCv[V_MIN] + (uvCv[V_MAX] - uvCv[V_MIN]) * (float) textureOffsetV;
						addV -= textureOffsetV;
						lowerV = true;
					}
					if (v + addV > sizeV) {
						addV = sizeV - v;
						if (firstV && textureOffsetV != 0) {
							uvCv[V_MAX] = uvCv[V_MIN] + (uvCv[V_MAX] - uvCv[V_MIN]) * (float) (addV / (textureSizeV - textureOffsetV));
						}
						else {
							uvCv[V_MAX] = uvCv[V_MIN] + (uvCv[V_MAX] - uvCv[V_MIN]) * (float) (addV / textureSizeV);
						}
					}
					
					double[] xyz = new double[4];
					xyz[U_MIN] = u;
					xyz[U_MAX] = u + addU;
					xyz[V_MIN] = v;
					xyz[V_MAX] = v + addV;
					infos.add(new RenderInfo(uvCv, xyz));
					
					if (lowerV) {
						v -= textureOffsetV;
					}
					firstV = false;
				}
				
				if (lowerU) {
					u -= textureOffsetU;
				}
			}
			return infos;
		}
		
		public enum DefaultFacingLocation implements IFacingLocation {
			INSTANCE;
			
			@Override
			public EnumFacing transformToWorld(EnumFacing face) {
				return face;
			}
		}
		
		public enum EnumShadeType {
			FACE(DefaultVertexFormats.COLOR_4UB),
			LIGHT(DefaultVertexFormats.TEX_2S),
			AMBIENT_OCCLUSION(DefaultVertexFormats.COLOR_4UB);
			
			private final VertexFormatElement element;
			
			EnumShadeType(VertexFormatElement element) {
				this.element = element;
			}
		}
		
		public enum EnumShadeArgument {
			NONE,
			FACE(EnumShadeType.FACE),
			FACE_LIGHT(EnumShadeType.FACE, EnumShadeType.LIGHT),
			FACE_OCCLUDE(EnumShadeType.FACE, EnumShadeType.AMBIENT_OCCLUSION),
			FACE_LIGHT_OCCLUDE(EnumShadeType.FACE, EnumShadeType.LIGHT, EnumShadeType.AMBIENT_OCCLUSION),
			LIGHT(EnumShadeType.LIGHT),
			LIGHT_OCCLUDE(EnumShadeType.LIGHT, EnumShadeType.AMBIENT_OCCLUSION),
			OCCLUDE(EnumShadeType.AMBIENT_OCCLUSION);
			
			public final ImmutableSet<EnumShadeType> types;
			final VertexFormat vertexFormat;
			
			EnumShadeArgument(EnumShadeType... types) {
				this.vertexFormat = new VertexFormat();
				vertexFormat.addElement(DefaultVertexFormats.POSITION_3F);
				vertexFormat.addElement(DefaultVertexFormats.TEX_2F);
				for (EnumShadeType type : types) {
					if (!vertexFormat.getElements().contains(type.element)) {
						vertexFormat.addElement(type.element);
					}
				}
				this.types = ImmutableSet.copyOf(types);
			}
			
			public boolean isEnabled(EnumShadeType type) {
				return types.contains(type);
			}
		}
		
		public interface IBlockLocation {
			
			Vec3d transformToWorld(Vec3d vec);
		}
		
		public interface IFacingLocation {
			
			EnumFacing transformToWorld(EnumFacing face);
		}
		
		private static class RenderInfo {
			
			private final float[] uv;
			private final double[] xyz;
			
			public RenderInfo(float[] uv, double[] xyz) {
				this.uv = uv;
				this.xyz = xyz;
			}
		}
	}
	
	class BlockModelCuboid {
		
		public double sizeX, sizeY, sizeZ;
		
		public double textureStartX = 0, textureStartY = 0, textureStartZ = 0;
		public double textureSizeX = 16, textureSizeY = 16, textureSizeZ = 16;
		public double textureOffsetX = 0, textureOffsetY = 0, textureOffsetZ = 0;
		
		public int[] textureFlips = new int[] {2, 2, 2, 2, 2, 2};
		
		public TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
		
		public boolean[] renderSides = new boolean[] {true, true, true, true, true, true, false};
		
		public final void setSize(double xSize, double ySize, double zSize) {
			sizeX = xSize;
			sizeY = ySize;
			sizeZ = zSize;
		}
		
		public double sizeX() {
			return sizeX;
		}
		
		public double sizeY() {
			return sizeY;
		}
		
		public double sizeZ() {
			return sizeZ;
		}
		
		public boolean getRenderSide(EnumFacing side) {
			return renderSides[side.ordinal()];
		}
		
		public void setRenderSide(EnumFacing side, boolean value) {
			renderSides[side.ordinal()] = value;
		}
		
		public TextureAtlasSprite getTexture(EnumFacing side) {
			return textures[side.ordinal()];
		}
		
		public void setTexture(TextureAtlasSprite tex) {
			Arrays.fill(textures, tex);
		}
		
		public void setTextures(TextureAtlasSprite down, TextureAtlasSprite up, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite west, TextureAtlasSprite east) {
			textures[0] = down;
			textures[1] = up;
			textures[2] = north;
			textures[3] = south;
			textures[4] = west;
			textures[5] = east;
		}
	}
}
