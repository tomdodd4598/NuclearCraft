package nc.block.generator;

import java.util.Random;

import nc.block.NCBlocks;
import nc.tile.generator.TileSolarPanel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSolarPanel extends BlockContainer {
	
	@SideOnly(Side.CLIENT)
	private IIcon iconSide;
	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockSolarPanel()
	{
	super(Material.iron);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon("nc:generator/solar/" + "top");
		this.iconSide = iconRegister.registerIcon("nc:generator/solar/" + "side");
		this.iconBottom = iconRegister.registerIcon("nc:generator/solar/" + "bottom");
	}

	public Item getItemDropped(int par1, Random random, int par3)
	{
		return Item.getItemFromBlock(NCBlocks.solarPanel);
	}
	
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
	}
	
	@SideOnly(Side.CLIENT)
	 public IIcon getIcon(int side, int metadata) {
		return side == 1 ? this.blockIcon : (side == 0 ? this.iconBottom : this.iconSide);
	    }
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		 return false;
	}
	
	public TileEntity createNewTileEntity(World world, int par1)
	{
		return new TileSolarPanel();
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack)
	{
		world.setBlockMetadataWithNotify(x, y, z, 0, 2);

	        if (itemstack.hasDisplayName())
	        {
	            //((TileSolarPanel)world.getTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
	        }
	}
	
	public boolean hasComparatorInputOverride()
	{
		return true;
	}
	
	public int getComparatorInputOverride(World world, int x, int y, int z, int i)
	{
		return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(x, y, z));
	}
	
	public Block idPicked(World world, int x, int y, int z)
	{
		return NCBlocks.solarPanel;
	}
	
}