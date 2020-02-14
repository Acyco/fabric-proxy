package com.extclp.proxy.mixins;

import com.extclp.proxy.api.ProxyClientConnection;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(ServerLoginNetworkHandler.class)
public class MixinServerLoginNetworkHandler {

    @Shadow @Final public ClientConnection client;

    @Shadow private GameProfile profile;

    @Redirect(method = "acceptPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerLoginNetworkHandler;toOfflineProfile(Lcom/mojang/authlib/GameProfile;)Lcom/mojang/authlib/GameProfile;"))
    public GameProfile onHello(ServerLoginNetworkHandler serverLoginNetworkHandler, GameProfile gameProfile){
        UUID uuid = ((ProxyClientConnection) client).getSpoofedUUID();
        if (uuid == null) {
            uuid = PlayerEntity.getOfflinePlayerUuid(this.profile.getName());
        }
        GameProfile profile = new GameProfile( uuid, this.profile.getName());

        if (((ProxyClientConnection) client).getSpoofedProfile() != null) {
            for (com.mojang.authlib.properties.Property property : ((ProxyClientConnection) client).getSpoofedProfile()) {
                profile.getProperties().put(property.getName(), property);
            }
        }
        return profile;
    }
}
