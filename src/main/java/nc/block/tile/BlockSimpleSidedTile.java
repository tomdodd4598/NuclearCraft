package nc.block.tile;

import nc.handler.TileInfoHandler;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSimpleSidedTile<TILE extends TileEntity> extends BlockSidedTile implements ITileType {
	
	protected final BlockSimpleTileInfo<TILE> tileInfo;
	
	public BlockSimpleSidedTile(String name) {
		super(Material.IRON);
		tileInfo = TileInfoHandler.getBlockSimpleTileInfo(name);
		setCreativeTab(tileInfo.creativeTab);
	}
	
	@Override
	public String getTileName() {
		return tileInfo.name;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return tileInfo.getNewTile();
	}
}
