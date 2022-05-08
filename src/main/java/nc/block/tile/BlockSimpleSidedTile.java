package nc.block.tile;

import nc.enumm.BlockEnums.SimpleTileType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSimpleSidedTile extends BlockSidedTile implements ITileType {
	
	private final SimpleTileType type;
	
	public BlockSimpleSidedTile(SimpleTileType type) {
		super(Material.IRON);
		this.type = type;
		setCreativeTab(type.getCreativeTab());
	}
	
	@Override
	public String getTileName() {
		return type.getName();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
}
