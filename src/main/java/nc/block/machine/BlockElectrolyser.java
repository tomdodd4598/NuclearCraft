package nc.block.machine;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.tile.machine.TileElectrolyser;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockElectrolyser extends BlockMachine {
	@SideOnly(Side.CLIENT)
	public IIcon iconBottom;
	
	public BlockElectrolyser(boolean active) {
		super(active, NuclearCraft.guiIdElectrolyser, "", "", "electrolyser");
	}
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileElectrolyser();
	}
	
	public static void updateBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		TileEntity tileentity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		keepInventory = true;
		if(active) {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.electrolyserActive);
		} else {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.electrolyserIdle);
		}
		keepInventory = false;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		if(tileentity != null) {
			tileentity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
	}
	
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(NCBlocks.electrolyserIdle);
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(NCBlocks.electrolyserIdle);
	}
	
	public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldMetadata) {
		if(!keepInventory) {
			TileElectrolyser tileentity = (TileElectrolyser) world.getTileEntity(x, y, z);
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
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if(isActive) {
			float x1 = (float) x + 0.5F;
			float y1 = (float) y + + 0.5F + random.nextFloat() * 0.8F - 0.4F;
			float y2 = (float) y + 1 + random.nextFloat() / 16.0F;
			float z1 = (float) z + 0.5F;
			float a = random.nextFloat() * 0.4F - 0.2F;
			float a1 = random.nextFloat() * 0.4F - 0.2F;
			float f = 0.52F;
			float f1 = random.nextFloat() * 0.6F - 0.3F;
			world.spawnParticle("splash", (double) (x1 + a), (double) (y2), (double) (z1 + a1), 0.0D, 0.0D, 0.0D);
			world.spawnParticle("reddust", (double) (x1 - f), (double) (y1), (double) (z1 + f1), 0.0D, 0.0D, 0.0D);
			world.spawnParticle("reddust", (double) (x1 + f), (double) (y1), (double) (z1 + f1), 0.0D, 0.0D, 0.0D);
			world.spawnParticle("reddust", (double) (x1 + f1), (double) (y1), (double) (z1 - f), 0.0D, 0.0D, 0.0D);
			world.spawnParticle("reddust", (double) (x1 + f1), (double) (y1), (double) (z1 + f), 0.0D, 0.0D, 0.0D);
		}
	}
}