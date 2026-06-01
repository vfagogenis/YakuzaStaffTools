package oscp.fagogenisv.yakuzastafftools.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import oscp.fagogenisv.yakuzastafftools.cheatsheet.BanCheatsheetData;
import oscp.fagogenisv.yakuzastafftools.cheatsheet.BanRule;

import java.util.List;
import java.util.Locale;

public final class PunishmentGuiScreen extends Screen {
    private static final int BG = 0x99000000;
    private static final int PANEL_BG = 0xBB05080C;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFFAAAAAA;
    private static final int RED = 0xFFFF3333;
    private static final int DARK_RED = 0xFFAA0000;
    private static final int GREEN = 0xFF00FF00;
    private static final int YELLOW = 0xFFFFFF00;
    private static final int ORANGE = 0xFFFFAA00;
    private static final int CYAN = 0xFF55FFFF;

    private final Screen parent;
    private final List<BanRule> rules = BanCheatsheetData.getRules();

    private String target;
    private int selectedRule;
    private int ruleScroll;
    private String selectedLength;
    private PunishmentType selectedType;

    private TextFieldWidget targetField;

    public PunishmentGuiScreen(Screen parent, String target) {
        this(parent, target, 0, 0, "Warn", PunishmentType.WARN);
    }

    private PunishmentGuiScreen(
            Screen parent,
            String target,
            int selectedRule,
            int ruleScroll,
            String selectedLength,
            PunishmentType selectedType
    ) {
        super(Text.literal("Moderation Tools"));
        this.parent = parent;
        this.target = target == null ? "" : target.trim();
        this.selectedRule = selectedRule;
        this.ruleScroll = ruleScroll;
        this.selectedLength = selectedLength;
        this.selectedType = selectedType;
    }

    @Override
    protected void init() {
        int panelW = Math.min(820, this.width - 40);
        int panelH = Math.min(300, this.height - 70);
        int panelX = this.width / 2 - panelW / 2;
        int panelY = 58;

        int x = panelX + 22;
        int contentW = panelW - 44;

        this.targetField = new TextFieldWidget(
                this.textRenderer,
                x,
                panelY + 42,
                Math.min(360, contentW - 240),
                20,
                Text.literal("Target")
        );
        this.targetField.setText(target);
        this.targetField.setMaxLength(16);
        this.addDrawableChild(targetField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> close())
                .dimensions(panelX + panelW - 112, panelY + 12, 90, 20)
                .build());

