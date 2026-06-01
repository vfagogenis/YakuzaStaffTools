package oscp.fagogenisv.yakuzastafftools.chat;

import net.minecraft.text.Text;

public final class CapturedChatMessage {
    private final Text message;
    private final ChatTab type;

    public CapturedChatMessage(Text message, ChatTab type) {
        this.message = message.copy();
        this.type = type;
    }

    public Text message() {
        return message;
    }

    public ChatTab type() {
        return type;
    }
}