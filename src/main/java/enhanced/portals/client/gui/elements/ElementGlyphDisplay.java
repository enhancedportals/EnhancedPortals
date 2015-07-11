package enhanced.portals.client.gui.elements;

import java.util.List;

import net.minecraft.util.ResourceLocation;
import enhanced.base.client.gui.BaseGui;
import enhanced.base.client.gui.elements.BaseElement;
import enhanced.core.Reference.ECMod;
import enhanced.portals.portal.GlyphIdentifier;

public class ElementGlyphDisplay extends BaseElement {
    GlyphIdentifier id;

    public ElementGlyphDisplay(BaseGui gui, int x, int y, GlyphIdentifier i) {
        super(gui, x, y, 162, 18);
        id = i;
    }

    public void setIdentifier(GlyphIdentifier i) {
        id = i;
    }

    @Override
    public void addTooltip(List<String> list) {

    }

    @Override
    protected void drawContent() {
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ECMod.ID, "textures/gui/player_inventory.png"));
        drawTexturedModalRect(posX, posY, 7, 7, sizeX, sizeY);

        parent.getMinecraft().renderEngine.bindTexture(ElementGlyphSelector.glyphs);

        if (id != null && id.size() > 0)
            for (int i = 0; i < 9; i++) {
                if (i >= id.size())
                    break;

                int glyph = id.get(i), X2 = i % 9 * 18, X = glyph % 9 * 18, Y = glyph / 9 * 18;
                drawTexturedModalRect(posX + X2, posY, X, Y, 18, 18);

            }
    }

    @Override
    public void update() {

    }
}
