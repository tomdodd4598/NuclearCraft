package nc.worldgen.structure.vault;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.*;

public class VaultStart extends StructureStart {
	
	public static int MIN_STANDARD_ROOMS = 7;
	public static int MAX_STANDARD_ROOMS = 9;
	public static int STANDARD_GRID_RADIUS = 9;
	public static int MAX_STANDARD_LEVELS = 2;
	public static int STANDARD_NODE_SPACING = 28;
	
	public static int MIN_DEEP_ROOMS = 5;
	public static int MAX_DEEP_ROOMS = 7;
	public static int DEEP_GRID_RADIUS = 7;
	public static int MAX_DEEP_LEVELS = 2;
	public static int DEEP_NODE_SPACING = 24;
	
	public static int ENTRANCE_TOP_PADDING = 2;
	public static int ENTRANCE_RADIUS = 2;
	public static int ENTRANCE_CHUNK_EDGE_PADDING = 3;
	public static int ENTRANCE_BUILDING_CLEARANCE = 2;
	public static int ENTRANCE_MIN_FORBIDDEN_FEATURE_HEIGHT = 4;
	public static int ENTRANCE_CANDIDATE_STEP = 1;
	public static int ENTRANCE_MAX_SURFACE_DELTA = 2;
	public static int ENTRANCE_CLEARANCE_HEIGHT = 3;
	public static int ENTRANCE_MAX_BLOCKED_AIR_CELLS = 4;
	
	public static int ROOM_LEVEL_DROP = 8;
	public static int DEEP_SECTION_EXTRA_DROP = 8;
	public static int MIN_DEEP_GAP_BELOW_TRANSITION = 8;
	public static int MIN_STANDARD_DEEP_VERTICAL_GAP = 6;
	public static int MIN_DEEP_BASE_Y = 4;
	public static int MIN_ROOM_BASE_Y = 4;
	public static int CORRIDOR_HALF_WIDTH = 2;
	public static int CORRIDOR_HEIGHT = 4;
	public static int VERTICAL_LINK_CHANCE = 4;
	public static int MAX_GROWTH_ATTEMPTS_MULTIPLIER = 50;
	
	public static int MIN_BASE_Y = 16;
	public static int TARGET_BASE_Y = 32;
	public static int TARGET_BASE_Y_RANDOM_SPAN = 16;
	public static int MIN_DEPTH_BELOW_SURFACE = 12;
	public static int MIN_SHAFT_HEIGHT = 8;
	public static int MIN_TRANSITION_GRID_DISTANCE = 3;
	public static int MIN_TRANSITION_LEVEL = 1;
	public static int TRANSITION_LEVEL_PRESSURE = 1000;
	public static int TRANSITION_DISTANCE_PRESSURE = 120;
	public static int UNIQUE_DEEP_LEVEL_PRESSURE = 1000;
	public static int UNIQUE_DEEP_DISTANCE_PRESSURE = 120;
	
	public VaultStart() {
	
	}
	
