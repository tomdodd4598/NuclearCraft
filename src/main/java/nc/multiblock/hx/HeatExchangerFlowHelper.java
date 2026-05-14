package nc.multiblock.hx;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;

import java.util.*;
import java.util.function.*;

public class HeatExchangerFlowHelper {
	
	public static Long2ObjectMap<ObjectSet<Vec3d>> getFlowMap(LongSet inletPosLongSet, LongSet outletPosLongSet, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate, Predicate<BlockPos> outletPredicate) {
		Long2ObjectMap<ObjectSet<Vec3d>> flowMap = new Long2ObjectOpenHashMap<>();
		
		if (inletPosLongSet.isEmpty() || outletPosLongSet.isEmpty()) {
			return flowMap;
		}
		
		Long2ObjectMap<Long2IntMap> inletDistanceMap = new Long2ObjectOpenHashMap<>();
		Long2ObjectMap<Long2IntMap> outletDistanceMap = new Long2ObjectOpenHashMap<>();
		
		for (long inletPosLong : inletPosLongSet) {
			inletDistanceMap.put(inletPosLong, getDistanceMap(BlockPos.fromLong(inletPosLong), connectionsFunction, openPredicate, spacePredicate));
		}
		
		for (long outletPosLong : outletPosLongSet) {
			outletDistanceMap.put(outletPosLong, getReverseDistanceMap(BlockPos.fromLong(outletPosLong), connectionsFunction, openPredicate, spacePredicate));
		}
		
		for (long inletPosLong : inletPosLongSet) {
			BlockPos inletPos = BlockPos.fromLong(inletPosLong);
			Long2IntMap fromInlet = inletDistanceMap.get(inletPosLong);
			
			if (fromInlet == null || fromInlet.isEmpty()) {
				continue;
			}
			
			for (long outletPosLong : outletPosLongSet) {
				BlockPos outletPos = BlockPos.fromLong(outletPosLong);
				Long2IntMap toOutlet = outletDistanceMap.get(outletPosLong);
				
				if (toOutlet == null || toOutlet.isEmpty()) {
					continue;
				}
				
				int shortestPathLength = Integer.MAX_VALUE;
				for (Long2IntMap.Entry entry : fromInlet.long2IntEntrySet()) {
					long posLong = entry.getLongKey();
					if (toOutlet.containsKey(posLong)) {
						shortestPathLength = Math.min(shortestPathLength, entry.getIntValue() + toOutlet.get(posLong));
					}
				}
				
				if (shortestPathLength != Integer.MAX_VALUE) {
					addShortestPathDirs(flowMap, inletPos, outletPos, fromInlet, toOutlet, shortestPathLength, connectionsFunction, openPredicate, spacePredicate);
				}
			}
		}
		
		return flowMap;
	}
	
