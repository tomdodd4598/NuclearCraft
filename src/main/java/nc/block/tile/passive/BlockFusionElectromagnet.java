package nc.block.tile.passive;

import java.util.Random;

import nc.init.NCBlocks;
import nc.proxy.CommonProxy;
import nc.tile.passive.Passives.TileFusionElectromagnet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFusionElectromagnet extends BlockPassive {
	
	public BlockFusionElectromagnet(String unlocalizedName, String registryName, boolean isActive) {
		super(unlocalizedName, registryName, isActive, CommonProxy.TAB_FUSION);
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFusionElectromagnet();
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(NCBlocks.fusion_electromagnet_idle);
	}
	
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(NCBlocks.fusion_electromagnet_idle);
	}
	
	public static void setState(boolean active, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		keepInventory = true;
		
		if (active) {
			world.setBlockState(pos, NCBlocks.fusion_electromagnet_active.getDefaultState(), 3);
		} else {
			world.setBlockState(pos, NCBlocks.fusion_electromagnet_idle.getDefaultState(), 3);
		}
		keepInventory = false;
		
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
}
