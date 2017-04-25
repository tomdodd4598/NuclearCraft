package me.modmuss50.jsonDestroyer.client;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.modmuss50.jsonDestroyer.JsonDestroyer;
import me.modmuss50.jsonDestroyer.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Matrix4f;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static net.minecraft.network.status.server.SPacketServerInfo.GSON;

public class ModelGenerator {

    private FaceBakery faceBakery = new FaceBakery();

    private JsonDestroyer jsonDestroyer;

    public ArrayList<BlockIconInfo> blockIconInfoList = new ArrayList<>();

    public HashMap<BlockIconInfo, TextureAtlasSprite> blockIconList = new HashMap<>();
    public HashMap<Fluid, TextureAtlasSprite> fluidIcons = new HashMap<>();
    public List<ItemIconInfo> itemIcons = new ArrayList<>();

    public ModelGenerator(JsonDestroyer jsonDestroyer) {
        this.jsonDestroyer = jsonDestroyer;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void textureStitch(TextureStitchEvent.Pre event) {
        blockIconInfoList.clear();
        fluidIcons.clear();
        itemIcons.clear();
        TextureMap textureMap = event.getMap();
        for (Object object : jsonDestroyer.objectsToDestroy) {
            if (object instanceof Block && object instanceof ITexturedBlock) {
                ITexturedBlock blockTextureProvider = (ITexturedBlock) object;
                Block block = (Block) object;
                for (int i = 0; i < blockTextureProvider.amountOfStates(); i++) {
                    for (EnumFacing side : EnumFacing.values()) {
                        String name;
                        name = blockTextureProvider.getTextureNameFromState(block.getStateFromMeta(i), side);
                        TextureAtlasSprite texture = textureMap.getTextureExtry(name);
                        if (texture == null) {
                            texture = new CustomTexture(name);
                            textureMap.setTextureEntry(texture);
                        }
                        BlockIconInfo iconInfo = new BlockIconInfo(block, i, side);
                        blockIconList.put(iconInfo, texture);
                        blockIconInfoList.add(iconInfo);
                    }
                }
            } else if (object instanceof BlockFluidBase && object instanceof ITexturedFluid) {
                BlockFluidBase blockFluidBase = (BlockFluidBase) object;
                String name = blockFluidBase.getFluid().getFlowing().toString();
                TextureAtlasSprite texture = textureMap.getTextureExtry(name);
                if (texture == null) {
                    texture = new CustomTexture(name);
                    textureMap.setTextureEntry(texture);
                }
                fluidIcons.put(blockFluidBase.getFluid(), texture);
            } else if (object instanceof Item && object instanceof ITexturedItem) {
                ITexturedItem itemTexture = (ITexturedItem) object;
                Item item = (Item) object;
                for (int i = 0; i < itemTexture.getMaxMeta(); i++) {
                    String name = itemTexture.getTextureName(i);
                    TextureAtlasSprite texture = textureMap.getTextureExtry(name);
                    if (texture == null) {
                        texture = new CustomTexture(name);
                        textureMap.setTextureEntry(texture);
                    }
                    ItemIconInfo info = new ItemIconInfo((Item) object, i, texture, name);
                    itemIcons.add(info);
                }
            } else if (object instanceof Item && object instanceof ITexturedBucket) {
                ITexturedBucket itemTexture = (ITexturedBucket) object;
                for (int i = 0; i < itemTexture.getMaxMeta(); i++) {
                    String name = itemTexture.getFluid(i).getStill().toString();
                    TextureAtlasSprite texture = textureMap.getTextureExtry(name);
                    if (texture == null) {
                        texture = new CustomTexture(name);
                        textureMap.setTextureEntry(texture);
                    }
                    ItemIconInfo info = new ItemIconInfo((Item) object, i, texture, name);
                    info.isBucket = true;
                    itemIcons.add(info);
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void bakeModels(ModelBakeEvent event) {
        ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        for (Object object : jsonDestroyer.objectsToDestroy) {
            if (object instanceof Block && object instanceof ITexturedBlock) {
                ITexturedBlock textureProvdier = (ITexturedBlock) object;
                Block block = (Block) object;
                for (int i = 0; i < textureProvdier.amountOfStates(); i++) {
                    HashMap<EnumFacing, TextureAtlasSprite> textureMap = new HashMap<>();
                    for (EnumFacing side : EnumFacing.VALUES) {
                        for (BlockIconInfo iconInfo : blockIconInfoList) {
                            if (iconInfo.getBlock() == block && iconInfo.getMeta() == i && iconInfo.getSide() == side) {
                                if (blockIconList.containsKey(iconInfo))
                                    textureMap.put(side, blockIconList.get(iconInfo));
                            }
                        }
                    }
                    if (textureMap.isEmpty()) {
                        return;
                    }

                    ItemCameraTransforms transforms = null;
                    try {
                        transforms = ModelHelper.loadTransformFromJson(new ResourceLocation("minecraft:models/block/block"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = IPerspectiveAwareModel.MapWrapper.getTransforms(transforms);

                    BlockModel model = new BlockModel(textureMap, block.getStateFromMeta(i).getBlock() instanceof IOpaqueBlock, map);
                    ModelResourceLocation modelResourceLocation = getModelResourceLocation(block.getStateFromMeta(i));

                    event.getModelRegistry().putObject(modelResourceLocation, model);
                    ModelResourceLocation inventory = getBlockInventoryResourceLocation(block);
                    event.getModelRegistry().putObject(inventory, model);
                    itemModelMesher.register(Item.getItemFromBlock(block), i, inventory);
                    event.getModelRegistry().putObject(modelResourceLocation, model);
                    itemModelMesher.register(Item.getItemFromBlock(block), i, modelResourceLocation);
                }
            } else if (object instanceof ITexturedFluid && object instanceof BlockFluidBase) {
                final BlockFluidBase blockFluidBase = (BlockFluidBase) object;
                Fluid fluid = blockFluidBase.getFluid();
                final ModelResourceLocation fluidLocation = new ModelResourceLocation(fluid.getFlowing(), "fluid");

                Item fluidItem = Item.getItemFromBlock(blockFluidBase);
                ModelBakery.registerItemVariants(fluidItem);
                ModelLoader.setCustomMeshDefinition(fluidItem, stack -> fluidLocation);
//                ModelLoader.setCustomStateMapper(fluid, new StateMapperBase() {
//                    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                        return fluidLocation;
//                    }
//                });

                for (int i = 0; i < 16; i++) {
                    ModelResourceLocation location = new ModelResourceLocation(getBlockResourceLocation(blockFluidBase), "level=" + i);
                    ModelFluid modelFluid = new ModelFluid(fluid);
                    Function<ResourceLocation, TextureAtlasSprite> textureGetter = location1 -> fluidIcons.get(fluid);
                    IBakedModel bakedModel = modelFluid.bake(modelFluid.getDefaultState(), DefaultVertexFormats.BLOCK, textureGetter);

                    event.getModelRegistry().putObject(location, bakedModel);
                }
                ModelResourceLocation inventoryLocation = new ModelResourceLocation(getBlockResourceLocation(blockFluidBase), "inventory");
                ModelFluid modelFluid = new ModelFluid(fluid);
                Function<ResourceLocation, TextureAtlasSprite> textureGetter = location -> fluidIcons.get(fluid);
                IBakedModel bakedModel = modelFluid.bake(modelFluid.getDefaultState(), DefaultVertexFormats.ITEM, textureGetter);

                event.getModelRegistry().putObject(inventoryLocation, bakedModel);
            } else if (object instanceof Item && object instanceof ITexturedItem) {
                ITexturedItem iTexturedItem = (ITexturedItem) object;
                Item item = (Item) object;
                for (int i = 0; i < iTexturedItem.getMaxMeta(); i++) {
                    TextureAtlasSprite texture = null;
                    ItemIconInfo itemIconInfo = null;
                    for (ItemIconInfo info : itemIcons) {
                        if (info.damage == i && info.getItem() == item && info.isBucket == false) {
                            texture = info.getSprite();
                            itemIconInfo = info;
                            break;
                        }
                    }
                    if (texture == null) {
                        break;
                    }

                    ModelResourceLocation inventory;
                    inventory = getItemInventoryResourceLocation(item);

                    if (iTexturedItem.getMaxMeta() != 1) {
                        if (iTexturedItem.getModel(new ItemStack(item, 1, i), Minecraft.getMinecraft().player, 0) != null) {
                            inventory = iTexturedItem.getModel(new ItemStack(item, 1, i), Minecraft.getMinecraft().player, 0);
                            ModelLoader.registerItemVariants(item, inventory);
                        }
                    }


                    final TextureAtlasSprite finalTexture = texture;
                    Function<ResourceLocation, TextureAtlasSprite> textureGetter = location -> finalTexture;
                    ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
                    builder.add(new ResourceLocation(itemIconInfo.textureName));
                    ItemLayerModel itemLayerModel = new ItemLayerModel(builder.build());
                    IBakedModel model = itemLayerModel.bake(ItemLayerModel.INSTANCE.getDefaultState(), DefaultVertexFormats.ITEM, textureGetter);


                    ItemCameraTransforms transforms = null;
                    try {
                        if (item instanceof IHandHeld) {
                            transforms = ModelHelper.loadTransformFromJson(new ResourceLocation("minecraft:models/item/handheld"));
                        } else {
                            transforms = ModelHelper.loadTransformFromJson(new ResourceLocation("minecraft:models/item/generated"));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = IPerspectiveAwareModel.MapWrapper.getTransforms(transforms);
                    IPerspectiveAwareModel iPerspectiveAwareModel = new IPerspectiveAwareModel.MapWrapper(model, map);
                    itemModelMesher.register(item, i, inventory);
                    event.getModelRegistry().putObject(inventory, iPerspectiveAwareModel);
                }
            } else if (object instanceof Item && object instanceof ITexturedBucket) {
                ITexturedBucket iTexturedBucket = (ITexturedBucket) object;
                Item item = (Item) object;
                for (int i = 0; i < iTexturedBucket.getMaxMeta(); i++) {
                    ModelResourceLocation inventory;
                    inventory = getItemInventoryResourceLocation(item);
                    if (iTexturedBucket.getMaxMeta() != 1) {
                        if (iTexturedBucket.getModel(new ItemStack(item, 1, i), Minecraft.getMinecraft().player, 0) != null) {
                            inventory = iTexturedBucket.getModel(new ItemStack(item, 1, i), Minecraft.getMinecraft().player, 0);
                        }
                    }
                    Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                    textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    ModelDynBucket modelDynBucket = new ModelDynBucket(new ResourceLocation("forge:items/bucket_base"), new ResourceLocation("forge:items/bucket_fluid"), new ResourceLocation("forge:items/bucket_cover"), iTexturedBucket.getFluid(i), iTexturedBucket.isGas(i));

                    IBakedModel model = modelDynBucket.bake(ItemLayerModel.INSTANCE.getDefaultState(), DefaultVertexFormats.ITEM, textureGetter);
                    itemModelMesher.register(item, i, inventory);
                    event.getModelRegistry().putObject(inventory, model);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), (new DefaultStateMapper()).getPropertyString(state.getProperties()));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getBlockInventoryResourceLocation(Block block) {
        return new ModelResourceLocation(Block.REGISTRY.getNameForObject(block), "inventory");
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getItemInventoryResourceLocation(Item block) {
        return new ModelResourceLocation(Item.REGISTRY.getNameForObject(block), "inventory");
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getBlockResourceLocation(Block block) {
        return Block.REGISTRY.getNameForObject(block);
    }

    public class CustomTexture extends TextureAtlasSprite {
        public CustomTexture(String spriteName) {
            super(spriteName);
        }
    }

    //BLOCK

    public class BlockIconInfo {
        public Block block;
        public int meta;
        public EnumFacing side;

        public BlockIconInfo(Block block, int meta, EnumFacing side) {
            this.block = block;
            this.meta = meta;
            this.side = side;
        }

        public Block getBlock() {
            return block;
        }

        public int getMeta() {
            return meta;
        }

        public EnumFacing getSide() {
            return side;
        }

    }

    public class BlockModel implements IPerspectiveAwareModel {
        HashMap<EnumFacing, TextureAtlasSprite> textureAtlasSpriteHashMap;
        boolean isOpaque;
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;

        public BlockModel(HashMap<EnumFacing, TextureAtlasSprite> textureAtlasSpriteHashMap, boolean isOpaque, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
            this.textureAtlasSpriteHashMap = textureAtlasSpriteHashMap;
            this.isOpaque = isOpaque;
            this.transforms = transforms;
        }


        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
            ArrayList<BakedQuad> list = new ArrayList<>();
            BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
            BlockPartFace face = new BlockPartFace(null, 0, "", uv);
            ModelRotation modelRot = ModelRotation.X0_Y0;
            boolean scale = true;
            if(isOpaque){
                list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 0.0F, 16.0F), face, textureAtlasSpriteHashMap.get(EnumFacing.DOWN), EnumFacing.DOWN, modelRot, null, scale, true));//down
                list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 16.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(EnumFacing.UP), EnumFacing.UP, modelRot, null, scale, true));//up
                list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 0.0F), face, textureAtlasSpriteHashMap.get(EnumFacing.NORTH), EnumFacing.NORTH, modelRot, null, scale, true));//north
                list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(EnumFacing.SOUTH), EnumFacing.SOUTH, modelRot, null, scale, true));//south
                list.add(faceBakery.makeBakedQuad(new Vector3f(16.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(EnumFacing.EAST), EnumFacing.EAST, modelRot, null, scale, true));//east
                list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(0.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(EnumFacing.WEST), EnumFacing.WEST, modelRot, null, scale, true));//west
            }
            if(side == null || isOpaque){
                return list;
            }
            if (!isOpaque) {
                switch (side) {
                    case DOWN:
                        list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 0.0F, 16.0F), face, textureAtlasSpriteHashMap.get(side), side, modelRot, null, scale, true));//down
                        break;
                    case UP:
                        list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 16.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(side), side, modelRot, null, scale, true));//up
                        break;
                    case NORTH:
                        list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 0.0F), face, textureAtlasSpriteHashMap.get(side), side, modelRot, null, scale, true));//north
                        break;
                    case SOUTH:
                        list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(side), side, modelRot, null, scale, true));//south
                        break;
                    case EAST:
                        list.add(faceBakery.makeBakedQuad(new Vector3f(16.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(side), side, modelRot, null, scale, true));//east
                        break;
                    case WEST:
                        list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(0.0F, 16.0F, 16.0F), face, textureAtlasSpriteHashMap.get(side), side, modelRot, null, scale, true));//west
                        break;
                }
            }
            return list;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }


        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return textureAtlasSpriteHashMap.get(EnumFacing.DOWN);
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }
        
        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            Pair<? extends IBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, cameraTransformType);
            return Pair.of(this, pair.getRight());
        }
    }


    //Item
    public class ItemIconInfo {

        Item item;
        int damage;
        TextureAtlasSprite sprite;
        String textureName;

        public boolean isBucket = false;

        public Item getItem() {
            return item;
        }

        public TextureAtlasSprite getSprite() {
            return sprite;
        }

        public ItemIconInfo(Item item, int damage, TextureAtlasSprite sprite, String textureName) {
            this.item = item;
            this.damage = damage;
            this.sprite = sprite;
            this.textureName = textureName;
        }

    }
}
