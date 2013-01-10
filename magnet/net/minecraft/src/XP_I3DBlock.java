package net.minecraft.src;

/**
 * A horrible solution to the issue that, without editing base classes, just about any attempt to draw a block will
 *  either have improper lighting or Z-fighting.
 * <p>
 * Also a horrible solution to the lack of multiple inheritance in Java.  This interface contains methods that I will most
 *  inevitably want to reuse in any other blocks I create.  Methods with "side" in the name have a default implementation in
 *  XP_3DBlockRender.  When implementing this interface on a block, just delegate those methods to a member of that type.
 * <p>
 * You can consider the "render" methods to be abstract.  They have no default implementation (or at least, no default implementation
 *  that doesn't throw an exception in your face).  Define them in your subclass of block.
 */
public interface XP_I3DBlock {
	public static final int faceBot = 0;
	public static final int faceTop = 1;
	public static final int faceE   = 2;
	public static final int faceW   = 3;
	public static final int faceN   = 4;
	public static final int faceS   = 5;
	
	// DELEGATE THE FOLLOWING METHODS TO A MEMBER OF XP_3DBlockRender.
	
	/**
	 * Sets the block to only render the given faces.
	 * @param sides An array of integers (preferably valued 0-5) representing sides to be rendered. 
	 */
	public void setSidesToRender(int[] sides);
	
	/**
	 * Sets a single side to be rendered.  All other sides will be set not to render.
	 * @param side An integer (preferably 0-5) representing the side to render.
	 */
	public void setSideToRender(int side);
	
	/**
	 * Sets a side to be rendered in addition to any other sides already being rendered.
	 * @param side An integer (preferably 0-5) representing the side to render.
	 */
	public void addSideToRender(int side);
	
	/**
	 * Disables all currently enabled sides from rendering.
	 */
	public void setNoSidesToRender();
	
	/**
	 * Enables all sides for rendering.
	 */
	public void setAllSidesToRender();
	
	/**
	 * Overrides Block.shouldSideBeRendered (when implemented in a subclass of Block), which is used
	 *  by RenderBlocks.renderStandardBlock to determine which faces to render.
	 * @param world
	 * @param i
	 * @param j
	 * @param k
	 * @param side
	 */
	public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side);
	
	// THESE METHODS ARE "ABSTRACT".
	// DEFINE THEM IN YOUR CLASS.
	
	/**
	 * Called by mod_XPMagnet.renderWorldBlock to render the block.
	 * @param renderer
	 * @param world
	 * @param i
	 * @param j
	 * @param k
	 */
	public boolean render3DWorldBlock(RenderBlocks renderer, IBlockAccess world, int i, int j, int k);
	
	/**
	 * Called by mod_XPMagnet.renderInvBlock to render the block.
	 * @param renderer
	 * @param metadata
	 */
	public void render3DInventoryBlock(RenderBlocks renderer, int metadata);
}
