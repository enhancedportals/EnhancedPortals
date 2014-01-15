package uk.co.shadeddimensions.ep3.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.library.ct.ConnectedTextures;
import uk.co.shadeddimensions.library.ct.ConnectedTexturesDetailed;
import cofh.api.block.IDismantleable;
import cofh.api.tileentity.ISidedBlockTexture;

public class BlockFrame extends BlockEnhancedPortals implements IDismantleable
{
    public static int PORTAL_CONTROLLER = 1, REDSTONE_INTERFACE = 2, NETWORK_INTERFACE = 3, DIALLING_DEVICE = 4, BIOMETRIC_IDENTIFIER = 5, MODULE_MANIPULATOR = 6, FRAME_TYPES = 7;

    public static Icon[] overlayIcons;
    public static ConnectedTextures connectedTextures;

    public BlockFrame(int id, String name)
    {
        super(id, Material.rock, true);
        setHardness(5);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);
        connectedTextures = new ConnectedTexturesDetailed("enhancedportals:frame/%s", id, -1);
    }

    @Override
    public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        BlockEnhancedPortals.renderPass = pass;
        return pass < 2;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (metadata == 0)
        {
            return new TileFrame();
        }
        else if (metadata == PORTAL_CONTROLLER)
        {
            return new TilePortalController();
        }
        else if (metadata == REDSTONE_INTERFACE)
        {
            return new TileRedstoneInterface();
        }
        else if (metadata == NETWORK_INTERFACE)
        {
            return new TileNetworkInterface();
        }
        else if (metadata == DIALLING_DEVICE)
        {
            return new TileDiallingDevice();
        }
        else if (metadata == BIOMETRIC_IDENTIFIER)
        {
            return new TileBiometricIdentifier();
        }
        else if (metadata == MODULE_MANIPULATOR)
        {
            return new TileModuleManipulator();
        }

        return null;
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock)
    {
        ItemStack dropBlock = new ItemStack(blockID, 1, world.getBlockMetadata(x, y, z));
        
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if (tile instanceof TilePortalFrame)
        {
            ((TilePortalFrame) tile).blockDismantled();
        }
        
        world.setBlockToAir(x, y, z);
        
        if (dropBlock != null && !returnBlock)
        {
            float f = 0.3F;
            double x2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double y2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double z2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            EntityItem item = new EntityItem(world, x + x2, y + y2, z + z2, dropBlock);
            item.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(item);
        }
        
        return dropBlock;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((ISidedBlockTexture) blockAccess.getBlockTileEntity(x, y, z)).getBlockTexture(side, BlockEnhancedPortals.renderPass);
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        return connectedTextures.getBaseIcon();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(CommonProxy.blockFrame.blockID, 1, world.getBlockMetadata(x, y, z));
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < FRAME_TYPES; i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public boolean isBlockNormalCube(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        overlayIcons = new Icon[FRAME_TYPES];

        for (int i = 0; i < overlayIcons.length; i++)
        {
            overlayIcons[i] = register.registerIcon("enhancedportals:portalFrame_" + i);
        }

        connectedTextures.registerIcons(register);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int par2, int par3, int par4, int par5)
    {
        return blockAccess.getBlockId(par2, par3, par4) == blockID ? false : super.shouldSideBeRendered(blockAccess, par2, par3, par4, par5);
    }
}
