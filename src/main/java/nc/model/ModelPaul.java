package nc.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelPaul extends ModelBase
{
  //fields
    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer rightarm;
    ModelRenderer leftarm;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
  
  public ModelPaul()
  {
    textureWidth = 128;
    textureHeight = 128;
    
      head = new ModelRenderer(this, 0, 0);
      head.addBox(-5F, -10F, -5F, 10, 10, 10);
      head.setRotationPoint(0F, -14F, 0F);
      head.setTextureSize(64, 32);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
      body = new ModelRenderer(this, 40, 0);
      body.addBox(-7F, 0F, -4F, 14, 20, 8);
      body.setRotationPoint(0F, -14F, 0F);
      body.setTextureSize(64, 32);
      body.mirror = true;
      setRotation(body, 0F, 0F, 0F);
      rightarm = new ModelRenderer(this, 0, 20);
      rightarm.addBox(-5F, -2F, -3F, 6, 21, 6);
      rightarm.setRotationPoint(-8F, -12F, 0F);
      rightarm.setTextureSize(64, 32);
      rightarm.mirror = true;
      setRotation(rightarm, 0F, 0F, 0F);
      leftarm = new ModelRenderer(this, 0, 20);
      leftarm.addBox(-1F, -2F, -2F, 6, 21, 6);
      leftarm.setRotationPoint(8F, -12F, -1F);
      leftarm.setTextureSize(64, 32);
      leftarm.mirror = true;
      setRotation(leftarm, 0F, 0F, 0F);
      rightleg = new ModelRenderer(this, 0, 47);
      rightleg.addBox(-3.5F, 0F, -3F, 6, 18, 6);
      rightleg.setRotationPoint(-3F, 6F, 0F);
      rightleg.setTextureSize(64, 32);
      rightleg.mirror = true;
      setRotation(rightleg, 0F, 0F, 0F);
      leftleg = new ModelRenderer(this, 0, 47);
      leftleg.addBox(-2.5F, 0F, -3F, 6, 18, 6);
      leftleg.setRotationPoint(3F, 6F, 0F);
      leftleg.setTextureSize(64, 32);
      leftleg.mirror = true;
      setRotation(leftleg, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    head.render(f5);
    body.render(f5);
    rightarm.render(f5);
    leftarm.render(f5);
    rightleg.render(f5);
    leftleg.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    
    this.head.rotateAngleY = f3 / (180F / (float)Math.PI);
    this.head.rotateAngleX = f4 / (180F / (float)Math.PI);
    this.rightarm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2.0F * f1 * 0.5F * 0.5F;
    this.leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F * 0.5F;
    this.rightarm.rotateAngleZ = 0.0F;
    this.leftarm.rotateAngleZ = 0.0F;
    this.rightleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1 * 0.5F;
    this.leftleg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1 * 0.5F;
    this.rightleg.rotateAngleY = 0.0F;
    this.leftleg.rotateAngleY = 0.0F;
  }

}
