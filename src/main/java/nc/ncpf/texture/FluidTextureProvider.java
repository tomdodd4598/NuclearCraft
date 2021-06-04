package nc.ncpf.texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
/**
 * Doesn't work, this is just a placeholder
 */
public class FluidTextureProvider implements TextureProvider{
    private int[] data;
    private int size = -1;
    public FluidTextureProvider(FluidStack stack){
        this(stack.getFluid());
    }
    public FluidTextureProvider(Fluid fluid){
        ResourceLocation resource = fluid.getStill();
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
        ITextureObject texture = Minecraft.getMinecraft().renderEngine.getTexture(resource);
        if(texture instanceof DynamicTexture){
            DynamicTexture tex = (DynamicTexture)texture;
            data = tex.getTextureData();
        }else throw new IllegalArgumentException("Resource location "+resource.toString()+" does not point to a dynamic texture!");
        int width = 0;
        int size;
        do{
            width++;
            size = width*width;
            if(data.length==size)this.size = width;
        }while(size<data.length);
        if(this.size==-1)throw new IllegalArgumentException("Texture is not square!");
    }
    @Override
    public int getSize(){
        return size;
    }
    @Override
    public int getColor(int x, int y){
        return data[x+y*size];
    }
}