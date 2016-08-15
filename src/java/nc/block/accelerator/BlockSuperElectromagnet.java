package nc.block.accelerator;

import java.util.Random;

import nc.block.NCBlocks;
import nc.tile.accelerator.TileSuperElectromagnet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSuperElectromagnet extends BlockContainer {
	private final boolean isActive;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;
	
	public BlockSuperElectromagnet(boolean isActive) {
	super(Material.iron);
	this.isActive = isActive;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("nc:accelerator/electromagnet/" + "side" + (this.isActive ? "Active" : "Idle"));
		this.iconTop = iconRegister.registerIcon("nc:accelerator/electromagnet/" + "top" + (this.isActive ? "Active" : "Idle"));
		this.iconBottom = iconRegister.registerIcon("nc:accelerator/electromagnet/" + "bottom" + (this.isActive ? "Active" : "Idle"));
	}

	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(NCBlocks.superElectromagnetIdle);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return (side == 0) ? this.iconBottom : ((side == 1) ? this.iconTop : this.blockIcon);
	}
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileSuperElectromagnet();
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack) {
		world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		if (itemstack.hasDisplayName()) {
			((TileSuperElectromagnet)world.getTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
		}
	}
	
	public static void updateBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		TileEntity tileentity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		
		if(active) {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.superElectromagnetActive);
		} else {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.superElectromagnetIdle);
		}
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		
		if(tileentity != null)
		{
			tileentity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(NCBlocks.superElectromagnetIdle);
	}
}