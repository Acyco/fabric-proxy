package com.extclp.proxy.mixins;

import com.extclp.proxy.api.ProxyClientConnection;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;
import java.util.UUID;

@Mixin(ClientConnection.class)
public class MixinClientConnection implements ProxyClientConnection {

    private UUID spoofedUUID;
    private Property[] spoofedProfile;

    @Shadow
    private SocketAddress address;

    @Override
    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    @Override
    public void setSpoofedProfile(Property[] spoofedProfile) {
        this.spoofedProfile = spoofedProfile;
    }

    @Override
    public void setSpoofedUUID(UUID spoofedUUID) {
        this.spoofedUUID = spoofedUUID;
    }

    @Override
    public UUID getSpoofedUUID() {
        return spoofedUUID;
    }

    @Override
    public Property[] getSpoofedProfile() {
        return spoofedProfile;
    }
}
