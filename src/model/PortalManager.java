package model;

import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;

class PortalManager {
    private HashMap<Integer, ArrayList<Portal>> portals = new HashMap<>();

    void addPortal(Portal portal) {
        val id = portal.getId();
        if (!portals.containsKey(id))
            portals.put(id, new ArrayList<>());

        if (portals.get(id).size() == 2)
            throw new IllegalArgumentException();
        portals.get(id).add(portal);
    }

    void connectPortals() {
        portals.forEach((key, list) -> {
            if (list.size() != 2)
                throw new IllegalStateException();
            Portal.connectPortals(list.get(0), list.get(1));
        });
    }
}
