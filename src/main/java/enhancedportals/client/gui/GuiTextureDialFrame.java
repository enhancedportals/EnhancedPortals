package enhancedportals.client.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.tileentity.portal.TileDiallingDevice;

public class GuiTextureDialFrame extends GuiTextureFrame
{
    TileDiallingDevice dial;
    boolean didSave, returnToEdit;

    public GuiTextureDialFrame(TileDiallingDevice d, EntityPlayer p)
    {
        this(d, p, false);
    }
    
    public GuiTextureDialFrame(TileDiallingDevice d, EntityPlayer p, boolean r)
    {
        super(d.getPortalController(), p);
        dial = d;
        returnToEdit = r;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(1000, guiLeft + 7, guiTop + ySize + 1, xSize - 14, 20, "Save"));

        Color c = new Color(ClientProxy.saveTexture.getFrameColour());
        sliderR.sliderValue = c.getRed() / 255f;
        sliderG.sliderValue = c.getGreen() / 255f;
        sliderB.sliderValue = c.getBlue() / 255f;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonSave.id)
        {
            ClientProxy.saveTexture.setFrameColour(Integer.parseInt(String.format("%02x%02x%02x", sliderR.getValue(), sliderG.getValue(), sliderB.getValue()), 16));
        }
        else if (button.id == buttonReset.id)
        {
            int colour = 0xffffff;
            ClientProxy.saveTexture.setFrameColour(colour);

            Color c = new Color(colour);
            sliderR.sliderValue = c.getRed() / 255f;
            sliderG.sliderValue = c.getGreen() / 255f;
            sliderB.sliderValue = c.getBlue() / 255f;
        }
        else if (button.id == 1000)
        {
            didSave = true;
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(dial, returnToEdit ? GuiHandler.DIALLING_DEVICE_D : GuiHandler.DIALLING_DEVICE_C));
        }
    }
    
    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        
        if (!didSave)
        {
            ClientProxy.saveGlyph = null;
            ClientProxy.saveName = null;
            ClientProxy.saveTexture = null;
        }
    }
}
