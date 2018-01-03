package nc.block.tile;

import nc.enumm.BlockEnums.SimpleTileType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSimpleSidedTile extends BlockSidedInventory {
	
	private final SimpleTileType type;

	public BlockSimpleSidedTile(SimpleTileType type) {
		super(type.getName(), Material.IRON);
		this.type = type;
		setCreativeTab(type.getTab());
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
}
