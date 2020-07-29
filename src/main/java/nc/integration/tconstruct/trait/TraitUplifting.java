package nc.integration.tconstruct.trait;

import nc.util.PotionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitUplifting extends AbstractTrait implements ITraitNC {
	
	public TraitUplifting() {
		super("uplifting", TextFormatting.LIGHT_PURPLE);
	}
	
	@Override
	public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
		uplift(player);
	}
	
	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		uplift(player);
	}
	
	private static void uplift(EntityLivingBase player) {
		if (!player.getEntityWorld().isRemote && random.nextInt(2) == 0) {
			player.addPotionEffect(new PotionEffect(PotionHelper.newEffect(10, 3, 81)));
		}
	}
}
