package nc.enumm;

import net.minecraft.util.IStringSerializable;

public class SideConfigEnums {
	
	public enum ProcessorSideConfig implements IStringSerializable {

		DEFAULT(-1, false, false), DISABLED(-1, false, false),
		ITEM_INPUT_1(1, true, false), ITEM_INPUT_2(2, true, false), ITEM_INPUT_3(3, true, false), ITEM_INPUT_4(4, true, false),
		ITEM_OUTPUT_1(1, false, false), ITEM_OUTPUT_2(2, false, false), ITEM_OUTPUT_3(3, false, false), ITEM_OUTPUT_4(4, false, false),
		FLUID_INPUT_1(1, true, true), FLUID_INPUT_2(2, true, true), FLUID_INPUT_3(3, true, true), FLUID_INPUT_4(4, true, true),
		FLUID_OUTPUT_1(1, false, true), FLUID_OUTPUT_2(2, false, true), FLUID_OUTPUT_3(3, false, true), FLUID_OUTPUT_4(4, false, true);
		
		private int slot;
		private boolean isInput;
		private boolean isFluid;
		
		private ProcessorSideConfig(int slot, boolean isInput, boolean isFluid) {
			this.slot = slot;
			this.isInput = isInput;
			this.isFluid = isFluid;
		}
		
		public boolean isDefault() {
			return this == DEFAULT;
		}
		
		public boolean isDisabled() {
			return this == DISABLED;
		}
		
		public boolean isItemInput(int slot) {
			return this.slot == slot && isInput && !isFluid;
		}
		
		public boolean isItemOutput(int slot) {
			return this.slot == slot && !isInput && !isFluid;
		}
		
		public boolean isFluidInput(int slot) {
			return this.slot == slot && isInput && isFluid;
		}
		
		public boolean isFluidOutput(int slot) {
			return this.slot == slot && !isInput && isFluid;
		}
		
		@Override
		public String getName() {
			return this.name().toLowerCase();
		}

		@Override
		public String toString() {
			return this.getName();
		}
	}
	
	public enum BatterySideConfig implements IStringSerializable {

		INPUT, OUTPUT, BOTH, DISABLED;
		
		public boolean canReceive() {
			return this == INPUT || this == BOTH;
		}
		
		public boolean canExtract() {
			return this == OUTPUT || this == BOTH;
		}

		public boolean canConnect() {
			return this != DISABLED;
		}
		
		@Override
		public String getName() {
			return this.name().toLowerCase();
		}

		@Override
		public String toString() {
			return this.getName();
		}
	}
}
