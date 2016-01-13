package com.nr.mod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import com.nr.mod.handlers.BombType;
import com.nr.mod.handlers.EntityBomb;
import com.nr.mod.handlers.NRExplosion;

public class EntityNuclearGrenade extends EntityThrowable {

	public EntityNuclearGrenade(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}
	public EntityNuclearGrenade(World par1World, EntityLivingBase par2EntityLivingBase) {
		super(par1World, par2EntityLivingBase);
	}
	public EntityNuclearGrenade(World par1World) {
		super(par1World);
	}

	protected void onImpact(MovingObjectPosition mop) {
		if(mop.typeOfHit == MovingObjectType.BLOCK) {
			switch(mop.sideHit) {
				case 0: //BOTTOM
					mop.blockY--;
					break;
				case 1: //TOP
					mop.blockY++;
					break;
				case 2: //EAST
					mop.blockZ--;
					break;
				case 3: //WEST
					mop.blockZ++;
					break;
				case 4: //NORTH
					mop.blockX--;
					break;
				case 5: //SOUTH
					mop.blockX++;
					break;
			}
			if (!this.worldObj.isRemote) {
				this.explode(worldObj, this.posX, this.posY, this.posZ, 30F, BombType.BOMB_STANDARD);
				this.setDead();
			}
		}
	}

	private void explode(World world, double x, double y, double z, float radius, BombType type) {
		NRExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.posX, (double)this.posY, (double)this.posZ, 30F, 1000F, true);
	}
}