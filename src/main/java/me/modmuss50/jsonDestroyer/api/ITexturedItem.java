package me.modmuss50.jsonDestroyer.api;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Use this to apply a texture to an item
 */
public interface ITexturedItem extends IDestroyable {

    /**
     * @param damage the damage of the item that the texture is being loaded for
     * @return the Texture name in the form of "modid:items/textureName"
     */
    String getTextureName(int damage);

    /**
     * @return Return 1 if basic item, else return the max damage
     */
    int getMaxMeta();

    ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining);
}
