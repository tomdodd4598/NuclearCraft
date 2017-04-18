package nc.entity;

import nc.NuclearCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityEMPPrimed extends Entity {
	
	private EntityLivingBase tntPlacedBy;
	public int fuse;

	public EntityEMPPrimed(World world) {
		super(world);
        this.preventEntitySpawning = true;
        this.fuse = 600;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
	}
	
	public EntityEMPPrimed(World world, double x, double y, double z, EntityLivingBase elb) {
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
        
        if (this.fuse < 10 && this.fuse > 1 && this.fuse % 3 == 0) {
            if (!this.worldObj.isRemote) {
                this.explode(worldObj);
            }
        }

        if (this.fuse-- <= 0) {
            this.setDead();

            if (!this.worldObj.isRemote) {
                this.explode(worldObj);
            }
        }
        else {
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ-0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.375D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX-0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.125D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
            this.worldObj.spawnParticle("reddust", this.posX+0.375D, this.posY + 0.5D, this.posZ+0.125D, 0.0D, 0.1D, 0.0D);
        }
    }
	
	private void explode(World world) {
        int x = (int) Math.floor(posX); int y = (int) Math.floor(posY); int z = (int) Math.floor(posZ);
        
        for (int i = -((int)(0.5D*NuclearCraft.explosionRadius)); i <= ((int)(0.5D*NuclearCraft.explosionRadius)); i++) {
    		for (int j = -((int)(0.5D*NuclearCraft.explosionRadius)); j <= ((int)(0.5D*NuclearCraft.explosionRadius)); j++) {
    			for (int k = -((int)(0.5D*NuclearCraft.explosionRadius)); k <= ((int)(0.5D*NuclearCraft.explosionRadius)); k++) {
    				if (i*i + j*j + k*k <= ((int)(0.5D*NuclearCraft.explosionRadius))*((int)(0.5D*NuclearCraft.explosionRadius)) && world.getTileEntity(x + i, y + j, z + k) != null) {
    					if (world.getTileEntity(x + i, y + j, z + k) instanceof IEnergyHandler) {
    						IEnergyHandler t = (IEnergyHandler) world.getTileEntity(x + i, y + j, z + k);
    						for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
    							if (t.canConnectEnergy(side)) {
    								t.receiveEnergy(side, -t.getEnergyStored(side), false);
    								break;
    							}
    						}
    					}
    					else if (world.getTileEntity(x + i, y + j, z + k) instanceof IEnergyReceiver) {
    						IEnergyReceiver t = (IEnergyReceiver) world.getTileEntity(x + i, y + j, z + k);
    						for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
    							if (t.canConnectEnergy(side)) {
    								t.receiveEnergy(side, -t.getEnergyStored(side), false);
    								break;
    							}
    						}
    					}
    					else if (world.getTileEntity(x + i, y + j, z + k) instanceof IEnergyProvider) {
    						IEnergyProvider t = (IEnergyProvider) world.getTileEntity(x + i, y + j, z + k);
    						for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
    							if (t.canConnectEnergy(side)) {
    								t.extractEnergy(side, t.getMaxEnergyStored(side), false);
    								break;
    							}
    						}
    					}
    				}
    			}
    		}
        }
        worldObj.playSoundEffect(posX, posY, posZ, "nc:shield3", 60.0F, 1.0F);
        worldObj.playSoundEffect(posX, posY, posZ, "nc:shield4", 60.0F, 1.0F);
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
