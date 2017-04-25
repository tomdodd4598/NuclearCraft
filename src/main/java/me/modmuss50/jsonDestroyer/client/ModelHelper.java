package me.modmuss50.jsonDestroyer.client;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ForgeBlockStateV1;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.util.JsonUtils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by mark on 29/03/16.
 */
@SideOnly(Side.CLIENT)
public class ModelHelper {

    public static ItemCameraTransforms loadTransformFromJson(ResourceLocation location) throws IOException {
        return ModelBlock.deserialize(getReaderForResource(location)).getAllTransforms();
    }


    public static Reader getReaderForResource(ResourceLocation location) throws IOException {
        ResourceLocation file = new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".json");
        IResource iresource = Minecraft.getMinecraft().getResourceManager().getResource(file);
        return new BufferedReader(new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8));
    }
}
