package nc.block.nuke;

import nc.entity.EntityNukePrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNuke extends BlockTNT {
    
	public BlockNuke() {
        super();
    }
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
	this.blockIcon = iconRegister.registerIcon("nc:nuke/" + "nuke" + "Side");
	this.iconBottom = iconRegister.registerIcon("nc:nuke/" + "nuke" + "Bottom");
	this.iconTop = iconRegister.registerIcon("nc:nuke/" + "nuke" + "Top");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return side == 0 ? this.iconBottom : (side == 1 ? this.iconTop : this.blockIcon);
	}
	
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion exp) {
		if (!world.isRemote) {
			EntityNukePrimed entitynukeprimed = new EntityNukePrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), exp.getExplosivePlacedBy());
			entitynukeprimed.fuse = world.rand.nextInt(entitynukeprimed.fuse / 4) + entitynukeprimed.fuse / 8;
			world.spawnEntityInWorld(entitynukeprimed);
        }
    }
	
	public void func_150114_a(World world, int x, int y, int z, int int1, EntityLivingBase elb) {
        if (!world.isRemote) {
            if ((int1 & 1) == 1) {
            	EntityNukePrimed entitynukeprimed = new EntityNukePrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), elb);
            	world.spawnEntityInWorld(entitynukeprimed);
            	world.playSoundAtEntity(entitynukeprimed, "nc:nukeAlarm", 4.0F, 1.0F);
            }
        }
    }
}
