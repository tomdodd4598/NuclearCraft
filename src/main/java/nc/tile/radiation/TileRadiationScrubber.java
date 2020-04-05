package nc.tile.radiation;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.radiation.RadiationHelper;
import nc.radiation.environment.RadiationEnvironmentHandler;
import nc.radiation.environment.RadiationEnvironmentInfo;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.tile.generator.TileItemFluidGenerator;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.FourPos;
import nc.util.MaterialHelper;
import nc.util.NCMath;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileRadiationScrubber extends TileItemFluidGenerator implements ITileRadiationEnvironment, SimpleComponent {
	
	private double efficiency = 0D, scrubberFraction = 0D, currentChunkLevel = 0D, currentChunkBuffer = 0D;
	
	public final ConcurrentMap<BlockPos, Integer> occlusionMap = new ConcurrentHashMap<>();
	
	private int radCheckCount = 0;
	
	public TileRadiationScrubber() {
		super("radiation_scrubber", 1, 1, 1, 1, 0, defaultItemSorptions(1, 1), defaultTankCapacities(32000, 1, 1), defaultTankSorptions(1, 1), NCRecipes.radiation_scrubber_valid_fluids, maxPower(), NCRecipes.radiation_scrubber);
		setEnergyConnectionAll(EnergyConnection.IN);
	}
	
	private static int maxPower() {
		int max = 0;
		List<ProcessorRecipe> recipes = NCRecipes.radiation_scrubber.getRecipeList();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null) continue;
			max = Math.max(max, recipe.getScrubberProcessPower());
		}
		return 20*max;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if(!world.isRemote) {
			for (int x = -searchRadius(); x <= searchRadius(); x++) for (int y = -searchRadius(); y <= searchRadius(); y++) for (int z = -searchRadius(); z <= searchRadius(); z++) {
				RadiationEnvironmentHandler.addTile(getFourPos().add(x, y, z), this);
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			boolean wasProcessing = isProcessing, shouldUpdate = false;
			isProcessing = isProcessing();
			if (isProcessing) process();
			
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				updateBlockType();
			}
			
			tickRadCount();
			if (shouldUpdate || shouldRadCheck()) {
				checkRadiationEnvironmentInfo();
			}
			
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void updateGenerator() {}
	
	@Override
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = 1D;
			baseProcessPower = 0D;
			efficiency = 0D;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getScrubberProcessTime();
		baseProcessPower = recipeInfo.getRecipe().getScrubberProcessPower();
		efficiency = recipeInfo.getRecipe().getScrubberProcessEfficiency();
		return true;
	}
	
	public double getRawScrubberRate() {
		if (!isProcessing) {
			return 0D;
		}
		double rateMult = currentChunkBuffer + NCConfig.radiation_spread_rate*Math.max(0D, (currentChunkLevel - currentChunkBuffer));
		if (NCConfig.radiation_scrubber_non_linear) {
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos));
			if (chunkSource == null || chunkSource.getEffectiveScrubberCount() == 0D) return 0D;
			return -rateMult*scrubberFraction*chunkSource.getScrubbingFraction()/chunkSource.getEffectiveScrubberCount();
		}
		return -rateMult*scrubberFraction;
	}
	
	public void tickRadCount() {
		radCheckCount++; radCheckCount %= NCConfig.machine_update_rate*20;
	}
	
	public boolean shouldRadCheck() {
		return radCheckCount == 0;
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		RadiationEnvironmentHandler.removeTile(this);
	}
	
	// Processing
	
	@Override
	public boolean isProcessing() {
		return readyToProcess();
	}
	
	@Override
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed && hasSufficientEnergy();
	}
	
	public boolean hasSufficientEnergy() {
		return getEnergyStored() >= (int)baseProcessPower;
	}
	
	@Override
	public void process() {
		time++;
		getEnergyStorage().changeEnergyStored((int) -baseProcessPower);
		if (time >= baseProcessTime) finishProcess();
	}
	
	// IC2 Tiers
	
	@Override
	public int getEUSourceTier() {
		return 1;
	}
	
	@Override
	public int getEUSinkTier() {
		return 10;
	}
	
	// IRadiationEnvironmentHandler
	
	@Override
	public void checkRadiationEnvironmentInfo() {
		double newScrubberFraction = getMaxScrubberFraction();
		
		Iterator<Entry<BlockPos, Integer>> occlusionIterator = occlusionMap.entrySet().iterator();
		
		int occlusionCount = 0;
		double tileCount = 0D;
		while (occlusionIterator.hasNext()) {
			Entry<BlockPos, Integer> occlusion = occlusionIterator.next();
			
			if (isOcclusive(pos, world, occlusion.getKey())) {
				newScrubberFraction -= getOcclusionPenalty()/pos.distanceSq(occlusion.getKey());
				occlusionCount++;
				tileCount += Math.max(1D, Math.sqrt(occlusion.getValue()));
			}
			else occlusionIterator.remove();
		}
		
		scrubberFraction = efficiency*(occlusionCount == 0 ? getMaxScrubberFraction() : Math.max(0D, (newScrubberFraction*occlusionCount)/tileCount));
	}
	
	@Override
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info) {
		FourPos fourPos = getFourPos(), infoPos = info.pos;
		if (fourPos.getDimension() == infoPos.getDimension() && !fourPos.equals(infoPos) && !info.tileMap.isEmpty() /*&& isOcclusive(fourPos.getBlockPos(), world, infoPos.getBlockPos())*/) {
			occlusionMap.put(infoPos.getBlockPos(), Math.max(1, info.tileMap.size()));
		}
	}
	
	@Override
	public double getRadiationContributionFraction() {
		return isProcessing ? -scrubberFraction : 0D;
	}
	
	@Override
	public double getCurrentChunkRadiationLevel() {
		return currentChunkLevel;
	}
	
	@Override
	public void setCurrentChunkRadiationLevel(double level) {
		currentChunkLevel = level;
	}
	
	@Override
	public double getCurrentChunkRadiationBuffer() {
		return currentChunkBuffer;
	}
	
	@Override
	public void setCurrentChunkRadiationBuffer(double buffer) {
		currentChunkBuffer = buffer;
	}
	
	public static double getMaxScrubberFraction() {
		return NCConfig.radiation_scrubber_non_linear ? 1D : NCConfig.radiation_scrubber_fraction;
	}
	
	private static double getOcclusionPenalty() {
		return getMaxScrubberFraction()/52D;
	}
	
	private static int searchRadius() {
		return NCConfig.radiation_scrubber_radius;
	}
	
	// Helper
	
	// All opaque blocks plus translucent full blocks are occlusive
	private static boolean isOcclusive(BlockPos pos, World world, BlockPos otherPos) {
		IBlockState state = world.getBlockState(otherPos);
		return pos.distanceSq(otherPos) < NCMath.sq(searchRadius()) && !MaterialHelper.isEmpty(state.getMaterial()) && (state.isOpaqueCube() || !state.getMaterial().isOpaque());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof TileRadiationScrubber)) return false;
		return getFourPos().equals(((TileRadiationScrubber)obj).getFourPos());
	}
	
	// NBT
	
	@Override
	public boolean shouldSaveRadiation() {
		return false;
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("baseProcessPower", baseProcessPower);
		nbt.setDouble("efficiency", efficiency);
		
		nbt.setDouble("scrubberFraction", scrubberFraction);
		nbt.setDouble("currentChunkLevel", currentChunkLevel);
		nbt.setDouble("currentChunkBuffer", currentChunkBuffer);
		
		int count = 0;
		for (Entry<BlockPos, Integer> occlusion : occlusionMap.entrySet()) {
			BlockPos pos = occlusion.getKey();
			nbt.setIntArray("occlusion" + count, new int[] {occlusion.getValue(), pos.getX(), pos.getY(), pos.getZ()});
			count++;
		}
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessPower = nbt.getDouble("baseProcessPower");
		efficiency = nbt.getDouble("efficiency");
		
		scrubberFraction = nbt.getDouble("scrubberFraction");
		currentChunkLevel = nbt.getDouble("currentChunkLevel");
		currentChunkBuffer = nbt.getDouble("currentChunkBuffer");
		
		for (String key : nbt.getKeySet()) {
			if (key.startsWith("occlusion")) {
				int[] data = nbt.getIntArray(key);
				if (data.length < 4) continue;
				occlusionMap.put(new BlockPos(data[1], data[2], data[3]), data[0]);
			}
		}
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_radiation_scrubber";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getRadiationRemovalRate(Context context, Arguments args) {
		return new Object[] {getRawScrubberRate()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEfficiency(Context context, Arguments args) {
		return new Object[] {Math.abs(100D*getRadiationContributionFraction()/getMaxScrubberFraction())};
	}
}
