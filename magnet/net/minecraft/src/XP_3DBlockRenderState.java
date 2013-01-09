package net.minecraft.src;

/**
 * This class provides default implementations for the methods in XP_I3DBlock
 *
 * Have a block implement XP_I3DBlock and delegate the following methods to this class, and it's almost like you have
 *  multiple inheritance.  Except that you don't, and your code will smell funky.
 * 
 */
public class XP_3DBlockRenderState implements XP_I3DBlock {
	private boolean __renderAllSides = true;
	private int __sideToRender = 0;
	
	@Override // XP_I3DBlock
	public void setSideToRender(int side) {
		this.__renderAllSides = false;
		this.__sideToRender = side;
	}
	
	@Override // XP_I3DBlock
	public void setAllSidesToRender() {
		this.__renderAllSides = true;
	}
	
	@Override // XP_I3DBlock
	public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side) {
		return __renderAllSides || (side == this.__sideToRender);
	}
}
