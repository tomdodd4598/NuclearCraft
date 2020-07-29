package nc.integration.crafttweaker;

import static nc.config.NCConfig.turbine_mb_per_blade;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.mc1120.util.CraftTweakerPlatformUtils;
import nc.Global;
import nc.NuclearCraft;
import nc.init.NCBlocks;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.block.BlockFissionPart;
import nc.multiblock.fission.block.port.BlockFissionFluidPort;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import nc.multiblock.turbine.TurbinePlacement;
import nc.multiblock.turbine.TurbineRotorBladeUtil;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IRotorBladeType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IRotorStatorType;
import nc.multiblock.turbine.block.BlockTurbinePart;
import nc.multiblock.turbine.block.BlockTurbineRotorBlade;
import nc.multiblock.turbine.block.BlockTurbineRotorStator;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.NCRecipes;
import nc.util.FluidStackHelper;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.nuclearcraft.Registration")
@ZenRegister
public class CTRegistration {
	
	public static final List<RegistrationInfo> INFO_LIST = new ArrayList<>();
	
	public static class TileSink extends TileSolidFissionSink {
		
		public TileSink(String sinkName, int coolingRate, String ruleID) {
			super(sinkName, coolingRate, ruleID);
		}
	}
	
	@ZenMethod
	public static void registerFissionSink(String sinkID, int cooling, String rule) {
		
		Block sink = NCBlocks.withName(
				new BlockFissionPart() {
					@Override
					public TileEntity createNewTileEntity(World world, int metadata) {
						return new TileSink(sinkID, cooling, sinkID + "_sink");
					}
					
					@Override
					public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
						if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) {
							return false;
						}
						return rightClickOnPart(world, pos, player, hand, facing);
					}
				},
				"solid_fission_sink_" + sinkID);
		
		INFO_LIST.add(new FissionSinkRegistrationInfo(sink, sinkID, cooling, rule));
	}
	
	public static class TileHeaterPort extends TileFissionHeaterPort {
		
		public TileHeaterPort(String coolantName) {
			super(coolantName);
		}
	}
	
	public static class TileHeater extends TileSaltFissionHeater {
		
		public TileHeater(String heaterName, String coolantName) {
			super(heaterName, coolantName);
		}
	}
	
	@ZenMethod
	public static void registerFissionHeater(String heaterID, String fluidInput, int inputAmount, String fluidOutput, int outputAmount, int cooling, String rule) {
		
		Block port = NCBlocks.withName(
				new BlockFissionFluidPort(TileHeaterPort.class, 303) {
					@Override
					public TileEntity createNewTileEntity(World world, int metadata) {
						return new TileHeaterPort(fluidInput);
					}
				},
				"fission_heater_port_" + heaterID);
		
		Block heater = NCBlocks.withName(
				new BlockFissionPart() {
					@Override
					public TileEntity createNewTileEntity(World world, int metadata) {
						return new TileHeater(heaterID, fluidInput);
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
				},
				"salt_fission_heater_" + heaterID);
		
		INFO_LIST.add(new FissionHeaterPortRegistrationInfo(port, heaterID));
		INFO_LIST.add(new FissionHeaterRegistrationInfo(heater, heaterID, fluidInput, inputAmount, fluidOutput, outputAmount, cooling, rule));
	}
	
	public static class TileCoil extends TileTurbineDynamoCoil {
		
		public TileCoil(String partName, double conductivity, String ruleID) {
			super(partName, conductivity, ruleID);
		}
	}
	
	@ZenMethod
	public static void registerTurbineCoil(String coilID, double conductivity, String rule) {
		
		Block coil = NCBlocks.withName(
				new BlockTurbinePart() {
					@Override
					public TileEntity createNewTileEntity(World world, int metadata) {
						return new TileCoil(coilID, conductivity, coilID + "_coil");
					}
					
					@Override
					public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
						if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) {
							return false;
						}
						return rightClickOnPart(world, pos, player, hand, facing);
					}
				},
				"turbine_dynamo_coil_" + coilID);
		
		INFO_LIST.add(new TurbineCoilRegistrationInfo(coil, coilID, conductivity, rule));
	}
	
	public static class TileBlade extends TileTurbineRotorBlade {
		
		final Block bladeBlock;
		
		public TileBlade(IRotorBladeType bladeType, Block bladeBlock) {
			super(bladeType);
			this.bladeBlock = bladeBlock;
		}
		
		@Override
		public IBlockState getRenderState() {
			return bladeBlock.getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
		}
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
		
		Block blade = NCBlocks.withName(
				new BlockTurbineRotorBlade(null) {
					@Override
					public TileEntity createNewTileEntity(World world, int metadata) {
						return new TileBlade(bladeType, this);
					}
				},
				"turbine_rotor_blade_" + bladeID);
		
		INFO_LIST.add(new TurbineBladeRegistrationInfo(blade, efficiency, expansionCoefficient));
	}
	
	public static class TileStator extends TileTurbineRotorStator {
		
		final Block bladeBlock;
		
		public TileStator(IRotorStatorType statorType, Block bladeBlock) {
			super(statorType);
			this.bladeBlock = bladeBlock;
		}
		
		@Override
		public IRotorBladeType getBladeType() {
			return statorType;
		}
		
		@Override
		public IBlockState getRenderState() {
			return bladeBlock.getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, dir);
		}
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
		
		Block stator = NCBlocks.withName(
				new BlockTurbineRotorStator() {
					@Override
					public TileEntity createNewTileEntity(World world, int metadata) {
						return new TileBlade(statorType, this);
					}
				},
				"turbine_rotor_stator_" + statorID);
		
		INFO_LIST.add(new TurbineStatorRegistrationInfo(stator, expansionCoefficient));
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
			NCBlocks.registerBlock(block, TextFormatting.LIGHT_PURPLE, new String[] {Lang.localise("tile." + Global.MOD_ID + ".turbine_dynamo_coil.conductivity") + " " + NCMath.decimalPlaces(100D * conductivity, 1) + "%"}, TextFormatting.AQUA, InfoHelper.NULL_ARRAY);
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
			NCBlocks.registerBlock(block, new TextFormatting[] {TextFormatting.LIGHT_PURPLE, TextFormatting.GRAY}, new String[] {Lang.localise(NCBlocks.fixedLine("turbine_rotor_blade_efficiency"), Math.round(100D * efficiency) + "%"), Lang.localise(NCBlocks.fixedLine("turbine_rotor_blade_expansion"), Math.round(100D * expansionCoefficient) + "%")}, TextFormatting.AQUA, InfoHelper.formattedInfo(NCBlocks.infoLine("turbine_rotor_blade"), UnitHelper.prefix(turbine_mb_per_blade, 5, "B/t", -1)));
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
			NCBlocks.registerBlock(block, TextFormatting.GRAY, new String[] {Lang.localise(NCBlocks.fixedLine("turbine_rotor_stator_expansion"), Math.round(100D * expansionCoefficient) + "%")}, TextFormatting.AQUA, InfoHelper.formattedInfo(NCBlocks.infoLine("turbine_rotor_stator")));
		}
	}
}