        addLengthButtons(x, panelY + 94, contentW);
        addTypeButtons(x, panelY + 142);
        addRuleButtons(x, panelY + 204, contentW);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Confirm Punishment"), button -> {
                    if (this.client != null && !rules.isEmpty()) {
                        this.target = currentTarget();

                        this.client.setScreen(new PunishmentConfirmScreen(
                                this,
                                this.target,
                                getSelectedRule(),
                                getOffenseLevelFromLength(),
                                buildCommand()
                        ));
                    }
                })
                .dimensions(panelX + panelW / 2 - 110, panelY + panelH - 26, 220, 20)
                .build());
    }

    private void addLengthButtons(int x, int y, int totalW) {
        String[] lengths = {"Warn", "30m", "6h", "1d", "7d", "15d", "30d", "60d", "perm"};

        int gap = 5;
        int buttonW = Math.max(44, (totalW - gap * (lengths.length - 1)) / lengths.length);

        for (int i = 0; i < lengths.length; i++) {
            String length = lengths[i];
            int bx = x + i * (buttonW + gap);

            this.addDrawableChild(ButtonWidget.builder(Text.literal(length), button -> {
                        selectedLength = length;
                        snapTypeForLength();
                        refresh();
                    })
                    .dimensions(bx, y, buttonW, 20)
                    .build());
        }
    }

    private void addTypeButtons(int x, int y) {
        int w = 115;
        int gap = 10;

        addTypeButton(PunishmentType.WARN, x, y, w);
        addTypeButton(PunishmentType.MUTE, x + (w + gap), y, w);
        addTypeButton(PunishmentType.BAN, x + (w + gap) * 2, y, w);
        addTypeButton(PunishmentType.IP_BAN, x + (w + gap) * 3, y, w);
    }

    private void addTypeButton(PunishmentType type, int x, int y, int w) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal(type.display), button -> {
                    selectedType = type;
                    refresh();
                })
                .dimensions(x, y, w, 20)
                .build());
    }

    private void addRuleButtons(int x, int y, int totalW) {
        int visible = 2;
        int gap = 10;
        int buttonW = (totalW - gap) / 2;

        int maxScroll = Math.max(0, rules.size() - visible);

        if (ruleScroll < 0) {
            ruleScroll = 0;
        }

        if (ruleScroll > maxScroll) {
            ruleScroll = maxScroll;
        }

        for (int i = 0; i < visible; i++) {
            int ruleIndex = ruleScroll + i;

            if (ruleIndex >= rules.size()) {
                break;
            }

            BanRule rule = rules.get(ruleIndex);
            int bx = x + i * (buttonW + gap);

            String prefix = ruleIndex == selectedRule ? "» " : "";

            this.addDrawableChild(ButtonWidget.builder(Text.literal(prefix + trim(rule.rule(), 40)), button -> {
                        selectedRule = ruleIndex;
                        snapSelectionToRule();
                        refresh();
                    })
                    .dimensions(bx, y, buttonW, 20)
                    .build());
        }
    }

    private void refresh() {
        if (this.client != null) {
            this.client.setScreen(new PunishmentGuiScreen(
                    parent,
                    currentTarget(),
                    selectedRule,
                    ruleScroll,
                    selectedLength,
                    selectedType
            ));
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int visible = 2;
        int maxScroll = Math.max(0, rules.size() - visible);

        if (verticalAmount > 0) {
            ruleScroll--;
        } else if (verticalAmount < 0) {
            ruleScroll++;
        }

        if (ruleScroll < 0) {
            ruleScroll = 0;
        }

        if (ruleScroll > maxScroll) {
            ruleScroll = maxScroll;
        }

        refresh();
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, BG);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Moderation Tools • WARN / MUTE / BAN"),
                this.width / 2,
                14,
                WHITE
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Pick a player, select punishment path, choose a rule, then confirm."),
                this.width / 2,
                28,
                MUTED
        );

        int panelW = Math.min(820, this.width - 40);
        int panelH = Math.min(300, this.height - 70);
        int panelX = this.width / 2 - panelW / 2;
        int panelY = 58;

        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, PANEL_BG);
        drawBorder(context, panelX, panelY, panelW, panelH, 0x88334455);

        drawContent(context, panelX + 22, panelY + 20, panelW - 44);

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawContent(DrawContext context, int x, int y, int w) {
        BanRule rule = getSelectedRule();

        draw(context, "Target Player", x, y, YELLOW);
        draw(context, "Current: " + currentTarget(), x + 395, y + 26, GREEN);

        draw(context, "Length", x, y + 66, YELLOW);
        draw(context, "Selected: " + selectedLength, x + 85, y + 66, getLengthColor(selectedLength));

        draw(context, "Punishment Type", x, y + 114, YELLOW);
        draw(context, "Selected: " + selectedType.display, x + 150, y + 114, selectedType.color);

        draw(context, "Rule Broken", x, y + 170, YELLOW);
        draw(context, "Selected: " + rule.rule(), x + 120, y + 170, getRuleColor(rule));
        draw(context, "Category: " + rule.category(), x, y + 186, MUTED);
        draw(context, "Mouse wheel scrolls rules.", x + 455, y + 186, MUTED);

        int previewY = y + 222;

        draw(context, "Command Preview", x, previewY, CYAN);
        drawBox(context, "/" + buildCommand(), x, previewY + 14, Math.min(700, w), 22, ORANGE);

        if (selectedType == PunishmentType.IP_BAN) {
            draw(context, "WARNING: DO NOT IP BAN BEDROCK PLAYERS.", x, previewY + 40, RED);
        }
    }

    private void snapSelectionToRule() {
        BanRule rule = getSelectedRule();
        String first = rule.first();

        if (first == null || first.isBlank()) {
            return;
        }

        selectedLength = normalizeLength(first);

        String category = rule.category().toLowerCase(Locale.ROOT);
        String lowerFirst = first.toLowerCase(Locale.ROOT);

        if (category.contains("chat") || lowerFirst.contains("mute")) {
            selectedType = PunishmentType.MUTE;
        } else if (lowerFirst.contains("warn")) {
            selectedType = PunishmentType.WARN;
        } else if (lowerFirst.contains("ip")) {
            selectedType = PunishmentType.IP_BAN;
        } else {
            selectedType = PunishmentType.BAN;
        }
    }

    private void snapTypeForLength() {
        if (selectedLength.equalsIgnoreCase("Warn")) {
            selectedType = PunishmentType.WARN;
        } else if (selectedType == PunishmentType.WARN) {
            selectedType = PunishmentType.BAN;
        }
    }

    private BanRule getSelectedRule() {
        if (rules.isEmpty()) {
            return new BanRule("Unknown", "Unknown", "Unknown", "Warn", "", "", "", "", "");
        }

        if (selectedRule < 0) {
            selectedRule = 0;
        }

        if (selectedRule >= rules.size()) {
            selectedRule = rules.size() - 1;
        }

        return rules.get(selectedRule);
    }

    private String buildCommand() {
        String targetName = currentTarget();
        String reason = sanitizeReason(getSelectedRule().rule());

        return switch (selectedType) {
            case WARN -> "warn " + targetName + " " + reason;

            case MUTE -> {
                if (selectedLength.equalsIgnoreCase("perm")) {
                    yield "mute " + targetName + " " + reason;
                }

                if (selectedLength.equalsIgnoreCase("Warn")) {
                    yield "warn " + targetName + " " + reason;
                }

                yield "tempmute " + targetName + " " + selectedLength + " " + reason;
            }

            case BAN -> {
                if (selectedLength.equalsIgnoreCase("perm")) {
                    yield "ban " + targetName + " " + reason;
                }

                if (selectedLength.equalsIgnoreCase("Warn")) {
                    yield "warn " + targetName + " " + reason;
                }

                yield "tempban " + targetName + " " + selectedLength + " " + reason;
            }

            case IP_BAN -> {
                if (selectedLength.equalsIgnoreCase("perm")) {
                    yield "banip " + targetName + " " + reason;
                }

                if (selectedLength.equalsIgnoreCase("Warn")) {
                    yield "warn " + targetName + " " + reason;
                }

                yield "tempbanip " + targetName + " " + selectedLength + " " + reason;
            }
        };
    }

    private int getOffenseLevelFromLength() {
        return switch (selectedLength.toLowerCase(Locale.ROOT)) {
            case "warn" -> 1;
            case "30m", "6h", "1d" -> 2;
            case "7d", "15d" -> 3;
            case "30d", "60d" -> 4;
            case "perm" -> 5;
            default -> 1;
        };
    }

    private String normalizeLength(String value) {
        String lower = value.toLowerCase(Locale.ROOT);

        if (lower.contains("warn")) return "Warn";
        if (lower.contains("30 min")) return "30m";
        if (lower.contains("6 hour") || lower.contains("6h")) return "6h";
        if (lower.contains("1 day")) return "1d";
        if (lower.contains("7 day")) return "7d";
        if (lower.contains("15 day")) return "15d";
        if (lower.contains("30 day")) return "30d";
        if (lower.contains("60 day")) return "60d";
        if (lower.contains("permanent")) return "perm";

        return "Warn";
    }

    private String currentTarget() {
        if (targetField != null) {
            String value = targetField.getText();

            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return target == null || target.isBlank() ? "Unknown" : target.trim();
    }

    private int getRuleColor(BanRule rule) {
        if (rule.category().toLowerCase(Locale.ROOT).contains("chat")) {
            return CYAN;
        }

        return RED;
    }

    private int getLengthColor(String length) {
        return switch (length.toLowerCase(Locale.ROOT)) {
            case "warn" -> YELLOW;
            case "30m", "6h", "1d" -> ORANGE;
            case "7d", "15d" -> 0xFFFF8800;
            case "30d", "60d" -> RED;
            case "perm" -> 0xFF555555;
            default -> WHITE;
        };
    }

    private void drawBox(DrawContext context, String text, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + h, 0xDD000000);
        drawBorder(context, x, y, w, h, color);
        draw(context, text, x + 6, y + 6, color);
    }

    private void draw(DrawContext context, String text, int x, int y, int color) {
        if (text == null || text.isBlank()) {
            return;
        }

        context.drawText(this.textRenderer, Text.literal(text), x, y, color, true);
    }

    private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + 1, color);
        context.fill(x, y + height - 1, x + width, y + height, color);
        context.fill(x, y, x + 1, y + height, color);
        context.fill(x + width - 1, y, x + width, y + height, color);
    }

    private String sanitizeReason(String reason) {
        return reason.replace("/", "").replace("\n", " ").replace("\r", " ").trim();
    }

    private String trim(String value, int max) {
        if (value == null) {
            return "";
        }

        if (value.length() <= max) {
            return value;
        }

        return value.substring(0, Math.max(0, max - 3)) + "...";
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    private enum PunishmentType {
        WARN("Warn", YELLOW),
        MUTE("Mute", ORANGE),
        BAN("Ban", RED),
        IP_BAN("IP-Ban", DARK_RED);

        private final String display;
        private final int color;

        PunishmentType(String display, int color) {
            this.display = display;
            this.color = color;
        }
    }
}