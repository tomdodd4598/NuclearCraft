package nc.worldgen.structure.vault;

import it.unimi.dsi.fastutil.longs.*;
import nc.Global;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.*;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;
import java.util.function.Supplier;

public class VaultComponent extends StructureComponent {
	
	public static final String COMPONENT_NAME = "NuclearCraftVaultComponent";
	
	public static final int TYPE_ROOM = 0;
	public static final int TYPE_CORRIDOR_X = 1;
	public static final int TYPE_CORRIDOR_Z = 2;
	public static final int TYPE_ENTRANCE = 3;
	public static final int TYPE_LADDER_SHAFT = 4;
	public static final int TYPE_STAIRS = 5;
	public static final int TYPE_HIDDEN_TRANSITION_SHAFT = 6;
	
	public static final int DOOR_NORTH = 1;
	public static final int DOOR_SOUTH = 2;
	public static final int DOOR_WEST = 4;
	public static final int DOOR_EAST = 8;
	public static final int DOOR_UP = 16;
	public static final int DOOR_DOWN = 32;
	
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(Global.MOD_ID, "chests/vault");
	
	private int pieceType = TYPE_ROOM;
	private int doorMask = 0;
	private int surfaceY = 0;
	private int roomTypeOrdinal = -1;
	private int sectorTypeOrdinal = VaultSectorType.STANDARD.ordinal();
	
	public VaultComponent() {
		setCoordBaseMode(null);
	}
	
