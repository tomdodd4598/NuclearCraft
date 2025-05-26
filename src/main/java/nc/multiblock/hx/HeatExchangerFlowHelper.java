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
		
		for (long inletPosLong : inletPosLongSet) {
			BlockPos inletPos = BlockPos.fromLong(inletPosLong);
			Object2ObjectMap<BlockPos, ObjectSet<BlockPos>> flowBFSMap = getFlowBFSMap(inletPos, connectionsFunction, openPredicate, spacePredicate, outletPredicate);
			
			for (long outletPosLong : outletPosLongSet) {
				BlockPos outletPos = BlockPos.fromLong(outletPosLong);
				if (flowBFSMap.containsKey(outletPos)) {
					List<List<BlockPos>> flowPaths = getFlowPaths(flowBFSMap, outletPos);
					for (List<BlockPos> flowPath : flowPaths) {
						for (int i = 1, end = flowPath.size() - 1; i < end; ++i) {
							long posLong = flowPath.get(i).toLong();
							ObjectSet<Vec3d> vecs = flowMap.get(posLong);
							if (vecs == null) {
								vecs = new ObjectOpenHashSet<>();
								flowMap.put(posLong, vecs);
							}
							
							BlockPos flowDir = flowPath.get(i + 1).subtract(flowPath.get(i - 1));
							vecs.add(new Vec3d(flowDir).normalize());
						}
					}
				}
			}
		}
		
		return flowMap;
	}
	
	public static Object2ObjectMap<BlockPos, ObjectSet<BlockPos>> getFlowBFSMap(BlockPos inletPos, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, EnumFacing> spacePredicate, Predicate<BlockPos> outletPredicate) {
		Object2ObjectMap<BlockPos, ObjectSet<BlockPos>> bfsMap = new Object2ObjectOpenHashMap<>();
		bfsMap.put(inletPos, new ObjectOpenHashSet<>());
		
		Object2IntMap<BlockPos> distMap = new Object2IntOpenHashMap<>();
		distMap.put(inletPos, 0);
		
		Queue<BlockPos> queue = new LinkedList<>();
		queue.add(inletPos);
		
		boolean begin = true;
		
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			HeatExchangerTubeSetting[] connections = connectionsFunction.apply(pos);
			
			for (int i = 0; i < 6; ++i) {
				if (connections == null || openPredicate.test(connections[i])) {
					EnumFacing dir = EnumFacing.VALUES[i];
					BlockPos offsetPos = pos.offset(dir);
					
					ObjectSet<BlockPos> offsetBFS = bfsMap.get(offsetPos);
					int dist = distMap.getInt(pos) + 1;
					
					if (offsetBFS == null) {
						boolean spaceExists = spacePredicate.test(offsetPos, dir);
						
						if (spaceExists || (!begin && outletPredicate.test(offsetPos))) {
							ObjectSet<BlockPos> posBFS = new ObjectOpenHashSet<>();
							posBFS.add(pos);
							bfsMap.put(offsetPos, posBFS);
							distMap.put(offsetPos, dist);
						}
						
						if (spaceExists) {
							queue.add(offsetPos);
						}
					}
					else if (distMap.getInt(offsetPos) == dist) {
						offsetBFS.add(pos);
					}
				}
			}
			
			begin = false;
		}
		
		return bfsMap;
	}
	
	public static List<List<BlockPos>> getFlowPaths(Object2ObjectMap<BlockPos, ObjectSet<BlockPos>> flowBFSMap, BlockPos outletPos) {
		List<List<BlockPos>> allPaths = new ArrayList<>();
		reconstructFlowPaths(flowBFSMap, outletPos, allPaths, new LinkedList<>());
		return allPaths;
	}
	
	public static void reconstructFlowPaths(Object2ObjectMap<BlockPos, ObjectSet<BlockPos>> flowBFSMap, BlockPos pos, List<List<BlockPos>> allPaths, LinkedList<BlockPos> path) {
		path.addFirst(pos);
		ObjectSet<BlockPos> posBFS = flowBFSMap.get(pos);
		if (posBFS.isEmpty()) {
			allPaths.add(new ArrayList<>(path));
		}
		else {
			for (BlockPos offsetPos : posBFS) {
				reconstructFlowPaths(flowBFSMap, offsetPos, allPaths, path);
			}
		}
		path.removeFirst();
	}
}
