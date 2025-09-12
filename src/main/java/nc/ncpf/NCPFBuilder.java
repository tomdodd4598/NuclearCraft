package nc.ncpf;

import nc.block.IBlockMeta;
import nc.block.fission.*;
import nc.block.fission.port.*;
import nc.enumm.MetaEnums;
import nc.enumm.MetaEnums.*;
import nc.init.NCBlocks;
import nc.integration.crafttweaker.CTRegistration;
import nc.multiblock.*;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.turbine.*;
import nc.ncpf.element.*;
import nc.ncpf.element.legacy.*;
import nc.ncpf.module.NCPFEmptyModule;
import nc.ncpf.value.*;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.recipe.multiblock.*;
import nc.tile.multiblock.ITileMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.item.*;

import java.util.*;
import java.util.function.Predicate;

public class NCPFBuilder {
	
	public static String configContext = "unknown";
	
	public static void translate(List<NCPFElement> list, Block... blocks) {
		for (Block block : blocks) {
			translate(list, block);
		}
	}
	
	public static void translate(List<NCPFElement> list, Block block) {
		translate(list, block, true);
	}
    
    private static List<Block> casingBlocks = new ArrayList<>();
    static{
        casingBlocks.add(NCBlocks.fission_casing);
        casingBlocks.add(NCBlocks.fission_glass);
        casingBlocks.add(NCBlocks.fission_monitor);
        casingBlocks.add(NCBlocks.fission_source_manager);
        casingBlocks.add(NCBlocks.fission_shield_manager);
        casingBlocks.add(NCBlocks.fission_power_port);
        casingBlocks.add(NCBlocks.fission_computer_port);
        casingBlocks.add(NCBlocks.turbine_casing);
        casingBlocks.add(NCBlocks.turbine_glass);
        casingBlocks.add(NCBlocks.turbine_computer_port);
        casingBlocks.add(NCBlocks.turbine_redstone_port);
        casingBlocks.add(NCBlocks.machine_frame);
        casingBlocks.add(NCBlocks.machine_glass);
        casingBlocks.add(NCBlocks.machine_power_port);
        casingBlocks.add(NCBlocks.machine_process_port);
        casingBlocks.add(NCBlocks.machine_reservoir_port);
        casingBlocks.add(NCBlocks.machine_redstone_port);
        casingBlocks.add(NCBlocks.machine_computer_port);
    }
	
