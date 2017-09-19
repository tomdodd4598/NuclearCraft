package nc.recipe;

public enum SorptionType {
	
	INPUT, OUTPUT, NEUTRAL;

	public boolean checkStackSize(int needed, int input) {
		return this == SorptionType.OUTPUT ? input == needed : (this == SorptionType.INPUT ? input >= needed : true);
	}
}
