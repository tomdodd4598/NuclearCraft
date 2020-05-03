package nc.multiblock.fission.salt.tile;

import static nc.init.NCCoolantFluids.COOLANTS;
import static nc.recipe.NCRecipes.coolant_heater;
import static nc.util.BlockPosHelper.DEFAULT_NON;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.SaltFissionHeaterSetting;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionCoolingComponent;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorBlockInfo;
import nc.multiblock.fission.tile.TileFissionPart;
import nc.multiblock.fission.tile.port.IFissionPortTarget;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import nc.multiblock.network.SaltFissionHeaterUpdatePacket;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.ProcessorRecipe;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.ITileGui;
import nc.tile.fluid.ITileFilteredFluid;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankOutputSetting;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.processor.IFluidProcessor;
import nc.util.BlockPosHelper;
import nc.util.FluidStackHelper;
import nc.util.GasHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileSaltFissionHeater extends TileFissionPart implements ITileFilteredFluid, ITileGui<SaltFissionHeaterUpdatePacket>, IFluidProcessor, IFissionCoolingComponent, IFissionPortTarget<TileFissionHeaterPort, TileSaltFissionHeater> {
	
	protected final @Nonnull List<Tank> tanks;
	protected final @Nonnull List<Tank> filterTanks;
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected @Nonnull SaltFissionHeaterSetting[] heaterSettings = new SaltFissionHeaterSetting[] {SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED};
	
	protected final int fluidInputSize = 1, fluidOutputSize = 1;
	
	protected int baseProcessCooling;
	public double heatingSpeedMultiplier; // Based on the cluster efficiency, but with heat/cooling taken into account
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public final String heaterName, coolantName;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	protected boolean isInValidPosition = false;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionHeaterPort masterPort = null;
	
	public TileSaltFissionHeater(String heaterName, String coolantName) {
		super(CuboidalPartPositionType.INTERIOR);
		this.heaterName = heaterName;
		this.coolantName = coolantName;
		tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME, Lists.newArrayList(coolantName)), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME, new ArrayList<>()));
		filterTanks = Lists.newArrayList(new Tank(1000, Lists.newArrayList(coolantName)), new Tank(1000, new ArrayList<>()));
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		
		playersToUpdate = new ObjectOpenHashSet<>();
	}
	
	protected TileSaltFissionHeater(String heaterName, int coolant) {
		this(heaterName, COOLANTS.get(coolant) + "nak");
	}
	
	public static class Standard extends TileSaltFissionHeater {
		
		public Standard() {
			super("standard", 0);
		}
		
		@Override
		public boolean isHeaterValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveVessel(pos.offset(dir))) return true;
			}
			return false;
		}
	}
	
	public static class Iron extends TileSaltFissionHeater {
		
		public Iron() {
			super("iron", 1);
		}
		
		@Override
		public boolean isHeaterValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveModerator(pos.offset(dir))) return true;
			}
			return false;
		}
	}
	
	public static class Redstone extends TileSaltFissionHeater {
		
		public Redstone() {
			super("redstone", 2);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean cell = false, moderator = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!cell && isActiveVessel(pos.offset(dir))) cell = true;
				if (!moderator && isActiveModerator(pos.offset(dir))) moderator = true;
				if (cell && moderator) return true;
			}
			return false;
		}
	}
	
	public static class Quartz extends TileSaltFissionHeater {
		
		public Quartz() {
			super("quartz", 3);
		}
		
		@Override
		public boolean isHeaterValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "redstone")) return true;
			}
			return false;
		}
	}
	
	public static class Obsidian extends TileSaltFissionHeater {
		
		public Obsidian() {
			super("obsidian", 4);
		}
		
		@Override
		public boolean isHeaterValid() {
			axialDirsLoop: for (EnumFacing[] axialDirs : BlockPosHelper.axialDirsList()) {
				for (EnumFacing dir : axialDirs) {
					if (!isActiveHeater(pos.offset(dir), "glowstone")) continue axialDirsLoop;
				}
				return true;
			}
			return false;
		}
	}
	
	public static class NetherBrick extends TileSaltFissionHeater {
		
		public NetherBrick() {
			super("nether_brick", 5);
		}
		
		@Override
		public boolean isHeaterValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "obsidian")) return true;
			}
			return false;
		}
	}
	
	public static class Glowstone extends TileSaltFissionHeater {
		
		public Glowstone() {
			super("glowstone", 6);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte moderators = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveModerator(pos.offset(dir))) moderators++;
				if (moderators >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Lapis extends TileSaltFissionHeater {
		
		public Lapis() {
			super("lapis", 7);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean cell = false, wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!cell && isActiveVessel(pos.offset(dir))) cell = true;
				if (!wall && isWall(pos.offset(dir))) wall = true;
				if (cell && wall) return true;
			}
			return false;
		}
	}
	
	public static class Gold extends TileSaltFissionHeater {
		
		public Gold() {
			super("gold", 8);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte iron = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "iron")) iron++;
				if (iron >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Prismarine extends TileSaltFissionHeater {
		
		public Prismarine() {
			super("prismarine", 9);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte standard = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "standard")) standard++;
				if (standard >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Slime extends TileSaltFissionHeater {
		
		public Slime() {
			super("slime", 10);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte standard = 0, lead = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "standard")) standard++;
				if (standard > 1) return false;
				if (lead < 2 && isActiveHeater(pos.offset(dir), "lead")) lead++;
			}
			return standard == 1 && lead >= 2;
		}
	}
	
	public static class EndStone extends TileSaltFissionHeater {
		
		public EndStone() {
			super("end_stone", 11);
		}
		
		@Override
		public boolean isHeaterValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveReflector(pos.offset(dir))) return true;
			}
			return false;
		}
	}
	
	public static class Purpur extends TileSaltFissionHeater {
		
		public Purpur() {
			super("purpur", 12);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte iron = 0;
			boolean endStone = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "iron")) iron++;
				if (iron > 1) return false;
				if (!endStone && isActiveHeater(pos.offset(dir), "end_stone")) endStone = true;
			}
			return iron == 1 && endStone;
		}
	}
	
	public static class Diamond extends TileSaltFissionHeater {
		
		public Diamond() {
			super("diamond", 13);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean cell = false, gold = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!cell && isActiveVessel(pos.offset(dir))) cell = true;
				if (!gold && isActiveHeater(pos.offset(dir), "gold")) gold = true;
				if (cell && gold) return true;
			}
			return false;
		}
	}
	
	public static class Emerald extends TileSaltFissionHeater {
		
		public Emerald() {
			super("emerald", 14);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean moderator = false, prismarine = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!moderator && isActiveModerator(pos.offset(dir))) moderator = true;
				if (!prismarine && isActiveHeater(pos.offset(dir), "prismarine")) prismarine = true;
				if (moderator && prismarine) return true;
			}
			return false;
		}
	}
	
	public static class Copper extends TileSaltFissionHeater {
		
		public Copper() {
			super("copper", 15);
		}
		
		@Override
		public boolean isHeaterValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "standard")) return true;
			}
			return false;
		}
	}
	
	public static class Tin extends TileSaltFissionHeater {
		
		public Tin() {
			super("tin", 16);
		}
		
		@Override
		public boolean isHeaterValid() {
			axialDirsLoop: for (EnumFacing[] axialDirs : BlockPosHelper.axialDirsList()) {
				for (EnumFacing dir : axialDirs) {
					if (!isActiveHeater(pos.offset(dir), "lapis")) continue axialDirsLoop;
				}
				return true;
			}
			return false;
		}
	}
	
	public static class Lead extends TileSaltFissionHeater {
		
		public Lead() {
			super("lead", 17);
		}
		
		@Override
		public boolean isHeaterValid() {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "iron")) return true;
			}
			return false;
		}
	}
	
	public static class Boron extends TileSaltFissionHeater {
		
		public Boron() {
			super("boron", 18);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte quartz = 0;
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "quartz")) quartz++;
				if (quartz > 1) return false;
				if (!wall && isWall(pos.offset(dir))) wall = true;
			}
			return quartz == 1 && wall;
		}
	}
	
	public static class Lithium extends TileSaltFissionHeater {
		
		public Lithium() {
			super("lithium", 19);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte l = 0;
			boolean[] lead = new boolean[] {false, false, false, false, false, false};
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "lead")) {
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
					if (!isActiveHeater(pos.offset(dir), "lead")) continue axialDirsLoop;
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
	
	public static class Magnesium extends TileSaltFissionHeater {
		
		public Magnesium() {
			super("magnesium", 20);
		}
		
		@Override
		public boolean isHeaterValid() {
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
	
	public static class Manganese extends TileSaltFissionHeater {
		
		public Manganese() {
			super("manganese", 21);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte cell = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveVessel(pos.offset(dir))) cell++;
				if (cell >= 2) return true;
			}
			return false;
		}
	}
	
	public static class Aluminum extends TileSaltFissionHeater {
		
		public Aluminum() {
			super("aluminum", 22);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean quartz = false, lapis = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!quartz && isActiveHeater(pos.offset(dir), "quartz")) quartz = true;
				if (!lapis && isActiveHeater(pos.offset(dir), "lapis")) lapis = true;
				if (quartz && lapis) return true;
			}
			return false;
		}
	}
	
	public static class Silver extends TileSaltFissionHeater {
		
		public Silver() {
			super("silver", 23);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte glowstone = 0;
			boolean tin = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (glowstone < 2 && isActiveHeater(pos.offset(dir), "glowstone")) glowstone++;
				if (!tin && isActiveHeater(pos.offset(dir), "tin")) tin = true;
				if (glowstone >= 2 && tin) return true;
			}
			return false;
		}
	}
	
	public static class Fluorite extends TileSaltFissionHeater {
		
		public Fluorite() {
			super("fluorite", 24);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean gold = false, prismarine = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!gold && isActiveHeater(pos.offset(dir), "gold")) gold = true;
				if (!prismarine && isActiveHeater(pos.offset(dir), "prismarine")) prismarine = true;
				if (gold && prismarine) return true;
			}
			return false;
		}
	}
	
	public static class Villiaumite extends TileSaltFissionHeater {
		
		public Villiaumite() {
			super("villiaumite", 25);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean redstone = false, endStone = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!redstone && isActiveHeater(pos.offset(dir), "redstone")) redstone = true;
				if (!endStone && isActiveHeater(pos.offset(dir), "end_stone")) endStone = true;
				if (redstone && endStone) return true;
			}
			return false;
		}
	}
	
	public static class Carobbiite extends TileSaltFissionHeater {
		
		public Carobbiite() {
			super("carobbiite", 26);
		}
		
		@Override
		public boolean isHeaterValid() {
			boolean endStone = false, copper = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!endStone && isActiveHeater(pos.offset(dir), "end_stone")) endStone = true;
				if (!copper && isActiveHeater(pos.offset(dir), "copper")) copper = true;
				if (endStone && copper) return true;
			}
			return false;
		}
	}
	
	public static class Arsenic extends TileSaltFissionHeater {
		
		public Arsenic() {
			super("arsenic", 27);
		}
		
		@Override
		public boolean isHeaterValid() {
			axialDirsLoop: for (EnumFacing[] axialDirs : BlockPosHelper.axialDirsList()) {
				for (EnumFacing dir : axialDirs) {
					if (!isActiveReflector(pos.offset(dir))) continue axialDirsLoop;
				}
				return true;
			}
			return false;
		}
	}
	
	public static class LiquidNitrogen extends TileSaltFissionHeater {
		
		public LiquidNitrogen() {
			super("liquid_nitrogen", 28);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte copper = 0;
			boolean purpur = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (copper < 2 && isActiveHeater(pos.offset(dir), "copper")) copper++;
				if (!purpur && isActiveHeater(pos.offset(dir), "purpur")) purpur = true;
				if (copper >= 2 && purpur) return true;
			}
			return false;
		}
	}
	
	public static class LiquidHelium extends TileSaltFissionHeater {
		
		public LiquidHelium() {
			super("liquid_helium", 29);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte redstone = 0;
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveHeater(pos.offset(dir), "redstone")) redstone++;
				if (redstone > 2) return false;
				if (!wall && isWall(pos.offset(dir))) wall = true;
			}
			return redstone == 2 && wall;
		}
	}
	
	public static class Enderium extends TileSaltFissionHeater {
		
		public Enderium() {
			super("enderium", 30);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte moderators = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveModerator(pos.offset(dir))) moderators++;
				if (moderators >= 3) return true;
			}
			return false;
		}
	}
	
	public static class Cryotheum extends TileSaltFissionHeater {
		
		public Cryotheum() {
			super("cryotheum", 31);
		}
		
		@Override
		public boolean isHeaterValid() {
			byte cell = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isActiveVessel(pos.offset(dir))) cell++;
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
		if (!isProcessing(false, false)) return false;
		else if (isInValidPosition) return true;
		return isInValidPosition = isHeaterValid();
	}
	
	@Override
	public boolean isFunctional() {
		return isProcessing;
	}
	
	public abstract boolean isHeaterValid();
	
	@Override
	public void resetStats() {
		isInValidPosition = false;
		heatingSpeedMultiplier = 0;
		
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true, true);
	}
	
	@Override
	public boolean isClusterRoot() {
		return false;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache) {
		refreshRecipe();
		refreshActivity();
		
		IFissionCoolingComponent.super.clusterSearch(id, clusterSearchCache);
		
		refreshIsProcessing(true, true);
	}
	
	public void refreshIsProcessing(boolean checkCluster, boolean checkValid) {
		isProcessing = isProcessing(checkCluster, checkValid);
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
		return baseProcessCooling;
	}
	
	@Override
	public ModeratorBlockInfo getModeratorBlockInfo(EnumFacing dir, boolean activeModeratorPos) {
		return new ModeratorBlockInfo(pos, this, false, false, 0, 0D);
	}
	
	// IFissionPortTarget
	
	@Override
	public BlockPos getMasterPortPos() {
		return masterPortPos;
	}
	
	@Override
	public void setMasterPortPos(BlockPos pos) {
		masterPortPos = pos;
	}
	
	@Override
	public void clearMasterPort() {
		masterPort = null;
		masterPortPos = DEFAULT_NON;
	}
	
	@Override
	public void refreshMasterPort() {
		masterPort = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionHeaterPort.class).get(masterPortPos.toLong());
		if (masterPort == null) masterPortPos = DEFAULT_NON;
	}
	
	@Override
	public boolean onPortRefresh() {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(isFunctional(), true);
		
		return isFunctional() ^ readyToProcess(false, true);
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			refreshIsProcessing(true, true);
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateHeater();
	}
	
	public void updateHeater() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing(true, true);
			boolean shouldRefresh = isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false, true);
			boolean shouldUpdate = wasProcessing != isProcessing;
			
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
			
			//tickHeater();
			//if (heaterCount == 0) pushFluid();
			
			if (shouldRefresh) {
				getMultiblock().refreshFlag = true;
			}

			sendUpdateToListeningPlayers();
			if (shouldUpdate) markDirty();
		}
	}
	
	/*public void tickHeater() {
		heaterCount++; heaterCount %= NCConfig.machine_update_rate / 2;
	}*/
	
	@Override
	public void refreshRecipe() {
		recipeInfo = coolant_heater.getRecipeInfoFromInputs(new ArrayList<>(), getFluidInputs());
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getSpeedMultiplier() {
		return heatingSpeedMultiplier;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessCooling = 0;
			return false;
		}
		baseProcessCooling = recipeInfo.getRecipe().getCoolantHeaterCoolingRate();
		return true;
	}
	
	// Processing
	
	public boolean isProcessing(boolean checkCluster, boolean checkValid) {
		return readyToProcess(checkCluster, checkValid);
	}
	
	public boolean readyToProcess(boolean checkCluster, boolean checkValid) {
		return canProcessInputs && isMultiblockAssembled() && !(checkCluster && cluster == null) && !(checkValid && !isInValidPosition);
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (!canProcess) {
			time = 0D;
		}
		return canProcess;
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) continue;
			if (fluidProduct.getStack() == null) return false;
			else if (!getTanks().get(j + fluidInputSize).isEmpty()) {
				if (!getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				} else if (getTanks().get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize(0) > getTanks().get(j + fluidInputSize).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		while (time >= 1D) finishProcess();
	}
	
	public void finishProcess() {
		int oldProcessCooling = baseProcessCooling;
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - 1D);
		refreshActivityOnProduction();
		if (!canProcessInputs) time = 0;
		
		if (getMultiblock() != null) {
			if (canProcessInputs) {
				if (oldProcessCooling != baseProcessCooling) {
					getMultiblock().refreshCluster(cluster);
				}
			}
			else {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	public void produceProducts() {
		if (recipeInfo == null) return;
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) getTanks().get(i).changeFluidAmount(-fluidIngredientStackSize);
			if (getTanks().get(i).getFluidAmount() <= 0) getTanks().get(i).setFluidStored(null);
		}
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) continue;
			if (getTanks().get(j + fluidInputSize).isEmpty()) {
				getTanks().get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			} else if (getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				getTanks().get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
			}
		}
	}
	
	// IProcessor
	
	@Override
	public int getFluidInputSize() {
		return fluidInputSize;
	}
	
	@Override
	public int getFluidOutputputSize() {
		return fluidOutputSize;
	}
	
	@Override
	public List<Tank> getFluidInputs() {
		return getTanks().subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.getRecipe().getFluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.getRecipe().getFluidProducts();
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
	}

	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}

	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFluidSides() : fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getGasWrapper() : gasWrapper;
	}
	
	public @Nonnull SaltFissionHeaterSetting[] getHeaterSettings() {
		return heaterSettings;
	}
	
	public void setHeaterSettings(@Nonnull SaltFissionHeaterSetting[] settings) {
		heaterSettings = settings;
	}
	
	public SaltFissionHeaterSetting getHeaterSetting(@Nonnull EnumFacing side) {
		return heaterSettings[side.getIndex()];
	}
	
	public void setHeaterSetting(@Nonnull EnumFacing side, @Nonnull SaltFissionHeaterSetting setting) {
		heaterSettings[side.getIndex()] = setting;
	}
	
	public void toggleHeaterSetting(@Nonnull EnumFacing side) {
		setHeaterSetting(side, getHeaterSetting(side).next());
		refreshFluidConnections(side);
		markDirtyAndNotify();
	}
	
	public void refreshFluidConnections(@Nonnull EnumFacing side) {
		switch (getHeaterSetting(side)) {
		case DISABLED:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		case DEFAULT:
			setTankSorption(side, 0, TankSorption.IN);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		case HOT_COOLANT_OUT:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.OUT);
			break;
		case COOLANT_SPREAD:
			setTankSorption(side, 0, TankSorption.OUT);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		default:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		}
	}
	
	//TODO
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {/*
		SaltFissionHeaterSetting thisSetting = getHeaterSetting(side);
		if (thisSetting == SaltFissionHeaterSetting.DISABLED) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileSaltFissionHeater) {
			TileSaltFissionHeater heater = (TileSaltFissionHeater)tile;
			SaltFissionHeaterSetting heaterSetting = heater.getHeaterSetting(side.getOpposite());
			
			if (thisSetting == SaltFissionHeaterSetting.COOLANT_SPREAD) {
				if (heaterSetting == SaltFissionHeaterSetting.DEFAULT) {
					pushCoolant(heater);
					pushHotCoolant(heater);
				} else if (heaterSetting == SaltFissionHeaterSetting.HOT_COOLANT_OUT) {
					pushCoolant(heater);
				}
			} else if (thisSetting == SaltFissionHeaterSetting.HOT_COOLANT_OUT && (heaterSetting == SaltFissionHeaterSetting.DEFAULT || heaterSetting == SaltFissionHeaterSetting.COOLANT_SPREAD)) {
				pushHotCoolant(heater);
			}
		}
		
		else if (thisSetting == SaltFissionHeaterSetting.HOT_COOLANT_OUT) {
			if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
			IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			
			if (adjStorage == null) return;
			
			for (int i = 0; i < getTanks().size(); i++) {
				if (getTanks().get(i).getFluid() == null || !getTankSorption(side, i).canDrain()) continue;
				
				getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
			}
		}
	*/}
	
	public void pushCoolant(TileSaltFissionHeater other) {
		int diff = getTanks().get(0).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
		if (diff > 1) {
			getTanks().get(0).drain(other.getTanks().get(0).fillInternal(getTanks().get(0).drain(diff/2, false), true), true);
		}
	}
	
	public void pushHotCoolant(TileSaltFissionHeater other) {
		getTanks().get(1).drain(other.getTanks().get(1).fillInternal(getTanks().get(1).drain(getTanks().get(1).getCapacity(), false), true), true);
	}

	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}

	@Override
	public void setInputTanksSeparated(boolean separated) {}

	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}

	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}

	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return true;
	}
	
	// ITileFilteredFluid
	
	@Override
	public @Nonnull List<Tank> getTanksInternal() {
		return tanks;
	}
	
	@Override
	public @Nonnull List<Tank> getFilterTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
	}
	
	@Override
	public boolean canModifyFilter(int tank) {
		return getMultiblock() != null ? !getMultiblock().isAssembled() : true;
	}
	
	@Override
	public void onFilterChanged(int slot) {
		/*if (!canModifyFilter(slot)) {
			getMultiblock().getLogic().refreshPorts();
		}*/
		markDirty();
	}
	
	@Override
	public int getFilterID() {
		return coolantName.hashCode();
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 203;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public SaltFissionHeaterUpdatePacket getGuiUpdatePacket() {
		return new SaltFissionHeaterUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks(), cluster, isProcessing, time);
	}
	
	@Override
	public void onGuiPacket(SaltFissionHeaterUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); i++) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); i++) {
			getFilterTanks().get(i).readInfo(message.filterTanksInfo.get(i));
		}
		clusterHeatStored = message.clusterHeatStored;
		clusterHeatCapacity = message.clusterHeatCapacity;
		isProcessing = message.isProcessing;
		time = message.time;
	}
	
	// NBT
	
	public NBTTagCompound writeHeaterSettings(NBTTagCompound nbt) {
		NBTTagCompound settingsTag = new NBTTagCompound();
		for (EnumFacing side : EnumFacing.VALUES) {
			settingsTag.setInteger("setting" + side.getIndex(), getHeaterSetting(side).ordinal());
		}
		nbt.setTag("heaterSettings", settingsTag);
		return nbt;
	}
	
	public void readHeaterSettings(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidConnections0")) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TankSorption sorption = TankSorption.values()[nbt.getInteger("fluidConnections" + side.getIndex())];
				switch (sorption) {
				case NON:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.NON);
					setHeaterSetting(side, SaltFissionHeaterSetting.DISABLED);
					break;
				case BOTH:
					setTankSorption(side, 0, TankSorption.IN);
					setTankSorption(side, 1, TankSorption.NON);
					setHeaterSetting(side, SaltFissionHeaterSetting.DEFAULT);
					break;
				case IN:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.OUT);
					setHeaterSetting(side, SaltFissionHeaterSetting.HOT_COOLANT_OUT);
					break;
				case OUT:
					setTankSorption(side, 0, TankSorption.OUT);
					setTankSorption(side, 1, TankSorption.NON);
					setHeaterSetting(side, SaltFissionHeaterSetting.COOLANT_SPREAD);
					break;
				default:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.NON);
					setHeaterSetting(side, SaltFissionHeaterSetting.DISABLED);
					break;
				}
			}
		}
		else {
			NBTTagCompound settingsTag = nbt.getCompoundTag("heaterSettings");
			for (EnumFacing side : EnumFacing.VALUES) {
				setHeaterSetting(side, SaltFissionHeaterSetting.values()[settingsTag.getInteger("setting" + side.getIndex())]);
				refreshFluidConnections(side);
			}
		}
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTanks(nbt);
		writeHeaterSettings(nbt);
		
		nbt.setInteger("baseProcessCooling", baseProcessCooling);
		nbt.setDouble("heatingSpeedMultiplier", heatingSpeedMultiplier);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		
		nbt.setLong("clusterHeat", heat);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readHeaterSettings(nbt);
		
		baseProcessCooling = nbt.getInteger("baseProcessCooling");
		heatingSpeedMultiplier = nbt.getDouble("heatingSpeedMultiplier");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		heat = nbt.getLong("clusterHeat");
		isInValidPosition = nbt.getBoolean("isInValidPosition");
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).writeToNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); i++) {
			filterTanks.get(i).writeToNBT(nbt, "filterTanks" + i);
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).readFromNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); i++) {
			filterTanks.get(i).readFromNBT(nbt, "filterTanks" + i);
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && NCConfig.enable_mek_gas && capability == GasHelper.GAS_HANDLER_CAPABILITY)) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getFluidSide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == GasHelper.GAS_HANDLER_CAPABILITY) {
			if (NCConfig.enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getGasWrapper();
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
