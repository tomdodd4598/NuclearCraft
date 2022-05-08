package nc.tile.processor;

import java.util.List;
import java.util.function.Supplier;

import nc.tab.NCTabs;
import nc.tile.BlockTileInfo;
import net.minecraft.tileentity.TileEntity;

public class ProcessorBlockInfo<TILE extends TileEntity> extends BlockTileInfo<TILE> {
	
	public final List<String> particles;
	
	public ProcessorBlockInfo(String name, Supplier<TILE> tileSupplier, List<String> particles) {
		super(name, tileSupplier, NCTabs.machine());
		this.particles = particles;
	}
}
