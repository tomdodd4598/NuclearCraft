package nc.entity;

import java.util.Calendar;

import javax.annotation.Nullable;

import nc.capability.radiation.entity.IEntityRads;
import nc.config.NCConfig;
import nc.entity.ai.EntityAIFeralGhoulLeap;
import nc.init.NCSounds;
import nc.radiation.RadSources;
import nc.radiation.RadiationHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.ForgeHooks;

public class EntityFeralGhoul extends EntityZombie {
	
	private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntityFeralGhoul.class, DataSerializers.BYTE);
	
	public EntityFeralGhoul(World world) {
		super(world);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(CLIMBING, Byte.valueOf((byte)0));
	}
	
	@Override
	protected void initEntityAI() {
		tasks.addTask(2, new EntityAIZombieAttack(this, 1D, false));
		tasks.addTask(1, new EntityAIFeralGhoulLeap(this));
		
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1D));
		tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1D));
		
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 24F));
		tasks.addTask(8, new EntityAILookIdle(this));
		
		tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1D, false));
		
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
		
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
		
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.4D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		//return SoundHandler.feral_ghoul_ambient;
		return SoundEvents.ENTITY_HUSK_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		//return SoundHandler.feral_ghoul_hurt;
		return NCSounds.feral_ghoul_charge;
	}
	
	@Override
	protected void playHurtSound(DamageSource source) {
		playSound(getHurtSound(source), getSoundVolume(), getSoundPitch() - 0.3F);
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return NCSounds.feral_ghoul_death;
	}
	
	@Override
	protected SoundEvent getStepSound() {
		//return SoundHandler.feral_ghoul_step;
		return SoundEvents.ENTITY_HUSK_STEP;
	}
	
	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_HUSK;
	}
	
	@Override
	protected boolean shouldBurnInDay() {
		return false;
	}
	
	@Override
	public void onUpdate() {
		if (!NCConfig.entity_register[0]) {
			setDead();
			return;
		}
		
		super.onUpdate();

		if (!world.isRemote) {
			setBesideClimbableBlock(collidedHorizontally);
		}
	}
	
	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateClimber(this, worldIn);
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = super.attackEntityAsMob(entityIn);
		
		if (flag && entityIn instanceof EntityLivingBase && !(entityIn instanceof IMob)) {
			EntityLivingBase target = (EntityLivingBase)entityIn;
			int mult = (int) (30F*MathHelper.clamp(world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty(), 1F, 2.5F));
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, mult));
			
			IEntityRads entityRads = RadiationHelper.getEntityRadiation(target);
			if (entityRads != null) {
				entityRads.setPoisonBuffer(entityRads.getPoisonBuffer() + RadSources.CAESIUM_137*mult);
				entityRads.setRecentPoisonAddition(RadSources.CAESIUM_137*mult);
				playSound(NCSounds.rad_poisoning, 1.35F, 1F + 0.2F*(rand.nextFloat() - rand.nextFloat()));
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && world.canSeeSky(new BlockPos(posX, getEntityBoundingBox().minY, posZ)) && world.countEntities(EntityFeralGhoul.class) < 10;
	}
	
	@Override
	protected boolean isValidLightLevel() {
		return true;
	}
	
	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextGaussian() * 0.05D, 1));
		setLeftHanded(rand.nextFloat() < 0.05F);
		
		float f = difficulty.getClampedAdditionalDifficulty();
		setCanPickUpLoot(rand.nextFloat() < 0.55F * f);
		
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);
		
		if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
			Calendar calendar = world.getCurrentDate();
			
			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F) {
				setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
				inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0F;
			}
		}
		
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextDouble() * 0.05000000074505806D, 0));
		double d0 = rand.nextDouble()*1.5D*f;
		
		if (d0 > 1D) {
			getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
		}
		
		if (rand.nextFloat() < f * 0.05F) {
			getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 0.25D + 0.5D, 0));
			getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 3D + 1D, 2));
		}
		
		return livingdata;
	}
	
	@Override
	protected float getJumpUpwardsMotion() {
		return 0.5F;
	}
	
	public void leap(double speedBoost) {
		motionY = getJumpUpwardsMotion();
		motionX *= speedBoost;
		motionZ *= speedBoost;
		
		if (isPotionActive(MobEffects.JUMP_BOOST)) {
			motionY += (getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
		}
		
		isAirBorne = true;
	}
	
	@Override
	protected void jump() {
		leap(1D);
		ForgeHooks.onLivingJump(this);
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {
		float[] ret = ForgeHooks.onLivingFall(this, distance, damageMultiplier);
		if (ret == null) return;
		distance = ret[0];
		damageMultiplier = ret[1];
		
		if (isBeingRidden()) {
			for (Entity entity : getPassengers()) {
				entity.fall(distance, damageMultiplier);
			}
		}
		
		int i = MathHelper.ceil((distance - 3F) * damageMultiplier);
		if (i > 0) {
			playSound(getFallSound(i), 1F, 1F);
			//playSound(SoundHandler.feral_ghoul_fall, 1F, 1F);
			playSound(SoundEvents.ENTITY_HUSK_AMBIENT, 1F, 1F);
			int j = MathHelper.floor(posX);
			int k = MathHelper.floor(posY - 0.20000000298023224D);
			int l = MathHelper.floor(posZ);
			IBlockState iblockstate = world.getBlockState(new BlockPos(j, k, l));
			
			if (iblockstate.getMaterial() != Material.AIR) {
				SoundType soundtype = iblockstate.getBlock().getSoundType(iblockstate, world, new BlockPos(j, k, l), this);
				playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
			}
		}
	}
	
	@Override
	public int getMaxFallHeight() {
		return 255;
	}
	
	public boolean isBesideClimbableBlock() {
		return (dataManager.get(CLIMBING).byteValue() & 1) != 0;
	}
	
	@Override
	public boolean isOnLadder() {
		return isBesideClimbableBlock();
	}
	
	public void setBesideClimbableBlock(boolean climbing) {
		byte b0 = dataManager.get(CLIMBING).byteValue();

		if (climbing) {
			b0 = (byte)(b0 | 1);
		}
		else {
			b0 = (byte)(b0 & -2);
		}

		dataManager.set(CLIMBING, Byte.valueOf(b0));
	}
	
	@Override
	public void travel(float strafe, float vertical, float forward) {
		if (isServerWorld() || canPassengerSteer()) {
			if (!isInWater()) {
				if (!isInLava()) {
					if (isElytraFlying()) {
						if (motionY > -0.5D) {
							fallDistance = 1F;
						}
						
						Vec3d vec3d = getLookVec();
						float f = rotationPitch * 0.017453292F;
						double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
						double d8 = Math.sqrt(motionX * motionX + motionZ * motionZ);
						double d1 = vec3d.length();
						float f4 = MathHelper.cos(f);
						f4 = (float)((double)f4 * (double)f4 * Math.min(1D, d1 / 0.4D));
						motionY += -0.08D + f4 * 0.06D;
						
						if (motionY < 0D && d6 > 0D) {
							double d2 = motionY * -0.1D * f4;
							motionY += d2;
							motionX += vec3d.x * d2 / d6;
							motionZ += vec3d.z * d2 / d6;
						}
						
						if (f < 0F) {
							double d10 = d8 * (-MathHelper.sin(f)) * 0.04D;
							motionY += d10 * 3.2D;
							motionX -= vec3d.x * d10 / d6;
							motionZ -= vec3d.z * d10 / d6;
						}
						
						if (d6 > 0D) {
							motionX += (vec3d.x / d6 * d8 - motionX) * 0.1D;
							motionZ += (vec3d.z / d6 * d8 - motionZ) * 0.1D;
						}
						
						motionX *= 0.9900000095367432D;
						motionY *= 0.9800000190734863D;
						motionZ *= 0.9900000095367432D;
						move(MoverType.SELF, motionX, motionY, motionZ);
						
						if (collidedHorizontally && !world.isRemote) {
							double d11 = Math.sqrt(motionX * motionX + motionZ * motionZ);
							double d3 = d8 - d11;
							float f5 = (float)(d3 * 10D - 3D);
							
							if (f5 > 0F) {
								playSound(getFallSound((int)f5), 1F, 1F);
								attackEntityFrom(DamageSource.FLY_INTO_WALL, f5);
							}
						}
						
						if (onGround && !world.isRemote) {
							setFlag(7, false);
						}
					}
					else {
						float f6 = 0.91F;
						BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(posX, getEntityBoundingBox().minY - 1D, posZ);
						
						if (onGround) {
							IBlockState underState = world.getBlockState(blockpos$pooledmutableblockpos);
							f6 = underState.getBlock().getSlipperiness(underState, world, blockpos$pooledmutableblockpos, this) * 0.91F;
						}
						
						float f7 = 0.16277136F / (f6 * f6 * f6);
						float f8;
						
						if (onGround) {
							f8 = getAIMoveSpeed() * f7;
						}
						else {
							f8 = jumpMovementFactor;
						}
						
						moveRelative(strafe, vertical, forward, f8);
						f6 = 0.91F;
						
						if (onGround) {
							IBlockState underState = world.getBlockState(blockpos$pooledmutableblockpos.setPos(posX, getEntityBoundingBox().minY - 1D, posZ));
							f6 = underState.getBlock().getSlipperiness(underState, world, blockpos$pooledmutableblockpos, this) * 0.91F;
						}
						
						if (isOnLadder()) {
							motionX = MathHelper.clamp(motionX, -0.15000000596046448D, 0.15000000596046448D);
							motionZ = MathHelper.clamp(motionZ, -0.15000000596046448D, 0.15000000596046448D);
							fallDistance = 0F;

							if (motionY < -0.15D) {
								motionY = -0.15D;
							}
							
							boolean flag = isSneaking();
							
							if (flag && motionY < 0D) {
								motionY = 0D;
							}
						}
						
						move(MoverType.SELF, motionX, motionY, motionZ);
						
						if (collidedHorizontally && isOnLadder()) {
							motionY = 0.2D;
						}
						
						if (isPotionActive(MobEffects.LEVITATION)) {
							motionY += (0.05D * (getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - motionY) * 0.2D;
						}
						else {
							blockpos$pooledmutableblockpos.setPos(posX, 0D, posZ);
							
							if (!world.isRemote || world.isBlockLoaded(blockpos$pooledmutableblockpos) && world.getChunk(blockpos$pooledmutableblockpos).isLoaded()) {
								if (!hasNoGravity()) {
									motionY -= 0.08D;
								}
							}
							else if (posY > 0D) {
								motionY = -0.1D;
							}
							else {
								motionY = 0D;
							}
						}
						
						motionY *= 0.9800000190734863D;
						
						if (onGround) {
							motionX *= f6;
							motionZ *= f6;
						}
						blockpos$pooledmutableblockpos.release();
					}
				}
				else {
					double d4 = posY;
					moveRelative(strafe, vertical, forward, 0.02F);
					move(MoverType.SELF, motionX, motionY, motionZ);
					motionX *= 0.5D;
					motionY *= 0.5D;
					motionZ *= 0.5D;
					
					if (!hasNoGravity()) {
						motionY -= 0.02D;
					}
					
					if (collidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d4, motionZ)) {
						motionY = 0.30000001192092896D;
					}
				}
			}
			else {
				double d0 = posY;
				float f1 = getWaterSlowDown();
				float f2 = 0.02F;
				float f3 = EnchantmentHelper.getDepthStriderModifier(this);
				
				if (f3 > 3F) {
					f3 = 3F;
				}
				
				if (!onGround) {
					f3 *= 0.5F;
				}
				
				if (f3 > 0F) {
					f1 += (0.54600006F - f1) * f3 / 3F;
					f2 += (getAIMoveSpeed() - f2) * f3 / 3F;
				}
				
				moveRelative(strafe, vertical, forward, f2);
				move(MoverType.SELF, motionX, motionY, motionZ);
				motionX *= f1;
				motionY *= 0.800000011920929D;
				motionZ *= f1;
				
				if (!hasNoGravity()) {
					motionY -= 0.02D;
				}
				
				if (collidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d0, motionZ)) {
					motionY = 0.30000001192092896D;
				}
			}
		}
		
		prevLimbSwingAmount = limbSwingAmount;
		double d5 = posX - prevPosX;
		double d7 = posZ - prevPosZ;
		double d9 = this instanceof net.minecraft.entity.passive.EntityFlying ? posY - prevPosY : 0D;
		float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4F;
		
		if (f10 > 1F) {
			f10 = 1F;
		}
		
		limbSwingAmount += (f10 - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public void setChild(boolean childZombie) {
		
	}
	
	@Override
	public boolean isChild() {
		return false;
	}
	
	@Override
	public void setArmsRaised(boolean armsRaised) {
		
	}
	
	@Override
	protected ItemStack getSkullDrop() {
		return ItemStack.EMPTY;
	}
	
	class Data implements IEntityLivingData {
		
	}
}
