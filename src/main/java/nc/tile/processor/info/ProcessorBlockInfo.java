package nc.tile.processor.info;

import java.util.List;
import java.util.function.Supplier;

import nc.tile.BlockTileInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

public class ProcessorBlockInfo<TILE extends TileEntity> extends BlockTileInfo<TILE> {
	
	public final List<String> particles;
	
	public ProcessorBlockInfo(String name, Supplier<TILE> tileSupplier, CreativeTabs creativeTab, List<String> particles) {
		super(name, tileSupplier, creativeTab);
		this.particles = particles;
	}
}