	public static void translate(List<NCPFElement> list, Block block, boolean includeModules) {
		List<NCPFElement> newElements = new ArrayList<>();
		
		if (block instanceof IBlockMeta<?> blockMeta) {
			int metadata = 0;
			
			for (Enum<?> variant : blockMeta.getValues()) {
				NCPFLegacyBlock legacyBlock = new NCPFLegacyBlock();
				legacyBlock.name = block.getRegistryName().toString();
				legacyBlock.metadata = metadata++;
				legacyBlock.blockstate.put("type", variant.toString());
				newElements.add(legacyBlock);
				
				if (block instanceof BlockFissionMetaShield) {
					legacyBlock.blockstate.put("active", false);
					
					NCPFLegacyBlock closedShield = new NCPFLegacyBlock();
					closedShield.name = block.getRegistryName().toString();
					closedShield.metadata = metadata;
					closedShield.blockstate.put("type", variant.toString());
					closedShield.blockstate.put("active", true);
					newElements.add(closedShield);
				}
				
				if (block instanceof BlockFissionFluidMetaPort) {
					legacyBlock.blockstate.put("active", false);
					
					NCPFLegacyBlock outputPort = new NCPFLegacyBlock();
					outputPort.name = block.getRegistryName().toString();
					outputPort.metadata = metadata;
					outputPort.blockstate.put("type", variant.toString());
					outputPort.blockstate.put("active", true);
					newElements.add(outputPort);
				}
			}
		}
		else {
			NCPFLegacyBlock legacyBlock = new NCPFLegacyBlock();
			
			if (new ItemStack(block).getHasSubtypes()) {
				legacyBlock.metadata = 0;
			}
			
			legacyBlock.name = block.getRegistryName().toString();
			newElements.add(legacyBlock);
			
			if (block instanceof BlockFissionVent || block instanceof BlockFissionItemPort || block instanceof BlockFissionFluidPort || block instanceof BlockFissionShield) {
				legacyBlock.blockstate.put("active", false);
				
				NCPFLegacyBlock outputBlock = new NCPFLegacyBlock();
				outputBlock.name = legacyBlock.name;
				outputBlock.blockstate.put("active", true);
				newElements.add(outputBlock);
			}
		}
		
		list.addAll(newElements);
		
		if (!includeModules) {
			return;
		}
		
		// Modules and Blockstates
		
		for (NCPFElement elem : newElements) {
			elem.modules = new HashMap<>();
			Integer meta = null;
			Map<String, Object> blockstate = null;
			boolean wall = false;
			
			if (elem instanceof NCPFLegacyBlock legacyBlock) {
				meta = legacyBlock.metadata;
				blockstate = legacyBlock.blockstate;
			}
            
            // Controllers
			
			if (block == NCBlocks.solid_fission_controller
                || block == NCBlocks.salt_fission_controller
                || block == NCBlocks.turbine_controller
                || block == NCBlocks.distiller_controller) {
				elem.modules.put("nuclearcraft:" + configContext + ":controller", new NCPFEmptyModule());
				wall = true;
			}
			
			// Fission
			
			if (block == NCBlocks.fission_vent) {
				Map<String, Object> vent = new HashMap<>();
				vent.put("output", blockstate.get("active"));
				elem.modules.put("nuclearcraft:" + configContext + ":coolant_vent", vent);
				wall = true;
			}
			
			if (block == NCBlocks.fission_source) {
				Map<String, Object> source = new HashMap<>();
				source.put("efficiency", MetaEnums.NeutronSourceType.values()[meta].getEfficiency());
				elem.modules.put("nuclearcraft:" + configContext + ":neutron_source", source);
				wall = true;
			}
			
			if (block == NCBlocks.solid_fission_sink) {
				Map<String, Object> sink = new HashMap<>();
				sink.put("cooling", MetaEnums.HeatSinkType.values()[meta].getCooling());
				elem.modules.put("nuclearcraft:" + configContext + ":heat_sink", sink);
				translatePlacementRules(sink, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
			}
			
			if (block == NCBlocks.solid_fission_sink2) {
				Map<String, Object> sink = new HashMap<>();
				sink.put("cooling", MetaEnums.HeatSinkType2.values()[meta].getCooling());
				elem.modules.put("nuclearcraft:" + configContext + ":heat_sink", sink);
				translatePlacementRules(sink, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
			}
			
			if (block == NCBlocks.salt_fission_heater) {
				Map<String, Object> heaterModule = new HashMap<>();
				elem.modules.put("nuclearcraft:" + configContext + ":heater", heaterModule);
				
				Map<String, Object> ports = new HashMap<>();
				List<NCPFElement> portElements = new ArrayList<>();
				translate(portElements, NCBlocks.fission_heater_port);
				NCPFLegacyBlock heater = (NCPFLegacyBlock) elem;
				for (Iterator<NCPFElement> it = portElements.iterator(); it.hasNext(); ) {
					NCPFLegacyBlock port = (NCPFLegacyBlock) it.next();
					if (!port.blockstate.get("type").equals(heater.blockstate.get("type"))) {
						it.remove();
					}
				}
				ports.put("input", portElements.get(0));
				ports.put("output", portElements.get(1));
				elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);
				
				Map<String, Object> recipesModule = new HashMap<>();
				List<NCPFElement> recipes = new ArrayList<>();
				translate(recipes, NCRecipes.coolant_heater, (recipe) -> ((ItemBlock) recipe.getItemIngredients().get(0).getStack().getItem()).getBlock() == block && recipe.getItemIngredients().get(0).getStack().getMetadata() == heater.metadata);
				recipesModule.put("recipes", recipes);
				elem.modules.put("ncpf:block_recipes", recipesModule);
				translatePlacementRules(heaterModule, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
			}
			
			if (block == NCBlocks.salt_fission_heater2) {
				Map<String, Object> heaterModule = new HashMap<>();
				elem.modules.put("nuclearcraft:" + configContext + ":heater", heaterModule);
				
				Map<String, Object> ports = new HashMap<>();
				List<NCPFElement> portElements = new ArrayList<>();
				translate(portElements, NCBlocks.fission_heater_port2);
				NCPFLegacyBlock heater = (NCPFLegacyBlock) elem;
				for (Iterator<NCPFElement> it = portElements.iterator(); it.hasNext(); ) {
					NCPFLegacyBlock port = (NCPFLegacyBlock) it.next();
					if (!port.blockstate.get("type").equals(heater.blockstate.get("type"))) {
						it.remove();
					}
				}
				ports.put("input", portElements.get(0));
				ports.put("output", portElements.get(1));
				elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);
				
				Map<String, Object> recipesModule = new HashMap<>();
				List<NCPFElement> recipes = new ArrayList<>();
				translate(recipes, NCRecipes.coolant_heater, (recipe) -> ((ItemBlock) recipe.getItemIngredients().get(0).getStack().getItem()).getBlock() == block && recipe.getItemIngredients().get(0).getStack().getMetadata() == heater.metadata);
				recipesModule.put("recipes", recipes);
				elem.modules.put("ncpf:block_recipes", recipesModule);
				translatePlacementRules(heaterModule, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
			}
			
			if (block == NCBlocks.solid_fission_cell) {
				elem.modules.put("nuclearcraft:" + configContext + ":fuel_cell", new NCPFEmptyModule());
				
				Map<String, Object> ports = new HashMap<>();
				List<NCPFElement> portElements = new ArrayList<>();
				translate(portElements, NCBlocks.fission_cell_port);
				ports.put("input", portElements.get(0));
				ports.put("output", portElements.get(1));
				elem.modules.put("nuclearcraft:overhaul_sfr:recipe_ports", ports);
				
				Map<String, Object> recipesModule = new HashMap<>();
				List<NCPFElement> recipes = new ArrayList<>();
				translate(recipes, NCRecipes.solid_fission);
				recipesModule.put("recipes", recipes);
				elem.modules.put("ncpf:block_recipes", recipesModule);
			}
			
			if (block == NCBlocks.salt_fission_vessel) {
				elem.modules.put("nuclearcraft:" + configContext + ":fuel_vessel", new NCPFEmptyModule());
				
				Map<String, Object> ports = new HashMap<>();
				List<NCPFElement> portElements = new ArrayList<>();
				translate(portElements, NCBlocks.fission_vessel_port);
				ports.put("input", portElements.get(0));
				ports.put("output", portElements.get(1));
				elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);
				
				Map<String, Object> recipesModule = new HashMap<>();
				List<NCPFElement> recipes = new ArrayList<>();
				translate(recipes, NCRecipes.salt_fission);
				recipesModule.put("recipes", recipes);
				elem.modules.put("ncpf:block_recipes", recipesModule);
			}
			
			if (block == NCBlocks.fission_irradiator) {
				elem.modules.put("nuclearcraft:" + configContext + ":irradiator", new NCPFEmptyModule());
				
				Map<String, Object> ports = new HashMap<>();
				List<NCPFElement> portElements = new ArrayList<>();
				translate(portElements, NCBlocks.fission_irradiator_port);
				ports.put("input", portElements.get(0));
				ports.put("output", portElements.get(1));
				elem.modules.put("nuclearcraft:"+configContext+":recipe_ports", ports);
				
				Map<String, Object> recipesModule = new HashMap<>();
				List<NCPFElement> recipes = new ArrayList<>();
				translate(recipes, NCRecipes.fission_irradiator);
				recipesModule.put("recipes", recipes);
				elem.modules.put("ncpf:block_recipes", recipesModule);
			}
			
			if (block == NCBlocks.fission_cell_port || block == NCBlocks.fission_irradiator_port || block == NCBlocks.fission_vessel_port || block == NCBlocks.fission_heater_port || block == NCBlocks.fission_heater_port2) {
				Map<String, Object> port = new HashMap<>();
				port.put("output", blockstate.get("active"));
				elem.modules.put("nuclearcraft:" + configContext + ":port", port);
			}
			
			if (block == NCBlocks.fission_conductor) {
				elem.modules.put("nuclearcraft:" + configContext + ":conductor", new NCPFEmptyModule());
			}
			
			if (block == NCBlocks.fission_shield) {
				if (Objects.equals(blockstate.get("active"), Boolean.FALSE)) {
					Map<String, Object> shield = new HashMap<>();
					shield.put("heat_per_flux", MetaEnums.NeutronShieldType.values()[meta].getHeatPerFlux());
					shield.put("efficiency", MetaEnums.NeutronShieldType.values()[meta].getEfficiency());
					shield.put("closed", newElements.get(1));
					elem.modules.put("nuclearcraft:" + configContext + ":neutron_shield", shield);
				}
			}
			
			// Turbine
			
			if (block == NCBlocks.turbine_inlet) {
				elem.modules.put("nuclearcraft:" + configContext + ":inlet", new NCPFEmptyModule());
			}
			
			if (block == NCBlocks.turbine_outlet) {
				elem.modules.put("nuclearcraft:" + configContext + ":outlet", new NCPFEmptyModule());
			}
			
			if (block == NCBlocks.turbine_rotor_blade_steel) {
				Map<String, Object> blade = new HashMap<>();
				blade.put("efficiency", TurbineRotorBladeUtil.TurbineRotorBladeType.STEEL.getEfficiency());
				blade.put("expansion", TurbineRotorBladeUtil.TurbineRotorBladeType.STEEL.getExpansionCoefficient());
				elem.modules.put("nuclearcraft:" + configContext + ":blade", blade);
			}
			
			if (block == NCBlocks.turbine_rotor_blade_extreme) {
				Map<String, Object> blade = new HashMap<>();
				blade.put("efficiency", TurbineRotorBladeUtil.TurbineRotorBladeType.EXTREME.getEfficiency());
				blade.put("expansion", TurbineRotorBladeUtil.TurbineRotorBladeType.EXTREME.getExpansionCoefficient());
				elem.modules.put("nuclearcraft:" + configContext + ":blade", blade);
			}
			
			if (block == NCBlocks.turbine_rotor_blade_sic_sic_cmc) {
				Map<String, Object> blade = new HashMap<>();
				blade.put("efficiency", TurbineRotorBladeUtil.TurbineRotorBladeType.SIC_SIC_CMC.getEfficiency());
				blade.put("expansion", TurbineRotorBladeUtil.TurbineRotorBladeType.SIC_SIC_CMC.getExpansionCoefficient());
				elem.modules.put("nuclearcraft:" + configContext + ":blade", blade);
			}
			
			if (block == NCBlocks.turbine_rotor_stator) {
				Map<String, Object> stator = new HashMap<>();
				stator.put("expansion", TurbineRotorBladeUtil.TurbineRotorStatorType.STANDARD.getExpansionCoefficient());
				elem.modules.put("nuclearcraft:" + configContext + ":stator", stator);
			}
			
			if (block == NCBlocks.turbine_dynamo_coil) {
				Map<String, Object> coil = new HashMap<>();
				coil.put("efficiency", TurbineDynamoCoilType.values()[meta].getConductivity());
				elem.modules.put("nuclearcraft:" + configContext + ":coil", coil);
				translatePlacementRules(coil, block, meta, TurbinePlacement.recipe_handler, TurbinePlacement.RULE_MAP);
			}
			
			if (block == NCBlocks.turbine_coil_connector) {
				Map<String, Object> connector = new HashMap<>();
				elem.modules.put("nuclearcraft:" + configContext + ":connector", connector);
				translatePlacementRules(connector, block, meta, TurbinePlacement.recipe_handler, TurbinePlacement.RULE_MAP);
			}
			
			if (block == NCBlocks.turbine_rotor_bearing) {
				elem.modules.put("nuclearcraft:" + configContext + ":bearing", new NCPFEmptyModule());
			}
			
			if (block == NCBlocks.turbine_rotor_shaft) {
				elem.modules.put("nuclearcraft:" + configContext + ":shaft", new NCPFEmptyModule());
			}
            
            // Distiller
            
            if (block == NCBlocks.distiller_sieve_tray) {
				elem.modules.put("nuclearcraft:" + configContext + ":sieve_tray", new NCPFEmptyModule());
            }
            
            if (block == NCBlocks.distiller_reflux_unit) {
				elem.modules.put("nuclearcraft:" + configContext + ":reflux_unit", new NCPFEmptyModule());
            }
            
            if (block == NCBlocks.distiller_reboiling_unit) {
				elem.modules.put("nuclearcraft:" + configContext + ":reboiling_unit", new NCPFEmptyModule());
            }
            
            if (block == NCBlocks.distiller_liquid_distributor) {
				elem.modules.put("nuclearcraft:" + configContext + ":liquid_distributor", new NCPFEmptyModule());
            }
            
			// CT Blocks
			
			for (CTRegistration.RegistrationInfo info : CTRegistration.INFO_LIST) {
				if (info instanceof CTRegistration.FissionSinkRegistrationInfo inf) {
					if (block == inf.block.get()) {
						Map<String, Object> sink = new HashMap<>();
						sink.put("cooling", inf.cooling);
						elem.modules.put("nuclearcraft:" + configContext + ":heat_sink", sink);
						translatePlacementRules(sink, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
					}
				}
				
				if (info instanceof CTRegistration.FissionHeaterRegistrationInfo inf) {
					if (block == inf.block.get()) {
						Map<String, Object> heaterModule = new HashMap<>();
						elem.modules.put("nuclearcraft:" + configContext + ":heater", heaterModule);
						
						Map<String, Object> ports = new HashMap<>();
						List<NCPFElement> portElements = new ArrayList<>();
						
						// CT Blocks
						
						for (CTRegistration.RegistrationInfo in : CTRegistration.INFO_LIST) {
							if (in instanceof CTRegistration.FissionHeaterPortRegistrationInfo heaterPort) {
								if (heaterPort.heaterID.equals(inf.heaterID)) {
									translate(portElements, heaterPort.block.get());
								}
							}
						}
						
						NCPFLegacyBlock heater = (NCPFLegacyBlock) elem;
						ports.put("input", portElements.get(0));
						ports.put("output", portElements.get(1));
						elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);
						
						Map<String, Object> recipesModule = new HashMap<>();
						List<NCPFElement> recipes = new ArrayList<>();
						translate(recipes, NCRecipes.coolant_heater, (recipe) -> ((ItemBlock) recipe.getItemIngredients().get(0).getStack().getItem()).getBlock() == block);
						recipesModule.put("recipes", recipes);
						elem.modules.put("ncpf:block_recipes", recipesModule);
						translatePlacementRules(heaterModule, block, meta, FissionPlacement.recipe_handler, FissionPlacement.RULE_MAP);
					}
				}
				
				if (info instanceof CTRegistration.FissionHeaterPortRegistrationInfo inf) {
					if (block == inf.block.get()) {
						Map<String, Object> port = new HashMap<>();
						port.put("output", blockstate.get("active"));
						elem.modules.put("nuclearcraft:" + configContext + ":port", port);
					}
				}
				
				if (info instanceof CTRegistration.FissionSourceRegistrationInfo inf) {
					if (block == inf.block.get()) {
						Map<String, Object> source = new HashMap<>();
						source.put("efficiency", inf.efficiency);
						elem.modules.put("nuclearcraft:" + configContext + ":neutron_source", source);
						wall = true;
					}
				}
				
				if (info instanceof CTRegistration.FissionShieldRegistrationInfo inf) {
					if (block == inf.block.get()) {
						if (Objects.equals(blockstate.get("active"), Boolean.FALSE)) {
							Map<String, Object> shield = new HashMap<>();
							shield.put("heat_per_flux", inf.heatPerFlux);
							shield.put("efficiency", inf.efficiency);
							shield.put("closed", newElements.get(1));
							elem.modules.put("nuclearcraft:" + configContext + ":neutron_shield", shield);
						}
					}
				}
				
				if (info instanceof CTRegistration.TurbineCoilRegistrationInfo inf) {
					if (block == inf.block.get()) {
						Map<String, Object> coil = new HashMap<>();
						coil.put("efficiency", inf.conductivity);
						elem.modules.put("nuclearcraft:" + configContext + ":coil", coil);
						translatePlacementRules(coil, block, meta, TurbinePlacement.recipe_handler, TurbinePlacement.RULE_MAP);
					}
				}
				
				if (info instanceof CTRegistration.TurbineBladeRegistrationInfo inf) {
					if (block == inf.block.get()) {
						Map<String, Object> blade = new HashMap<>();
						blade.put("efficiency", inf.bladeType.getEfficiency());
						blade.put("expansion", inf.bladeType.getExpansionCoefficient());
						elem.modules.put("nuclearcraft:" + configContext + ":blade", blade);
					}
				}
				
				if (info instanceof CTRegistration.TurbineStatorRegistrationInfo inf) {
					if (block == inf.block.get()) {
						Map<String, Object> stator = new HashMap<>();
						stator.put("expansion", inf.statorType.getExpansionCoefficient());
						elem.modules.put("nuclearcraft:" + configContext + ":stator", stator);
					}
				}
			}
			
			if (wall || casingBlocks.contains(block)) {
				Map<String, Object> casing = new HashMap<>();
				casing.put("edge", block == NCBlocks.fission_casing || block == NCBlocks.turbine_casing || block == NCBlocks.machine_frame);
				elem.modules.put("nuclearcraft:" + configContext + ":casing", casing);
			}
			
			if (elem.modules.isEmpty()) {
				elem.modules = null;
			}
		}
	}
	
	public static void translate(List<NCPFElement> list, BasicRecipeHandler recipes) {
		translate(list, recipes, x -> true);
	}
	
	public static void translate(List<NCPFElement> list, BasicRecipeHandler recipes, Predicate<BasicRecipe> filter) {
		for (BasicRecipe recipe : recipes.getRecipeList()) {
			if (!filter.test(recipe)) {
				continue;
			}
            
            list.add(translate(recipes, recipe));
		}
	}
    
    public static NCPFElement translate(BasicRecipeHandler recipes, BasicRecipe recipe){
        NCPFRecipe recipeElement = new NCPFRecipe();
        
        boolean hasSkippedTheCoolantHeaterBlockThatThisRecipeBelongsToButThatIsNotActuallyPartOfThisRecipe = false;
        
        for(IItemIngredient ingredient : recipe.getItemIngredients()){
            if(recipes instanceof CoolantHeaterRecipes && !hasSkippedTheCoolantHeaterBlockThatThisRecipeBelongsToButThatIsNotActuallyPartOfThisRecipe){
                hasSkippedTheCoolantHeaterBlockThatThisRecipeBelongsToButThatIsNotActuallyPartOfThisRecipe = true;
                continue;
            }
            if(ingredient instanceof EmptyItemIngredient)continue;
            recipeElement.inputs.add(translateIngredient(ingredient));
        }
        for(IFluidIngredient ingredient : recipe.getFluidIngredients()){
            if(ingredient instanceof EmptyFluidIngredient)continue;
            recipeElement.inputs.add(translateIngredient(ingredient));
        }
        
        for(IItemIngredient ingredient : recipe.getItemProducts()){
            if(ingredient instanceof EmptyItemIngredient)continue;
            recipeElement.outputs.add(translateIngredient(ingredient));
        }
        for(IFluidIngredient ingredient : recipe.getFluidProducts()){
            if(ingredient instanceof EmptyFluidIngredient)continue;
            recipeElement.outputs.add(translateIngredient(ingredient));
        }
        
        for(NCPFElement input : recipeElement.inputs)input.modules = null;
        for(NCPFElement output : recipeElement.outputs)output.modules = null;
        
        NCPFElement element = recipeElement;
        
        if(recipeElement.inputs.size()==1&&recipeElement.outputs.isEmpty()){
            element = recipeElement.inputs.get(0);
            element.quantity = null;
        }

        if (recipes instanceof FissionHeatingRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("heat", recipe.getFissionHeatingHeatPerInputMB());
            stats.put("output_ratio", recipe.getFluidProducts().get(0).getStack().amount / (float) recipe.getFluidIngredients().get(0).getStack().amount);
            element.modules.put("nuclearcraft:overhaul_sfr:coolant_recipe_stats", stats);
        }

        if (recipes instanceof FissionModeratorRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> moderator = new HashMap<>();
            moderator.put("flux", recipe.getFissionModeratorFluxFactor());
            moderator.put("efficiency", recipe.getFissionModeratorEfficiency());
            element.modules.put("nuclearcraft:" + configContext + ":moderator", moderator);
        }

        if (recipes instanceof FissionReflectorRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> reflector = new HashMap<>();
            reflector.put("efficiency", recipe.getFissionReflectorEfficiency());
            reflector.put("reflectivity", recipe.getFissionReflectorReflectivity());
            element.modules.put("nuclearcraft:" + configContext + ":reflector", reflector);
        }

        if (recipes instanceof FissionIrradiatorRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> irradiator = new HashMap<>();
            irradiator.put("heat", recipe.getIrradiatorHeatPerFlux());
            irradiator.put("efficiency", recipe.getIrradiatorProcessEfficiency());
            element.modules.put("nuclearcraft:" + configContext + ":irradiator_stats", irradiator);
        }

        if (recipes instanceof SolidFissionRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> fuel = new HashMap<>();
            fuel.put("efficiency", recipe.getFissionFuelEfficiency());
            fuel.put("heat", recipe.getFissionFuelHeat());
            fuel.put("time", recipe.getFissionFuelTime());
            fuel.put("criticality", recipe.getFissionFuelCriticality());
            fuel.put("self_priming", recipe.getFissionFuelSelfPriming());
            element.modules.put("nuclearcraft:overhaul_sfr:fuel_stats", fuel);
        }

        if (recipes instanceof SaltFissionRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> fuel = new HashMap<>();
            fuel.put("efficiency", recipe.getFissionFuelEfficiency());
            fuel.put("heat", recipe.getFissionFuelHeat());
            fuel.put("time", recipe.getSaltFissionFuelTime());
            fuel.put("criticality", recipe.getFissionFuelCriticality());
            fuel.put("self_priming", recipe.getFissionFuelSelfPriming());
            element.modules.put("nuclearcraft:overhaul_msr:fuel_stats", fuel);
        }

        if (recipes instanceof CoolantHeaterRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> heater = new HashMap<>();
            heater.put("cooling", recipe.getCoolantHeaterCoolingRate());
            element.modules.put("nuclearcraft:overhaul_msr:heater_stats", heater);
        }

        if (recipes instanceof TurbineRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("power", recipe.getTurbinePowerPerMB());
            stats.put("coefficient", recipe.getTurbineExpansionLevel());
            element.modules.put("nuclearcraft:overhaul_turbine:recipe_stats", stats);
        }
        
        if (recipes instanceof MultiblockDistillerRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("time", recipe.getProcessTimeMultiplier());
            stats.put("power", recipe.getProcessPowerMultiplier());
            element.modules.put("nuclearcraft:overhaul_distiller:recipe_stats", stats);
        }
        
        if (recipes instanceof MachineSieveAssemblyRecipes) {
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }
            
            Map<String, Object> assembly = new HashMap<>();
            assembly.put("efficiency", recipe.getMachineSieveAssemblyEfficiency());
            element.modules.put("nuclearcraft:"+configContext+":sieve_assembly", assembly);
        }
        
        return element;
    }
	
