package nc.multiblock.fission.solid.tile;

import static nc.config.NCConfig.fission_sink_cooling_rate;

import java.util.Iterator;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import nc.multiblock.PlacementRule;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileSolidFissionSink extends TileFissionPart implements IFissionCoolingComponent {
	
	public String sinkName;
	public int coolingRate;
	
	public String ruleID;
	public PlacementRule<IFissionPart> placementRule;
	
	private FissionCluster cluster = null;
	private long heat = 0L;
	public boolean isInValidPosition = false;
	
	/** Don't use this constructor! */
	public TileSolidFissionSink() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	public TileSolidFissionSink(String sinkName, int coolingRate, String ruleID) {
		this();
		this.sinkName = sinkName;
		this.coolingRate = coolingRate;
		this.ruleID = ruleID;
		this.placementRule = FissionPlacement.RULE_MAP.get(ruleID);
	}
	
	protected TileSolidFissionSink(String sinkName, int coolingID) {
		this(sinkName, fission_sink_cooling_rate[coolingID], sinkName + "_sink");
	}
	
	public static class Water extends TileSolidFissionSink {
		
		public Water() {
			super("water", 0);
		}
	}
	
	public static class Iron extends TileSolidFissionSink {
		
		public Iron() {
			super("iron", 1);
		}
	}
	
	public static class Redstone extends TileSolidFissionSink {
		
		public Redstone() {
			super("redstone", 2);
		}
	}
	
	public static class Quartz extends TileSolidFissionSink {
		
		public Quartz() {
			super("quartz", 3);
		}
	}
	
	public static class Obsidian extends TileSolidFissionSink {
		
		public Obsidian() {
			super("obsidian", 4);
		}
	}
	
	public static class NetherBrick extends TileSolidFissionSink {
		
		public NetherBrick() {
			super("nether_brick", 5);
		}
	}
	
	public static class Glowstone extends TileSolidFissionSink {
		
		public Glowstone() {
			super("glowstone", 6);
		}
	}
	
	public static class Lapis extends TileSolidFissionSink {
		
		public Lapis() {
			super("lapis", 7);
		}
	}
	
	public static class Gold extends TileSolidFissionSink {
		
		public Gold() {
			super("gold", 8);
		}
	}
	
	public static class Prismarine extends TileSolidFissionSink {
		
		public Prismarine() {
			super("prismarine", 9);
		}
	}
	
	public static class Slime extends TileSolidFissionSink {
		
		public Slime() {
			super("slime", 10);
		}
	}
	
	public static class EndStone extends TileSolidFissionSink {
		
		public EndStone() {
			super("end_stone", 11);
		}
	}
	
	public static class Purpur extends TileSolidFissionSink {
		
		public Purpur() {
			super("purpur", 12);
		}
	}
	
	public static class Diamond extends TileSolidFissionSink {
		
		public Diamond() {
			super("diamond", 13);
		}
	}
	
	public static class Emerald extends TileSolidFissionSink {
		
		public Emerald() {
			super("emerald", 14);
		}
	}
	
	public static class Copper extends TileSolidFissionSink {
		
		public Copper() {
			super("copper", 15);
		}
	}
	
	public static class Tin extends TileSolidFissionSink {
		
		public Tin() {
			super("tin", 16);
		}
	}
	
	public static class Lead extends TileSolidFissionSink {
		
		public Lead() {
			super("lead", 17);
		}
	}
	
	public static class Boron extends TileSolidFissionSink {
		
		public Boron() {
			super("boron", 18);
		}
	}
	
	public static class Lithium extends TileSolidFissionSink {
		
		public Lithium() {
			super("lithium", 19);
		}
	}
	
	public static class Magnesium extends TileSolidFissionSink {
		
		public Magnesium() {
			super("magnesium", 20);
		}
	}
	
	public static class Manganese extends TileSolidFissionSink {
		
		public Manganese() {
			super("manganese", 21);
		}
	}
	
	public static class Aluminum extends TileSolidFissionSink {
		
		public Aluminum() {
			super("aluminum", 22);
		}
	}
	
	public static class Silver extends TileSolidFissionSink {
		
		public Silver() {
			super("silver", 23);
		}
	}
	
	public static class Fluorite extends TileSolidFissionSink {
		
		public Fluorite() {
			super("fluorite", 24);
		}
	}
	
	public static class Villiaumite extends TileSolidFissionSink {
		
		public Villiaumite() {
			super("villiaumite", 25);
		}
	}
	
	public static class Carobbiite extends TileSolidFissionSink {
		
		public Carobbiite() {
			super("carobbiite", 26);
		}
	}
	
	public static class Arsenic extends TileSolidFissionSink {
		
		public Arsenic() {
			super("arsenic", 27);
		}
	}
	
	public static class LiquidNitrogen extends TileSolidFissionSink {
		
		public LiquidNitrogen() {
			super("liquid_nitrogen", 28);
		}
	}
	
	public static class LiquidHelium extends TileSolidFissionSink {
		
		public LiquidHelium() {
			super("liquid_helium", 29);
		}
	}
	
	public static class Enderium extends TileSolidFissionSink {
		
		public Enderium() {
			super("enderium", 30);
		}
	}
	
	public static class Cryotheum extends TileSolidFissionSink {
		
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
		nbt.setString("sinkName", sinkName);
		nbt.setInteger("coolingRate", coolingRate);
		nbt.setString("ruleID", ruleID);
		
		nbt.setLong("clusterHeat", heat);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("sinkName"))
			sinkName = nbt.getString("sinkName");
		if (nbt.hasKey("coolingRate"))
			coolingRate = nbt.getInteger("coolingRate");
		if (nbt.hasKey("ruleID")) {
			ruleID = nbt.getString("ruleID");
			placementRule = FissionPlacement.RULE_MAP.get(ruleID);
		}
		
		heat = nbt.getLong("clusterHeat");
	}
}
