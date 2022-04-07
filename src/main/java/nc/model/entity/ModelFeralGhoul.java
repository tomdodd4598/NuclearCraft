package nc.model.entity;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ModelFeralGhoul extends ModelBiped {
	
	public ModelFeralGhoul() {
		textureWidth = 64;
		textureHeight = 64;
		
		leftArmPose = rightArmPose = ArmPose.EMPTY;
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.setRotationPoint(0F, 3.5F, -2.2F);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, -0.1F);
		setRotateAngle(bipedHead, 0.11344640137963141F, 0F, 0F);
		
		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.setRotationPoint(0F, 3.5F, -2.2F);
		bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, 0.4F);
		setRotateAngle(bipedHeadwear, 0.11344640137963141F, 0F, 0F);
		
		bipedBody = new ModelRenderer(this, 16, 16);
		bipedBody.setRotationPoint(0F, 2.4F, -2.7F);
		bipedBody.addBox(-4F, 0F, -2F, 8, 12, 4, -0.2F);
		setRotateAngle(bipedBody, 0.4363323129985824F, 0F, 0F);
		
		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.setRotationPoint(-1.9F, 12F, 2.1F);
		bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, -0.2F);
		setRotateAngle(bipedRightLeg, -0.017453292519943295F, 0F, 0.02617993877991494F);
		
		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.setRotationPoint(1.9F, 12F, 2.1F);
		bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, -0.2F);
		setRotateAngle(bipedLeftLeg, -0.017453292519943295F, 0F, -0.02617993877991494F);
		
		bipedRightArm = new ModelRenderer(this, 40, 16);
		bipedRightArm.setRotationPoint(-4.2F, 4.4F, -1.7F);
		bipedRightArm.addBox(-3F, -2F, -2F, 4, 16, 4, -0.5F);
		setRotateAngle(bipedRightArm, -0.3490658503988659F, -0.10000736613927509F, 0.10000736613927509F);
		
		bipedLeftArm = new ModelRenderer(this, 40, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.setRotationPoint(4.2F, 4.4F, -1.7F);
		bipedLeftArm.addBox(-1F, -2F, -2F, 4, 16, 4, -0.5F);
		setRotateAngle(bipedLeftArm, -0.3490658503988659F, 0.10000736613927509F, -0.10000736613927509F);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
		
		bipedHead.setRotationPoint(0F, 3.5F, -2.2F);
		
		bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
		
		if (flag) {
			bipedHead.rotateAngleX = 0.11344640137963141F - (float) (Math.PI / 4D);
		}
		else {
			bipedHead.rotateAngleX = 0.11344640137963141F + headPitch * 0.017453292F;
		}
		
		bipedBody.rotateAngleY = 0F;
		bipedRightArm.setRotationPoint(-4.2F, 4.4F, -1.7F);
		bipedLeftArm.setRotationPoint(4.2F, 4.4F, -1.7F);
		
		float f = 1F;
		
		if (flag) {
			f = (float) (entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
			f = f / 0.2F;
			f = f * f * f;
		}
		
		if (f < 1F) {
			f = 1F;
		}
		
		bipedRightArm.rotateAngleX = -0.3490658503988659F + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount * 0.5F / f;
		bipedLeftArm.rotateAngleX = -0.3490658503988659F + MathHelper.cos(limbSwing * 0.6662F) * 2F * limbSwingAmount * 0.5F / f;
		bipedRightArm.rotateAngleZ = 0.10000736613927509F;
		bipedLeftArm.rotateAngleZ = -0.10000736613927509F;
		bipedRightLeg.rotateAngleX = -0.017453292519943295F + MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		bipedLeftLeg.rotateAngleX = -0.017453292519943295F + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
		bipedRightLeg.rotateAngleY = 0F;
		bipedLeftLeg.rotateAngleY = 0F;
		bipedRightLeg.rotateAngleZ = 0.02617993877991494F;
		bipedLeftLeg.rotateAngleZ = -0.02617993877991494F;
		
		if (isRiding) {
			bipedRightArm.rotateAngleX += -0.3490658503988659F - (float) Math.PI / 5F;
			bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
			bipedRightLeg.rotateAngleX = -1.4137167F;
			bipedRightLeg.rotateAngleY = (float) Math.PI / 10F;
			bipedRightLeg.rotateAngleZ = 0.07853982F;
			bipedLeftLeg.rotateAngleX = -1.4137167F;
			bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
			bipedLeftLeg.rotateAngleZ = -0.07853982F;
		}
		
		bipedRightArm.rotateAngleY = -0.10000736613927509F;
		bipedRightArm.rotateAngleZ = 0.10000736613927509F;
		
		switch (leftArmPose) {
			case EMPTY:
				bipedLeftArm.rotateAngleY = 0.10000736613927509F;
				break;
			case BLOCK:
				bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
				bipedLeftArm.rotateAngleY = 0.5235988F;
				break;
			case ITEM:
				bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - (float) Math.PI / 10F;
				bipedLeftArm.rotateAngleY = 0F;
				break;
			default:
				break;
		}
		
		switch (rightArmPose) {
			case EMPTY:
				bipedRightArm.rotateAngleY = -0.10000736613927509F;
				break;
			case BLOCK:
				bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
				bipedRightArm.rotateAngleY = -0.5235988F;
				break;
			case ITEM:
				bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - (float) Math.PI / 10F;
				bipedRightArm.rotateAngleY = 0F;
				break;
			default:
				break;
		}
		
		if (swingProgress > 0F) {
			EnumHandSide enumhandside = getMainHand(entityIn);
			ModelRenderer modelrenderer = getArmForSide(enumhandside);
			float f1 = swingProgress;
			bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;
			
			if (enumhandside == EnumHandSide.LEFT) {
				bipedBody.rotateAngleY *= -1F;
			}
			
			bipedRightArm.rotationPointZ = MathHelper.sin(bipedBody.rotateAngleY) * 5F;
			bipedRightArm.rotationPointX = -MathHelper.cos(bipedBody.rotateAngleY) * 5F;
			bipedLeftArm.rotationPointZ = -MathHelper.sin(bipedBody.rotateAngleY) * 5F;
			bipedLeftArm.rotationPointX = MathHelper.cos(bipedBody.rotateAngleY) * 5F;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
			f1 = 1F - swingProgress;
			f1 = f1 * f1;
			f1 = f1 * f1;
			f1 = 1F - f1;
			float f2 = MathHelper.sin(f1 * (float) Math.PI);
			float f3 = MathHelper.sin(swingProgress * (float) Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
			modelrenderer.rotateAngleX = (float) (modelrenderer.rotateAngleX - (f2 * 1.2D + f3));
			modelrenderer.rotateAngleY += bipedBody.rotateAngleY * 2F;
			modelrenderer.rotateAngleZ += MathHelper.sin(swingProgress * (float) Math.PI) * -0.4F;
		}
		
		bipedBody.rotateAngleX = 0.4363323129985824F;
		bipedRightLeg.setRotationPoint(-1.9F, 12F, 2.1F);
		bipedLeftLeg.setRotationPoint(1.9F, 12F, 2.1F);
		bipedHead.setRotationPoint(0F, 3.5F, -2.2F);
		
		bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		
		copyModelAngles(bipedHead, bipedHeadwear);
		
		bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}
	
	/** This is a helper function from Tabula to set the rotation of model parts */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