	public static void translateIngredients(List<NCPFElement> list, BasicRecipeHandler recipes) {
		for (BasicRecipe recipe : recipes.getRecipeList()) {
            NCPFElement element = translate(recipes, recipe);
            
            if(element instanceof NCPFRecipe){
                NCPFRecipe recip = (NCPFRecipe)element;
                list.addAll(recip.inputs);
                list.addAll(recip.outputs);
            }
		}
	}
	
	private static <T> NCPFElement translateIngredient(IIngredient<T> ingredient) {
		if (ingredient instanceof FluidArrayIngredient array) {
			NCPFListElement list = new NCPFListElement();
			for (IFluidIngredient fluidIngredient : array.ingredientList) {
				list.elements.add(translateIngredient(fluidIngredient));
			}
			return list.elements.size() == 1 ? list.elements.get(0) : list;
		}
		
		if (ingredient instanceof ItemArrayIngredient array) {
			NCPFListElement list = new NCPFListElement();
			for (IItemIngredient ingr : array.ingredientList) {
				list.elements.add(translateIngredient(ingr));
			}
			return list.elements.size() == 1 ? list.elements.get(0) : list;
		}
		
		if (ingredient instanceof ItemIngredient item) {
			if (item.stack.getItem().getRegistryName().toString().equals("forge:bucketfilled")) {
				ItemStack stack = item.stack;
				NCPFLegacyFluid legacyFluid = new NCPFLegacyFluid();
				legacyFluid.name = stack.getTagCompound().getString("FluidName");
                legacyFluid.quantity = 1000;
				return legacyFluid;
			}
			return translate(item.stack);
		}
		
		if (ingredient instanceof FluidIngredient fluid) {
			NCPFLegacyFluid legacyFluid = new NCPFLegacyFluid();
			legacyFluid.name = fluid.fluidName;
            legacyFluid.quantity = fluid.stack.amount;
			return legacyFluid;
		}
		
		if (ingredient instanceof OreIngredient ore) {
			NCPFOredict oredict = new NCPFOredict();
			oredict.oredict = ore.oreName;
            oredict.quantity = ore.stackSize;
			return oredict;
		}
		
		throw new UnsupportedOperationException("Could not translate IIngredient: " + ingredient.getClass().getName());
	}
	
