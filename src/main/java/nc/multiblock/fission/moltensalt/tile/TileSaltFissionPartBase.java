package nc.multiblock.fission.moltensalt.tile;

import nc.Global;
import nc.multiblock.MultiblockControllerBase;
import nc.multiblock.cuboidal.CuboidalMultiblockTileEntityBase;
import nc.multiblock.validation.IMultiblockValidator;
import nc.util.NCUtil;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public abstract class TileSaltFissionPartBase extends CuboidalMultiblockTileEntityBase {
	
	private final PartPositionType positionType;
	
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
	
	@Override
	public void onMachineActivated() {
		
	}

	@Override
	public void onMachineDeactivated() {
		
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
	
	Block getBlock(BlockPos pos) {
		return this.getWorld().getBlockState(pos).getBlock();
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
}
