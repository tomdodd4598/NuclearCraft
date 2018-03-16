package ic2.api.energy.tile;

import net.minecraft.util.EnumFacing;

/**
 * An interface to mark a tile entity as a producer of heat
 * <p>Other tile entities that want to draw heat will call these methods,
 * the source implementor has no obligation to find heat sinks/acceptors itself</p>
 *
 * @author Thunderdark, Chocohead
 */
public interface IHeatSource {
	/** @deprecated Use {@link #getConnectionBandwidth(EnumFacing)} */
	@Deprecated
	int  maxrequestHeatTick(EnumFacing directionFrom);

	/**
	 * Gets the maximum heat that can ever be expected to be taken from the given side
	 *
	 * @param side The side that the heat would be pulled from
	 * @return The theoretical maximum heat that could be taken from the given side
	 */
	default int getConnectionBandwidth(EnumFacing side) {
		return maxrequestHeatTick(side);
	}

	/** @deprecated Use {@link #drawHeat(EnumFacing, int, boolean)} */
	@Deprecated
	int requestHeat(EnumFacing directionFrom, int requestheat);

	/**
	 * Try draw/simulate drawing the requested heat out of the given side
	 *
	 * @param side The side to draw the heat from
	 * @param request The amount of heat to draw
	 * @param simulate Whether to actually draw the heat or not
	 *
	 * @return The actual transmitted heat
	 */
	default int drawHeat(EnumFacing side, int request, boolean simulate) {
		return !simulate ? requestHeat(side, request) : maxrequestHeatTick(side);
	}
}
