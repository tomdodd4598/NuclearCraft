package nc.block.tile;

import nc.enumm.BlockEnums.SimpleTileType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSimpleTile extends BlockInventory {
	
	private final SimpleTileType type;
	
	public BlockSimpleTile(SimpleTileType type) {
		this(type, false, false);
	}
	
	public BlockSimpleTile(SimpleTileType type, boolean smartRender) {
		this(type, true, smartRender);
	}

	public BlockSimpleTile(SimpleTileType type, boolean transparent, boolean smartRender) {
		super(type.getName(), Material.IRON);
		this.type = type;
		setCreativeTab(type.getTab());
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
}
