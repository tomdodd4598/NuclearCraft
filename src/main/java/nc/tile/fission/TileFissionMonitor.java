package nc.tile.fission;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.render.BlockHighlightTracker;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import static nc.block.property.BlockProperties.FACING_ALL;
import static nc.util.PosHelper.DEFAULT_NON;

public class TileFissionMonitor extends TileFissionPart {
	
	protected BlockPos componentPos = DEFAULT_NON;
	
	public TileFissionMonitor() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
		if (!world.isRemote) {
			EnumFacing facing = getPartPosition().getFacing();
			if (facing != null) {
				world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING_ALL, facing), 2);
			}
		}
	}
	
	public BlockPos getComponentPos() {
		return componentPos;
	}
	
	public IFissionComponent getComponent() {
		FissionReactor reactor = getMultiblock();
		return reactor == null ? null : reactor.getPartMap(IFissionComponent.class).get(componentPos.toLong());
	}
	
	// IMultitoolLogic
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
		if (nbt != null) {
			if (!player.isSneaking()) {
				String displayName = getTileBlockDisplayName();
				if (nbt.hasKey("fissionComponentInfo", 10)) {
					NBTTagCompound info = nbt.getCompoundTag("fissionComponentInfo");
					if (info.hasKey("componentPos", 99)) {
						componentPos = BlockPos.fromLong(info.getLong("componentPos"));
						BlockHighlightTracker.sendPacket(player, componentPos, 5000);
						player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.load_component_info", info.getString("displayName"), displayName)));
						nbt.removeTag("fissionComponentInfo");
						return true;
					}
					else {
						player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.invalid_component_info", info.getString("displayName"), displayName)));
					}
				}
				else {
					if (!DEFAULT_NON.equals(componentPos)) {
						IFissionComponent component = getComponent();
						if (component != null) {
							BlockHighlightTracker.sendPacket(player, componentPos, 5000);
							BlockPos pos = component.getTilePos();
							player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.connected_component_info", displayName, component.getTileBlockDisplayName(), pos.getX(), pos.getY(), pos.getZ())));
							return true;
						}
					}
					player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.no_connected_component_info", displayName)));
					return true;
				}
			}
		}
		return super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setLong("componentPos", componentPos.toLong());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		componentPos = BlockPos.fromLong(nbt.getLong("componentPos"));
	}
}
