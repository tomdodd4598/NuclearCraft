package nc.tile.radiation;

import java.util.Iterator;
import java.util.List;

import nc.config.NCConfig;
import nc.radiation.environment.RadiationEnvironmentHandler;
import nc.radiation.environment.RadiationEnvironmentInfo;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.TileEnergy;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.KeyPair;
import nc.util.MaterialHelper;
import nc.util.NCMath;
import nc.util.SetList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileScrubber extends TileEnergy implements IRadiationEnvironmentHandler {
	
	private static final int RADIUS = 5;
	private static final int POWER_USE = 50;
	
	private static double occlusionPenalty = NCConfig.radiation_scrubber_rate/52D;
	public static double maxScrubberRate = NCConfig.radiation_scrubber_rate;
	
	private double scrubberRate = 0D;
	public final List<KeyPair<BlockPos, Integer>> occlusionList = new SetList<KeyPair<BlockPos, Integer>>();
	
	public TileScrubber() {
		super(32000, ITileEnergy.energyConnectionAll(EnergyConnection.IN));
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if(!world.isRemote) {
			for (int x = -RADIUS; x <= RADIUS; x++) for (int y = -RADIUS; y <= RADIUS; y++) for (int z = -RADIUS; z <= RADIUS; z++) {
				RadiationEnvironmentHandler.addTile(this, pos.add(x, y, z));
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			getEnergyStorage().changeEnergyStored(-POWER_USE);
			tickTile();
			if(shouldTileCheck()) checkRadiationEnvironmentInfo();
		}
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate*20;
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
		
		Iterator<KeyPair<BlockPos, Integer>> occlusionIterator = occlusionList.iterator();
		
		int occlusionCount = 0;
		double tileCount = 0D;
		while (occlusionIterator.hasNext()) {
			KeyPair<BlockPos, Integer> occlusion = occlusionIterator.next();
			
			if (isOcclusive(pos, world, occlusion.getLeft())) {
				newScrubberRate -= occlusionPenalty/pos.distanceSq(occlusion.getLeft());
				occlusionCount++;
				tileCount += Math.max(1D, Math.sqrt(occlusion.getRight()));
			}
			else occlusionIterator.remove();
		}
		
		scrubberRate = occlusionCount == 0 ? maxScrubberRate : Math.max(0D, (newScrubberRate*occlusionCount)/tileCount);
	}
	
	@Override
	public void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info) {
		BlockPos blockPos = info.pos;
		if (!pos.equals(blockPos) && isOcclusive(pos, world, blockPos)) {
			KeyPair newOcclusion = KeyPair.of(blockPos, Math.max(1, info.tileList.size()));
			int index = occlusionList.indexOf(newOcclusion);
			if (index < 0) {
				occlusionList.add(newOcclusion);
			} else {
				occlusionList.set(index, newOcclusion);
			}
		}
	}
	
	@Override
	public double getChunkBufferContribution() {
		return getEnergyStorage().getEnergyStored() >= POWER_USE ? -scrubberRate : 0D;
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
	
	// IC2
	
	@Override
	public int getEUSourceTier() {
		return 1;
	}
	
	@Override
	public int getEUSinkTier() {
		return 4;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("scrubberRate", scrubberRate);
		
		int count = 0;
		for (KeyPair<BlockPos, Integer> occlusion : occlusionList) {
			BlockPos pos = occlusion.getLeft();
			nbt.setIntArray("occlusion" + count, new int[] {occlusion.getRight(), pos.getX(), pos.getY(), pos.getZ()});
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
				occlusionList.add(KeyPair.of(new BlockPos(data[1], data[2], data[3]), data[0]));
			}
		}
	}
}
