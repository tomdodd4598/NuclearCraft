package nc.multiblock;

public interface ITileLogicMultiblockPart<MULTIBLOCK extends Multiblock & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> extends ITileMultiblockPart<MULTIBLOCK> {
	
	public default LOGIC getLogic() {
		return getMultiblock().getLogic();
	}
}
