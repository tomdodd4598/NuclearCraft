package nc.entity;

import nc.item.NCItems;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityNuclearMonster extends EntityMob {

	public EntityNuclearMonster(World world) {
		super(world);
		
		this.setSize(0.6F, 3.9F);
		this.stepHeight = 1.0F;
		
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.65D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 0.65D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}
	
	protected String getLivingSound() {
        return "nc:nuclearMonsterScream";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "nc:nuclearMonsterHit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "nc:nuclearMonsterDeath";
    }

    protected void dropRareDrop(int p_70600_1_) {
            this.entityDropItem(new ItemStack(NCItems.material, 1, 37), this.rand.nextInt(2));
    }
    
    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.obsidian);
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    	int j = this.rand.nextInt(2);
    	for (int k = 0; k < j; ++k) this.entityDropItem(new ItemStack(NCItems.material, 1, 33), this.rand.nextInt(2));
    	int l = this.rand.nextInt(3);
    	for (int m = 0; m < l; ++m) this.entityDropItem(new ItemStack(NCItems.material, 1, 29), this.rand.nextInt(2));
    	int n = this.rand.nextInt(2);
    	for (int o = 0; o < n; ++o) this.entityDropItem(new ItemStack(NCItems.material, 1, 35), this.rand.nextInt(2));
    }
    
    protected int decreaseAirSupply(int p_70682_1_) {
        return p_70682_1_;
    }

	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.8D);
    }
	
	protected boolean isAIEnabled() {
        return true;
    }

}