	public static NCPFElement translate(ItemStack stack) {
		return translate(stack, true);
	}
	
	public static NCPFElement translate(ItemStack stack, boolean includeModules) {
		Item item = stack.getItem();
		if (item instanceof ItemBlock itemBlock) {
			Block block = itemBlock.getBlock();
			List<NCPFElement> list = new ArrayList<>();
			translate(list, block, includeModules);
			for (NCPFElement elem : list) {
                elem.quantity = stack.getCount();
				if (elem instanceof NCPFLegacyBlock ncpf && ncpf.metadata != null && ncpf.metadata == stack.getMetadata()) {
					return elem;
				}
			}
			return list.get(0);
		}
		
		NCPFLegacyItem legacyItem = new NCPFLegacyItem();
		legacyItem.name = stack.getItem().getRegistryName().toString();
		if (stack.getItem().getHasSubtypes()) {
			legacyItem.metadata = stack.getMetadata();
		}
        legacyItem.quantity = stack.getCount();
		
		return legacyItem;
	}
	
	private static <MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> void translatePlacementRules(Map<String, Object> module, Block block, Integer meta, PlacementRule.RecipeHandler recipeHandler, Map<String, PlacementRule<MULTIBLOCK, T>> ruleMap) {
		List<NCPFPlacementRule> rules = new ArrayList<>();
		
		for (BasicRecipe recipe : recipeHandler.getRecipeList()) {
			ItemStack stack = recipe.getItemIngredients().get(0).getStack();
			
			if (((ItemBlock) stack.getItem()).getBlock() != block) {
				continue;
			}
			
			if (stack.getHasSubtypes() == (meta == null)) {
				continue;
			}
			
			if (stack.getHasSubtypes() && stack.getMetadata() != meta) {
				continue;
			}
			
			// Recipe is a valid placement rule for this block
			
			String ruleID = recipe.getPlacementRuleID();
			PlacementRule<MULTIBLOCK, T> rule = ruleMap.get(ruleID);
			
			NCPFPlacementRule placementRule = translatePlacementRule(rule);
			rules.add(placementRule);
		}
		
		module.put("rules", rules);
	}
	
