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

public class BlockFissionHeaterPort2 extends BlockFissionFluidMetaPort<TileFissionHeaterPort, TileSaltFissionHeater, MetaEnums.CoolantHeaterType2> {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.CoolantHeaterType2.class);
	
	public BlockFissionHeaterPort2() {
		super(TileFissionHeaterPort.class, MetaEnums.CoolantHeaterType2.class, TYPE, 303);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, AXIS_ALL);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch(metadata) {
		case 0:
			return new TileFissionHeaterPort.Tin();
		case 1:
			return new TileFissionHeaterPort.Lead();
		case 2:
			return new TileFissionHeaterPort.Boron();
		case 3:
			return new TileFissionHeaterPort.Lithium();
		case 4:
			return new TileFissionHeaterPort.Magnesium();
		case 5:
			return new TileFissionHeaterPort.Manganese();
		case 6:
			return new TileFissionHeaterPort.Aluminum();
		case 7:
			return new TileFissionHeaterPort.Silver();
		case 8:
			return new TileFissionHeaterPort.Fluorite();
		case 9:
			return new TileFissionHeaterPort.Villiaumite();
		case 10:
			return new TileFissionHeaterPort.Carobbiite();
		case 11:
			return new TileFissionHeaterPort.Arsenic();
		case 12:
			return new TileFissionHeaterPort.LiquidNitrogen();
		case 13:
			return new TileFissionHeaterPort.LiquidHelium();
		case 14:
			return new TileFissionHeaterPort.Enderium();
		case 15:
			return new TileFissionHeaterPort.Cryotheum();
		}
		return new TileFissionHeaterPort.Tin();
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
