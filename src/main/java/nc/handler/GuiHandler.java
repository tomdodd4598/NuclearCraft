package nc.handler;

import it.unimi.dsi.fastutil.objects.*;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	private static int guiId = 0;
	
	private static final Object2IntMap<String> GUI_ID_MAP = new Object2IntOpenHashMap<>();
	
	public static int getGuiId(String name) {
		if (!GUI_ID_MAP.containsKey(name)) {
			GUI_ID_MAP.put(name, guiId++);
		}
		return GUI_ID_MAP.getInt(name);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof ITileGui) {
			return ((ITileGui) tile).getContainerInfo().getNewContainer(id, player, tile);
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof ITileGui) {
			return ((ITileGui) tile).getContainerInfo().getNewGui(id, player, tile);
		}
		return null;
	}
}
