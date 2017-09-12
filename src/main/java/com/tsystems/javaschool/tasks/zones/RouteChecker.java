package com.tsystems.javaschool.tasks.zones;

import java.util.*;

public class RouteChecker {
    /**
     * Checks whether required zones are connected with each other.
     * By connected we mean that there is a path from any zone to any zone from the requested list.
     *
     * Each zone from initial state may contain a list of it's neighbours. The link is defined as unidirectional,
     * but can be used as bidirectional.
     * For instance, if zone A is connected with B either:
     *  - A has link to B
     *  - OR B has a link to A
     *  - OR both of them have a link to each other
     *
     * @param zoneState current list of all available zones
     * @param requestedZoneIds zone IDs from request
     * @return true of zones are connected, false otherwise
     */
    public boolean checkRoute(List<Zone> zoneState, List<Integer> requestedZoneIds){
        boolean result = false;

        Map<Integer, List<Integer>> zones = new HashMap<>();
        for(Zone z: zoneState){
            zones.put(z.getId(), new ArrayList<>());
        }

        for(Map.Entry<Integer, List<Integer>> entry: zones.entrySet()){
            zoneState.stream().filter(z -> z.getId() == entry.getKey()).forEach(z -> {
                for (Integer item : z.getNeighbours()) {
                    entry.getValue().add(item);
                }
            });
        }

        for(Integer item: requestedZoneIds){
            zones.entrySet().stream().filter(entry -> entry.getValue().contains(item)).forEach(entry -> {
                for (Map.Entry<Integer, List<Integer>> entry1 : zones.entrySet()) {
                    if (item == entry1.getKey() && !entry1.getValue().contains(entry.getKey())) {
                        entry1.getValue().add(entry.getKey());
                    }
                }
            });
        }

        for(Integer item: requestedZoneIds){
            List<Integer> route = getRoute(item, zones, new ArrayList<>(), requestedZoneIds);
            if(route.containsAll(requestedZoneIds)){
                result = true;
                break;
            }
        }

        return result;
    }

    private static List<Integer> getRoute(Integer item,
                                          Map<Integer,
                                          List<Integer>> zones,
                                          List<Integer> route,
                                          List<Integer> requestedZoneIds
    ) {

        for(Map.Entry<Integer, List<Integer>> entry: zones.entrySet()){
            if(entry.getValue().contains(item) && requestedZoneIds.contains(item)){
                if(!route.contains(item)) {
                    route.add(item);
                    item = entry.getKey();
                    getRoute(item, zones, route, requestedZoneIds);
                } else if(!route.contains(entry.getKey())){
                    item = entry.getKey();
                    getRoute(item, zones, route, requestedZoneIds);
                }
            }
        }
        return route;
    }
}
