package nc.multiblock.fission.block.port;

import static nc.block.property.BlockProperties.AXIS_ALL;

import nc.NuclearCraft;
import nc.enumm.MetaEnums;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFissionHeaterPort extends BlockFissionFluidMetaPort<TileFissionHeaterPort, TileSaltFissionHeater, MetaEnums.CoolantHeaterType> {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.CoolantHeaterType.class);
	
	public BlockFissionHeaterPort() {
		super(TileFissionHeaterPort.class, MetaEnums.CoolantHeaterType.class, TYPE, 303);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, AXIS_ALL);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch(metadata) {
		case 0:
			return new TileFissionHeaterPort.Standard();
		case 1:
			return new TileFissionHeaterPort.Iron();
		case 2:
			return new TileFissionHeaterPort.Redstone();
		case 3:
			return new TileFissionHeaterPort.Quartz();
		case 4:
			return new TileFissionHeaterPort.Obsidian();
		case 5:
			return new TileFissionHeaterPort.NetherBrick();
		case 6:
			return new TileFissionHeaterPort.Glowstone();
		case 7:
			return new TileFissionHeaterPort.Lapis();
		case 8:
			return new TileFissionHeaterPort.Gold();
		case 9:
			return new TileFissionHeaterPort.Prismarine();
		case 10:
			return new TileFissionHeaterPort.Slime();
		case 11:
			return new TileFissionHeaterPort.EndStone();
		case 12:
			return new TileFissionHeaterPort.Purpur();
		case 13:
			return new TileFissionHeaterPort.Diamond();
		case 14:
			return new TileFissionHeaterPort.Emerald();
		case 15:
			return new TileFissionHeaterPort.Copper();
		}
		return new TileFissionHeaterPort.Standard();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		
		if (!world.isRemote) {
			player.openGui(NuclearCraft.instance, guiID, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return rightClickOnPart(world, pos, player, hand, facing, true);
	}
}