	public VaultStart(World worldIn, java.util.Random rand, int chunkX, int chunkZ) {
		super(chunkX, chunkZ);
		
		BlockPos entranceAnchor = pickVillageEntranceAnchor(worldIn, chunkX, chunkZ);
		if (entranceAnchor == null) {
			return;
		}
		
		int startX = entranceAnchor.getX(), startZ = entranceAnchor.getZ();
		int topY = entranceAnchor.getY();
		
		int targetBaseY = TARGET_BASE_Y + rand.nextInt(Math.max(1, TARGET_BASE_Y_RANDOM_SPAN));
		int baseY = Math.max(MIN_BASE_Y, Math.min(topY - MIN_DEPTH_BELOW_SURFACE, targetBaseY));
		int surfaceY = Math.min(worldIn.getHeight() - ENTRANCE_TOP_PADDING, Math.max(baseY + MIN_SHAFT_HEIGHT, topY));
		
		Int2ObjectMap<VaultNode> nodeById = new Int2ObjectOpenHashMap<>();
		Long2ObjectMap<VaultNode> standardLookup = new Long2ObjectOpenHashMap<>();
		Long2ObjectMap<VaultNode> deepLookup = new Long2ObjectOpenHashMap<>();
		ObjectList<VaultNode> standardNodes = new ObjectArrayList<>();
		ObjectList<VaultNode> deepNodes = new ObjectArrayList<>();
		ObjectList<VaultConnectorEdge> vaultConnectorEdges = new ObjectArrayList<>();
		LongSet edgeKeySet = new LongOpenHashSet();
		
		int nextNodeId = 0;
		VaultNode entryNode = new VaultNode(nextNodeId++, VaultSectorType.STANDARD, 0, 0, 0, VaultRoomType.ENTRY_ROOM);
		nodeById.put(entryNode.id, entryNode);
		standardLookup.put(nodeKey(entryNode.sectorType, entryNode.level, entryNode.gridX, entryNode.gridZ), entryNode);
		standardNodes.add(entryNode);
		
		int minStandardRooms = Math.max(1, MIN_STANDARD_ROOMS), maxStandardRooms = Math.max(minStandardRooms, MAX_STANDARD_ROOMS);
		int targetStandardRooms = minStandardRooms + rand.nextInt(maxStandardRooms - minStandardRooms + 1);
		int standardAttempts = 0, maxStandardAttempts = targetStandardRooms * Math.max(1, MAX_GROWTH_ATTEMPTS_MULTIPLIER);
		int maxStandardLevelsByDepth = Math.max(1, Math.min(MAX_STANDARD_LEVELS, 1 + (baseY - MIN_ROOM_BASE_Y) / Math.max(1, ROOM_LEVEL_DROP)));
		
		while (standardNodes.size() < targetStandardRooms && standardAttempts < maxStandardAttempts) {
			++standardAttempts;
			VaultNode parent = standardNodes.get(rand.nextInt(standardNodes.size()));
			boolean vertical = rand.nextInt(Math.max(1, VERTICAL_LINK_CHANCE)) == 0;
			
			int childLevel = parent.level;
			int childGridX = parent.gridX;
			int childGridZ = parent.gridZ;
			VaultConnectorType connectorType = VaultConnectorType.CORRIDOR;
			
			if (vertical) {
				if (parent.id == entryNode.id) {
					continue;
				}
				if ((parent.doorMask & (VaultComponent.DOOR_UP | VaultComponent.DOOR_DOWN)) != 0) {
					continue;
				}
				if (parent.level + 1 >= maxStandardLevelsByDepth) {
					continue;
				}
				childLevel = parent.level + 1;
				connectorType = rand.nextBoolean() ? VaultConnectorType.STAIRS : VaultConnectorType.LADDER_SHAFT;
			}
			else {
				switch (rand.nextInt(4)) {
					case 0 -> --childGridZ;
					case 1 -> ++childGridZ;
					case 2 -> --childGridX;
					default -> ++childGridX;
				}
			}
			
			if (Math.abs(childGridX) > STANDARD_GRID_RADIUS || Math.abs(childGridZ) > STANDARD_GRID_RADIUS) {
				continue;
			}
			
			long childKey = nodeKey(VaultSectorType.STANDARD, childLevel, childGridX, childGridZ);
			VaultNode child = standardLookup.get(childKey);
			if (child == null) {
				VaultRoomType roomType;
				if (connectorType != VaultConnectorType.CORRIDOR) {
					roomType = VaultRoomType.STAIR_ROOM;
				}
				else {
					int pick = rand.nextInt(10);
					roomType = pick < 6 ? VaultRoomType.SMALL : pick < 8 ? VaultRoomType.LARGE : VaultRoomType.JUNCTION;
				}
				child = new VaultNode(nextNodeId++, VaultSectorType.STANDARD, childLevel, childGridX, childGridZ, roomType);
				nodeById.put(child.id, child);
				standardLookup.put(childKey, child);
				standardNodes.add(child);
			}
			
			if (child.id == parent.id) {
				continue;
			}
			long edgeKey = edgeKey(parent.id, child.id);
			if (!edgeKeySet.add(edgeKey)) {
				continue;
			}
			
			vaultConnectorEdges.add(new VaultConnectorEdge(parent.id, child.id, connectorType));
			if (child.level == parent.level) {
				if (child.gridX == parent.gridX + 1) {
					parent.doorMask |= VaultComponent.DOOR_EAST;
					child.doorMask |= VaultComponent.DOOR_WEST;
				}
				else if (child.gridX == parent.gridX - 1) {
					parent.doorMask |= VaultComponent.DOOR_WEST;
					child.doorMask |= VaultComponent.DOOR_EAST;
				}
				else if (child.gridZ == parent.gridZ + 1) {
					parent.doorMask |= VaultComponent.DOOR_SOUTH;
					child.doorMask |= VaultComponent.DOOR_NORTH;
				}
				else if (child.gridZ == parent.gridZ - 1) {
					parent.doorMask |= VaultComponent.DOOR_NORTH;
					child.doorMask |= VaultComponent.DOOR_SOUTH;
				}
			}
			else {
				parent.doorMask |= VaultComponent.DOOR_DOWN;
				child.doorMask |= VaultComponent.DOOR_UP;
			}
		}
		
		int minGridX = Integer.MAX_VALUE, maxGridX = Integer.MIN_VALUE;
		int minGridZ = Integer.MAX_VALUE, maxGridZ = Integer.MIN_VALUE;
		int maxStandardLevel = 0;
		for (VaultNode node : standardNodes) {
			if (node.gridX < minGridX) {
				minGridX = node.gridX;
			}
			if (node.gridX > maxGridX) {
				maxGridX = node.gridX;
			}
			if (node.gridZ < minGridZ) {
				minGridZ = node.gridZ;
			}
			if (node.gridZ > maxGridZ) {
				maxGridZ = node.gridZ;
			}
			if (node.level > maxStandardLevel) {
				maxStandardLevel = node.level;
			}
		}
		int minimumDeepBase = Math.max(MIN_DEEP_BASE_Y, MIN_ROOM_BASE_Y);
		int minimumTransitionToDeepGap = 5;
		
		VaultNode transitionNode = null;
		int bestTransitionScore = Integer.MIN_VALUE;
		int minimumTransitionY = MIN_DEEP_BASE_Y + Math.max(3, MIN_DEEP_GAP_BELOW_TRANSITION) + 2;
		VaultNode fallbackTransitionNode = null;
		int fallbackTransitionScore = Integer.MIN_VALUE;
		VaultNode backupTransitionNode = null;
		int backupTransitionScore = Integer.MIN_VALUE;
		int transitionLevelPressure = Math.max(1, TRANSITION_LEVEL_PRESSURE);
		int transitionDistancePressure = Math.max(1, TRANSITION_DISTANCE_PRESSURE);
		for (VaultNode node : standardNodes) {
			if (node.id == entryNode.id) {
				continue;
			}
			int nodeY = baseY - node.level * ROOM_LEVEL_DROP;
			int entryDistance = Math.abs(node.gridX) + Math.abs(node.gridZ);
			int cornerDistance = Math.min(Math.min(Math.abs(node.gridX - minGridX) + Math.abs(node.gridZ - minGridZ), Math.abs(node.gridX - minGridX) + Math.abs(node.gridZ - maxGridZ)), Math.min(Math.abs(node.gridX - maxGridX) + Math.abs(node.gridZ - minGridZ), Math.abs(node.gridX - maxGridX) + Math.abs(node.gridZ - maxGridZ)));
			boolean hasHorizontalConnection = (node.doorMask & (VaultComponent.DOOR_NORTH | VaultComponent.DOOR_SOUTH | VaultComponent.DOOR_EAST | VaultComponent.DOOR_WEST)) != 0;
			boolean hasVerticalConnection = (node.doorMask & (VaultComponent.DOOR_UP | VaultComponent.DOOR_DOWN)) != 0;
			boolean canHostDeepSection = nodeY - minimumDeepBase >= minimumTransitionToDeepGap;
			int baseScore = node.level * transitionLevelPressure + entryDistance * transitionDistancePressure;
			int backupScore = baseScore - cornerDistance * 10;
			if (backupScore > backupTransitionScore) {
				backupTransitionScore = backupScore;
				backupTransitionNode = node;
			}
			int fallbackScore = baseScore - cornerDistance * 10 - (hasVerticalConnection ? 25 : 0);
			if (canHostDeepSection && hasHorizontalConnection && entryDistance >= Math.max(1, MIN_TRANSITION_GRID_DISTANCE - 1) && fallbackScore > fallbackTransitionScore) {
				fallbackTransitionScore = fallbackScore;
				fallbackTransitionNode = node;
			}
			if (nodeY < minimumTransitionY || node.level < MIN_TRANSITION_LEVEL || entryDistance < MIN_TRANSITION_GRID_DISTANCE || !hasHorizontalConnection || hasVerticalConnection || !canHostDeepSection) {
				continue;
			}
			int transitionScore = baseScore - cornerDistance * 10;
			if (transitionScore > bestTransitionScore) {
				bestTransitionScore = transitionScore;
				transitionNode = node;
			}
		}
		if (transitionNode == null) {
			transitionNode = fallbackTransitionNode != null ? fallbackTransitionNode : backupTransitionNode == null ? entryNode : backupTransitionNode;
		}
		int transitionNodeY = baseY - transitionNode.level * ROOM_LEVEL_DROP;
		if (transitionNodeY - minimumDeepBase < minimumTransitionToDeepGap) {
			transitionNode = entryNode;
		}
		transitionNode.roomType = VaultRoomType.SECRET_TRANSITION_ROOM;
		
		int transitionCenterX = startX + transitionNode.gridX * STANDARD_NODE_SPACING;
		int transitionCenterY = baseY - transitionNode.level * ROOM_LEVEL_DROP;
		int transitionCenterZ = startZ + transitionNode.gridZ * STANDARD_NODE_SPACING;
		
		int maximumDeepBaseForShaft = transitionCenterY - Math.max(3, MIN_DEEP_GAP_BELOW_TRANSITION);
		if (maximumDeepBaseForShaft < minimumDeepBase) {
			maximumDeepBaseForShaft = transitionCenterY - 3;
		}
		if (maximumDeepBaseForShaft < minimumDeepBase) {
			maximumDeepBaseForShaft = minimumDeepBase;
		}
		int deepBaseY = baseY - maxStandardLevel * ROOM_LEVEL_DROP - DEEP_SECTION_EXTRA_DROP;
		if (deepBaseY < minimumDeepBase) {
			deepBaseY = minimumDeepBase;
		}
		int maximumDeepBaseForConnector = transitionCenterY - minimumTransitionToDeepGap;
		if (deepBaseY > maximumDeepBaseForConnector) {
			deepBaseY = maximumDeepBaseForConnector;
		}
		if (deepBaseY < minimumDeepBase) {
			deepBaseY = minimumDeepBase;
		}
		if (deepBaseY > maximumDeepBaseForShaft) {
			deepBaseY = maximumDeepBaseForShaft;
		}
		int lowestStandardRoomY = baseY - maxStandardLevel * ROOM_LEVEL_DROP;
		int deepestAllowedBaseForSeparation = lowestStandardRoomY - MIN_STANDARD_DEEP_VERTICAL_GAP;
		if (deepBaseY > deepestAllowedBaseForSeparation) {
			deepBaseY = deepestAllowedBaseForSeparation;
		}
		if (deepBaseY < minimumDeepBase) {
			deepBaseY = minimumDeepBase;
		}
		
		VaultNode deepEntryNode = new VaultNode(nextNodeId++, VaultSectorType.DEEP, 0, 0, 0, VaultRoomType.DEEP_STAIR_ROOM);
		nodeById.put(deepEntryNode.id, deepEntryNode);
		deepLookup.put(nodeKey(deepEntryNode.sectorType, deepEntryNode.level, deepEntryNode.gridX, deepEntryNode.gridZ), deepEntryNode);
		deepNodes.add(deepEntryNode);
		
		long transitionEdgeKey = edgeKey(transitionNode.id, deepEntryNode.id);
		if (edgeKeySet.add(transitionEdgeKey)) {
			vaultConnectorEdges.add(new VaultConnectorEdge(transitionNode.id, deepEntryNode.id, VaultConnectorType.HIDDEN_TRANSITION_SHAFT));
			transitionNode.doorMask |= VaultComponent.DOOR_DOWN;
			deepEntryNode.doorMask |= VaultComponent.DOOR_UP;
		}
		
		int minDeepRooms = Math.max(1, MIN_DEEP_ROOMS), maxDeepRooms = Math.max(minDeepRooms, MAX_DEEP_ROOMS);
		int targetDeepRooms = minDeepRooms + rand.nextInt(maxDeepRooms - minDeepRooms + 1);
		int deepAttempts = 0, maxDeepAttempts = targetDeepRooms * Math.max(1, MAX_GROWTH_ATTEMPTS_MULTIPLIER);
		int maxDeepLevelsByDepth = Math.max(1, Math.min(MAX_DEEP_LEVELS, 1 + (deepBaseY - MIN_ROOM_BASE_Y) / Math.max(1, ROOM_LEVEL_DROP)));
		
		while (deepNodes.size() < targetDeepRooms && deepAttempts < maxDeepAttempts) {
			++deepAttempts;
			VaultNode parent = deepNodes.get(rand.nextInt(deepNodes.size()));
			boolean vertical = rand.nextInt(Math.max(1, VERTICAL_LINK_CHANCE)) == 0;
			
			int childLevel = parent.level;
			int childGridX = parent.gridX;
			int childGridZ = parent.gridZ;
			VaultConnectorType connectorType = VaultConnectorType.CORRIDOR;
			
			if (vertical) {
				if (parent.id == deepEntryNode.id) {
					continue;
				}
				if ((parent.doorMask & (VaultComponent.DOOR_UP | VaultComponent.DOOR_DOWN)) != 0) {
					continue;
				}
				if (parent.level + 1 >= maxDeepLevelsByDepth) {
					continue;
				}
				childLevel = parent.level + 1;
				connectorType = rand.nextBoolean() ? VaultConnectorType.STAIRS : VaultConnectorType.LADDER_SHAFT;
			}
			else {
				switch (rand.nextInt(4)) {
					case 0 -> --childGridZ;
					case 1 -> ++childGridZ;
					case 2 -> --childGridX;
					default -> ++childGridX;
				}
			}
			
			if (Math.abs(childGridX) > DEEP_GRID_RADIUS || Math.abs(childGridZ) > DEEP_GRID_RADIUS) {
				continue;
			}
			
			long childKey = nodeKey(VaultSectorType.DEEP, childLevel, childGridX, childGridZ);
			VaultNode child = deepLookup.get(childKey);
			if (child == null) {
				VaultRoomType roomType;
				if (connectorType != VaultConnectorType.CORRIDOR) {
					roomType = VaultRoomType.DEEP_STAIR_ROOM;
				}
				else {
					int pick = rand.nextInt(10);
					roomType = pick < 5 ? VaultRoomType.DEEP_SMALL : pick < 8 ? VaultRoomType.DEEP_LARGE : VaultRoomType.DEEP_JUNCTION;
				}
				int childCenterX = transitionCenterX + childGridX * DEEP_NODE_SPACING;
				int childCenterY = deepBaseY - childLevel * ROOM_LEVEL_DROP;
				int childCenterZ = transitionCenterZ + childGridZ * DEEP_NODE_SPACING;
				if (childCenterY < MIN_ROOM_BASE_Y) {
					continue;
				}
				boolean overlapsStandard = false;
				for (VaultNode standardNode : standardNodes) {
					int standardCenterX = startX + standardNode.gridX * STANDARD_NODE_SPACING;
					int standardCenterY = baseY - standardNode.level * ROOM_LEVEL_DROP;
					int standardCenterZ = startZ + standardNode.gridZ * STANDARD_NODE_SPACING;
					if (childCenterX - roomType.halfX <= standardCenterX + standardNode.roomType.halfX && childCenterX + roomType.halfX >= standardCenterX - standardNode.roomType.halfX && childCenterZ - roomType.halfZ <= standardCenterZ + standardNode.roomType.halfZ && childCenterZ + roomType.halfZ >= standardCenterZ - standardNode.roomType.halfZ && childCenterY <= standardCenterY + 6 && childCenterY + 6 >= standardCenterY) {
						overlapsStandard = true;
						break;
					}
				}
				if (overlapsStandard) {
					continue;
				}
				child = new VaultNode(nextNodeId++, VaultSectorType.DEEP, childLevel, childGridX, childGridZ, roomType);
				nodeById.put(child.id, child);
				deepLookup.put(childKey, child);
				deepNodes.add(child);
			}
			
			if (child.id == parent.id) {
				continue;
			}
			long edgeKey = edgeKey(parent.id, child.id);
			if (!edgeKeySet.add(edgeKey)) {
				continue;
			}
			
			vaultConnectorEdges.add(new VaultConnectorEdge(parent.id, child.id, connectorType));
			if (child.level == parent.level) {
				if (child.gridX == parent.gridX + 1) {
					parent.doorMask |= VaultComponent.DOOR_EAST;
					child.doorMask |= VaultComponent.DOOR_WEST;
				}
				else if (child.gridX == parent.gridX - 1) {
					parent.doorMask |= VaultComponent.DOOR_WEST;
					child.doorMask |= VaultComponent.DOOR_EAST;
				}
				else if (child.gridZ == parent.gridZ + 1) {
					parent.doorMask |= VaultComponent.DOOR_SOUTH;
					child.doorMask |= VaultComponent.DOOR_NORTH;
				}
				else if (child.gridZ == parent.gridZ - 1) {
					parent.doorMask |= VaultComponent.DOOR_NORTH;
					child.doorMask |= VaultComponent.DOOR_SOUTH;
				}
			}
			else {
				parent.doorMask |= VaultComponent.DOOR_DOWN;
				child.doorMask |= VaultComponent.DOOR_UP;
			}
		}
		
		for (VaultNode node : standardNodes) {
			node.centerX = startX + node.gridX * STANDARD_NODE_SPACING;
			node.centerY = baseY - node.level * ROOM_LEVEL_DROP;
			node.centerZ = startZ + node.gridZ * STANDARD_NODE_SPACING;
			node.doorMask = 0;
		}
		
		for (VaultNode node : deepNodes) {
			node.centerX = transitionCenterX + node.gridX * DEEP_NODE_SPACING;
			node.centerY = deepBaseY - node.level * ROOM_LEVEL_DROP;
			node.centerZ = transitionCenterZ + node.gridZ * DEEP_NODE_SPACING;
			node.doorMask = 0;
		}
		
		ObjectList<VaultComponent> connectorPieces = new ObjectArrayList<>();
		IntList connectorFromNodeIds = new IntArrayList();
		IntList connectorToNodeIds = new IntArrayList();
		Int2ObjectMap<IntSet> connectorAdjacency = new Int2ObjectOpenHashMap<>();
		
		int standardTransitionNodeId = transitionNode.id;
		int deepTransitionNodeId = deepEntryNode.id;
		
		for (VaultConnectorEdge edge : vaultConnectorEdges) {
			VaultNode nodeA = nodeById.get(edge.fromNodeId);
			VaultNode nodeB = nodeById.get(edge.toNodeId);
			if (nodeA == null || nodeB == null) {
				continue;
			}
			
			boolean nodeAProtected = nodeA.id == entryNode.id || nodeA.id == standardTransitionNodeId || nodeA.id == deepTransitionNodeId;
			boolean nodeBProtected = nodeB.id == entryNode.id || nodeB.id == standardTransitionNodeId || nodeB.id == deepTransitionNodeId;
			if ((nodeAProtected || nodeBProtected) && edge.connectorType != VaultConnectorType.CORRIDOR && edge.connectorType != VaultConnectorType.HIDDEN_TRANSITION_SHAFT) {
				continue;
			}
			
			if (nodeA.sectorType == nodeB.sectorType && nodeA.centerY == nodeB.centerY) {
				int y = nodeA.centerY;
				if (nodeA.centerZ == nodeB.centerZ) {
					int leftCenter = Math.min(nodeA.centerX, nodeB.centerX), rightCenter = Math.max(nodeA.centerX, nodeB.centerX);
					VaultNode leftRoom = nodeA.centerX <= nodeB.centerX ? nodeA : nodeB;
					VaultNode rightRoom = leftRoom == nodeA ? nodeB : nodeA;
					int minX = leftCenter + leftRoom.roomType.halfX;
					int maxX = rightCenter - rightRoom.roomType.halfX;
					if (maxX >= minX) {
						StructureBoundingBox box = new StructureBoundingBox(minX, y, nodeA.centerZ - CORRIDOR_HALF_WIDTH, maxX, y + CORRIDOR_HEIGHT, nodeA.centerZ + CORRIDOR_HALF_WIDTH);
						connectorPieces.add(new VaultComponent(VaultComponent.TYPE_CORRIDOR_X, box, 0, 0, -1, nodeA.sectorType.ordinal()));
						connectorFromNodeIds.add(leftRoom.id);
						connectorToNodeIds.add(rightRoom.id);
						leftRoom.doorMask |= VaultComponent.DOOR_EAST;
						rightRoom.doorMask |= VaultComponent.DOOR_WEST;
						connectorAdjacency.computeIfAbsent(leftRoom.id, key -> new IntOpenHashSet()).add(rightRoom.id);
						connectorAdjacency.computeIfAbsent(rightRoom.id, key -> new IntOpenHashSet()).add(leftRoom.id);
					}
				}
				else if (nodeA.centerX == nodeB.centerX) {
					int northCenter = Math.min(nodeA.centerZ, nodeB.centerZ), southCenter = Math.max(nodeA.centerZ, nodeB.centerZ);
					VaultNode northRoom = nodeA.centerZ <= nodeB.centerZ ? nodeA : nodeB;
					VaultNode southRoom = northRoom == nodeA ? nodeB : nodeA;
					int minZ = northCenter + northRoom.roomType.halfZ;
					int maxZ = southCenter - southRoom.roomType.halfZ;
					if (maxZ >= minZ) {
						StructureBoundingBox box = new StructureBoundingBox(nodeA.centerX - CORRIDOR_HALF_WIDTH, y, minZ, nodeA.centerX + CORRIDOR_HALF_WIDTH, y + CORRIDOR_HEIGHT, maxZ);
						connectorPieces.add(new VaultComponent(VaultComponent.TYPE_CORRIDOR_Z, box, 0, 0, -1, nodeA.sectorType.ordinal()));
						connectorFromNodeIds.add(northRoom.id);
						connectorToNodeIds.add(southRoom.id);
						northRoom.doorMask |= VaultComponent.DOOR_SOUTH;
						southRoom.doorMask |= VaultComponent.DOOR_NORTH;
						connectorAdjacency.computeIfAbsent(northRoom.id, key -> new IntOpenHashSet()).add(southRoom.id);
						connectorAdjacency.computeIfAbsent(southRoom.id, key -> new IntOpenHashSet()).add(northRoom.id);
					}
				}
				continue;
			}
			
			VaultNode topNode = nodeA.centerY >= nodeB.centerY ? nodeA : nodeB;
			VaultNode bottomNode = topNode == nodeA ? nodeB : nodeA;
			int minY = bottomNode.centerY + 1;
			int maxY = topNode.centerY + 1;
			if (maxY <= minY) {
				continue;
			}
			
			int pieceType;
			if (edge.connectorType == VaultConnectorType.HIDDEN_TRANSITION_SHAFT) {
				pieceType = VaultComponent.TYPE_HIDDEN_TRANSITION_SHAFT;
			}
			else if (edge.connectorType == VaultConnectorType.STAIRS) {
				pieceType = VaultComponent.TYPE_STAIRS;
			}
			else {
				pieceType = VaultComponent.TYPE_LADDER_SHAFT;
			}
			
			int radius = pieceType == VaultComponent.TYPE_STAIRS || pieceType == VaultComponent.TYPE_HIDDEN_TRANSITION_SHAFT ? 2 : 1;
			StructureBoundingBox box = new StructureBoundingBox(topNode.centerX - radius, minY, topNode.centerZ - radius, topNode.centerX + radius, maxY, topNode.centerZ + radius);
			connectorPieces.add(new VaultComponent(pieceType, box, 0, 0, -1, topNode.sectorType.ordinal()));
			connectorFromNodeIds.add(topNode.id);
			connectorToNodeIds.add(bottomNode.id);
			topNode.doorMask |= VaultComponent.DOOR_DOWN;
			bottomNode.doorMask |= VaultComponent.DOOR_UP;
			connectorAdjacency.computeIfAbsent(topNode.id, key -> new IntOpenHashSet()).add(bottomNode.id);
			connectorAdjacency.computeIfAbsent(bottomNode.id, key -> new IntOpenHashSet()).add(topNode.id);
		}
		
		IntSet reachableNodeIds = new IntOpenHashSet();
		IntArrayFIFOQueue nodeQueue = new IntArrayFIFOQueue();
		reachableNodeIds.add(entryNode.id);
		nodeQueue.enqueue(entryNode.id);
		while (!nodeQueue.isEmpty()) {
			int nodeId = nodeQueue.dequeueInt();
			IntSet neighbors = connectorAdjacency.get(nodeId);
			if (neighbors == null || neighbors.isEmpty()) {
				continue;
			}
			IntIterator neighborIterator = neighbors.iterator();
			while (neighborIterator.hasNext()) {
				int neighborId = neighborIterator.nextInt();
				if (reachableNodeIds.add(neighborId)) {
					nodeQueue.enqueue(neighborId);
				}
			}
		}
		
		VaultNode uniqueDeepRoom = null;
		int uniqueScore = Integer.MIN_VALUE;
		int uniqueDeepLevelPressure = Math.max(1, UNIQUE_DEEP_LEVEL_PRESSURE);
		int uniqueDeepDistancePressure = Math.max(1, UNIQUE_DEEP_DISTANCE_PRESSURE);
		for (VaultNode node : deepNodes) {
			if (node.id == deepEntryNode.id || !reachableNodeIds.contains(node.id) || (node.doorMask & (VaultComponent.DOOR_UP | VaultComponent.DOOR_DOWN)) != 0) {
				continue;
			}
			int score = node.level * uniqueDeepLevelPressure + (Math.abs(node.gridX) + Math.abs(node.gridZ)) * uniqueDeepDistancePressure;
			if (score > uniqueScore) {
				uniqueScore = score;
				uniqueDeepRoom = node;
			}
		}
		if (uniqueDeepRoom == null) {
			uniqueScore = Integer.MIN_VALUE;
			for (VaultNode node : deepNodes) {
				if (node.id == deepEntryNode.id || !reachableNodeIds.contains(node.id)) {
					continue;
				}
				int score = node.level * uniqueDeepLevelPressure + (Math.abs(node.gridX) + Math.abs(node.gridZ)) * uniqueDeepDistancePressure;
				if (score > uniqueScore) {
					uniqueScore = score;
					uniqueDeepRoom = node;
				}
			}
		}
		if (uniqueDeepRoom == null) {
			uniqueDeepRoom = deepEntryNode;
		}
		uniqueDeepRoom.roomType = VaultRoomType.DEEP_UNIQUE_LOOT_ROOM;
		
		for (int i = 0; i < connectorPieces.size(); ++i) {
			int fromId = connectorFromNodeIds.getInt(i), toId = connectorToNodeIds.getInt(i);
			if (fromId != uniqueDeepRoom.id && toId != uniqueDeepRoom.id) {
				continue;
			}
			VaultNode fromNode = nodeById.get(fromId), toNode = nodeById.get(toId);
			if (fromNode == null || toNode == null || fromNode.centerY != toNode.centerY) {
				continue;
			}
			StructureBoundingBox box = connectorPieces.get(i).getBoundingBox();
			if (box == null) {
				continue;
			}
			
			if (fromNode.centerZ == toNode.centerZ) {
				VaultNode leftNode = fromNode.centerX <= toNode.centerX ? fromNode : toNode;
				VaultNode rightNode = leftNode == fromNode ? toNode : fromNode;
				int minX = leftNode.centerX + leftNode.roomType.halfX;
				int maxX = rightNode.centerX - rightNode.roomType.halfX;
				if (maxX >= minX) {
					box.minX = minX;
					box.maxX = maxX;
				}
			}
			else if (fromNode.centerX == toNode.centerX) {
				VaultNode northNode = fromNode.centerZ <= toNode.centerZ ? fromNode : toNode;
				VaultNode southNode = northNode == fromNode ? toNode : fromNode;
				int minZ = northNode.centerZ + northNode.roomType.halfZ;
				int maxZ = southNode.centerZ - southNode.roomType.halfZ;
				if (maxZ >= minZ) {
					box.minZ = minZ;
					box.maxZ = maxZ;
				}
			}
		}
		
		for (VaultNode node : standardNodes) {
			if (!reachableNodeIds.contains(node.id)) {
				continue;
			}
			StructureBoundingBox box = new StructureBoundingBox(node.centerX - node.roomType.halfX, node.centerY, node.centerZ - node.roomType.halfZ, node.centerX + node.roomType.halfX, node.centerY + 6, node.centerZ + node.roomType.halfZ);
			components.add(new VaultComponent(VaultComponent.TYPE_ROOM, box, node.doorMask, 0, node.roomType.ordinal(), node.sectorType.ordinal()));
		}
		
		for (VaultNode node : deepNodes) {
			if (!reachableNodeIds.contains(node.id)) {
				continue;
			}
			StructureBoundingBox box = new StructureBoundingBox(node.centerX - node.roomType.halfX, node.centerY, node.centerZ - node.roomType.halfZ, node.centerX + node.roomType.halfX, node.centerY + 6, node.centerZ + node.roomType.halfZ);
			components.add(new VaultComponent(VaultComponent.TYPE_ROOM, box, node.doorMask, 0, node.roomType.ordinal(), node.sectorType.ordinal()));
		}
		
		for (int i = 0; i < connectorPieces.size(); ++i) {
			if (reachableNodeIds.contains(connectorFromNodeIds.getInt(i)) && reachableNodeIds.contains(connectorToNodeIds.getInt(i))) {
				components.add(connectorPieces.get(i));
			}
		}
		
		StructureBoundingBox entranceBox = new StructureBoundingBox(startX - ENTRANCE_RADIUS, baseY, startZ - ENTRANCE_RADIUS, startX + ENTRANCE_RADIUS, surfaceY, startZ + ENTRANCE_RADIUS);
		components.add(new VaultComponent(VaultComponent.TYPE_ENTRANCE, entranceBox, 0, surfaceY, VaultRoomType.ENTRY_ROOM.ordinal(), VaultSectorType.STANDARD.ordinal()));
		
		updateBoundingBox();
	}
	
