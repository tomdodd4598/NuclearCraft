package nc.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {
	
	public static NBTTagCompound saveAllItems(NBTTagCompound tag, List<ItemStack>... lists) {
		if (lists.length == 0) return tag;
		NBTTagList nbttaglist = new NBTTagList();
		
		int i = 0;
		for (List<ItemStack> list : lists) {
			for (ItemStack stack : list) {
				if (!stack.isEmpty()) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setByte("Slot", (byte)i);
					stack.writeToNBT(nbttagcompound);
					nbttaglist.appendTag(nbttagcompound);
				}
				i++;
			}
		}
		
		tag.setTag("Items", nbttaglist);
		
		return tag;
	}
	
	public static void loadAllItems(NBTTagCompound tag, List<ItemStack>... lists) {
		if (lists.length == 0) return;
		NBTTagList nbttaglist = tag.getTagList("Items", 10);
		
		int n = 0, offset = 0;
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			
			while (j - offset >= lists[n].size()) {
				offset += lists[n].size();
				n++;
			}
			if (j - offset >= 0 && j - offset < lists[n].size()) {
				lists[n].set(j - offset, new ItemStack(nbttagcompound));
			}
		}
	}
}
