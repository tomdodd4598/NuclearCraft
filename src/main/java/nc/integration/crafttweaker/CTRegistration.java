package nc.integration.crafttweaker;

import static nc.config.NCConfig.turbine_mb_per_blade;
import static nc.util.FluidStackHelper.*;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.mc1120.util.CraftTweakerPlatformUtils;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.NCInfo;
import nc.block.item.energy.ItemBlockBattery;
import nc.handler.OreDictHandler;
import nc.init.*;
import nc.item.NCItemMetaArray;
import nc.multiblock.battery.block.BlockBattery;
import nc.multiblock.battery.tile.TileBattery;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.fission.block.*;
import nc.multiblock.fission.block.port.BlockFissionFluidPort;
import nc.multiblock.fission.salt.block.BlockSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.solid.block.BlockSolidFissionSink;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import nc.multiblock.rtg.block.BlockRTG;
import nc.multiblock.rtg.tile.TileRTG;
import nc.multiblock.turbine.TurbinePlacement;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import nc.multiblock.turbine.block.*;
import nc.multiblock.turbine.tile.*;
import nc.radiation.RadSources;
import nc.recipe.*;
import nc.recipe.ingredient.EmptyFluidIngredient;
import nc.tab.NCTabs;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.annotations.Optional;

@ZenClass("mods.nuclearcraft.Registration")
@ZenRegister
public class CTRegistration {
	
	public static final List<RegistrationInfo> INFO_LIST = new ArrayList<>();
	
	public static final Object2ObjectMap<String, FissionIsotopeRegistrationInfo> FISSION_ISOTOPE_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
	public static final Object2ObjectMap<String, FissionFuelRegistrationInfo> FISSION_FUEL_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
	
