package nc.block.tile.processor;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.tile.BlockSidedInventoryGui;
import nc.proxy.CommonProxy;
import nc.tile.energy.generator.TileEnergyGeneratorProcessor;
import nc.tile.energy.processor.TileEnergyProcessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockProcessor extends BlockSidedInventoryGui {
	
	private final boolean isActive;
	private SoundEvent sound = null;
	private String particle1 = "";
	private String particle2 = "";
	
	public BlockProcessor(String unlocalizedName, String registryName, String particle1, String particle2, SoundEvent sound, boolean isActive, int guiId) {
		super(unlocalizedName, registryName, Material.IRON, guiId);
		this.particle1 = particle1;
		this.particle2 = particle2;
		this.sound = sound;
		this.isActive = isActive;
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		if (!isActive) setCreativeTab(CommonProxy.NC_TAB);
		//else setLightLevel(0.875F);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else if (player != null) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEnergyProcessor || tileentity instanceof TileEnergyGeneratorProcessor) {
				FMLNetworkHandler.openGui(player, NuclearCraft.instance, guiId, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("incomplete-switch")
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (particle1 == "" && particle2 == "") return;
		if (isActive) {
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			double d0 = (double)pos.getX() + 0.5D;
			double d1 = (double)pos.getY() + 0.125D + rand.nextDouble() * 0.75D;
			double d2 = (double)pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			
			if (sound != null) {
				if (rand.nextDouble() < 0.2D) {
					world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, sound, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
				}
			}
		
			switch (enumfacing) {
				case WEST:
					if (particle1 != "") world.spawnParticle(EnumParticleTypes.getByName(particle1), d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					if (particle2 != "") world.spawnParticle(EnumParticleTypes.getByName(particle2), d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case EAST:
					if (particle1 != "") world.spawnParticle(EnumParticleTypes.getByName(particle1), d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					if (particle2 != "") world.spawnParticle(EnumParticleTypes.getByName(particle2), d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case NORTH:
					if (particle1 != "") world.spawnParticle(EnumParticleTypes.getByName(particle1), d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
					if (particle2 != "") world.spawnParticle(EnumParticleTypes.getByName(particle2), d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case SOUTH:
					if (particle1 != "") world.spawnParticle(EnumParticleTypes.getByName(particle1), d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
					if (particle2 != "") world.spawnParticle(EnumParticleTypes.getByName(particle2), d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
}
