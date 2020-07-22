#loader preinit

mods.nuclearcraft.Registration.registerFissionSink("extreme", 210, "exactly one sic_sic_cmc sink");
mods.nuclearcraft.Registration.registerFissionSink("sic_sic_cmc", 215, "exactly one lead sink");

mods.nuclearcraft.Registration.registerFissionHeater("extreme", "tritium", 1, "deuterium", 1, 210, "exactly one sic_sic_cmc heater");
mods.nuclearcraft.Registration.registerFissionHeater("sic_sic_cmc", "helium3", 1, "helium", 1, 215, "exactly one lead heater");

mods.nuclearcraft.Registration.registerTurbineCoil("extreme", 1.14, "four of any coil");
mods.nuclearcraft.Registration.registerTurbineCoil("sic_sic_cmc", 1.16, "exactly two axial connectors");

mods.nuclearcraft.Registration.registerTurbineBlade("manganese", 1.05, 1.35);
mods.nuclearcraft.Registration.registerTurbineBlade("copper", 1.15, 1.55);

mods.nuclearcraft.Registration.registerTurbineStator("magnesium", 0.6);
mods.nuclearcraft.Registration.registerTurbineStator("boron", 0.85);

#mods.nuclearcraft.Extractor.addRecipe(<ore:ingotGold>*8 | <minecraft:diamond>*2, mods.nuclearcraft.ChanceItemIngredient.create(<ore:ingotIron>*4, 50, 2), mods.nuclearcraft.ChanceFluidIngredient.create(<liquid:liquid_nitrogen>*250, 75, 35), 1.5, 2.0);
#mods.nuclearcraft.ChemicalReactor.addRecipe(<liquid:hydrogen>*750 | <liquid:deuterium>*500 | <liquid:tritium>*250, <liquid:water>*500 | null, null, mods.nuclearcraft.ChanceFluidIngredient.create(<liquid:liquid_helium>*100, 40, 10, 60), 2.0, 1.5, 0.000000005);
#mods.nuclearcraft.FuelReprocessor.addRecipe(<ore:ingotSilver>*4, <ore:ingotManganese>*4, <ore:ingotManganeseOxide>*4, <ore:ingotManganeseDioxide>*4, <ore:ingotIron>*5, <ore:ingotGold>*6, null);
#mods.nuclearcraft.Manufactory.addRecipe(<minecraft:magma_cream>*5, null);
#mods.nuclearcraft.SolidFission.addRecipe(<minecraft:magma_cream>*4 | <minecraft:diamond>*2, mods.nuclearcraft.ChanceItemIngredient.create(<ore:ingotSilver>*4, 50, 2), 100, 125, 1.15, 99, true, 0.005);