package nc.tile.radiation;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.config.NCConfig;
import nc.radiation.environment.RadiationEnvironmentHandler;
import nc.radiation.environment.RadiationEnvironmentInfo;
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
	
	private static final int RADIUS = 5;
	
	private static final double OCCLUSION_PENALTY_FRACTION = NCConfig.radiation_scrubber_fraction/52D;
	public static final double MAX_SCRUBBER_RATE_FRACTION = NCConfig.radiation_scrubber_fraction;
	
	private double scrubberRateFraction = 0D;
	public double rawScrubberRate = 0D;
	private double currentChunkBuffer = 0D;
	
	public final Map<BlockPos, Integer> occlusionMap = new ConcurrentHashMap<BlockPos, Integer>();
	
	private int radCheckCount = 0;
	
	public TileRadiationScrubber() {
		super("radiation_scrubber", new OreIngredient("dustBorax", 1), -NCConfig.radiation_scrubber_borax_rate, -NCConfig.radiation_scrubber_power, NCConfig.machine_update_rate / 5);
		stackChange = new OreIngredient("dustBorax", MathHelper.abs(itemChange)*NCConfig.machine_update_rate / 5);
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if(!world.isRemote) {
			for (int x = -RADIUS; x <= RADIUS; x++) for (int y = -RADIUS; y <= RADIUS; y++) for (int z = -RADIUS; z <= RADIUS; z++) {
				RadiationEnvironmentHandler.addTile(pos.add(x, y, z), this);
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			tickTile();
			tickRadCount();
			if(shouldRadCheck()) checkSurroundings();
		}
	}
	
	public void checkSurroundings() {
		checkRadiationEnvironmentInfo();
		if (!isActive) rawScrubberRate = 0D;
		else {
			rawScrubberRate = -currentChunkBuffer*scrubberRateFraction;
		}
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
	
	// IRadiationEnvironmentHandler
	
	@Override
	public void checkRadiationEnvironmentInfo() {
		double newScrubberRateFraction = MAX_SCRUBBER_RATE_FRACTION;
		
		Iterator<Entry<BlockPos, Integer>> occlusionIterator = occlusionMap.entrySet().iterator();
		
		int occlusionCount = 0;
		double tileCount = 0D;
		while (occlusionIterator.hasNext()) {
			Entry<BlockPos, Integer> occlusion = occlusionIterator.next();
			
			if (isOcclusive(pos, world, occlusion.getKey())) {
				newScrubberRateFraction -= OCCLUSION_PENALTY_FRACTION/pos.distanceSq(occlusion.getKey());
				occlusionCount++;
				tileCount += Math.max(1D, Math.sqrt(occlusion.getValue()));
			}
			else occlusionIterator.remove();
		}
		
		scrubberRateFraction = occlusionCount == 0 ? MAX_SCRUBBER_RATE_FRACTION : Math.max(0D, (newScrubberRateFraction*occlusionCount)/tileCount);
	}
	
	@Override
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info) {
		BlockPos blockPos = info.pos;
		if (!pos.equals(blockPos) && isOcclusive(pos, world, blockPos)) {
			occlusionMap.put(blockPos, Math.max(1, info.tileMap.size()));
		}
	}
	
	@Override
	public double getChunkBufferContributionFraction() {
		return isActive ? -scrubberRateFraction : 0D;
	}
	
	@Override
	public double getCurrentChunkBuffer() {
		return currentChunkBuffer;
	}
	
	@Override
	public void setCurrentChunkBuffer(double buffer) {
		currentChunkBuffer = buffer;
	}
	
	// Helper
	
	private static boolean isOcclusive(BlockPos pos, World world, BlockPos otherPos) {
		return pos.distanceSq(otherPos) < NCMath.square(RADIUS) && !MaterialHelper.isEmpty(world.getBlockState(otherPos).getMaterial());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof TileRadiationScrubber)) return false;
		return pos.equals(((TileRadiationScrubber)obj).pos);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("scrubberRate", scrubberRateFraction);
		nbt.setDouble("rawScrubberRate", rawScrubberRate);
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
		scrubberRateFraction = nbt.getDouble("scrubberRate");
		rawScrubberRate = nbt.getDouble("rawScrubberRate");
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
		return new Object[] {rawScrubberRate};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEfficiency(Context context, Arguments args) {
		return new Object[] {Math.abs(100D*getChunkBufferContributionFraction()/MAX_SCRUBBER_RATE_FRACTION)};
	}
}
