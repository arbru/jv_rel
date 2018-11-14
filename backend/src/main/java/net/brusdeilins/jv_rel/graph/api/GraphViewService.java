package net.brusdeilins.jv_rel.graph.api;

import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;

public interface GraphViewService {
    Iterable<JvrGraphDao> getGraphs();

    JvrGraphDao getGraph(String id);
}
