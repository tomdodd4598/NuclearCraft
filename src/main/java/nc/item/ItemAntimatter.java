package nc.item;

import nc.NuclearCraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;

public class ItemAntimatter extends ItemNC {
	
	public ItemAntimatter(String nam, String... lines) {
		super("", nam, lines);
	}

	public boolean onEntityItemUpdate(EntityItem entityItem) {
		if (entityItem != null) {
			if (entityItem.onGround) {
				int x = (int) Math.floor(entityItem.posX); int y = (int) Math.floor(entityItem.posY); int z = (int) Math.floor(entityItem.posZ);
		        
		        for (int i = -((int)(0.2D*NuclearCraft.explosionRadius)); i <= ((int)(0.2D*NuclearCraft.explosionRadius)); i++) {
		    		for (int j = -((int)(0.2D*NuclearCraft.explosionRadius)); j <= ((int)(0.2D*NuclearCraft.explosionRadius)); j++) {
		    			for (int k = -((int)(0.2D*NuclearCraft.explosionRadius)); k <= ((int)(0.2D*NuclearCraft.explosionRadius)); k++) {
		    				if (i*i + j*j + k*k <= ((int)(0.2D*NuclearCraft.explosionRadius))*((int)(0.2D*NuclearCraft.explosionRadius)) && entityItem.worldObj.getBlock(x + i, y + j, z + k) != Blocks.bedrock) {
		    					entityItem.worldObj.setBlockToAir(x + i, y + j, z + k);
		    				}
		    			}
		    		}
		        }
		        entityItem.worldObj.playSoundEffect(entityItem.posX, entityItem.posY, entityItem.posZ, "random.explode", 4.0F, 1.0F);
		        entityItem.worldObj.playSoundEffect(entityItem.posX, entityItem.posY, entityItem.posZ, "nc:shield2", 12.0F, 1.0F);
				entityItem.setDead();
		        return true;
			}
		}
		return false;
    }
}
