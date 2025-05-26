package nc.integration.groovyscript;

public class GSBasicRecipeBuilderImpl {
	
	public static class GSBasicProcessorRecipeBuilder extends GSBasicRecipeBuilder<GSBasicProcessorRecipeBuilder> {
		
		public GSBasicProcessorRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSBasicProcessorRecipeBuilder timeMultiplier(double timeMultiplier) {
			return setExtra(0, timeMultiplier);
		}
		
		public GSBasicProcessorRecipeBuilder powerMultiplier(double powerMultiplier) {
			return setExtra(1, powerMultiplier);
		}
		
		public GSBasicProcessorRecipeBuilder processRadiation(double processRadiation) {
			return setExtra(2, processRadiation);
		}
	}
	
	public static class GSDecayGeneratorRecipeBuilder extends GSBasicRecipeBuilder<GSDecayGeneratorRecipeBuilder> {
		
		public GSDecayGeneratorRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSDecayGeneratorRecipeBuilder meanLifetime(double meanLifetime) {
			return setExtra(0, meanLifetime);
		}
		
		public GSDecayGeneratorRecipeBuilder power(double power) {
			return setExtra(1, power);
		}
		
		public GSDecayGeneratorRecipeBuilder radiation(double radiation) {
			return setExtra(2, radiation);
		}
	}
	
	public static class GSMultiblockElectrolyzerRecipeBuilder extends GSBasicRecipeBuilder<GSMultiblockElectrolyzerRecipeBuilder> {
		
		public GSMultiblockElectrolyzerRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSMultiblockElectrolyzerRecipeBuilder timeMultiplier(double timeMultiplier) {
			return setExtra(0, timeMultiplier);
		}
		
		public GSMultiblockElectrolyzerRecipeBuilder powerMultiplier(double powerMultiplier) {
			return setExtra(1, powerMultiplier);
		}
		
		public GSMultiblockElectrolyzerRecipeBuilder processRadiation(double processRadiation) {
			return setExtra(2, processRadiation);
		}
		
		public GSMultiblockElectrolyzerRecipeBuilder electrolyteGroup(String electrolyteGroup) {
			return setExtra(3, electrolyteGroup);
		}
	}
	
	public static class GSMultiblockDistillerRecipeBuilder extends GSBasicRecipeBuilder<GSMultiblockDistillerRecipeBuilder> {
		
		public GSMultiblockDistillerRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSMultiblockDistillerRecipeBuilder timeMultiplier(double timeMultiplier) {
			return setExtra(0, timeMultiplier);
		}
		
		public GSMultiblockDistillerRecipeBuilder powerMultiplier(double powerMultiplier) {
			return setExtra(1, powerMultiplier);
		}
		
		public GSMultiblockDistillerRecipeBuilder processRadiation(double processRadiation) {
			return setExtra(2, processRadiation);
		}
	}
	
	public static class GSMultiblockInfiltratorRecipeBuilder extends GSBasicRecipeBuilder<GSMultiblockInfiltratorRecipeBuilder> {
		
		public GSMultiblockInfiltratorRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSMultiblockInfiltratorRecipeBuilder timeMultiplier(double timeMultiplier) {
			return setExtra(0, timeMultiplier);
		}
		
		public GSMultiblockInfiltratorRecipeBuilder powerMultiplier(double powerMultiplier) {
			return setExtra(1, powerMultiplier);
		}
		
		public GSMultiblockInfiltratorRecipeBuilder processRadiation(double processRadiation) {
			return setExtra(2, processRadiation);
		}
		
		public GSMultiblockInfiltratorRecipeBuilder heatingFactor(double heatingFactor) {
			return setExtra(3, heatingFactor);
		}
	}
	
	public static class GSFissionIrradiatorRecipeBuilder extends GSBasicRecipeBuilder<GSFissionIrradiatorRecipeBuilder> {
		
		public GSFissionIrradiatorRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSFissionIrradiatorRecipeBuilder fluxRequired(long fluxRequired) {
			return setExtra(0, fluxRequired);
		}
		
