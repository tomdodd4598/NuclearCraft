package nc.entity;

import nc.NuclearCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityAntimatterBombPrimed extends Entity {
	
	private EntityLivingBase tntPlacedBy;
	public int fuse;

	public EntityAntimatterBombPrimed(World world) {
		super(world);
        this.preventEntitySpawning = true;
        this.fuse = 600;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
	}
	
	public EntityAntimatterBombPrimed(World world, double x, double y, double z, EntityLivingBase elb) {
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
                this.explode(worldObj);
            }
        }
        else {
        	this.worldObj.spawnParticle("lava", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        	this.worldObj.spawnParticle("depthsuspend", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.5D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.3D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX-0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("portal", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
        }
    }
	
	private void explode(World world) {
        int x = (int) Math.floor(posX); int y = (int) Math.floor(posY); int z = (int) Math.floor(posZ);
        
        for (int i = -((int)(0.8D*NuclearCraft.explosionRadius)); i <= ((int)(0.8D*NuclearCraft.explosionRadius)); i++) {
    		for (int j = -((int)(0.8D*NuclearCraft.explosionRadius)); j <= ((int)(0.8D*NuclearCraft.explosionRadius)); j++) {
    			for (int k = -((int)(0.8D*NuclearCraft.explosionRadius)); k <= ((int)(0.8D*NuclearCraft.explosionRadius)); k++) {
    				if (i*i + j*j + k*k <= ((int)(0.8D*NuclearCraft.explosionRadius))*((int)(0.8D*NuclearCraft.explosionRadius)) && world.getBlock(x + i, y + j, z + k) != Blocks.bedrock) {
    					world.setBlockToAir(x + i, y + j, z + k);
    				}
    			}
    		}
        }
        worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 16.0F, 1.0F);
        worldObj.playSoundEffect(posX, posY, posZ, "nc:shield2", 40.0F, 1.0F);
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
