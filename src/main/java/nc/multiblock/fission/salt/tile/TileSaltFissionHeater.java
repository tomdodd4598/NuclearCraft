package nc.multiblock.fission.salt.tile;

import static nc.recipe.NCRecipes.coolant_heater;
import static nc.recipe.NCRecipes.coolant_heater_valid_fluids;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.SaltFissionHeaterSetting;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionCoolingComponent;
import nc.multiblock.fission.tile.TileFissionPart;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.ProcessorRecipe;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankOutputSetting;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.tile.processor.IFluidProcessor;
import nc.util.BlockPosHelper;
import nc.util.FluidStackHelper;
import nc.util.GasHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class TileSaltFissionHeater extends TileFissionPart implements IFluidProcessor, IFissionCoolingComponent {
	
	protected final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*2, coolant_heater_valid_fluids.get(0)), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*4, new ArrayList<>()));
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected @Nonnull SaltFissionHeaterSetting[] heaterSettings = new SaltFissionHeaterSetting[] {SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED, SaltFissionHeaterSetting.DISABLED};
	
	protected final int fluidInputSize = 1, fluidOutputSize = 1;
	
	protected double baseProcessCooling;
	protected double clusterCoolingEfficiency; // Based on the cluster efficiency, but with heat/cooling taken into account
	
	protected double time;
	protected boolean isProcessing, canProcessInputs;
	
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	public final String heaterName;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	protected boolean isInValidPosition = false;
	
	public TileSaltFissionHeater(String heaterName) {
		super(CuboidalPartPositionType.INTERIOR);
		this.heaterName = heaterName;
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	public static class Standard extends TileSaltFissionHeater {
		
		public Standard() {
			super("standard");
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
			super("iron");
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
			super("redstone");
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
			super("quartz");
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
			super("obsidian");
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
			super("nether_brick");
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
			super("glowstone");
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
			super("lapis");
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
			super("gold");
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
			super("prismarine");
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
			super("slime");
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
			super("end_stone");
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
			super("purpur");
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
			super("diamond");
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
			super("emerald");
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
			super("copper");
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
			super("tin");
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
			super("lead");
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
			super("boron");
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
			super("lithium");
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
			super("magnesium");
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
			super("manganese");
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
			super("aluminum");
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
			super("silver");
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
			super("fluorite");
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
			super("villiaumite");
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
			super("carobbiite");
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
			super("arsenic");
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
			super("liquid_nitrogen");
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
			super("liquid_helium");
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
			super("enderium");
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
			super("cryotheum");
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
		if (!isProcessing) return false;
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
		
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache) {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(false);
		
		IFissionCoolingComponent.super.clusterSearch(id, clusterSearchCache);
	}
	
	public void refreshIsProcessing(boolean checkCluster) {
		isProcessing = isProcessing(checkCluster);
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
		//TODO
		return (long) baseProcessCooling;
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			refreshIsProcessing(true);
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
			isProcessing = isProcessing(true);
			boolean shouldRefresh = !isProcessing && isProcessing(false);
			boolean shouldUpdate = wasProcessing != isProcessing;
			
			if (isProcessing) process();
			
			//tickHeater();
			//if (heaterCount == 0) pushFluid();
			
			if (shouldRefresh && isMultiblockAssembled()) {
				getMultiblock().refreshFlag = true;
			}
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
		return clusterCoolingEfficiency;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessCooling = 0D;
			return false;
		}
		baseProcessCooling = recipeInfo.getRecipe().getCoolantHeaterCoolingRate();
		return true;
	}
	
	// Processing
	
	public boolean isProcessing(boolean checkCluster) {
		return readyToProcess(checkCluster);
	}
	
	public boolean readyToProcess(boolean checkCluster) {
		return canProcessInputs && isMultiblockAssembled() && !(checkCluster && (cluster == null || !isInValidPosition));
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
			else if (!tanks.get(j + fluidInputSize).isEmpty()) {
				if (!tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				} else if (tanks.get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize(0) > tanks.get(j + fluidInputSize).getCapacity()) {
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
		double oldProcessCooling = baseProcessCooling;
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
			if (fluidIngredientStackSize > 0) tanks.get(i).changeFluidAmount(-fluidIngredientStackSize);
			if (tanks.get(i).getFluidAmount() <= 0) tanks.get(i).setFluidStored(null);
		}
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) continue;
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			} else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
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
		return tanks.subList(0, fluidInputSize);
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
	@Nonnull
	public List<Tank> getTanks() {
		return tanks;
	}

	@Override
	@Nonnull
	public FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}

	@Override
	@Nonnull
	public FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
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
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
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
	}
	
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
		
		nbt.setDouble("baseProcessCooling", baseProcessCooling);
		nbt.setDouble("clusterCoolingEfficiency", clusterCoolingEfficiency);
		
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
		
		baseProcessCooling = nbt.getDouble("baseProcessCooling");
		clusterCoolingEfficiency = nbt.getDouble("clusterCoolingEfficiency");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		heat = nbt.getLong("clusterHeat");
		isInValidPosition = nbt.getBoolean("isInValidPosition");
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
