package nc.config;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import nc.block.IBlockMeta;
import nc.block.fission.BlockFissionMetaShield;
import nc.block.fission.BlockFissionVent;
import nc.block.fission.port.BlockFissionFluidMetaPort;
import nc.block.fission.port.BlockFissionFluidPort;
import nc.block.fission.port.BlockFissionItemPort;
import nc.enumm.MetaEnums;
import nc.init.NCBlocks;
import nc.multiblock.PlacementRule;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.TurbinePlacement;
import nc.multiblock.turbine.TurbineRotorBladeUtil;
import nc.recipe.BasicRecipe;
import nc.recipe.BasicRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ingredient.FluidArrayIngredient;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.IIngredient;
import nc.recipe.ingredient.ItemArrayIngredient;
import nc.recipe.ingredient.ItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import nc.recipe.multiblock.CoolantHeaterRecipes;
import nc.recipe.multiblock.FissionHeatingRecipes;
import nc.recipe.multiblock.FissionIrradiatorRecipes;
import nc.recipe.multiblock.FissionModeratorRecipes;
import nc.recipe.multiblock.FissionReflectorRecipes;
import nc.recipe.multiblock.SaltFissionRecipes;
import nc.recipe.multiblock.SolidFissionRecipes;
import nc.recipe.multiblock.TurbineRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.ncplanner.ncpf.NCPFModuleList;
import net.ncplanner.ncpf.NCPFPlacementRule;
import net.ncplanner.ncpf.element.NCPFElement;
import net.ncplanner.ncpf.element.NCPFLegacyBlock;
import net.ncplanner.ncpf.element.NCPFLegacyFluid;
import net.ncplanner.ncpf.element.NCPFLegacyItem;
import net.ncplanner.ncpf.element.NCPFListElement;
import net.ncplanner.ncpf.element.NCPFModuleElement;
import net.ncplanner.ncpf.element.NCPFOredict;
import net.ncplanner.ncpf.module.NCPFEmptyModule;
import net.ncplanner.ncpf.module.NCPFGenericModule;
public class NCPFTranslator{
    public static String configContext = "unknown";
    public static void translate(List<NCPFElement> list, Block... blocks){
        for(var block : blocks)translate(list, block);
    }
    public static void translate(List<NCPFElement> list, Block block){
        translate(list, block, true);
    }
    public static void translate(List<NCPFElement> list, Block block, boolean includeModules){
        ArrayList<NCPFElement> newElements = new ArrayList<>();
        if(block instanceof IBlockMeta metaBlock){
            int metadata = -1;
            for(var variant : metaBlock.getValues()){
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
                if(block instanceof BlockFissionFluidMetaPort){
                    ncpf.blockstate.put("active", false);
                    NCPFLegacyBlock output = new NCPFLegacyBlock();
                    output.name = block.getRegistryName().toString();
                    output.metadata = metadata;
                    output.blockstate = new HashMap<>();
                    output.blockstate.put("type", variant.toString());
                    output.blockstate.put("active", true);
                    newElements.add(output);
                }
            }
        }else{
            NCPFLegacyBlock ncpf = new NCPFLegacyBlock();
            ncpf.name = block.getRegistryName().toString();
            newElements.add(ncpf);
            if(block instanceof BlockFissionVent||block instanceof BlockFissionItemPort||block instanceof BlockFissionFluidPort){
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
        if(!includeModules)return;
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
                translatePlacementRules(sink, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
            }
            if(block==NCBlocks.solid_fission_sink2){
                var sink = new NCPFGenericModule();
                sink.put("cooling", MetaEnums.HeatSinkType2.values()[meta].getCooling());
                elem.modules.put("nuclearcraft:"+configContext+":heat_sink", sink);
                translatePlacementRules(sink, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
            }
            if(block==NCBlocks.salt_fission_heater){
                var heaterModule = new NCPFGenericModule();
                elem.modules.put("nuclearcraft:"+configContext+":heater", heaterModule);
                var ports = new NCPFGenericModule();

                var portElements = new ArrayList<NCPFElement>();
                translate(portElements, NCBlocks.fission_heater_port);
                var heater = (NCPFLegacyBlock)elem;
                for(Iterator<NCPFElement> it = portElements.iterator(); it.hasNext();){
                    NCPFLegacyBlock port = (NCPFLegacyBlock)it.next();
                    if(!port.blockstate.get("type").equals(heater.blockstate.get("type")))it.remove();
                }
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);

                var recipesModule = new NCPFGenericModule();
                ArrayList<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.coolant_heater, (recipe) -> {
                    return ((ItemBlock)recipe.getItemIngredients().get(0).getStack().getItem()).getBlock()==block
                        &&recipe.getItemIngredients().get(0).getStack().getMetadata()==heater.metadata;
                });
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
                translatePlacementRules(heaterModule, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
            }
            if(block==NCBlocks.salt_fission_heater2){
                var heaterModule = new NCPFGenericModule();
                elem.modules.put("nuclearcraft:"+configContext+":heater", heaterModule);
                var ports = new NCPFGenericModule();

                var portElements = new ArrayList<NCPFElement>();
                translate(portElements, NCBlocks.fission_heater_port2);
                var heater = (NCPFLegacyBlock)elem;
                for(Iterator<NCPFElement> it = portElements.iterator(); it.hasNext();){
                    NCPFLegacyBlock port = (NCPFLegacyBlock)it.next();
                    if(!port.blockstate.get("type").equals(heater.blockstate.get("type")))it.remove();
                }
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);

                var recipesModule = new NCPFGenericModule();
                ArrayList<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.coolant_heater, (recipe) -> {
                    return ((ItemBlock)recipe.getItemIngredients().get(0).getStack().getItem()).getBlock()==block
                        &&recipe.getItemIngredients().get(0).getStack().getMetadata()==heater.metadata;
                });
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
                translatePlacementRules(heaterModule, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
            }
            if(block==NCBlocks.solid_fission_cell){
                elem.modules.put("nuclearcraft:"+configContext+":fuel_cell", new NCPFEmptyModule());
                var ports = new NCPFGenericModule();
                var portElements = new ArrayList<NCPFElement>();
                translate(portElements, NCBlocks.fission_cell_port);
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_sfr:recipe_ports", ports);
                var recipesModule = new NCPFGenericModule();
                ArrayList<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.solid_fission);
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
            }
            if(block==NCBlocks.salt_fission_vessel){
                elem.modules.put("nuclearcraft:"+configContext+":fuel_vessel", new NCPFEmptyModule());
                var ports = new NCPFGenericModule();
                var portElements = new ArrayList<NCPFElement>();
                translate(portElements, NCBlocks.fission_vessel_port);
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);
                var recipesModule = new NCPFGenericModule();
                ArrayList<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.salt_fission);
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
            }
            if(block==NCBlocks.fission_irradiator){
                elem.modules.put("nuclearcraft:"+configContext+":irradiator", new NCPFEmptyModule());
                var ports = new NCPFGenericModule();
                var portElements = new ArrayList<NCPFElement>();
                translate(portElements, NCBlocks.fission_irradiator_port);
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_sfr:recipe_ports", ports);
                var recipesModule = new NCPFGenericModule();
                ArrayList<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.fission_irradiator);
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
            }
            if(block==NCBlocks.fission_cell_port||block==NCBlocks.fission_irradiator_port||block==NCBlocks.fission_vessel_port||block==NCBlocks.fission_heater_port||block==NCBlocks.fission_heater_port2){
                var port = new NCPFGenericModule();
                port.put("output", blockstate.get("active"));
                elem.modules.put("nuclearcraft:"+configContext+":port", port);
            }
            if(block==NCBlocks.fission_conductor){
                elem.modules.put("nuclearcraft:"+configContext+":conductor", new NCPFEmptyModule());
            }
            if(block==NCBlocks.fission_shield){
                if(Objects.equals(blockstate.get("active"), Boolean.FALSE)){
                    var shield = new NCPFGenericModule();
                    shield.put("heat_per_flux", MetaEnums.NeutronShieldType.values()[meta].getHeatPerFlux());
                    shield.put("efficiency", MetaEnums.NeutronShieldType.values()[meta].getEfficiency());
                    shield.put("closed", newElements.get(1));
                    elem.modules.put("nuclearcraft:"+configContext+":neutron_shield", shield);
                }
            }

            // Turbine
            if(block==NCBlocks.turbine_inlet){
                elem.modules.put("nuclearcraft:"+configContext+":inlet", new NCPFEmptyModule());
            }
            if(block==NCBlocks.turbine_outlet){
                elem.modules.put("nuclearcraft:"+configContext+":outlet", new NCPFEmptyModule());
            }
            if(block==NCBlocks.turbine_rotor_blade_steel){
                var blade = new NCPFGenericModule();
                blade.put("efficiency", TurbineRotorBladeUtil.TurbineRotorBladeType.STEEL.getEfficiency());
                blade.put("expansion", TurbineRotorBladeUtil.TurbineRotorBladeType.STEEL.getExpansionCoefficient());
                elem.modules.put("nuclearcraft:"+configContext+":blade", blade);
            }
            if(block==NCBlocks.turbine_rotor_blade_extreme){
                var blade = new NCPFGenericModule();
                blade.put("efficiency", TurbineRotorBladeUtil.TurbineRotorBladeType.EXTREME.getEfficiency());
                blade.put("expansion", TurbineRotorBladeUtil.TurbineRotorBladeType.EXTREME.getExpansionCoefficient());
                elem.modules.put("nuclearcraft:"+configContext+":blade", blade);
            }
            if(block==NCBlocks.turbine_rotor_blade_sic_sic_cmc){
                var blade = new NCPFGenericModule();
                blade.put("efficiency", TurbineRotorBladeUtil.TurbineRotorBladeType.SIC_SIC_CMC.getEfficiency());
                blade.put("expansion", TurbineRotorBladeUtil.TurbineRotorBladeType.SIC_SIC_CMC.getExpansionCoefficient());
                elem.modules.put("nuclearcraft:"+configContext+":blade", blade);
            }
            if(block==NCBlocks.turbine_rotor_stator){
                var stator = new NCPFGenericModule();
                stator.put("expansion", TurbineRotorBladeUtil.TurbineRotorStatorType.STANDARD.getExpansionCoefficient());
                elem.modules.put("nuclearcraft:"+configContext+":stator", stator);
            }
            if(block==NCBlocks.turbine_dynamo_coil){
                var coil = new NCPFGenericModule();
                coil.put("efficiency", TurbineDynamoCoilType.values()[meta].getConductivity());
                elem.modules.put("nuclearcraft:"+configContext+":coil", coil);
                translatePlacementRules(coil, block, meta, TurbinePlacement.recipe_handler, TurbinePlacement.RULE_MAP);
            }
            if(block==NCBlocks.turbine_coil_connector){
                var connector = new NCPFGenericModule();
                elem.modules.put("nuclearcraft:"+configContext+":connector", connector);
                translatePlacementRules(connector, block, meta, TurbinePlacement.recipe_handler, TurbinePlacement.RULE_MAP);
            }
            if(block==NCBlocks.turbine_rotor_bearing){
                elem.modules.put("nuclearcraft:"+configContext+":bearing", new NCPFEmptyModule());
            }
            if(block==NCBlocks.turbine_rotor_shaft){
                elem.modules.put("nuclearcraft:"+configContext+":shaft", new NCPFEmptyModule());
            }

            if(casing||block==NCBlocks.fission_casing||block==NCBlocks.fission_glass||block==NCBlocks.fission_monitor||block==NCBlocks.fission_source_manager||block==NCBlocks.fission_shield_manager||block==NCBlocks.fission_power_port||block==NCBlocks.fission_computer_port||block==NCBlocks.turbine_casing||block==NCBlocks.turbine_glass||block==NCBlocks.turbine_computer_port||block==NCBlocks.turbine_redstone_port){
                var casin = new NCPFGenericModule();
                casin.put("edge", block==NCBlocks.fission_casing||block==NCBlocks.turbine_casing);
                elem.modules.put("nuclearcraft:"+configContext+":casing", casin);
            }
            if(elem.modules.isEmpty())elem.modules = null;
        }
    }
    public static void translate(List<NCPFElement> list, BasicRecipeHandler recipes){
        translate(list, recipes, (t) -> true);
    }
    public static void translate(List<NCPFElement> list, BasicRecipeHandler recipes, Predicate<BasicRecipe> filter){
        if(!(recipes instanceof CoolantHeaterRecipes)){
            if(recipes.getItemInputSize()+recipes.getFluidInputSize()!=1)throw new IllegalArgumentException("Cannot convert recipes to NCPF element unless they have exactly one input!");
        }
        for(var recipe : recipes.getRecipeList()){
            if(!filter.test(recipe))continue;
            IIngredient ingredient = recipes.getFluidInputSize()>0?recipe.getFluidIngredients().get(0):recipe.getItemIngredients().get(0);
            var element = translateIngredient(ingredient);
            if(recipes instanceof FissionHeatingRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var stats = new NCPFGenericModule();
                stats.put("heat", recipe.getFissionHeatingHeatPerInputMB());
                stats.put("output_ratio", recipe.getFluidProducts().get(0).getStack().amount/(float)recipe.getFluidIngredients().get(0).getStack().amount);
                stats.put("output", translateIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_sfr:coolant_recipe_stats", stats);
            }
            if(recipes instanceof FissionModeratorRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var moderator = new NCPFGenericModule();
                moderator.put("flux", recipe.getFissionModeratorFluxFactor());
                moderator.put("efficiency", recipe.getFissionModeratorEfficiency());
                element.modules.put("nuclearcraft:"+configContext+":moderator", moderator);
            }
            if(recipes instanceof FissionReflectorRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var reflector = new NCPFGenericModule();
                reflector.put("efficiency", recipe.getFissionReflectorEfficiency());
                reflector.put("reflectivity", recipe.getFissionReflectorReflectivity());
                element.modules.put("nuclearcraft:"+configContext+":reflector", reflector);
            }
            if(recipes instanceof FissionIrradiatorRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var irradiator = new NCPFGenericModule();
                irradiator.put("heat", recipe.getIrradiatorHeatPerFlux());
                irradiator.put("efficiency", recipe.getIrradiatorProcessEfficiency());
                irradiator.put("output", translateIngredient(recipe.getItemProducts().get(0)));
                element.modules.put("nuclearcraft:"+configContext+":irradiator_stats", irradiator);
            }
            if(recipes instanceof SolidFissionRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var fuel = new NCPFGenericModule();
                fuel.put("efficiency", recipe.getFissionFuelEfficiency());
                fuel.put("heat", recipe.getFissionFuelHeat());
                fuel.put("time", recipe.getFissionFuelTime());
                fuel.put("criticality", recipe.getFissionFuelCriticality());
                fuel.put("self_priming", recipe.getFissionFuelSelfPriming());
                fuel.put("output", translateIngredient(recipe.getItemProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_sfr:fuel_stats", fuel);
            }
            if(recipes instanceof SaltFissionRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var fuel = new NCPFGenericModule();
                fuel.put("efficiency", recipe.getFissionFuelEfficiency());
                fuel.put("heat", recipe.getFissionFuelHeat());
                fuel.put("time", recipe.getSaltFissionFuelTime());
                fuel.put("criticality", recipe.getFissionFuelCriticality());
                fuel.put("self_priming", recipe.getFissionFuelSelfPriming());
                fuel.put("output", translateIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_msr:fuel_stats", fuel);
            }
            if(recipes instanceof CoolantHeaterRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var heater = new NCPFGenericModule();
                heater.put("cooling", recipe.getCoolantHeaterCoolingRate());
                heater.put("output", translateIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_msr:heater_stats", heater);
            }
            if(recipes instanceof TurbineRecipes){
                if(element.modules==null)element.modules = new NCPFModuleList();
                var stats = new NCPFGenericModule();
                stats.put("power", recipe.getTurbinePowerPerMB());
                stats.put("coefficient", recipe.getTurbineExpansionLevel());
                stats.put("output", translateIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_turbine:recipe_stats", stats);
            }
            list.add(element);
        }
    }
    public static void translateOutputs(List<NCPFElement> list, BasicRecipeHandler recipes){
        for(var recipe : recipes.getRecipeList()){
            for(var item : recipe.getItemProducts()){
                list.add(translateIngredient(item));
            }
            for(var fluid : recipe.getFluidProducts()){
                list.add(translateIngredient(fluid));
            }
        }
    }
    private static NCPFElement translateIngredient(IIngredient ingredient){
        if(ingredient instanceof FluidArrayIngredient array){
            NCPFListElement ncpf = new NCPFListElement();
            for(var ingr : array.ingredientList){
                ncpf.elements.add(translateIngredient(ingr));
            }
            return ncpf.elements.size()==1?ncpf.elements.get(0):ncpf;
        }
        if(ingredient instanceof ItemArrayIngredient array){
            NCPFListElement ncpf = new NCPFListElement();
            for(var ingr : array.ingredientList){
                ncpf.elements.add(translateIngredient(ingr));
            }
            return ncpf.elements.size()==1?ncpf.elements.get(0):ncpf;
        }
        if(ingredient instanceof ItemIngredient item){
            return translate(item.stack);
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
    public static NCPFElement translate(ItemStack stack){
        return translate(stack, true);
    }
    public static NCPFElement translate(ItemStack stack, boolean includeModules){
        var realItem = stack.getItem();
        if(realItem instanceof ItemBlock bitem){
            var block = bitem.getBlock();
            var lst = new ArrayList<NCPFElement>();
            translate(lst, block, includeModules);
            for(var elem : lst){
                if(elem instanceof NCPFLegacyBlock ncpf&&ncpf.metadata!=null&&ncpf.metadata==stack.getMetadata()){
                    return elem;
                }
            }
            return lst.get(0);
        }
        NCPFLegacyItem ncpf = new NCPFLegacyItem();
        ncpf.name = stack.getItem().getRegistryName().toString();
        if(stack.getItem().getHasSubtypes())ncpf.metadata = stack.getMetadata();
        return ncpf;
    }
    private static void translatePlacementRules(NCPFGenericModule module, Block block, Integer meta, PlacementRule.RecipeHandler recipeHandler, Object2ObjectMap ruleMap){
        ArrayList<NCPFPlacementRule> rules = new ArrayList<>();

        for(var recipe : recipeHandler.getRecipeList()){
            var stack = recipe.getItemIngredients().get(0).getStack();
            if(((ItemBlock)stack.getItem()).getBlock()!=block)continue;
            if(stack.getHasSubtypes()!=(meta!=null))continue;
            if(stack.getHasSubtypes()&&stack.getMetadata()!=meta)continue;
            //recipe is a valid placement rule for this block.

            String ruleID = recipe.getPlacementRuleID();
            PlacementRule rule = (PlacementRule)ruleMap.get(ruleID);

            NCPFPlacementRule ncpf = translatePlacementRule(rule);
            rules.add(ncpf);
        }

        module.put("rules", rules);
    }
    private static NCPFPlacementRule translatePlacementRule(PlacementRule rule){
        NCPFPlacementRule ncpf = new NCPFPlacementRule();
        if(rule instanceof PlacementRule.And and){
            ncpf.type = NCPFPlacementRule.RuleType.and;
            for(var subRule : and.subRules){
                ncpf.rules.add(translatePlacementRule((PlacementRule)subRule));
            }
        }
        if(rule instanceof PlacementRule.Or or){
            ncpf.type = NCPFPlacementRule.RuleType.or;
            for(var subRule : or.subRules){
                ncpf.rules.add(translatePlacementRule((PlacementRule)subRule));
            }
        }
        if(rule instanceof PlacementRule.Adjacent adjacent){
            if(rule instanceof FissionPlacement.AdjacentCasing)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":casing");
            if(rule instanceof FissionPlacement.AdjacentConductor)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":conductor");
            if(rule instanceof FissionPlacement.AdjacentModerator)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":moderator");
            if(rule instanceof FissionPlacement.AdjacentReflector)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":reflector");
            if(rule instanceof FissionPlacement.AdjacentIrradiator)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":irradiator");
            if(rule instanceof FissionPlacement.AdjacentShield)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":neutron_shield");
            if(rule instanceof FissionPlacement.AdjacentCell)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":fuel_cell");
            if(rule instanceof FissionPlacement.AdjacentSink sink){
                if(sink.sinkType.equals("any"))ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":heat_sink");
                else{
                    for(var type : MetaEnums.HeatSinkType.values()){
                        if(sink.sinkType.equals(type.getName())){
                            ncpf.block = translate(new ItemStack(NCBlocks.solid_fission_sink, 1, type.ordinal()), false);
                        }
                    }
                    for(var type : MetaEnums.HeatSinkType2.values()){
                        if(sink.sinkType.equals(type.getName())){
                            ncpf.block = translate(new ItemStack(NCBlocks.solid_fission_sink2, 1, type.ordinal()), false);
                        }
                    }
                }
                if(ncpf.block==null)throw new IllegalArgumentException("Could not find target sink: "+sink.sinkType+"!");
            }
            if(rule instanceof FissionPlacement.AdjacentVessel)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":fuel_vessel");
            if(rule instanceof FissionPlacement.AdjacentHeater heater){
                if(heater.heaterType.equals("any"))ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":heater");
                else{
                    for(var type : MetaEnums.CoolantHeaterType.values()){
                        if(heater.heaterType.equals(type.getName())){
                            ncpf.block = translate(new ItemStack(NCBlocks.salt_fission_heater, 1, type.ordinal()), false);
                        }
                    }
                    for(var type : MetaEnums.CoolantHeaterType2.values()){
                        if(heater.heaterType.equals(type.getName())){
                            ncpf.block = translate(new ItemStack(NCBlocks.salt_fission_heater2, 1, type.ordinal()), false);
                        }
                    }
                }
                if(ncpf.block==null)throw new IllegalArgumentException("Could not find target heater: "+heater.heaterType+"!");
            }
            if(rule instanceof TurbinePlacement.AdjacentCasing)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":casing");
            if(rule instanceof TurbinePlacement.AdjacentBearing)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":bearing");
            if(rule instanceof TurbinePlacement.AdjacentConnector)ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":connector");
            if(rule instanceof TurbinePlacement.AdjacentCoil coil){
                if(coil.coilType.equals("any"))ncpf.block = new NCPFModuleElement("nuclearcraft:"+configContext+":coil");
                else{
                    for(var type : TurbineDynamoCoilType.values()){
                        if(coil.coilType.equals(type.getName())){
                            ncpf.block = translate(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, type.ordinal()), false);
                        }
                    }
                }
                if(ncpf.block==null)throw new IllegalArgumentException("Could not find target coil: "+coil.coilType+"!");
            }
            if(ncpf.block==null)throw new IllegalArgumentException("Could not find target for rule: "+rule.getClass().getName()+"!");

            switch(adjacent.countType){
                case AT_LEAST -> {
                    ncpf.min = adjacent.amount;
                    ncpf.max = 6;
                }
                case AT_MOST -> {
                    ncpf.min = 0;
                    ncpf.max = adjacent.amount;
                }
                case EXACTLY ->
                    ncpf.min = ncpf.max = adjacent.amount;
            }
            switch(adjacent.adjType){
                case AXIAL -> {
                    ncpf.type = NCPFPlacementRule.RuleType.axial;
                    ncpf.min /= 2;
                    ncpf.max /= 2;
                    if(adjacent.countType==PlacementRule.CountType.EXACTLY){
                        NCPFPlacementRule and = new NCPFPlacementRule();
                        and.type = NCPFPlacementRule.RuleType.and;

                        NCPFPlacementRule individual = new NCPFPlacementRule();
                        individual.type = ncpf.type;
                        individual.block = ncpf.block;
                        individual.min = ncpf.min*2;
                        individual.max = ncpf.max*2;
                        individual.type = NCPFPlacementRule.RuleType.between;

                        and.rules.add(individual);
                        and.rules.add(ncpf);
                        ncpf = and;
                    }
                }
                case EDGE ->
                    ncpf.type = NCPFPlacementRule.RuleType.edge;
                case STANDARD ->
                    ncpf.type = NCPFPlacementRule.RuleType.between;
                case VERTEX ->
                    ncpf.type = NCPFPlacementRule.RuleType.vertex;
            }
        }
        return ncpf;
    }
}
