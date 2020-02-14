package com.extclp.proxy.mixins;

import com.extclp.proxy.api.ProxyClientConnection;
import com.extclp.proxy.api.ProxyHandshakeC2SPacket;
import com.google.gson.Gson;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.network.packet.LoginDisconnectS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import net.minecraft.server.network.packet.HandshakeC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(ServerHandshakeNetworkHandler.class)
public class MixinServerHandshakeNetworkHandler {

    private static final Gson GSON = new Gson();

    @Shadow @Final private ClientConnection client;

    @Inject(method = "onHandshake", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 0,target =
            "Lnet/minecraft/network/ClientConnection;setPacketListener(Lnet/minecraft/network/listener/PacketListener;)V"))
    public void onLoginServer(HandshakeC2SPacket packet, CallbackInfo ci){
        String[] split = ((ProxyHandshakeC2SPacket) packet).getAddress().split("\00");
        if (split.length == 3 || split.length == 4) {
            ((ProxyHandshakeC2SPacket) packet).setAddress(split[0]);
            ((ProxyClientConnection) client).setAddress(new InetSocketAddress(split[1], ((InetSocketAddress) client.getAddress()).getPort()));
            ((ProxyClientConnection) client).setSpoofedUUID(UUIDTypeAdapter.fromString(split[2]));
        } else {
            Text disconnectMessage = new LiteralText("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
            client.send(new LoginDisconnectS2CPacket(disconnectMessage));
            client.disconnect(disconnectMessage);
            return;
        }
        if (split.length == 4) {
            ((ProxyClientConnection) client).setSpoofedProfile(GSON.fromJson(split[3], Property[].class));
        }
    }
}
