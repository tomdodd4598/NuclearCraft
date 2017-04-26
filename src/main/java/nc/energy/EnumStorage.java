package nc.energy;

public class EnumStorage {
	public enum EnergyConnection {
		IN, OUT, BOTH, NON;

		public boolean canReceive() {
			return this == IN || this == BOTH;
		}
		
		public boolean canExtract() {
			return this == OUT || this == BOTH;
		}

		public boolean canConnect() {
			return this != NON;
		}
	}
}
