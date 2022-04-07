package nc.model;

import java.util.*;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.vecmath.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.*;
import com.google.common.collect.*;

import nc.Global;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.*;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.*;

public final class ModelTexturedFluid implements IModel {
	
	public static final ModelTexturedFluid WATER = new ModelTexturedFluid(new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"));
	protected final ResourceLocation still, flowing;
	
	public ModelTexturedFluid(ResourceLocation still, ResourceLocation flowing) {
		this.still = still;
		this.flowing = flowing;
	}
	
	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableSet.of(still, flowing);
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<TransformType, TRSRTransformation> map = PerspectiveMapWrapper.getTransforms(state);
		return new BakedFluidTextured(state.apply(Optional.empty()), map, format, bakedTextureGetter.apply(still), bakedTextureGetter.apply(flowing), Optional.empty());
	}
	
	@Override
	public IModelState getDefaultState() {
		return ModelRotation.X0_Y0;
	}
	
	public static enum FluidTexturedLoader implements ICustomModelLoader {
		
		INSTANCE;
		
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {}
		
		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation.getNamespace().equals(Global.MOD_ID) && (modelLocation.getPath().equals("fluid") || modelLocation.getPath().equals("models/block/fluid") || modelLocation.getPath().equals("models/item/fluid"));
		}
		
