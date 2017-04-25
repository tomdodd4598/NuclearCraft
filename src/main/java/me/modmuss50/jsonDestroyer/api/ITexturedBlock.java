package me.modmuss50.jsonDestroyer.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * Use this to texture a block
 */
public interface ITexturedBlock extends IDestroyable {

    /**
     * @param state the block state, ignore this is you have a block with no states.
     * @param side  the side of the block, ignore this is you want every side of your block to be the same
     * @return the texture name in the form of "modid:blocks/textureName.png"
     */
    String getTextureNameFromState(IBlockState state, EnumFacing side);


    /**
     * Return 0 if you are not using sates
     *
     * @return the amount of states to load textures for, the state is based on Block.getStateFromMeta
     */
    int amountOfStates();
}
