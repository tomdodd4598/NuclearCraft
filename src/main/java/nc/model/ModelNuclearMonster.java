package nc.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelNuclearMonster extends ModelBase
{
  //fields
    ModelRenderer Head;
    ModelRenderer Headwear;
    ModelRenderer Body;
    ModelRenderer RightArm;
    ModelRenderer LeftArm;
    ModelRenderer RightLeg;
    ModelRenderer LeftLeg;
  
  public ModelNuclearMonster()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      Head = new ModelRenderer(this, 0, 0);
      Head.addBox(-4F, -8F, -4F, 8, 8, 8);
      Head.setRotationPoint(0F, -30F, 0F);
      Head.setTextureSize(64, 64);
      Head.mirror = true;
      setRotation(Head, 0F, 0F, 0F);
      Headwear = new ModelRenderer(this, 0, 16);
      Headwear.addBox(-4F, -8F, -4F, 8, 8, 8);
      Headwear.setRotationPoint(0F, -30F, 0F);
      Headwear.setTextureSize(64, 64);
      Headwear.mirror = true;
      setRotation(Headwear, 0F, 0F, 0F);
      Body = new ModelRenderer(this, 32, 16);
      Body.addBox(-4F, 0F, -2F, 8, 4, 4);
      Body.setRotationPoint(0F, -30F, 0F);
      Body.setTextureSize(64, 64);
      Body.mirror = true;
      setRotation(Body, 0F, 0F, 0F);
      RightArm = new ModelRenderer(this, 56, 0);
      RightArm.addBox(-1F, -2F, -1F, 2, 52, 2);
      RightArm.setRotationPoint(-5F, -28F, 0F);
      RightArm.setTextureSize(64, 64);
      RightArm.mirror = true;
      setRotation(RightArm, 0F, 0F, 0F);
      LeftArm = new ModelRenderer(this, 56, 0);
      LeftArm.addBox(-1F, -2F, -1F, 2, 52, 2);
      LeftArm.setRotationPoint(5F, -28F, 0F);
      LeftArm.setTextureSize(64, 64);
      LeftArm.mirror = true;
      setRotation(LeftArm, 0F, 0F, 0F);
      RightLeg = new ModelRenderer(this, 56, 0);
      RightLeg.addBox(-1F, 0F, -1F, 2, 50, 2);
      RightLeg.setRotationPoint(-2F, -26F, 0F);
      RightLeg.setTextureSize(64, 64);
      RightLeg.mirror = true;
      setRotation(RightLeg, 0F, 0F, 0F);
      LeftLeg = new ModelRenderer(this, 56, 0);
      LeftLeg.addBox(-1F, 0F, -1F, 2, 50, 2);
      LeftLeg.setRotationPoint(2F, -26F, 0F);
      LeftLeg.setTextureSize(64, 64);
      LeftLeg.mirror = true;
      setRotation(LeftLeg, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Head.render(f5);
    Headwear.render(f5);
    Body.render(f5);
    RightArm.render(f5);
    LeftArm.render(f5);
    RightLeg.render(f5);
    LeftLeg.render(f5);
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
    
    this.Head.rotateAngleY = f3 / (180F / (float)Math.PI);
    this.Head.rotateAngleX = f4 / (180F / (float)Math.PI);
    this.Headwear.rotateAngleY = this.Head.rotateAngleY;
    this.Headwear.rotateAngleX = this.Head.rotateAngleX;
    this.RightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2.0F * f1 * 0.5F * 0.1F;
    this.LeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F * 0.1F;
    this.RightArm.rotateAngleZ = 0.0F;
    this.LeftArm.rotateAngleZ = 0.0F;
    this.RightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1 * 0.1F;
    this.LeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1 * 0.1F;
    this.RightLeg.rotateAngleY = 0.0F;
    this.LeftLeg.rotateAngleY = 0.0F;
  }

}
