package model;

import java.util.ArrayList;
import java.util.HashMap;

class PortalManager {
    private HashMap<Integer, ArrayList<Portal>> portals = new HashMap<>();

    void addPortal(int key, Portal portal) {
        if (!portals.containsKey(key))
            portals.put(key, new ArrayList<>());

        if (portals.get(key).size() == 2)
            throw new IllegalArgumentException();
        portals.get(key).add(portal);
    }

    void connectPortals() {
        portals.forEach((key, list) -> {
            if (list.size() != 2)
                throw new IllegalStateException();
            Portal.connectPortals(list.get(0), list.get(1));
        });
    }
}