		public GSFissionIrradiatorRecipeBuilder heatPerFlux(double heatPerFlux) {
			return setExtra(1, heatPerFlux);
		}
		
		public GSFissionIrradiatorRecipeBuilder efficiency(double efficiency) {
			return setExtra(2, efficiency);
		}
		
		public GSFissionIrradiatorRecipeBuilder minFluxPerTick(long minFluxPerTick) {
			return setExtra(3, minFluxPerTick);
		}
		
		public GSFissionIrradiatorRecipeBuilder maxFluxPerTick(long maxFluxPerTick) {
			return setExtra(4, maxFluxPerTick);
		}
		
		public GSFissionIrradiatorRecipeBuilder radiation(double radiation) {
			return setExtra(5, radiation);
		}
	}
	
	public static class GSPebbleFissionRecipeBuilder extends GSBasicRecipeBuilder<GSPebbleFissionRecipeBuilder> {
		
		public GSPebbleFissionRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSPebbleFissionRecipeBuilder time(int time) {
			return setExtra(0, time);
		}
		
		public GSPebbleFissionRecipeBuilder heat(int heat) {
			return setExtra(1, heat);
		}
		
		public GSPebbleFissionRecipeBuilder efficiency(double efficiency) {
			return setExtra(2, efficiency);
		}
		
		public GSPebbleFissionRecipeBuilder criticality(int criticality) {
			return setExtra(3, criticality);
		}
		
		public GSPebbleFissionRecipeBuilder decayFactor(double decayFactor) {
			return setExtra(4, decayFactor);
		}
		
		public GSPebbleFissionRecipeBuilder selfPriming(boolean selfPriming) {
			return setExtra(5, selfPriming);
		}
		
		public GSPebbleFissionRecipeBuilder radiation(double radiation) {
			return setExtra(6, radiation);
		}
	}
	
	public static class GSSolidFissionRecipeBuilder extends GSBasicRecipeBuilder<GSSolidFissionRecipeBuilder> {
		
		public GSSolidFissionRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSSolidFissionRecipeBuilder time(int time) {
			return setExtra(0, time);
		}
		
		public GSSolidFissionRecipeBuilder heat(int heat) {
			return setExtra(1, heat);
		}
		
		public GSSolidFissionRecipeBuilder efficiency(double efficiency) {
			return setExtra(2, efficiency);
		}
		
		public GSSolidFissionRecipeBuilder criticality(int criticality) {
			return setExtra(3, criticality);
		}
		
		public GSSolidFissionRecipeBuilder decayFactor(double decayFactor) {
			return setExtra(4, decayFactor);
		}
		
		public GSSolidFissionRecipeBuilder selfPriming(boolean selfPriming) {
			return setExtra(5, selfPriming);
		}
		
		public GSSolidFissionRecipeBuilder radiation(double radiation) {
			return setExtra(6, radiation);
		}
	}
	
	public static class GSFissionHeatingRecipeBuilder extends GSBasicRecipeBuilder<GSFissionHeatingRecipeBuilder> {
		
		public GSFissionHeatingRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSFissionHeatingRecipeBuilder heatPerInputMB(int heatPerInputMB) {
			return setExtra(0, heatPerInputMB);
		}
	}
	
	public static class GSSaltFissionRecipeBuilder extends GSBasicRecipeBuilder<GSSaltFissionRecipeBuilder> {
		
		public GSSaltFissionRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSSaltFissionRecipeBuilder time(double time) {
			return setExtra(0, time);
		}
		
		public GSSaltFissionRecipeBuilder heat(int heat) {
			return setExtra(1, heat);
		}
		
		public GSSaltFissionRecipeBuilder efficiency(double efficiency) {
			return setExtra(2, efficiency);
		}
		
		public GSSaltFissionRecipeBuilder criticality(int criticality) {
			return setExtra(3, criticality);
		}
		
		public GSSaltFissionRecipeBuilder decayFactor(double decayFactor) {
			return setExtra(4, decayFactor);
		}
		
