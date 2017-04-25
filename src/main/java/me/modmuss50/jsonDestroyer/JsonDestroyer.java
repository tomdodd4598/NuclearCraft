package me.modmuss50.jsonDestroyer;


import me.modmuss50.jsonDestroyer.api.IDestroyable;
import me.modmuss50.jsonDestroyer.client.ModelGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

/**
 * Create a new instance of this in your mod
 */
public class JsonDestroyer {

    public boolean isAvailable = false;
    public ArrayList<Object> objectsToDestroy = new ArrayList<Object>();

    ModelGenerator modelGenerator;


    /**
     * Call this in pre-init, doesn't matter if you call it on the server side
     */
    public void load() {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            isAvailable = true;
            modelGenerator = new ModelGenerator(this);
            MinecraftForge.EVENT_BUS.register(modelGenerator);
        }
    }

    /**
     * Use this to register an object to be rendered
     *
     * @param object the object to load
     */
    public void registerObject(Object object) {
        if (object instanceof IDestroyable) {
            if (!objectsToDestroy.contains(object)) {
                objectsToDestroy.add(object);
            }
        }
    }

}
