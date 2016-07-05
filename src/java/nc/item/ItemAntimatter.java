package nc.item;

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
		        
		        for (int i = -20; i <= 20; i++) {
		    		for (int j = -20; j <= 20; j++) {
		    			for (int k = -20; k <= 20; k++) {
		    				if (i*i + j*j + k*k <= 400 && entityItem.worldObj.getBlock(x + i, y + j, z + k) != Blocks.bedrock) {
		    					entityItem.worldObj.setBlockToAir(x + i, y + j, z + k);
		    				}
		    			}
		    		}
		        }
		        entityItem.worldObj.playSoundEffect(entityItem.posX, entityItem.posY, entityItem.posZ, "random.explode", 4.0F, (1.0F + (entityItem.worldObj.rand.nextFloat() - entityItem.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
				entityItem.setDead();
		        return true;
			}
		}
		return false;
    }
}
