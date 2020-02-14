package com.extclp.proxy.api;

import com.mojang.authlib.properties.Property;

import java.net.SocketAddress;
import java.util.UUID;

public interface ProxyClientConnection {

    void setAddress(SocketAddress socketAddress);

    void setSpoofedUUID(UUID uuid);

    void setSpoofedProfile(Property[] profile);

    UUID getSpoofedUUID();

    Property[] getSpoofedProfile();
}
