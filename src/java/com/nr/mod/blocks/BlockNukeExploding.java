package com.nr.mod.blocks;

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
	private IIcon iconBottom;

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
	this.blockIcon = iconRegister.registerIcon("nr:nuke/" + "nuke" + "SideOn");
	this.iconBottom = iconRegister.registerIcon("nr:nuke/" + "nuke" + "Bottom");
	this.iconTop = iconRegister.registerIcon("nr:nuke/" + "nuke" + "Top");
	}

	@SideOnly(Side.CLIENT)
	 public IIcon getIcon(int side, int metadata) {
		return side == 0 ? this.iconBottom : (side == 1 ? this.iconTop : this.blockIcon);
	}
}