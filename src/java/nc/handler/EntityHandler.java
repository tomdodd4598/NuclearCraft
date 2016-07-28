package nc.handler;

import java.util.Random;

import nc.NuclearCraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.registry.EntityRegistry;

public class EntityHandler {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerMonsters(Class entityClass, String name) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		long x = name.hashCode();
		Random random = new Random(x);
		int mainColor = random.nextInt() * 16777215;
		int subColor = random.nextInt() * 16777215;
		
		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		if(NuclearCraft.enableNuclearMonster) {
			EntityRegistry.addSpawn(entityClass, 3, 1, 1, EnumCreatureType.monster, BiomeGenBase.beach, BiomeGenBase.birchForest, BiomeGenBase.birchForestHills, BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.extremeHills, BiomeGenBase.forestHills, BiomeGenBase.mesa, BiomeGenBase.mesaPlateau, BiomeGenBase.plains, BiomeGenBase.river, BiomeGenBase.roofedForest, BiomeGenBase.savanna, BiomeGenBase.swampland, BiomeGenBase.taiga);
			EntityRegistry.addSpawn(entityClass, 8, 1, 1, EnumCreatureType.monster, BiomeGenBase.hell);
		}
		EntityRegistry.registerModEntity(entityClass, name, entityId, NuclearCraft.instance, 64, 1, true);
		EntityList.entityEggs.put(Integer.valueOf(entityId), new EntityList.EntityEggInfo(entityId, mainColor, subColor));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerPaul(Class entityClass, String name) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		long x = name.hashCode();
		Random random = new Random(x);
		int mainColor = random.nextInt() * 16777215;
		int subColor = random.nextInt() * 16777215;
		
		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		if(NuclearCraft.enablePaul) {
			EntityRegistry.addSpawn(entityClass, 1, 1, 1, EnumCreatureType.monster, BiomeGenBase.beach, BiomeGenBase.birchForest, BiomeGenBase.birchForestHills, BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.extremeHills, BiomeGenBase.forestHills, BiomeGenBase.mesa, BiomeGenBase.mesaPlateau, BiomeGenBase.plains, BiomeGenBase.river, BiomeGenBase.roofedForest, BiomeGenBase.savanna, BiomeGenBase.swampland, BiomeGenBase.taiga);
		}
		EntityRegistry.registerModEntity(entityClass, name, entityId, NuclearCraft.instance, 64, 1, true);
		EntityList.entityEggs.put(Integer.valueOf(entityId), new EntityList.EntityEggInfo(entityId, mainColor, subColor));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerNuke(Class entityClass, String name) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, NuclearCraft.instance, 64, 1, true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerEMP(Class entityClass, String name) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, NuclearCraft.instance, 64, 1, true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerAntimatterBomb(Class entityClass, String name) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, NuclearCraft.instance, 64, 1, true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerNuclearGrenade(Class entityClass, String name) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, NuclearCraft.instance, 64, 1, true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerEntityBullet(Class entityClass, String name) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, NuclearCraft.instance, 64, 1, true);
	}
}