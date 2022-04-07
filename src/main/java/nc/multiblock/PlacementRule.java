package nc.multiblock;

import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.tile.ITileMultiblockPart;
import nc.multiblock.turbine.TurbinePlacement;
import nc.recipe.BasicRecipeHandler;
import nc.util.*;
import net.minecraft.util.EnumFacing;

public abstract class PlacementRule<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> {
	
	protected final @Nullable List<PlacementRule<MULTIBLOCK, T>> subRules;
	protected final List<String> dependencies;
	protected final boolean requiresRecheck;
	
	public PlacementRule(@Nullable List<PlacementRule<MULTIBLOCK, T>> subRules, List<String> dependencies, boolean requiresRecheck) {
		this.subRules = subRules;
		this.dependencies = dependencies;
		this.requiresRecheck = requiresRecheck;
	}
	
	public @Nullable List<PlacementRule<MULTIBLOCK, T>> getSubRules() {
		return subRules;
	}
	
	public List<String> getDependencies() {
		return dependencies;
	}
	
	public abstract void checkIsRuleAllowed(String ruleID);
	
	public void incrementMinimalAdjacencies(Int2IntMap adjacencyMap) {}
	
	/** Return true if an ambiguity is guaranteed. */
	public abstract boolean isCycle(Object2BooleanMap<String> boolMap);
	
	public boolean requiresRecheck() {
		return requiresRecheck;
	}
	
	public abstract boolean satisfied(T tile);
	
	// Setup
	
	public static void preInit() {
		FissionPlacement.preInit();
		TurbinePlacement.preInit();
	}
	
	public static void init() {
		FissionPlacement.init();
		TurbinePlacement.init();
	}
	
	public static void postInit() {
		FissionPlacement.postInit();
		TurbinePlacement.postInit();
	}
	
	public static void refreshRecipeCaches() {
		FissionPlacement.recipe_handler.refreshCache();
		TurbinePlacement.recipe_handler.refreshCache();
	}
	
	// Rule Parser
	
	public static <MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> PlacementRule<MULTIBLOCK, T> parse(String string, List<RuleParser<MULTIBLOCK, T>> parsers) {
		for (RuleParser<MULTIBLOCK, T> parser : parsers) {
			PlacementRule<MULTIBLOCK, T> rule = parser.parseRule(string);
			if (rule != null) {
				return rule;
			}
		}
		throw new IllegalArgumentException("The placement rule string \"" + string + "\" could not be parsed!");
	}
	
	public static abstract class RuleParser<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> {
		
		/** Returns non-null PlacementRule if the string is successfully parsed. */
		protected abstract @Nullable PlacementRule<MULTIBLOCK, T> parseRule(String s);
	}
	
	public static abstract class DefaultRuleParser<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends RuleParser<MULTIBLOCK, T> {
		
		@Override
		protected @Nullable PlacementRule<MULTIBLOCK, T> parseRule(String string) {
			string = string.toLowerCase(Locale.ROOT);
			
			List<PlacementRule<MULTIBLOCK, T>> rules = new ArrayList<>();
			String pattern = string.contains("&&") ? "&&" : "||";
			for (String s : string.split(Pattern.quote(pattern))) {
				PlacementRule<MULTIBLOCK, T> rule = partialParse(s);
				if (rule == null) {
					return null;
				}
				rules.add(rule);
			}
			
			return pattern.equals("&&") ? new And<>(rules) : new Or<>(rules);
		}
		
		protected abstract @Nullable PlacementRule<MULTIBLOCK, T> partialParse(String s);
	}
	
	// Basic Compound Rule Types
	
	public abstract static class BasicCompoundRule<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends PlacementRule<MULTIBLOCK, T> {
		
		public BasicCompoundRule(List<PlacementRule<MULTIBLOCK, T>> rules) {
			super(rules, concatDependencies(rules), mergeRequiresRecheck(rules));
		}
		
		@Override
		public void checkIsRuleAllowed(String ruleID) {
			Int2IntMap adjacencyMap = new Int2IntOpenHashMap();
			if (subRules != null) {
				for (PlacementRule<MULTIBLOCK, T> subRule : subRules) {
					subRule.checkIsRuleAllowed(ruleID);
					subRule.incrementMinimalAdjacencies(adjacencyMap);
				}
			}
			checkMinimalAdjacencies(ruleID, adjacencyMap);
		}
		
