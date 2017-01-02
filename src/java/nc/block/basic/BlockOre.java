package nc.block.basic;

import java.util.List;
import java.util.Random;

import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOre extends Block {

	public BlockOre(String unlocalizedName, Material material) {
		super(material);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(NuclearCraft.tabNC);
        this.setHardness(3.0F);
        this.setResistance(8.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 1);
        this.setBlockTextureName("nc:ore/" + unlocalizedName);
	}
	
	public IIcon[] icons = new IIcon[6];
	public IIcon[] icons2 = new IIcon[4];
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < 6; i ++) {
			this.icons[i] = reg.registerIcon(this.textureName + i);
		}
		for (int i = 0; i < 4; i ++) {
			this.icons2[i] = reg.registerIcon(this.textureName + (i+6));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta < 6){ return this.icons[meta]; }
		else if (meta >= 6 && meta < 10) { return this.icons2[meta-6]; }
		else { return null; }
	}
	
	public Item getItemDropped(int meta, Random rand, int int2) {
		if (meta == 6) { return NCItems.material; }
		else { return Item.getItemFromBlock(this); }
    }
	
	@Override
	public int damageDropped(int meta) {
		if (meta < 6) { return meta; }
		else if (meta == 6) { return 33; }
		else if (meta > 6 && meta < 10) { return meta; }
		else return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 10; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	// For pickBlock
	public int getDamageValue(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
}
