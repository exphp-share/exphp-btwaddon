package net.minecraft.src;

import java.util.HashSet;
import java.util.Set;

/**
 * This class provides default implementations for some of the methods in {@code XP_I3DBlock}.
 * <p>
 * Have a block implement {@code XP_I3DBlock} and delegate the side-related methods to this class.
 */
public class XP_3DBlockRender implements XP_I3DBlock {
	
	public Block owner;
	public boolean renderAllSides = true;
	public Set<Integer> sidesToRender = new HashSet();
	
	/**
	 * Constructor.
	 * 
	 * @param owner The block of which this XP_3DBlockRender is a member.
	 */
	public XP_3DBlockRender(Block owner) {
		this.owner = owner;
	}
	
	@Override // XP_I3DBlock
	public void setSideToRender(int side) {
		this.setNoSidesToRender();
		this.addSideToRender(side);
	}
	
	@Override // XP_I3DBlock
	public void setNoSidesToRender() {
		this.renderAllSides = false;
		this.sidesToRender.clear();
	}
	
	@Override // XP_I3DBlock
	public void setAllSidesToRender() {
		this.renderAllSides = true;
	}
	
	@Override // XP_I3DBlock
	public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side) {
		return this.renderAllSides || this.sidesToRender.contains(side);
	}
	
	@Override // XP_I3DBlock
	public void addSideToRender(int side) {
		this.sidesToRender.add(side);
	}
	
	@Override // XP_I3DBlock
	public void setSidesToRender(int[] sides) {
		this.setNoSidesToRender();
		for (int side : sides) {
			this.addSideToRender(side);
		}
	}
	
	/**
	 * !!! DO NOT USE !!!
	 */
	@Override // XP_I3DBlock
	public boolean render3DWorldBlock(RenderBlocks renderer, IBlockAccess world, int i, int j, int k) {
		this.__tellUserToCastrateExpHP();
		return false;
	}
	
	/**
	 * !!! DO NOT USE !!!
	 */
	@Override // XP_I3DBlock
	public void render3DInventoryBlock(RenderBlocks renderer, int metadata) {
		this.__tellUserToCastrateExpHP();
	}
	
	private void __tellUserToCastrateExpHP() {
		Exception error = new Exception(String.format("ExpHP didn't listen to himself and broke %s. Go castrate him.", this.owner.getBlockName()));
		ModLoader.throwException("Zoinks!", error);
	}
}
