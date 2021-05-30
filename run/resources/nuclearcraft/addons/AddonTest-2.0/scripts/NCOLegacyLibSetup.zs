#loader preinit

import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.OreInfo;
import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.LiquidInfo;

import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionIsotopeInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionFuelInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FuelReprocessorInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FuelCentrifugeInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionSetup;

var isotopeSuffix as string[] = ["", "_c", "_ox", "_ni", "_za"];

FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("abc_123", isotopeSuffix, "ABC123", 0.00314));
FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("abc_124", isotopeSuffix, "ABC124", 0.0159));

//FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("xyz_786", isotopeSuffix, "XYZ786", 0x5BFFA5, 0.00265));
//FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("xyz_789", isotopeSuffix, "XYZ789", 0x6EFF51, 0.000358));

FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("def_124", isotopeSuffix, "DEF124", 0.00979));
FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("def_125", isotopeSuffix, "DEF125", 0.0323));
FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("ghi_125", isotopeSuffix, "GHI125", 0.00846));
FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("ghi_126", isotopeSuffix, "GHI126", 0.000264));

//FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("uvw_787", isotopeSuffix, "UVW787", 0x6892AB, 0.00338));
//FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("uvw_788", isotopeSuffix, "UVW788", 0x4D92BD, 0.0327));
//FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("rst_789", isotopeSuffix, "RST789", 0x7269BB, 0.00950));
//FissionSetup.FISSION_ISOTOPE_INFOS.add(FissionIsotopeInfo("rst_790", isotopeSuffix, "RST790", 0x4E68D2, 0.000288));

var solidFuelPrefix as string[] = ["pellet_", "fuel_", "depleted_fuel_"];
var solidFuelSuffix as string[] = ["", "_c", "_tr", "_ox", "_ni", "_za"];
var liquidFuelPrefix as string[] = ["depleted_"];
var liquidFuelSuffix as string[] = ["", "_fluoride", "_fluoride_flibe"];

FissionSetup.FISSION_FUEL_INFOS.add(FissionFuelInfo("leabc_123", solidFuelPrefix, solidFuelSuffix, "LEABC123", 1000, 250, 1.5, 80, 0.075, false, 0.0014, 0.0055));
//FissionSetup.FISSION_FUEL_INFOS.add(FissionFuelInfo("hexyz_789", solidFuelPrefix, solidFuelSuffix, liquidFuelPrefix, liquidFuelSuffix, "HEXYZ789", 0x59FF7A, 0x446FC4, 2000, 150, 1.2, 120, 0.05, true, 0.00079, 0.028));

FissionSetup.FUEL_REPROCESSOR_INFOS.add(FuelReprocessorInfo("ingotDepletedLEABC123", 9, "ingotDEF124", 3, "ingotDEF125", 1, "ingotGHI125", 2, "ingotGHI126", 1, OreInfo("dustStrontium90", 3, 40, 1), OreInfo("dustMolybdenum", 1, 80)));
//FissionSetup.FUEL_REPROCESSOR_INFOS.add(FuelReprocessorInfo("ingotDepletedHEXYZ789", 9, "ingotUVW787", 1, "ingotUVW788", 1, "ingotRST789", 1, "ingotRST790", 2, OreInfo("dustPromethium147", 2), OreInfo("dustEuropium155", 2, 50, 1)));

//FissionSetup.FUEL_CENTRIFUGE_INFOS.add(FuelCentrifugeInfo("depleted_hexyz_789", "uvw_787", 1, "uvw_788", 1, "rst_789", 1, "rst_790", 2, LiquidInfo("promethium_147", 288), LiquidInfo("europium_155", 288, 50, 144, 144)));
