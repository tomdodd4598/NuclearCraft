package nc.ncpf;

import com.google.gson.*;
import nc.Global;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.integration.crafttweaker.CTRegistration;
import nc.ncpf.element.*;
import nc.ncpf.module.*;
import nc.ncpf.nuclearcraft.*;
import nc.recipe.NCRecipes;
import nc.util.NCUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.io.*;
import java.util.*;

public class NCPFWriter {
	
	public static void exportNCPF() {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			NCPFRoot ncpf = new NCPFRoot();
			
			ncpf.modules.put("nuclearcraft:generated", new NCPFGeneratedModule());
			
			// Fission SFR
			{
				NCPFBuilder.configContext = "overhaul_sfr";
				NCPFOverhaulSFRConfiguration cfg = new NCPFOverhaulSFRConfiguration();
				
				Map<String, Object> settings = new HashMap<>();
				settings.put("min_size", NCConfig.fission_min_size);
				settings.put("max_size", NCConfig.fission_max_size);
				settings.put("neutron_reach", NCConfig.fission_neutron_reach);
				settings.put("sparsity_penalty_multiplier", NCConfig.fission_sparsity_penalty_params[0]);
				settings.put("sparsity_penalty_threshold", NCConfig.fission_sparsity_penalty_params[1]);
				settings.put("cooling_efficiency_leniency", NCConfig.fission_cooling_efficiency_leniency);
				cfg.modules.put("nuclearcraft:overhaul_sfr_configuration_settings", settings);
				
				// Blocks
				NCPFBuilder.translate(cfg.blocks, NCBlocks.solid_fission_controller);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_monitor);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_source_manager);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_shield_manager);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_vent);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_computer_port);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_casing);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_glass);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_source);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.solid_fission_cell);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_cell_port);
				NCPFBuilder.translate(cfg.blocks, NCRecipes.fission_moderator);
				NCPFBuilder.translate(cfg.blocks, NCRecipes.fission_reflector);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_shield);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_conductor);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_irradiator);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_irradiator_port);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.solid_fission_sink);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.solid_fission_sink2);
				
				// CT Blocks
				for (CTRegistration.RegistrationInfo info : CTRegistration.INFO_LIST) {
					if (info instanceof CTRegistration.FissionSinkRegistrationInfo sink) {
						NCPFBuilder.translate(cfg.blocks, sink.block.get());
					}
					
					if (info instanceof CTRegistration.FissionSourceRegistrationInfo source) {
						NCPFBuilder.translate(cfg.blocks, source.block.get());
					}
					
					if (info instanceof CTRegistration.FissionShieldRegistrationInfo shield) {
						NCPFBuilder.translate(cfg.blocks, shield.block.get());
					}
				}
				
				// Coolant Recipes
				NCPFBuilder.translate(cfg.coolant_recipes, NCRecipes.fission_heating);
				
				Map<String, Object> globalElementsModule = new HashMap<>();
				List<NCPFElement> globalElements = new ArrayList<>();
				
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.fission_heating);
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.solid_fission);
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.fission_irradiator);
				
				List<List<NCPFElement>> lists = new ArrayList<>();
				lists.add(cfg.blocks);
				lists.add(cfg.coolant_recipes);
				
				removeDuplicateNCPFElements(gson, globalElements, lists);
				
				lists.add(globalElements);
				
				for (NCPFElement block : cfg.blocks) {
					if (block.modules == null) {
						continue;
					}
					
					Map<String, Object> blockRecipes = NCPFHelper.get(block.modules, "ncpf:block_recipes");
					if (blockRecipes == null) {
						continue;
					}
					
					List<NCPFElement> recipes = NCPFHelper.get(blockRecipes, "recipes");
					lists.add(recipes);
				}
				
				List<NCPFElement> elementsToHaveOredictTagsAdded = new ArrayList<>();
				List<String> oredictTagsToAddToThoseAforementionedElements = new ArrayList<>();
				
				// Ore Dictionary
				for (List<NCPFElement> elements : lists) {
					//noinspection ForLoopReplaceableByForEach
					for (int i = 0; i < elements.size(); ++i) {
						NCPFElement elem = elements.get(i);
						if (elem instanceof NCPFOredict oredict) {
							oreLoop:
							for (ItemStack stack : OreDictionary.getOres(oredict.oredict, false)) {
								NCPFElement stackElem = NCPFBuilder.translate(stack);
								for (NCPFElement globalElem : globalElements) {
									if (gson.toJson(stackElem).equals(gson.toJson(globalElem))) {
										elementsToHaveOredictTagsAdded.add(globalElem);
										oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
										continue oreLoop;
									}
								}
								
								elementsToHaveOredictTagsAdded.add(stackElem);
								oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
								globalElements.add(stackElem);
							}
						}
					}
				}
				
				for (int i = 0; i < elementsToHaveOredictTagsAdded.size(); ++i) {
					NCPFElement elem = elementsToHaveOredictTagsAdded.get(i);
					if (elem.modules == null) {
						elem.modules = new HashMap<>();
					}
					
					if (!elem.modules.containsKey("plannerator:tags")) {
						Map<String, Object> tags = new HashMap<>();
						tags.put("tags", new ArrayList<String>());
						elem.modules.put("plannerator:tags", tags);
					}
					
					NCPFHelper.<List<String>>get(NCPFHelper.get(elem.modules, "plannerator:tags"), "tags").add(oredictTagsToAddToThoseAforementionedElements.get(i));
				}
				
				globalElementsModule.put("elements", globalElements);
				cfg.modules.put("plannerator:global_elements", globalElementsModule);
				ncpf.configuration.put("nuclearcraft:overhaul_sfr", cfg);
			}
			
			// Fission MSR
			{
				NCPFBuilder.configContext = "overhaul_msr";
				NCPFOverhaulMSRConfiguration cfg = new NCPFOverhaulMSRConfiguration();
				
				Map<String, Object> settings = new HashMap<>();
				settings.put("min_size", NCConfig.fission_min_size);
				settings.put("max_size", NCConfig.fission_max_size);
				settings.put("neutron_reach", NCConfig.fission_neutron_reach);
				settings.put("sparsity_penalty_multiplier", NCConfig.fission_sparsity_penalty_params[0]);
				settings.put("sparsity_penalty_threshold", NCConfig.fission_sparsity_penalty_params[1]);
				settings.put("cooling_efficiency_leniency", NCConfig.fission_cooling_efficiency_leniency);
				cfg.modules.put("nuclearcraft:overhaul_msr_configuration_settings", settings);
				
				// Blocks
				NCPFBuilder.translate(cfg.blocks, NCBlocks.salt_fission_controller);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_monitor);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_source_manager);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_shield_manager);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_computer_port);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_casing);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_glass);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_source);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.salt_fission_vessel);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_vessel_port);
				NCPFBuilder.translate(cfg.blocks, NCRecipes.fission_moderator);
				NCPFBuilder.translate(cfg.blocks, NCRecipes.fission_reflector);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_shield);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_conductor);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_irradiator);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_irradiator_port);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.salt_fission_heater);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_heater_port);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.salt_fission_heater2);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.fission_heater_port2);
				
				// CT Blocks
				for (CTRegistration.RegistrationInfo info : CTRegistration.INFO_LIST) {
					if (info instanceof CTRegistration.FissionHeaterPortRegistrationInfo heaterPort) {
						NCPFBuilder.translate(cfg.blocks, heaterPort.block.get());
					}
					
					if (info instanceof CTRegistration.FissionHeaterRegistrationInfo heater) {
						NCPFBuilder.translate(cfg.blocks, heater.block.get());
					}
					
					if (info instanceof CTRegistration.FissionSourceRegistrationInfo source) {
						NCPFBuilder.translate(cfg.blocks, source.block.get());
					}
					
					if (info instanceof CTRegistration.FissionShieldRegistrationInfo shield) {
						NCPFBuilder.translate(cfg.blocks, shield.block.get());
					}
				}
				
				Map<String, Object> globalElementsModule = new HashMap<>();
				List<NCPFElement> globalElements = new ArrayList<>();
				
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.salt_fission);
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.coolant_heater);
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.fission_irradiator);
				
				List<List<NCPFElement>> lists = new ArrayList<>();
				lists.add(cfg.blocks);
				
				globalLoop:
				for (int i = 0; i < globalElements.size(); ++i) {
					NCPFElement globalElem = globalElements.get(i);
					for (int j = 0; j < globalElements.size(); ++j) {
						if (i == j) {
							continue;
						}
						
						NCPFElement elem = globalElements.get(j);
						if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
							globalElements.remove(j--);
						}
					}
					
					for (List<NCPFElement> list : lists) {
						for (NCPFElement elem : list) {
							if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
								globalElements.remove(i--);
								continue globalLoop;
							}
						}
					}
				}
				
				lists.add(globalElements);
				
				for (NCPFElement block : cfg.blocks) {
					if (block.modules == null) {
						continue;
					}
					
					Map<String, Object> blockRecipes = NCPFHelper.get(block.modules, "ncpf:block_recipes");
					if (blockRecipes == null) {
						continue;
					}
					
					List<NCPFElement> recipes = NCPFHelper.get(blockRecipes, "recipes");
					lists.add(recipes);
				}
				
				List<NCPFElement> elementsToHaveOredictTagsAdded = new ArrayList<>();
				List<String> oredictTagsToAddToThoseAforementionedElements = new ArrayList<>();
				
				// Ore Dictionary
				for (List<NCPFElement> elements : lists) {
					//noinspection ForLoopReplaceableByForEach
					for (int i = 0; i < elements.size(); ++i) {
						NCPFElement elem = elements.get(i);
						if (elem instanceof NCPFOredict oredict) {
							oreLoop:
							for (ItemStack stack : OreDictionary.getOres(oredict.oredict, false)) {
								NCPFElement stackElem = NCPFBuilder.translate(stack);
								for (NCPFElement globalElem : globalElements) {
									if (gson.toJson(stackElem).equals(gson.toJson(globalElem))) {
										elementsToHaveOredictTagsAdded.add(globalElem);
										oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
										continue oreLoop;
									}
								}
								
								elementsToHaveOredictTagsAdded.add(stackElem);
								oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
								globalElements.add(stackElem);
							}
						}
					}
				}
				
				for (int i = 0; i < elementsToHaveOredictTagsAdded.size(); ++i) {
					NCPFElement elem = elementsToHaveOredictTagsAdded.get(i);
					if (elem.modules == null) {
						elem.modules = new HashMap<>();
					}
					
					if (!elem.modules.containsKey("plannerator:tags")) {
						Map<String, Object> tags = new HashMap<>();
						tags.put("tags", new ArrayList<String>());
						elem.modules.put("plannerator:tags", tags);
					}
					
					NCPFHelper.<List<String>>get(NCPFHelper.get(elem.modules, "plannerator:tags"), "tags").add(oredictTagsToAddToThoseAforementionedElements.get(i));
				}
				
				globalElementsModule.put("elements", globalElements);
				cfg.modules.put("plannerator:global_elements", globalElementsModule);
				ncpf.configuration.put("nuclearcraft:overhaul_msr", cfg);
			}
			
			// Turbine
			{
				NCPFBuilder.configContext = "overhaul_turbine";
				NCPFOverhaulTurbineConfiguration cfg = new NCPFOverhaulTurbineConfiguration();
				
				Map<String, Object> settings = new HashMap<>();
				settings.put("min_width", NCConfig.turbine_min_size);
				settings.put("min_length", NCConfig.turbine_min_size);
				settings.put("max_size", NCConfig.turbine_max_size);
				settings.put("throughput_efficiency_leniency_multiplier", NCConfig.turbine_throughput_leniency_params[0]);
				settings.put("throughput_efficiency_leniency_threshold", NCConfig.turbine_throughput_leniency_params[1]);
				settings.put("throughput_factor", NCConfig.turbine_tension_throughput_factor);
				settings.put("fluid_per_blade", NCConfig.turbine_mb_per_blade);
				settings.put("power_bonus", NCConfig.turbine_power_bonus_multiplier);
				cfg.modules.put("nuclearcraft:overhaul_turbine_configuration_settings", settings);
				
				// Blocks
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_controller);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_computer_port);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_redstone_port);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_casing);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_glass);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_inlet);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_outlet);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_rotor_blade_steel);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_rotor_blade_extreme);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_rotor_blade_sic_sic_cmc);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_rotor_stator);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_dynamo_coil);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_coil_connector);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_rotor_bearing);
				NCPFBuilder.translate(cfg.blocks, NCBlocks.turbine_rotor_shaft);
				
				// CT Blocks
				for (CTRegistration.RegistrationInfo info : CTRegistration.INFO_LIST) {
					if (info instanceof CTRegistration.TurbineCoilRegistrationInfo coil) {
						NCPFBuilder.translate(cfg.blocks, coil.block.get());
					}
					
					if (info instanceof CTRegistration.TurbineBladeRegistrationInfo blade) {
						NCPFBuilder.translate(cfg.blocks, blade.block.get());
					}
					
					if (info instanceof CTRegistration.TurbineStatorRegistrationInfo stator) {
						NCPFBuilder.translate(cfg.blocks, stator.block.get());
					}
				}
				
				// Coolant Recipes
				NCPFBuilder.translate(cfg.recipes, NCRecipes.turbine);
				
				Map<String, Object> globalElementsModule = new HashMap<>();
				List<NCPFElement> globalElements = new ArrayList<>();
				
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.turbine);
				
				List<List<NCPFElement>> lists = new ArrayList<>();
				lists.add(cfg.blocks);
				lists.add(cfg.recipes);
				
				globalLoop:
				for (int i = 0; i < globalElements.size(); ++i) {
					NCPFElement globalElem = globalElements.get(i);
					for (int j = 0; j < globalElements.size(); ++j) {
						if (i == j) {
							continue;
						}
						
						NCPFElement elem = globalElements.get(j);
						if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
							globalElements.remove(j--);
						}
					}
					
					for (List<NCPFElement> list : lists) {
						for (NCPFElement elem : list) {
							if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
								globalElements.remove(i--);
								continue globalLoop;
							}
						}
					}
				}
				
				lists.add(globalElements);
				
				for (NCPFElement block : cfg.blocks) {
					if (block.modules == null) {
						continue;
					}
					
					Map<String, Object> blockRecipes = NCPFHelper.get(block.modules, "ncpf:block_recipes");
					if (blockRecipes == null) {
						continue;
					}
					
					List<NCPFElement> recipes = NCPFHelper.get(blockRecipes, "recipes");
					lists.add(recipes);
				}
				
				List<NCPFElement> elementsToHaveOredictTagsAdded = new ArrayList<>();
				List<String> oredictTagsToAddToThoseAforementionedElements = new ArrayList<>();
				
				// Ore Dictionary
				for (List<NCPFElement> elements : lists) {
					//noinspection ForLoopReplaceableByForEach
					for (int i = 0; i < elements.size(); ++i) {
						NCPFElement elem = elements.get(i);
						if (elem instanceof NCPFOredict oredict) {
							oreLoop:
							for (ItemStack stack : OreDictionary.getOres(oredict.oredict, false)) {
								NCPFElement stackElem = NCPFBuilder.translate(stack);
								for (NCPFElement globalElem : globalElements) {
									if (gson.toJson(stackElem).equals(gson.toJson(globalElem))) {
										elementsToHaveOredictTagsAdded.add(globalElem);
										oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
										continue oreLoop;
									}
								}
								
								elementsToHaveOredictTagsAdded.add(stackElem);
								oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
								globalElements.add(stackElem);
							}
						}
					}
				}
				
				for (int i = 0; i < elementsToHaveOredictTagsAdded.size(); ++i) {
					NCPFElement elem = elementsToHaveOredictTagsAdded.get(i);
					if (elem.modules == null) {
						elem.modules = new HashMap<>();
					}
					
					if (!elem.modules.containsKey("plannerator:tags")) {
						Map<String, Object> tags = new HashMap<>();
						tags.put("tags", new ArrayList<String>());
						elem.modules.put("plannerator:tags", tags);
					}
					
					NCPFHelper.<List<String>>get(NCPFHelper.get(elem.modules, "plannerator:tags"), "tags").add(oredictTagsToAddToThoseAforementionedElements.get(i));
				}
				
				globalElementsModule.put("elements", globalElements);
				cfg.modules.put("plannerator:global_elements", globalElementsModule);
				
				ncpf.configuration.put("nuclearcraft:overhaul_turbine", cfg);
			}
            
            // Distiller
            {
                NCPFBuilder.configContext = "overhaul_distiller";
                NCPFOverhaulDistillerConfiguration cfg = new NCPFOverhaulDistillerConfiguration();
                
                Map<String, Object> settings = new HashMap<>();
                settings.put("min_size", NCConfig.machine_min_size);
                settings.put("max_size", NCConfig.machine_max_size);
                settings.put("base_time", NCConfig.machine_distiller_time);
                settings.put("base_power", NCConfig.machine_distiller_power);
                
                // Blocks
                NCPFBuilder.translate(cfg.blocks, NCBlocks.distiller_controller);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.machine_redstone_port);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.machine_computer_port);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.machine_frame);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.machine_glass);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.machine_power_port);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.machine_process_port); //TODO 10 times- 2 inputs, 8 outputs
                NCPFBuilder.translate(cfg.blocks, NCBlocks.distiller_sieve_tray);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.distiller_reflux_unit);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.distiller_reboiling_unit);
                NCPFBuilder.translate(cfg.blocks, NCBlocks.distiller_liquid_distributor);
                NCPFBuilder.translate(cfg.blocks, NCRecipes.machine_sieve_assembly);
                
				// Recipes
				NCPFBuilder.translate(cfg.recipes, NCRecipes.multiblock_distiller);
				
				Map<String, Object> globalElementsModule = new HashMap<>();
				List<NCPFElement> globalElements = new ArrayList<>();
				
				NCPFBuilder.translateIngredients(globalElements, NCRecipes.multiblock_distiller);
				
				List<List<NCPFElement>> lists = new ArrayList<>();
				lists.add(cfg.blocks);
				lists.add(cfg.recipes);
				
				globalLoop:
				for (int i = 0; i < globalElements.size(); ++i) {
					NCPFElement globalElem = globalElements.get(i);
					for (int j = 0; j < globalElements.size(); ++j) {
						if (i == j) {
							continue;
						}
						
						NCPFElement elem = globalElements.get(j);
						if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
							globalElements.remove(j--);
						}
					}
					
					for (List<NCPFElement> list : lists) {
						for (NCPFElement elem : list) {
							if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
								globalElements.remove(i--);
								continue globalLoop;
							}
						}
					}
				}
				
				lists.add(globalElements);
				
				for (NCPFElement block : cfg.blocks) {
					if (block.modules == null) {
						continue;
					}
					
					Map<String, Object> blockRecipes = NCPFHelper.get(block.modules, "ncpf:block_recipes");
					if (blockRecipes == null) {
						continue;
					}
					
					List<NCPFElement> recipes = NCPFHelper.get(blockRecipes, "recipes");
					lists.add(recipes);
				}
				
				List<NCPFElement> elementsToHaveOredictTagsAdded = new ArrayList<>();
				List<String> oredictTagsToAddToThoseAforementionedElements = new ArrayList<>();
				
				// Ore Dictionary
				for (List<NCPFElement> elements : lists) {
					//noinspection ForLoopReplaceableByForEach
					for (int i = 0; i < elements.size(); ++i) {
						NCPFElement elem = elements.get(i);
						if (elem instanceof NCPFOredict oredict) {
							oreLoop:
							for (ItemStack stack : OreDictionary.getOres(oredict.oredict, false)) {
								NCPFElement stackElem = NCPFBuilder.translate(stack);
								for (NCPFElement globalElem : globalElements) {
									if (gson.toJson(stackElem).equals(gson.toJson(globalElem))) {
										elementsToHaveOredictTagsAdded.add(globalElem);
										oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
										continue oreLoop;
									}
								}
								
								elementsToHaveOredictTagsAdded.add(stackElem);
								oredictTagsToAddToThoseAforementionedElements.add(oredict.oredict);
								globalElements.add(stackElem);
							}
						}
					}
				}
				
				for (int i = 0; i < elementsToHaveOredictTagsAdded.size(); ++i) {
					NCPFElement elem = elementsToHaveOredictTagsAdded.get(i);
					if (elem.modules == null) {
						elem.modules = new HashMap<>();
					}
					
					if (!elem.modules.containsKey("plannerator:tags")) {
						Map<String, Object> tags = new HashMap<>();
						tags.put("tags", new ArrayList<String>());
						elem.modules.put("plannerator:tags", tags);
					}
					
					NCPFHelper.<List<String>>get(NCPFHelper.get(elem.modules, "plannerator:tags"), "tags").add(oredictTagsToAddToThoseAforementionedElements.get(i));
				}
				
				globalElementsModule.put("elements", globalElements);
				cfg.modules.put("plannerator:global_elements", globalElementsModule);
				
				ncpf.configuration.put("nuclearcraft:overhaul_distiller", cfg);
            }
			
			try (FileWriter writer = new FileWriter(new File(Loader.instance().getConfigDir(), "nuclearcraft.ncpf.json"))) {
				gson.toJson(ncpf, writer);
			}
		}
		catch (Exception e) {
			NCUtil.getLogger().error("Unable to create nuclearcraft.ncpf.json file!", e);
		}
	}
	
	private static void removeDuplicateNCPFElements(Gson gson, List<NCPFElement> globalElements, List<List<NCPFElement>> lists) {
		for (int i = 0; i < globalElements.size(); ++i) {
			NCPFElement globalElem = globalElements.get(i);
			Map<String, Object> globalModulesWas = globalElem.modules;
			globalElem.modules = null;
			
			for (int j = 0; j < globalElements.size(); ++j) {
				if (i == j) {
					continue;
				}
				
				NCPFElement elem = globalElements.get(j);
				Map<String, Object> modulesWas = elem.modules;
				
				elem.modules = null;
				if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
					globalElements.remove(j--);
				}
				elem.modules = modulesWas;
			}
			
			listLoop:
			for (List<NCPFElement> list : lists) {
				for (NCPFElement elem : list) {
					Map<String, Object> modulesWas = elem.modules;
					
					elem.modules = null;
					if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
						globalElements.remove(i--);
						elem.modules = modulesWas;
						break listLoop;
					}
					elem.modules = modulesWas;
				}
			}
			
			globalElem.modules = globalModulesWas;
		}
	}
}
