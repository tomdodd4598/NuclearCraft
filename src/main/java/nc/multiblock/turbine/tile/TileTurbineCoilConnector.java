package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import net.minecraft.util.EnumFacing;

public class TileTurbineCoilConnector extends TileTurbinePartBase  {
    public TileTurbineCoilConnector() {
        super(CuboidalPartPositionType.WALL);
    }

    @Override
    public void onMachineAssembled(Turbine controller) {
        doStandardNullControllerResponse(controller);
        super.onMachineAssembled(controller);
    }

    @Override
    public void onMachineBroken() {
        super.onMachineBroken();
        if (getWorld().isRemote) return;
    }

    public boolean isValidPosition(EnumFacing dir){
        return world.getTileEntity(pos.offset(dir)) instanceof TileTurbineDynamoCoil;
    }

}
