package nc.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import it.unimi.dsi.fastutil.objects.*;
import nc.integration.groovyscript.GSBasicRecipeRegistryImpl.*;
import nc.integration.groovyscript.ingredient.*;
import nc.recipe.NCRecipes;

import java.util.function.Function;

public class GSContainer extends GroovyPropertyContainer {
	
	@GroovyBlacklist
	protected static GSContainer instance;
	
	@GroovyBlacklist
	protected final Object2ObjectMap<String, GSBasicRecipeRegistry> registryCache = new Object2ObjectOpenHashMap<>();
	
	protected GSContainer() {
		super();
		for (String name : NCRecipes.BASIC_PROCESSOR_RECIPE_HANDLER_NAME_ARRAY) {
			addProperty(getRecipeRegistryInternal(name));
		}
		RECIPE_REGISTRY_MAP.forEach((k, v) -> addProperty(v.apply(k)));
		addProperty(new GSStaticRecipeHandler());
		addProperty(new GSChanceItemIngredient());
		addProperty(new GSChanceFluidIngredient());
	}
	
	@GroovyBlacklist
	protected GSBasicRecipeRegistry getRecipeRegistry(String name) {
		GSBasicRecipeRegistry registry = registryCache.get(name);
		if (registry == null) {
			registry = getRecipeRegistryInternal(name);
			addProperty(registry);
			registryCache.put(name, registry);
		}
		return registry;
	}
	
	@GroovyBlacklist
	protected GSBasicRecipeRegistry getRecipeRegistryInternal(String name) {
		return RECIPE_REGISTRY_MAP.getOrDefault(name, GSBasicProcessorRecipeRegistry::new).apply(name);
	}
	
	@GroovyBlacklist
	private static final Object2ObjectMap<String, Function<String, GSBasicRecipeRegistry>> RECIPE_REGISTRY_MAP = new Object2ObjectLinkedOpenHashMap<>();
	
	static {
		RECIPE_REGISTRY_MAP.put("decay_generator", GSDecayGeneratorRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("machine_diaphragm", GSDiaphragmRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("machine_sieve_assembly", GSSieveAssemblyRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("multiblock_electrolyzer", GSMultiblockElectrolyzerRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("electrolyzer_cathode", GSElectrolyzerCathodeRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("electrolyzer_anode", GSElectrolyzerAnodeRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("multiblock_distiller", GSMultiblockDistillerRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("multiblock_infiltrator", GSMultiblockInfiltratorRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("infiltrator_pressure_fluid", GSInfiltratorPressureFluidRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("multiblock_decay_pool", GSMultiblockDecayPoolRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("decay_pool_heat_source", GSDecayPoolHeatSourceRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("fission_moderator", GSFissionModeratorRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("fission_reflector", GSFissionReflectorRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("fission_irradiator", GSFissionIrradiatorRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("pebble_fission", GSPebbleFissionRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("solid_fission", GSSolidFissionRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("fission_heating", GSFissionHeatingRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("salt_fission", GSSaltFissionRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("fission_emergency_cooling", GSFissionEmergencyCoolingRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("heat_exchanger", GSHeatExchangerRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("condenser", GSCondenserRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("condenser_dissipation_fluid", GSCondenserDissipationFluidRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("turbine", GSTurbineRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("radiation_scrubber", GSRadiationScrubberRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("radiation_block_mutation", GSRadiationBlockMutationRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("radiation_block_purification", GSRadiationBlockPurificationRecipeRegistry::new);
		RECIPE_REGISTRY_MAP.put("wasteland_block_mapping", GSWastelandBlockMappingRecipeRegistry::new);
	}
}
