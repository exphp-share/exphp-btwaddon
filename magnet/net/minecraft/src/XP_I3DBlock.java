package net.minecraft.src;

/**
 * Interface for blocks that render 1 side at a time.
 *
 * When implementing this interface, please just delegate these methods to a member of type XP_3DBlockRenderState.
 *
 */
public interface XP_I3DBlock {
	public void setSideToRender(int side);
	public void setAllSidesToRender();
	public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side);
}
