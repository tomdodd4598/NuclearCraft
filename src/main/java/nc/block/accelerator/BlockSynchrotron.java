package nc.block.accelerator;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.block.machine.BlockMachine;
import nc.tile.accelerator.TileSynchrotron;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSynchrotron extends BlockMachine {
	public BlockSynchrotron(boolean active) {
		super(active, NuclearCraft.guiIdSynchrotron, "", "", "synchrotron");
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("nc:accelerator/synchrotron/" + "side" + (this.isActive ? "Active" : "Idle"));
		this.iconFront = iconRegister.registerIcon("nc:accelerator/synchrotron/" + "front" + (this.isActive ? "Active" : "Idle"));
		this.iconTop = iconRegister.registerIcon("nc:accelerator/synchrotron/" + "top" + (this.isActive ? "Active" : "Idle"));
		this.iconBottom = iconRegister.registerIcon("nc:accelerator/synchrotron/" + "bottom" + (this.isActive ? "Active" : "Idle"));
	}
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileSynchrotron();
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true; 
		} else {
			FMLNetworkHandler.openGui(player, NuclearCraft.instance, NuclearCraft.guiIdSynchrotron, world, x, y, z);
		}
		
		IChatComponent localIChatComponent;
		TileSynchrotron s = (TileSynchrotron) world.getTileEntity(x, y, z);
		double e = s.efficiency;
    	localIChatComponent = IChatComponent.Serializer.func_150699_a(""+e);
    	if (world.isRemote) {((ICommandSender) player).addChatMessage(localIChatComponent);}
		return true;
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack) {
		super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemstack);
		
		IChatComponent localIChatComponent;
    	localIChatComponent = IChatComponent.Serializer.func_150699_a("[{text:\"Use NuclearCraft's NEI info system or click here for help with the mod!\",color:white,italic:false,clickEvent:{action:open_url,value:\"http://minecraft.curseforge.com/projects/nuclearcraft-mod\"}}]");

    	if (world.isRemote) {((ICommandSender) entityLivingBase).addChatMessage(localIChatComponent);}
	}
	
	public static void updateBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		TileEntity tileentity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		keepInventory = true;
		if(active) {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.synchrotronActive);
		} else {
			worldObj.setBlock(xCoord, yCoord, zCoord, NCBlocks.synchrotronIdle);
		}
		keepInventory = false;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		if(tileentity != null) {
			tileentity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
	}
	
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(NCBlocks.synchrotronIdle);
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(NCBlocks.synchrotronIdle);
	}
	
	public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldMetadata) {
		if(!keepInventory) {
			TileSynchrotron tileentity = (TileSynchrotron) world.getTileEntity(x, y, z);
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