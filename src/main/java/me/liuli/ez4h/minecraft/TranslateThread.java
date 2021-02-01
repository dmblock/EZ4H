package me.liuli.ez4h.minecraft;

import com.github.steveice10.packetlib.packet.Packet;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import me.liuli.ez4h.EZ4H;

public class TranslateThread implements Runnable {
    private final Client client;

    public TranslateThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        long time = 0;
        while (client.isAlive()) {
            try {
                if (time < 50) {
                    Thread.sleep(50 - time);
                }
                time = System.currentTimeMillis();

                BedrockPacket[] bedrockPackets = client.getPacketStorage().getBedrockPackets();
                Packet[] javaPackets = client.getPacketStorage().getJavaPackets();
                if (EZ4H.getDebugManager().isOther()) {
                    EZ4H.getLogger().debug("Translating " + bedrockPackets.length + " bedrock, " + javaPackets.length + " java packets");
                }
                for (BedrockPacket bedrockPacket : bedrockPackets) {
                    EZ4H.getTranslatorManager().translatePacket(bedrockPacket, client);
                }
                for (Packet javaPacket : javaPackets) {
                    EZ4H.getTranslatorManager().translatePacket(javaPacket, client);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis() - time;
        }
    }
}