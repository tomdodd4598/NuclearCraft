package nc.block.generator;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.tile.generator.TileFusionReactor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFusionReactor extends BlockContainer {
	
	private Random rand = new Random();
	
	private static boolean keepInventory;

	public BlockFusionReactor(Material material) {
		super(material);
		this.setBlockBounds(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);
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
		return new TileFusionReactor();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
	this.blockIcon = iconRegister.registerIcon("nc:generator/fusionReactor/" + "dummy");
	}
	
	public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int q, float a, float b, float c) {
		if (!player.isSneaking()) {
			player.openGui(NuclearCraft.instance, NuclearCraft.guiIdFusionReactor, world, x, y, z);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		if (world.getBlock(x+1, y, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x, y, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y, z-1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x, y, z-1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y, z-1).getMaterial() != Material.air) return false;
		
		else if (world.getBlock(x, y+1, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y+1, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y+1, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x, y+1, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y+1, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y+1, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y+1, z-1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x, y+1, z-1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y+1, z-1).getMaterial() != Material.air) return false;
		
		else if (world.getBlock(x, y+2, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y+2, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y+2, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x, y+2, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y+2, z+1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y+2, z).getMaterial() != Material.air) return false;
		else if (world.getBlock(x-1, y+2, z-1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x, y+2, z-1).getMaterial() != Material.air) return false;
		else if (world.getBlock(x+1, y+2, z-1).getMaterial() != Material.air) return false;
		return true;
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
		
		world.setBlock(x+1, y, z, NCBlocks.fusionReactorBlock);
		world.setBlock(x+1, y, z+1, NCBlocks.fusionReactorBlock);
		world.setBlock(x, y, z+1, NCBlocks.fusionReactorBlock);
		world.setBlock(x-1, y, z+1, NCBlocks.fusionReactorBlock);
		world.setBlock(x-1, y, z, NCBlocks.fusionReactorBlock);
		world.setBlock(x-1, y, z-1, NCBlocks.fusionReactorBlock);
		world.setBlock(x, y, z-1, NCBlocks.fusionReactorBlock);
		world.setBlock(x+1, y, z-1, NCBlocks.fusionReactorBlock);
		
		world.setBlock(x, y+1, z, NCBlocks.fusionReactorBlock);
		world.setBlock(x+1, y+1, z, NCBlocks.fusionReactorBlock);
		world.setBlock(x+1, y+1, z+1, NCBlocks.fusionReactorBlock);
		world.setBlock(x, y+1, z+1, NCBlocks.fusionReactorBlock);
		world.setBlock(x-1, y+1, z+1, NCBlocks.fusionReactorBlock);
		world.setBlock(x-1, y+1, z, NCBlocks.fusionReactorBlock);
		world.setBlock(x-1, y+1, z-1, NCBlocks.fusionReactorBlock);
		world.setBlock(x, y+1, z-1, NCBlocks.fusionReactorBlock);
		world.setBlock(x+1, y+1, z-1, NCBlocks.fusionReactorBlock);
		
		world.setBlock(x, y+2, z, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x+1, y+2, z, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x+1, y+2, z+1, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x, y+2, z+1, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x-1, y+2, z+1, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x-1, y+2, z, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x-1, y+2, z-1, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x, y+2, z-1, NCBlocks.fusionReactorBlockTop);
		world.setBlock(x+1, y+2, z-1, NCBlocks.fusionReactorBlockTop);
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack) {
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
			((TileFusionReactor)world.getTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
		}
	}
	
	private void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isRemote) {
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
		return Item.getItemFromBlock(NCBlocks.fusionReactor);
	}
	
	public void breakBlock(World world, int x, int y, int z, Block oldblockID, int oldMetadata) {
	
		if(!keepInventory)
		{
			TileFusionReactor tileentity = (TileFusionReactor) world.getTileEntity(x, y, z);
			
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
	world.setBlockToAir(x, y+2, z);
	world.setBlockToAir(x+1, y+2, z);
	world.setBlockToAir(x+1, y+2, z+1);
	world.setBlockToAir(x, y+2, z+1);
	world.setBlockToAir(x-1, y+2, z+1);
	world.setBlockToAir(x-1, y+2, z);
	world.setBlockToAir(x-1, y+2, z-1);
	world.setBlockToAir(x, y+2, z-1);
	world.setBlockToAir(x+1, y+2, z-1);
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
		TileFusionReactor tileentity = (TileFusionReactor) world.getTileEntity(x, y, z);
		return tileentity.efficiency <= NuclearCraft.fusionComparatorEfficiency ? (int) (15D*tileentity.efficiency/NuclearCraft.fusionComparatorEfficiency) : 15;
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(NCBlocks.fusionReactor);
	}
}
