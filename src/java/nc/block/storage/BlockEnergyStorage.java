package nc.block.storage;

import java.util.ArrayList;

import nc.tile.storage.TileStorage;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockEnergyStorage extends BlockContainer {

	@SideOnly(Side.CLIENT)
	public IIcon blockIcon1;
	@SideOnly(Side.CLIENT)
	public IIcon blockIcon2;
	@SideOnly(Side.CLIENT)
	public IIcon iconTop;
	@SideOnly(Side.CLIENT)
	public IIcon iconTop1;
	@SideOnly(Side.CLIENT)
	public IIcon iconTop2;
	
	public String name;
	
	public BlockEnergyStorage(String nam) {
		super(Material.iron);
		name = nam;
	}
	
	public int damageDropped(int par1) {
		return par1;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon("nc:storage/" + name + "/" + "sideIn");
		blockIcon1 = iconRegister.registerIcon("nc:storage/" + name + "/" + "sideOut");
		blockIcon2 = iconRegister.registerIcon("nc:storage/" + name + "/" + "sideDisabled");
		iconTop = iconRegister.registerIcon("nc:storage/" + name + "/" + "topIn");
		iconTop1 = iconRegister.registerIcon("nc:storage/" + name + "/" + "topOut");
		iconTop2 = iconRegister.registerIcon("nc:storage/" + name + "/" + "topDisabled");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		TileEntity te = access.getTileEntity(x, y, z);
		if (te instanceof TileStorage) {
			TileStorage t = (TileStorage) access.getTileEntity(x, y, z);
			if (side <= 1) {
				return t.sideMode[side] == 0 ? iconTop : (t.sideMode[side] == 1 ? iconTop1 : iconTop2);
			} else {
				return t.sideMode[side] == 0 ? blockIcon : (t.sideMode[side] == 1 ? blockIcon1 : blockIcon2);
			}
		}
		return side <= 1 ? iconTop : blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return side <= 1 ? iconTop : blockIcon;
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player != null) {
			if (world.getTileEntity(x, y, z) instanceof TileStorage) {
				TileStorage te = (TileStorage) world.getTileEntity(x, y, z);
				if (world.isRemote && !player.isSneaking()) player.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "Mode: " + (te.sideMode[side] == 0 ? "Output" : (te.sideMode[side] == 1 ? "Disabled" : "Input"))));
				if (!player.isSneaking()) te.incrSide(side);
			}
		}
		return true;
	}
	
	public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return Lists.newArrayList(getDataDrop(world, x, y, z, world.getTileEntity(x, y, z) != null ? world.getTileEntity(x, y, z) : null));
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return getDataDrop(world, x, y, z, world.getTileEntity(x, y, z) != null ? world.getTileEntity(x, y, z) : null);
	}
	
	public ItemStack getDataDrop(World world, int x, int y, int z, TileEntity t) {
		ItemStack itemStack = new ItemStack(this, 1);
		processDrop(world, x, y, z, t, itemStack);
		return itemStack;
	}
	
	protected void processDrop(World world, int x, int y, int z, TileEntity t, ItemStack drop) {
		NBTTagCompound tag = new NBTTagCompound();
		if(t != null && t instanceof TileStorage) { // THE ISSUE IS HERE - WHY IS THE TILE NULL?
			((TileStorage) t).writeNBT(tag);
		}
		drop.setTagCompound(tag);
	}
	
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		super.harvestBlock(world, player, x, y, z, meta);
		world.setBlockToAir(x, y, z);
	}
	
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		if (willHarvest) return true;
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, player, stack);
		world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		TileStorage t = (TileStorage) world.getTileEntity(x, y, z);
		if(t == null) {
			return;
		}
		if(stack.stackTagCompound != null) {
			t.readNBT(stack.stackTagCompound);
		}
		if(world.isRemote) {
			return;
		}
		world.markBlockForUpdate(x, y, z);
	}
}