	private static <MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> NCPFPlacementRule translatePlacementRule(PlacementRule<MULTIBLOCK, T> rule) {
		NCPFPlacementRule placementRule = new NCPFPlacementRule();
		
		if (rule instanceof PlacementRule.And<?, ?> and) {
			placementRule.type = NCPFPlacementRuleType.and;
			for (PlacementRule<?, ?> subRule : and.subRules) {
				placementRule.rules.add(translatePlacementRule(subRule));
			}
		}
		
		if (rule instanceof PlacementRule.Or<?, ?> or) {
			placementRule.type = NCPFPlacementRuleType.or;
			for (PlacementRule<?, ?> subRule : or.subRules) {
				placementRule.rules.add(translatePlacementRule(subRule));
			}
		}
		
		if (rule instanceof PlacementRule.Adjacent<?, ?> adjacent) {
			if (rule instanceof FissionPlacement.AdjacentCasing) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":casing");
			}
			
			if (rule instanceof FissionPlacement.AdjacentConductor) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":conductor");
			}
			
			if (rule instanceof FissionPlacement.AdjacentModerator) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":moderator");
			}
			
			if (rule instanceof FissionPlacement.AdjacentReflector) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":reflector");
			}
			
			if (rule instanceof FissionPlacement.AdjacentIrradiator) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":irradiator");
			}
			
			if (rule instanceof FissionPlacement.AdjacentShield) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":neutron_shield");
			}
			
			if (rule instanceof FissionPlacement.AdjacentCell) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":fuel_cell");
			}
			
			if (rule instanceof FissionPlacement.AdjacentSink sink) {
				if (sink.sinkType.equals("any")) {
					placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":heat_sink");
				}
				else {
					for (HeatSinkType type : MetaEnums.HeatSinkType.values()) {
						if (sink.sinkType.equals(type.getName())) {
							placementRule.block = translate(new ItemStack(NCBlocks.solid_fission_sink, 1, type.ordinal()), false);
						}
					}
					
					for (HeatSinkType2 type : MetaEnums.HeatSinkType2.values()) {
						if (sink.sinkType.equals(type.getName())) {
							placementRule.block = translate(new ItemStack(NCBlocks.solid_fission_sink2, 1, type.ordinal()), false);
						}
					}
					
					for (CTRegistration.RegistrationInfo info : CTRegistration.INFO_LIST) {
						if (info instanceof CTRegistration.FissionSinkRegistrationInfo inf) {
							if (sink.sinkType.equals(inf.sinkID)) {
								placementRule.block = translate(new ItemStack(inf.block.get()), false);
							}
						}
					}
				}
				
				if (placementRule.block == null) {
					throw new IllegalArgumentException("Could not find target sink: " + sink.sinkType + "!");
				}
			}
			
			if (rule instanceof FissionPlacement.AdjacentVessel) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":fuel_vessel");
			}
			
			if (rule instanceof FissionPlacement.AdjacentHeater heater) {
				if (heater.heaterType.equals("any")) {
					placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":heater");
				}
				else {
					for (CoolantHeaterType type : MetaEnums.CoolantHeaterType.values()) {
						if (heater.heaterType.equals(type.getName())) {
							placementRule.block = translate(new ItemStack(NCBlocks.salt_fission_heater, 1, type.ordinal()), false);
						}
					}
					
					for (CoolantHeaterType2 type : MetaEnums.CoolantHeaterType2.values()) {
						if (heater.heaterType.equals(type.getName())) {
							placementRule.block = translate(new ItemStack(NCBlocks.salt_fission_heater2, 1, type.ordinal()), false);
						}
					}
					
					for (CTRegistration.RegistrationInfo info : CTRegistration.INFO_LIST) {
						if (info instanceof CTRegistration.FissionHeaterRegistrationInfo inf) {
							if (heater.heaterType.equals(inf.heaterID)) {
								placementRule.block = translate(new ItemStack(inf.block.get()), false);
							}
						}
					}
				}
				
				if (placementRule.block == null) {
					throw new IllegalArgumentException("Could not find target heater: " + heater.heaterType + "!");
				}
			}
			
			if (rule instanceof TurbinePlacement.AdjacentCasing) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":casing");
			}
			
			if (rule instanceof TurbinePlacement.AdjacentBearing) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":bearing");
			}
			
			if (rule instanceof TurbinePlacement.AdjacentConnector) {
				placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":connector");
			}
			
			if (rule instanceof TurbinePlacement.AdjacentCoil coil) {
				if (coil.coilType.equals("any")) {
					placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":coil");
				}
				else {
					for (TurbineDynamoCoilType type : TurbineDynamoCoilType.values()) {
						if (coil.coilType.equals(type.getName())) {
							placementRule.block = translate(new ItemStack(NCBlocks.turbine_dynamo_coil, 1, type.ordinal()), false);
						}
					}
					
					for (CTRegistration.RegistrationInfo info : CTRegistration.INFO_LIST) {
						if (info instanceof CTRegistration.TurbineCoilRegistrationInfo inf) {
							if (coil.coilType.equals(inf.coilID)) {
								placementRule.block = translate(new ItemStack(inf.block.get()), false);
							}
						}
					}
				}
				
				if (placementRule.block == null) {
					throw new IllegalArgumentException("Could not find target coil: " + coil.coilType + "!");
				}
			}
			
			if (placementRule.block == null) {
				throw new IllegalArgumentException("Could not find target for rule: " + rule.getClass().getName() + "!");
			}
			
			switch (adjacent.countType) {
				case AT_LEAST -> {
					placementRule.min = adjacent.amount;
					placementRule.max = 6;
				}
				case AT_MOST -> {
					placementRule.min = 0;
					placementRule.max = adjacent.amount;
				}
				case EXACTLY -> placementRule.min = placementRule.max = adjacent.amount;
			}
			
			switch (adjacent.adjType) {
				case AXIAL -> {
					placementRule.type = NCPFPlacementRuleType.axial;
					placementRule.min /= 2;
					placementRule.max /= 2;
					if (adjacent.countType == PlacementRule.CountType.EXACTLY) {
						NCPFPlacementRule and = new NCPFPlacementRule();
						and.type = NCPFPlacementRuleType.and;
						
						NCPFPlacementRule individual = new NCPFPlacementRule();
						individual.type = placementRule.type;
						individual.block = placementRule.block;
						individual.min = placementRule.min * 2;
						individual.max = placementRule.max * 2;
						individual.type = NCPFPlacementRuleType.between;
						
						and.rules.add(individual);
						and.rules.add(placementRule);
						placementRule = and;
					}
				}
				case EDGE -> placementRule.type = NCPFPlacementRuleType.edge;
				case STANDARD -> placementRule.type = NCPFPlacementRuleType.between;
				case VERTEX -> placementRule.type = NCPFPlacementRuleType.vertex;
			}
		}
		
		return placementRule;
	}
}
