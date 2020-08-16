#loader nc_preinit

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