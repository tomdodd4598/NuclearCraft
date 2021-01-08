package nc.integration.crafttweaker;

import static nc.config.NCConfig.turbine_mb_per_blade;

import java.util.*;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.mc1120.util.CraftTweakerPlatformUtils;
import nc.*;
import nc.init.NCBlocks;
import nc.multiblock.fission.*;
import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.block.port.BlockFissionFluidPort;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import nc.multiblock.turbine.TurbinePlacement;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import nc.multiblock.turbine.block.*;
import nc.multiblock.turbine.tile.*;
import nc.recipe.*;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.nuclearcraft.Registration")
@ZenRegister
public class CTRegistration {
	
	public static final List<RegistrationInfo> INFO_LIST = new ArrayList<>();
	
	@ZenMethod
	public static void registerFissionSink(String sinkID, int cooling, String rule) {
		
		Block sink = NCBlocks.withName(new BlockFissionPart() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileSolidFissionSink(sinkID, cooling, sinkID + "_sink");
			}
			
			@Override
			public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
				if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) {
					return false;
				}
				return rightClickOnPart(world, pos, player, hand, facing);
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
				return new TileFissionHeaterPort(fluidInput);
			}
		}, "fission_heater_port_" + heaterID);
		
		Block heater = NCBlocks.withName(new BlockFissionPart() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileSaltFissionHeater(heaterID, fluidInput);
			}
			
			@Override
			public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
				if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) {
					return false;
				}
				
				if (!world.isRemote) {
					TileEntity tile = world.getTileEntity(pos);
					if (tile instanceof TileSaltFissionHeater) {
						TileSaltFissionHeater heater = (TileSaltFissionHeater) tile;
						FissionReactor reactor = heater.getMultiblock();
						if (reactor != null) {
							FluidStack fluidStack = FluidStackHelper.getFluid(player.getHeldItem(hand));
							if (heater.canModifyFilter(0) && heater.getTanks().get(0).isEmpty() && fluidStack != null && !FluidStackHelper.stacksEqual(heater.getFilterTanks().get(0).getFluid(), fluidStack) && heater.getTanks().get(0).canFillFluidType(fluidStack)) {
								player.sendMessage(new TextComponentString(Lang.localise("message.nuclearcraft.filter") + " " + TextFormatting.BOLD + Lang.localise(fluidStack.getUnlocalizedName())));
								FluidStack filter = fluidStack.copy();
								filter.amount = 1000;
								heater.getFilterTanks().get(0).setFluid(filter);
								heater.onFilterChanged(0);
							}
							else {
								player.openGui(NuclearCraft.instance, 203, world, pos.getX(), pos.getY(), pos.getZ());
							}
							return true;
						}
					}
				}
				return rightClickOnPart(world, pos, player, hand, facing, true);
			}
		}, "salt_fission_heater_" + heaterID);
		
		INFO_LIST.add(new FissionHeaterPortRegistrationInfo(port, heaterID));
		INFO_LIST.add(new FissionHeaterRegistrationInfo(heater, heaterID, fluidInput, inputAmount, fluidOutput, outputAmount, cooling, rule));
		CraftTweakerAPI.logInfo("Registered fission coolant heater and a respective port with ID \"" + heaterID + "\", cooling rate " + cooling + " H/t, placement rule \"" + rule + "\" and recipe [" + inputAmount + " * " + fluidInput + " -> " + outputAmount + " * " + fluidOutput + "]");
	}
	
	@ZenMethod
	public static void registerTurbineCoil(String coilID, double conductivity, String rule) {
		
		Block coil = NCBlocks.withName(new BlockTurbinePart() {
			
			@Override
			public TileEntity createNewTileEntity(World world, int metadata) {
				return new TileTurbineDynamoCoil(coilID, conductivity, coilID + "_coil");
			}
			
			@Override
			public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
				if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) {
					return false;
				}
				return rightClickOnPart(world, pos, player, hand, facing);
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
			NCBlocks.registerBlock(block, TextFormatting.BLUE, new String[] {Lang.localise("tile." + Global.MOD_ID + ".solid_fission_sink.cooling_rate") + " " + cooling + " H/t"}, TextFormatting.AQUA, InfoHelper.NULL_ARRAY);
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
			NCBlocks.registerBlock(block, TextFormatting.BLUE, new String[] {Lang.localise("tile." + Global.MOD_ID + ".salt_fission_heater.cooling_rate") + " " + cooling + " H/t"}, TextFormatting.AQUA, InfoHelper.NULL_ARRAY);
		}
		
		@Override
		public void recipeInit() {
			NCRecipes.coolant_heater.addRecipe(AbstractRecipeHandler.fluidStack(fluidInput, inputAmount), AbstractRecipeHandler.fluidStack(fluidOutput, outputAmount), cooling, heaterID + "_heater");
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
			NCBlocks.registerBlock(block, TextFormatting.LIGHT_PURPLE, new String[] {Lang.localise("tile." + Global.MOD_ID + ".turbine_dynamo_coil.conductivity") + " " + NCMath.pcDecimalPlaces(conductivity, 1)}, TextFormatting.AQUA, InfoHelper.NULL_ARRAY);
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
}