	@ZenMethod
	public static void registerFissionSink(String sinkID, int cooling, String rule) {
		
		Block sink = NCBlocks.withName(new BlockSolidFissionSink() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileSolidFissionSink(sinkID, cooling, sinkID + "_sink");
			}
		}, "solid_fission_sink_" + sinkID);
		
		INFO_LIST.add(new FissionSinkRegistrationInfo(sink, sinkID, cooling, rule));
		CraftTweakerAPI.logInfo("Registered fission heat sink with ID \"" + sinkID + "\", cooling rate " + cooling + " H/t and placement rule \"" + rule + "\"");
	}
	
	@ZenMethod
	public static void registerFissionHeater(String heaterID, String fluidInput, int inputAmount, String fluidOutput, int outputAmount, int cooling, String rule) {
		
		Block port = NCBlocks.withName(new BlockFissionFluidPort<TileFissionHeaterPort, TileSaltFissionHeater>(TileFissionHeaterPort.class, 303) {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileFissionHeaterPort(heaterID, fluidInput);
			}
		}, "fission_heater_port_" + heaterID);
		
		Block heater = NCBlocks.withName(new BlockSaltFissionHeater() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileSaltFissionHeater(heaterID, fluidInput);
			}
		}, "salt_fission_heater_" + heaterID);
		
		INFO_LIST.add(new FissionHeaterPortRegistrationInfo(port, heaterID));
		INFO_LIST.add(new FissionHeaterRegistrationInfo(heater, heaterID, fluidInput, inputAmount, fluidOutput, outputAmount, cooling, rule));
		CraftTweakerAPI.logInfo("Registered fission coolant heater and a respective port with ID \"" + heaterID + "\", cooling rate " + cooling + " H/t, placement rule \"" + rule + "\" and recipe [" + inputAmount + " * " + fluidInput + " -> " + outputAmount + " * " + fluidOutput + "]");
	}
	
	@ZenMethod
	public static void registerFissionSource(String sourceID, double efficiency) {
		
		Block source = NCBlocks.withName(new BlockFissionSource() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileFissionSource(efficiency);
			}
		}, "fission_source_" + sourceID);
		
		INFO_LIST.add(new FissionSourceRegistrationInfo(source, efficiency));
		CraftTweakerAPI.logInfo("Registered fission neutron source with ID \"" + sourceID + "\" and efficiency " + efficiency);
	}
	
	@ZenMethod
	public static void registerFissionShield(String shieldID, double heatPerFlux, double efficiency) {
		
		Block shield = NCBlocks.withName(new BlockFissionShield() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileFissionShield(heatPerFlux, efficiency);
			}
		}, "fission_shield_" + shieldID);
		
		INFO_LIST.add(new FissionShieldRegistrationInfo(shield, heatPerFlux, efficiency));
		CraftTweakerAPI.logInfo("Registered fission neutron shield with ID \"" + shieldID + "\", heat per flux " + heatPerFlux + " H/N and efficiency " + efficiency);
	}
	
	@ZenMethod
	public static void registerTurbineCoil(String coilID, double conductivity, String rule) {
		
		Block coil = NCBlocks.withName(new BlockTurbineDynamoCoil() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileTurbineDynamoCoil(coilID, conductivity, coilID + "_coil");
			}
		}, "turbine_dynamo_coil_" + coilID);
		
		INFO_LIST.add(new TurbineCoilRegistrationInfo(coil, coilID, conductivity, rule));
		CraftTweakerAPI.logInfo("Registered turbine dynamo coil with ID \"" + coilID + "\", conductivity " + conductivity + " and placement rule \"" + rule + "\"");
	}
	
	@ZenMethod
	public static void registerTurbineBlade(String bladeID, double efficiency, double expansionCoefficient) {
		
		IRotorBladeType bladeType = new IRotorBladeType() {
			
			@Override
			public String getName() {
				return bladeID;
			}
			
			@Override
			public double getEfficiency() {
				return efficiency;
			}
			
			@Override
			public double getExpansionCoefficient() {
				return expansionCoefficient;
			}
			
		};
		
		Block blade = NCBlocks.withName(new BlockTurbineRotorBlade(null) {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileTurbineRotorBlade(bladeType);
			}
		}, "turbine_rotor_blade_" + bladeID);
		
		INFO_LIST.add(new TurbineBladeRegistrationInfo(blade, efficiency, expansionCoefficient));
		CraftTweakerAPI.logInfo("Registered turbine rotor blade with ID \"" + bladeID + "\", efficiency " + efficiency + " and expansion coefficient " + expansionCoefficient);
	}
	
	@ZenMethod
	public static void registerTurbineStator(String statorID, double expansionCoefficient) {
		
		IRotorStatorType statorType = new IRotorStatorType() {
			
			@Override
			public String getName() {
				return statorID;
			}
			
			@Override
			public double getExpansionCoefficient() {
				return expansionCoefficient;
			}
			
		};
		
		Block stator = NCBlocks.withName(new BlockTurbineRotorStator() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileTurbineRotorStator(statorType);
			}
		}, "turbine_rotor_stator_" + statorID);
		
		INFO_LIST.add(new TurbineStatorRegistrationInfo(stator, expansionCoefficient));
		CraftTweakerAPI.logInfo("Registered turbine rotor stator with ID \"" + statorID + "\" and expansion coefficient " + expansionCoefficient);
	}
	
	@ZenMethod
	public static void registerRTG(String rtgID, long power, double radiation) {
		
		Block rtg = NCBlocks.withName(new BlockRTG(null) {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileRTG(power, radiation);
			}
		}, "rtg_" + rtgID);
		
		INFO_LIST.add(new RTGRegistrationInfo(rtg, power));
		CraftTweakerAPI.logInfo("Registered RTG with ID \"" + rtgID + "\", power " + power + " RF/t and radiation " + radiation + " Rad/t");
	}
	
	@ZenMethod
	public static void registerBattery(String batteryID, long capacity, int energyTier) {
		
		Block battery = NCBlocks.withName(new BlockBattery(null) {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileBattery(capacity, energyTier);
			}
		}, "battery_" + batteryID);
		
		INFO_LIST.add(new BatteryRegistrationInfo(battery, capacity, energyTier));
		CraftTweakerAPI.logInfo("Registered battery with ID \"" + batteryID + "\", capacity " + capacity + " RF/t and energy tier " + energyTier);
	}
	
	private static void addFissionIsotopeItem(FissionIsotopeRegistrationInfo info, String name, String model, String ore) {
		info.types.add(name);
		info.models.add(model);
		info.textures.add(name);
		info.ores.add(ore);
	}
	
	@ZenMethod
	public static void registerFissionIsotope(String item_, String name_, String model, String ore_, double radiation, boolean raw, boolean carbide, boolean oxide, boolean nitride, boolean zirconiumAlloy, @Optional(valueLong = Long.MIN_VALUE) long fluidColor) {
		final String item = item_.toLowerCase(Locale.ROOT);
		final String name = name_.toLowerCase(Locale.ROOT);
		final String ore = StringHelper.capitalize(ore_);
		
		FissionIsotopeRegistrationInfo info;
		if (FISSION_ISOTOPE_INFO_MAP.containsKey(item)) {
			info = FISSION_ISOTOPE_INFO_MAP.get(item);
		}
		else {
			info = new FissionIsotopeRegistrationInfo(item);
			FISSION_ISOTOPE_INFO_MAP.put(item, info);
			INFO_LIST.add(info);
		}
		
		if (raw || carbide || oxide || nitride || zirconiumAlloy) {
			info.rawNames.add(name);
			info.rawOres.add(ore);
		}
		else {
			info.rawNames.add(null);
			info.rawOres.add(null);
		}
		
		if (fluidColor != Long.MIN_VALUE) {
			info.rawFluids.add(name);
			info.rawFluidColors.add((int) fluidColor);
		}
		else {
			info.rawFluids.add(null);
			info.rawFluidColors.add(0);
		}
		
		if (raw) {
			addFissionIsotopeItem(info, name, model, "ingot" + ore);
		}
		
		if (carbide) {
			addFissionIsotopeItem(info, name + "_c", model, "ingot" + ore + "Carbide");
		}
		
		if (oxide) {
			addFissionIsotopeItem(info, name + "_ox", model, "ingot" + ore + "Oxide");
		}
		
		if (nitride) {
			addFissionIsotopeItem(info, name + "_ni", model, "ingot" + ore + "Nitride");
		}
		
		if (zirconiumAlloy) {
			addFissionIsotopeItem(info, name + "_za", model, "ingot" + ore + "ZA");
		}
		
		RadSources.RUNNABLES.add(() -> RadSources.putIsotope(radiation, ore, name));
		
		CraftTweakerAPI.logInfo("Registered fission isotope with name \"" + name + "\", ore dict base entry \"" + ore + "\" and radiation " + radiation + " Rad/t");
	}
	
	private static void addFissionFuelItem(FissionFuelRegistrationInfo info, String name, String model, String ore) {
		info.types.add(name);
		info.models.add(model);
		info.textures.add(name);
		info.ores.add(ore);
	}
	
	private static class FissionFuelStats {
		
		final int time;
		final int heat;
		final double efficiency;
		final int crit;
		final double decay;
		final boolean prime;
		final double radiation;
		
		public FissionFuelStats(int time, int heat, double efficiency, int crit, double decay, boolean prime, double radiation) {
			this.time = time;
			this.heat = heat;
			this.efficiency = efficiency;
			this.crit = crit;
			this.decay = decay;
			this.prime = prime;
			this.radiation = radiation;
		}
	}
	
	@ZenMethod
	public static void registerFissionFuel(String item_, String name_, String model, String ore_, int time, int heat, double efficiency, int crit, double decay, boolean prime, double fissionRadiation, double fuelRadiation, double depletedRadiation, boolean raw, boolean carbide, boolean triso, boolean oxide, boolean nitride, boolean zirconiumAlloy, @Optional(valueLong = Long.MIN_VALUE) long fluidColor, @Optional(valueLong = Long.MIN_VALUE) long depletedFluidColor) {
		final String item = item_.toLowerCase(Locale.ROOT);
		final String name = name_.toLowerCase(Locale.ROOT);
		final String ore = StringHelper.capitalize(ore_);
		
		FissionFuelRegistrationInfo info;
		if (FISSION_FUEL_INFO_MAP.containsKey(item)) {
			info = FISSION_FUEL_INFO_MAP.get(item);
		}
		else {
			info = new FissionFuelRegistrationInfo(item);
			FISSION_FUEL_INFO_MAP.put(item, info);
			INFO_LIST.add(info);
		}
		
		if (raw || carbide || triso || oxide || nitride || zirconiumAlloy) {
			info.rawNames.add(name);
			info.rawOres.add(ore);
		}
		else {
			info.rawNames.add(null);
			info.rawOres.add(null);
		}
		
		if (fluidColor != Long.MIN_VALUE || depletedFluidColor != Long.MIN_VALUE) {
			info.rawFluids.add(name);
			info.rawFluidColors.add((int) fluidColor);
			info.rawDepletedFluidColors.add((int) depletedFluidColor);
		}
		else {
			info.rawFluids.add(null);
			info.rawFluidColors.add(0);
			info.rawDepletedFluidColors.add(0);
		}
		
		info.fissionStats.add(new FissionFuelStats(time, heat, efficiency, crit, decay, prime, fissionRadiation));
		
		if (raw) {
			addFissionFuelItem(info, name, model, "ingot" + ore);
		}
		
		if (carbide) {
			addFissionFuelItem(info, name + "_c", model, "ingot" + ore + "Carbide");
		}
		
		if (triso) {
			addFissionFuelItem(info, name + "_tr", model, "ingot" + ore + "TRISO");
		}
		
		if (oxide) {
			addFissionFuelItem(info, name + "_ox", model, "ingot" + ore + "Oxide");
		}
		
		if (nitride) {
			addFissionFuelItem(info, name + "_ni", model, "ingot" + ore + "Nitride");
		}
		
		if (zirconiumAlloy) {
			addFissionFuelItem(info, name + "_za", model, "ingot" + ore + "ZA");
		}
		
		if (triso) {
			addFissionFuelItem(info, "depleted_" + name + "_tr", model, "ingotDepleted" + ore + "TRISO");
		}
		
		if (oxide) {
			addFissionFuelItem(info, "depleted_" + name + "_ox", model, "ingotDepleted" + ore + "Oxide");
		}
		
		if (nitride) {
			addFissionFuelItem(info, "depleted_" + name + "_ni", model, "ingotDepleted" + ore + "Nitride");
		}
		
		if (zirconiumAlloy) {
			addFissionFuelItem(info, "depleted_" + name + "_za", model, "ingotDepleted" + ore + "ZA");
		}
		
		RadSources.RUNNABLES.add(() -> RadSources.putFuel(fuelRadiation, depletedRadiation, ore, name));
		
		CraftTweakerAPI.logInfo("Registered fission fuel with name \"" + name + "\", item model \"" + model + "\", ore dict base entry \"" + ore + "\", radiation " + fuelRadiation + " Rad/t and depleted radiation " + depletedRadiation + " Rad/t");
	}
	
	// Registration Wrapper
	
	public abstract static class RegistrationInfo {
		
		public abstract void preInit();
		
		public abstract void recipeInit();
		
		public abstract void init();
		
		public abstract void postInit();
	}
	
	public static class BlockRegistrationInfo extends RegistrationInfo {
		
		protected final Block block;
		
		public BlockRegistrationInfo(Block block) {
			this.block = block;
		}
		
		@Override
		public void preInit() {
			registerBlock();
			
			if (CraftTweakerPlatformUtils.isClient()) {
				registerRender();
			}
		}
		
		public void registerBlock() {
			NCBlocks.registerBlock(block);
		}
		
		public void registerRender() {
			NCBlocks.registerRender(block);
		}
		
		@Override
		public void recipeInit() {}
		
		@Override
		public void init() {}
		
		@Override
		public void postInit() {}
	}
	
	public static class TileBlockRegistrationInfo extends BlockRegistrationInfo {
		
		public TileBlockRegistrationInfo(Block block) {
			super(block);
		}
	}
	
	public static class FissionSinkRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final String sinkID, rule;
		protected final int cooling;
		
		FissionSinkRegistrationInfo(Block block, String sinkID, int cooling, String rule) {
			super(block);
			this.sinkID = sinkID;
			this.cooling = cooling;
			this.rule = rule;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, TextFormatting.BLUE, NCInfo.coolingRateInfo(cooling, "solid_fission_sink"), TextFormatting.AQUA, InfoHelper.NULL_ARRAY);
		}
		
		@Override
		public void init() {
			super.init();
			FissionPlacement.addRule(sinkID + "_sink", rule, block);
		}
	}
	
	public static class FissionHeaterRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final String heaterID, fluidInput, fluidOutput, rule;
		protected final int inputAmount, outputAmount, cooling;
		
		FissionHeaterRegistrationInfo(Block block, String heaterID, String fluidInput, int inputAmount, String fluidOutput, int outputAmount, int cooling, String rule) {
			super(block);
			this.heaterID = heaterID;
			this.fluidInput = fluidInput;
			this.inputAmount = inputAmount;
			this.fluidOutput = fluidOutput;
			this.outputAmount = outputAmount;
			this.cooling = cooling;
			this.rule = rule;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, TextFormatting.BLUE, NCInfo.coolingRateInfo(cooling, "salt_fission_heater"), TextFormatting.AQUA, InfoHelper.NULL_ARRAY);
		}
		
		@Override
		public void recipeInit() {
			NCRecipes.coolant_heater.addRecipe(block, AbstractRecipeHandler.fluidStack(fluidInput, inputAmount), AbstractRecipeHandler.fluidStack(fluidOutput, outputAmount), cooling, heaterID + "_heater");
		}
		
		@Override
		public void init() {
			super.init();
			FissionPlacement.addRule(heaterID + "_heater", rule, block);
		}
	}
	
	public static class FissionHeaterPortRegistrationInfo extends TileBlockRegistrationInfo {
		
		FissionHeaterPortRegistrationInfo(Block block, String heaterID) {
			super(block);
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block);
		}
	}
	
	public static class FissionSourceRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final double efficiency;
		
		FissionSourceRegistrationInfo(Block block, double efficiency) {
			super(block);
			this.efficiency = efficiency;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, TextFormatting.LIGHT_PURPLE, NCInfo.neutronSourceEfficiencyInfo(efficiency), TextFormatting.AQUA, NCInfo.neutronSourceDescriptionInfo());
		}
	}
	
	public static class FissionShieldRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final double heatPerFlux, efficiency;
		
		FissionShieldRegistrationInfo(Block block, double heatPerFlux, double efficiency) {
			super(block);
			this.heatPerFlux = heatPerFlux;
			this.efficiency = efficiency;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, new TextFormatting[] {TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE}, NCInfo.neutronShieldStatInfo(heatPerFlux, efficiency), TextFormatting.AQUA, NCInfo.neutronShieldDescriptionInfo());
		}
	}
	
	public static class TurbineCoilRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final String coilID, rule;
		protected final double conductivity;
		
		TurbineCoilRegistrationInfo(Block block, String coilID, double conductivity, String rule) {
			super(block);
			this.coilID = coilID;
			this.conductivity = conductivity;
			this.rule = rule;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, TextFormatting.LIGHT_PURPLE, NCInfo.coilConductivityInfo(conductivity), TextFormatting.AQUA, InfoHelper.NULL_ARRAY);
		}
		
		@Override
		public void init() {
			super.init();
			TurbinePlacement.addRule(coilID + "_coil", rule, block);
		}
	}
	
	public static class TurbineBladeRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final double efficiency, expansionCoefficient;
		
		TurbineBladeRegistrationInfo(Block block, double efficiency, double expansionCoefficient) {
			super(block);
			this.efficiency = efficiency;
			this.expansionCoefficient = expansionCoefficient;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, new TextFormatting[] {TextFormatting.LIGHT_PURPLE, TextFormatting.GRAY}, new String[] {Lang.localise(NCBlocks.fixedLine("turbine_rotor_blade_efficiency"), NCMath.pcDecimalPlaces(efficiency, 1)), Lang.localise(NCBlocks.fixedLine("turbine_rotor_blade_expansion"), NCMath.pcDecimalPlaces(expansionCoefficient, 1))}, TextFormatting.AQUA, InfoHelper.formattedInfo(NCBlocks.infoLine("turbine_rotor_blade"), UnitHelper.prefix(turbine_mb_per_blade, 5, "B/t", -1)));
		}
	}
	
	public static class TurbineStatorRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final double expansionCoefficient;
		
		TurbineStatorRegistrationInfo(Block block, double expansionCoefficient) {
			super(block);
			this.expansionCoefficient = expansionCoefficient;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, TextFormatting.GRAY, new String[] {Lang.localise(NCBlocks.fixedLine("turbine_rotor_stator_expansion"), NCMath.pcDecimalPlaces(expansionCoefficient, 1))}, TextFormatting.AQUA, InfoHelper.formattedInfo(NCBlocks.infoLine("turbine_rotor_stator")));
		}
	}
	
	public static class RTGRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final long power;
		
		RTGRegistrationInfo(Block block, long power) {
			super(block);
			this.power = power;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, InfoHelper.formattedInfo(NCBlocks.infoLine("rtg"), UnitHelper.prefix(power, 5, "RF/t")));
		}
	}
	
	public static class BatteryRegistrationInfo extends TileBlockRegistrationInfo {
		
		protected final long capacity;
		protected final int energyTier;
		
		BatteryRegistrationInfo(Block block, long capacity, int energyTier) {
			super(block);
			this.capacity = capacity;
			this.energyTier = energyTier;
		}
		
		@Override
		public void registerBlock() {
			NCBlocks.registerBlock(block, new ItemBlockBattery(block, capacity, NCMath.toInt(capacity), energyTier, InfoHelper.formattedInfo(NCBlocks.infoLine("energy_storage"))));
		}
	}
	
	public static class ItemRegistrationInfo extends RegistrationInfo {
		
		protected final Item item;
		protected final CreativeTabs tab;
		
		public ItemRegistrationInfo(Item item, CreativeTabs tab) {
			this.item = item;
			this.tab = tab;
		}
		
		@Override
		public void preInit() {
			registerItem();
			
			if (CraftTweakerPlatformUtils.isClient()) {
				registerRender();
			}
		}
		
		public void registerItem() {
			NCItems.registerItem(item, tab);
		}
		
		public void registerRender() {
			NCItems.registerRender(item);
		}
		
		@Override
		public void recipeInit() {}
		
		@Override
		public void init() {}
		
		@Override
		public void postInit() {}
	}
	
	public static class MetaItemRegistrationInfo extends RegistrationInfo {
		
		protected final String name;
		protected Item item = null;
		protected CreativeTabs tab;
		
		public final List<String> types = new ArrayList<>();
		public final List<String> models = new ArrayList<>();
		public final List<String> textures = new ArrayList<>();
		
		public MetaItemRegistrationInfo(String name, CreativeTabs tab) {
			this.name = name;
			this.tab = tab;
		}
		
		public void createModelJson() {
			StringBuilder builder = new StringBuilder();
			String s = IOHelper.NEW_LINE;
			
			builder.append("{" + s + "	\"forge_marker\": 1," + s + "	\"defaults\": {" + s + "		\"model\": \"builtin/generated\"," + s + "		\"transform\": \"forge:default-item\"" + s + "	}," + s + "	\"variants\": {" + s + "		\"type\": {" + s);
			
			for (int i = 0; i < types.size(); ++i) {
				builder.append("			\"" + types.get(i) + "\": {" + s);
				
				String model = models.get(i);
				if (model != null) {
					builder.append("				\"model\": \"" + model + "\"," + s);
				}
				
				builder.append("				\"textures\": {" + s + "					\"layer0\": \"nuclearcraft:items/" + name + "/" + textures.get(i) + "\"" + s + "				}" + s + "			" + (i < types.size() - 1 ? "}," : "}") + s);
			}
			
			builder.append("		}" + s + "	}" + s + "}" + s);
			
			try {
				FileUtils.writeStringToFile(new File("resources/nuclearcraft/blockstates/items/" + name + ".json"), builder.toString());
			}
			catch (IOException e) {
				NCUtil.getLogger().catching(e);
			}
		}
		
		@Override
		public void preInit() {
			if (types.isEmpty()) {
				return;
			}
			
			createModelJson();
			
			item = NCItems.withName(new NCItemMetaArray(types), name);
			
			registerItem();
			
			if (CraftTweakerPlatformUtils.isClient()) {
				registerRender();
			}
		}
		
		public void registerItem() {
			NCItems.registerItem(item, tab);
		}
		
		public void registerRender() {
			for (int i = 0; i < types.size(); ++i) {
				NCItems.registerRender(item, i, types.get(i));
			}
		}
		
		@Override
		public void recipeInit() {}
		
		@Override
		public void init() {}
		
		@Override
		public void postInit() {}
	}
	
	public static class FissionIsotopeRegistrationInfo extends MetaItemRegistrationInfo {
		
		public final List<String> rawNames = new ArrayList<>();
		public final List<String> rawOres = new ArrayList<>();
		public final List<String> rawFluids = new ArrayList<>();
		public final IntList rawFluidColors = new IntArrayList();
		public final List<String> ores = new ArrayList<>();
		
		public FissionIsotopeRegistrationInfo(String name) {
			super(name, NCTabs.material());
		}
		
		@Override
		public void preInit() {
			super.preInit();
			
			if (item != null) {
				for (int i = 0; i < types.size(); ++i) {
					OreDictHandler.registerOre(item, i, ores.get(i));
				}
			}
			
			for (int i = 0; i < rawFluids.size(); ++i) {
				String rawFluid = rawFluids.get(i);
				if (rawFluid != null) {
					NCFissionFluids.addIsotopeFluids(rawFluid, rawFluidColors.getInt(i));
				}
			}
		}
		
		@Override
		public void recipeInit() {
			if (item == null) {
				return;
			}
			
			for (int i = 0; i < rawOres.size(); ++i) {
				String rawOre = rawOres.get(i), rawFluid = rawFluids.get(i);
				
				if (rawOre != null) {
					for (ItemStack output : OreDictionary.getOres("ingot" + rawOre, false)) {
						for (ItemStack input : OreDictionary.getOres("ingot" + rawOre + "Oxide", false)) {
							GameRegistry.addSmelting(input, output, 0F);
						}
						for (ItemStack input : OreDictionary.getOres("ingot" + rawOre + "Nitride", false)) {
							GameRegistry.addSmelting(input, output, 0F);
						}
					}
					
					NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes(rawOre, 1, "Zirconium", 1, rawOre + "ZA", 1, 1D, 1D);
					NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes(rawOre, 1, "Graphite", 1, rawOre + "Carbide", 1, 1D, 1D);
					NCRecipes.infuser.addRecipe("ingot" + rawOre, AbstractRecipeHandler.fluidStack("oxygen", BUCKET_VOLUME), "ingot" + rawOre + "Oxide", 1D, 1D);
					NCRecipes.infuser.addRecipe("ingot" + rawOre, AbstractRecipeHandler.fluidStack("nitrogen", BUCKET_VOLUME), "ingot" + rawOre + "Nitride", 1D, 1D);
					NCRecipes.separator.addRecipe("ingot" + rawOre + "ZA", "ingot" + rawOre, "dustZirconium", 1D, 1D);
					NCRecipes.separator.addRecipe("ingot" + rawOre + "Carbide", "ingot" + rawOre, "dustGraphite", 1D, 1D);
				}
				
				if (rawOre != null && rawFluid != null) {
					NCRecipes.ingot_former.addRecipe(AbstractRecipeHandler.fluidStack(rawFluid, INGOT_VOLUME), "ingot" + rawOre, 1D, 1D);
					NCRecipes.melter.addRecipe("ingot" + rawOre, AbstractRecipeHandler.fluidStack(rawFluid, INGOT_VOLUME), 1D, 1D);
				}
			}
		}
	}
	
	public static final double TRISO_TIME_MULT = 0.9D;
	public static final double TRISO_HEAT_MULT = 1D / 0.9D;
	public static final double TRISO_CRIT_MULT = 0.9D;
	
	public static final double[] SFR_TIME_MULT = new double[] {1D, 1.25D, 0.8D};
	public static final double[] SFR_HEAT_MULT = new double[] {1D, 0.8D, 1.25D};
	public static final double[] SFR_CRIT_MULT = new double[] {1D, 1.25D, 0.85D};
	
	public static final double MSR_TIME_MULT = 1.25D;
	public static final double MSR_HEAT_MULT = 0.8D;
	public static final double MSR_CRIT_MULT = 1D;
	
	public static class FissionFuelRegistrationInfo extends MetaItemRegistrationInfo {
		
		public final List<String> rawNames = new ArrayList<>();
		public final List<String> rawOres = new ArrayList<>();
		public final List<String> rawFluids = new ArrayList<>();
		public final IntList rawFluidColors = new IntArrayList();
		public final IntList rawDepletedFluidColors = new IntArrayList();
		public final List<FissionFuelStats> fissionStats = new ArrayList<>();
		public final List<String> ores = new ArrayList<>();
		
		public FissionFuelRegistrationInfo(String name) {
			super(name, NCTabs.material());
		}
		
		@Override
		public void preInit() {
			super.preInit();
			
			if (item != null) {
				for (int i = 0; i < types.size(); ++i) {
					OreDictHandler.registerOre(item, i, ores.get(i));
				}
			}
			
			for (int i = 0; i < rawFluids.size(); ++i) {
				String rawFluid = rawFluids.get(i);
				if (rawFluid != null) {
					NCFissionFluids.addFuelFluids(rawFluid, rawFluidColors.getInt(i));
					NCFissionFluids.addFuelFluids("depleted_" + rawFluid, rawDepletedFluidColors.getInt(i));
				}
			}
		}
		
		@Override
		public void recipeInit() {
			if (item == null) {
				return;
			}
			
			for (int i = 0; i < rawOres.size(); ++i) {
				String rawOre = rawOres.get(i), rawFluid = rawFluids.get(i);
				FissionFuelStats stats = fissionStats.get(i);
				
				if (rawOre != null) {
					for (ItemStack output : OreDictionary.getOres("ingot" + rawOre, false)) {
						for (ItemStack input : OreDictionary.getOres("ingot" + rawOre + "Oxide", false)) {
							GameRegistry.addSmelting(input, output, 0F);
						}
						for (ItemStack input : OreDictionary.getOres("ingot" + rawOre + "Nitride", false)) {
							GameRegistry.addSmelting(input, output, 0F);
						}
					}
					
					NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes(rawOre, 1, "Zirconium", 1, rawOre + "ZA", 1, 1D, 1D);
					NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes(rawOre, 1, "Graphite", 1, rawOre + "Carbide", 1, 1D, 1D);
					NCRecipes.infuser.addRecipe("ingot" + rawOre, AbstractRecipeHandler.fluidStack("oxygen", BUCKET_VOLUME), "ingot" + rawOre + "Oxide", 1D, 1D);
					NCRecipes.infuser.addRecipe("ingot" + rawOre, AbstractRecipeHandler.fluidStack("nitrogen", BUCKET_VOLUME), "ingot" + rawOre + "Nitride", 1D, 1D);
					NCRecipes.separator.addRecipe("ingot" + rawOre + "ZA", "ingot" + rawOre, "dustZirconium", 1D, 1D);
					NCRecipes.separator.addRecipe("ingot" + rawOre + "Carbide", "ingot" + rawOre, "dustGraphite", 1D, 1D);
					NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("ingot" + rawOre + "Carbide", 9), "dustGraphite", "ingotPyrolyticCarbon", "ingotSiliconCarbide", AbstractRecipeHandler.oreStack("ingot" + rawOre + "TRISO", 9), 1D, 1D);
					
					NCRecipes.pebble_fission.addRecipe("ingot" + rawOre + "TRISO", "ingotDepleted" + rawOre + "TRISO", (int) (TRISO_TIME_MULT * stats.time), (int) (TRISO_HEAT_MULT * stats.heat), stats.efficiency, (int) (TRISO_CRIT_MULT * stats.crit), stats.decay, stats.prime, stats.radiation);
					NCRecipes.solid_fission.addRecipe("ingot" + rawOre + "Oxide", "ingotDepleted" + rawOre + "Oxide", (int) (SFR_TIME_MULT[0] * stats.time), (int) (SFR_HEAT_MULT[0] * stats.heat), stats.efficiency, (int) (SFR_CRIT_MULT[0] * stats.crit), stats.decay, stats.prime, stats.radiation);
					NCRecipes.solid_fission.addRecipe("ingot" + rawOre + "Nitride", "ingotDepleted" + rawOre + "Nitride", (int) (SFR_TIME_MULT[1] * stats.time), (int) (SFR_HEAT_MULT[1] * stats.heat), stats.efficiency, (int) (SFR_CRIT_MULT[1] * stats.crit), stats.decay, stats.prime, stats.radiation);
					NCRecipes.solid_fission.addRecipe("ingot" + rawOre + "ZA", "ingotDepleted" + rawOre + "ZA", (int) (SFR_TIME_MULT[2] * stats.time), (int) (SFR_HEAT_MULT[2] * stats.heat), stats.efficiency, (int) (SFR_CRIT_MULT[2] * stats.crit), stats.decay, stats.prime, stats.radiation);
				}
				
				if (rawFluid != null) {
					EmptyFluidIngredient empty = AbstractRecipeHandler.emptyFluidStack();
					NCRecipes.chemical_reactor.addRecipe(AbstractRecipeHandler.fluidStack(rawFluid, INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("fluorine", BUCKET_VOLUME / 2), AbstractRecipeHandler.fluidStack(rawFluid + "_fluoride", INGOT_VOLUME / 2), empty, 0.5D, 0.5D);
					NCRecipes.electrolyzer.addRecipe(AbstractRecipeHandler.fluidStack(rawFluid + "_fluoride", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack(rawFluid, INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("fluorine", BUCKET_VOLUME / 2), empty, empty, 0.5D, 1D);
					NCRecipes.electrolyzer.addRecipe(AbstractRecipeHandler.fluidStack("depleted_" + rawFluid + "_fluoride", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("depleted_" + rawFluid, INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("fluorine", BUCKET_VOLUME / 2), empty, empty, 0.5D, 1D);
					NCRecipes.salt_mixer.addRecipe(AbstractRecipeHandler.fluidStack(rawFluid + "_fluoride", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("flibe", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack(rawFluid + "_fluoride_flibe", INGOT_VOLUME / 2), 0.5D, 1D);
					NCRecipes.centrifuge.addRecipe(AbstractRecipeHandler.fluidStack(rawFluid + "_fluoride_flibe", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack(rawFluid + "_fluoride", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("flibe", INGOT_VOLUME / 2), empty, empty, empty, empty, 0.5D, 1D);
					NCRecipes.centrifuge.addRecipe(AbstractRecipeHandler.fluidStack("depleted_" + rawFluid + "_fluoride_flibe", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("depleted_" + rawFluid + "_fluoride", INGOT_VOLUME / 2), AbstractRecipeHandler.fluidStack("flibe", INGOT_VOLUME / 2), empty, empty, empty, empty, 0.5D, 1D);
					
					NCRecipes.salt_fission.addRecipe(AbstractRecipeHandler.fluidStack(rawFluid + "_fluoride_flibe", 1), AbstractRecipeHandler.fluidStack("depleted_" + rawFluid + "_fluoride_flibe", 1), MSR_TIME_MULT * stats.time / INGOT_VOLUME, (int) (MSR_HEAT_MULT * stats.heat), stats.efficiency, MSR_CRIT_MULT * stats.crit, stats.decay, stats.prime, stats.radiation);
				}
				
				if (rawOre != null && rawFluid != null) {
					NCRecipes.ingot_former.addRecipe(AbstractRecipeHandler.fluidStack(rawFluid, INGOT_VOLUME), "ingot" + rawOre, 1D, 1D);
					NCRecipes.melter.addRecipe("ingot" + rawOre, AbstractRecipeHandler.fluidStack(rawFluid, INGOT_VOLUME), 1D, 1D);
				}
			}
		}
	}
}
