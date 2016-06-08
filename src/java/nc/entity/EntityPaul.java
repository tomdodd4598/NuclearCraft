package nc.entity;

import nc.item.NCItems;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityPaul extends EntityMob {

	public EntityPaul(World world) {
		super(world);
		
		this.setSize(1.2F, 2.9F);
		this.stepHeight = 1.0F;
		
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntitySkeleton.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityZombie.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityBlaze.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityEnderman.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityMagmaCube.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPigZombie.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntitySilverfish.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntitySlime.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityCreeper.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntitySpider.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityWitch.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityNuclearMonster.class, 1.0D, true));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySkeleton.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityBlaze.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityEnderman.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMagmaCube.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPigZombie.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySilverfish.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySlime.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityCreeper.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySpider.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWitch.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityNuclearMonster.class, 0, true));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, NCItems.dominoes, false));
    }
	
	protected String getLivingSound() {
        return "nc:paulTooFar";
    }

	protected String getHurtSound() {
        return "nc:paulTooFar";
    }

    protected String getDeathSound() {
        return "nc:paulTooFar";
    }
    
    protected Item getDropItem() {
        return NCItems.fishAndRicecake;
    }
    
    protected boolean canDespawn() {
		return false;
    	
    }
    
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int j;
        int k;
        
            j = this.rand.nextInt(3 + p_70628_2_);

            for (k = 0; k < j; ++k) {
                this.dropItem(NCItems.ricecake, 1);
            }

        j = this.rand.nextInt(2 + p_70628_2_);

        for (k = 0; k < j; ++k) {
            this.dropItem(NCItems.dominoes, 1);
        }
    }

    protected void dropRareDrop(int p_70600_1_) {
            this.entityDropItem(new ItemStack(NCItems.fishAndRicecake	, 1, 1), 0.0F);
    }
    
    protected int decreaseAirSupply(int p_70682_1_) {
        return p_70682_1_;
    }

	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.6D);
    }
	
	protected boolean isAIEnabled() {
        return true;
    }
}
