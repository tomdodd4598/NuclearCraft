package nc.config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nc.block.BlockMeta;
import nc.block.fission.BlockFissionMetaShield;
import nc.block.fission.BlockFissionVent;
import nc.block.fission.port.BlockFissionItemPort;
import nc.init.NCBlocks;
import nc.recipe.BasicRecipeHandler;
import nc.recipe.ingredient.FluidArrayIngredient;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.IIngredient;
import nc.recipe.ingredient.ItemArrayIngredient;
import nc.recipe.ingredient.ItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.ncplanner.ncpf.element.NCPFElement;
import net.ncplanner.ncpf.element.NCPFLegacyBlock;
import net.ncplanner.ncpf.element.NCPFLegacyFluid;
import net.ncplanner.ncpf.element.NCPFLegacyItem;
import net.ncplanner.ncpf.element.NCPFListElement;
import net.ncplanner.ncpf.element.NCPFOredict;
public class NCPFTranslator{
    public static void translate(List<NCPFElement> list, Block... blocks){
        for(var block : blocks)translate(list, block);
    }
    public static void translate(List<NCPFElement> list, Block block){
        ArrayList<NCPFElement> newElements = new ArrayList<>();
        if(block instanceof BlockMeta metaBlock){
            int metadata = -1;
            for(var variant : metaBlock.values){
                NCPFLegacyBlock ncpf = new NCPFLegacyBlock();
                ncpf.name = block.getRegistryName().toString();
                ncpf.metadata = ++metadata;
                ncpf.blockstate = new HashMap<>();
                ncpf.blockstate.put("type", variant.toString());
                newElements.add(ncpf);
                if(block instanceof BlockFissionMetaShield){
                    ncpf.blockstate.put("active", false);
                    NCPFLegacyBlock closed = new NCPFLegacyBlock();
                    closed.name = block.getRegistryName().toString();
                    closed.metadata = metadata;
                    closed.blockstate = new HashMap<>();
                    closed.blockstate.put("type", variant.toString());
                    closed.blockstate.put("active", true);
                    newElements.add(closed);
                }
            }
        }else{
            NCPFLegacyBlock ncpf = new NCPFLegacyBlock();
            ncpf.name = block.getRegistryName().toString();
            newElements.add(ncpf);
            if(block instanceof BlockFissionVent||block instanceof BlockFissionItemPort){
                ncpf.blockstate = new HashMap<>();
                ncpf.blockstate.put("active", false);
                NCPFLegacyBlock output = new NCPFLegacyBlock();
                output.name = block.getRegistryName().toString();
                output.blockstate = new HashMap<>();
                output.blockstate.put("active", true);
                newElements.add(output);
            }
        }
        list.addAll(newElements);
    }
    public static void translate(List<NCPFElement> list, BasicRecipeHandler recipes){
        if(recipes.getItemInputSize()+recipes.getFluidInputSize()!=1)throw new IllegalArgumentException("Cannot convert recipes to NCPF element unless they have exactly one input!");
        for(var recipe : recipes.getRecipeList()){
            IIngredient ingredient = recipes.getItemInputSize()>0?recipe.getItemIngredients().get(0):recipe.getFluidIngredients().get(0);
            list.add(translateIngredient(ingredient));
        }
    }
    private static NCPFElement translateIngredient(IIngredient ingredient){
        if(ingredient instanceof FluidArrayIngredient array){
            NCPFListElement ncpf = new NCPFListElement();
            for(var ingr : array.ingredientList){
                ncpf.elements.add(translateIngredient(ingr));
            }
            return ncpf;
        }
        if(ingredient instanceof ItemArrayIngredient array){
            NCPFListElement ncpf = new NCPFListElement();
            for(var ingr : array.ingredientList){
                ncpf.elements.add(translateIngredient(ingr));
            }
            return ncpf;
        }
        if(ingredient instanceof ItemIngredient item){
            var realItem = item.stack.getItem();
            if(realItem instanceof ItemBlock bitem){
                var block = bitem.getBlock();
                var lst = new ArrayList<NCPFElement>();
                translate(lst, block);
                for(var elem : lst){
                    if(elem instanceof NCPFLegacyBlock ncpf&&ncpf.metadata!=null&&ncpf.metadata==item.stack.getMetadata()){
                        return elem;
                    }
                }
                return lst.get(0);
            }
            NCPFLegacyItem ncpf = new NCPFLegacyItem();
            ncpf.name = item.stack.getItem().getRegistryName().toString();
            if(item.stack.getItem().getHasSubtypes())ncpf.metadata = item.stack.getMetadata();
            return ncpf;
        }
        if(ingredient instanceof FluidIngredient fluid){
            NCPFLegacyFluid ncpf = new NCPFLegacyFluid();
            ncpf.name = fluid.fluidName;
            return ncpf;
        }
        if(ingredient instanceof OreIngredient ore){
            NCPFOredict ncpf = new NCPFOredict();
            ncpf.oredict = ore.oreName;
            return ncpf;
        }
        throw new UnsupportedOperationException("Could not translate IIngredient: "+ingredient.getClass().getName());
    }
}
