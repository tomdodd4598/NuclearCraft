#loader contenttweaker

import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionIsotopeInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionIsotopeInfoListContainer;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionFuelInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionFuelInfoListContainer;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionHelper;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionSetup;

import mods.contenttweaker.BlockMaterial;
import mods.contenttweaker.Item;
import mods.contenttweaker.Fluid;
import mods.contenttweaker.ResourceLocation;
import mods.contenttweaker.SoundEvent;
import mods.contenttweaker.VanillaFactory;

import mods.nuclearcraft.ColorHelper;

function registerFissionPellet(unloc as string) {
	var item = VanillaFactory.createItem(unloc);
	item.unlocalizedName = unloc;
	item.textureLocation = ResourceLocation.create("contenttweaker:item/" ~ unloc);
	item.register();
}

function registerFissionFluid(name as string, color as int) {
	var fluid = VanillaFactory.createFluid(name, color);
	fluid.density = 5000;
	fluid.luminosity = 10;
	fluid.temperature = 1200;
	fluid.viscosity = 8000;
	fluid.fillSound = <soundevent:item.bucket.fill_lava>;
	fluid.emptySound = <soundevent:item.bucket.empty_lava>;
	fluid.stillLocation = "nuclearcraft:blocks/fluids/molten_still";
	fluid.flowingLocation = "nuclearcraft:blocks/fluids/molten_flow";
	fluid.material = <blockmaterial:lava>;
	fluid.register();
}

function registerIsotope(info as FissionIsotopeInfo) {
	for suffix in info.suffix {
		var isotope = VanillaFactory.createItem(info.name ~ suffix);
		isotope.register();
	}
	
	if (info.hasLiquid) {
		registerFissionFluid(info.name, info.liquidColor);
	}
}

function registerFuel(info as FissionFuelInfo) {
	for i in 0 to 2 {
		registerFissionPellet(info.solidPrefix[0] ~ info.name ~ info.solidSuffix[i]);
	}
	
	for i in 2 to 6 {
		registerFissionPellet(info.solidPrefix[1] ~ info.name ~ info.solidSuffix[i]);
		registerFissionPellet(info.solidPrefix[2] ~ info.name ~ info.solidSuffix[i]);
	}
	
	if (info.hasLiquid) {
		registerFissionFluid(info.name, info.liquidColor);
		registerFissionFluid(info.name ~ info.liquidSuffix[1], ColorHelper.getFluorideColor(info.liquidColor));
		registerFissionFluid(info.name ~ info.liquidSuffix[2], ColorHelper.getFLIBEColor(info.liquidColor));
		registerFissionFluid(info.liquidPrefix[0] ~ info.name, info.depletedLiquidColor);
		registerFissionFluid(info.liquidPrefix[0] ~ info.name ~ info.liquidSuffix[1], ColorHelper.getFluorideColor(info.depletedLiquidColor));
		registerFissionFluid(info.liquidPrefix[0] ~ info.name ~ info.liquidSuffix[2], ColorHelper.getFLIBEColor(info.depletedLiquidColor));
	}
}

for info in FissionSetup.FISSION_ISOTOPE_INFOS.get() {
	registerIsotope(info);
}

for info in FissionSetup.FISSION_FUEL_INFOS.get() {
	registerFuel(info);
}
