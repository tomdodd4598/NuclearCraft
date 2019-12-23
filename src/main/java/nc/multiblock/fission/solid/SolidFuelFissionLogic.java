package nc.multiblock.fission.solid;

import static nc.block.property.BlockProperties.ACTIVE;
import static nc.block.property.BlockProperties.FACING_ALL;
import static nc.recipe.NCRecipes.fission_heating;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.Multiblock;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerMultiblockController;
import nc.multiblock.container.ContainerSolidFissionController;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.FissionReactorLogic;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.multiblock.network.FissionUpdatePacket;
import nc.multiblock.network.SolidFissionUpdatePacket;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.fluid.Tank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SolidFuelFissionLogic extends FissionReactorLogic {
	
	public List<Tank> tanks = Lists.newArrayList(new Tank(FissionReactor.BASE_TANK_CAPACITY, NCRecipes.fission_heating_valid_fluids.get(0)), new Tank(FissionReactor.BASE_TANK_CAPACITY, null));
	
	public RecipeInfo<ProcessorRecipe> heatingRecipeInfo;
	
	public int heatingOutputRate = 0;
	public double sparsityEfficiencyMult = 0D, effectiveHeating = 0D, reservedEffectiveHeat = 0D, heatingRecipeRate = 0D, heatingOutputRateFP = 0D;
	
	public SolidFuelFissionLogic(FissionReactorLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public void onResetStats() {
		heatingOutputRate = 0;
		sparsityEfficiencyMult = effectiveHeating = heatingRecipeRate = heatingOutputRateFP = 0D;
	}
	
	@Override
	public void onReactorFormed() {
		tanks.get(0).setCapacity(FissionReactor.BASE_TANK_CAPACITY*getCapacityMultiplier());
		tanks.get(1).setCapacity(FissionReactor.BASE_TANK_CAPACITY*getCapacityMultiplier());
		
		super.onReactorFormed();
	}
	
	@Override
	public boolean isMachineWhole(Multiblock multiblock) {
		if (getPartMap(TileSaltFissionVessel.class).size() != 0) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_vessels", null);
			return false;
		}
		if (getPartMap(TileSaltFissionHeater.class).size() != 0) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_heaters", null);
			return false;
		}
		return true;
	}
	
	@Override
	public void distributeFlux(final ObjectSet<IFissionFuelComponent> primedCache, final ObjectSet<IFissionFuelComponent> primedFailureCache) {
		for (TileFissionSource source : getPartMap(TileFissionSource.class).values()) {
			IBlockState state = getWorld().getBlockState(source.getPos());
			EnumFacing facing = source.getPartPosition().getFacing();
			source.refreshIsRedstonePowered(getWorld(), source.getPos());
			getWorld().setBlockState(source.getPos(), state.withProperty(FACING_ALL, facing != null ? facing : state.getValue(FACING_ALL)).withProperty(ACTIVE, source.getIsRedstonePowered()), 3);
			
			if (!source.getIsRedstonePowered()) continue;
			PrimingTargetInfo targetInfo = source.getPrimingTarget();
			if (targetInfo == null) continue;
			IFissionFuelComponent fuelComponent = targetInfo.fuelComponent;
			if (fuelComponent == null || primedFailureCache.contains(fuelComponent)) continue;
			
			fuelComponent.tryPriming(getReactor());
			if (fuelComponent.isPrimed()) {
				primedCache.add(fuelComponent);
			}
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			iterateFluxSearch(primedComponent);
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			primedComponent.refreshIsProcessing(false);
			primedComponent.refreshLocal();
			primedComponent.unprime();
			
			if (!primedComponent.isFunctional()) {
				primedFailureCache.add(primedComponent);
				getReactor().refreshFlag = true;
			}
		}
	}
	
	@Override
	public void refreshClusters() {
		for (TileSolidFissionCell cell : getPartMap(TileSolidFissionCell.class).values()) {
			cell.refreshModerators();
		}
		
		getReactor().passiveModeratorCache.removeAll(getReactor().activeModeratorCache);
		
		for (TileSolidFissionCell cell : getPartMap(TileSolidFissionCell.class).values()) {
			iterateClusterSearch(cell);
		}
		
		for (long posLong : getReactor().activeModeratorCache) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(BlockPos.fromLong(posLong).offset(dir).toLong());
				if (component != null) iterateClusterSearch(component);
			}
		}
		
		for (long posLong : getReactor().activeReflectorCache) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(BlockPos.fromLong(posLong).offset(dir).toLong());
				if (component != null) iterateClusterSearch(component);
			}
		}
		
		super.refreshClusters();
	}
	
	@Override
	public void refreshReactorStats() {
		getReactor().resetStats();
		for (FissionCluster cluster : getReactor().getClusterMap().values()) {
			if (cluster.connectedToWall) {
				getReactor().usefulPartCount += cluster.componentCount;
				getReactor().fuelComponentCount += cluster.fuelComponentCount;
				getReactor().cooling += cluster.cooling;
				getReactor().rawHeating += cluster.rawHeating;
				effectiveHeating += cluster.effectiveHeating;
				getReactor().totalHeatMult += cluster.totalHeatMult;
				getReactor().totalEfficiency += cluster.totalEfficiency;
			}
		}
		
		getReactor().usefulPartCount += getReactor().passiveModeratorCache.size() + getReactor().activeModeratorCache.size() + getReactor().activeReflectorCache.size();
		double usefulPartRatio = (double)getReactor().usefulPartCount/(double)getReactor().getInteriorVolume();
		sparsityEfficiencyMult = usefulPartRatio >= NCConfig.fission_sparsity_penalty_params[1] ? 1D : (1D - NCConfig.fission_sparsity_penalty_params[0])*Math.sin(usefulPartRatio*Math.PI/(2D*NCConfig.fission_sparsity_penalty_params[1])) + NCConfig.fission_sparsity_penalty_params[0];
		effectiveHeating *= sparsityEfficiencyMult;
		getReactor().totalEfficiency *= sparsityEfficiencyMult;
		getReactor().meanHeatMult = getReactor().fuelComponentCount == 0 ? 0D : (double)getReactor().totalHeatMult/(double)getReactor().fuelComponentCount;
		getReactor().meanEfficiency = getReactor().fuelComponentCount == 0 ? 0D : getReactor().totalEfficiency/getReactor().fuelComponentCount;
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		getReactor().heatBuffer.changeHeatStored(getReactor().rawHeating);
		updateFluidHeating();
		
		if (getReactor().heatBuffer.isFull() && NCConfig.fission_overheat) {
			getReactor().heatBuffer.setHeatStored(0);
			reservedEffectiveHeat = 0D;
			doMeltdown();
			return true;
		}
		
		return super.onUpdateServer();
	}
	
	public void updateFluidHeating() {
		if (getReactor().isReactorOn) {
			refreshRecipe();
			if (canProcessInputs()) {
				produceProducts();
				return;
			}
		}
		heatingOutputRate = 0;
		heatingRecipeRate = heatingOutputRateFP = 0D;
	}
	
	public void refreshRecipe() {
		heatingRecipeInfo = fission_heating.getRecipeInfoFromInputs(new ArrayList<ItemStack>(), tanks.subList(0, 1));
	}
	
	public boolean canProcessInputs() {
		if (!getReactor().isReactorOn || !setRecipeStats()) return false;
		return canProduceProducts();
	}
	
	public boolean setRecipeStats() {
		if (heatingRecipeInfo == null) {
			heatingOutputRate = 0;
			heatingRecipeRate = heatingOutputRateFP = 0D;
			return false;
		}
		return true;
	}
	
	public boolean canProduceProducts() {
		ProcessorRecipe recipe = heatingRecipeInfo.getRecipe();
		IFluidIngredient fluidProduct = recipe.fluidProducts().get(0);
		int productSize = fluidProduct.getMaxStackSize(0);
		if (productSize <= 0 || fluidProduct.getStack() == null) return false;
		
		int heatPerMB = recipe.getFissionHeatingHeatPerInputMB();
		int inputSize = recipe.fluidIngredients().get(0).getMaxStackSize(heatingRecipeInfo.getFluidIngredientNumbers().get(0));
		
		double usedInput = Math.min(tanks.get(0).getFluidAmount(), getEffectiveHeat()/heatPerMB);
		heatingRecipeRate = heatingOutputRateFP = Math.min(Integer.MAX_VALUE, Math.min((tanks.get(1).getCapacity() - tanks.get(1).getFluidAmount())/productSize, usedInput/inputSize));
		reservedEffectiveHeat += (heatingRecipeRate - (int)heatingRecipeRate)*inputSize*heatPerMB;
		
		int extraRecipeRate = (int) Math.min(Integer.MAX_VALUE - heatingRecipeRate, reservedEffectiveHeat/(heatPerMB*inputSize));
		heatingRecipeRate += extraRecipeRate;
		reservedEffectiveHeat -= extraRecipeRate*inputSize*heatPerMB;
		
		return tanks.get(1).isEmpty() || tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack());
	}
	
	public void produceProducts() {
		ProcessorRecipe recipe = heatingRecipeInfo.getRecipe();
		int inputSize = recipe.fluidIngredients().get(0).getMaxStackSize(heatingRecipeInfo.getFluidIngredientNumbers().get(0));
		int heatingRecipeRateInt = (int)heatingRecipeRate;
		
		if (heatingRecipeRateInt*inputSize > 0) tanks.get(0).changeFluidAmount(-heatingRecipeRateInt*inputSize);
		if (tanks.get(0).getFluidAmount() <= 0) tanks.get(0).setFluidStored(null);
		
		IFluidIngredient fluidProduct = recipe.fluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) > 0) {
			int stackSize = 0;
			if (tanks.get(1).isEmpty()) {
				tanks.get(1).setFluidStored(fluidProduct.getNextStack(0));
				stackSize = tanks.get(1).getFluidAmount();
				heatingOutputRate = heatingRecipeRateInt*stackSize;
				tanks.get(1).setFluidAmount(heatingOutputRate);
			} else if (tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				stackSize = fluidProduct.getNextStackSize(0);
				heatingOutputRate = heatingRecipeRateInt*stackSize;
				tanks.get(1).changeFluidAmount(heatingOutputRate);
			}
			heatingOutputRateFP *= stackSize;
			if (heatingOutputRateFP > stackSize) heatingOutputRateFP = Math.round(heatingOutputRateFP);
		}
		
		long heatRemoval = (long) ((getReactor().rawHeating/effectiveHeating)*heatingRecipeRate*inputSize*recipe.getFissionHeatingHeatPerInputMB());
		getReactor().heatBuffer.changeHeatStored(-heatRemoval);
	}
	
	public double getEffectiveHeat() {
		return getReactor().rawHeating == 0L ? 0D : (effectiveHeating/getReactor().rawHeating)*getReactor().heatBuffer.getHeatStored();
	}
	
	public long getNetClusterHeating() {
		return getReactor().rawHeating - getReactor().cooling;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		if (getReactor().isReactorOn) {
			int i = getPartMap(TileSolidFissionCell.class).size();
			for (TileSolidFissionCell cell : getPartMap(TileSolidFissionCell.class).values()) {
				if (rand.nextDouble() < 1D/i && playFissionSound(cell)) return;
				if (cell.isFunctional()) i--;
			}
		}
	}
	
	// NBT
	
	@Override
	public void writeToNBT(NBTTagCompound data, SyncReason syncReason) {
		super.writeToNBT(data, syncReason);
		NBTTagCompound logicTag = new NBTTagCompound();
		writeTanks(tanks, logicTag);
		logicTag.setInteger("heatingOutputRate", heatingOutputRate);
		logicTag.setDouble("sparsityEfficiencyMult", sparsityEfficiencyMult);
		logicTag.setDouble("effectiveHeating", effectiveHeating);
		logicTag.setDouble("reservedEffectiveHeat", reservedEffectiveHeat);
		logicTag.setDouble("heatingOutputRateFP", heatingOutputRateFP);
		data.setTag("solid_fuel", logicTag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data, SyncReason syncReason) {
		super.readFromNBT(data, syncReason);
		if (data.hasKey("solid_fuel")) {
			NBTTagCompound logicTag = data.getCompoundTag("solid_fuel");
			readTanks(tanks, logicTag);
			heatingOutputRate = logicTag.getInteger("heatingOutputRate");
			sparsityEfficiencyMult = logicTag.getDouble("sparsityEfficiencyMult");
			effectiveHeating = logicTag.getDouble("effectiveHeating");
			reservedEffectiveHeat = logicTag.getDouble("reservedEffectiveHeat");
			heatingOutputRateFP = logicTag.getDouble("heatingOutputRateFP");
		}
	}
	
	// Packets
	
	@Override
	public SolidFissionUpdatePacket getUpdatePacket() {
		return new SolidFissionUpdatePacket(getReactor().controller.getTilePos(), getReactor().isReactorOn, getReactor().heatBuffer, getReactor().clusterCount, getReactor().cooling, getReactor().rawHeating, getReactor().totalHeatMult, getReactor().meanHeatMult, getReactor().fuelComponentCount, getReactor().usefulPartCount, getReactor().totalEfficiency, getReactor().meanEfficiency, effectiveHeating, heatingOutputRateFP, sparsityEfficiencyMult, reservedEffectiveHeat);
	}
	
	@Override
	public void onPacket(FissionUpdatePacket message) {
		super.onPacket(message);
		if (message instanceof SolidFissionUpdatePacket) {
			SolidFissionUpdatePacket packet = (SolidFissionUpdatePacket) message;
			effectiveHeating = packet.effectiveHeating;
			heatingOutputRateFP = packet.heatingOutputRateFP;
			sparsityEfficiencyMult = packet.sparsityEfficiencyMult;
			reservedEffectiveHeat = packet.reservedEffectiveHeat;
		}
	}
	
	@Override
	public ContainerMultiblockController<FissionReactor, IFissionController> getContainer(EntityPlayer player) {
		return new ContainerSolidFissionController(player, getReactor().controller);
	}
	
	@Override
	public void clearAllMaterial() {
		super.clearAllMaterial();
		for (Tank tank : tanks) {
			tank.setFluidStored(null);
		}
	}
}
