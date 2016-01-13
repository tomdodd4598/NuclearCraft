package com.nr.mod.blocks.tileentities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNuclearWorkspace extends BlockContainer {
	
	private Random rand = new Random();
	
	private static boolean keepInventory;

	public BlockNuclearWorkspace(Material material) {
		super(material);
		
		this.setBlockBounds(0F, 0F, 0F, 1F, 0.75F, 1F);
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
		return new TileEntityNuclearWorkspace();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
	this.blockIcon = iconRegister.registerIcon("nr:machines/" + "nuclearWorkspaceInHand");
	}
	
	public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int q, float a, float b, float c) {
		
		if (!player.isSneaking()) {
			player.openGui(NuclearRelativistics.instance, NuclearRelativistics.guiIdNuclearWorkspace, world, x, y, z);
			return true;
		} else {
			return false;
		}
	}
	
	public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldMetadata) {
		if(!keepInventory)
		{
			TileEntityNuclearWorkspace tileentity = (TileEntityNuclearWorkspace) world.getTileEntity(x, y, z);
			
			if(tileentity != null)
			{
				for(int i = 0; i < tileentity.getSizeInventory(); i++)
				{
					ItemStack itemstack = tileentity.getStackInSlot(i);
					
					if(itemstack != null)
					{
						float f = this.rand.nextFloat() * 0.8F + 0.1F;
						float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
						float f2 = this.rand.nextFloat() * 0.8F + 0.1F;
						
						while(itemstack.stackSize > 0)
						{
							int j = this.rand.nextInt(21) + 10;
							
							if(j > itemstack.stackSize)
							{
								j = itemstack.stackSize;
							}
							
							itemstack.stackSize -= 	j;
							EntityItem item = new EntityItem(world, (double) ((float) x + f), ((float) y + f1), ((float) z + f2),
							new ItemStack (itemstack.getItem(), j, itemstack.getItemDamage()));
							
							if(itemstack.hasTagCompound())
							{
								item.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
							}
							
							float f3 = 0.05F;
							item.motionX = (double)((float) this.rand.nextGaussian() * f3);
							item.motionY = (double)((float) this.rand.nextGaussian() * f3 + 0.2F);
							item.motionZ = (double)((float) this.rand.nextGaussian() * f3);
							
							world.spawnEntityInWorld(item);
							
						}
					}
				}
				
				world.func_147453_f(x, y, z, oldBlockID);
			}
		}
		
		super.breakBlock(world, x, y, z, oldBlockID, oldMetadata);
	}
	
	public Block idPicked(World world, int x, int y, int z)
	{
		return NRBlocks.nuclearWorkspace;
	}
}
