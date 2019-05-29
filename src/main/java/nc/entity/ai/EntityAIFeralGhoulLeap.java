package nc.entity.ai;

import java.util.Random;

import nc.entity.EntityFeralGhoul;
import nc.handler.SoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFeralGhoulLeap extends EntityAIBase {
	
	EntityFeralGhoul ghoul;
	EntityLivingBase leapTarget;
	final Random rand = new Random();
	
	public EntityAIFeralGhoulLeap(EntityFeralGhoul ghoul) {
		this.ghoul = ghoul;
		setMutexBits(5);
	}
	
	@Override
	public boolean shouldExecute() {
		leapTarget = ghoul.getAttackTarget();
		
		if (leapTarget == null) {
			return false;
		}
		else {
			if (ghoul.getDistanceSq(leapTarget) > 16D && ghoul.getDistanceSq(leapTarget) < 64D) {
				if (!ghoul.onGround) {
					return false;
				}
				else {
					return ghoul.getRNG().nextInt(4) == 0;
				}
			}
			else {
				return false;
			}
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !ghoul.onGround;
	}
	
	@Override
	public void startExecuting() {
		ghoul.leap(1.25D);
		ghoul.playSound(SoundHandler.feral_ghoul_charge, 1F, 1F + 0.2F*(rand.nextFloat() - rand.nextFloat()));
	}
}
