#loader nc_preinit

mods.nuclearcraft.Registration.registerFissionSink("extreme", 210, "exactly one sic_sic_cmc sink");
mods.nuclearcraft.Registration.registerFissionSink("sic_sic_cmc", 215, "exactly one lead sink");

mods.nuclearcraft.Registration.registerFissionHeater("extreme", "tritium", 1, "deuterium", 1, 210, "exactly one sic_sic_cmc heater");
mods.nuclearcraft.Registration.registerFissionHeater("sic_sic_cmc", "helium3", 1, "helium", 1, 215, "exactly one lead heater");