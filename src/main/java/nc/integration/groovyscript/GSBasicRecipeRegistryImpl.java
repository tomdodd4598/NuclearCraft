package nc.integration.groovyscript;

import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription.Type;
import nc.integration.groovyscript.GSBasicRecipeBuilderImpl.*;
import nc.recipe.NCRecipes;

public class GSBasicRecipeRegistryImpl {
	
	@RegistryDescription
	public static class GSBasicProcessorRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSBasicProcessorRecipeRegistry(String name) {
			super(name);
		}
		
		public GSBasicProcessorRecipeBuilder builder() {
			return new GSBasicProcessorRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSDecayGeneratorRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSDecayGeneratorRecipeRegistry(String name) {
			super(name);
		}
		
		public GSDecayGeneratorRecipeBuilder builder() {
			return new GSDecayGeneratorRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSDiaphragmRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSDiaphragmRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public void add(Object block, double efficiency, double contactFactor) {
			addRecipeInternal(block, efficiency, contactFactor);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void remove(Object block) {
			removeRecipeWithInputInternal(block);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void removeAll() {
			removeAllRecipesInternal();
		}
	}
	
	@RegistryDescription
	public static class GSSieveAssemblyRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSSieveAssemblyRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public void add(Object block, double efficiency) {
			addRecipeInternal(block, efficiency);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void remove(Object block) {
			removeRecipeWithInputInternal(block);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void removeAll() {
			removeAllRecipesInternal();
		}
	}
	
	@RegistryDescription
	public static class GSMultiblockElectrolyzerRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSMultiblockElectrolyzerRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public static void addElectrolyte(String electrolyteGroup, Object fluid, double efficiency) {
			NCRecipes.multiblock_electrolyzer.addElectrolyte(electrolyteGroup, GSHelper.buildAdditionFluidIngredient(fluid), efficiency);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public static void removeElectrolyte(String electrolyteGroup, Object fluid) {
			NCRecipes.multiblock_electrolyzer.removeElectrolyte(electrolyteGroup, GSHelper.buildAdditionFluidIngredient(fluid));
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public static void removeElectrolyteGroup(String electrolyteGroup) {
			NCRecipes.multiblock_electrolyzer.removeElectrolyteGroup(electrolyteGroup);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public static void removeAllElectrolyteGroups() {
			NCRecipes.multiblock_electrolyzer.removeAllElectrolyteGroups();
		}
		
		public GSMultiblockElectrolyzerRecipeBuilder builder() {
			return new GSMultiblockElectrolyzerRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSElectrolyzerCathodeRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSElectrolyzerCathodeRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public void add(Object block, double efficiency) {
			addRecipeInternal(block, efficiency);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void remove(Object block) {
			removeRecipeWithInputInternal(block);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void removeAll() {
			removeAllRecipesInternal();
		}
	}
	
	@RegistryDescription
	public static class GSElectrolyzerAnodeRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSElectrolyzerAnodeRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public void add(Object block, double efficiency) {
			addRecipeInternal(block, efficiency);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void remove(Object block) {
			removeRecipeWithInputInternal(block);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void removeAll() {
			removeAllRecipesInternal();
		}
	}
	
	@RegistryDescription
	public static class GSMultiblockDistillerRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSMultiblockDistillerRecipeRegistry(String name) {
			super(name);
		}
		
		public GSMultiblockDistillerRecipeBuilder builder() {
			return new GSMultiblockDistillerRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSMultiblockInfiltratorRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSMultiblockInfiltratorRecipeRegistry(String name) {
			super(name);
		}
		
		public GSMultiblockInfiltratorRecipeBuilder builder() {
			return new GSMultiblockInfiltratorRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSInfiltratorPressureFluidRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSInfiltratorPressureFluidRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public void add(Object fluid, double efficiency) {
			addRecipeInternal(fluid, efficiency);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void remove(Object fluid) {
			removeRecipeWithInputInternal(fluid);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void removeAll() {
			removeAllRecipesInternal();
		}
	}
	
	@RegistryDescription
	public static class GSFissionModeratorRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSFissionModeratorRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public void add(Object block, int fluxFactor, double efficiency) {
			addRecipeInternal(block, fluxFactor, efficiency);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void remove(Object block) {
			removeRecipeWithInputInternal(block);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void removeAll() {
			removeAllRecipesInternal();
		}
	}
	
	@RegistryDescription
	public static class GSFissionReflectorRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSFissionReflectorRecipeRegistry(String name) {
			super(name);
		}
		
		@MethodDescription(type = Type.ADDITION)
		public void add(Object block, double efficiency, double reflectivity) {
			addRecipeInternal(block, efficiency, reflectivity);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void remove(Object block) {
			removeRecipeWithInputInternal(block);
		}
		
		@MethodDescription(type = Type.REMOVAL)
		public void removeAll() {
			removeAllRecipesInternal();
		}
	}
	
	@RegistryDescription
	public static class GSFissionIrradiatorRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSFissionIrradiatorRecipeRegistry(String name) {
			super(name);
		}
		
		public GSFissionIrradiatorRecipeBuilder builder() {
			return new GSFissionIrradiatorRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSPebbleFissionRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSPebbleFissionRecipeRegistry(String name) {
			super(name);
		}
		
		public GSPebbleFissionRecipeBuilder builder() {
			return new GSPebbleFissionRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSSolidFissionRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSSolidFissionRecipeRegistry(String name) {
			super(name);
		}
		
		public GSSolidFissionRecipeBuilder builder() {
			return new GSSolidFissionRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSFissionHeatingRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSFissionHeatingRecipeRegistry(String name) {
			super(name);
		}
		
		public GSFissionHeatingRecipeBuilder builder() {
			return new GSFissionHeatingRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSSaltFissionRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSSaltFissionRecipeRegistry(String name) {
			super(name);
		}
		
		public GSSaltFissionRecipeBuilder builder() {
			return new GSSaltFissionRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSFissionEmergencyCoolingRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSFissionEmergencyCoolingRecipeRegistry(String name) {
			super(name);
		}
		
		public GSFissionEmergencyCoolingRecipeBuilder builder() {
			return new GSFissionEmergencyCoolingRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSHeatExchangerRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSHeatExchangerRecipeRegistry(String name) {
			super(name);
		}
		
		public GSHeatExchangerRecipeBuilder builder() {
			return new GSHeatExchangerRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSTurbineRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSTurbineRecipeRegistry(String name) {
			super(name);
		}
		
		public GSTurbineRecipeBuilder builder() {
			return new GSTurbineRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSRadiationScrubberRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSRadiationScrubberRecipeRegistry(String name) {
			super(name);
		}
		
		public GSRadiationScrubberRecipeBuilder builder() {
			return new GSRadiationScrubberRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSRadiationBlockMutationRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSRadiationBlockMutationRecipeRegistry(String name) {
			super(name);
		}
		
		public GSRadiationBlockMutationRecipeBuilder builder() {
			return new GSRadiationBlockMutationRecipeBuilder(this);
		}
	}
	
	@RegistryDescription
	public static class GSRadiationBlockPurificationRecipeRegistry extends GSBasicRecipeRegistry {
		
		public GSRadiationBlockPurificationRecipeRegistry(String name) {
			super(name);
		}
		
		public GSRadiationBlockPurificationRecipeBuilder builder() {
			return new GSRadiationBlockPurificationRecipeBuilder(this);
		}
	}
}
