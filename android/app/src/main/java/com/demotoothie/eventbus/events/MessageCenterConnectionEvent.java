package com.example.sdkpoc.buildwin.common.eventbus.events;

public final class MessageCenterConnectionEvent {
    public boolean connected;

    public MessageCenterConnectionEvent(boolean connected) {
        this.connected = connected;
    }
}
