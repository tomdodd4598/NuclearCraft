package com.nr.mod.proxy;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;

public abstract class AbstractPacketThreadsafe {

public abstract void handleClientSafe(NetHandlerPlayClient netHandler);

  public abstract void handleServerSafe(NetHandlerPlayServer netHandler);
}