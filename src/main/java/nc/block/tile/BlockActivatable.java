package nc.block.tile;

import java.util.Random;

import nc.enumm.BlockEnums.ActivatableTileType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockActivatable extends BlockInventory implements IActivatable {
	
	protected final boolean isActive;
	protected final ActivatableTileType type;
	
	public BlockActivatable(ActivatableTileType type, boolean isActive) {
		super(type.getName() + (isActive ? "_active" : "_idle"), Material.IRON);
		this.isActive = isActive;
		if (!isActive) setCreativeTab(type.getTab());
		this.type = type;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(type.getIdleBlock());
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(type.getIdleBlock());
	}
	
	@Override
	public void setState(boolean active, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		keepInventory = true;
		
		if (active) {
			world.setBlockState(pos, type.getActiveBlock().getDefaultState(), 3);
		} else {
			world.setBlockState(pos, type.getIdleBlock().getDefaultState(), 3);
		}
		keepInventory = false;
		
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
}
