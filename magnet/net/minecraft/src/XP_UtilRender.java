package net.minecraft.src;


// TODO: Refactor this and the XP_I3DBlock classes.  Somehow.
// TODO: And for the love of god, find a better method for rendering a block face by face than to repeatedly call 
//       a complicated method that is intended for rendering the entire block!
public class XP_UtilRender {
	private static Tessellator tessie = Tessellator.instance;
	
	public static void render3DBlockTopFace(RenderBlocks renderer, XP_I3DBlock block, int i, int j, int k, int textureIndex) {
		block.setSideToRender(1);
		renderer.renderStandardBlock((Block)block, i, j, k);
	}
	
	public static void render3DBlockBottomFace(RenderBlocks renderer, XP_I3DBlock block, int i, int j, int k, int textureIndex) {
		block.setSideToRender(0);
		renderer.renderStandardBlock((Block)block, i, j, k);
	}
	
	public static void render3DBlockNorthFace(RenderBlocks renderer, XP_I3DBlock block, int i, int j, int k, int textureIndex) {
		block.setSideToRender(4);
		renderer.renderStandardBlock((Block)block, i, j, k);
	}
	
	public static void render3DBlockSouthFace(RenderBlocks renderer, XP_I3DBlock block, int i, int j, int k, int textureIndex) {
		block.setSideToRender(5);
		renderer.renderStandardBlock((Block)block, i, j, k);
	}
	
	public static void render3DBlockEastFace(RenderBlocks renderer, XP_I3DBlock block, int i, int j, int k, int textureIndex) {
		block.setSideToRender(2);
		renderer.renderStandardBlock((Block)block, i, j, k);
	}
	
	public static void render3DBlockWestFace(RenderBlocks renderer, XP_I3DBlock block, int i, int j, int k, int textureIndex) {
		block.setSideToRender(3);
		renderer.renderStandardBlock((Block)block, i, j, k);
	}
}
