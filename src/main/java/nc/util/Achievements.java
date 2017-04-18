package nc.util;

import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class Achievements {
    public final AchievementPage page;

    public Achievements(String name) {
        page = new AchievementPage(name, new Achievement[0]);
        AchievementPage.registerAchievementPage(page);
        
        //cpw.mods.fml.common.FMLCommonHandler.instance().bus().register(this);
    }

    public Achievement registerAchievement(Achievement achievement) {
            page.getAchievements().add(achievement.registerStat());
            return achievement;
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        Item item = event.crafting.getItem();
        int damage = event.crafting.getItemDamage();

        for (Achievement a : page.getAchievements()) {
            if (item.equals(a.theItemStack.getItem()) && damage == a.theItemStack.getItemDamage()) {
                event.player.addStat(a, 1);
            }
        }
    }
}