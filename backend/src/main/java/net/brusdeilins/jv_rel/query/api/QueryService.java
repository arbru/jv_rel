package net.brusdeilins.jv_rel.query.api;

import java.util.Set;

import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryDto;

public interface QueryService {
    JvrGraphDto process(Set<JvrEntityDto> startNodes, String queryId);

    Iterable<JvrQueryDto> getQueries();

    JvrQueryDto getQuery(String id);

    void saveQuery(JvrQueryDto query);
}
