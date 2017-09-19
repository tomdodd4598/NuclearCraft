package nc.recipe;

public enum StackType {
	
	ITEM, FLUID, UNSPECIFIED;
	
	public boolean isItem() {
		return this == ITEM;
	}
	
	public boolean isFluid() {
		return this == FLUID;
	}
}
