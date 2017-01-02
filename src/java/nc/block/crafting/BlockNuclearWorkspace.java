package nc.block.crafting;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.tile.crafting.TileNuclearWorkspace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNuclearWorkspace extends BlockContainer {
	
	private Random rand = new Random();

	public BlockNuclearWorkspace(Material material) {
		super(material);
		
		this.setBlockBounds(0F, 0F, 0F, 1F, 0.75F, 1F);
	}
	
	public int getRenderType() {
		return -1;
	}
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}

	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileNuclearWorkspace();
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("nc:machine/workspace/" + "dummy");
	}
	
	public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int q, float a, float b, float c) {
		if (!player.isSneaking()) {
			player.openGui(NuclearCraft.instance, NuclearCraft.guiIdNuclearWorkspace, world, x, y, z);
			return true;
		} else {
			return false;
		}
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack) {
		 IChatComponent localIChatComponent;
		 localIChatComponent = IChatComponent.Serializer.func_150699_a("[{text:\"Use NuclearCraft's NEI info system or click here for help with the mod!\",color:white,italic:false,clickEvent:{action:open_url,value:\"http://minecraft.curseforge.com/projects/nuclearcraft-mod\"}}]");

		 if (world.isRemote) {((ICommandSender) entityLivingBase).addChatMessage(localIChatComponent);}
	}
	
	public Block idPicked(World world, int x, int y, int z) {
		return NCBlocks.nuclearWorkspace;
	}
	
	public void breakBlock(World world, int x, int y, int z, Block block, int integer) {
		TileNuclearWorkspace craft = (TileNuclearWorkspace)world.getTileEntity(x, y, z);
        if (craft != null) {
            for (int i = 1; i < 26; i++) {
                ItemStack itemstack = craft.getStackInSlot(i);

                if (itemstack != null) {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int j1 = this.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }
                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
                world.func_147453_f(x, y, z, block);
            }
        }
        super.breakBlock(world, x, y, z, block, integer);
    }
}
