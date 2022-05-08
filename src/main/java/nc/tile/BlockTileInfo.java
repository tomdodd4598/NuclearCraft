package nc.tile;

import java.util.function.Supplier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

public abstract class BlockTileInfo<TILE extends TileEntity> {
	
	public final String name;
	
	protected final Supplier<TILE> tileSupplier;
	public final CreativeTabs creativeTab;
	
	public BlockTileInfo(String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		this.name = name;
		this.tileSupplier = tileSupplier;
		this.creativeTab = creativeTab;
	}
	
	public TileEntity getNewTile() {
		return tileSupplier.get();
	}
}
