package nc.block.quantum;

import java.util.Random;

import nc.block.NCBlocks;
import nc.tile.quantum.TileSimpleQuantum;
import nc.util.Complex;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSimpleQuantum extends BlockContainer {

	public BlockSimpleQuantum(boolean s) {
		super(Material.iron);
		spin = s;
	}
	
	public final boolean spin;
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileSimpleQuantum();
	}

	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister i) {
		blockIcon = i.registerIcon("nc:" + this.getUnlocalizedName().substring(5));
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return blockIcon;
	}
	
	public static void set(double a, boolean s, World worldObj, int x, int y, int z) {
		int meta = worldObj.getBlockMetadata(x, y, z);
		TileSimpleQuantum tile = (TileSimpleQuantum) worldObj.getTileEntity(x, y, z);
		if (s) worldObj.setBlock(x, y, z, NCBlocks.simpleQuantumUp);
		else worldObj.setBlock(x, y, z, NCBlocks.simpleQuantumDown);
		tile.angle = a;
		worldObj.setBlockMetadataWithNotify(x, y, z, meta, 2);
		if(tile != null) {
			tile.validate();
			worldObj.setTileEntity(x, y, z, tile);
		}
	}
	
	public void s(World world, EntityLivingBase elb, String s) {
		if (world.isRemote) ((ICommandSender) elb).addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + s));
	}
	
	public void m(World world, EntityLivingBase elb, Complex[][] z) {
		s(world, elb, Arrays.deepToString(z));
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			TileSimpleQuantum t = (TileSimpleQuantum) world.getTileEntity(x, y, z);
			double newAngle = (player.rotationYaw + 360D) % 360;
			//double displayedAngle = (player.rotationYaw - t.angle + 360D) % 360 <= 180 ? (player.rotationYaw - t.angle + 360D) % 360 : 360 - ((player.rotationYaw - t.angle + 360D) % 360);
			double p = Math.pow(Math.cos(((t.angle - newAngle)/2)*(Math.PI/180)), 2);
			double rand = new Random().nextDouble();
			boolean s = false;
			if (player != null) {
				if (!world.isRemote) {
					if (p == 0) {
						s = false;
					} else if (p >= rand) {
						s = true;
					} else {
						s = false;
					}
					//set(newAngle, s, world, x, y, z);
					t.spin = s;
				}
				if (s == true) {
					t.angle = newAngle;
				} else {
					t.angle = /*180 +*/ newAngle;
				}
			}
			//if (world.isRemote) player.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + ("Angle: " + Math.round(displayedAngle) + "°")));
		}
		return true;
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack itemstack) {
		double l = (double) ((elb.rotationYaw + 360) % 360);
		TileSimpleQuantum t = (TileSimpleQuantum) world.getTileEntity(x, y, z);
		t.angle = l;
		t.spin = true;
		
		//if (world.isRemote) ((ICommandSender) elb).addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + ("Angle: " + Math.round(t.angle))));
		
		/*m(world, elb, Matrix.spinZ(1, 0.5, 1));
		m(world, elb, Matrix.spinX(1, 0.5, 1));
		m(world, elb, Matrix.spinY(1, 0.5, 1));
		
		m(world, elb, Matrix.spinZ(2, 0.5, 1));
		m(world, elb, Matrix.spinX(2, 0.5, 1));
		m(world, elb, Matrix.spinY(2, 0.5, 1));*/
	}

	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return false;
	}
	
	public boolean canProvidePower() {
		return true;
	}
	
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		if (world.getBlock(x, y, z) == NCBlocks.simpleQuantumUp) return 0;
		else return 15;
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(NCBlocks.simpleQuantumUp);
	}
}