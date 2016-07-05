package nc.block.basic;

import java.util.List;

import nc.NuclearCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBlock extends Block {

	public BlockBlock(String unlocalizedName, Material material) {
		super(material);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(NuclearCraft.tabNC);
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setStepSound(soundTypeMetal);
        this.setHarvestLevel("pickaxe", 0);
        this.setBlockTextureName("nc:metal/" + unlocalizedName);
	}

	public IIcon[] icons = new IIcon[6];
	public IIcon[] icons2 = new IIcon[5];
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < 6; i ++) {
			this.icons[i] = reg.registerIcon(this.textureName + i);
		}
		for (int i = 0; i < 5; i ++) {
			this.icons2[i] = reg.registerIcon(this.textureName + (i+6));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta < 6) return this.icons[meta];
		else if (meta >= 6 && meta < 11) return this.icons2[meta-6];
		else return null;
	}

	@Override
	public int damageDropped(int meta) {
		if (meta < 11) return meta;
		else return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 11; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
