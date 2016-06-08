package nc.handler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityMobThrowable extends EntityThrowable {

	private int throwerId;

	private float damage;

	public EntityMobThrowable(World world) {
		super(world);
	}

	public EntityMobThrowable(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	public EntityMobThrowable(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityMobThrowable(World world, EntityLivingBase shooter, EntityLivingBase target, float velocity, float wobble) {
		super(world, shooter);
		this.posY = shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D;
		double d0 = target.posX - shooter.posX;
		double d1 = target.boundingBox.minY + (double)(target.height / 3.0F) - this.posY;
		double d2 = target.posZ - shooter.posZ;
		double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		if (d3 >= 1.0E-7D) {
			float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
			yOffset = 0.0F;
			float f4 = (float) d3 * 0.2F;
			setThrowableHeading(d0, d1 + (double) f4, d2, velocity, wobble);
		}
	}

	@Override
	public EntityLivingBase getThrower() {
		EntityLivingBase thrower = super.getThrower();
		if (thrower == null) {
			return (EntityLivingBase) worldObj.getEntityByID(throwerId);
		}
		return thrower;
	}

	public float getDamage() {
		return damage;
	}

	public EntityMobThrowable setDamage(float amount) {
		this.damage = amount;
		return this;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("throwerId", (getThrower() == null ? -1 : getThrower().getEntityId()));
		compound.setFloat("damage", damage);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		throwerId = compound.getInteger("throwerId");
		damage = compound.getFloat("damage");
	}
}