	private static BlockPos pickVillageEntranceAnchor(World world, int chunkX, int chunkZ) {
		int chunkMinX = chunkX << 4, chunkMinZ = chunkZ << 4;
		int chunkMaxX = chunkMinX + 15, chunkMaxZ = chunkMinZ + 15;
		int minX = chunkMinX + ENTRANCE_CHUNK_EDGE_PADDING;
		int minZ = chunkMinZ + ENTRANCE_CHUNK_EDGE_PADDING;
		int maxX = chunkMaxX - ENTRANCE_CHUNK_EDGE_PADDING;
		int maxZ = chunkMaxZ - ENTRANCE_CHUNK_EDGE_PADDING;
		if (minX > maxX || minZ > maxZ) {
			return null;
		}
		
		int anchorX = chunkMinX + 8, anchorZ = chunkMinZ + 8;
		int fallbackSurfaceY = world.getSeaLevel();
		ObjectList<StructureBoundingBox> forbiddenSurfaceBoxes = new ObjectArrayList<>();
		
		MapGenStructureData villageData = (MapGenStructureData) world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village");
		if (villageData != null) {
			NBTTagCompound villageTag = villageData.getTagCompound();
			String key = MapGenStructureData.formatChunkCoords(chunkX, chunkZ);
			if (villageTag.hasKey(key, 10)) {
				NBTTagCompound villageStart = villageTag.getCompoundTag(key);
				if ((!villageStart.hasKey("Valid") || villageStart.getBoolean("Valid")) && villageStart.hasKey("BB", 11)) {
					int[] bounds = villageStart.getIntArray("BB");
					if (bounds.length >= 6) {
						int villageMinX = Math.min(bounds[0], bounds[3]);
						int villageMaxX = Math.max(bounds[0], bounds[3]);
						int villageMinZ = Math.min(bounds[2], bounds[5]);
						int villageMaxZ = Math.max(bounds[2], bounds[5]);
						int villageCenterX = (villageMinX + villageMaxX) >> 1;
						int villageCenterZ = (villageMinZ + villageMaxZ) >> 1;
						minX = villageMinX + ENTRANCE_CHUNK_EDGE_PADDING;
						minZ = villageMinZ + ENTRANCE_CHUNK_EDGE_PADDING;
						maxX = villageMaxX - ENTRANCE_CHUNK_EDGE_PADDING;
						maxZ = villageMaxZ - ENTRANCE_CHUNK_EDGE_PADDING;
						if (minX <= maxX && minZ <= maxZ) {
							anchorX = Math.max(minX, Math.min(maxX, villageCenterX));
							anchorZ = Math.max(minZ, Math.min(maxZ, villageCenterZ));
						}
						fallbackSurfaceY = Math.max(world.getSeaLevel(), Math.min(bounds[1], bounds[4]));
					}
				}
				
				if (villageStart.hasKey("Children", 9)) {
					NBTTagList children = villageStart.getTagList("Children", 10);
					int clearance = Math.max(0, ENTRANCE_BUILDING_CLEARANCE);
					int minForbiddenHeight = Math.max(0, ENTRANCE_MIN_FORBIDDEN_FEATURE_HEIGHT);
					for (int i = 0; i < children.tagCount(); ++i) {
						NBTTagCompound child = children.getCompoundTagAt(i);
						if (!child.hasKey("BB", 11)) {
							continue;
						}
						int[] childBounds = child.getIntArray("BB");
						if (childBounds.length < 6) {
							continue;
						}
						int childMinY = Math.min(childBounds[1], childBounds[4]);
						int childMaxY = Math.max(childBounds[1], childBounds[4]);
						if (childMaxY - childMinY < minForbiddenHeight) {
							continue;
						}
						int childMinX = Math.min(childBounds[0], childBounds[3]) - clearance;
						int childMaxX = Math.max(childBounds[0], childBounds[3]) + clearance;
						int childMinZ = Math.min(childBounds[2], childBounds[5]) - clearance;
						int childMaxZ = Math.max(childBounds[2], childBounds[5]) + clearance;
						forbiddenSurfaceBoxes.add(new StructureBoundingBox(childMinX, childMinZ, childMaxX, childMaxZ));
					}
				}
			}
		}
		
		int bestScore = Integer.MIN_VALUE;
		BlockPos bestAnchor = null;
		int entranceRadius = Math.max(1, ENTRANCE_RADIUS);
		int collisionRadius = entranceRadius + Math.max(0, ENTRANCE_BUILDING_CLEARANCE);
		int clearanceHeight = Math.max(1, ENTRANCE_CLEARANCE_HEIGHT);
		int maxSurfaceDelta = Math.max(0, ENTRANCE_MAX_SURFACE_DELTA);
		int maxBlockedAirCells = Math.max(0, ENTRANCE_MAX_BLOCKED_AIR_CELLS);
		int sampleStep = Math.max(1, ENTRANCE_CANDIDATE_STEP);
		int maxSearchOffset = Math.max(maxX - minX, maxZ - minZ);
		
		for (int searchOffset = 0; searchOffset <= maxSearchOffset; ++searchOffset) {
			boolean visited = false;
			int xMin = Math.max(minX, anchorX - searchOffset);
			int xMax = Math.min(maxX, anchorX + searchOffset);
			int zMin = Math.max(minZ, anchorZ - searchOffset);
			int zMax = Math.min(maxZ, anchorZ + searchOffset);
			
			for (int x = xMin; x <= xMax; x += sampleStep) {
				for (int z = zMin; z <= zMax; z += sampleStep) {
					if (searchOffset > 0 && x > xMin && x < xMax && z > zMin && z < zMax) {
						continue;
					}
					visited = true;
					
					int footprintMinX = x - collisionRadius;
					int footprintMaxX = x + collisionRadius;
					int footprintMinZ = z - collisionRadius;
					int footprintMaxZ = z + collisionRadius;
					if (footprintMinX < minX || footprintMaxX > maxX || footprintMinZ < minZ || footprintMaxZ > maxZ) {
						continue;
					}
					
					boolean intersectsVillagePiece = false;
					for (StructureBoundingBox forbiddenBox : forbiddenSurfaceBoxes) {
						if (footprintMaxX >= forbiddenBox.minX && footprintMinX <= forbiddenBox.maxX && footprintMaxZ >= forbiddenBox.minZ && footprintMinZ <= forbiddenBox.maxZ) {
							intersectsVillagePiece = true;
							break;
						}
					}
					if (intersectsVillagePiece) {
						continue;
					}
					
					boolean loadedFootprint = true;
					for (int px = x - entranceRadius; px <= x + entranceRadius && loadedFootprint; ++px) {
						for (int pz = z - entranceRadius; pz <= z + entranceRadius; ++pz) {
							if (!world.isBlockLoaded(new BlockPos(px, 0, pz), false)) {
								loadedFootprint = false;
								break;
							}
						}
					}
					
					int y = loadedFootprint ? world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY() : fallbackSurfaceY;
					if (y <= 0) {
						continue;
					}
					
					int minTopY = y;
					int maxTopY = y;
					if (loadedFootprint) {
						for (int px = x - entranceRadius; px <= x + entranceRadius; ++px) {
							for (int pz = z - entranceRadius; pz <= z + entranceRadius; ++pz) {
								int topY = world.getTopSolidOrLiquidBlock(new BlockPos(px, 0, pz)).getY();
								if (topY < minTopY) {
									minTopY = topY;
								}
								if (topY > maxTopY) {
									maxTopY = topY;
								}
							}
						}
					}
					if (maxTopY - minTopY > maxSurfaceDelta) {
						continue;
					}
					
					int blockedAirCells = 0;
					if (loadedFootprint) {
						for (int px = x - entranceRadius; px <= x + entranceRadius; ++px) {
							for (int pz = z - entranceRadius; pz <= z + entranceRadius; ++pz) {
								for (int py = y + 1; py <= y + clearanceHeight; ++py) {
									Material material = world.getBlockState(new BlockPos(px, py, pz)).getMaterial();
									if (!material.isReplaceable() && !material.isLiquid()) {
										++blockedAirCells;
										if (blockedAirCells > maxBlockedAirCells) {
											break;
										}
									}
								}
								if (blockedAirCells > maxBlockedAirCells) {
									break;
								}
							}
							if (blockedAirCells > maxBlockedAirCells) {
								break;
							}
						}
					}
					if (blockedAirCells > maxBlockedAirCells) {
						continue;
					}
					
					int centerDistance = Math.abs(x - anchorX) + Math.abs(z - anchorZ);
					int score = 10000 - centerDistance * 16 - (maxTopY - minTopY) * 24 - blockedAirCells * 48;
					if (score > bestScore) {
						bestScore = score;
						bestAnchor = new BlockPos(x, y, z);
					}
				}
			}
			
			if (bestAnchor != null || !visited) {
				break;
			}
		}
		
		return bestAnchor;
	}
	
	@Override
	public boolean isSizeableStructure() {
		return !components.isEmpty();
	}
	
	private static long nodeKey(VaultSectorType sectorType, int level, int x, int z) {
		return ((long) sectorType.ordinal() & 255L) << 56 | ((long) level & 255L) << 48 | ((long) x & 16777215L) << 24 | ((long) z & 16777215L);
	}
	
	private static long edgeKey(int a, int b) {
		int min = Math.min(a, b), max = Math.max(a, b);
		return ((long) min << 32) | ((long) max & 4294967295L);
	}
}
