#mods.nuclearcraft.Extractor.addRecipe(<ore:ingotGold>*8 | <minecraft:diamond>*2, mods.nuclearcraft.ChanceItemIngredient.create(<ore:ingotIron>*4, 50, 2), mods.nuclearcraft.ChanceFluidIngredient.create(<liquid:liquid_nitrogen>*250, 75, 35), 1.5, 2.0);

#mods.nuclearcraft.ChemicalReactor.addRecipe(<liquid:hydrogen>*750 | <liquid:deuterium>*500 | <liquid:tritium>*250, <liquid:water>*500 | null, null, mods.nuclearcraft.ChanceFluidIngredient.create(<liquid:liquid_helium>*100, 40, 10, 60), 2.0, 1.5, 0.000000005);

#mods.nuclearcraft.FuelReprocessor.addRecipe(<ore:ingotSilver>*4, <ore:ingotManganese>*4, <ore:ingotManganeseOxide>*4, <ore:ingotManganeseDioxide>*4, <ore:ingotIron>*5, <ore:ingotGold>*6, null, null, null);

#mods.nuclearcraft.Manufactory.addRecipe(<minecraft:magma_cream>*5, null);

#mods.nuclearcraft.SolidFission.addRecipe(<minecraft:magma_cream>*4 | <minecraft:diamond>*2, mods.nuclearcraft.ChanceItemIngredient.create(<ore:ingotSilver>*4, 50, 2), 100, 125, 1.15, 99, true, 0.005);

#mods.nuclearcraft.DecayHastener.removeRecipeWithInput(<ore:ingotUranium235> | <ore:ingotUranium235Oxide> | <ore:ingotUranium235Nitride>);