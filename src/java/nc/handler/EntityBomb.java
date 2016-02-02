package nc.handler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBomb extends EntityMobThrowable implements IEntityBomb {
	/** Time until explosion */
	private int fuseTime = 24;

	/** Uses ItemBomb's radius if this value is zero */
	private float radius = 0.0F;

	/** Whether this bomb is capable of destroying blocks */
	private boolean canGrief = true;

	/** Factor by which affected entity's motion will be multiplied */
	protected float motionFactor = 1.0F;

	/** Factor by which radius of block destruction is multiplied */
	protected float destructionFactor = 1.0F;

	/** Watchable object index for bomb's type */
	private static final int BOMBTYPE_DATAWATCHER_INDEX = 22;

	public EntityBomb(World world) {
		super(world);
	}

	public EntityBomb(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	public EntityBomb(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityBomb(World world, EntityLivingBase shooter, EntityLivingBase target, float velocity, float wobble) {
		super(world, shooter, target, velocity, wobble);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		setDamage(0.0F);
		dataWatcher.addObject(BOMBTYPE_DATAWATCHER_INDEX, BombType.BOMB_STANDARD.ordinal());
	}

	/**
	 * Adds time to bomb's fuse
	 */
	public EntityBomb addTime(int time) {
		fuseTime = Math.max(fuseTime + time, fuseTime);
		return this;
	}

	/**
	 * Sets the bomb's fuse time
	 */
	public EntityBomb setTime(int time) {
		fuseTime = Math.max(time, 1);
		return this;
	}

	/**
	 * Sets this bomb's radius
	 */
	public EntityBomb setRadius(float radius) {
		this.radius = radius;
		return this;
	}

	@Override
	public float getMotionFactor() {
		return motionFactor;
	}

	/**
	 * Sets amount by which entity's motion will be multiplied
	 */
	public EntityBomb setMotionFactor(float amount) {
		motionFactor = amount;
		return this;
	}

	@Override
	public float getDestructionFactor() {
		return destructionFactor;
	}

	/**
	 * Sets the amount by which block destruction radius will be multiplied
	 */
	public EntityBomb setDestructionFactor(float factor) {
		this.destructionFactor = factor;
		return this;
	}

	/**
	 * Sets this bomb to not destroy blocks, but still cause damage 
	 */
	public EntityBomb setNoGrief() {
		canGrief = false;
		return this;
	}

	@Override
	public BombType getType() {
		return BombType.values()[dataWatcher.getWatchableObjectInt(BOMBTYPE_DATAWATCHER_INDEX)];
	}

	/**
	 * Set this bomb's {@link BombType}
	 */
	public EntityBomb setType(BombType type) {
		dataWatcher.updateObject(BOMBTYPE_DATAWATCHER_INDEX, type.ordinal());
		return this;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.075F;
	}

	@Override
	protected float func_70182_d() {
		return 0.5F;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.boundingBox;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.03999999910593033D;
		// func_145771_j is pushOutOfBlocks
		noClip = func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
		moveEntity(motionX, motionY, motionZ);

		float f = 0.98F;
		if (onGround) {
			f = 0.58800006F;
			Block block = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

			if (block.getMaterial() != Material.air) {
				f = block.slipperiness * 0.98F;
			}
		}

		motionX *= (double) f;
		motionY *= 0.9800000190734863D;
		motionZ *= (double) f;

		if (onGround) {
			motionY *= -0.5D;
		}

		Material material = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)).getMaterial();
		// func_147470_e is isBoundingBoxBurning
		boolean inFire = (material == Material.lava || material == Material.fire) || worldObj.func_147470_e(boundingBox);
		if (isDud(inFire)) {
			fuseTime += 10;
			return;
		}

		if (ticksExisted % 20 == 0) {
			playSound("game.tnt.primed", 1.0F, 2.0F + rand.nextFloat() * 0.4F);
		}

		if (!worldObj.isRemote && shouldExplode(inFire)) {
			NCExplosion.createExplosion(this, worldObj, posX, posY, posZ, radius, getDamage(), canGrief);
			setDead();
		}
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		motionX *= 0.5F;
		motionY *= -0.5F;
		motionZ *= 0.5F;
	}

	/**
	 * Returns true if the bomb is a dud: in the water or a water bomb in the nether
	 * @param inFire whether this bomb is in fire, lava, or currently burning
	 */
	private boolean isDud(boolean inFire) {
		switch(getType()) {
		case BOMB_WATER: return inFire || (ticksExisted > 8 && worldObj.provider.dimensionId == -1);
		default: return (worldObj.getBlock((int) posX, (int) posY, (int) posZ).getMaterial() == Material.water);
		}
	}

	/**
	 * Returns whether this bomb should explode
	 * @param inFire whether this bomb is in fire, lava, or currently burning
	 */
	private boolean shouldExplode(boolean inFire) {
		boolean inNether = (worldObj.provider.dimensionId == -1 && getType() == BombType.BOMB_STANDARD);
		return (ticksExisted >= fuseTime || inNether || (inFire && getType() == BombType.BOMB_STANDARD));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setByte("bombType", (byte) getType().ordinal());
		compound.setInteger("fuseTime", fuseTime);
		compound.setFloat("bombRadius", radius);
		compound.setFloat("motionFactor", motionFactor);
		compound.setFloat("destructionFactor", destructionFactor);
		compound.setBoolean("canGrief", canGrief);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setType(BombType.values()[compound.getByte("bombType") % BombType.values().length]);
		fuseTime = compound.getInteger("fuseTime");
		radius = compound.getFloat("bombRadius");
		motionFactor = compound.getFloat("motionFactor");
		destructionFactor = compound.getFloat("destructionFactor");
		canGrief = compound.getBoolean("canGrief");
	}
}