package nc.multiblock.rtg.block;

import nc.multiblock.block.BlockMultiblockPart;
import nc.multiblock.rtg.RTGType;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRTG extends BlockMultiblockPart {
	
	private final RTGType type;
	
	public BlockRTG(RTGType type) {
		super(Material.IRON, NCTabs.MACHINE);
		this.type = type;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND) return false;
		
		return rightClickOnPart(world, pos, player, hand, facing, false);
	}
}
