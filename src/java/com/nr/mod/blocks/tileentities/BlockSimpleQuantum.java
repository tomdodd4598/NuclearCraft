package com.nr.mod.blocks.tileentities;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.nr.mod.blocks.NRBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSimpleQuantum extends BlockContainer {

	public BlockSimpleQuantum(boolean s) {
		super(Material.iron);
		spin = s;
	}
	
	public final boolean spin;
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntitySimpleQuantum();
	}

	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister i) {
		blockIcon = i.registerIcon("nr:" + this.getUnlocalizedName().substring(5));
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return blockIcon;
	}
	
	public static void set(double a, boolean s, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		TileEntitySimpleQuantum tileentity = (TileEntitySimpleQuantum) worldObj.getTileEntity(xCoord, yCoord, zCoord);
		if(s) worldObj.setBlock(xCoord, yCoord, zCoord, NRBlocks.simpleQuantumUp);
		else worldObj.setBlock(xCoord, yCoord, zCoord, NRBlocks.simpleQuantumDown);
		tileentity.angle = a;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, i, 2);
		if(tileentity != null) {
			tileentity.validate();
			worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			TileEntitySimpleQuantum t = (TileEntitySimpleQuantum) world.getTileEntity(x, y, z);
			double newAngle = (double) (player.rotationYaw+360)%360;
			double p = Math.pow(Math.cos(((t.angle-newAngle)/2)*(Math.PI/180)), 2);
			double rand = new Random().nextDouble();
			boolean s = false;
			if (player != null) {
				if (!world.isRemote) {
					if (p == 0) {
						t.spin=false; s=false;
					} else if (p >= rand) {
						t.spin=true; s=true;
					} else {
						t.spin=false; s=false;
					}
					set(newAngle, s, world, x, y, z);
				}
				if (s==true) {
					t.angle = newAngle;
				} else {
					t.angle = 180 + newAngle;
				}
			}
			if (world.isRemote) player.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + ("Angle: " + Math.round(newAngle))));
		}
		return true;
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack) {
		double l = (double) entityLivingBase.rotationYaw;
		TileEntitySimpleQuantum t = (TileEntitySimpleQuantum) world.getTileEntity(x, y, z);
		t.angle = l;
		t.spin=true;
		
		if (world.isRemote) ((ICommandSender) entityLivingBase).addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + ("Angle: " + Math.round(t.angle))));
	}

	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return false;
	}
}