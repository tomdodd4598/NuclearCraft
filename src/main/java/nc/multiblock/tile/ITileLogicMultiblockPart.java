package nc.multiblock.tile;

import nc.multiblock.*;
import nc.network.multiblock.MultiblockUpdatePacket;

public interface ITileLogicMultiblockPart<MULTIBLOCK extends Multiblock<T, PACKET> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, PACKET>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T, PACKET>, PACKET extends MultiblockUpdatePacket> extends ITileMultiblockPart<MULTIBLOCK> {

    default LOGIC getLogic() {
        MULTIBLOCK multiblock = getMultiblock();

        return multiblock == null ? null : multiblock.getLogic();
    }
}
