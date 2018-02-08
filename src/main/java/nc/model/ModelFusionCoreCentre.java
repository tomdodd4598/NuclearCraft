package nc.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFusionCoreCentre extends ModelBase {
	
	ModelRenderer core_centre;
	
	public ModelFusionCoreCentre() {
		textureWidth = 256;
		textureHeight = 128;
		
		core_centre = modelCuboid(0, 58, 16, 44, 16, (float) (8D - 16D/Math.sqrt(2)), 2F, 8F, 0D, 0.125D, 0D, true);
	}
	
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		core_centre.render(scale);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
	}
	
	private ModelCuboid modelCuboid(int textureX, int textureY, int width, int height, int depth, float posX, float posY, float posZ, double rotationX, double rotationY, double rotationZ, boolean mirror) {
		return new ModelCuboid(this, 256, 128, textureX, textureY, width, height, depth, posX, posY, posZ, rotationX, rotationY, rotationZ, mirror);
	}
}
