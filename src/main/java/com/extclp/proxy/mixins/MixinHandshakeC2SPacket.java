package com.extclp.proxy.mixins;

import com.extclp.proxy.api.ProxyHandshakeC2SPacket;
import net.minecraft.server.network.packet.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HandshakeC2SPacket.class)
public class MixinHandshakeC2SPacket implements ProxyHandshakeC2SPacket {

    @Shadow private String address;

    @ModifyConstant(method = "read", constant = @Constant(intValue = 255))
    public int changeReadAddressSize(int original){
        return Short.MAX_VALUE;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }
}
