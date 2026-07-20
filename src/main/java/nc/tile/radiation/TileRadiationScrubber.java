package nc.tile.radiation;

import com.google.common.collect.AbstractIterator;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import li.cil.oc.api.machine.*;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.*;
import nc.recipe.BasicRecipe;
import nc.tile.processor.TileProcessorImpl.TileBasicEnergyProcessor;
import nc.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;
import java.util.*;

import static nc.config.NCConfig.*;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileRadiationScrubber extends TileBasicEnergyProcessor<TileRadiationScrubber> implements ITileRadiationEnvironment {
	
	private double efficiency = 0D, scrubberFraction = 0D, currentChunkLevel = 0D, currentChunkBuffer = 0D;
	
	private static final Map<World, Object2IntOpenHashMap<BlockPos>> occlusionMaps = new WeakHashMap<>();
	
	private int radCheckCount = RadiationHandler.RAND.nextInt(machine_update_rate * 20);
	
	public TileRadiationScrubber() {
		super("radiation_scrubber");
	}
	
	private static Object2IntOpenHashMap<BlockPos> occlusionMap(World world) {
		return occlusionMaps.computeIfAbsent(world, k -> new Object2IntOpenHashMap<>());
	}
	
	private Iterable<BlockPos> affectedPositions() {
		return () -> new AbstractIterator<BlockPos>() {
			private final MutableBlockPos mutable = new MutableBlockPos();
			private int x = -radiation_scrubber_radius;
			private int y = -radiation_scrubber_radius;
			private int z = -radiation_scrubber_radius;
			
			@Override
			protected BlockPos computeNext() {
				for (; x <= radiation_scrubber_radius; x++) {
					for (; y <= radiation_scrubber_radius; y++) {
						for (; z <= radiation_scrubber_radius; z++) {
							if (x * x + y * y + z * z < NCMath.sq(radiation_scrubber_radius)) {
								return mutable.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
							}
						}
					}
				}
				return endOfData();
			}
		};
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			Object2IntOpenHashMap<BlockPos> occlusionMap = occlusionMap(world);
			for (BlockPos p : affectedPositions()) {
				occlusionMap.addTo(p.toImmutable(), 1);
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
	public void onChunkUnload() {
		super.onChunkUnload();
		if (!world.isRemote) {
			Object2IntOpenHashMap<BlockPos> occlusionMap = occlusionMap(world);
			for (BlockPos p : affectedPositions()) {
				if (occlusionMap.addTo(p, -1) <= 1) {
					occlusionMap.removeInt(p);
				}
			}
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (!world.isRemote) {
			Object2IntOpenHashMap<BlockPos> occlusionMap = occlusionMap(world);
			for (BlockPos p : affectedPositions()) {
				if (occlusionMap.addTo(p, -1) <= 1) {
					occlusionMap.removeInt(p);
				}
			}
		}
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
		
		int occlusionCount = 0;
		double tileCount = 0D;
		Object2IntOpenHashMap<BlockPos> occlusionMap = occlusionMap(world);
		for (BlockPos p : affectedPositions()) {
			if (isOcclusive(pos, world, p)) {
				newScrubberFraction -= getOcclusionPenalty() / pos.distanceSq(p);
				++occlusionCount;
				tileCount += Math.max(1D, Math.sqrt(occlusionMap.getInt(p)));
			}
		}
		
		scrubberFraction = efficiency * (occlusionCount == 0 ? getMaxScrubberFraction() : Math.max(0D, newScrubberFraction * occlusionCount / tileCount));
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
		return !MaterialHelper.isEmpty(mat) && !MaterialHelper.isFoliage(mat) && (!state.isOpaqueCube() || !mat.isOpaque());
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
