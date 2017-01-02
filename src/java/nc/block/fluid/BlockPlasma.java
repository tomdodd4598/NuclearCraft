package nc.block.fluid;

import java.util.Random;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPlasma extends BlockFluidClassic {
	@SideOnly(Side.CLIENT)
    public static IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    public static IIcon flowingIcon;
    
    public static DamageSource damageSource;
    
    public static int tickCount;
    
    private Random rand1 = new Random();
    
	public BlockPlasma(Fluid f, Material m, DamageSource damage) {
		super(f, m);
		damageSource = damage;
		setQuantaPerBlock(10);
		setCreativeTab(NuclearCraft.tabNC);
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
            return (side == 0 || side == 1)? stillIcon : flowingIcon;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
            stillIcon = register.registerIcon("nc:fluid/plasmaStill");
            flowingIcon = register.registerIcon("nc:fluid/plasmaFlowing");
    }
    
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.canDisplace(world, x, y, z);
    }
    
    public boolean displaceIfPossible(World world, int x, int y, int z) {
            if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
            return super.displaceIfPossible(world, x, y, z);
    }
    
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    	if ((entity.motionY < -0.8D) || (entity.motionY > 0.8D)) {
    		entity.motionY *= 0.8D;
    	}
    	if ((entity.motionZ < -0.8D) || (entity.motionZ > 0.8D)) {
    		entity.motionZ *= 0.8D;
    	}
    	if ((entity.motionX < -0.8D) || (entity.motionX > 0.8D)) {
    		entity.motionX *= 0.8D;
    	}
    	if (world.isRemote) {
    		return;
    	}
    	if (entity instanceof EntityBlaze) {
    		return;
    	} else {
    		boolean bool = entity.velocityChanged;
    		entity.attackEntityFrom(damageSource, 8.0F);
    		entity.velocityChanged = bool;
    	}
    }
    
    public boolean f(World world, int x, int y, int z) {
		return world.getBlock(x, y, z) != NCBlocks.electromagnetActive && world.getBlock(x, y, z) != NCBlocks.fusionReactor && world.getBlock(x, y, z) != NCBlocks.fusionReactorBlock && world.getBlock(x, y, z) != Blocks.air;
	}
    
    public boolean f2(World world, int x, int y, int z) {
		return world.getBlock(x, y, z) != NCBlocks.electromagnetActive;
	}
    
    public boolean g(World world, int x, int y, int z) {
		return world.getBlock(x, y, z) != NCBlocks.electromagnetIdle && world.getBlock(x, y, z) != NCBlocks.electromagnetActive && world.getBlock(x, y, z) != NCBlocks.blockFusionPlasma;
	}
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
    	/*if (rand1.nextFloat() > 0.0F && g(world, x, y - 1, z) && !this.isSourceBlock(world, x, y, z)) world.setBlock(x, y, z, NCBlocks.plasmaFire);
    	if (tickCount >= 20) {
    		if (this.isSourceBlock(world, x, y, z) && (world.getBlock(x, y + 1, z) == Blocks.air || world.getBlock(x, y + 1, z) == NCBlocks.plasmaFire || world.getBlock(x, y + 1, z) == NCBlocks.blockFusionPlasma)) {
    			world.setBlock(x, y, z, Blocks.air); world.setBlock(x, y + 1, z, NCBlocks.blockFusionPlasma);
    		}
    	} else tickCount ++;*/
    	super.updateTick(world, x, y, z, rand);
    	if (world.getBlock(x, y + 1, z) != NCBlocks.blockFusionPlasma) {
    		if (isSourceBlock(world, x, y, z)) {
    			if (rand1.nextFloat() > 0.96F && f2(world, x, y - 1, z)) world.setBlock(x, y, z, Blocks.fire);
    		} else {
    			if (rand1.nextFloat() > 0.3F && f(world, x, y - 1, z)) world.setBlock(x, y, z, Blocks.fire);
    		}
    	}
    }
}
