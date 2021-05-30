#loader nc_preinit

import mods.nuclearcraft.Registration;

Registration.registerFissionIsotope("fission_isotope_addon_test", "xyz_786", null, "XYZ786", 0.00265, true, true, true, true, true, 0x5BFFA5);
Registration.registerFissionIsotope("fission_isotope_addon_test", "xyz_789", null, "XYZ789", 0.000358, true, true, true, true, true, 0x6EFF51);

Registration.registerFissionIsotope("fission_isotope_addon_test", "uvw_787", null, "UVW787", 0.00338, true, true, true, true, true, 0x6892AB);
Registration.registerFissionIsotope("fission_isotope_addon_test", "uvw_788", null, "UVW788", 0.0327, true, true, true, true, true, 0x4D92BD);
Registration.registerFissionIsotope("fission_isotope_addon_test", "rst_789", null, "RST789", 0.00950, true, true, true, true, true, 0x7269BB);
Registration.registerFissionIsotope("fission_isotope_addon_test", "rst_790", null, "RST790", 0.000288, true, true, true, true, true, 0x4E68D2);

Registration.registerFissionFuel("fission_fuel_addon_test", "hexyz_789", "nuclearcraft:item/pellet", "HEXYZ789", 1000, 250, 1.5, 80, 0.075, false, 0.00045, 0.00079, 0.028, true, true, true, true, true, true, 0x59FF7A, 0x446FC4);

//FissionSetup.FUEL_REPROCESSOR_INFOS.add(FuelReprocessorInfo("ingotDepletedHEXYZ789", 9, "ingotUVW787", 1, "ingotUVW788", 1, "ingotRST789", 1, "ingotRST790", 2, OreInfo("dustPromethium147", 2), OreInfo("dustEuropium155", 2, 50, 1)));

//FissionSetup.FUEL_CENTRIFUGE_INFOS.add(FuelCentrifugeInfo("depleted_hexyz_789", "uvw_787", 1, "uvw_788", 1, "rst_789", 1, "rst_790", 2, LiquidInfo("promethium_147", 288), LiquidInfo("europium_155", 288, 50, 144, 144)));
