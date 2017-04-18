package nc.block.machine;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.tile.machine.TileHeliumExtractor;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockHeliumExtractor extends BlockMachine {
	public BlockHeliumExtractor(boolean active) {
		super(active, NuclearCraft.guiIdHeliumExtractor, "portal", "reddust", "heliumExtractor");
	}

	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileHeliumExtractor();
	}
	
	public static void updateBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		TileEntity tileentity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		keepInventory = true;
		if(active) {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.heliumExtractorActive);
		} else {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.heliumExtractorIdle);
		}
		keepInventory = false;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		if(tileentity != null) {
			tileentity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
	}
	
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(NCBlocks.heliumExtractorIdle);
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(NCBlocks.heliumExtractorIdle);
	}
	
	public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldMetadata) {
		if(!keepInventory) {
			TileHeliumExtractor tileentity = (TileHeliumExtractor) world.getTileEntity(x, y, z);
			if(tileentity != null) {
				for(int i = 0; i < tileentity.getSizeInventory(); i++) {
					ItemStack itemstack = tileentity.getStackInSlot(i);
					if(itemstack != null) {
						float f = this.rand.nextFloat() * 0.8F + 0.1F;
						float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
						float f2 = this.rand.nextFloat() * 0.8F + 0.1F;
						while(itemstack.stackSize > 0) {
							int j = this.rand.nextInt(21) + 10;
							if(j > itemstack.stackSize) {
								j = itemstack.stackSize;
							}
							itemstack.stackSize -= 	j;
							EntityItem item = new EntityItem(world, (double) ((float) x + f), ((float) y + f1), ((float) z + f2),
							new ItemStack (itemstack.getItem(), j, itemstack.getItemDamage()));
							if(itemstack.hasTagCompound()) {
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
}