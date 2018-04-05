package nc.multiblock.fission.moltensalt.block;

import nc.Global;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockControllerBase;
import nc.multiblock.validation.ValidationError;
import nc.proxy.CommonProxy;
import nc.util.Lang;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSaltFissionPartBase extends BlockContainer {
	
	public BlockSaltFissionPartBase(String name) {
		super(Material.IRON);
		setUnlocalizedName(Global.MOD_ID + "." + name);
		setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		setDefaultState(blockState.getBaseState());
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setCreativeTab(CommonProxy.TAB_SALT_FISSION_BLOCKS);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	public boolean rightClickOnPart(World world, BlockPos pos, EntityPlayer player) {
		if (!world.isRemote) {
			if (player.getHeldItemMainhand().isEmpty()) {
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof IMultiblockPart) {
					MultiblockControllerBase controller = ((IMultiblockPart) tile).getMultiblockController();
					if (controller != null) {
						ValidationError e = controller.getLastError();
						if (e != null) {
							player.sendMessage(e.getChatMessage());
							return true;
						}
					} else {
						player.sendMessage(new TextComponentString(Lang.localise(Global.MOD_ID + ".multiblock_validation.no_controller")));
						return true;
					}
				}
			}
		}
		return false;
	}
}
