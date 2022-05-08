package nc.block.tile;

import java.util.function.Supplier;

import nc.tile.BlockTileInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

public class BlockSimpleTileInfo<TILE extends TileEntity> extends BlockTileInfo<TILE> {
	
	public BlockSimpleTileInfo(String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		super(name, tileSupplier, creativeTab);
	}
}
