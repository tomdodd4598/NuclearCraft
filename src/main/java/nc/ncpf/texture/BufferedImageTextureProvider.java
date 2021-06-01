package nc.ncpf.texture;
import java.awt.image.BufferedImage;
public class BufferedImageTextureProvider implements TextureProvider{
    private final BufferedImage image;
    public BufferedImageTextureProvider(BufferedImage image){
        if(image.getWidth()!=image.getHeight())throw new IllegalArgumentException("Image must be square! (animated textures are not supported)");
        this.image = image;
    }
    @Override
    public int getSize(){
        return image.getWidth();
    }
    @Override
    public int getColor(int x, int y){
        return image.getRGB(x, y);
    }
}