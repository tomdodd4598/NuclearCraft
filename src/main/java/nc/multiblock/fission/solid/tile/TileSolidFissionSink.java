package nc.multiblock.fission.solid.tile;

import static nc.config.NCConfig.fission_sink_cooling_rate;

import java.util.Iterator;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import nc.multiblock.PlacementRule;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSolidFissionSink extends TileFissionPart implements IFissionCoolingComponent {
	
	public String sinkType;
	public int coolingRate;
	
	public String ruleID;
	public PlacementRule<FissionReactor, IFissionPart> placementRule;
	
	private FissionCluster cluster = null;
	private long heat = 0L;
	public boolean isInValidPosition = false;
	
	/** Don't use this constructor! */
	public TileSolidFissionSink() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	public TileSolidFissionSink(String sinkType, int coolingRate, String ruleID) {
		this();
		this.sinkType = sinkType;
		this.coolingRate = coolingRate;
		this.ruleID = ruleID;
		this.placementRule = FissionPlacement.RULE_MAP.get(ruleID);
	}
	
	protected static class Meta extends TileSolidFissionSink {
		
		protected Meta(String sinkType, int coolingID) {
			super(sinkType, fission_sink_cooling_rate[coolingID], sinkType + "_sink");
		}
		
		@Override
		public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
			return oldState != newState;
		}
	}
	
	public static class Water extends Meta {
		
		public Water() {
			super("water", 0);
		}
	}
	
	public static class Iron extends Meta {
		
		public Iron() {
			super("iron", 1);
		}
	}
	
	public static class Redstone extends Meta {
		
		public Redstone() {
			super("redstone", 2);
		}
	}
	
	public static class Quartz extends Meta {
		
		public Quartz() {
			super("quartz", 3);
		}
	}
	
	public static class Obsidian extends Meta {
		
		public Obsidian() {
			super("obsidian", 4);
		}
	}
	
	public static class NetherBrick extends Meta {
		
		public NetherBrick() {
			super("nether_brick", 5);
		}
	}
	
	public static class Glowstone extends Meta {
		
		public Glowstone() {
			super("glowstone", 6);
		}
	}
	
	public static class Lapis extends Meta {
		
		public Lapis() {
			super("lapis", 7);
		}
	}
	
	public static class Gold extends Meta {
		
		public Gold() {
			super("gold", 8);
		}
	}
	
	public static class Prismarine extends Meta {
		
		public Prismarine() {
			super("prismarine", 9);
		}
	}
	
	public static class Slime extends Meta {
		
		public Slime() {
			super("slime", 10);
		}
	}
	
	public static class EndStone extends Meta {
		
		public EndStone() {
			super("end_stone", 11);
		}
	}
	
	public static class Purpur extends Meta {
		
		public Purpur() {
			super("purpur", 12);
		}
	}
	
	public static class Diamond extends Meta {
		
		public Diamond() {
			super("diamond", 13);
		}
	}
	
	public static class Emerald extends Meta {
		
		public Emerald() {
			super("emerald", 14);
		}
	}
	
	public static class Copper extends Meta {
		
		public Copper() {
			super("copper", 15);
		}
	}
	
	public static class Tin extends Meta {
		
		public Tin() {
			super("tin", 16);
		}
	}
	
	public static class Lead extends Meta {
		
		public Lead() {
			super("lead", 17);
		}
	}
	
	public static class Boron extends Meta {
		
		public Boron() {
			super("boron", 18);
		}
	}
	
	public static class Lithium extends Meta {
		
		public Lithium() {
			super("lithium", 19);
		}
	}
	
	public static class Magnesium extends Meta {
		
		public Magnesium() {
			super("magnesium", 20);
		}
	}
	
	public static class Manganese extends Meta {
		
		public Manganese() {
			super("manganese", 21);
		}
	}
	
	public static class Aluminum extends Meta {
		
		public Aluminum() {
			super("aluminum", 22);
		}
	}
	
	public static class Silver extends Meta {
		
		public Silver() {
			super("silver", 23);
		}
	}
	
	public static class Fluorite extends Meta {
		
		public Fluorite() {
			super("fluorite", 24);
		}
	}
	
	public static class Villiaumite extends Meta {
		
		public Villiaumite() {
			super("villiaumite", 25);
		}
	}
	
	public static class Carobbiite extends Meta {
		
		public Carobbiite() {
			super("carobbiite", 26);
		}
	}
	
	public static class Arsenic extends Meta {
		
		public Arsenic() {
			super("arsenic", 27);
		}
	}
	
	public static class LiquidNitrogen extends Meta {
		
		public LiquidNitrogen() {
			super("liquid_nitrogen", 28);
		}
	}
	
	public static class LiquidHelium extends Meta {
		
		public LiquidHelium() {
			super("liquid_helium", 29);
		}
	}
	
	public static class Enderium extends Meta {
		
		public Enderium() {
			super("enderium", 30);
		}
	}
	
	public static class Cryotheum extends Meta {
		
		public Cryotheum() {
			super("cryotheum", 31);
		}
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	// IFissionComponent
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setClusterInternal(@Nullable FissionCluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (componentFailCache.containsKey(pos.toLong())) {
			return isInValidPosition = false;
		}
		else if (placementRule.requiresRecheck()) {
			isInValidPosition = placementRule.satisfied(this);
			if (isInValidPosition) {
				assumedValidCache.put(pos.toLong(), this);
			}
			return isInValidPosition;
		}
		else if (isInValidPosition) {
			return true;
		}
		return isInValidPosition = placementRule.satisfied(this);
	}
	
	@Override
	public boolean isFunctional() {
		return isInValidPosition;
	}
	
	@Override
	public void resetStats() {
		isInValidPosition = false;
	}
	
	@Override
	public boolean isClusterRoot() {
		return false;
	}
	
	@Override
	public long getHeatStored() {
		return heat;
	}
	
	@Override
	public void setHeatStored(long heat) {
		this.heat = heat;
	}
	
	@Override
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
		
	}
	
	@Override
	public boolean isNullifyingSources(EnumFacing side) {
		return false;
	}
	
	@Override
	public long getCooling() {
		return coolingRate;
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setString("sinkName", sinkType);
		nbt.setInteger("coolingRate", coolingRate);
		nbt.setString("ruleID", ruleID);
		
		nbt.setLong("clusterHeat", heat);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("sinkName")) sinkType = nbt.getString("sinkName");
		if (nbt.hasKey("coolingRate")) coolingRate = nbt.getInteger("coolingRate");
		if (nbt.hasKey("ruleID")) {
			ruleID = nbt.getString("ruleID");
			placementRule = FissionPlacement.RULE_MAP.get(ruleID);
		}
		
		heat = nbt.getLong("clusterHeat");
	}
}
