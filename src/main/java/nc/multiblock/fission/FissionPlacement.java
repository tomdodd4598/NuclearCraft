package nc.multiblock.fission;

import it.unimi.dsi.fastutil.objects.*;
import nc.init.NCBlocks;
import nc.multiblock.PlacementRule;
import nc.multiblock.PlacementRule.*;
import nc.recipe.*;
import nc.tile.fission.*;
import nc.util.StringHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

import static nc.config.NCConfig.*;

public abstract class FissionPlacement {
	
	/**
	 * List of all defined rule parsers. Earlier entries are prioritised!
	 */
	public static final List<PlacementRule.RuleParser<FissionReactor, IFissionPart>> RULE_PARSER_LIST = new LinkedList<>();
	
	/**
	 * Map of all placement rule IDs to unparsed rule strings, used for ordered iterations.
	 */
	public static final Object2ObjectMap<String, String> RULE_MAP_RAW = new Object2ObjectArrayMap<>();
	
	/**
	 * Map of all defined placement rules.
	 */
	public static final Object2ObjectMap<String, PlacementRule<FissionReactor, IFissionPart>> RULE_MAP = new PlacementMap<>();
	
	/**
	 * List of all defined tooltip builders. Earlier entries are prioritised!
	 */
	public static final List<PlacementRule.TooltipBuilder<FissionReactor, IFissionPart>> TOOLTIP_BUILDER_LIST = new LinkedList<>();
	
	public static PlacementRule.RecipeHandler recipe_handler;
	
	/**
	 * Map of all localized tooltips.
	 */
	public static final Object2ObjectMap<String, String> TOOLTIP_MAP = new Object2ObjectOpenHashMap<>();
	
	public static void preInit() {
		RULE_PARSER_LIST.add(new DefaultRuleParser());
		
		TOOLTIP_BUILDER_LIST.add(new DefaultTooltipBuilder());
	}
	
