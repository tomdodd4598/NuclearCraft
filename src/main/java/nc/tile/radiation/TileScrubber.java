package nc.tile.radiation;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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

public class TileScrubber extends TilePassiveAbstract implements ITileRadiationEnvironment {
	
	private static final int RADIUS = 5;
	private static final int POWER_USE = 50;
	
	private static double occlusionPenalty = NCConfig.radiation_scrubber_rate/52D;
	public static double maxScrubberRate = NCConfig.radiation_scrubber_rate;
	
	private double scrubberRate = 0D;
	public final Map<BlockPos, Integer> occlusionMap = new ConcurrentHashMap<BlockPos, Integer>();
	
	private int radCheckCount = 0;
	
	public TileScrubber() {
		super("radiation_scrubber", new OreIngredient("dustBorax", 1), -NCConfig.radiation_scrubber_borax_rate, -POWER_USE*20, NCConfig.machine_update_rate / 5);
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
			if(shouldRadCheck()) checkRadiationEnvironmentInfo();
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
		double newScrubberRate = maxScrubberRate;
		
		Iterator<Entry<BlockPos, Integer>> occlusionIterator = occlusionMap.entrySet().iterator();
		
		int occlusionCount = 0;
		double tileCount = 0D;
		while (occlusionIterator.hasNext()) {
			Entry<BlockPos, Integer> occlusion = occlusionIterator.next();
			
			if (isOcclusive(pos, world, occlusion.getKey())) {
				newScrubberRate -= occlusionPenalty/pos.distanceSq(occlusion.getKey());
				occlusionCount++;
				tileCount += Math.max(1D, Math.sqrt(occlusion.getValue()));
			}
			else occlusionIterator.remove();
		}
		
		scrubberRate = occlusionCount == 0 ? maxScrubberRate : Math.max(0D, (newScrubberRate*occlusionCount)/tileCount);
	}
	
	@Override
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info) {
		BlockPos blockPos = info.pos;
		if (!pos.equals(blockPos) && isOcclusive(pos, world, blockPos)) {
			occlusionMap.put(blockPos, Math.max(1, info.tileMap.size()));
		}
	}
	
	@Override
	public double getChunkBufferContribution() {
		return isRunning ? -scrubberRate : 0D;
	}
	
	// Helper
	
	private static boolean isOcclusive(BlockPos pos, World world, BlockPos otherPos) {
		return pos.distanceSq(otherPos) < NCMath.square(RADIUS) && !MaterialHelper.isEmpty(world.getBlockState(otherPos).getMaterial());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof TileScrubber)) return false;
		return pos.equals(((TileScrubber)obj).pos);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("scrubberRate", scrubberRate);
		
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
		scrubberRate = nbt.getDouble("scrubberRate");
		
		for (String key : nbt.getKeySet()) {
			if (key.startsWith("occlusion")) {
				int[] data = nbt.getIntArray(key);
				if (data.length < 4) continue;
				occlusionMap.put(new BlockPos(data[1], data[2], data[3]), data[0]);
			}
		}
	}
}
