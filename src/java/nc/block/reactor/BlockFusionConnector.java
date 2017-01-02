package nc.block.reactor;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFusionConnector extends Block {

	public BlockFusionConnector() {
	super(Material.iron);
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("nc:generator/fusionEM/" + this.getUnlocalizedName().substring(5));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return blockIcon;
	}
	
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return false;
    }
}
