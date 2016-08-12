package nc.block.generator;

import java.util.Random;

import nc.block.NCBlocks;
import nc.tile.generator.TileWRTG;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWRTG extends BlockContainer {
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	public BlockWRTG(){
		super(Material.iron);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
	this.blockIcon = iconRegister.registerIcon("nc:generator/RTG/" + "WRTGSide");
	this.iconTop = iconRegister.registerIcon("nc:generator/RTG/" + "WRTG");
	}

	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(NCBlocks.WRTG);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return side < 2 ? this.iconTop : this.blockIcon;
	}
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileWRTG();
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack) {
		world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		if (itemstack.hasDisplayName()) {
			//((TileWRTG)world.getTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
		}
	}
	
}