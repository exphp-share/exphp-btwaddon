package net.minecraft.src;

import forge.ITextureProvider;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.lwjgl.opengl.GL11;

public class XP_BlockMagnet extends BlockContainer implements FCIBlockMechanical, FCIBlock, ITextureProvider, XP_I3DBlock {
	
	private XP_3DBlockRender __3DRenderState = new XP_3DBlockRender(this);
	
	// Metadata bits are, from lowest to highest:
	//    MechanicalInput  **UNUSED**  PowerLevel-PowerLevel
	private static final int __flagMechInput = 1;
	private static final int __flagPowerLevelOffset = 2;
	private static final int __flagPowerLevelMask   = 12;
	
	private static final double __bottomRingWidth = 0.25D;
	private static final double __bottomHoleDepth = 0.125D;
	
	protected XP_BlockMagnet(int id) {
		super(id, Material.rock);

		this.blockIndexInTexture = 0;
		this.setHardness(3.5F);
		this.setStepSound(soundStoneFootstep);
		this.setBlockName("xpMagnet");
		this.setRequiresSelfNotify();
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override // Block
	public void onBlockAdded(World world, int i, int j, int k) {
		world.setBlockTileEntity(i, j, k, this.createNewTileEntity(world,i,j,k));
		
		this.__updateMagnet(world, i, j, k);
	}
	
	// Makes the magnet update its state (metadata and tile entity state) to fit its current surroundings.
	private void __updateMagnet(World world, int i, int j, int k) {
		this.__updateFlagMechInput(world, i, j, k);
		
		// Update the tile entity's state next tick.
		world.addBlockEvent(i, j, k, this.blockID, 0, 0);
	}
	
	private void __updateFlagMechInput(World world, int i, int j, int k) {
		int oldMetadata = world.getBlockMetadata(i, j, k);
		int metadata = oldMetadata;
		
		if (this.IsInputtingMechanicalPower(world, i, j, k)) {
			metadata |= this.__flagMechInput;
		} else {
			metadata &= ~this.__flagMechInput;
		}
		
		int oldState = (oldMetadata & this.__flagMechInput) / this.__flagMechInput;
		int state = (metadata & this.__flagMechInput) / this.__flagMechInput;
		
		if (oldState != state) {
			world.setBlockMetadataWithNotify(i, j, k, metadata);
		}
	}
	
	public boolean getFlagMechInput(World world, int i, int j, int k) {
		return ((world.getBlockMetadata(i, j, k) & this.__flagMechInput) != 0);
	}
	
	public void setFlagPowerLevel(World world, int i, int j, int k, int powerLevel) {
		int metadata;
		metadata =  world.getBlockMetadata(i, j, k);
		metadata &= ~(this.__flagPowerLevelMask);
		metadata |= (powerLevel << this.__flagPowerLevelOffset);
		
		world.setBlockMetadata(i, j, k, metadata);
	}
	
	public int getFlagPowerLevel(World world, int i, int j, int k) {
		return this.getFlagPowerLevelFromMetadata(world.getBlockMetadata(i, j, k));
	}
	
	public int getFlagPowerLevelFromMetadata(int metadata) {
		// Take advantage of the fact that power level is stored in the highest order bits
		return metadata >> this.__flagPowerLevelOffset;
	}
	
	@Override // Block
	public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
		XP_TileEntityMagnet tileEnt = (XP_TileEntityMagnet)world.getBlockTileEntity(i, j, k);
		
		if (tileEnt != null) {
			tileEnt.emitBeamParticles(rand);
		}
	}
	
	@Override // Block
	public void onNeighborBlockChange(World world, int i, int j, int k, int blockId) {
		this.__updateMagnet(world, i, j, k);
	}
	
	@Override // Block
	public void updateTick(World world, int i, int j, int k, Random rand) {
		// Random ticks are necessary in case somebody places a fence 2 blocks below, which does not trigger an update
		this.__updateMagnet(world, i, j, k);
	}
	
	@Override // BlockContainer
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	public TileEntity createNewTileEntity(World world, int i, int j, int k) {
		return new XP_TileEntityMagnet(i, j, k);
	}
	
	@Override // FCIBlockMechanical
	public boolean CanOutputMechanicalPower() {
		return false;
	}

	@Override // FCIBlockMechanical
	public boolean CanInputMechanicalPower() {
		return true;
	}

	@Override // FCIBlockMechanical
	public boolean IsInputtingMechanicalPower(World world, int i, int j, int k) {
		return FCUtilsMechPower.IsBlockPoweredByAxleToSide(world, i, j, k, 1); // Axle to TOP
	}

