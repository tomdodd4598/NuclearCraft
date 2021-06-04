package nc.ncpf.texture;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
/**
 * placeholder
 */
public class BlockTextureProvider extends MissingTextureProvider{
    public BlockTextureProvider(ItemStack stack){
        this(Block.getBlockFromItem(stack.getItem()), stack.getMetadata());
    }
    public BlockTextureProvider(Block block){
        this(block, 0);
    }
    public BlockTextureProvider(Block block, int meta){}
}