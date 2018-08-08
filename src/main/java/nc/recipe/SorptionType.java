package nc.recipe;

public enum SorptionType {
	
	INPUT, OUTPUT, NEUTRAL;

	public boolean checkStackSize(int needed, int toCheck) {
		return this == SorptionType.OUTPUT ? toCheck == needed : (this == SorptionType.INPUT ? toCheck >= needed : true);
	}
}