		@Override
		public IModel loadModel(ResourceLocation modelLocation) {
			return WATER;
		}
	}
	
	protected static final class BakedFluidTextured implements IBakedModel {
		
		protected static final int X[] = {0, 0, 1, 1};
		protected static final int Z[] = {0, 1, 1, 0};
		protected static final float eps = 0.001F;
		
		protected final LoadingCache<Long, BakedFluidTextured> modelCache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<Long, BakedFluidTextured>() {
			
			@Override
			public BakedFluidTextured load(Long key) throws Exception {
				int opacity = (int) (key & 0x3FF);
				key >>>= 10;
				boolean gas = (key & 1) != 0;
				key >>>= 1;
				boolean statePresent = (key & 1) != 0;
				key >>>= 1;
				int[] cornerRound = new int[4];
				for (int i = 0; i < 4; ++i) {
					cornerRound[i] = (int) (key & 0x3FF);
					key >>>= 10;
				}
				int flowRound = (int) (key & 0x7FF) - 1024;
				return new BakedFluidTextured(transformation, transforms, format, opacity, still, flowing, gas, statePresent, cornerRound, flowRound);
			}
		});
		
		protected final Optional<TRSRTransformation> transformation;
		protected final ImmutableMap<TransformType, TRSRTransformation> transforms;
		protected final VertexFormat format;
		protected final TextureAtlasSprite still, flowing;
		protected final EnumMap<EnumFacing, List<BakedQuad>> faceQuads;
		
		public BakedFluidTextured(Optional<TRSRTransformation> transformation, ImmutableMap<TransformType, TRSRTransformation> transforms, VertexFormat format, TextureAtlasSprite still, TextureAtlasSprite flowing, Optional<IExtendedBlockState> stateOption) {
			this(transformation, transforms, format, 0xFF, still, flowing, false, stateOption.isPresent(), getCorners(stateOption), getFlow(stateOption));
		}
		
		private static int[] getCorners(Optional<IExtendedBlockState> stateOption) {
			int[] cornerRound = new int[] {0, 0, 0, 0};
			if (stateOption.isPresent()) {
				IExtendedBlockState state = stateOption.get();
				for (int i = 0; i < 4; ++i) {
					Float level = state.getValue(BlockFluidBase.LEVEL_CORNERS[i]);
					cornerRound[i] = Math.round((level == null ? 7F / 8 : level) * 768);
				}
			}
			return cornerRound;
		}
		
		protected static int getFlow(Optional<IExtendedBlockState> stateOption) {
			Float flow = -1000F;
			if (stateOption.isPresent()) {
				flow = stateOption.get().getValue(BlockFluidBase.FLOW_DIRECTION);
				if (flow == null) {
					flow = -1000F;
				}
			}
			int flowRound = (int) Math.round(Math.toDegrees(flow));
			flowRound = MathHelper.clamp(flowRound, -1000, 1000);
			return flowRound;
		}
		
		public BakedFluidTextured(Optional<TRSRTransformation> transformation, ImmutableMap<TransformType, TRSRTransformation> transforms, VertexFormat format, int opacity, TextureAtlasSprite still, TextureAtlasSprite flowing, boolean gas, boolean statePresent, int[] cornerRound, int flowRound) {
			this.transformation = transformation;
			this.transforms = transforms;
			this.format = format;
			this.still = still;
			this.flowing = flowing;
			
			faceQuads = Maps.newEnumMap(EnumFacing.class);
			for (EnumFacing side : EnumFacing.VALUES) {
				faceQuads.put(side, ImmutableList.of());
			}
			
			if (statePresent) {
				float[] y = new float[4];
				for (int i = 0; i < 4; ++i) {
					if (gas) {
						y[i] = 1 - cornerRound[i] / 768F;
					}
					else {
						y[i] = cornerRound[i] / 768F;
					}
				}
				
				float flow = (float) Math.toRadians(flowRound);
				
				TextureAtlasSprite topSprite = flowing;
				float scale = 4;
				if (flow < -17F) {
					flow = 0;
					scale = 8;
					topSprite = still;
				}
				
				float c = MathHelper.cos(flow) * scale;
				float s = MathHelper.sin(flow) * scale;
				
				EnumFacing side = gas ? EnumFacing.DOWN : EnumFacing.UP;
				UnpackedBakedQuad.Builder builder;
				ImmutableList.Builder<BakedQuad> topFaceBuilder = ImmutableList.builder();
				for (int k = 0; k < 2; ++k) {
					builder = new UnpackedBakedQuad.Builder(format);
					builder.setQuadOrientation(side);
					builder.setTexture(topSprite);
					builder.setQuadTint(0);
					for (int i = gas ? 3 : 0; i != (gas ? -1 : 4); i += gas ? -1 : 1) {
						int l = k * 3 + (1 - 2 * k) * i;
						putVertex(builder, side, X[l], y[l], Z[l], topSprite.getInterpolatedU(8 + c * (X[l] * 2 - 1) + s * (Z[l] * 2 - 1)), topSprite.getInterpolatedV(8 + c * (X[(l + 1) % 4] * 2 - 1) + s * (Z[(l + 1) % 4] * 2 - 1)), opacity);
					}
					topFaceBuilder.add(builder.build());
				}
				faceQuads.put(side, topFaceBuilder.build());
				
				side = side.getOpposite();
				builder = new UnpackedBakedQuad.Builder(format);
				builder.setQuadOrientation(side);
				builder.setTexture(still);
				builder.setQuadTint(0);
				for (int i = gas ? 3 : 0; i != (gas ? -1 : 4); i += gas ? -1 : 1) {
					putVertex(builder, side, Z[i], gas ? 1 : 0, X[i], still.getInterpolatedU(Z[i] * 16), still.getInterpolatedV(X[i] * 16), opacity);
				}
				faceQuads.put(side, ImmutableList.of(builder.build()));
				
				for (int i = 0; i < 4; ++i) {
					side = EnumFacing.byHorizontalIndex((5 - i) % 4);
					BakedQuad q[] = new BakedQuad[2];
					
					for (int k = 0; k < 2; ++k) {
						builder = new UnpackedBakedQuad.Builder(format);
						builder.setQuadOrientation(side);
						builder.setTexture(flowing);
						builder.setQuadTint(0);
						for (int j = 0; j < 4; ++j) {
							int l = k * 3 + (1 - 2 * k) * j;
							float yl = Z[l] * y[(i + X[l]) % 4];
							if (gas && Z[l] == 0) {
								yl = 1;
							}
							putVertex(builder, side, X[(i + X[l]) % 4], yl, Z[(i + X[l]) % 4], flowing.getInterpolatedU(X[l] * 8), flowing.getInterpolatedV((gas ? yl : 1 - yl) * 8), opacity);
						}
						q[k] = builder.build();
					}
					faceQuads.put(side, ImmutableList.of(q[0], q[1]));
				}
			}
			else {
				UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
				builder.setQuadOrientation(EnumFacing.UP);
				builder.setTexture(still);
				builder.setQuadTint(0);
				for (int i = 0; i < 4; ++i) {
					putVertex(builder, EnumFacing.UP, Z[i], X[i], 0, still.getInterpolatedU(Z[i] * 16), still.getInterpolatedV(X[i] * 16), opacity);
				}
				faceQuads.put(EnumFacing.SOUTH, ImmutableList.of(builder.build()));
			}
		}
		
		protected void putVertex(UnpackedBakedQuad.Builder builder, EnumFacing side, float x, float y, float z, float u, float v, int opacity) {
			for (int e = 0; e < format.getElementCount(); ++e) {
				switch (format.getElement(e).getUsage()) {
					case POSITION:
						float[] data = new float[] {x - side.getDirectionVec().getX() * eps, y, z - side.getDirectionVec().getZ() * eps, 1};
						if (transformation.isPresent() && transformation.get() != TRSRTransformation.identity()) {
							Vector4f vec = new Vector4f(data);
							transformation.get().getMatrix().transform(vec);
							vec.get(data);
						}
						builder.put(e, data);
						break;
					case COLOR:
						builder.put(e, 1F, 1F, 1F, opacity / 255F);
						break;
					case UV:
						if (format.getElement(e).getIndex() == 0) {
							builder.put(e, u, v, 0F, 1F);
							break;
						}
						//$FALL-THROUGH$
					case NORMAL:
						builder.put(e, side.getXOffset(), side.getYOffset(), side.getZOffset(), 0F);
						break;
					default:
						builder.put(e);
						break;
				}
			}
		}
		
		@Override
		public boolean isAmbientOcclusion() {
			return true;
		}
		
		@Override
		public boolean isGui3d() {
			return false;
		}
		
		@Override
		public boolean isBuiltInRenderer() {
			return false;
		}
		
		@Override
		public TextureAtlasSprite getParticleTexture() {
			return still;
		}
		
		@Override
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
			BakedFluidTextured model = this;
			if (state instanceof IExtendedBlockState) {
				IExtendedBlockState exState = (IExtendedBlockState) state;
				int[] cornerRound = getCorners(Optional.of(exState));
				int flowRound = getFlow(Optional.of(exState));
				long key = flowRound + 1024;
				for (int i = 3; i >= 0; --i) {
					key <<= 10;
					key |= cornerRound[i];
				}
				key <<= 1;
				key |= 1;
				Fluid fluid = ((IFluidBlock) state.getBlock()).getFluid();
				key <<= 1;
				key |= fluid.isGaseous() ? 1 : 0;
				key <<= 10;
				key |= fluid.getColor() >>> 24;
				model = modelCache.getUnchecked(key);
			}
			if (side == null) {
				return ImmutableList.of();
			}
			return model.faceQuads.get(side);
		}
		
		@Override
		public ItemOverrideList getOverrides() {
			return ItemOverrideList.NONE;
		}
		
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type) {
			return PerspectiveMapWrapper.handlePerspective(this, transforms, type);
		}
	}
	
	@Override
	public IModel retexture(ImmutableMap<String, String> textures) {
		ResourceLocation stillTexture = still;
		ResourceLocation flowingTexture = flowing;
		if (textures.containsKey("still")) {
			stillTexture = new ResourceLocation(textures.get("still"));
		}
		if (textures.containsKey("flowing")) {
			flowingTexture = new ResourceLocation(textures.get("flowing"));
		}
		return new ModelTexturedFluid(stillTexture, flowingTexture);
	}
}
