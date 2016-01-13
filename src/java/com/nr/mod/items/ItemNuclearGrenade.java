package com.nr.mod.items;

import com.nr.mod.entity.EntityNuclearGrenade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNuclearGrenade extends Item {

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par3EntityPlayer.capabilities.isCreativeMode) {
			 --par1ItemStack.stackSize;
			}

	if (!par2World.isRemote) {
		par2World.spawnEntityInWorld(new EntityNuclearGrenade(par2World, par3EntityPlayer));
	} return par1ItemStack;
	}
}
