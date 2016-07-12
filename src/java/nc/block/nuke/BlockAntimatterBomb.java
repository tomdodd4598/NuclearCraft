package nc.block.nuke;

import nc.entity.EntityAntimatterBombPrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAntimatterBomb extends BlockTNT {
    
	public BlockAntimatterBomb() {
        super();
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("nc:nuke/" + "antimatterBombOn");
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int int1, int int2) {
        return this.blockIcon;
    }
	
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion exp) {
		if (!world.isRemote) {
			EntityAntimatterBombPrimed entitybombprimed = new EntityAntimatterBombPrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), exp.getExplosivePlacedBy());
			entitybombprimed.fuse = world.rand.nextInt(entitybombprimed.fuse / 4) + entitybombprimed.fuse / 8;
			world.spawnEntityInWorld(entitybombprimed);
        }
    }
	
	public void func_150114_a(World world, int x, int y, int z, int int1, EntityLivingBase elb) {
        if (!world.isRemote) {
            if ((int1 & 1) == 1) {
            	EntityAntimatterBombPrimed entitybombprimed = new EntityAntimatterBombPrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), elb);
            	world.spawnEntityInWorld(entitybombprimed);
            	world.playSoundAtEntity(entitybombprimed, "nc:nukeAlarm", 4.0F, 1.0F);
            }
        }
    }
}
