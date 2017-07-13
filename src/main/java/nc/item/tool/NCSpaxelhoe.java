package nc.item.tool;

import java.util.List;

import nc.util.NCInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class NCSpaxelhoe extends NCPickaxe {
	
	private float efficiency;
	
	public NCSpaxelhoe(String unlocalizedName, ToolMaterial material, String... tooltip) {
		super(unlocalizedName, material, tooltip);
		attackSpeed = -2.4F;
		damageVsEntity = 3.0F + material.getDamageVsEntity();
		efficiency = material.getEfficiencyOnProperMaterial();
	}
	
	public float getStrVsBlock(ItemStack stack, IBlockState state){
		return efficiency;
	}
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return true;
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(itemStack, player, tooltip, advanced);
        if (info.length > 0) NCInfo.infoFull(tooltip, info);
    }
}
