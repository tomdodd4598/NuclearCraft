package nc.block.storage;

import java.util.Random;

import nc.block.NCBlocks;
import nc.tile.storage.TileVoltaicPile;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockVoltaicPile extends BlockEnergyStorage {
	
	public BlockVoltaicPile() {
		super("voltaicPile");
	}

	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(NCBlocks.voltaicPile);
	}
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileVoltaicPile();
	}
}