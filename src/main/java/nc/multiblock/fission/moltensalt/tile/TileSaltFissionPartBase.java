package nc.multiblock.fission.moltensalt.tile;

import nc.Global;
import nc.multiblock.MultiblockControllerBase;
import nc.multiblock.cuboidal.CuboidalMultiblockTileEntityBase;
import nc.multiblock.fission.moltensalt.SaltFissionReactor;
import nc.multiblock.validation.IMultiblockValidator;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public abstract class TileSaltFissionPartBase extends CuboidalMultiblockTileEntityBase {
	
	private final PartPositionType positionType;
	
	public boolean isReactorOn;
	
	public TileSaltFissionPartBase(PartPositionType positionType) {
		super();
		this.positionType = positionType;
	}
	
	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new SaltFissionReactor(getWorld());
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return SaltFissionReactor.class;
	}
	
	public SaltFissionReactor getReactor() {
		if (getMultiblockController() instanceof SaltFissionReactor) return (SaltFissionReactor) getMultiblockController();
		return null;
	}
	
	public boolean isMultiblockAssembled() {
		if (getMultiblockController() == null) return false;
		return getMultiblockController().isAssembled();
	}
	
	public void setIsReactorOn() {
		if (getReactor() != null) isReactorOn = getReactor().isReactorOn;
	}
	
	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
	}
	
	public PartPositionType getPartPositionType() {
		return positionType;
	}
	
	static enum PartPositionType {
		WALL,
		FRAME,
		INTERIOR;
		
		boolean isGoodForWalls() {
			return this == WALL;
		}
		
		boolean isGoodForFrame() {
			return this == FRAME;
		}
		
		boolean isGoodForInterior() {
			return this == INTERIOR;
		}
	}
	
	IBlockState getBlockState(BlockPos pos) {
		return getWorld().getBlockState(pos);
	}
	
	Block getBlock(BlockPos pos) {
		return getBlockState(pos).getBlock();
	}
	
	@Override
	public boolean isGoodForFrame(IMultiblockValidator validator) {
		if (positionType.isGoodForFrame()) return true;
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_here", getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
		return false;
	}

	@Override
	public boolean isGoodForSides(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_here", getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
		return false;
	}

	@Override
	public boolean isGoodForTop(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_here", getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
		return false;
	}

	@Override
	public boolean isGoodForBottom(IMultiblockValidator validator) {
		if (positionType.isGoodForWalls()) return true;
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_here", getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
		return false;
	}

	@Override
	public boolean isGoodForInterior(IMultiblockValidator validator) {
		if (positionType.isGoodForInterior()) return true;
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_here", getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
		return false;
	}
	
	public void doStandardNullControllerResponse(MultiblockControllerBase controller) {
		if (controller == null) {
			throw nullControllerError();
		}

		if (this.getMultiblockController() == null) {
			nullControllerWarn();
			onAttached(controller);
		}
	}
	
	void setLastValidatorError(IMultiblockValidator validator) {
		validator.setLastError(Global.MOD_ID + ".multiblock_validation.salt_fission.invalid_here", getPos().getX(), getPos().getY(), getPos().getZ(), getBlock(getPos()).getLocalizedName());
	}
	
	IllegalArgumentException nullControllerError() {
		return new IllegalArgumentException("Attempted to attach " + getBlock(getPos()).getLocalizedName() + " to a null controller. This should never happen - please report this bug to the NuclearCraft GitHub repo!");
	}
	
	void nullControllerWarn() {
		NCUtil.getLogger().warn(getBlock(getPos()).getLocalizedName() + " at (%d, %d, %d) is being assembled without being attached to a controller. It is recommended that the multiblock is completely disassambled and rebuilt if these errors continue!", getPos().getX(), getPos().getY(), getPos().getZ());
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isReactorOn", isReactorOn);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isReactorOn = nbt.getBoolean("isReactorOn");
	}
}
