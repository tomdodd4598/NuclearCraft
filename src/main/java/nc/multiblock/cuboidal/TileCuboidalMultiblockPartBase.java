package nc.multiblock.cuboidal;

import nc.Global;
import nc.multiblock.MultiblockBase;
import nc.multiblock.validation.IMultiblockValidator;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public abstract class TileCuboidalMultiblockPartBase<T extends MultiblockBase> extends CuboidalMultiblockTileBase<T> {
	
	protected final CuboidalPartPositionType positionType;
	
	protected final Class<T> tClass;
	
	public TileCuboidalMultiblockPartBase(Class<T> tClass, CuboidalPartPositionType positionType) {
		super();
		this.tClass = tClass;
		this.positionType = positionType;
	}

	@Override
	public Class<T> getMultiblockType() {
		return tClass;
	}
	
	public boolean isMultiblockAssembled() {
		if (getMultiblock() == null) return false;
		return getMultiblock().isAssembled();
	}
	
	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
	
	public CuboidalPartPositionType getPartPositionType() {
		return positionType;
	}
	
	protected IBlockState getBlockState(BlockPos pos) {
		return getWorld().getBlockState(pos);
	}
	
	protected Block getBlock(BlockPos pos) {
		return getBlockState(pos).getBlock();
	}
	
	@Override
	public boolean isGoodForFrame(IMultiblockValidator validator) {
		if (positionType.isGoodForFrame()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForSides(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForTop(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForBottom(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		setStandardLastError(validator);
		return false;
	}

	@Override
	public boolean isGoodForInterior(IMultiblockValidator validator) {
		if (positionType.isGoodForInterior()) return true;
		setStandardLastError(validator);
		return false;
	}
	
	public void doStandardNullControllerResponse(T controller) {
		if (controller == null) {
			throw nullControllerError();
		}

		if (getMultiblock() == null) {
			nullControllerWarn();
			onAttached(controller);
		}
	}
	
	protected void setStandardLastError(IMultiblockValidator validator) {
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.invalid_block", getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
	}
	
	protected IllegalArgumentException nullControllerError() {
		return new IllegalArgumentException("Attempted to attach " + getBlock(getPos()).getLocalizedName() + " to a null controller. This should never happen - please report this bug to the NuclearCraft GitHub repo!");
	}
	
	void nullControllerWarn() {
		NCUtil.getLogger().warn(getBlock(getPos()).getLocalizedName() + " at (%d, %d, %d) is being assembled without being attached to a controller. It is recommended that the multiblock is completely disassambled and rebuilt if these errors continue!", getPos().getX(), getPos().getY(), getPos().getZ());
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}
}
