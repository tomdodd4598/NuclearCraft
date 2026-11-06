package nc.tile.radiation;

import java.util.Map;
import java.util.WeakHashMap;

import com.google.common.collect.Iterables;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.radiation.RadiationHelper;
import nc.recipe.ingredient.OreIngredient;
import nc.tile.passive.TilePassiveAbstract;
import nc.util.MaterialHelper;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileRadiationScrubber extends TilePassiveAbstract implements ITileRadiationEnvironment, SimpleComponent {
	
	private double scrubberFraction = 0D, currentChunkLevel = 0D, currentChunkBuffer = 0D;
	
	private static final Map<World, Object2IntOpenHashMap<BlockPos>> occlusionMaps = new WeakHashMap<>();
	
	private int radCheckCount = 0, occlusionRadius = -1;
	
	public TileRadiationScrubber() {
		super("radiation_scrubber", new OreIngredient("dustBorax", 1), -NCConfig.radiation_scrubber_borax_rate, -NCConfig.radiation_scrubber_power, NCConfig.machine_update_rate / 5);
		stackChange = new OreIngredient("dustBorax", MathHelper.abs(itemChange)*NCConfig.machine_update_rate / 5);
	}
	
	private static Object2IntOpenHashMap<BlockPos> getOcclusionMap(World world) {
		return occlusionMaps.computeIfAbsent(world, x -> new Object2IntOpenHashMap<>());
	}
	
	private Iterable<BlockPos.MutableBlockPos> getAffectedPositions(int radius) {
		return Iterables.filter(BlockPos.getAllInBoxMutable(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius)), p -> pos.distanceSq(p) < NCMath.square(radius));
	}

	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote && occlusionRadius < 0) {
			occlusionRadius = searchRadius();
			Object2IntOpenHashMap<BlockPos> occlusionMap = getOcclusionMap(world);
			for (BlockPos p : getAffectedPositions(occlusionRadius)) {
				occlusionMap.addTo(p.toImmutable(), 1);
			}
		}
	}

	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			tickRadCount();
			if(shouldRadCheck()) {
				checkRadiationEnvironmentInfo();
			}
		}
	}
	
	public double getRawScrubberRate() {
		if (!isActive) {
			return 0D;
		}
		double rateMult = currentChunkBuffer + NCConfig.radiation_spread_rate*Math.max(0D, (currentChunkLevel - currentChunkBuffer));
		if (NCConfig.radiation_scrubber_alt) {
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
	public void onChunkUnload() {
		super.onChunkUnload();
		removeOcclusion();
		isAdded = false;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		removeOcclusion();
		isAdded = false;
	}

	private void removeOcclusion() {
		if (world == null || world.isRemote || occlusionRadius < 0) {
			return;
		}
		
		Object2IntOpenHashMap<BlockPos> occlusionMap = getOcclusionMap(world);
		for (BlockPos p : getAffectedPositions(occlusionRadius)) {
			if (occlusionMap.addTo(p, -1) <= 1) {
				occlusionMap.removeInt(p);
			}
		}
		occlusionRadius = -1;
	}

	// IRadiationEnvironmentHandler
	
	@Override
	public void checkRadiationEnvironmentInfo() {
		double newScrubberFraction = getMaxScrubberFraction();
		
		int occlusionCount = 0;
		double tileCount = 0D;
		Object2IntOpenHashMap<BlockPos> occlusionMap = getOcclusionMap(world);
		for (BlockPos p : getAffectedPositions(searchRadius())) {
			if (isOcclusive(pos, world, p)) {
				newScrubberFraction -= getOcclusionPenalty() / pos.distanceSq(p);
				++occlusionCount;
				tileCount += Math.max(1D, Math.sqrt(occlusionMap.getInt(p)));
			}
		}
		
		scrubberFraction = occlusionCount == 0 ? getMaxScrubberFraction() : Math.max(0D, (newScrubberFraction*occlusionCount)/tileCount);
	}
	
	@Override
	public double getRadiationContributionFraction() {
		return isActive ? -scrubberFraction : 0D;
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
		return NCConfig.radiation_scrubber_alt ? 1D : NCConfig.radiation_scrubber_fraction;
	}
	
	private static double getOcclusionPenalty() {
		return getMaxScrubberFraction()/52D;
	}
	
	private static int searchRadius() {
		return NCConfig.radiation_scrubber_radius;
	}
	
	// Helper
	
	private static boolean isOcclusive(BlockPos pos, World world, BlockPos otherPos) {
		return !pos.equals(otherPos) && !MaterialHelper.isEmpty(world.getBlockState(otherPos).getMaterial());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof TileRadiationScrubber)) return false;
		return getFourPos().equals(((TileRadiationScrubber)obj).getFourPos());
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("scrubberRate", scrubberFraction);
		nbt.setDouble("currentChunkLevel", currentChunkLevel);
		nbt.setDouble("currentChunkBuffer", currentChunkBuffer);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		scrubberFraction = nbt.getDouble("scrubberRate");
		currentChunkLevel = nbt.getDouble("currentChunkLevel");
		currentChunkBuffer = nbt.getDouble("currentChunkBuffer");
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
