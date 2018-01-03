package nc.item.tool;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCSpaxelhoe extends NCPickaxe {
	
	private float efficiency;
	
	public NCSpaxelhoe(String unlocalizedName, ToolMaterial material, String... tooltip) {
		super(unlocalizedName, material, tooltip);
		attackSpeed = -2.4F;
		attackDamage = 3.0F + material.getAttackDamage();
		efficiency = material.getEfficiency();
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state){
		return efficiency;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return true;
	}
}