		public void checkMinimalAdjacencies(String ruleID, Int2IntMap adjacencyMap) {
			for (int count : adjacencyMap.values()) {
				if (count > 6) {
					throw new IllegalArgumentException("Adjacency placement rule with ID \"" + ruleID + "\" can not require more than six adjacencies!");
				}
			}
		}
	}
	
	public static class And<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends BasicCompoundRule<MULTIBLOCK, T> {
		
		public And(List<PlacementRule<MULTIBLOCK, T>> rules) {
			super(rules);
		}
		
		@Override
		public boolean isCycle(Object2BooleanMap<String> boolMap) {
			for (boolean b : boolMap.values()) {
				if (b) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean satisfied(T tile) {
			for (PlacementRule<MULTIBLOCK, T> rule : subRules) {
				if (!rule.satisfied(tile)) {
					return false;
				}
			}
			return true;
		}
	}
	
	public static class Or<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends BasicCompoundRule<MULTIBLOCK, T> {
		
		public Or(List<PlacementRule<MULTIBLOCK, T>> rules) {
			super(rules);
		}
		
		@Override
		public boolean isCycle(Object2BooleanMap<String> boolMap) {
			for (PlacementRule<MULTIBLOCK, T> rule : subRules) {
				if (rule.isCycle(boolMap)) {
					return true;
				}
			}
			if (boolMap.isEmpty()) {
				return false;
			}
			for (boolean b : boolMap.values()) {
				if (!b) {
					return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean satisfied(T tile) {
			for (PlacementRule<MULTIBLOCK, T> rule : subRules) {
				if (rule.satisfied(tile)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static <MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> List<String> concatDependencies(List<PlacementRule<MULTIBLOCK, T>> rules) {
		ObjectSet<String> dependencies = new ObjectOpenHashSet<>();
		for (PlacementRule<MULTIBLOCK, T> rule : rules) {
			dependencies.addAll(rule.getDependencies());
			if (rule.subRules != null) {
				dependencies.addAll(concatDependencies(rule.subRules));
			}
		}
		return new ArrayList<>(dependencies);
	}
	
	public static <MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> boolean mergeRequiresRecheck(List<PlacementRule<MULTIBLOCK, T>> rules) {
		boolean requiresRecheck = false;
		for (PlacementRule<MULTIBLOCK, T> rule : rules) {
			if (rule.requiresRecheck) {
				requiresRecheck = true;
			}
			if (rule.subRules != null) {
				if (mergeRequiresRecheck(rule.subRules)) {
					requiresRecheck = true;
				}
			}
		}
		return requiresRecheck;
	}
	
	// Basic Sub-Rule
	
	public abstract static class Adjacent<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends PlacementRule<MULTIBLOCK, T> {
		
		protected final int amount;
		protected final CountType countType;
		protected final AdjacencyType adjType;
		
		protected Adjacent(String dependency, int amount, CountType countType, AdjacencyType adjType) {
			super(null, Lists.newArrayList(dependency), countType.requiresRecheck());
			this.amount = amount;
			this.countType = countType;
			this.adjType = adjType;
		}
		
		@Override
		public void checkIsRuleAllowed(String ruleID) {
			if (amount > 6) {
				throw new IllegalArgumentException("Adjacency placement rule with ID \"" + ruleID + "\" can not require more than six adjacencies!");
			}
			if (amount < 0) {
				throw new IllegalArgumentException("Adjacency placement rule with ID \"" + ruleID + "\" can not require a negative number of adjacencies!");
			}
			if (countType != CountType.EXACTLY && amount == 0) {
				throw new IllegalArgumentException("Adjacency placement rule with ID \"" + ruleID + "\" can only require zero adjacencies if also an exact rule!");
			}
			if (adjType == AdjacencyType.AXIAL && (amount & 1) != 0) {
				throw new IllegalArgumentException("Axial adjacency placement rule with ID \"" + ruleID + "\" must require an even number of adjacencies!");
			}
			if (adjType == AdjacencyType.VERTEX) {
				if (amount != 3) {
					throw new IllegalArgumentException("Vertex adjacency placement rule with ID \"" + ruleID + "\" must require three adjacencies!");
				}
				if (countType == CountType.AT_MOST) {
					throw new IllegalArgumentException("Vertex adjacency placement rule with ID \"" + ruleID + "\" can not be an 'at most' rule!");
				}
			}
			if (adjType == AdjacencyType.EDGE) {
				if (amount != 2) {
					throw new IllegalArgumentException("Edge adjacency placement rule with ID \"" + ruleID + "\" must require two adjacencies!");
				}
				if (countType == CountType.AT_MOST) {
					throw new IllegalArgumentException("Edge adjacency placement rule with ID \"" + ruleID + "\" can not be an 'at most' rule!");
				}
			}
		}
		
		@Override
		public void incrementMinimalAdjacencies(Int2IntMap adjacencyMap) {
			adjacencyMap.put(1, adjacencyMap.get(1) + (countType == CountType.AT_MOST ? 0 : amount));
		}
		
		@Override
		public boolean isCycle(Object2BooleanMap<String> boolMap) {
			if (countType.requiresRecheck()) {
				for (Object2BooleanMap.Entry<String> entry : boolMap.object2BooleanEntrySet()) {
					if (entry.getBooleanValue() && dependencies.contains(entry.getKey())) {
						return true;
					}
				}
			}
			return false;
		}
		
		public String buildSubTooltip() {
			if (countType == CountType.EXACTLY && amount == 0) {
				return Lang.localise("nc.sf.placement_rule.adjacent.no", I18nHelper.getPluralForm("nc.sf." + dependencies.get(0), 0, Lang.localise("nc.sf.no")));
			}
			return Lang.localise("nc.sf.placement_rule.adjacent." + countType.tooltipSubstring(amount) + adjType.tooltipSubstring(amount), I18nHelper.getPluralForm("nc.sf." + dependencies.get(0), amount, Lang.localise("nc.sf." + StringHelper.NUMBER_I2S_MAP.get(amount))));
		}
		
		@Override
		public boolean satisfied(T tile) {
			byte count = 0;
			if (adjType == AdjacencyType.STANDARD) {
				for (EnumFacing dir : EnumFacing.VALUES) {
					if (satisfied(tile, dir)) {
						++count;
					}
					
					if (countType == CountType.AT_LEAST) {
						if (count >= amount) {
							return true;
						}
					}
					else if (count > amount) {
						return false;
					}
				}
				return countType == CountType.AT_MOST || (countType == CountType.EXACTLY && count == amount);
			}
			else if (adjType == AdjacencyType.AXIAL) {
				if (countType == CountType.EXACTLY) {
					boolean[] dirs = new boolean[] {false, false, false, false, false, false};
					for (EnumFacing dir : EnumFacing.VALUES) {
						if (satisfied(tile, dir)) {
							++count;
							if (count > amount) {
								return false;
							}
							
							dirs[dir.getIndex()] = true;
						}
					}
					if (count != amount) {
						return false;
					}
					
					count = 0;
					if (dirs[0] && dirs[1]) {
						++count;
					}
					if (dirs[2] && dirs[3]) {
						++count;
					}
					if (dirs[4] && dirs[5]) {
						++count;
					}
					return count == amount / 2;
				}
				else {
					loop: for (EnumFacing[] axialDirs : PosHelper.axialDirsList()) {
						for (EnumFacing dir : axialDirs) {
							if (!satisfied(tile, dir)) {
								continue loop;
							}
						}
						++count;
						if (countType == CountType.AT_LEAST) {
							if (count >= amount / 2) {
								return true;
							}
						}
						else if (count > amount / 2) {
							return false;
						}
					}
					return countType == CountType.AT_MOST;
				}
			}
			else {
				if (countType == CountType.EXACTLY) {
					boolean[] dirs = new boolean[] {false, false, false, false, false, false};
					for (EnumFacing dir : EnumFacing.VALUES) {
						if (satisfied(tile, dir)) {
							++count;
							if (count > amount) {
								return false;
							}
							
							dirs[dir.getIndex()] = true;
						}
					}
					if (count != amount) {
						return false;
					}
					
					loop: for (EnumFacing[] typeDirs : (adjType == AdjacencyType.VERTEX ? PosHelper.VERTEX_DIRS : PosHelper.EDGE_DIRS)) {
						for (EnumFacing dir : typeDirs) {
							if (!dirs[dir.getIndex()]) {
								continue loop;
							}
						}
						return true;
					}
					return false;
				}
				else {
					loop: for (EnumFacing[] typeDirs : (adjType == AdjacencyType.VERTEX ? PosHelper.VERTEX_DIRS : PosHelper.EDGE_DIRS)) {
						for (EnumFacing dir : typeDirs) {
							if (!satisfied(tile, dir)) {
								continue loop;
							}
						}
						return true;
					}
					return false;
				}
			}
		}
		
		public abstract boolean satisfied(T tile, EnumFacing dir);
	}
	
	public enum AdjacencyType {
		
		STANDARD,
		AXIAL,
		VERTEX,
		EDGE;
		
		public String tooltipSubstring(int amount) {
			switch (this) {
				case STANDARD:
					return "";
				case AXIAL:
					return amount == 2 ? "_along_axis" : "_along_axes";
				case VERTEX:
					return "_at_vertex";
				case EDGE:
					return "_along_edge";
				default:
					return "";
			}
		}
	}
	
	public enum CountType {
		
		AT_LEAST,
		EXACTLY,
		AT_MOST;
		
		public boolean requiresRecheck() {
			return this != AT_LEAST;
		}
		
		public String tooltipSubstring(int amount) {
			switch (this) {
				case AT_LEAST:
					return "at_least";
				case EXACTLY:
					return "exactly";
				case AT_MOST:
					return "at_most";
				default:
					return "";
			}
		}
	}
	
	// Placement Map
	
	public static class PlacementMap<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends Object2ObjectOpenHashMap<String, PlacementRule<MULTIBLOCK, T>> {
		
		@Override
		public PlacementRule<MULTIBLOCK, T> put(final String ruleID, final PlacementRule<MULTIBLOCK, T> rule) {
			rule.checkIsRuleAllowed(ruleID);
			PlacementRule<MULTIBLOCK, T> ret = super.put(ruleID, rule);
			checkCycles(ruleID, rule);
			return ret;
		}
		
		public void checkCycles(final String ruleID, final PlacementRule<MULTIBLOCK, T> rule) {
			Object2ObjectMap<String, Vertex<String>> vertexMap = new Object2ObjectOpenHashMap<>();
			Object2ObjectMap<String, Object2BooleanMap<String>> cycleMap = new Object2ObjectOpenHashMap<>();
			ObjectSet<String> finished = new ObjectOpenHashSet<>();
			
			Stack<Vertex<String>> vertexStack = new Stack<>();
			vertexStack.push(new Vertex<>(ruleID));
			
			while (!vertexStack.isEmpty()) {
				Vertex<String> v = vertexStack.pop();
				if (v == null) {
					continue;
				}
				
				if (vertexMap.containsKey(v.data)) {
					if (vertexMap.get(v.data).parent == null) {
						vertexMap.get(v.data).parent = v.parent;
					}
					continue;
				}
				
				vertexMap.put(v.data, v);
				Object2BooleanMap<String> boolMap = new Object2BooleanOpenHashMap<>();
				cycleMap.put(v.data, boolMap);
				
				for (String dep : get(v.data).getDependencies()) {
					if (containsKey(dep)) {
						v.addChild(dep);
					}
					else {
						finished.add(dep);
					}
					boolMap.put(dep, !finished.contains(dep));
				}
				
				if (!get(v.data).isCycle(boolMap)) {
					for (String s : v.getPath(false)) {
						if (!cycleMap.containsKey(s) || !get(s).isCycle(cycleMap.get(s))) {
							finished.add(s);
						}
						else {
							break;
						}
					}
				}
				
				for (Vertex<String> child : v.children) {
					vertexStack.push(child);
				}
			}
			
			for (String k : cycleMap.keySet()) {
				Object2BooleanMap<String> boolMap = cycleMap.get(k);
				for (String s : boolMap.keySet()) {
					boolMap.put(s, false);
				}
			}
			
			Stack<String> stack = new Stack<>();
			stack.push(ruleID);
			
			while (!stack.isEmpty()) {
				Vertex<String> v = vertexMap.get(stack.pop());
				if (v == null) {
					continue;
				}
				
				Object2BooleanMap<String> boolMap = cycleMap.get(v.data);
				
				if (v.parent != null && cycleMap.containsKey(v.parent.data)) {
					cycleMap.get(v.parent.data).put(v.data, !finished.contains(v.data));
				}
				
				if (boolMap != null && get(v.data).isCycle(boolMap)) {
					boolean totalCycle = false;
					List<String> cycles = new ArrayList<>();
					
					for (Vertex<String> child : v.children) {
						boolean continueCycle = true;
						Stack<String> traversedPath = new Stack<>();
						
						String cycle = "[";
						for (String s : child.getPath(true)) {
							traversedPath.push(s);
							if (cycleMap.containsKey(s) && get(s).isCycle(cycleMap.get(s))) {
								cycle += ("\"" + s + "\" -> ");
							}
							else {
								continueCycle = false;
								break;
							}
						}
						
						if (!continueCycle) {
							while (!traversedPath.isEmpty()) {
								String s = traversedPath.pop();
								if (!cycleMap.containsKey(s) || !get(s).isCycle(cycleMap.get(s))) {
									finished.add(s);
								}
								else {
									break;
								}
							}
						}
						else {
							totalCycle = true;
							cycle = StringHelper.removeSuffix(cycle, 4) + " -> ...]";
							cycles.add(cycle);
						}
					}
					
					if (totalCycle && v.partOfCycle()) {
						String out;
						if (cycles.size() == 1) {
							out = cycles.get(0);
						}
						else {
							out = "{";
							for (String c : cycles) {
								out += (c + ", ");
							}
							out = StringHelper.removeSuffix(out, 2) + "}";
						}
						
						throw new IllegalArgumentException("The placement rule with ID \"" + ruleID + "\" contained the cyclic dependency " + out + "!");
					}
				}
				
				for (Vertex<String> child : v.children) {
					if (!finished.contains(child.data)) {
						stack.push(child.data);
					}
				}
			}
		}
	}
	
	// Tooltip Builder
	
	public static abstract class TooltipBuilder<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> {
		
		/** Returns non-null PlacementRule if the tooltip is successfully built. */
		public abstract String buildTooltip(PlacementRule<MULTIBLOCK, T> rule);
	}
	
	public static class DefaultTooltipBuilder<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends TooltipBuilder<MULTIBLOCK, T> {
		
		@Override
		public String buildTooltip(PlacementRule<MULTIBLOCK, T> rule) {
			if (rule instanceof Adjacent) {
				return Lang.localise("nc.sf.placement_rule.and_or.adjacent4", Lang.localise("nc.sf.placement_rule.and_or.adjacent0", Lang.localise("nc.sf.placement_rule.adjacent.must_be_adjacent_to"), ((Adjacent<MULTIBLOCK, T>) rule).buildSubTooltip()));
			}
			else if (rule instanceof And || rule instanceof Or) {
				LinkedList<String> subTooltips = new LinkedList<>();
				for (PlacementRule<MULTIBLOCK, T> r : rule.getSubRules()) {
					subTooltips.add(r instanceof Adjacent ? ((Adjacent<MULTIBLOCK, T>) r).buildSubTooltip() : "?");
				}
				return Lang.localise("nc.sf.placement_rule.and_or.adjacent4", Lang.localise("nc.sf.placement_rule.and_or.adjacent0", Lang.localise("nc.sf.placement_rule.adjacent.must_be_adjacent_to"), joinSubTooltips(subTooltips, Lang.localise("nc.sf." + (rule instanceof And ? "and" : "or")))));
			}
			return rule.getDependencies().toString();
		}
		
		protected String joinSubTooltips(LinkedList<String> subTooltips, String conj) {
			if (subTooltips.size() > 2) {
				return Lang.localise("nc.sf.placement_rule.and_or.adjacent1", subTooltips.removeFirst(), joinSubTooltips(subTooltips, conj));
			}
			else if (subTooltips.size() == 2) {
				return Lang.localise("nc.sf.placement_rule.and_or.adjacent" + (hasPotentialAmbiguity(subTooltips.get(0), subTooltips.get(1)) ? 3 : 2), subTooltips.get(0), conj, subTooltips.get(1));
			}
			else if (subTooltips.size() == 1) {
				return subTooltips.get(0);
			}
			else {
				return "?";
			}
		}
		
		protected List<String> p_patterns = null, l_patterns = null;
		protected List<String[]> p_splits = null, l_splits = null;
		protected List<boolean[]> p_invs = null, l_invs = null;
		
		protected boolean hasPotentialAmbiguity(String prev, String last) {
			if (p_patterns == null) {
				setupAmbiguityChecks();
			}
			
			loop: for (int i = 0; i < p_patterns.size(); ++i) {
				boolean p = false;
				String[] p_split = p_splits.get(i);
				boolean[] p_inv = p_invs.get(i);
				if (p_patterns.get(i) == null) {
					p = true;
				}
				else if (p_patterns.get(i).equals("&&")) {
					for (int j = 0; j < p_inv.length; ++j) {
						if (!(p_inv[j] ^ prev.contains(p_split[j]))) {
							continue loop;
						}
					}
					p = true;
				}
				else {
					for (int j = 0; j < p_inv.length; ++j) {
						if (p_inv[j] ^ prev.contains(p_split[j])) {
							p = true;
							break;
						}
					}
					if (!p)
						continue;
				}
				
				String[] l_split = l_splits.get(i);
				boolean[] l_inv = l_invs.get(i);
				if (l_patterns.get(i) == null) {
					return true;
				}
				else if (l_patterns.get(i).equals("&&")) {
					for (int j = 0; j < l_inv.length; ++j) {
						if (!(l_inv[j] ^ last.contains(l_split[j]))) {
							continue loop;
						}
					}
					return true;
				}
				else {
					for (int j = 0; j < l_inv.length; ++j) {
						if (l_inv[j] ^ last.contains(l_split[j])) {
							return true;
						}
					}
				}
			}
			return false;
		}
		
		protected void setupAmbiguityChecks() {
			p_patterns = new ArrayList<>();
			l_patterns = new ArrayList<>();
			p_splits = new ArrayList<>();
			l_splits = new ArrayList<>();
			p_invs = new ArrayList<>();
			l_invs = new ArrayList<>();
			
			int i = 0;
			String s = "nc.sf.placement_rule.adjacent.ambiguity";
			while (Lang.canLocalise(s + i + "prev") || Lang.canLocalise(s + i + "last")) {
				
				if (Lang.canLocalise(s + i + "prev")) {
					String p = Lang.localise(s + i + "prev");
					p_patterns.add(p.contains("&&") ? "&&" : "||");
					
					p_splits.add(p.split(Pattern.quote(p_patterns.get(i))));
					
					String[] p_split = p_splits.get(i);
					p_invs.add(new boolean[p_split.length]);
					
					boolean[] p_inv = p_invs.get(i);
					for (int j = 0; j < p_inv.length; ++j) {
						p_inv[j] = p_split[j].startsWith("!");
						if (p_inv[j]) {
							p_split[j] = p_split[j].substring(1);
						}
					}
				}
				else {
					p_patterns.add(null);
					p_splits.add(null);
					p_invs.add(null);
				}
				
				if (Lang.canLocalise(s + i + "last")) {
					String l = Lang.localise(s + i + "last");
					l_patterns.add(l.contains("&&") ? "&&" : "||");
					
					l_splits.add(l.split(Pattern.quote(l_patterns.get(i))));
					
					String[] l_split = l_splits.get(i);
					l_invs.add(new boolean[l_split.length]);
					
					boolean[] l_inv = l_invs.get(i);
					for (int j = 0; j < l_invs.get(i).length; ++j) {
						l_inv[j] = l_split[j].startsWith("!");
						if (l_inv[j]) {
							l_split[j] = l_split[j].substring(1);
						}
					}
				}
				else {
					l_patterns.add(null);
					l_splits.add(null);
					l_invs.add(null);
				}
				
				++i;
			}
		}
	}
	
	// Recipe Handler
	
	public static class RecipeHandler extends BasicRecipeHandler {
		
		public RecipeHandler(String type) {
			super(type + "_placement_rules", 1, 0, 0, 0);
		}
		
		@Override
		public void addRecipes() {}
		
		@Override
		public List<Object> fixExtras(List<Object> extras) {
			List<Object> fixed = new ArrayList<>(1);
			fixed.add(extras.size() > 0 && extras.get(0) instanceof String ? (String) extras.get(0) : "");
			return fixed;
		}
	}
}
