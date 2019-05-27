package nc.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCuboid extends ModelRenderer {
	
	public ModelCuboid(ModelBase model, int textureWidth, int textureHeight, int textureX, int textureY, int width, int height, int depth, float posX, float posY, float posZ, double rotationX, double rotationY, double rotationZ, boolean mirror) {
		super(model, textureX, textureY);
		addBox(0F, 0F, 0F, depth, height, width);
		setRotationPoint(posX, -posY - height, -posZ);
		setTextureSize(textureWidth, textureHeight);
		this.mirror = mirror;
		rotateAngleY = (float)(2D*Math.PI*(0.25D - rotationY));
		rotateAngleX = (float)(2D*Math.PI*rotationX);
		rotateAngleZ = (float)(2D*Math.PI*rotationZ);
	}
	
	public ModelCuboid(ModelBase model, int textureWidth, int textureHeight, int textureX, int textureY, int width, int height, int depth, float posX, float posY, float posZ, boolean mirror) {
		this(model, textureWidth, textureHeight, textureX, textureY, width, height, depth, posX, posY, posZ, 0D, 0D, 0D, mirror);
	}
}
