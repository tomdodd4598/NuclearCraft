package nc.block.crafting;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.tile.crafting.TileNuclearWorkspace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNuclearWorkspace extends BlockContainer {

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
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileNuclearWorkspace();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
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
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemstack)
	{
		 IChatComponent localIChatComponent;
		 localIChatComponent = IChatComponent.Serializer.func_150699_a("[{text:\"Use NuclearCraft's NEI info system or click here for help with the mod!\",color:white,italic:false,clickEvent:{action:open_url,value:\"http://minecraft.curseforge.com/projects/nuclearcraft-mod\"}}]");

		 if (world.isRemote) {((ICommandSender) entityLivingBase).addChatMessage(localIChatComponent);}
	}
	
	public Block idPicked(World world, int x, int y, int z) {
		return NCBlocks.nuclearWorkspace;
	}
}
