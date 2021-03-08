package nc.integration.crafttweaker;

import static nc.config.NCConfig.turbine_mb_per_blade;

import java.util.*;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.mc1120.util.CraftTweakerPlatformUtils;
import nc.NCInfo;
import nc.block.item.energy.ItemBlockBattery;
import nc.init.NCBlocks;
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
import nc.recipe.*;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.nuclearcraft.Registration")
@ZenRegister
public class CTRegistration {
	
	public static final List<RegistrationInfo> INFO_LIST = new ArrayList<>();
	
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
		
		Block port = NCBlocks.withName(new BlockFissionFluidPort(TileFissionHeaterPort.class, 303) {
			
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
	
	// Registration Wrapper
	
	public abstract static class RegistrationInfo {
		
		public abstract void preInit();
		
		public void recipeInit() {}
		
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
}
