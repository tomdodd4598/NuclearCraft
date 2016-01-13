package com.nr.mod.utils;

import net.minecraft.entity.player.EntityPlayer;

public interface IWrenchable {
	public boolean onWrench(EntityPlayer player, int hitSide);
}