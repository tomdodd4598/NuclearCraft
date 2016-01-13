package com.nr.mod.blocks.tileentities;

import java.util.Random;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import com.nr.mod.NuclearRelativistics;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockMachine extends BlockContainer {

	public Random rand = new Random();
	public final boolean isActive;
	@SideOnly(Side.CLIENT)
	public IIcon iconFront;
	public static boolean keepInventory;
	public int guiID;
	public String particleEffect1;
	public String particleEffect2;
	
	public BlockMachine(boolean active, int idgui, String pe1, String pe2) {
		super(Material.iron);
		isActive = active;
		guiID = idgui;
		particleEffect1 = pe1;
		particleEffect2 = pe2;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("nr:machines/" + "eMachine" + "Side");
		this.iconFront = iconRegister.registerIcon("nr:machines/" + "electricCrusher" + "Front" + (this.isActive ? "Active" : "Idle"));
	}

	public Item getItemDropped(int par1, Random random, int par3) {
		return super.getItemDropped(par1, random, par3);
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}
	
	public void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isRemote)
		{
			Block block = world.getBlock(x, y, z - 1);
			Block block1 = world.getBlock(x, y, z + 1);
			Block block2 = world.getBlock(x - 1, y, z);
			Block block3 = world.getBlock(x + 1, y, z);
			byte b0 = 3;
			if (block.func_149730_j() && !block1.func_149730_j()) {
				b0 = 3;
			}
			if (block1.func_149730_j() && !block.func_149730_j()) {
				b0 = 2;
			}
			if (block2.func_149730_j() && !block3.func_149730_j()) {
				b0 = 5;
			}
			if (block3.func_149730_j() && !block2.func_149730_j()) {
				b0 = 4;
			}
			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
	        }
	    }
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return metadata == 0 && side == 3 ? this.iconFront : (side == metadata ? this.iconFront : this.blockIcon);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true; 
		} else {
			FMLNetworkHandler.openGui(player, NuclearRelativistics.instance, guiID, world, x, y, z);
		}
		return true;
	}
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityElectricCrusher();
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (particleEffect1 != "" && particleEffect2 != "") {
			if(isActive) {
				int direction = world.getBlockMetadata(x,  y,  z);
				float x1 = (float) x + 0.5F;
				float y1 = (float) y + + 0.5F + random.nextFloat() * 0.8F - 0.4F;
				float z1 = (float) z + 0.5F;
				float f = 0.52F;
				float f1 = random.nextFloat() * 0.6F - 0.3F;
				if(direction == 4) {
					world.spawnParticle(particleEffect1, (double) (x1 - f), (double) (y1), (double) (z1 + f1), 0.0D, 0.0D, 0.0D);
					world.spawnParticle(particleEffect2, (double) (x1 - f), (double) (y1), (double) (z1 + f1), 0.0D, 0.0D, 0.0D);
				}
				else if(direction == 5) {
					world.spawnParticle(particleEffect1, (double) (x1 + f), (double) (y1), (double) (z1 + f1), 0.0D, 0.0D, 0.0D);
					world.spawnParticle(particleEffect2, (double) (x1 + f), (double) (y1), (double) (z1 + f1), 0.0D, 0.0D, 0.0D);
				}
				else if(direction == 2) {
					world.spawnParticle(particleEffect1, (double) (x1 + f1), (double) (y1), (double) (z1 - f), 0.0D, 0.0D, 0.0D);
					world.spawnParticle(particleEffect2, (double) (x1 + f1), (double) (y1), (double) (z1 - f), 0.0D, 0.0D, 0.0D);
				}
				else if(direction == 3) {
					world.spawnParticle(particleEffect1, (double) (x1 + f1), (double) (y1), (double) (z1 + f), 0.0D, 0.0D, 0.0D);
					world.spawnParticle(particleEffect2, (double) (x1 + f1), (double) (y1), (double) (z1 + f), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack) {
		int l = MathHelper.floor_double((double)(entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
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
			((TileEntityMachine)world.getTileEntity(x, y, z)).setGuiDisplayName(itemstack.getDisplayName());
		}
	}

	public static void updateBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {}
	
	public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldMetadata) {
		super.breakBlock(world, x, y, z, oldBlockID, oldMetadata);
	}
	
	public boolean hasComparatorInputOverride() {
		return true;
	}
	
	public int getComparatorInputOverride(World world, int x, int y, int z, int i) {
		return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(x, y, z));
	}
	
	public Block idPicked(World world, int x, int y, int z) {
		return null;
	}
}