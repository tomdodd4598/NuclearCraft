package nc.block.basic;


import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPlasmaFire extends BlockFire {

	public BlockPlasmaFire() {
		super();
	}
	
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).isReplaceable(world, x, y, z);
    }
	
	public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {

            if (world.isRaining() && (world.canLightningStrikeAt(x, y, z) || world.canLightningStrikeAt(x - 1, y, z) || world.canLightningStrikeAt(x + 1, y, z) || world.canLightningStrikeAt(x, y, z - 1) || world.canLightningStrikeAt(x, y, z + 1))) {
                world.setBlockToAir(x, y, z);
            } else {
                int l = world.getBlockMetadata(x, y, z);

                world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world) + rand.nextInt(10));

                boolean flag1 = world.isBlockHighHumidity(x, y, z);
                byte b0 = 0;

                if (flag1) b0 = -50;

                this.tryCatchFire(world, x + 1, y, z, 300 + b0, rand, l, WEST );
                this.tryCatchFire(world, x - 1, y, z, 300 + b0, rand, l, EAST );
                this.tryCatchFire(world, x, y - 1, z, 250 + b0, rand, l, UP   );
                this.tryCatchFire(world, x, y + 1, z, 250 + b0, rand, l, DOWN );
                this.tryCatchFire(world, x, y, z - 1, 300 + b0, rand, l, SOUTH);
                this.tryCatchFire(world, x, y, z + 1, 300 + b0, rand, l, NORTH);

                for (int i1 = x - 1; i1 <= x + 1; ++i1) {
                    for (int j1 = z - 1; j1 <= z + 1; ++j1) {
                        for (int k1 = y - 1; k1 <= y + 4; ++k1) {
                            if (i1 != x || k1 != y || j1 != z) {
                                int l1 = 100;

                                if (k1 > y + 1) l1 += (k1 - (y + 1)) * 100;

                                int i2 = this.getChanceOfNeighborsEncouragingFire(world, i1, k1, j1);

                                if (i2 > 0) {
                                    int j2 = (i2 + 40 + world.difficultySetting.getDifficultyId() * 7) / (l + 30);

                                    if (flag1) j2 /= 2;

                                    if (j2 > 0 && rand.nextInt(l1) <= j2 && (!world.isRaining() || !world.canLightningStrikeAt(i1, k1, j1)) && !world.canLightningStrikeAt(i1 - 1, k1, z) && !world.canLightningStrikeAt(i1 + 1, k1, j1) && !world.canLightningStrikeAt(i1, k1, j1 - 1) && !world.canLightningStrikeAt(i1, k1, j1 + 1)) {
                                        int k2 = l + rand.nextInt(5) / 4;

                                        if (k2 > 15) k2 = 15;

                                        world.setBlock(i1, k1, j1, this);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
	
	private void tryCatchFire(World world, int x, int y, int z, int int1, Random rand, int int2, ForgeDirection face) {
        int j1 = world.getBlock(x, y, z).getFlammability(world, x, y, z, face);

        if (rand.nextInt(int1) < j1) {
            boolean flag = world.getBlock(x, y, z) == Blocks.tnt;

            int k1 = int2 + rand.nextInt(5) / 4;

            if (k1 > 15) k1 = 15;

            world.setBlock(x, y, z, this);

            if (flag) Blocks.tnt.onBlockDestroyedByPlayer(world, x, y, z, 1);
        }
    }
	
	private int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z) {
        byte b0 = 0;

        if (!world.isAirBlock(x, y, z)) return 0;
        else {
            int l = b0;
            l = this.getChanceToEncourageFire(world, x + 1, y, z, l, WEST );
            l = this.getChanceToEncourageFire(world, x - 1, y, z, l, EAST );
            l = this.getChanceToEncourageFire(world, x, y - 1, z, l, UP   );
            l = this.getChanceToEncourageFire(world, x, y + 1, z, l, DOWN );
            l = this.getChanceToEncourageFire(world, x, y, z - 1, l, SOUTH);
            l = this.getChanceToEncourageFire(world, x, y, z + 1, l, NORTH);
            return l;
        }
    }
	
	public boolean canNeighborBurn(World world, int x, int y, int z) {
		return this.canCatchFire(world, x + 1, y, z, WEST ) ||
               this.canCatchFire(world, x - 1, y, z, EAST ) ||
               this.canCatchFire(world, x, y - 1, z, UP   ) ||
               this.canCatchFire(world, x, y + 1, z, DOWN ) ||
               this.canCatchFire(world, x, y, z - 1, SOUTH) ||
               this.canCatchFire(world, x, y, z + 1, NORTH);
    }
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {}

    public void onBlockAdded(World world, int x, int y, int z) {
        if (world.provider.dimensionId > 0 || !Blocks.portal.func_150000_e(world, x, y, z)) {
        	world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world) + world.rand.nextInt(10));
        }
    }
}
