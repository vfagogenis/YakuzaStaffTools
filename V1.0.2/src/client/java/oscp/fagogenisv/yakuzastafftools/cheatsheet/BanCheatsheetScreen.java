package oscp.fagogenisv.yakuzastafftools.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import oscp.fagogenisv.yakuzastafftools.cheatsheet.BanCheatsheetData;
import oscp.fagogenisv.yakuzastafftools.cheatsheet.BanRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BanCheatsheetScreen extends Screen {
    private static final int BG = 0x99000000;
    private static final int PANEL_BG = 0x77000000;
    private static final int HEADER_LINE = 0x88FFFFFF;

    private static final int RED = 0xFFFF2222;
    private static final int DARK_RED = 0xFFAA0000;
    private static final int GREEN = 0xFF00FF00;
    private static final int YELLOW = 0xFFFFFF00;
    private static final int ORANGE = 0xFFFFAA00;
    private static final int CYAN = 0xFF00AAAA;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MAGENTA = 0xFFFF00FF;
    private static final int MUTED = 0xFFAAAAAA;

    private static final int ROW_HEIGHT = 26;

    private final Screen parent;
    private final List<BanRule> allRules = BanCheatsheetData.getRules();

    private TextFieldWidget searchBox;
    private int scroll = 0;
    private String lastSearch = "";

    public BanCheatsheetScreen(Screen parent) {
        super(Text.literal("Punishment Cheatsheet"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        this.searchBox = new TextFieldWidget(
                this.textRenderer,
                centerX - 190,
                44,
                380,
                20,
                Text.literal("Search")
        );

        this.searchBox.setMaxLength(64);
        this.searchBox.setPlaceholder(Text.literal("Search rule, category, punish..."));
        this.searchBox.setText("");
        this.addDrawableChild(this.searchBox);
        this.setInitialFocus(this.searchBox);

        addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> close())
                .dimensions(centerX - 75, this.height - 32, 150, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, BG);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Punishment Cheatsheet"),
                this.width / 2,
                16,
                WHITE
        );

        String currentSearch = searchBox == null ? "" : searchBox.getText();

        if (!currentSearch.equals(lastSearch)) {
            scroll = 0;
            lastSearch = currentSearch;
        }

        drawSearchLabel(context);
        drawTable(context);

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawSearchLabel(DrawContext context) {
        if (searchBox == null) {
            return;
        }

        context.drawText(
                this.textRenderer,
                Text.literal("Search"),
                searchBox.getX(),
                searchBox.getY() - 12,
                MUTED,
                true
        );
    }

    private void drawTable(DrawContext context) {
        List<BanRule> rules = getFilteredRules();

        int tableX = 0;
        int tableY = 78;
        int tableW = this.width;
        int tableH = this.height - 116;

        context.fill(tableX, tableY, tableX + tableW, tableY + tableH, PANEL_BG);
        context.fill(tableX, tableY, tableX + tableW, tableY + 1, HEADER_LINE);
        context.fill(tableX, tableY + tableH, tableX + tableW, tableY + tableH + 1, HEADER_LINE);

        int ruleX = 28;
        int p1X = this.width / 3;
        int p2X = this.width / 2;
        int p3X = (int) (this.width * 0.68);
        int p4X = (int) (this.width * 0.85);

        int headerY = tableY + 10;

        drawCentered(context, "NOTE: DO NOT IP BAN BEDROCK PLAYERS!", this.width / 2, headerY, MAGENTA);
        headerY += 28;

        draw(context, "Rule", ruleX, headerY, WHITE);
        draw(context, "#1 Punish", p1X, headerY, GREEN);
        draw(context, "#2 Punish", p2X, headerY, YELLOW);
        draw(context, "#3 Punish", p3X, headerY, ORANGE);
        draw(context, "#4 Punish", p4X, headerY, RED);

        int listStartY = headerY + 24;
        int visibleRows = Math.max(1, (tableY + tableH - listStartY - 6) / ROW_HEIGHT);

        int maxScroll = Math.max(0, rules.size() - visibleRows);

        if (scroll > maxScroll) {
            scroll = maxScroll;
        }

        if (scroll < 0) {
            scroll = 0;
        }

        if (rules.isEmpty()) {
            drawCentered(context, "No matching rules found.", this.width / 2, listStartY + 40, MUTED);
            return;
        }

        int y = listStartY;
        String currentCategory = "";

        for (int i = scroll; i < rules.size(); i++) {
            if (y > tableY + tableH - ROW_HEIGHT) {
                break;
            }

            BanRule rule = rules.get(i);

            if (!rule.category().equals(currentCategory)) {
                currentCategory = rule.category();

                int categoryColor = currentCategory.toLowerCase(Locale.ROOT).contains("chat") ? CYAN : DARK_RED;

                draw(context, currentCategory, ruleX, y, categoryColor);
                y += ROW_HEIGHT;

                if (y > tableY + tableH - ROW_HEIGHT) {
                    break;
                }
            }

            drawTrimmed(context, rule.rule(), ruleX, y, p1X - ruleX - 16, categoryColorForRule(rule));
            draw(context, rule.first(), p1X, y, GREEN);
            draw(context, rule.second(), p2X, y, YELLOW);
            draw(context, rule.third(), p3X, y, ORANGE);
            draw(context, rule.fourth(), p4X, y, RED);

            y += ROW_HEIGHT;
        }

        drawScrollbar(context, tableY, tableH, visibleRows, rules.size());
    }

    private List<BanRule> getFilteredRules() {
        String query = searchBox == null ? "" : searchBox.getText().trim().toLowerCase(Locale.ROOT);

        if (query.isBlank()) {
            return allRules;
        }

        List<BanRule> filtered = new ArrayList<>();

        for (BanRule rule : allRules) {
            String searchable = (
                    rule.category() + " " +
                            rule.rule() + " " +
                            rule.first() + " " +
                            rule.second() + " " +
                            rule.third() + " " +
                            rule.fourth() + " " +
                            rule.fifth() + " " +
                            rule.note()
            ).toLowerCase(Locale.ROOT);

            if (searchable.contains(query)) {
                filtered.add(rule);
            }
        }

        return filtered;
    }

    private int categoryColorForRule(BanRule rule) {
        if (rule.category().toLowerCase(Locale.ROOT).contains("chat")) {
            return CYAN;
        }

        return DARK_RED;
    }

    private void drawScrollbar(DrawContext context, int tableY, int tableH, int visibleRows, int totalRows) {
        if (totalRows <= visibleRows) {
            return;
        }

        int barX = this.width - 8;
        int barY = tableY + 2;
        int barH = tableH - 4;

        context.fill(barX, barY, barX + 5, barY + barH, 0x66000000);

        int maxScroll = Math.max(1, totalRows - visibleRows);
        int thumbH = Math.max(30, barH * visibleRows / totalRows);
        int thumbY = barY + (barH - thumbH) * scroll / maxScroll;

        context.fill(barX, thumbY, barX + 5, thumbY + thumbH, 0xFFCCCCCC);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        List<BanRule> rules = getFilteredRules();

        int tableY = 78;
        int tableH = this.height - 116;
        int listStartY = tableY + 10 + 28 + 24;
        int visibleRows = Math.max(1, (tableY + tableH - listStartY - 6) / ROW_HEIGHT);

        int maxScroll = Math.max(0, rules.size() - visibleRows);

        if (verticalAmount > 0) {
            scroll -= 3;
        } else if (verticalAmount < 0) {
            scroll += 3;
        }

        if (scroll < 0) {
            scroll = 0;
        }

        if (scroll > maxScroll) {
            scroll = maxScroll;
        }

        return true;
    }

    private void draw(DrawContext context, String text, int x, int y, int color) {
        if (text == null || text.isBlank()) {
            return;
        }

        context.drawText(this.textRenderer, Text.literal(text), x, y, color, true);
    }

    private void drawCentered(DrawContext context, String text, int x, int y, int color) {
        if (text == null || text.isBlank()) {
            return;
        }

        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(text), x, y, color);
    }

    private void drawTrimmed(DrawContext context, String text, int x, int y, int maxWidth, int color) {
        if (text == null || text.isBlank()) {
            return;
        }

        String output = text;

        while (this.textRenderer.getWidth(output) > maxWidth && output.length() > 3) {
            output = output.substring(0, Math.max(0, output.length() - 4)) + "...";
        }

        draw(context, output, x, y, color);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}