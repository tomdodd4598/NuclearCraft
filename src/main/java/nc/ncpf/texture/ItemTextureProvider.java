package nc.ncpf.texture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
/**
 * placeholder
 */
public class ItemTextureProvider extends MissingTextureProvider{
    public ItemTextureProvider(ItemStack stack){
        this(stack.getItem(), stack.getMetadata());
    }
    public ItemTextureProvider(Item item, int meta){}
}