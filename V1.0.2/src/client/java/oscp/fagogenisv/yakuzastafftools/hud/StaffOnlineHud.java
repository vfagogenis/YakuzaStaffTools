package oscp.fagogenisv.yakuzastafftools.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.staff.StaffMember;

import java.util.List;

public final class StaffOnlineHud {
    private static final int TITLE_COLOR = 0xFF55FFFF;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int BG = 0x90000000;
    private static final int LINE_HEIGHT = 10;

    public void render(DrawContext context) {
        if (!YakuzaStaffToolsClient.CONFIG.enableMod || !YakuzaStaffToolsClient.CONFIG.enableStaffHud) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;

        List<StaffMember> staff = YakuzaStaffToolsClient.STAFF_TRACKER.getOnlineStaffMembers();

        int width = 165;
        int visibleStaff = Math.min(8, staff.size());
        int height = 18 + Math.max(1, visibleStaff) * LINE_HEIGHT;

        int x = YakuzaStaffToolsClient.CONFIG.staffHudX;
        int y = YakuzaStaffToolsClient.CONFIG.staffHudY;

        context.fill(x - 4, y - 4, x + width, y + height, BG);
        context.drawText(renderer, "Staff Online (" + staff.size() + ")", x, y, TITLE_COLOR, true);

        if (staff.isEmpty()) {
            context.drawText(renderer, "- none", x, y + 14, TEXT_COLOR, true);
            return;
        }

        int lineY = y + 14;

        for (int i = 0; i < visibleStaff; i++) {
            StaffMember member = staff.get(i);

            String line = "[" + member.rank() + "] " + member.username();

            if (renderer.getWidth(line) > width - 8) {
                line = member.username();
            }

            context.drawText(renderer, line, x, lineY, TEXT_COLOR, true);
            lineY += LINE_HEIGHT;
        }
    }
}