package nc.entity;

import nc.NuclearCraft;
import nc.handler.BombType;
import nc.handler.EntityBomb;
import nc.handler.NCExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityNukePrimed extends Entity {
	
	private EntityLivingBase tntPlacedBy;
	public int fuse;

	public EntityNukePrimed(World world) {
		super(world);
        this.preventEntitySpawning = true;
        this.fuse = 600;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
	}
	
	public EntityNukePrimed(World world, double x, double y, double z, EntityLivingBase elb) {
        this(world);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * Math.PI * 2.0D);
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.fuse = 600;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = elb;
    }
	
	protected void entityInit() {}

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

	public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0) {
            this.setDead();

            if (!this.worldObj.isRemote) {
                this.explode(worldObj, this.posX, this.posY, this.posZ, 0.6F*NuclearCraft.explosionRadius, BombType.BOMB_STANDARD);
            }
        }
        else {
            this.worldObj.spawnParticle("lava", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.375D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.0D, 0.0D);
        }
    }
	
	private void explode(World world, double x, double y, double z, float radius, BombType type) {
        NCExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.posX, (double)this.posY, (double)this.posZ, radius, NuclearCraft.explosionRadius, true);
    }
	
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setByte("Fuse", (byte)this.fuse);
    }

	protected void readEntityFromNBT(NBTTagCompound nbt2) {
        this.fuse = nbt2.getByte("Fuse");
    }
    
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }
	
	public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }
}
