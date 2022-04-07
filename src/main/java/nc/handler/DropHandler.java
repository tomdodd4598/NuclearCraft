package nc.handler;

import static nc.config.NCConfig.*;

import java.util.*;

import it.unimi.dsi.fastutil.objects.*;
import nc.capability.radiation.resistance.IRadiationResistance;
import nc.entity.EntityFeralGhoul;
import nc.enumm.MetaEnums;
import nc.init.NCItems;
import nc.radiation.RadiationHelper;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler {
	
	private final Random rand = new Random();
	
	@SubscribeEvent
	public void addEntityDrops(LivingDropsEvent event) {
		Entity entity = event.getEntity();
		if (entity.getEntityWorld().getGameRules().getBoolean("doMobLoot")) {
			if (entity instanceof EntityFeralGhoul) {
				if (rand.nextInt(50) < 1) {
					event.getDrops().add(entityItem(entity, new ItemStack(NCItems.ingot, 1, 3), "ingotThorium"));
				}
				if (rand.nextInt(50) < 1) {
					event.getDrops().add(entityItem(entity, new ItemStack(NCItems.ingot, 1, 4), "ingotUranium"));
				}
			}
			
			if (rare_drops) {
				if (entity instanceof EntityMob) {
					if (rand.nextInt(100) < 1) {
						event.getDrops().add(entityItem(entity, new ItemStack(NCItems.dominos, 1), null));
					}
				}
			}
		}
	}
	
	private static EntityItem entityItem(Entity entity, ItemStack stack, String oreName) {
		return new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, OreDictHelper.getPrioritisedCraftingStack(stack, oreName));
	}
	
	private final Object2ObjectMap<FourPos, TileEntity> tileMap = new Object2ObjectOpenHashMap<>();
	
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) {
		World world = event.getWorld();
		if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
			BlockPos pos = event.getPos();
			TileEntity tile = world.getTileEntity(pos);
			
			if (tile != null) {
				tileMap.put(new FourPos(pos, world.provider.getDimension()), tile);
			}
		}
	}
	
	@SubscribeEvent
	public void addBlockDrops(HarvestDropsEvent event) {
		World world = event.getWorld();
		if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
			List<ItemStack> drops = event.getDrops();
			Block block = event.getState().getBlock();
			
			if (!event.isSilkTouching()) {
				if (ore_drops[0]) {
					blockDrop(drops, block, NCItems.gem, 1, 1, 0, Blocks.REDSTONE_ORE, 25);
				}
				if (ore_drops[0]) {
					blockDrop(drops, block, NCItems.gem, 1, 1, 0, Blocks.LIT_REDSTONE_ORE, 25);
				}
				if (ore_drops[1]) {
					blockDrop(drops, block, NCItems.dust, 1, 1, 9, Blocks.COAL_ORE, 18);
				}
				if (ore_drops[2]) {
					blockDrop(drops, block, NCItems.dust, 1, 1, 10, Blocks.QUARTZ_ORE, 18);
				}
				if (ore_drops[3]) {
					blockDrop(drops, block, NCItems.gem, 2, 2, 2, Blocks.LAPIS_ORE, 95);
				}
				if (ore_drops[4]) {
					blockDrop(drops, block, NCItems.gem_dust, 1, 1, 6, Blocks.COAL_ORE, 18);
				}
				if (ore_drops[5]) {
					blockDrop(drops, block, NCItems.gem, 0, 1, 3, Blocks.DIAMOND_ORE, 100);
				}
				if (ore_drops[6]) {
					blockDrop(drops, block, NCItems.gem, 1, 3, 4, Blocks.DIAMOND_ORE, 100);
				}
			}
			
			FourPos pos = new FourPos(event.getPos(), world.provider.getDimension());
			TileEntity tile = tileMap.get(pos);
			
			if (tile != null) {
				IRadiationResistance resistance = RadiationHelper.getRadiationResistance(tile);
				if (resistance != null) {
					for (int i = MetaEnums.RadShieldingType.values().length; i > 0; --i) {
						if (resistance.getShieldingRadResistance() >= radiation_shielding_level[i - 1]) {
							drops.add(new ItemStack(NCItems.rad_shielding, 1, i - 1));
							break;
						}
					}
				}
				tileMap.remove(pos);
			}
		}
	}
	
	private void blockDrop(List<ItemStack> drops, Block block, Item drop, int min, int max, int meta, Block target, int chance) {
		if (block == target && rand.nextInt(100) < chance) {
			drops.add(new ItemStack(drop, min + rand.nextInt(max - min + 1), meta));
		}
	}
}
