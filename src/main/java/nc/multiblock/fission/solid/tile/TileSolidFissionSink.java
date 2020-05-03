package nc.multiblock.fission.solid.tile;

import javax.annotation.Nullable;

import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.IFissionCoolingComponent;
import nc.multiblock.fission.tile.TileFissionPart;
import nc.util.BlockPosHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileSolidFissionSink extends TileFissionPart implements IFissionCoolingComponent {
	
	public final int coolingRate;
	public final String sinkName;
	
	private FissionCluster cluster = null;
	private long heat = 0L;
	public boolean isInValidPosition = false;
	
	public TileSolidFissionSink(int coolingRate, String sinkName) {
		super(CuboidalPartPositionType.INTERIOR);
		this.coolingRate = coolingRate;
		this.sinkName = sinkName;
	}
	
	public static class Water extends TileSolidFissionSink {
		
		public Water() {
			super(NCConfig.fission_sink_cooling_rate[0], "water");
		}
		
		@Override
		public boolean isSinkValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveCell(pos.offset(dir))) return true;
			}
			return false;
		}
	}
	
	public static class Iron extends TileSolidFissionSink {
		
		public Iron() {
			super(NCConfig.fission_sink_cooling_rate[1], "iron");
		}
		
		@Override
		public boolean isSinkValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveModerator(pos.offset(dir))) return true;
			}
			return false;
		}
	}
	
	public static class Redstone extends TileSolidFissionSink {
		
		public Redstone() {
			super(NCConfig.fission_sink_cooling_rate[2], "redstone");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean cell = false, moderator = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!cell && isActiveCell(pos.offset(dir))) cell = true;
				if (!moderator && isActiveModerator(pos.offset(dir))) moderator = true;
				if (cell && moderator) return true;
			}
			return false;
		}
	}
	
	public static class Quartz extends TileSolidFissionSink {
		
		public Quartz() {
			super(NCConfig.fission_sink_cooling_rate[3], "quartz");
		}
		
		@Override
		public boolean isSinkValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "redstone")) return true;
			}
			return false;
		}
	}
	
	public static class Obsidian extends TileSolidFissionSink {
		
		public Obsidian() {
			super(NCConfig.fission_sink_cooling_rate[4], "obsidian");
		}
		
		@Override
		public boolean isSinkValid() {
			axialDirsLoop: for (EnumFacing[] axialDirs : BlockPosHelper.axialDirsList()) {
				for (EnumFacing dir : axialDirs) {
					if (!isActiveSink(pos.offset(dir), "glowstone")) continue axialDirsLoop;
				}
				return true;
			}
			return false;
		}
	}
	
	public static class NetherBrick extends TileSolidFissionSink {
		
		public NetherBrick() {
			super(NCConfig.fission_sink_cooling_rate[5], "nether_brick");
		}
		
		@Override
		public boolean isSinkValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "obsidian")) return true;
			}
			return false;
		}
	}
	
	public static class Glowstone extends TileSolidFissionSink {
		
		public Glowstone() {
			super(NCConfig.fission_sink_cooling_rate[6], "glowstone");
		}
		
		@Override
		public boolean isSinkValid() {
			byte moderators = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveModerator(pos.offset(dir))) moderators++;
				if (moderators >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Lapis extends TileSolidFissionSink {
		
		public Lapis() {
			super(NCConfig.fission_sink_cooling_rate[7], "lapis");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean cell = false, wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!cell && isActiveCell(pos.offset(dir))) cell = true;
				if (!wall && isWall(pos.offset(dir))) wall = true;
				if (cell && wall) return true;
			}
			return false;
		}
	}
	
	public static class Gold extends TileSolidFissionSink {
		
		public Gold() {
			super(NCConfig.fission_sink_cooling_rate[8], "gold");
		}
		
		@Override
		public boolean isSinkValid() {
			byte iron = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "iron")) iron++;
				if (iron >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Prismarine extends TileSolidFissionSink {
		
		public Prismarine() {
			super(NCConfig.fission_sink_cooling_rate[9], "prismarine");
		}
		
		@Override
		public boolean isSinkValid() {
			byte water = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "water")) water++;
				if (water >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Slime extends TileSolidFissionSink {
		
		public Slime() {
			super(NCConfig.fission_sink_cooling_rate[10], "slime");
		}
		
		@Override
		public boolean isSinkValid() {
			byte water = 0, lead = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "water")) water++;
				if (water > 1) return false;
				if (lead < 2 && isActiveSink(pos.offset(dir), "lead")) lead++;
			}
			return water == 1 && lead >= 2;
		}
	}
	
	public static class EndStone extends TileSolidFissionSink {
		
		public EndStone() {
			super(NCConfig.fission_sink_cooling_rate[11], "end_stone");
		}
		
		@Override
		public boolean isSinkValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveReflector(pos.offset(dir))) return true;
			}
			return false;
		}
	}
	
	public static class Purpur extends TileSolidFissionSink {
		
		public Purpur() {
			super(NCConfig.fission_sink_cooling_rate[12], "purpur");
		}
		
		@Override
		public boolean isSinkValid() {
			byte iron = 0;
			boolean endStone = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "iron")) iron++;
				if (iron > 1) return false;
				if (!endStone && isActiveSink(pos.offset(dir), "end_stone")) endStone = true;
			}
			return iron == 1 && endStone;
		}
	}
	
	public static class Diamond extends TileSolidFissionSink {
		
		public Diamond() {
			super(NCConfig.fission_sink_cooling_rate[13], "diamond");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean cell = false, gold = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!cell && isActiveCell(pos.offset(dir))) cell = true;
				if (!gold && isActiveSink(pos.offset(dir), "gold")) gold = true;
				if (cell && gold) return true;
			}
			return false;
		}
	}
	
	public static class Emerald extends TileSolidFissionSink {
		
		public Emerald() {
			super(NCConfig.fission_sink_cooling_rate[14], "emerald");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean moderator = false, prismarine = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!moderator && isActiveModerator(pos.offset(dir))) moderator = true;
				if (!prismarine && isActiveSink(pos.offset(dir), "prismarine")) prismarine = true;
				if (moderator && prismarine) return true;
			}
			return false;
		}
	}
	
	public static class Copper extends TileSolidFissionSink {
		
		public Copper() {
			super(NCConfig.fission_sink_cooling_rate[15], "copper");
		}
		
		@Override
		public boolean isSinkValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "water")) return true;
			}
			return false;
		}
	}
	
	public static class Tin extends TileSolidFissionSink {
		
		public Tin() {
			super(NCConfig.fission_sink_cooling_rate[16], "tin");
		}
		
		@Override
		public boolean isSinkValid() {
			axialDirsLoop: for (EnumFacing[] axialDirs : BlockPosHelper.axialDirsList()) {
				for (EnumFacing dir : axialDirs) {
					if (!isActiveSink(pos.offset(dir), "lapis")) continue axialDirsLoop;
				}
				return true;
			}
			return false;
		}
	}
	
	public static class Lead extends TileSolidFissionSink {
		
		public Lead() {
			super(NCConfig.fission_sink_cooling_rate[17], "lead");
		}
		
		@Override
		public boolean isSinkValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "iron")) return true;
			}
			return false;
		}
	}
	
	public static class Boron extends TileSolidFissionSink {
		
		public Boron() {
			super(NCConfig.fission_sink_cooling_rate[18], "boron");
		}
		
		@Override
		public boolean isSinkValid() {
			byte quartz = 0;
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "quartz")) quartz++;
				if (quartz > 1) return false;
				if (!wall && isWall(pos.offset(dir))) wall = true;
			}
			return quartz == 1 && wall;
		}
	}
	
	public static class Lithium extends TileSolidFissionSink {
		
		public Lithium() {
			super(NCConfig.fission_sink_cooling_rate[19], "lithium");
		}
		
		@Override
		public boolean isSinkValid() {
			byte l = 0;
			boolean[] lead = new boolean[] {false, false, false, false, false, false};
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "lead")) {
					l++;
					lead[dir.ordinal()] = true;
				}
				if (l > 2) return false;
				if (!wall && isWall(pos.offset(dir))) wall = true;
			}
			return wall && l == 2 && ((lead[0] && lead[1]) || (lead[2] && lead[3]) || (lead[4] && lead[5]));
			
			/*boolean lead = false;
			axialDirsLoop: for (EnumFacing[] axialDirs : BlockPosHelper.axialDirsList()) {
				for (EnumFacing dir : axialDirs) {
					if (!isActiveSink(pos.offset(dir), "lead")) continue axialDirsLoop;
				}
				lead = true;
				break;
			}
			if (!lead) return false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isWall(pos.offset(dir))) return true;
			}
			return false;*/
		}
	}
	
	public static class Magnesium extends TileSolidFissionSink {
		
		public Magnesium() {
			super(NCConfig.fission_sink_cooling_rate[20], "magnesium");
		}
		
		@Override
		public boolean isSinkValid() {
			byte moderator = 0;
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveModerator(pos.offset(dir))) moderator++;
				if (moderator > 1) return false;
				if (!wall && isWall(pos.offset(dir))) wall = true;
			}
			return moderator == 1 && wall;
		}
	}
	
	public static class Manganese extends TileSolidFissionSink {
		
		public Manganese() {
			super(NCConfig.fission_sink_cooling_rate[21], "manganese");
		}
		
		@Override
		public boolean isSinkValid() {
			byte cell = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveCell(pos.offset(dir))) cell++;
				if (cell >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Aluminum extends TileSolidFissionSink {
		
		public Aluminum() {
			super(NCConfig.fission_sink_cooling_rate[22], "aluminum");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean quartz = false, lapis = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!quartz && isActiveSink(pos.offset(dir), "quartz")) quartz = true;
				if (!lapis && isActiveSink(pos.offset(dir), "lapis")) lapis = true;
				if (quartz && lapis) return true;
			}
			return false;
		}
	}
	
	public static class Silver extends TileSolidFissionSink {
		
		public Silver() {
			super(NCConfig.fission_sink_cooling_rate[23], "silver");
		}
		
		@Override
		public boolean isSinkValid() {
			byte glowstone = 0;
			boolean tin = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (glowstone < 2 && isActiveSink(pos.offset(dir), "glowstone")) glowstone++;
				if (!tin && isActiveSink(pos.offset(dir), "tin")) tin = true;
				if (glowstone >= 2 && tin) return true;
			}
			return false;
		}
	}
	
	public static class Fluorite extends TileSolidFissionSink {
		
		public Fluorite() {
			super(NCConfig.fission_sink_cooling_rate[24], "fluorite");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean gold = false, prismarine = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!gold && isActiveSink(pos.offset(dir), "gold")) gold = true;
				if (!prismarine && isActiveSink(pos.offset(dir), "prismarine")) prismarine = true;
				if (gold && prismarine) return true;
			}
			return false;
		}
	}
	
	public static class Villiaumite extends TileSolidFissionSink {
		
		public Villiaumite() {
			super(NCConfig.fission_sink_cooling_rate[25], "villiaumite");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean redstone = false, endStone = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!redstone && isActiveSink(pos.offset(dir), "redstone")) redstone = true;
				if (!endStone && isActiveSink(pos.offset(dir), "end_stone")) endStone = true;
				if (redstone && endStone) return true;
			}
			return false;
		}
	}
	
	public static class Carobbiite extends TileSolidFissionSink {
		
		public Carobbiite() {
			super(NCConfig.fission_sink_cooling_rate[26], "carobbiite");
		}
		
		@Override
		public boolean isSinkValid() {
			boolean endStone = false, copper = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!endStone && isActiveSink(pos.offset(dir), "end_stone")) endStone = true;
				if (!copper && isActiveSink(pos.offset(dir), "copper")) copper = true;
				if (endStone && copper) return true;
			}
			return false;
		}
	}
	
	public static class Arsenic extends TileSolidFissionSink {
		
		public Arsenic() {
			super(NCConfig.fission_sink_cooling_rate[27], "arsenic");
		}
		
		@Override
		public boolean isSinkValid() {
			axialDirsLoop: for (EnumFacing[] axialDirs : BlockPosHelper.axialDirsList()) {
				for (EnumFacing dir : axialDirs) {
					if (!isActiveReflector(pos.offset(dir))) continue axialDirsLoop;
				}
				return true;
			}
			return false;
		}
	}
	
	public static class LiquidNitrogen extends TileSolidFissionSink {
		
		public LiquidNitrogen() {
			super(NCConfig.fission_sink_cooling_rate[28], "liquid_nitrogen");
		}
		
		@Override
		public boolean isSinkValid() {
			byte copper = 0;
			boolean purpur = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (copper < 2 && isActiveSink(pos.offset(dir), "copper")) copper++;
				if (!purpur && isActiveSink(pos.offset(dir), "purpur")) purpur = true;
				if (copper >= 2 && purpur) return true;
			}
			return false;
		}
	}
	
	public static class LiquidHelium extends TileSolidFissionSink {
		
		public LiquidHelium() {
			super(NCConfig.fission_sink_cooling_rate[29], "liquid_helium");
		}
		
		@Override
		public boolean isSinkValid() {
			byte redstone = 0;
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveSink(pos.offset(dir), "redstone")) redstone++;
				if (redstone > 2) return false;
				if (!wall && isWall(pos.offset(dir))) wall = true;
			}
			return redstone == 2 && wall;
		}
	}
	
	public static class Enderium extends TileSolidFissionSink {
		
		public Enderium() {
			super(NCConfig.fission_sink_cooling_rate[30], "enderium");
		}
		
		@Override
		public boolean isSinkValid() {
			byte moderators = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveModerator(pos.offset(dir))) moderators++;
				if (moderators >= 3) return true;
			}
			return false;
		}
	}
	
	public static class Cryotheum extends TileSolidFissionSink {
		
		public Cryotheum() {
			super(NCConfig.fission_sink_cooling_rate[31], "cryotheum");
		}
		
		@Override
		public boolean isSinkValid() {
			byte cell = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveCell(pos.offset(dir))) cell++;
				if (cell >= 3) return true;
			}
			return false;
		}
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
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
	public boolean isValidHeatConductor() {
		if (isInValidPosition) return true;
		return isInValidPosition = isSinkValid();
	}
	
	@Override
	public boolean isFunctional() {
		return isInValidPosition;
	}
	
	public abstract boolean isSinkValid();
	
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
	public void onClusterMeltdown() {}
	
	@Override
	public long getCooling() {
		return coolingRate;
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setLong("clusterHeat", heat);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		heat = nbt.getLong("clusterHeat");
	}
}
