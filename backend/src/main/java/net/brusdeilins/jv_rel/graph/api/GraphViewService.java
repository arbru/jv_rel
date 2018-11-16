package net.brusdeilins.jv_rel.graph.api;

import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;

public interface GraphViewService {
    Iterable<JvrGraphDto> getGraphs();

    JvrGraphDto getGraph(String id);
    
    void saveGraph(JvrGraphDto graph);
}
