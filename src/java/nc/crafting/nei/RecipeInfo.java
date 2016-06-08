package nc.crafting.nei;

import java.util.HashMap;

import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.OffsetPositioner;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IStackPositioner;

import com.google.common.base.Objects;

public class RecipeInfo
{    
    private static class OverlayKey
    {
        String ident;
        Class<? extends GuiContainer> guiClass;
        
        public OverlayKey(Class<? extends GuiContainer> classz, String ident)
        {
            this.guiClass = classz;
            this.ident = ident;
        }

        public boolean equals(Object obj)
        {
            if(!(obj instanceof OverlayKey))
                return false;
            OverlayKey key = (OverlayKey)obj;
            return Objects.equal(ident, key.ident) && guiClass == key.guiClass;
        }
        
        public int hashCode()
        {
            return Objects.hashCode(ident, guiClass);
        }
    }
    
    static HashMap<OverlayKey, IOverlayHandler> overlayMap = new HashMap<OverlayKey, IOverlayHandler>();
    static HashMap<OverlayKey, IStackPositioner> positionerMap = new HashMap<OverlayKey, IStackPositioner>();
    static HashMap<Class<? extends GuiContainer>, int[]> offsets = new HashMap<Class<? extends GuiContainer>, int[]>();
    
    public static void registerOverlayHandler(Class<? extends GuiContainer> classz, IOverlayHandler handler, String ident)
    {
        overlayMap.put(new OverlayKey(classz, ident), handler);
    }
        
    public static void registerGuiOverlay(Class<? extends GuiContainer> classz, String ident, IStackPositioner positioner)
    {
        positionerMap.put(new OverlayKey(classz, ident), positioner);
        if(positioner instanceof OffsetPositioner && !offsets.containsKey(classz))
        {
            OffsetPositioner p = (OffsetPositioner)positioner;
            setGuiOffset(classz, p.offsetx, p.offsety);
        }            
    }

    public static void setGuiOffset(Class<? extends GuiContainer> classz, int x, int y)
    {
        offsets.put(classz, new int[]{x, y});
    }

    public static boolean hasDefaultOverlay(GuiContainer gui, String ident)
    {
        return positionerMap.containsKey(new OverlayKey(gui.getClass(), ident));
    }

    public static boolean hasOverlayHandler(GuiContainer gui, String ident)
    {
        return overlayMap.containsKey(new OverlayKey(gui.getClass(), ident));
    }

    public static IOverlayHandler getOverlayHandler(GuiContainer gui, String ident)
    {
        return overlayMap.get(new OverlayKey(gui.getClass(), ident));
    }

    public static IStackPositioner getStackPositioner(GuiContainer gui, String ident)
    {
        return positionerMap.get(new OverlayKey(gui.getClass(), ident));
    }
    
    public static int[] getGuiOffset(GuiContainer gui)
    {
        int[] offset = offsets.get(gui.getClass());
        return offset == null ? new int[]{5, 11} : offset;
    }

    public static void registerGuiOverlayHandler(Class<? extends GuiContainer> classz, IOverlayHandler handler, String ident)
    {
        RecipeInfo.registerOverlayHandler(classz, handler, ident);
    }
}