	private static Long2IntMap getDistanceMap(BlockPos inletPos, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate) {
		Long2IntMap distanceMap = new Long2IntOpenHashMap();
		distanceMap.defaultReturnValue(-1);
		
		Queue<BlockPos> queue = new ArrayDeque<>();
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			if (isTraversableDir(inletPos, dir, connectionsFunction, openPredicate, spacePredicate)) {
				BlockPos offsetPos = inletPos.offset(dir);
				long offsetPosLong = offsetPos.toLong();
				if (!distanceMap.containsKey(offsetPosLong)) {
					distanceMap.put(offsetPosLong, 1);
					queue.add(offsetPos);
				}
			}
		}
		
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			int dist = distanceMap.get(pos.toLong());
			
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isTraversableDir(pos, dir, connectionsFunction, openPredicate, spacePredicate)) {
					BlockPos offsetPos = pos.offset(dir);
					long offsetPosLong = offsetPos.toLong();
					if (!distanceMap.containsKey(offsetPosLong)) {
						distanceMap.put(offsetPosLong, dist + 1);
						queue.add(offsetPos);
					}
				}
			}
		}
		
		return distanceMap;
	}
	
	private static Long2IntMap getReverseDistanceMap(BlockPos outletPos, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate) {
		Long2IntMap distanceMap = new Long2IntOpenHashMap();
		distanceMap.defaultReturnValue(-1);
		
		Queue<BlockPos> queue = new ArrayDeque<>();
		
		for (EnumFacing dirFromOutletToSpace : EnumFacing.VALUES) {
			BlockPos spacePos = outletPos.offset(dirFromOutletToSpace);
			EnumFacing dirFromSpaceToOutlet = dirFromOutletToSpace.getOpposite();
			
			if (spacePredicate.test(spacePos, dirFromOutletToSpace) && isOpenDir(spacePos, dirFromSpaceToOutlet, connectionsFunction, openPredicate)) {
				long spacePosLong = spacePos.toLong();
				if (!distanceMap.containsKey(spacePosLong)) {
					distanceMap.put(spacePosLong, 1);
					queue.add(spacePos);
				}
			}
		}
		
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			int dist = distanceMap.get(pos.toLong());
			
			for (EnumFacing dirFromPosToPrevious : EnumFacing.VALUES) {
				BlockPos previousPos = pos.offset(dirFromPosToPrevious);
				EnumFacing dirFromPreviousToPos = dirFromPosToPrevious.getOpposite();
				
				if (spacePredicate.test(previousPos, dirFromPosToPrevious) && isTraversableDir(previousPos, dirFromPreviousToPos, connectionsFunction, openPredicate, spacePredicate)) {
					long previousPosLong = previousPos.toLong();
					if (!distanceMap.containsKey(previousPosLong)) {
						distanceMap.put(previousPosLong, dist + 1);
						queue.add(previousPos);
					}
				}
			}
		}
		
		return distanceMap;
	}
	
	private static void addShortestPathDirs(Long2ObjectMap<ObjectSet<Vec3d>> flowMap, BlockPos inletPos, BlockPos outletPos, Long2IntMap fromInlet, Long2IntMap toOutlet, int shortestPathLength, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate) {
		for (Long2IntMap.Entry entry : fromInlet.long2IntEntrySet()) {
			long posLong = entry.getLongKey();
			int inletDist = entry.getIntValue();
			int outletDist = toOutlet.get(posLong);
			
			if (outletDist == -1 || inletDist + outletDist != shortestPathLength) {
				continue;
			}
			
			BlockPos pos = BlockPos.fromLong(posLong);
			List<BlockPos> incomingPosList = getIncomingPosList(pos, inletPos, fromInlet, toOutlet, connectionsFunction, openPredicate, spacePredicate);
			List<BlockPos> outgoingPosList = getOutgoingPosList(pos, outletPos, fromInlet, toOutlet, connectionsFunction, openPredicate, spacePredicate);
			
			if (incomingPosList.isEmpty() || outgoingPosList.isEmpty()) {
				continue;
			}
			
			ObjectSet<Vec3d> vecs = flowMap.get(posLong);
			if (vecs == null) {
				vecs = new ObjectOpenHashSet<>();
				flowMap.put(posLong, vecs);
			}
			
			for (BlockPos incomingPos : incomingPosList) {
				for (BlockPos outgoingPos : outgoingPosList) {
					BlockPos flowDir = outgoingPos.subtract(incomingPos);
					vecs.add(new Vec3d(flowDir).normalize());
				}
			}
		}
	}
	
	private static List<BlockPos> getIncomingPosList(BlockPos pos, BlockPos inletPos, Long2IntMap fromInlet, Long2IntMap toOutlet, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate) {
		List<BlockPos> incomingPosList = new ArrayList<>();
		long posLong = pos.toLong();
		int inletDist = fromInlet.get(posLong);
		int outletDist = toOutlet.get(posLong);
		
		for (EnumFacing dirFromPosToIncoming : EnumFacing.VALUES) {
			BlockPos incomingPos = pos.offset(dirFromPosToIncoming);
			EnumFacing dirFromIncomingToPos = dirFromPosToIncoming.getOpposite();
			
			if (incomingPos.equals(inletPos)) {
				if (inletDist == 1 && isTraversableDir(incomingPos, dirFromIncomingToPos, connectionsFunction, openPredicate, spacePredicate)) {
					incomingPosList.add(incomingPos);
				}
				continue;
			}
			
			long incomingPosLong = incomingPos.toLong();
			if (fromInlet.get(incomingPosLong) == inletDist - 1 && toOutlet.get(incomingPosLong) == outletDist + 1 && isTraversableDir(incomingPos, dirFromIncomingToPos, connectionsFunction, openPredicate, spacePredicate)) {
				incomingPosList.add(incomingPos);
			}
		}
		
		return incomingPosList;
	}
	
	private static List<BlockPos> getOutgoingPosList(BlockPos pos, BlockPos outletPos, Long2IntMap fromInlet, Long2IntMap toOutlet, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate) {
		List<BlockPos> outgoingPosList = new ArrayList<>();
		long posLong = pos.toLong();
		int inletDist = fromInlet.get(posLong);
		int outletDist = toOutlet.get(posLong);
		
		for (EnumFacing dirFromPosToOutgoing : EnumFacing.VALUES) {
			BlockPos outgoingPos = pos.offset(dirFromPosToOutgoing);
			
			if (outgoingPos.equals(outletPos)) {
				if (outletDist == 1 && isOpenDir(pos, dirFromPosToOutgoing, connectionsFunction, openPredicate)) {
					outgoingPosList.add(outgoingPos);
				}
				continue;
			}
			
			long outgoingPosLong = outgoingPos.toLong();
			if (fromInlet.get(outgoingPosLong) == inletDist + 1 && toOutlet.get(outgoingPosLong) == outletDist - 1 && isTraversableDir(pos, dirFromPosToOutgoing, connectionsFunction, openPredicate, spacePredicate)) {
				outgoingPosList.add(outgoingPos);
			}
		}
		
		return outgoingPosList;
	}
	
	private static boolean isTraversableDir(BlockPos pos, EnumFacing dir, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate) {
		return isOpenDir(pos, dir, connectionsFunction, openPredicate) && spacePredicate.test(pos.offset(dir), dir);
	}
	
	private static boolean isOpenDir(BlockPos pos, EnumFacing dir, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate) {
		HeatExchangerTubeSetting[] connections = connectionsFunction.apply(pos);
		return connections == null || openPredicate.test(connections[dir.getIndex()]);
	}
}
