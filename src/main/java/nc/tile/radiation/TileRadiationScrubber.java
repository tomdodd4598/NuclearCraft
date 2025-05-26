package nc.tile.radiation;

import li.cil.oc.api.machine.*;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.*;
import nc.radiation.environment.*;
import nc.recipe.BasicRecipe;
import nc.tile.processor.TileProcessorImpl.TileBasicEnergyProcessor;
import nc.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.*;

import static nc.config.NCConfig.*;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileRadiationScrubber extends TileBasicEnergyProcessor<TileRadiationScrubber> implements ITileRadiationEnvironment {
	
	private double efficiency = 0D, scrubberFraction = 0D, currentChunkLevel = 0D, currentChunkBuffer = 0D;
	
	public final ConcurrentMap<BlockPos, Integer> occlusionMap = new ConcurrentHashMap<>();
	
	private int radCheckCount = RadiationHandler.RAND.nextInt(machine_update_rate * 20);
	
	public TileRadiationScrubber() {
		super("radiation_scrubber");
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			for (int x = -radiation_scrubber_radius; x <= radiation_scrubber_radius; ++x) {
				for (int y = -radiation_scrubber_radius; y <= radiation_scrubber_radius; ++y) {
					for (int z = -radiation_scrubber_radius; z <= radiation_scrubber_radius; ++z) {
						RadiationEnvironmentHandler.addTile(getFourPos().add(x, y, z), this);
					}
				}
			}
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean shouldUpdate = onTick();
			
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
	public void setRecipeStats(@Nullable BasicRecipe recipe) {
		if (recipe == null) {
			baseProcessTime = 1D;
			baseProcessPower = 0D;
			efficiency = 0D;
		}
		else {
			baseProcessTime = recipe.getScrubberProcessTime();
			baseProcessPower = recipe.getScrubberProcessPower();
			efficiency = recipe.getScrubberProcessEfficiency();
		}
	}
	
	public double getRawScrubberRate() {
		if (!isProcessing) {
			return 0D;
		}
		double rateMult = currentChunkBuffer + radiation_spread_rate * Math.max(0D, currentChunkLevel - currentChunkBuffer);
		if (radiation_scrubber_non_linear) {
			IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos));
			if (chunkSource == null || chunkSource.getEffectiveScrubberCount() == 0D) {
				return 0D;
			}
			return -rateMult * scrubberFraction * chunkSource.getScrubbingFraction() / chunkSource.getEffectiveScrubberCount();
		}
		return -rateMult * scrubberFraction;
	}
	
	public void tickRadCount() {
		++radCheckCount;
		radCheckCount %= machine_update_rate * 20;
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
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed && hasSufficientEnergy();
	}
	
	@Override
	public boolean hasSufficientEnergy() {
		return getEnergyStoredLong() >= (long) baseProcessPower;
	}
	
	@Override
	public void process() {
		++time;
		getEnergyStorage().changeEnergyStored((long) -baseProcessPower);
		if (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
	// IC2 Tiers
	
	@Override
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		return 1;
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
				newScrubberFraction -= getOcclusionPenalty() / pos.distanceSq(occlusion.getKey());
				++occlusionCount;
				tileCount += Math.max(1D, Math.sqrt(occlusion.getValue()));
			}
			else {
				occlusionIterator.remove();
			}
		}
		
		scrubberFraction = efficiency * (occlusionCount == 0 ? getMaxScrubberFraction() : Math.max(0D, newScrubberFraction * occlusionCount / tileCount));
	}
	
	@Override
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info) {
		FourPos fourPos = getFourPos(), infoPos = info.pos;
		if (fourPos.getDimension() == infoPos.getDimension() && !fourPos.equals(infoPos) && !info.tileMap.isEmpty() /* && isOcclusive(fourPos.getBlockPos(), world, infoPos.getBlockPos()) */) {
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
		return radiation_scrubber_non_linear ? 1D : radiation_scrubber_fraction;
	}
	
	private static double getOcclusionPenalty() {
		return getMaxScrubberFraction() / 52D;
	}
	
	// Helper
	
	// All opaque blocks plus translucent full blocks are occlusive
	private static boolean isOcclusive(BlockPos pos, World world, BlockPos otherPos) {
		IBlockState state = world.getBlockState(otherPos);
		Material mat = state.getMaterial();
		return pos.distanceSq(otherPos) < NCMath.sq(radiation_scrubber_radius) && !MaterialHelper.isEmpty(mat) && !MaterialHelper.isFoliage(mat) && (!state.isOpaqueCube() || !mat.isOpaque());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof TileRadiationScrubber other)) {
			return false;
		}
		return getFourPos().equals(other.getFourPos());
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
		
		NBTHelper.writeBlockPosToIntegerMap(nbt, occlusionMap, "occlusionMap");
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
		
		NBTHelper.readBlockPosToIntegerMap(nbt, occlusionMap, "occlusionMap");
	}
	
	// OpenComputers
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getRadiationRemovalRate(Context context, Arguments args) {
		return new Object[] {getRawScrubberRate()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getEfficiency(Context context, Arguments args) {
		return new Object[] {Math.abs(100D * getRadiationContributionFraction() / getMaxScrubberFraction())};
	}
}
