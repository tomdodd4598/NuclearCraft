package nc.block.reactor;

import nc.tile.other.TileTubing2;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTubing2 extends BlockContainer {

	public BlockTubing2() {
		super(Material.iron);
		
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
	}
	
	public int getRenderType() {
		return -1;
	}
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileTubing2();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
	this.blockIcon = iconRegister.registerIcon("nc:reactor/" + "accStraight2InHand");
	}
}