	public static void init() {
		recipe_handler = new RecipeHandler();
		
		RULE_MAP.put("", new PlacementRule.Or<>(new ArrayList<>()));
		
		addRule("oxygen_cooler", fission_cooler_rule[0], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 0));
		addRule("hydrogen_cooler", fission_cooler_rule[1], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 1));
		addRule("helium_cooler", fission_cooler_rule[2], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 2));
		addRule("nitrogen_cooler", fission_cooler_rule[3], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 3));
		addRule("fluorine_cooler", fission_cooler_rule[4], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 4));
		addRule("methane_cooler", fission_cooler_rule[5], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 5));
		addRule("carbon_dioxide_cooler", fission_cooler_rule[6], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 6));
		addRule("carbon_monoxide_cooler", fission_cooler_rule[7], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 7));
		addRule("ethene_cooler", fission_cooler_rule[8], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 8));
		addRule("ethyne_cooler", fission_cooler_rule[9], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 9));
		addRule("fluoromethane_cooler", fission_cooler_rule[10], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 10));
		addRule("ammonia_cooler", fission_cooler_rule[11], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 11));
		addRule("diborane_cooler", fission_cooler_rule[12], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 12));
		addRule("sulfur_dioxide_cooler", fission_cooler_rule[13], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 13));
		addRule("sulfur_trioxide_cooler", fission_cooler_rule[14], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 14));
		addRule("sulfur_hexafluoride_cooler", fission_cooler_rule[15], new ItemStack(NCBlocks.pebble_fission_cooler, 1, 15));
		
		addRule("water_sink", fission_sink_rule[0], new ItemStack(NCBlocks.solid_fission_sink, 1, 0));
		addRule("iron_sink", fission_sink_rule[1], new ItemStack(NCBlocks.solid_fission_sink, 1, 1));
		addRule("redstone_sink", fission_sink_rule[2], new ItemStack(NCBlocks.solid_fission_sink, 1, 2));
		addRule("quartz_sink", fission_sink_rule[3], new ItemStack(NCBlocks.solid_fission_sink, 1, 3));
		addRule("obsidian_sink", fission_sink_rule[4], new ItemStack(NCBlocks.solid_fission_sink, 1, 4));
		addRule("nether_brick_sink", fission_sink_rule[5], new ItemStack(NCBlocks.solid_fission_sink, 1, 5));
		addRule("glowstone_sink", fission_sink_rule[6], new ItemStack(NCBlocks.solid_fission_sink, 1, 6));
		addRule("lapis_sink", fission_sink_rule[7], new ItemStack(NCBlocks.solid_fission_sink, 1, 7));
		addRule("gold_sink", fission_sink_rule[8], new ItemStack(NCBlocks.solid_fission_sink, 1, 8));
		addRule("prismarine_sink", fission_sink_rule[9], new ItemStack(NCBlocks.solid_fission_sink, 1, 9));
		addRule("slime_sink", fission_sink_rule[10], new ItemStack(NCBlocks.solid_fission_sink, 1, 10));
		addRule("end_stone_sink", fission_sink_rule[11], new ItemStack(NCBlocks.solid_fission_sink, 1, 11));
		addRule("purpur_sink", fission_sink_rule[12], new ItemStack(NCBlocks.solid_fission_sink, 1, 12));
		addRule("diamond_sink", fission_sink_rule[13], new ItemStack(NCBlocks.solid_fission_sink, 1, 13));
		addRule("emerald_sink", fission_sink_rule[14], new ItemStack(NCBlocks.solid_fission_sink, 1, 14));
		addRule("copper_sink", fission_sink_rule[15], new ItemStack(NCBlocks.solid_fission_sink, 1, 15));
		addRule("tin_sink", fission_sink_rule[16], new ItemStack(NCBlocks.solid_fission_sink2, 1, 0));
		addRule("lead_sink", fission_sink_rule[17], new ItemStack(NCBlocks.solid_fission_sink2, 1, 1));
		addRule("boron_sink", fission_sink_rule[18], new ItemStack(NCBlocks.solid_fission_sink2, 1, 2));
		addRule("lithium_sink", fission_sink_rule[19], new ItemStack(NCBlocks.solid_fission_sink2, 1, 3));
		addRule("magnesium_sink", fission_sink_rule[20], new ItemStack(NCBlocks.solid_fission_sink2, 1, 4));
		addRule("manganese_sink", fission_sink_rule[21], new ItemStack(NCBlocks.solid_fission_sink2, 1, 5));
		addRule("aluminum_sink", fission_sink_rule[22], new ItemStack(NCBlocks.solid_fission_sink2, 1, 6));
		addRule("silver_sink", fission_sink_rule[23], new ItemStack(NCBlocks.solid_fission_sink2, 1, 7));
		addRule("fluorite_sink", fission_sink_rule[24], new ItemStack(NCBlocks.solid_fission_sink2, 1, 8));
		addRule("villiaumite_sink", fission_sink_rule[25], new ItemStack(NCBlocks.solid_fission_sink2, 1, 9));
		addRule("carobbiite_sink", fission_sink_rule[26], new ItemStack(NCBlocks.solid_fission_sink2, 1, 10));
		addRule("arsenic_sink", fission_sink_rule[27], new ItemStack(NCBlocks.solid_fission_sink2, 1, 11));
		addRule("liquid_nitrogen_sink", fission_sink_rule[28], new ItemStack(NCBlocks.solid_fission_sink2, 1, 12));
		addRule("liquid_helium_sink", fission_sink_rule[29], new ItemStack(NCBlocks.solid_fission_sink2, 1, 13));
		addRule("enderium_sink", fission_sink_rule[30], new ItemStack(NCBlocks.solid_fission_sink2, 1, 14));
		addRule("cryotheum_sink", fission_sink_rule[31], new ItemStack(NCBlocks.solid_fission_sink2, 1, 15));
		
		addRule("standard_heater", fission_heater_rule[0], new ItemStack(NCBlocks.salt_fission_heater, 1, 0));
		addRule("iron_heater", fission_heater_rule[1], new ItemStack(NCBlocks.salt_fission_heater, 1, 1));
		addRule("redstone_heater", fission_heater_rule[2], new ItemStack(NCBlocks.salt_fission_heater, 1, 2));
		addRule("quartz_heater", fission_heater_rule[3], new ItemStack(NCBlocks.salt_fission_heater, 1, 3));
		addRule("obsidian_heater", fission_heater_rule[4], new ItemStack(NCBlocks.salt_fission_heater, 1, 4));
		addRule("nether_brick_heater", fission_heater_rule[5], new ItemStack(NCBlocks.salt_fission_heater, 1, 5));
		addRule("glowstone_heater", fission_heater_rule[6], new ItemStack(NCBlocks.salt_fission_heater, 1, 6));
		addRule("lapis_heater", fission_heater_rule[7], new ItemStack(NCBlocks.salt_fission_heater, 1, 7));
		addRule("gold_heater", fission_heater_rule[8], new ItemStack(NCBlocks.salt_fission_heater, 1, 8));
		addRule("prismarine_heater", fission_heater_rule[9], new ItemStack(NCBlocks.salt_fission_heater, 1, 9));
		addRule("slime_heater", fission_heater_rule[10], new ItemStack(NCBlocks.salt_fission_heater, 1, 10));
		addRule("end_stone_heater", fission_heater_rule[11], new ItemStack(NCBlocks.salt_fission_heater, 1, 11));
		addRule("purpur_heater", fission_heater_rule[12], new ItemStack(NCBlocks.salt_fission_heater, 1, 12));
		addRule("diamond_heater", fission_heater_rule[13], new ItemStack(NCBlocks.salt_fission_heater, 1, 13));
		addRule("emerald_heater", fission_heater_rule[14], new ItemStack(NCBlocks.salt_fission_heater, 1, 14));
		addRule("copper_heater", fission_heater_rule[15], new ItemStack(NCBlocks.salt_fission_heater, 1, 15));
		addRule("tin_heater", fission_heater_rule[16], new ItemStack(NCBlocks.salt_fission_heater2, 1, 0));
		addRule("lead_heater", fission_heater_rule[17], new ItemStack(NCBlocks.salt_fission_heater2, 1, 1));
		addRule("boron_heater", fission_heater_rule[18], new ItemStack(NCBlocks.salt_fission_heater2, 1, 2));
		addRule("lithium_heater", fission_heater_rule[19], new ItemStack(NCBlocks.salt_fission_heater2, 1, 3));
		addRule("magnesium_heater", fission_heater_rule[20], new ItemStack(NCBlocks.salt_fission_heater2, 1, 4));
		addRule("manganese_heater", fission_heater_rule[21], new ItemStack(NCBlocks.salt_fission_heater2, 1, 5));
		addRule("aluminum_heater", fission_heater_rule[22], new ItemStack(NCBlocks.salt_fission_heater2, 1, 6));
		addRule("silver_heater", fission_heater_rule[23], new ItemStack(NCBlocks.salt_fission_heater2, 1, 7));
		addRule("fluorite_heater", fission_heater_rule[24], new ItemStack(NCBlocks.salt_fission_heater2, 1, 8));
		addRule("villiaumite_heater", fission_heater_rule[25], new ItemStack(NCBlocks.salt_fission_heater2, 1, 9));
		addRule("carobbiite_heater", fission_heater_rule[26], new ItemStack(NCBlocks.salt_fission_heater2, 1, 10));
		addRule("arsenic_heater", fission_heater_rule[27], new ItemStack(NCBlocks.salt_fission_heater2, 1, 11));
		addRule("liquid_nitrogen_heater", fission_heater_rule[28], new ItemStack(NCBlocks.salt_fission_heater2, 1, 12));
		addRule("liquid_helium_heater", fission_heater_rule[29], new ItemStack(NCBlocks.salt_fission_heater2, 1, 13));
		addRule("enderium_heater", fission_heater_rule[30], new ItemStack(NCBlocks.salt_fission_heater2, 1, 14));
		addRule("cryotheum_heater", fission_heater_rule[31], new ItemStack(NCBlocks.salt_fission_heater2, 1, 15));
	}
	
	public static void addRule(String id, String rule, Object... blocks) {
		RULE_MAP_RAW.put(id, rule);
		RULE_MAP.put(id, parse(rule));
		for (Object block : blocks) {
			recipe_handler.addRecipe(block, id);
		}
	}
	
	public static void postInit() {
		for (Object2ObjectMap.Entry<String, PlacementRule<FissionReactor, IFissionPart>> entry : RULE_MAP.object2ObjectEntrySet()) {
			for (PlacementRule.TooltipBuilder<FissionReactor, IFissionPart> builder : TOOLTIP_BUILDER_LIST) {
				String tooltip = builder.buildTooltip(entry.getValue());
				if (tooltip != null) {
					TOOLTIP_MAP.put(entry.getKey(), tooltip);
				}
			}
		}
	}
	
	// Default Rule Parser
	
	public static PlacementRule<FissionReactor, IFissionPart> parse(String string) {
		return PlacementRule.parse(string, RULE_PARSER_LIST);
	}
	
	/**
	 * Rule parser for all rule types available in base NC.
	 */
	public static class DefaultRuleParser extends PlacementRule.DefaultRuleParser<FissionReactor, IFissionPart> {
		
		@Override
		protected @Nullable PlacementRule<FissionReactor, IFissionPart> partialParse(String s) {
			s = s.toLowerCase(Locale.ROOT);
			
			s = s.replaceAll("at exactly one vertex", "vertex");
			
			boolean exact = s.contains("exact"), atMost = s.contains("at most");
			boolean axial = s.contains("axial"), vertex = s.contains("vertex"), edge = s.contains("edge");
			
			if ((exact && atMost) || (axial && vertex)) {
				return null;
			}
			
			s = s.replaceAll("at least", "");
			s = s.replaceAll("exactly", "");
			s = s.replaceAll("exact", "");
			s = s.replaceAll("at most", "");
			s = s.replaceAll("axially", "");
			s = s.replaceAll("axial", "");
			s = s.replaceAll("at one vertex", "");
			s = s.replaceAll("at a vertex", "");
			s = s.replaceAll("at vertex", "");
			s = s.replaceAll("vertex", "");
			s = s.replaceAll("at one edge", "");
			s = s.replaceAll("at an edge", "");
			s = s.replaceAll("at edge", "");
			s = s.replaceAll("along one edge", "");
			s = s.replaceAll("along an edge", "");
			s = s.replaceAll("along edge", "");
			s = s.replaceAll("edge", "");
			
			int amount = -1;
			String rule = null, type = null;
			
			String[] split = s.split(Pattern.quote(" "));
			for (int i = 0; i < split.length; ++i) {
				if (StringHelper.NUMBER_S2I_MAP.containsKey(split[i])) {
					amount = StringHelper.NUMBER_S2I_MAP.getInt(split[i]);
				}
				else if (rule == null) {
					if (split[i].contains("wall") || split[i].contains("casing")) {
						rule = "casing";
					}
					else if (split[i].contains("conductor")) {
						rule = "conductor";
					}
					else if (split[i].contains("moderator")) {
						rule = "moderator";
					}
					else if (split[i].contains("reflector")) {
						rule = "reflector";
					}
					else if (split[i].contains("irradiator")) {
						rule = "irradiator";
					}
					else if (split[i].contains("shield")) {
						rule = "shield";
					}
					else if (split[i].contains("chamber")) {
						rule = "chamber";
					}
					else if (split[i].contains("cooler")) {
						rule = "cooler";
						if (i > 0) {
							type = split[i - 1];
						}
						else {
							return null;
						}
					}
					else if (split[i].contains("cell")) {
						rule = "cell";
					}
					else if (split[i].contains("sink")) {
						rule = "sink";
						if (i > 0) {
							type = split[i - 1];
						}
						else {
							return null;
						}
					}
					else if (split[i].contains("vessel")) {
						rule = "vessel";
					}
					else if (split[i].contains("heater")) {
						rule = "heater";
						if (i > 0) {
							type = split[i - 1];
						}
						else {
							return null;
						}
					}
				}
			}
			
			if (amount < 0 || rule == null) {
				return null;
			}
			
			CountType countType = exact ? CountType.EXACTLY : (atMost ? CountType.AT_MOST : CountType.AT_LEAST);
			AdjacencyType adjType = axial ? AdjacencyType.AXIAL : (vertex ? AdjacencyType.VERTEX : (edge ? AdjacencyType.EDGE : AdjacencyType.STANDARD));
			
			return switch (rule) {
				case "casing" -> new AdjacentCasing(amount, countType, adjType);
				case "conductor" -> new AdjacentConductor(amount, countType, adjType);
				case "moderator" -> new AdjacentModerator(amount, countType, adjType);
				case "reflector" -> new AdjacentReflector(amount, countType, adjType);
				case "irradiator" -> new AdjacentIrradiator(amount, countType, adjType);
				case "shield" -> new AdjacentShield(amount, countType, adjType);
				case "chamber" -> new AdjacentChamber(amount, countType, adjType);
				case "cooler" -> new AdjacentCooler(amount, countType, adjType, type);
				case "cell" -> new AdjacentCell(amount, countType, adjType);
				case "sink" -> new AdjacentSink(amount, countType, adjType, type);
				case "vessel" -> new AdjacentVessel(amount, countType, adjType);
				case "heater" -> new AdjacentHeater(amount, countType, adjType, type);
				default -> null;
			};
			
		}
	}
	
	// Adjacent
	
	public static abstract class Adjacent extends PlacementRule.Adjacent<FissionReactor, IFissionPart> {
		
		public Adjacent(String dependency, int amount, CountType countType, AdjacencyType adjType) {
			super(dependency, amount, countType, adjType);
		}
	}
	
	public static class AdjacentCasing extends Adjacent {
		
		public AdjacentCasing(int amount, CountType countType, AdjacencyType adjType) {
			super("reactor_casing", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isCasing(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentConductor extends Adjacent {
		
		public AdjacentConductor(int amount, CountType countType, AdjacencyType adjType) {
			super("conductor", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isConductor(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentModerator extends Adjacent {
		
		public AdjacentModerator(int amount, CountType countType, AdjacencyType adjType) {
			super("moderator", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isActiveModerator(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentReflector extends Adjacent {
		
		public AdjacentReflector(int amount, CountType countType, AdjacencyType adjType) {
			super("reflector", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isActiveReflector(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentIrradiator extends Adjacent {
		
		public AdjacentIrradiator(int amount, CountType countType, AdjacencyType adjType) {
			super("irradiator", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isFunctionalIrradiator(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentShield extends Adjacent {
		
		public AdjacentShield(int amount, CountType countType, AdjacencyType adjType) {
			super("shield", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isFunctionalShield(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentChamber extends Adjacent {
		
		public AdjacentChamber(int amount, CountType countType, AdjacencyType adjType) {
			super("chamber", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isFunctionalChamber(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentCooler extends Adjacent {
		
		public final String coolerType;
		
		public AdjacentCooler(int amount, CountType countType, AdjacencyType adjType, String coolerType) {
			super(coolerType + "_cooler", amount, countType, adjType);
			this.coolerType = coolerType;
		}
		
		@Override
		public void checkIsRuleAllowed(String ruleID) {
			super.checkIsRuleAllowed(ruleID);
			if (countType != CountType.AT_LEAST && coolerType.equals("any")) {
				throw new IllegalArgumentException((countType == CountType.EXACTLY ? "Exact 'any cooler'" : "'At most n of any cooler'") + " placement rule with ID \"" + ruleID + "\" is disallowed due to potential ambiguity during rule checks!");
			}
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isValidCooler(part.getMultiblock(), part.getTilePos().offset(dir), coolerType, simulate);
		}
	}
	
	public static class AdjacentCell extends Adjacent {
		
		public AdjacentCell(int amount, CountType countType, AdjacencyType adjType) {
			super("cell", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isFunctionalCell(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentSink extends Adjacent {
		
		public final String sinkType;
		
		public AdjacentSink(int amount, CountType countType, AdjacencyType adjType, String sinkType) {
			super(sinkType + "_sink", amount, countType, adjType);
			this.sinkType = sinkType;
		}
		
		@Override
		public void checkIsRuleAllowed(String ruleID) {
			super.checkIsRuleAllowed(ruleID);
			if (countType != CountType.AT_LEAST && sinkType.equals("any")) {
				throw new IllegalArgumentException((countType == CountType.EXACTLY ? "Exact 'any sink'" : "'At most n of any sink'") + " placement rule with ID \"" + ruleID + "\" is disallowed due to potential ambiguity during rule checks!");
			}
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isValidSink(part.getMultiblock(), part.getTilePos().offset(dir), sinkType, simulate);
		}
	}
	
	public static class AdjacentVessel extends Adjacent {
		
		public AdjacentVessel(int amount, CountType countType, AdjacencyType adjType) {
			super("vessel", amount, countType, adjType);
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isFunctionalVessel(part.getMultiblock(), part.getTilePos().offset(dir), simulate);
		}
	}
	
	public static class AdjacentHeater extends Adjacent {
		
		public final String heaterType;
		
		public AdjacentHeater(int amount, CountType countType, AdjacencyType adjType, String heaterType) {
			super(heaterType + "_heater", amount, countType, adjType);
			this.heaterType = heaterType;
		}
		
		@Override
		public void checkIsRuleAllowed(String ruleID) {
			super.checkIsRuleAllowed(ruleID);
			if (countType != CountType.AT_LEAST && heaterType.equals("any")) {
				throw new IllegalArgumentException((countType == CountType.EXACTLY ? "Exact 'any heater'" : "'At most n of any heater'") + " placement rule with ID \"" + ruleID + "\" is disallowed due to potential ambiguity during rule checks!");
			}
		}
		
		@Override
		public boolean satisfied(IFissionPart part, EnumFacing dir, boolean simulate) {
			return isValidHeater(part.getMultiblock(), part.getTilePos().offset(dir), heaterType, simulate);
		}
	}
	
	// Helper Methods
	
	public static boolean isCasing(FissionReactor reactor, BlockPos pos, boolean simulate) {
		TileEntity tile = reactor.WORLD.getTileEntity(pos);
		return tile instanceof TileFissionPart part && part.getPartPositionType().isGoodForWall();
	}
	
	public static boolean isConductor(FissionReactor reactor, BlockPos pos, boolean simulate) {
		return reactor.getPartMap(TileFissionConductor.class).get(pos.toLong()) != null;
	}
	
	public static boolean isActiveModerator(FissionReactor reactor, BlockPos pos, boolean simulate) {
		IFissionComponent component = reactor.getPartMap(IFissionComponent.class).get(pos.toLong());
		return (component != null && component.isActiveModerator()) || (reactor.activeModeratorCache.contains(pos.toLong()) && RecipeHelper.blockRecipe(NCRecipes.fission_moderator, reactor.WORLD, pos) != null);
	}
	
	public static boolean isActiveReflector(FissionReactor reactor, BlockPos pos, boolean simulate) {
		return reactor.activeReflectorCache.contains(pos.toLong()) && RecipeHelper.blockRecipe(NCRecipes.fission_reflector, reactor.WORLD, pos) != null;
	}
	
	public static boolean isFunctionalIrradiator(FissionReactor reactor, BlockPos pos, boolean simulate) {
		TileFissionIrradiator irradiator = reactor.getPartMap(TileFissionIrradiator.class).get(pos.toLong());
		return irradiator != null && irradiator.isFunctional(simulate);
	}
	
	public static boolean isFunctionalShield(FissionReactor reactor, BlockPos pos, boolean simulate) {
		TileFissionShield shield = reactor.getPartMap(TileFissionShield.class).get(pos.toLong());
		return shield != null && shield.isFunctional(simulate);
	}
	
	public static boolean isFunctionalChamber(FissionReactor reactor, BlockPos pos, boolean simulate) {
		TilePebbleFissionChamber chamber = reactor.getPartMap(TilePebbleFissionChamber.class).get(pos.toLong());
		return chamber != null && chamber.isFunctional(simulate);
	}
	
	public static boolean isValidCooler(FissionReactor reactor, BlockPos pos, String coolerType, boolean simulate) {
		TilePebbleFissionCooler cooler = reactor.getPartMap(TilePebbleFissionCooler.class).get(pos.toLong());
		return cooler != null && cooler.isFunctional(simulate) && (coolerType.equals("any") || cooler.coolerType.equals(coolerType));
	}
	
	public static boolean isFunctionalCell(FissionReactor reactor, BlockPos pos, boolean simulate) {
		TileSolidFissionCell cell = reactor.getPartMap(TileSolidFissionCell.class).get(pos.toLong());
		return cell != null && cell.isFunctional(simulate);
	}
	
	public static boolean isValidSink(FissionReactor reactor, BlockPos pos, String sinkType, boolean simulate) {
		TileSolidFissionSink sink = reactor.getPartMap(TileSolidFissionSink.class).get(pos.toLong());
		return sink != null && sink.isFunctional(simulate) && (sinkType.equals("any") || sink.sinkType.equals(sinkType));
	}
	
	public static boolean isFunctionalVessel(FissionReactor reactor, BlockPos pos, boolean simulate) {
		TileSaltFissionVessel vessel = reactor.getPartMap(TileSaltFissionVessel.class).get(pos.toLong());
		return vessel != null && vessel.isFunctional(simulate);
	}
	
	public static boolean isValidHeater(FissionReactor reactor, BlockPos pos, String heaterType, boolean simulate) {
		TileSaltFissionHeater heater = reactor.getPartMap(TileSaltFissionHeater.class).get(pos.toLong());
		return heater != null && heater.isFunctional(simulate) && (heaterType.equals("any") || heater.heaterType.equals(heaterType));
	}
	
	// Default Tooltip Builder
	
	public static class DefaultTooltipBuilder extends PlacementRule.DefaultTooltipBuilder<FissionReactor, IFissionPart> {}
	
	// Recipe Handler
	
	public static class RecipeHandler extends PlacementRule.RecipeHandler {
		
		public RecipeHandler() {
			super("fission");
		}
	}
}
