package nc.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import it.unimi.dsi.fastutil.objects.*;
import nc.integration.groovyscript.GSBasicRecipeRegistryImpl.*;
import nc.integration.groovyscript.ingredient.*;
import nc.recipe.NCRecipes;

public class GSContainer extends GroovyPropertyContainer {
	
	@GroovyBlacklist
	protected static GSContainer instance;
	
	@GroovyBlacklist
	protected final Object2ObjectMap<String, GSBasicRecipeRegistry> registryCache = new Object2ObjectOpenHashMap<>();
	
	protected GSContainer() {
		super();
		for (String name : NCRecipes.CT_RECIPE_HANDLER_NAME_ARRAY) {
			addProperty(getRecipeRegistryInternal(name));
		}
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
		return switch (name) {
			case "decay_generator" -> new GSDecayGeneratorRecipeRegistry(name);
			case "machine_diaphragm" -> new GSDiaphragmRecipeRegistry(name);
			case "machine_sieve_assembly" -> new GSSieveAssemblyRecipeRegistry(name);
			case "multiblock_electrolyzer" -> new GSMultiblockElectrolyzerRecipeRegistry(name);
			case "electrolyzer_cathode" -> new GSElectrolyzerCathodeRecipeRegistry(name);
			case "electrolyzer_anode" -> new GSElectrolyzerAnodeRecipeRegistry(name);
			case "multiblock_distiller" -> new GSMultiblockDistillerRecipeRegistry(name);
			case "fission_moderator" -> new GSFissionModeratorRecipeRegistry(name);
			case "fission_reflector" -> new GSFissionReflectorRecipeRegistry(name);
			case "fission_irradiator" -> new GSFissionIrradiatorRecipeRegistry(name);
			case "pebble_fission" -> new GSPebbleFissionRecipeRegistry(name);
			case "solid_fission" -> new GSSolidFissionRecipeRegistry(name);
			case "fission_heating" -> new GSFissionHeatingRecipeRegistry(name);
			case "salt_fission" -> new GSSaltFissionRecipeRegistry(name);
			case "fission_emergency_cooling" -> new GSFissionEmergencyCoolingRecipeRegistry(name);
			case "heat_exchanger" -> new GSHeatExchangerRecipeRegistry(name);
			case "turbine" -> new GSTurbineRecipeRegistry(name);
			case "radiation_scrubber" -> new GSRadiationScrubberRecipeRegistry(name);
			case "radiation_block_mutation" -> new GSRadiationBlockMutationRecipeRegistry(name);
			case "radiation_block_purification" -> new GSRadiationBlockPurificationRecipeRegistry(name);
			default -> new GSBasicProcessorRecipeRegistry(name);
		};
	}
}
