package nc.block.nuke;

import nc.entity.EntityEMPPrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEMP extends BlockTNT {
    
	public BlockEMP() {
        super();
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("nc:nuke/" + "EMP");
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int int1, int int2) {
        return this.blockIcon;
    }
	
	public void func_150114_a(World world, int x, int y, int z, int int1, EntityLivingBase elb) {
        if (!world.isRemote) {
            if ((int1 & 1) == 1) {
            	EntityEMPPrimed entitynukeprimed = new EntityEMPPrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), elb);
            	world.spawnEntityInWorld(entitynukeprimed);
            	world.playSoundAtEntity(entitynukeprimed, "nc:nukeAlarm", 4.0F, 1.0F);
            }
        }
    }
}
