package nc.config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nc.block.BlockMeta;
import nc.block.fission.BlockFissionMetaShield;
import nc.block.fission.BlockFissionVent;
import nc.block.fission.port.BlockFissionItemPort;
import nc.enumm.MetaEnums;
import nc.init.NCBlocks;
import nc.recipe.BasicRecipeHandler;
import nc.recipe.ingredient.FluidArrayIngredient;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.IIngredient;
import nc.recipe.ingredient.ItemArrayIngredient;
import nc.recipe.ingredient.ItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import nc.recipe.multiblock.FissionHeatingRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.ncplanner.ncpf.NCPFModuleList;
import net.ncplanner.ncpf.element.NCPFElement;
import net.ncplanner.ncpf.element.NCPFLegacyBlock;
import net.ncplanner.ncpf.element.NCPFLegacyFluid;
import net.ncplanner.ncpf.element.NCPFLegacyItem;
import net.ncplanner.ncpf.element.NCPFListElement;
import net.ncplanner.ncpf.element.NCPFOredict;
import net.ncplanner.ncpf.module.NCPFEmptyModule;
import net.ncplanner.ncpf.module.NCPFGenericModule;
public class NCPFTranslator{
    public static String configContext = "unknown";
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

        // Add modules & block stats
        for(var elem : newElements){
            elem.modules = new NCPFModuleList();
            Integer meta = null;
            Map<String, Object> blockstate = null;
            boolean casing = false;
            if(elem instanceof NCPFLegacyBlock blk){
                meta = blk.metadata;
                blockstate = blk.blockstate;
            }
            if(block==NCBlocks.solid_fission_controller||block==NCBlocks.salt_fission_controller||block==NCBlocks.turbine_controller){
                elem.modules.put("nuclearcraft:"+configContext+":controller", new NCPFEmptyModule());
                casing = true;
            }
            if(block==NCBlocks.fission_vent){
                var vent = new NCPFGenericModule();
                vent.put("output", blockstate.get("active"));
                elem.modules.put("nuclearcraft:"+configContext+":coolant_vent", vent);
                casing = true;
            }
            if(block==NCBlocks.fission_source){
                var source = new NCPFGenericModule();
                source.put("efficiency", MetaEnums.NeutronSourceType.values()[meta].getEfficiency());
                elem.modules.put("nuclearcraft:"+configContext+":neutron_source", source);
                casing = true;
            }
            if(block==NCBlocks.solid_fission_sink){
                var sink = new NCPFGenericModule();
                sink.put("cooling", MetaEnums.HeatSinkType.values()[meta].getCooling());
                elem.modules.put("nuclearcraft:"+configContext+":heat_sink", sink);
                //TODO placement rules (FissionPlacement.recipe_handler
            }
            if(block==NCBlocks.solid_fission_sink2){
                var sink = new NCPFGenericModule();
                sink.put("cooling", MetaEnums.HeatSinkType2.values()[meta].getCooling());
                elem.modules.put("nuclearcraft:"+configContext+":heat_sink", sink);
                //TODO placement rules (FissionPlacement.recipe_handler
            }
            if(block==NCBlocks.solid_fission_cell){
                elem.modules.put("nuclearcraft:"+configContext+":fuel_cell", new NCPFEmptyModule());
                //TODO recipe ports module
                //TODO fuels
            }
            if(block==NCBlocks.fission_irradiator){
                elem.modules.put("nuclearcraft:"+configContext+":irradiator", new NCPFEmptyModule());
                //TODO recipe ports module
                //TODO irradiator recipes
            }
            if(block==NCBlocks.fission_cell_port||block==NCBlocks.fission_irradiator_port){
                var port = new NCPFGenericModule();
                port.put("output", blockstate.get("active"));
                elem.modules.put("nuclearcraft:"+configContext+":port", port);
            }
            if(block==NCBlocks.fission_conductor){
                elem.modules.put("nuclearcraft:"+configContext+":conductor", new NCPFEmptyModule());
            }
            //TODO moderator recipes
            if(block==NCBlocks.fission_reflector){
                var reflector = new NCPFGenericModule();
                reflector.put("efficiency", MetaEnums.NeutronReflectorType.values()[meta].getEfficiency());
                reflector.put("reflectivity", MetaEnums.NeutronReflectorType.values()[meta].getReflectivity());
                elem.modules.put("nuclearcraft:"+configContext+":reflector", reflector);
                //TODO placement rules (FissionPlacement.recipe_handler
            }
            if(block==NCBlocks.fission_shield){
                if(Objects.equals(blockstate.get("active"), Boolean.FALSE)){
                    var shield = new NCPFGenericModule();
                    shield.put("heat_per_flux", MetaEnums.NeutronShieldType.values()[meta].getHeatPerFlux());
                    shield.put("efficiency", MetaEnums.NeutronShieldType.values()[meta].getEfficiency());
                    elem.modules.put("nuclearcraft:"+configContext+":neutron_shield", shield);
                    //TODO closed reference
                }
            }
            if(casing||block==NCBlocks.fission_casing||block==NCBlocks.fission_glass||block==NCBlocks.fission_monitor||block==NCBlocks.fission_source_manager||block==NCBlocks.fission_shield_manager||block==NCBlocks.fission_power_port||block==NCBlocks.fission_glass||block==NCBlocks.fission_computer_port){
                var casin = new NCPFGenericModule();
                casin.put("edge", block==NCBlocks.fission_casing);
                elem.modules.put("nuclearcraft:"+configContext+":casing", casin);
            }
            if(elem.modules.isEmpty())elem.modules = null;
        }
    }
    public static void translate(List<NCPFElement> list, BasicRecipeHandler recipes){
        if(recipes.getItemInputSize()+recipes.getFluidInputSize()!=1)throw new IllegalArgumentException("Cannot convert recipes to NCPF element unless they have exactly one input!");
        for(var recipe : recipes.getRecipeList()){
            IIngredient ingredient = recipes.getItemInputSize()>0?recipe.getItemIngredients().get(0):recipe.getFluidIngredients().get(0);
            var element = translateIngredient(ingredient);
            if(recipes instanceof FissionHeatingRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var stats = new NCPFGenericModule();
                stats.put("heat", recipe.getFissionHeatingHeatPerInputMB());
                stats.put("output_ratio", recipe.getFluidProducts().get(0).getStack().amount/(float)recipe.getFluidIngredients().get(0).getStack().amount);
                element.modules.put("nuclearcraft:overhaul_sfr:coolant_recipe_stats", stats);
            }
            list.add(element);
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
