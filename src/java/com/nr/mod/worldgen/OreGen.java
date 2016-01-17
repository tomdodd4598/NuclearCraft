package com.nr.mod.worldgen;
 
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;

import cpw.mods.fml.common.IWorldGenerator;
 
public class OreGen implements IWorldGenerator
{
	public OreGen()
    {
		copper = new WorldGenMinable(NRBlocks.blockOre, 0, NuclearRelativistics.oreSizeCopper, Blocks.stone);
		tin = new WorldGenMinable(NRBlocks.blockOre, 1, NuclearRelativistics.oreSizeTin, Blocks.stone);
		lead = new WorldGenMinable(NRBlocks.blockOre, 2, NuclearRelativistics.oreSizeLead, Blocks.stone);
		silver = new WorldGenMinable(NRBlocks.blockOre, 3, NuclearRelativistics.oreSizeSilver, Blocks.stone);
		pitchblende = new WorldGenMinable(NRBlocks.blockOre, NuclearRelativistics.oreSizePitchblende, 6, Blocks.stone);
		thorium = new WorldGenMinable(NRBlocks.blockOre, 5, NuclearRelativistics.oreSizeThorium, Blocks.stone);
		lithium = new WorldGenMinable(NRBlocks.blockOre, 7, NuclearRelativistics.oreSizeLithium, Blocks.stone);
		boron = new WorldGenMinable(NRBlocks.blockOre, 8, NuclearRelativistics.oreSizeBoron, Blocks.stone);
        plutonium = new WorldGenMinable(NRBlocks.blockOre, 6, NuclearRelativistics.oreSizePlutonium, Blocks.netherrack);
    }
	
	@Override
    public void generate (Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.isHellWorld)
        {
            generateNether(random, chunkX * 16, chunkZ * 16, world);
        }
        else if (world.provider.terrainType != WorldType.FLAT)
        {
            generateSurface(random, chunkX * 16, chunkZ * 16, world);
        }
    }
 
	void generateSurface (Random random, int xChunk, int zChunk, World world)
    {
		int xPos, yPos, zPos;
        if (NuclearRelativistics.oreGenCopper)
        { for (int i = 0; i < NuclearRelativistics.oreRarityCopper; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(60); zPos = zChunk + random.nextInt(16);
                copper.generate(world, random, xPos, yPos, zPos); } }

        if (NuclearRelativistics.oreGenTin)
        { for (int i = 0; i < NuclearRelativistics.oreRarityTin; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(60); zPos = zChunk + random.nextInt(16);
                tin.generate(world, random, xPos, yPos, zPos); } }

        if (NuclearRelativistics.oreGenLead)
        { for (int i = 0; i < NuclearRelativistics.oreRarityLead; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(40); zPos = zChunk + random.nextInt(16);
                lead.generate(world, random, xPos, yPos, zPos); } }

        if (NuclearRelativistics.oreGenSilver)
        { for (int i = 0; i < NuclearRelativistics.oreRaritySilver; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(40); zPos = zChunk + random.nextInt(16);
                silver.generate(world, random, xPos, yPos, zPos); } }

        if (NuclearRelativistics.oreGenPitchblende)
        { for (int i = 0; i < NuclearRelativistics.oreRarityPitchblende; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(40); zPos = zChunk + random.nextInt(16);
                pitchblende.generate(world, random, xPos, yPos, zPos); } }

        if (NuclearRelativistics.oreGenThorium)
        { for (int i = 0; i < NuclearRelativistics.oreRarityThorium; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(40); zPos = zChunk + random.nextInt(16);
                thorium.generate(world, random, xPos, yPos, zPos); } }
        
        if (NuclearRelativistics.oreGenLithium)
        { for (int i = 0; i < NuclearRelativistics.oreRarityLithium; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(25); zPos = zChunk + random.nextInt(16);
                lithium.generate(world, random, xPos, yPos, zPos); } }
        
        if (NuclearRelativistics.oreGenBoron)
        { for (int i = 0; i < NuclearRelativistics.oreRarityBoron; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(20); zPos = zChunk + random.nextInt(16);
                boron.generate(world, random, xPos, yPos, zPos); } }
        
    }
    
    void generateNether (Random random, int xChunk, int zChunk, World world)
    {
        int xPos, yPos, zPos;
        if (NuclearRelativistics.oreGenPlutonium)
        { for (int i = 0; i < NuclearRelativistics.oreRarityPlutonium; i++) { xPos = xChunk + random.nextInt(16); yPos = random.nextInt(128); zPos = zChunk + random.nextInt(16);
                plutonium.generate(world, random, xPos, yPos, zPos); } }
    }
 
    /**
     * Adds an Ore Spawn to Minecraft. Simply register all Ores to spawn with this method in your Generation method in your IWorldGeneration extending Class
     * 
     * @param The Block to spawn
     * @param The World to spawn in
     * @param A Random object for retrieving random positions within the world to spawn the Block
     * @param An int for passing the X-Coordinate for the Generation method
     * @param An int for passing the Z-Coordinate for the Generation method
     * @param An int for setting the maximum X-Coordinate values for spawning on the X-Axis on a Per-Chunk basis
     * @param An int for setting the maximum Z-Coordinate values for spawning on the Z-Axis on a Per-Chunk basis
     * @param An int for setting the maximum size of a vein
     * @param An int for the Number of chances available for the Block to spawn per-chunk
     * @param An int for the minimum Y-Coordinate height at which this block may spawn
     * @param An int for the maximum Y-Coordinate height at which this block may spawn
     **/
    public void addOreSpawn(Block block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chancesToSpawn, int minY, int maxY)
    {
        assert maxY > minY : "The maximum Y must be greater than the Minimum Y";
        assert maxX > 0 && maxX <= 16 : "addOreSpawn: The Maximum X must be greater than 0 and less than 16";
        assert minY > 0 : "addOreSpawn: The Minimum Y must be greater than 0";
        assert maxY < 256 && maxY > 0 : "addOreSpawn: The Maximum Y must be less than 256 but greater than 0";
        assert maxZ > 0 && maxZ <= 16 : "addOreSpawn: The Maximum Z must be greater than 0 and less than 16";
 
        int diffBtwnMinMaxY = maxY - minY;
        for (int x = 0; x < chancesToSpawn; x++)
        {
            int posX = blockXPos + random.nextInt(maxX);
            int posY = minY + random.nextInt(diffBtwnMinMaxY);
            int posZ = blockZPos + random.nextInt(maxZ);
            (new WorldGenMinable(block, maxVeinSize)).generate(world, random, posX, posY, posZ);
        }
    }
    
    WorldGenMinable copper;
    WorldGenMinable tin;
    WorldGenMinable lead;
    WorldGenMinable silver;
    WorldGenMinable pitchblende;
    WorldGenMinable thorium;
    WorldGenMinable lithium;
    WorldGenMinable boron;
    WorldGenMinable plutonium;
}