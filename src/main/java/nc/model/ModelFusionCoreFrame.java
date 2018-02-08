package nc.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFusionCoreFrame extends ModelBase {
	
	ModelRenderer foot_1_1, foot_2_1, foot_3_1, foot_4_1;
	ModelRenderer foot_1_2, foot_2_2, foot_3_2, foot_4_2;
	ModelRenderer foot_1_3, foot_2_3, foot_3_3, foot_4_3;
	ModelRenderer foot_1_4, foot_2_4, foot_3_4, foot_4_4;
	ModelRenderer foot_1_5, foot_2_5, foot_3_5, foot_4_5;
	
	ModelRenderer base, top;
	
	ModelRenderer plate_1, plate_2, plate_3, plate_4;
	
	ModelRenderer axle_x, axle_z;
	
	public ModelFusionCoreFrame() {
		textureWidth = 256;
		textureHeight = 128;
		
		foot_1_1 = modelCuboid(192, 0, 14, 4, 14, -14F, 2F, -14F, true);
		foot_2_1 = modelCuboid(192, 0, 14, 4, 14, 16F, 2F, -14F, true);
		foot_3_1 = modelCuboid(192, 0, 14, 4, 14, 16F, 2F, 16F, true);
		foot_4_1 = modelCuboid(192, 0, 14, 4, 14, -14F, 2F, 16F, true);
		
		foot_1_2 = modelCuboid(192, 18, 12, 4, 12, -12F, 6F, -12F, true);
		foot_2_2 = modelCuboid(192, 18, 12, 4, 12, 16F, 6F, -12F, true);
		foot_3_2 = modelCuboid(192, 18, 12, 4, 12, 16F, 6F, 16F, true);
		foot_4_2 = modelCuboid(192, 18, 12, 4, 12, -12F, 6F, 16F, true);
		
		foot_1_3 = modelCuboid(192, 34, 10, 28, 10, -10F, 10F, -10F, true);
		foot_2_3 = modelCuboid(192, 34, 10, 28, 10, 16F, 10F, -10F, true);
		foot_3_3 = modelCuboid(192, 34, 10, 28, 10, 16F, 10F, 16F, true);
		foot_4_3 = modelCuboid(192, 34, 10, 28, 10, -10F, 10F, 16F, true);
		
		foot_1_4 = modelCuboid(192, 18, 12, 4, 12, -12F, 38F, -12F, true);
		foot_2_4 = modelCuboid(192, 18, 12, 4, 12, 16F, 38F, -12F, true);
		foot_3_4 = modelCuboid(192, 18, 12, 4, 12, 16F, 38F, 16F, true);
		foot_4_4 = modelCuboid(192, 18, 12, 4, 12, -12F, 38F, 16F, true);
		
		foot_1_5 = modelCuboid(192, 0, 14, 4, 14, -14F, 42F, -14F, true);
		foot_2_5 = modelCuboid(192, 0, 14, 4, 14, 16F, 42F, -14F, true);
		foot_3_5 = modelCuboid(192, 0, 14, 4, 14, 16F, 42F, 16F, true);
		foot_4_5 = modelCuboid(192, 0, 14, 4, 14, -14F, 42F, 16F, true);
		
		base = modelCuboid(0, 0, 48, 2, 48, -16F, 0F, -16F, true);
		top = modelCuboid(0, 0, 48, 2, 48, -16F, 46F, -16F, true);
		
		plate_1 = modelCuboid(96, 50, 2, 16, 16, 0F, 16F, -14F, 0F, 0.25F, 0F, true);
		plate_2 = modelCuboid(96, 50, 2, 16, 16, -16F, 16F, 0F, true);
		plate_3 = modelCuboid(96, 50, 2, 16, 16, 0F, 16F, 32F, 0F, 0.25F, 0F, true);
		plate_4 = modelCuboid(96, 50, 2, 16, 16, 30F, 16F, 0F, true);
		
		axle_x = modelCuboid(0, 50, 4, 4, 44, -14F, 22F, 10F, 0F, 0.25F, 0F, true);
		axle_z = modelCuboid(0, 50, 4, 4, 44, 6F, 22F, -14F, true);
	}
	
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		foot_1_1.render(scale);
		foot_2_1.render(scale);
		foot_3_1.render(scale);
		foot_4_1.render(scale);
		
		foot_1_2.render(scale);
		foot_2_2.render(scale);
		foot_3_2.render(scale);
		foot_4_2.render(scale);
		
		foot_1_3.render(scale);
		foot_2_3.render(scale);
		foot_3_3.render(scale);
		foot_4_3.render(scale);
		
		foot_1_4.render(scale);
		foot_2_4.render(scale);
		foot_3_4.render(scale);
		foot_4_4.render(scale);
		
		foot_1_5.render(scale);
		foot_2_5.render(scale);
		foot_3_5.render(scale);
		foot_4_5.render(scale);
		
		base.render(scale);
		top.render(scale);
		
		plate_1.render(scale);
		plate_2.render(scale);
		plate_3.render(scale);
		plate_4.render(scale);
		
		axle_x.render(scale);
		axle_z.render(scale);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
	}
	
	private ModelCuboid modelCuboid(int textureX, int textureY, int width, int height, int depth, float posX, float posY, float posZ, double rotationX, double rotationY, double rotationZ, boolean mirror) {
		return new ModelCuboid(this, 256, 128, textureX, textureY, width, height, depth, posX, posY, posZ, rotationX, rotationY, rotationZ, mirror);
	}
	
	private ModelCuboid modelCuboid(int textureX, int textureY, int width, int height, int depth, float posX, float posY, float posZ, boolean mirror) {
		return new ModelCuboid(this, 256, 128, textureX, textureY, width, height, depth, posX, posY, posZ, mirror);
	}
}
