package nc.item.tool;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import nc.Global;
import nc.util.NCInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

public class NCAxe extends ItemTool {
	
	String[] info;
	
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE});
	private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
	private static final float[] ATTACK_SPEEDS = new float[] {-3.2F, -3.2F, -3.1F, -3.0F, -3.0F};
	
	public NCAxe(String unlocalizedName, ToolMaterial material, String... tooltip) {
		super(material, EFFECTIVE_ON);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, unlocalizedName));
		
		String[] strings = new String[tooltip.length];
		for (int i = 0; i < tooltip.length; i++) {
			strings[i] = tooltip[i];
		}
		info = strings;
	}
	
	public float getStrVsBlock(ItemStack stack, IBlockState state){
		Material material = state.getMaterial();
		return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
    }
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(itemStack, player, tooltip, advanced);
        if (info.length > 0) NCInfo.infoFull(tooltip, info);
    }
}