	@Override // FCIBlockMechanical
	public boolean IsOutputtingMechanicalPower(World world, int i, int j, int k) {
		return false;
	}

	@Override // FCIBlockMechanical
	public boolean CanInputAxlePowerToFacing(World world, int i, int j, int k, int facing) {
		return facing == 1;
	}

	@Override // FCIBlockMechanical
	public void Overpower(World world, int i, int j, int k) {
		int c;
		for (c = 0; c < 5; ++c) FCUtilsItem.EjectSingleItemWithRandomOffset(world, i, j, k, Block.stone.blockID, 0);
		for (c = 0; c < 3; ++c) FCUtilsItem.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcSteel.itemID, 0);
		for (c = 0; c < 2; ++c) FCUtilsItem.EjectSingleItemWithRandomOffset(world, i, j, k, Block.torchRedstoneActive.blockID, 0);
		for (c = 0; c < 2; ++c) FCUtilsItem.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcPolishedLapis.itemID, 0);
		for (c = 0; c < 1; ++c) FCUtilsItem.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.itemID, 0);
		
		world.playAuxSFX(mod_FCBetterThanWolves.m_iMechanicalDeviceExplodeAuxFXID, i, j, k, 0);
		world.setBlockWithNotify(i, j, k, 0);
	}
	
	@Override // FCIBlock
	public int GetFacing(IBlockAccess world, int i, int j, int k) {
		return 0;
	}
	
	@Override // FCIBlock
	public void SetFacing(World world, int i, int j, int k, int facing) {}

	@Override // FCIBlock
	public int GetFacingFromMetadata(int metadata) {
		return 0;
	}

	@Override // FCIBlock
	public int SetFacingInMetadata(int metadata, int facing) {
		return metadata;
	}

	@Override // FCIBlock
	public boolean CanRotateOnTurntable(IBlockAccess world, int i, int j, int k) {
		return false;
	}

	@Override // FCIBlock
	public boolean CanTransmitRotationHorizontallyOnTurntable(IBlockAccess world, int i, int j, int k) {
		return true;
	}

	@Override // FCIBlock
	public boolean CanTransmitRotationVerticallyOnTurntable(IBlockAccess world, int i, int j, int k) {
		return true;
	}

	@Override // FCIBlock
	public void RotateAroundJAxis(World world, int i, int j, int k, boolean clockwise) {}

	@Override // FCIBlock
	public int RotateMetadataAroundJAxis(int metadata, boolean clockwise) {
		return metadata;
	}

	@Override // FCIBlock
	public boolean ToggleFacing(World world, int i, int j, int k, boolean unused_probablyReverseOrder) {
		return false;
	}
	
	@Override // Block
	public void ClientNotificationOfMetadataChange(World world, int i, int j, int k, int prevMetadata, int metadata) {
		world.setBlockMetadata(i, j, k, metadata);
	}
	
	@Override // Block
	public int getBlockTexture(IBlockAccess world, int i, int j, int k, int side) {
		return this.getBlockTextureFromSideAndMetadata(side, world.getBlockMetadata(i, j, k));
	}

	@Override // Block
	public int getBlockTextureFromSide(int side) {
		return this.getBlockTextureFromSideAndMetadata(side, 0);
	}
	
	@Override // Block
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		switch(side) {
		case 0:
			return mod_XPMagnet.magTerrainMagnetBottom + this.getFlagPowerLevelFromMetadata(metadata);
		case 1:
			return mod_XPMagnet.magTerrainMagnetTop;
		default:
			return mod_XPMagnet.magTerrainMagnetSide + this.getFlagPowerLevelFromMetadata(metadata);
		}
	}
	
	@Override // Block
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override // Block
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override // Block
	public int getRenderType() {
		return mod_XPMagnet.rendId3dBlock; 
	}
	
	// An attempt at rendering the block with a hole on the bottom so that items
	//  don't appear to clip into it as they bob up and down.
	public boolean render3DWorldBlock(RenderBlocks renderer, IBlockAccess world, int i, int j, int k) {
		
		int metadata = world.getBlockMetadata(i,j,k);
		
		int texBottom = this.getBlockTextureFromSideAndMetadata(0, metadata);
		int texTop = this.getBlockTextureFromSideAndMetadata(1, metadata);
		int texSide = this.getBlockTextureFromSideAndMetadata(2, metadata);
		
		// Outer faces
		
		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		
		this.setNoSidesToRender();
		
		if (!world.isBlockOpaqueCube(i, j+1, k)) {
			this.addSideToRender(faceTop);
		}
		if (!world.isBlockOpaqueCube(i-1, j, k)) {
			this.addSideToRender(faceN);
		}
		if (!world.isBlockOpaqueCube(i+1, j, k)) {
			this.addSideToRender(faceS);
		}
		if (!world.isBlockOpaqueCube(i, j, k-1)) {
			this.addSideToRender(faceE);
		}
		if (!world.isBlockOpaqueCube(i, j, k+1)) {
			this.addSideToRender(faceW);
		}
		
		renderer.renderStandardBlock(this, i, j, k);
		
		// Render the bottom if it is visible.
		
		if(!world.isBlockOpaqueCube(i, j-1, k)) {
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, this.__bottomRingWidth, this.__bottomHoleDepth, 1.0D);
			this.setSidesToRender(new int[] {faceBot, faceS});
			renderer.renderStandardBlock(this, i, j, k);
			
			renderer.setRenderBounds(1.0D - this.__bottomRingWidth, 0.0D, 0.0D, 1.0D, this.__bottomHoleDepth, 1.0D);
			this.setSidesToRender(new int[] {faceBot, faceN});
			renderer.renderStandardBlock(this, i, j, k);
			
			renderer.setRenderBounds(this.__bottomRingWidth, 0.0D, 0.0D, 1.0D - this.__bottomRingWidth, this.__bottomHoleDepth, this.__bottomRingWidth);
			this.setSidesToRender(new int[] {faceBot, faceW});
			renderer.renderStandardBlock(this, i, j, k);

			renderer.setRenderBounds(this.__bottomRingWidth, 0.0D, 1.0D - this.__bottomRingWidth, 1.0D - this.__bottomRingWidth, this.__bottomHoleDepth, 1.0D);
			this.setSidesToRender(new int[] {faceBot, faceE});
			renderer.renderStandardBlock(this, i, j, k);
			
			renderer.setRenderBounds(this.__bottomRingWidth, this.__bottomHoleDepth, this.__bottomRingWidth, 1.0D - this.__bottomRingWidth, 1.0D, 1.0D - this.__bottomRingWidth);
			this.setSideToRender(faceBot);
			renderer.renderStandardBlock(this, i, j, k);
		}
		
		// Clean up
		renderer.overrideBlockTexture = -1;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setAllSidesToRender();
		return true;
	}
	
	@Override // XP_I3DBlock
	public void render3DInventoryBlock(RenderBlocks renderer, int metadata) {
		Tessellator tessie = Tessellator.instance;
		
		this.setBlockBoundsForItemRender();
		
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		tessie.startDrawingQuads();
		tessie.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderBottomFace((Block)this, 0.0D, 0.0D, 0.0D, this.getBlockTextureFromSideAndMetadata(0, metadata));
		tessie.draw();
		
		tessie.startDrawingQuads();
		tessie.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderTopFace((Block)this, 0.0D, 0.0D, 0.0D, this.getBlockTextureFromSideAndMetadata(1, metadata));
		tessie.draw();
		
		tessie.startDrawingQuads();
		tessie.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderEastFace((Block)this, 0.0D, 0.0D, 0.0D, this.getBlockTextureFromSideAndMetadata(2, metadata));
		tessie.draw();
		
		tessie.startDrawingQuads();
		tessie.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace((Block)this, 0.0D, 0.0D, 0.0D, this.getBlockTextureFromSideAndMetadata(3, metadata));
		tessie.draw();
		
		tessie.startDrawingQuads();
		tessie.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderNorthFace((Block)this, 0.0D, 0.0D, 0.0D, this.getBlockTextureFromSideAndMetadata(4, metadata));
		tessie.draw();
		
		tessie.startDrawingQuads();
		tessie.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderSouthFace((Block)this, 0.0D, 0.0D, 0.0D, this.getBlockTextureFromSideAndMetadata(5, metadata));
		tessie.draw();
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	@Override // ITextureProvider
	public String getTextureFile() {
		return mod_XPMagnet.magTerrainFilename;
	}
	
	@Override // XP_I3DBlock
	public void setSideToRender(int side) {
		this.__3DRenderState.setSideToRender(side);
	}
	
	@Override // XP_I3DBlock
	public void addSideToRender(int side) {
		this.__3DRenderState.addSideToRender(side);
	}
	
	@Override // XP_I3DBlock
	public void setSidesToRender(int[] sides) {
		this.__3DRenderState.setSidesToRender(sides);
	}
	
	@Override // XP_I3DBlock
	public void setNoSidesToRender() {
		this.__3DRenderState.setNoSidesToRender();
	}
	
	@Override // XP_I3DBlock
	public void setAllSidesToRender() {
		this.__3DRenderState.setAllSidesToRender();
	}
	
	@Override // XP_I3DBlock
	public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side) {
		return this.__3DRenderState.shouldSideBeRendered(world, i, j, k, side);
	}
}
