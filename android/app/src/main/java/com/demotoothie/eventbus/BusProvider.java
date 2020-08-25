package com.demotoothie.eventbus;

import com.squareup.otto.Bus;

public final class BusProvider {

    public static Bus BUS = new Bus();

    public static Bus getBus() {
        return BUS;
    }

    private BusProvider() {
    }

}
