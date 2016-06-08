package nc.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTubing2 extends ModelBase
{
  //fields
    ModelRenderer Shape9;
    ModelRenderer Shape8;
    ModelRenderer Shape7;
    ModelRenderer Shape6;
    ModelRenderer Shape4;
    ModelRenderer Shape3;
    ModelRenderer Shape2;
    ModelRenderer Shape1;
    ModelRenderer Shape5;
  
  public ModelTubing2()
  {
    textureWidth = 128;
    textureHeight = 128;
    
      Shape9 = new ModelRenderer(this, 0, 0);
      Shape9.addBox(0F, 0F, 0F, 6, 1, 16);
      Shape9.setRotationPoint(-8F, 23F, 3F);
      Shape9.setTextureSize(64, 32);
      Shape9.mirror = true;
      setRotation(Shape9, 0F, (float) (Math.PI/2), 0F);
      Shape8 = new ModelRenderer(this, 44, 0);
      Shape8.addBox(0F, 0F, 0F, 10, 1, 16);
      Shape8.setRotationPoint(-8F, 22F, 5F);
      Shape8.setTextureSize(64, 32);
      Shape8.mirror = true;
      setRotation(Shape8, 0F, (float) (Math.PI/2), 0F);
      Shape7 = new ModelRenderer(this, 0, 17);
      Shape7.addBox(0F, 0F, 0F, 12, 1, 16);
      Shape7.setRotationPoint(-8F, 21F, 6F);
      Shape7.setTextureSize(64, 32);
      Shape7.mirror = true;
      setRotation(Shape7, 0F, (float) (Math.PI/2), 0F);
      Shape6 = new ModelRenderer(this, 56, 17);
      Shape6.addBox(0F, 0F, 0F, 14, 2, 16);
      Shape6.setRotationPoint(-8F, 19F, 7F);
      Shape6.setTextureSize(64, 32);
      Shape6.mirror = true;
      setRotation(Shape6, 0F, (float) (Math.PI/2), 0F);
      Shape4 = new ModelRenderer(this, 56, 17);
      Shape4.addBox(0F, 0F, 0F, 14, 2, 16);
      Shape4.setRotationPoint(-8F, 11F, 7F);
      Shape4.setTextureSize(64, 32);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, (float) (Math.PI/2), 0F);
      Shape3 = new ModelRenderer(this, 0, 17);
      Shape3.addBox(0F, 0F, 0F, 12, 1, 16);
      Shape3.setRotationPoint(-8F, 10F, 6F);
      Shape3.setTextureSize(64, 32);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, (float) (Math.PI/2), 0F);
      Shape2 = new ModelRenderer(this, 44, 0);
      Shape2.addBox(0F, 0F, 0F, 10, 1, 16);
      Shape2.setRotationPoint(-8F, 9F, 5F);
      Shape2.setTextureSize(64, 32);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, (float) (Math.PI/2), 0F);
      Shape1 = new ModelRenderer(this, 0, 0);
      Shape1.addBox(0F, 0F, 0F, 6, 1, 16);
      Shape1.setRotationPoint(-8F, 8F, 3F);
      Shape1.setTextureSize(64, 32);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, (float) (Math.PI/2), 0F);
      Shape5 = new ModelRenderer(this, 0, 35);
      Shape5.addBox(0F, 0F, 0F, 16, 6, 16);
      Shape5.setRotationPoint(-8F, 13F, 8F);
      Shape5.setTextureSize(64, 32);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, (float) (Math.PI/2), 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Shape9.render(f5);
    Shape8.render(f5);
    Shape7.render(f5);
    Shape6.render(f5);
    Shape4.render(f5);
    Shape3.render(f5);
    Shape2.render(f5);
    Shape1.render(f5);
    Shape5.render(f5);
  }
  
  public void renderModel(float f5)
  {
    Shape9.render(f5);
    Shape8.render(f5);
    Shape7.render(f5);
    Shape6.render(f5);
    Shape5.render(f5);
    Shape4.render(f5);
    Shape3.render(f5);
    Shape2.render(f5);
    Shape1.render(f5);
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
  }

}