	VaultComponent(int pieceType, StructureBoundingBox boundingBox, int doorMask, int surfaceY, int roomTypeOrdinal, int sectorTypeOrdinal) {
		this();
		
		this.pieceType = pieceType;
		this.boundingBox = boundingBox;
		this.doorMask = doorMask;
		this.surfaceY = surfaceY;
		this.roomTypeOrdinal = roomTypeOrdinal;
		this.sectorTypeOrdinal = sectorTypeOrdinal;
	}
	
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("Type", pieceType);
		tagCompound.setInteger("Doors", doorMask);
		tagCompound.setInteger("SurfaceY", surfaceY);
		tagCompound.setInteger("RoomType", roomTypeOrdinal);
		tagCompound.setInteger("SectorType", sectorTypeOrdinal);
	}
	
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager manager) {
		pieceType = tagCompound.getInteger("Type");
		doorMask = tagCompound.getInteger("Doors");
		surfaceY = tagCompound.getInteger("SurfaceY");
		roomTypeOrdinal = tagCompound.hasKey("RoomType", 3) ? tagCompound.getInteger("RoomType") : -1;
		sectorTypeOrdinal = tagCompound.hasKey("SectorType", 3) ? tagCompound.getInteger("SectorType") : VaultSectorType.STANDARD.ordinal();
	}
	
	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		return switch (pieceType) {
			case TYPE_CORRIDOR_X -> generateCorridor(worldIn, randomIn, structureBoundingBoxIn, true);
			case TYPE_CORRIDOR_Z -> generateCorridor(worldIn, randomIn, structureBoundingBoxIn, false);
			case TYPE_ENTRANCE -> generateEntrance(worldIn, randomIn, structureBoundingBoxIn);
			case TYPE_LADDER_SHAFT -> generateLadderShaft(worldIn, randomIn, structureBoundingBoxIn, false);
			case TYPE_STAIRS -> generateStairs(worldIn, randomIn, structureBoundingBoxIn);
			case TYPE_HIDDEN_TRANSITION_SHAFT -> generateLadderShaft(worldIn, randomIn, structureBoundingBoxIn, true);
			default -> generateRoom(worldIn, randomIn, structureBoundingBoxIn);
		};
	}
	
	private boolean generateRoom(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		VaultRoomType roomType = roomTypeOrdinal >= 0 && roomTypeOrdinal < VaultRoomType.values().length ? VaultRoomType.values()[roomTypeOrdinal] : VaultRoomType.SMALL;
		int minX = boundingBox.minX, minY = boundingBox.minY, minZ = boundingBox.minZ;
		int maxX = boundingBox.maxX, maxY = boundingBox.maxY, maxZ = boundingBox.maxZ;
		
		Supplier<IBlockState> wall = () -> roomType.getWallState(randomIn);
		Supplier<IBlockState> floor = () -> roomType.getFloorState(randomIn);
		Supplier<IBlockState> ceiling = () -> roomType.getCeilingState(randomIn);
		Supplier<IBlockState> light = () -> roomType.getLightState(randomIn);
		
		fillWithBlocks(worldIn, structureBoundingBoxIn, minX, minY, minZ, maxX, maxY, maxZ, wall, Blocks.AIR::getDefaultState, false);
		fillWithBlocks(worldIn, structureBoundingBoxIn, minX + 1, minY, minZ + 1, maxX - 1, minY, maxZ - 1, floor, floor, false);
		fillWithBlocks(worldIn, structureBoundingBoxIn, minX + 1, maxY, minZ + 1, maxX - 1, maxY, maxZ - 1, ceiling, ceiling, false);
		fillWithAir(worldIn, structureBoundingBoxIn, minX + 1, minY + 1, minZ + 1, maxX - 1, maxY - 1, maxZ - 1);
		
		int centerX = (minX + maxX) >> 1, centerZ = (minZ + maxZ) >> 1;
		int lightMinX = minX + 2, lightMaxX = maxX - 2;
		int lightMinZ = minZ + 2, lightMaxZ = maxZ - 2;
		if (lightMinX <= lightMaxX && lightMinZ <= lightMaxZ) {
			int lightSizeX = lightMaxX - lightMinX + 1;
			int lightSizeZ = lightMaxZ - lightMinZ + 1;
			for (int x = lightMinX; x <= lightMaxX; ++x) {
				int xIndex = x - lightMinX;
				boolean xLit;
				if ((lightSizeX & 1) == 1) {
					xLit = (xIndex & 1) == 0;
				}
				else {
					int xBand = xIndex >> 1;
					int xEdgeBand = Math.min(xBand, (lightSizeX - 1 - xIndex) >> 1);
					xLit = (xEdgeBand & 1) == 0;
				}
				if (!xLit) {
					continue;
				}
				for (int z = lightMinZ; z <= lightMaxZ; ++z) {
					int zIndex = z - lightMinZ;
					boolean zLit;
					if ((lightSizeZ & 1) == 1) {
						zLit = (zIndex & 1) == 0;
					}
					else {
						int zBand = zIndex >> 1;
						int zEdgeBand = Math.min(zBand, (lightSizeZ - 1 - zIndex) >> 1);
						zLit = (zEdgeBand & 1) == 0;
					}
					if (!zLit) {
						continue;
					}
					setBlockState(worldIn, light.get(), x, maxY, z, structureBoundingBoxIn);
				}
			}
		}
		
		if ((doorMask & DOOR_NORTH) != 0) {
			fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, minY + 1, minZ, centerX + 1, minY + 3, minZ + 1);
		}
		if ((doorMask & DOOR_SOUTH) != 0) {
			fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, minY + 1, maxZ - 1, centerX + 1, minY + 3, maxZ);
		}
		if ((doorMask & DOOR_WEST) != 0) {
			fillWithAir(worldIn, structureBoundingBoxIn, minX, minY + 1, centerZ - 1, minX + 1, minY + 3, centerZ + 1);
		}
		if ((doorMask & DOOR_EAST) != 0) {
			fillWithAir(worldIn, structureBoundingBoxIn, maxX - 1, minY + 1, centerZ - 1, maxX, minY + 3, centerZ + 1);
		}
		if ((doorMask & DOOR_UP) != 0) {
			fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, maxY - 1, centerZ - 1, centerX + 1, maxY, centerZ + 1);
		}
		if ((doorMask & DOOR_DOWN) != 0) {
			fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, minY, centerZ - 1, centerX + 1, minY + 2, centerZ + 1);
		}
		
		int chestX = centerX + (centerX + 2 <= maxX - 1 ? 2 : -2);
		int chestZ = centerZ + (centerZ + 2 <= maxZ - 1 ? 2 : -2);
		boolean chestPlaced = false;
		if (roomType.isUniqueLootRoom) {
			generateChest(worldIn, structureBoundingBoxIn, randomIn, chestX, minY + 1, chestZ, LOOT_TABLE);
			chestPlaced = true;
		}
		
		int floorY = minY + 1;
		int innerMinX = minX + 1, innerMaxX = maxX - 1;
		int innerMinZ = minZ + 1, innerMaxZ = maxZ - 1;
		int placeMinX = minX + 2, placeMaxX = maxX - 2;
		int placeMinZ = minZ + 2, placeMaxZ = maxZ - 2;
		if (placeMinX <= placeMaxX && placeMinZ <= placeMaxZ) {
			LongSet occupied = new LongOpenHashSet();
			
			for (int x = placeMinX; x <= placeMaxX; ++x) {
				occupied.add((((long) x) << 32) ^ (centerZ & 0xFFFFFFFFL));
			}
			for (int z = placeMinZ; z <= placeMaxZ; ++z) {
				occupied.add((((long) centerX) << 32) ^ (z & 0xFFFFFFFFL));
			}
			
			if ((doorMask & DOOR_NORTH) != 0) {
				for (int x = Math.max(placeMinX, centerX - 2); x <= Math.min(placeMaxX, centerX + 2); ++x) {
					for (int z = Math.max(placeMinZ, minZ + 1); z <= Math.min(placeMaxZ, minZ + 4); ++z) {
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
			}
			if ((doorMask & DOOR_SOUTH) != 0) {
				for (int x = Math.max(placeMinX, centerX - 2); x <= Math.min(placeMaxX, centerX + 2); ++x) {
					for (int z = Math.max(placeMinZ, maxZ - 4); z <= Math.min(placeMaxZ, maxZ - 1); ++z) {
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
			}
			if ((doorMask & DOOR_WEST) != 0) {
				for (int x = Math.max(placeMinX, minX + 1); x <= Math.min(placeMaxX, minX + 4); ++x) {
					for (int z = Math.max(placeMinZ, centerZ - 2); z <= Math.min(placeMaxZ, centerZ + 2); ++z) {
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
			}
			if ((doorMask & DOOR_EAST) != 0) {
				for (int x = Math.max(placeMinX, maxX - 4); x <= Math.min(placeMaxX, maxX - 1); ++x) {
					for (int z = Math.max(placeMinZ, centerZ - 2); z <= Math.min(placeMaxZ, centerZ + 2); ++z) {
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
			}
			if ((doorMask & (DOOR_UP | DOOR_DOWN)) != 0) {
				for (int x = Math.max(placeMinX, centerX - 2); x <= Math.min(placeMaxX, centerX + 2); ++x) {
					for (int z = Math.max(placeMinZ, centerZ - 2); z <= Math.min(placeMaxZ, centerZ + 2); ++z) {
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
			}
			
			if (chestPlaced) {
				for (int x = Math.max(placeMinX, chestX - 1); x <= Math.min(placeMaxX, chestX + 1); ++x) {
					for (int z = Math.max(placeMinZ, chestZ - 1); z <= Math.min(placeMaxZ, chestZ + 1); ++z) {
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
			}
			
			IBlockState crateState = Blocks.PLANKS.getDefaultState();
			IBlockState voltaicState = NCBlocks.voltaic_pile_basic == null ? null : NCBlocks.voltaic_pile_basic.getDefaultState();
			IBlockState slabState = Blocks.DOUBLE_STONE_SLAB.getDefaultState();
			IBlockState furnaceState = NCConfig.register_processor[0] && NCBlocks.nuclear_furnace != null ? NCBlocks.nuclear_furnace.getDefaultState() : Blocks.FURNACE.getDefaultState();
			IBlockState leadState = StackHelper.getBlockStateFromStack(OreDictHelper.getPrioritisedCraftingStack(Blocks.AIR, "blockLead"));
			if (leadState == null || leadState.getMaterial() == Material.AIR) {
				leadState = Blocks.IRON_BLOCK.getDefaultState();
			}
			IBlockState decayGeneratorState = NCBlocks.decay_generator == null ? Blocks.IRON_BLOCK.getDefaultState() : NCBlocks.decay_generator.getDefaultState();
			IBlockState noteBlockState = Blocks.NOTEBLOCK.getDefaultState();
			IBlockState jukeboxState = Blocks.JUKEBOX.getDefaultState();
			IBlockState enchantingTableState = Blocks.ENCHANTING_TABLE.getDefaultState();
			IBlockState enderChestState = Blocks.ENDER_CHEST.getDefaultState();
			
			int interiorArea = (placeMaxX - placeMinX + 1) * (placeMaxZ - placeMinZ + 1);
			int targetDecorations = Math.max(1, interiorArea / 32);
			if (roomType == VaultRoomType.ENTRY_ROOM || roomType == VaultRoomType.SECRET_TRANSITION_ROOM) {
				targetDecorations = Math.min(targetDecorations, 1);
			}
			if (roomType == VaultRoomType.DEEP_UNIQUE_LOOT_ROOM) {
				targetDecorations = Math.max(targetDecorations, 2);
			}
			int placedDecorations = 0;
			int maxAttempts = Math.max(12, targetDecorations * 10);
			for (int attempt = 0; attempt < maxAttempts && placedDecorations < targetDecorations; ++attempt) {
				int totalWeight = 0;
				for (VaultDecoration decoration : VaultDecoration.values()) {
					int weight = decoration.getWeight(roomType);
					if (decoration == VaultDecoration.VOLTAIC_PILE && voltaicState == null) {
						weight = 0;
					}
					totalWeight += Math.max(0, weight);
				}
				if (totalWeight <= 0) {
					break;
				}
				
				int roll = randomIn.nextInt(totalWeight);
				VaultDecoration decoration = VaultDecoration.CRATE_PILE;
				for (VaultDecoration candidate : VaultDecoration.values()) {
					int weight = candidate.getWeight(roomType);
					if (candidate == VaultDecoration.VOLTAIC_PILE && voltaicState == null) {
						weight = 0;
					}
					weight = Math.max(0, weight);
					if (weight == 0) {
						continue;
					}
					roll -= weight;
					if (roll < 0) {
						decoration = candidate;
						break;
					}
				}
				
				if (decoration == VaultDecoration.CRATE_PILE) {
					int sizeX = 2 + randomIn.nextInt(3);
					int sizeZ = 2 + randomIn.nextInt(3);
					if (placeMaxX - placeMinX + 1 < sizeX || placeMaxZ - placeMinZ + 1 < sizeZ) {
						continue;
					}
					int startX = placeMinX + randomIn.nextInt(placeMaxX - placeMinX - sizeX + 2);
					int startZ = placeMinZ + randomIn.nextInt(placeMaxZ - placeMinZ - sizeZ + 2);
					
					boolean blocked = false;
					for (int x = startX; x < startX + sizeX && !blocked; ++x) {
						for (int z = startZ; z < startZ + sizeZ; ++z) {
							if (occupied.contains((((long) x) << 32) ^ (z & 0xFFFFFFFFL))) {
								blocked = true;
								break;
							}
						}
					}
					if (blocked) {
						continue;
					}
					
					int maxStackHeight = Math.max(1, maxY - floorY - 1);
					for (int x = startX; x < startX + sizeX; ++x) {
						for (int z = startZ; z < startZ + sizeZ; ++z) {
							int stackHeight = Math.min(maxStackHeight, 1 + randomIn.nextInt(3));
							for (int y = 0; y < stackHeight; ++y) {
								setBlockState(worldIn, crateState, x, floorY + y, z, structureBoundingBoxIn);
							}
							occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
						}
					}
					++placedDecorations;
					continue;
				}
				
				if (decoration == VaultDecoration.VOLTAIC_PILE) {
					int pileX = 0;
					int pileZ = 0;
					boolean placed = false;
					for (int edgeAttempt = 0; edgeAttempt < 12 && !placed; ++edgeAttempt) {
						int side = randomIn.nextInt(4);
						if (side == 0) {
							pileX = innerMinX + randomIn.nextInt(innerMaxX - innerMinX + 1);
							pileZ = innerMinZ;
						}
						else if (side == 1) {
							pileX = innerMinX + randomIn.nextInt(innerMaxX - innerMinX + 1);
							pileZ = innerMaxZ;
						}
						else if (side == 2) {
							pileX = innerMinX;
							pileZ = innerMinZ + randomIn.nextInt(innerMaxZ - innerMinZ + 1);
						}
						else {
							pileX = innerMaxX;
							pileZ = innerMinZ + randomIn.nextInt(innerMaxZ - innerMinZ + 1);
						}
						if ((doorMask & DOOR_NORTH) != 0 && pileZ == innerMinZ && pileX >= centerX - 2 && pileX <= centerX + 2) {
							continue;
						}
						if ((doorMask & DOOR_SOUTH) != 0 && pileZ == innerMaxZ && pileX >= centerX - 2 && pileX <= centerX + 2) {
							continue;
						}
						if ((doorMask & DOOR_WEST) != 0 && pileX == innerMinX && pileZ >= centerZ - 2 && pileZ <= centerZ + 2) {
							continue;
						}
						if ((doorMask & DOOR_EAST) != 0 && pileX == innerMaxX && pileZ >= centerZ - 2 && pileZ <= centerZ + 2) {
							continue;
						}
						if (occupied.contains((((long) pileX) << 32) ^ (pileZ & 0xFFFFFFFFL))) {
							continue;
						}
						placed = true;
					}
					if (!placed) {
						continue;
					}
					setBlockState(worldIn, voltaicState, pileX, floorY, pileZ, structureBoundingBoxIn);
					occupied.add((((long) pileX) << 32) ^ (pileZ & 0xFFFFFFFFL));
					++placedDecorations;
					continue;
				}
				
				if (decoration == VaultDecoration.FURNACE_BENCH) {
					int[] shapeX = null;
					int[] shapeZ = null;
					int totalCells = 0;
					int shapeAttempts = 12;
					while (shapeAttempts-- > 0 && shapeX == null) {
						if (randomIn.nextInt(3) == 0) {
							int armA = 3 + randomIn.nextInt(3);
							int armB = 2 + randomIn.nextInt(2);
							totalCells = armA + armB - 1;
							shapeX = new int[totalCells];
							shapeZ = new int[totalCells];
							int corner = randomIn.nextInt(4);
							int index = 0;
							if (corner == 0) {
								for (int i = 0; i < armA; ++i) {
									shapeX[index] = innerMinX + i;
									shapeZ[index] = innerMinZ;
									++index;
								}
								for (int i = 1; i < armB; ++i) {
									shapeX[index] = innerMinX;
									shapeZ[index] = innerMinZ + i;
									++index;
								}
							}
							else if (corner == 1) {
								for (int i = 0; i < armA; ++i) {
									shapeX[index] = innerMaxX - i;
									shapeZ[index] = innerMinZ;
									++index;
								}
								for (int i = 1; i < armB; ++i) {
									shapeX[index] = innerMaxX;
									shapeZ[index] = innerMinZ + i;
									++index;
								}
							}
							else if (corner == 2) {
								for (int i = 0; i < armA; ++i) {
									shapeX[index] = innerMinX + i;
									shapeZ[index] = innerMaxZ;
									++index;
								}
								for (int i = 1; i < armB; ++i) {
									shapeX[index] = innerMinX;
									shapeZ[index] = innerMaxZ - i;
									++index;
								}
							}
							else {
								for (int i = 0; i < armA; ++i) {
									shapeX[index] = innerMaxX - i;
									shapeZ[index] = innerMaxZ;
									++index;
								}
								for (int i = 1; i < armB; ++i) {
									shapeX[index] = innerMaxX;
									shapeZ[index] = innerMaxZ - i;
									++index;
								}
							}
						}
						else {
							int side = randomIn.nextInt(4);
							int lineLength = 3 + randomIn.nextInt(3);
							totalCells = lineLength;
							shapeX = new int[totalCells];
							shapeZ = new int[totalCells];
							if (side == 0 || side == 1) {
								if (innerMaxX - innerMinX + 1 < lineLength) {
									shapeX = null;
									shapeZ = null;
									continue;
								}
								int startX = innerMinX + randomIn.nextInt(innerMaxX - innerMinX - lineLength + 2);
								int z = side == 0 ? innerMinZ : innerMaxZ;
								for (int i = 0; i < lineLength; ++i) {
									shapeX[i] = startX + i;
									shapeZ[i] = z;
								}
							}
							else {
								if (innerMaxZ - innerMinZ + 1 < lineLength) {
									shapeX = null;
									shapeZ = null;
									continue;
								}
								int startZ = innerMinZ + randomIn.nextInt(innerMaxZ - innerMinZ - lineLength + 2);
								int x = side == 2 ? innerMinX : innerMaxX;
								for (int i = 0; i < lineLength; ++i) {
									shapeX[i] = x;
									shapeZ[i] = startZ + i;
								}
							}
						}
						
						boolean blocked = false;
						for (int i = 0; i < totalCells; ++i) {
							int x = shapeX[i], z = shapeZ[i];
							if (x < innerMinX || x > innerMaxX || z < innerMinZ || z > innerMaxZ) {
								blocked = true;
								break;
							}
							if ((doorMask & DOOR_NORTH) != 0 && z == innerMinZ && x >= centerX - 2 && x <= centerX + 2) {
								blocked = true;
								break;
							}
							if ((doorMask & DOOR_SOUTH) != 0 && z == innerMaxZ && x >= centerX - 2 && x <= centerX + 2) {
								blocked = true;
								break;
							}
							if ((doorMask & DOOR_WEST) != 0 && x == innerMinX && z >= centerZ - 2 && z <= centerZ + 2) {
								blocked = true;
								break;
							}
							if ((doorMask & DOOR_EAST) != 0 && x == innerMaxX && z >= centerZ - 2 && z <= centerZ + 2) {
								blocked = true;
								break;
							}
							if (occupied.contains((((long) x) << 32) ^ (z & 0xFFFFFFFFL))) {
								blocked = true;
								break;
							}
							for (int j = i + 1; j < totalCells; ++j) {
								if (shapeX[j] == x && shapeZ[j] == z) {
									blocked = true;
									break;
								}
							}
							if (blocked) {
								break;
							}
						}
						if (blocked) {
							shapeX = null;
							shapeZ = null;
						}
					}
					if (shapeX == null) {
						continue;
					}
					
					for (int i = 0; i < totalCells; ++i) {
						setBlockState(worldIn, slabState, shapeX[i], floorY, shapeZ[i], structureBoundingBoxIn);
						occupied.add((((long) shapeX[i]) << 32) ^ (shapeZ[i] & 0xFFFFFFFFL));
					}
					
					int furnaceCount = Math.min(totalCells, 1 + randomIn.nextInt(2));
					boolean[] usedFurnaceSpot = new boolean[totalCells];
					for (int i = 0; i < furnaceCount; ++i) {
						int index = randomIn.nextInt(totalCells);
						int attempts = 8;
						while (usedFurnaceSpot[index] && attempts-- > 0) {
							index = randomIn.nextInt(totalCells);
						}
						usedFurnaceSpot[index] = true;
						EnumFacing facing;
						if (shapeZ[index] == innerMinZ) {
							facing = EnumFacing.SOUTH;
						}
						else if (shapeZ[index] == innerMaxZ) {
							facing = EnumFacing.NORTH;
						}
						else if (shapeX[index] == innerMinX) {
							facing = EnumFacing.EAST;
						}
						else {
							facing = EnumFacing.WEST;
						}
						IBlockState orientedFurnace = furnaceState;
						if (orientedFurnace.getPropertyKeys().contains(BlockHorizontal.FACING)) {
							orientedFurnace = orientedFurnace.withProperty(BlockHorizontal.FACING, facing);
						}
						setBlockState(worldIn, orientedFurnace, shapeX[index], floorY, shapeZ[index], structureBoundingBoxIn);
					}
					++placedDecorations;
					continue;
				}
				
				int musicMode = decoration == VaultDecoration.MUSIC_LINE ? randomIn.nextInt(3) : 0;
				int magicMode = decoration == VaultDecoration.MAGIC_LINE ? randomIn.nextInt(3) : 0;
				int length = decoration == VaultDecoration.DECAY_LEAD_LINE ? 3 + randomIn.nextInt(4) : (decoration == VaultDecoration.MUSIC_LINE ? (musicMode == 2 ? 2 : 1) : (magicMode == 2 ? 2 : 1));
				int[] lineX = null;
				int[] lineZ = null;
				int lineAttempts = 10;
				while (lineAttempts-- > 0 && lineX == null) {
					int side = randomIn.nextInt(4);
					lineX = new int[length];
					lineZ = new int[length];
					if (side == 0 || side == 1) {
						if (innerMaxX - innerMinX + 1 < length) {
							lineX = null;
							lineZ = null;
							continue;
						}
						int startX = innerMinX + randomIn.nextInt(innerMaxX - innerMinX - length + 2);
						int z = side == 0 ? innerMinZ : innerMaxZ;
						for (int i = 0; i < length; ++i) {
							lineX[i] = startX + i;
							lineZ[i] = z;
						}
					}
					else {
						if (innerMaxZ - innerMinZ + 1 < length) {
							lineX = null;
							lineZ = null;
							continue;
						}
						int startZ = innerMinZ + randomIn.nextInt(innerMaxZ - innerMinZ - length + 2);
						int x = side == 2 ? innerMinX : innerMaxX;
						for (int i = 0; i < length; ++i) {
							lineX[i] = x;
							lineZ[i] = startZ + i;
						}
					}
					
					boolean blocked = false;
					for (int i = 0; i < length; ++i) {
						int x = lineX[i], z = lineZ[i];
						if ((doorMask & DOOR_NORTH) != 0 && z == innerMinZ && x >= centerX - 2 && x <= centerX + 2) {
							blocked = true;
							break;
						}
						if ((doorMask & DOOR_SOUTH) != 0 && z == innerMaxZ && x >= centerX - 2 && x <= centerX + 2) {
							blocked = true;
							break;
						}
						if ((doorMask & DOOR_WEST) != 0 && x == innerMinX && z >= centerZ - 2 && z <= centerZ + 2) {
							blocked = true;
							break;
						}
						if ((doorMask & DOOR_EAST) != 0 && x == innerMaxX && z >= centerZ - 2 && z <= centerZ + 2) {
							blocked = true;
							break;
						}
						if (occupied.contains((((long) x) << 32) ^ (z & 0xFFFFFFFFL))) {
							blocked = true;
							break;
						}
					}
					if (blocked) {
						lineX = null;
						lineZ = null;
					}
				}
				if (lineX == null) {
					continue;
				}
				
				if (decoration == VaultDecoration.DECAY_LEAD_LINE) {
					boolean decayFirst = randomIn.nextBoolean();
					for (int i = 0; i < length; ++i) {
						int x = lineX[i], z = lineZ[i];
						boolean placeDecay = ((i & 1) == 0) == decayFirst;
						IBlockState state = placeDecay ? decayGeneratorState : leadState;
						if (placeDecay && state.getPropertyKeys().contains(BlockHorizontal.FACING)) {
							if (z == innerMinZ) {
								state = state.withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH);
							}
							else if (z == innerMaxZ) {
								state = state.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH);
							}
							else if (x == innerMinX) {
								state = state.withProperty(BlockHorizontal.FACING, EnumFacing.EAST);
							}
							else {
								state = state.withProperty(BlockHorizontal.FACING, EnumFacing.WEST);
							}
						}
						setBlockState(worldIn, state, x, floorY, z, structureBoundingBoxIn);
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
				else if (decoration == VaultDecoration.MUSIC_LINE) {
					int jukeboxIndex = -1;
					if (musicMode == 1) {
						jukeboxIndex = 0;
					}
					else if (musicMode == 2) {
						jukeboxIndex = randomIn.nextBoolean() ? 1 : 0;
					}
					for (int i = 0; i < length; ++i) {
						int x = lineX[i], z = lineZ[i];
						IBlockState state = i == jukeboxIndex ? jukeboxState : noteBlockState;
						setBlockState(worldIn, state, x, floorY, z, structureBoundingBoxIn);
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
				else {
					int enderChestIndex = -1;
					if (magicMode == 1) {
						enderChestIndex = 0;
					}
					else if (magicMode == 2) {
						enderChestIndex = randomIn.nextBoolean() ? 1 : 0;
					}
					for (int i = 0; i < length; ++i) {
						int x = lineX[i], z = lineZ[i];
						IBlockState state = enchantingTableState;
						if (i == enderChestIndex) {
							state = enderChestState;
							EnumFacing outwardFacing;
							if (z == innerMinZ) {
								outwardFacing = EnumFacing.SOUTH;
							}
							else if (z == innerMaxZ) {
								outwardFacing = EnumFacing.NORTH;
							}
							else if (x == innerMinX) {
								outwardFacing = EnumFacing.EAST;
							}
							else {
								outwardFacing = EnumFacing.WEST;
							}
							if (state.getPropertyKeys().contains(BlockHorizontal.FACING)) {
								state = state.withProperty(BlockHorizontal.FACING, outwardFacing);
							}
							else if (state.getPropertyKeys().contains(BlockEnderChest.FACING)) {
								state = state.withProperty(BlockEnderChest.FACING, outwardFacing);
							}
						}
						setBlockState(worldIn, state, x, floorY, z, structureBoundingBoxIn);
						occupied.add((((long) x) << 32) ^ (z & 0xFFFFFFFFL));
					}
				}
				++placedDecorations;
			}
		}
		
		return true;
	}
	
	private boolean generateCorridor(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn, boolean alongX) {
		VaultSectorType sectorType = sectorTypeOrdinal == VaultSectorType.DEEP.ordinal() ? VaultSectorType.DEEP : VaultSectorType.STANDARD;
		int minX = boundingBox.minX, minY = boundingBox.minY, minZ = boundingBox.minZ;
		int maxX = boundingBox.maxX, maxY = boundingBox.maxY, maxZ = boundingBox.maxZ;
		
		Supplier<IBlockState> wall = () -> sectorType.getCorridorWallState(randomIn);
		Supplier<IBlockState> floor = () -> sectorType.getCorridorFloorState(randomIn);
		Supplier<IBlockState> light = () -> sectorType.getCorridorLightState(randomIn);
		
		fillWithBlocks(worldIn, structureBoundingBoxIn, minX, minY, minZ, maxX, maxY, maxZ, wall, Blocks.AIR::getDefaultState, false);
		if (alongX) {
			fillWithBlocks(worldIn, structureBoundingBoxIn, minX, minY, minZ + 1, maxX, minY, maxZ - 1, floor, floor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, minX, minY + 1, minZ + 1, maxX, maxY - 1, maxZ - 1);
		}
		else {
			fillWithBlocks(worldIn, structureBoundingBoxIn, minX + 1, minY, minZ, maxX - 1, minY, maxZ, floor, floor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, minX + 1, minY + 1, minZ, maxX - 1, maxY - 1, maxZ);
		}
		
		if (alongX) {
			int z = (minZ + maxZ) >> 1;
			for (int x = minX; x <= maxX; ++x) {
				setBlockState(worldIn, light.get(), x, maxY, z, structureBoundingBoxIn);
			}
		}
		else {
			int x = (minX + maxX) >> 1;
			for (int z = minZ; z <= maxZ; ++z) {
				setBlockState(worldIn, light.get(), x, maxY, z, structureBoundingBoxIn);
			}
		}
		
		return true;
	}
	
	private boolean generateEntrance(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		VaultSectorType sectorType = sectorTypeOrdinal == VaultSectorType.DEEP.ordinal() ? VaultSectorType.DEEP : VaultSectorType.STANDARD;
		int minX = boundingBox.minX, minY = boundingBox.minY, minZ = boundingBox.minZ;
		int maxX = boundingBox.maxX, maxZ = boundingBox.maxZ;
		int maxY = Math.min(surfaceY, boundingBox.maxY);
		
		Supplier<IBlockState> shell = () -> sectorType.getEntranceShaftShellState(randomIn);
		Supplier<IBlockState> hatch = () -> sectorType.getEntranceHatchState(randomIn);
		
		IBlockState ladderWest = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.EAST);
		IBlockState ladderEast = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST);
		IBlockState trapdoor = Blocks.IRON_TRAPDOOR.getStateFromMeta(8);
		
		fillWithBlocks(worldIn, structureBoundingBoxIn, minX, minY, minZ, maxX, maxY, maxZ, shell, Blocks.AIR::getDefaultState, false);
		
		int centerX = (minX + maxX) >> 1, centerZ = (minZ + maxZ) >> 1;
		int shaftBottomY = minY + 1;
		int shaftHeight = maxY - shaftBottomY + 1;
		int firstFloorY = shaftBottomY + shaftHeight / 3;
		int secondFloorY = shaftBottomY + (shaftHeight << 1) / 3;
		if (secondFloorY <= firstFloorY) {
			secondFloorY = firstFloorY + 1;
		}
		if (secondFloorY > maxY - 1) {
			secondFloorY = maxY - 1;
		}
		
		fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, shaftBottomY, centerZ - 1, centerX + 1, firstFloorY - 1, centerZ + 1);
		fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, firstFloorY + 1, centerZ - 1, centerX + 1, secondFloorY - 1, centerZ + 1);
		fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, secondFloorY + 1, centerZ - 1, centerX + 1, maxY, centerZ + 1);
		
		fillWithBlocks(worldIn, structureBoundingBoxIn, centerX - 1, firstFloorY, centerZ - 1, centerX + 1, firstFloorY, centerZ + 1, hatch, hatch, false);
		fillWithBlocks(worldIn, structureBoundingBoxIn, centerX - 1, secondFloorY, centerZ - 1, centerX + 1, secondFloorY, centerZ + 1, hatch, hatch, false);
		
		setBlockState(worldIn, trapdoor, centerX - 1, firstFloorY, centerZ, structureBoundingBoxIn);
		setBlockState(worldIn, trapdoor, centerX + 1, secondFloorY, centerZ, structureBoundingBoxIn);
		
		for (int y = shaftBottomY; y <= firstFloorY - 1; ++y) {
			setBlockState(worldIn, ladderWest, centerX - 1, y, centerZ, structureBoundingBoxIn);
		}
		for (int y = firstFloorY + 1; y <= secondFloorY - 1; ++y) {
			setBlockState(worldIn, ladderEast, centerX + 1, y, centerZ, structureBoundingBoxIn);
		}
		for (int y = secondFloorY + 1; y <= maxY; ++y) {
			setBlockState(worldIn, ladderWest, centerX - 1, y, centerZ, structureBoundingBoxIn);
		}
		fillWithAir(worldIn, structureBoundingBoxIn, centerX + 1, minY + 2, centerZ - 1, maxX, minY + 4, centerZ + 1);
		
		for (int x = centerX - 1; x <= centerX + 1; ++x) {
			for (int z = centerZ - 1; z <= centerZ + 1; ++z) {
				setBlockState(worldIn, hatch.get(), x, maxY, z, structureBoundingBoxIn);
			}
		}
		setBlockState(worldIn, trapdoor, centerX - 1, maxY, centerZ, structureBoundingBoxIn);
		
		return true;
	}
	
	private boolean generateLadderShaft(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn, boolean hidden) {
		VaultSectorType sectorType = sectorTypeOrdinal == VaultSectorType.DEEP.ordinal() ? VaultSectorType.DEEP : VaultSectorType.STANDARD;
		Supplier<IBlockState> shell = () -> sectorType.getLadderShaftShellState(randomIn);
		
		int minX = boundingBox.minX, minY = boundingBox.minY, minZ = boundingBox.minZ;
		int maxX = boundingBox.maxX, maxY = boundingBox.maxY, maxZ = boundingBox.maxZ;
		int centerX = (minX + maxX) >> 1, centerZ = (minZ + maxZ) >> 1;
		boolean wideShaft = maxX - minX >= 4 && maxZ - minZ >= 4;
		IBlockState ladder = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.EAST);
		
		fillWithBlocks(worldIn, structureBoundingBoxIn, minX, minY, minZ, maxX, maxY, maxZ, shell, Blocks.AIR::getDefaultState, false);
		
		if (wideShaft) {
			fillWithAir(worldIn, structureBoundingBoxIn, centerX - 1, minY, centerZ - 1, centerX + 1, maxY, centerZ + 1);
		}
		else {
			fillWithAir(worldIn, structureBoundingBoxIn, centerX, minY, centerZ, centerX, maxY, centerZ);
		}
		
		int ladderX = wideShaft ? centerX - 1 : centerX;
		for (int y = minY; y <= maxY; ++y) {
			setBlockState(worldIn, ladder, ladderX, y, centerZ, structureBoundingBoxIn);
		}
		
		fillWithAir(worldIn, structureBoundingBoxIn, centerX + 1, minY + 1, centerZ - 1, maxX, minY + 3, centerZ + 1);
		
		return true;
	}
	
	private boolean generateStairs(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		VaultSectorType sectorType = sectorTypeOrdinal == VaultSectorType.DEEP.ordinal() ? VaultSectorType.DEEP : VaultSectorType.STANDARD;
		IBlockState shell = sectorType.getStairsShellState(randomIn);
		IBlockState step = sectorType.getStairsStepState(randomIn);
		
		int minX = boundingBox.minX, minY = boundingBox.minY, minZ = boundingBox.minZ;
		int maxX = boundingBox.maxX, maxY = boundingBox.maxY, maxZ = boundingBox.maxZ;
		int centerX = (minX + maxX) >> 1, centerZ = (minZ + maxZ) >> 1;
		
		fillWithBlocks(worldIn, structureBoundingBoxIn, minX, minY, minZ, maxX, maxY, maxZ, shell, Blocks.AIR.getDefaultState(), false);
		fillWithAir(worldIn, structureBoundingBoxIn, minX + 1, minY, minZ + 1, maxX - 1, maxY, maxZ - 1);
		
		int[][] stepPath = new int[][] {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};
		int stepIndex = 0;
		for (int y = maxY; y >= minY; --y) {
			int[] offset = stepPath[stepIndex % stepPath.length];
			int[] nextOffset = stepPath[(stepIndex + 1) % stepPath.length];
			EnumFacing facing;
			int dx = nextOffset[0] - offset[0];
			int dz = nextOffset[1] - offset[1];
			if (Math.abs(dx) >= Math.abs(dz)) {
				facing = dx >= 0 ? EnumFacing.EAST : EnumFacing.WEST;
			}
			else {
				facing = dz >= 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
			}
			setBlockState(worldIn, step.withProperty(BlockStairs.FACING, facing.getOpposite()), centerX + offset[0], y, centerZ + offset[1], structureBoundingBoxIn);
			setBlockState(worldIn, Blocks.AIR.getDefaultState(), centerX + offset[0], y + 1, centerZ + offset[1], structureBoundingBoxIn);
			++stepIndex;
		}
		fillWithAir(worldIn, structureBoundingBoxIn, centerX + 1, minY + 1, centerZ - 1, maxX, minY + 3, centerZ + 1);
		
		return true;
	}
	
	protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingBox, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Supplier<IBlockState> boundaryBlockState, Supplier<IBlockState> insideBlockState, boolean existingOnly) {
		for (int i = yMin; i <= yMax; ++i) {
			for (int j = xMin; j <= xMax; ++j) {
				for (int k = zMin; k <= zMax; ++k) {
					if (!existingOnly || getBlockStateFromPos(worldIn, j, i, k, boundingBox).getMaterial() != Material.AIR) {
						if (i != yMin && i != yMax && j != xMin && j != xMax && k != zMin && k != zMax) {
							setBlockState(worldIn, insideBlockState.get(), j, i, k, boundingBox);
						}
						else {
							setBlockState(worldIn, boundaryBlockState.get(), j, i, k, boundingBox);
						}
					}
				}
			}
		}
	}
}
