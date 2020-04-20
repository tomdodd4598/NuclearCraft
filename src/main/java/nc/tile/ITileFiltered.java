package nc.tile;

public interface ITileFiltered extends ITile {
	
	public boolean canModifyFilter(int slot);
	
	public void onFilterChanged(int slot);
	
	public int getFilterID();
}
