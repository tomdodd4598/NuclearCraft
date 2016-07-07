package nc.item;

public class ItemDominos extends ItemFoodNC {
	
	public ItemDominos(int food, float saturation, boolean wolfFood, String nam, String... lines) {
		super(food, saturation, wolfFood, nam, lines);
	}

	/*public boolean onEntityItemUpdate(EntityItem entityItem) {
		if (entityItem.ticksExisted % 100 == 0) {
			entityItem.playSound("nc:paulTooFar", 1.0F, 1.0F);
			entityItem.setVelocity(0, 0.25, 0);
		}
		return false;
    }*/
}
