package nc.block.nuke;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNukeExploding extends Block {

	public BlockNukeExploding() {
	super(Material.tnt);
	}
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
	this.blockIcon = iconRegister.registerIcon("nc:nuke/" + "nuke" + "SideOn");
	this.iconBottom = iconRegister.registerIcon("nc:nuke/" + "nuke" + "Bottom");
	this.iconTop = iconRegister.registerIcon("nc:nuke/" + "nuke" + "Top");
	}

	@SideOnly(Side.CLIENT)
	 public IIcon getIcon(int side, int metadata) {
		return side == 0 ? this.iconBottom : (side == 1 ? this.iconTop : this.blockIcon);
	}
}