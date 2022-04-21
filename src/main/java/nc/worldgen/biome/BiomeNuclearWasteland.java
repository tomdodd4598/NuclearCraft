package nc.worldgen.biome;

import java.util.Random;

import nc.entity.EntityFeralGhoul;
import nc.init.NCBlocks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.*;

public class BiomeNuclearWasteland extends NCBiome {
	
	public BiomeNuclearWasteland() {
		super(new BiomeProperties("Nuclear Wasteland").setBaseHeight(0.12F).setHeightVariation(0.02F).setTemperature(2F).setWaterColor(0x994C00).setRainDisabled());
		
		topBlock = NCBlocks.wasteland_earth.getDefaultState();
		fillerBlock = Blocks.SAND.getDefaultState();
		waterBlock = FluidRegistry.getFluid("corium").getBlock().getDefaultState();
		frozenBlock = Blocks.MAGMA.getDefaultState();
		
		setSpawnables();
		setFlowers();
	}
	
	protected void setSpawnables() {
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
		
		spawnableMonsterList.add(new Biome.SpawnListEntry(EntityFeralGhoul.class, Short.MAX_VALUE, 1, 1));
	}
	
	protected void setFlowers() {
		flowers.clear();
		addFlower(NCBlocks.glowing_mushroom.getDefaultState(), 10);
	}
	
	@Override
	public BiomeDecorator createBiomeDecorator() {
		return getModdedBiomeDecorator(new Decorator());
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return TREE_FEATURE;
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand) {
		return new WorldGenTallGrass(BlockTallGrass.EnumType.DEAD_BUSH);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float currentTemperature) {
		return 0x9DA071;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		return getModdedBiomeGrassColor(0x994C00);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
		return getModdedBiomeFoliageColor(0x994C00);
	}
	
	protected int portalGenChance = 256;
	
	public static class Decorator extends BiomeDecorator {
		
		@Override
		protected void genDecorations(Biome biomeIn, World worldIn, Random random) {
			super.genDecorations(biomeIn, worldIn, random);
			
			if (biomeIn instanceof BiomeNuclearWasteland) {
				BiomeNuclearWasteland wasteland = (BiomeNuclearWasteland) biomeIn;
				
				if (random.nextInt(wasteland.portalGenChance) == 0) {
					int x = random.nextInt(16) + 8;
					int y = random.nextInt(worldIn.getHeight());
					int z = random.nextInt(16) + 8;
					if (new WorldGenLakes(NCBlocks.wasteland_portal).generate(worldIn, random, chunkPos.add(x, y, z))) {
						wasteland.portalGenChance = 256;
					}
				}
				else {
					--wasteland.portalGenChance;
				}
			}
		}
	}
}
