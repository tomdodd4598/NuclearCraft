package nc.block.nuke;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEMPExploding extends Block {

	public BlockEMPExploding() {
	super(Material.tnt);
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
}