		public GSSaltFissionRecipeBuilder selfPriming(boolean selfPriming) {
			return setExtra(5, selfPriming);
		}
		
		public GSSaltFissionRecipeBuilder radiation(double radiation) {
			return setExtra(6, radiation);
		}
	}
	
	public static class GSFissionEmergencyCoolingRecipeBuilder extends GSBasicRecipeBuilder<GSFissionEmergencyCoolingRecipeBuilder> {
		
		public GSFissionEmergencyCoolingRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSFissionEmergencyCoolingRecipeBuilder coolingPerInputMB(double coolingPerInputMB) {
			return setExtra(0, coolingPerInputMB);
		}
	}
	
	public static class GSHeatExchangerRecipeBuilder extends GSBasicRecipeBuilder<GSHeatExchangerRecipeBuilder> {
		
		public GSHeatExchangerRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSHeatExchangerRecipeBuilder heatDifference(double heatDifference) {
			return setExtra(0, heatDifference);
		}
		
		public GSHeatExchangerRecipeBuilder temperatureIn(int temperatureIn) {
			return setExtra(1, temperatureIn);
		}
		
		public GSHeatExchangerRecipeBuilder temperatureOut(int temperatureOut) {
			return setExtra(2, temperatureOut);
		}
		
		public GSHeatExchangerRecipeBuilder latentHeating(boolean latentHeating) {
			return setExtra(3, latentHeating);
		}
		
		public GSHeatExchangerRecipeBuilder preferredFlowDirection(int preferredFlowDirection) {
			return setExtra(4, preferredFlowDirection);
		}
		
		public GSHeatExchangerRecipeBuilder flowDirectionBonus(double flowDirectionBonus) {
			return setExtra(5, flowDirectionBonus);
		}
	}
	
	public static class GSTurbineRecipeBuilder extends GSBasicRecipeBuilder<GSTurbineRecipeBuilder> {
		
		public GSTurbineRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSTurbineRecipeBuilder powerPerMB(double powerPerMB) {
			return setExtra(0, powerPerMB);
		}
		
		public GSTurbineRecipeBuilder expansionLevel(double expansionLevel) {
			return setExtra(1, expansionLevel);
		}
		
		public GSTurbineRecipeBuilder spinUpMultiplier(double spinUpMultiplier) {
			return setExtra(2, spinUpMultiplier);
		}
		
		public GSTurbineRecipeBuilder particleEffect(String particleEffect) {
			return setExtra(3, particleEffect);
		}
		
		public GSTurbineRecipeBuilder particleSpeedMultiplier(double particleSpeedMultiplier) {
			return setExtra(4, particleSpeedMultiplier);
		}
	}
	
	public static class GSRadiationScrubberRecipeBuilder extends GSBasicRecipeBuilder<GSRadiationScrubberRecipeBuilder> {
		
		public GSRadiationScrubberRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSRadiationScrubberRecipeBuilder processTime(long processTime) {
			return setExtra(0, processTime);
		}
		
		public GSRadiationScrubberRecipeBuilder processPower(long processPower) {
			return setExtra(1, processPower);
		}
		
		public GSRadiationScrubberRecipeBuilder processEfficiency(double processEfficiency) {
			return setExtra(2, processEfficiency);
		}
	}
	
	public static class GSRadiationBlockMutationRecipeBuilder extends GSBasicRecipeBuilder<GSRadiationBlockMutationRecipeBuilder> {
		
		public GSRadiationBlockMutationRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSRadiationBlockMutationRecipeBuilder radiationThreshold(double radiationThreshold) {
			return setExtra(0, radiationThreshold);
		}
	}
	
	public static class GSRadiationBlockPurificationRecipeBuilder extends GSBasicRecipeBuilder<GSRadiationBlockPurificationRecipeBuilder> {
		
		public GSRadiationBlockPurificationRecipeBuilder(GSBasicRecipeRegistry registry) {
			super(registry);
		}
		
		public GSRadiationBlockPurificationRecipeBuilder radiationThreshold(double radiationThreshold) {
			return setExtra(0, radiationThreshold);
		}
	}
}
