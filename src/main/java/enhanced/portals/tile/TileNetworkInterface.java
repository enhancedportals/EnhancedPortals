package enhanced.portals.tile;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import enhanced.base.utilities.Localisation;
import enhanced.base.xmod.ComputerCraft;
import enhanced.base.xmod.OpenComputers;
import enhanced.portals.EnhancedPortals;
import enhanced.portals.network.GuiHandler;
import enhanced.portals.utility.GeneralUtils;
import enhanced.portals.utility.Reference.EPGuis;
import enhanced.portals.utility.Reference.EPItems;
import enhanced.portals.utility.Reference.EPMod;

@InterfaceList(value = { @Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = ComputerCraft.MOD_ID), @Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = OpenComputers.MOD_ID) })
public class TileNetworkInterface extends TileFrame implements IPeripheral, SimpleComponent {
    @Override
    public boolean activate(EntityPlayer player, ItemStack stack) {
        if (player.isSneaking())
            return false;

        TileController controller = getPortalController();
        
        if (stack != null && controller != null && controller.isFinalized)
            if (GeneralUtils.isWrench(stack) && !player.isSneaking()) {
                if (!EnhancedPortals.proxy.networkManager.hasUID(controller)) {
                    if (!worldObj.isRemote)
                        player.addChatComponentMessage(new ChatComponentText(Localisation.getChatError(EPMod.ID, "noUidSet")));
                } else
                    GuiHandler.openGui(player, controller, EPGuis.NETWORK_INTERFACE_A);
            } else if (stack.getItem() == EPItems.nanobrush) {
                GuiHandler.openGui(player, controller, EPGuis.TEXTURE_A);
                return true;
            }

        return false;
    }

    @Override
    public void addDataToPacket(NBTTagCompound tag) {

    }

    @Override
    @Method(modid = ComputerCraft.MOD_ID)
    public void attach(IComputerAccess computer) {

    }

    @Override
    @Method(modid = ComputerCraft.MOD_ID)
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        if (method == 0)
            getPortalController().constructConnection();
        else if (method == 1)
            getPortalController().deconstructConnection();

        return null;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    @Method(modid = ComputerCraft.MOD_ID)
    public void detach(IComputerAccess computer) {

    }

    @Callback(doc = "function():boolean -- Attempts to create a connection to the next portal in the network.")
    @Method(modid = OpenComputers.MOD_ID)
    public Object[] dial(Context context, Arguments args) {
        getPortalController().constructConnection();
        return new Object[] { true };
    }

    @Override
    @Method(modid = ComputerCraft.MOD_ID)
    public boolean equals(IPeripheral other) {
        return other == this;
    }

    @Override
    @Method(modid = OpenComputers.MOD_ID)
    public String getComponentName() {
        return "ep_interface_network";
    }

    @Override
    @Method(modid = ComputerCraft.MOD_ID)
    public String[] getMethodNames() {
        return new String[] { "dial", "terminate" };
    }

    @Override
    @Method(modid = ComputerCraft.MOD_ID)
    public String getType() {
        return "ep_interface_network";
    }

    @Override
    public void onDataPacket(NBTTagCompound tag) {

    }

    @Callback(doc = "function():boolean -- Terminates any active connection.")
    @Method(modid = OpenComputers.MOD_ID)
    public Object[] terminate(Context context, Arguments args) {
        getPortalController().deconstructConnection();
        return new Object[] { true };
    }
}