package com.nr.mod.blocks.tileentities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFusionReactor extends BlockContainer {
	
	private Random rand = new Random();
	
	private static boolean keepInventory;

	public BlockFusionReactor(Material material) {
		super(material);
		
		this.setBlockBounds(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
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
		return new TileEntityFusionReactor();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
	this.blockIcon = iconRegister.registerIcon("nr:machines/" + "fusionReactorInHand");
	}
	
	public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int q, float a, float b, float c) {
		getBelow(world, x, y, z);
		if (!player.isSneaking()) {
			player.openGui(NuclearRelativistics.instance, NuclearRelativistics.guiIdFusionReactor, world, x, y - 2*getBelow(world, x, y, z), z);
			return true;
		} else {
			return false;
		}
	}
	
	public int getBelow(World world, int x, int y, int z) {
		int below = 0;
		for (int yDown = 1; yDown < 128; ++yDown) {
			if (world.getBlock(x, y - 2*yDown, z) == NRBlocks.fusionReactor) {
				below++;
			} else break;
		}
		return below;
	}
	
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
	if (world.getBlock(x+1, y, z) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x+1, y, z+1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x, y, z+1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x-1, y, z+1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x-1, y, z) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x-1, y, z-1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x, y, z-1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x+1, y, z-1) != Blocks.air) {
		return false;
	}
	
	else if (world.getBlock(x, y+1, z) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x+1, y+1, z) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x+1, y+1, z+1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x, y+1, z+1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x-1, y+1, z+1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x-1, y+1, z) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x-1, y+1, z-1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x, y+1, z-1) != Blocks.air) {
		return false;
	}
	else if (world.getBlock(x+1, y+1, z-1) != Blocks.air) {
		return false;
	}
	return true;
}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
		
		world.setBlock(x+1, y, z, NRBlocks.fusionReactorBlock);
		world.setBlock(x+1, y, z+1, NRBlocks.fusionReactorBlock);
		world.setBlock(x, y, z+1, NRBlocks.fusionReactorBlock);
		world.setBlock(x-1, y, z+1, NRBlocks.fusionReactorBlock);
		world.setBlock(x-1, y, z, NRBlocks.fusionReactorBlock);
		world.setBlock(x-1, y, z-1, NRBlocks.fusionReactorBlock);
		world.setBlock(x, y, z-1, NRBlocks.fusionReactorBlock);
		world.setBlock(x+1, y, z-1, NRBlocks.fusionReactorBlock);
		world.setBlock(x, y+1, z, NRBlocks.fusionReactorBlock);
		world.setBlock(x+1, y+1, z, NRBlocks.fusionReactorBlock);
		world.setBlock(x+1, y+1, z+1, NRBlocks.fusionReactorBlock);
		world.setBlock(x, y+1, z+1, NRBlocks.fusionReactorBlock);
		world.setBlock(x-1, y+1, z+1, NRBlocks.fusionReactorBlock);
		world.setBlock(x-1, y+1, z, NRBlocks.fusionReactorBlock);
		world.setBlock(x-1, y+1, z-1, NRBlocks.fusionReactorBlock);
		world.setBlock(x, y+1, z-1, NRBlocks.fusionReactorBlock);
		world.setBlock(x+1, y+1, z-1, NRBlocks.fusionReactorBlock);
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack)
	{
		int l = MathHelper.floor_double(entityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
		
		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		
		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
			
		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		
		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

		if (itemstack.hasDisplayName()) {
			((TileEntityFusionReactor)world.getTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
		}
		
		IChatComponent localIChatComponent;
    	localIChatComponent = IChatComponent.Serializer.func_150699_a("[{text:\"Click here for help with the mod!\",color:aqua,italic:false,clickEvent:{action:open_url,value:\"http://minecraft.curseforge.com/mc-mods/226254-nuclearcraft-mod-1-7-10\"}}]");
    	
    	if (world.isRemote) {((ICommandSender) entityLivingBase).addChatMessage(localIChatComponent);}
	}
	
	private void setDefaultDirection(World world, int x, int y, int z)
	 {
	        if (!world.isRemote)
	        {
	            Block block = world.getBlock(x, y, z - 1);
	            Block block1 = world.getBlock(x, y, z + 1);
	            Block block2 = world.getBlock(x - 1, y, z);
	            Block block3 = world.getBlock(x + 1, y, z);
	            byte b0 = 3;

	            if (block.func_149730_j() && !block1.func_149730_j())
	            {
	                b0 = 3;
	            }

	            if (block1.func_149730_j() && !block.func_149730_j())
	            {
	                b0 = 2;
	            }

	            if (block2.func_149730_j() && !block3.func_149730_j())
	            {
	                b0 = 5;
	            }

	            if (block3.func_149730_j() && !block2.func_149730_j())
	            {
	                b0 = 4;
	            }

	            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
	        }
	    }
	
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(NRBlocks.fusionReactor);
	}
	
	public void breakBlock(World world, int x, int y, int z, Block oldblockID, int oldMetadata) {
	
		if(!keepInventory)
		{
			TileEntityFusionReactor tileentity = (TileEntityFusionReactor) world.getTileEntity(x, y, z);
			
			if(tileentity != null)
			{
				if (tileentity.isMain()) {
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
				}
				
				world.func_147453_f(x, y, z, oldblockID);
			}
		}	
		
	world.setBlockToAir(x+1, y, z);
	world.setBlockToAir(x+1, y, z+1);
	world.setBlockToAir(x, y, z+1);
	world.setBlockToAir(x-1, y, z+1);
	world.setBlockToAir(x-1, y, z);
	world.setBlockToAir(x-1, y, z-1);
	world.setBlockToAir(x, y, z-1);
	world.setBlockToAir(x+1, y, z-1);
	world.setBlockToAir(x, y+1, z);
	world.setBlockToAir(x+1, y+1, z);
	world.setBlockToAir(x+1, y+1, z+1);
	world.setBlockToAir(x, y+1, z+1);
	world.setBlockToAir(x-1, y+1, z+1);
	world.setBlockToAir(x-1, y+1, z);
	world.setBlockToAir(x-1, y+1, z-1);
	world.setBlockToAir(x, y+1, z-1);
	world.setBlockToAir(x+1, y+1, z-1);
	super.breakBlock(world, x, y, z, oldblockID, oldMetadata);
	}
	
	public static void updateBlockState(World worldObj, int xCoord, int yCoord, int zCoord)
	{
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		TileEntity tileentity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		keepInventory = true;

		keepInventory = false;
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		
		if(tileentity != null)
		{
			tileentity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
	}
	
	public boolean hasComparatorInputOverride()
	{
		return true;
	}
	
	public int getComparatorInputOverride(World world, int x, int y, int z, int i)
	{
		TileEntityFusionReactor tileentity = (TileEntityFusionReactor) world.getTileEntity(x, y, z);
		return (int) Math.ceil(tileentity.efficiency/7);
	}
	
	public Block idPicked(World world, int x, int y, int z)
	{
		return NRBlocks.fusionReactor;
	}
}
