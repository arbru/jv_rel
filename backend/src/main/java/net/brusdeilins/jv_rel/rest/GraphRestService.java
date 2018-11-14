package net.brusdeilins.jv_rel.rest;

import static net.brusdeilins.jv_rel.rest.Transformer.TO_ENTITY_DTO;
import static net.brusdeilins.jv_rel.rest.Transformer.TO_GRAPH_DTO;
import static net.brusdeilins.jv_rel.rest.Transformer.TO_GRAPH_INFO_DTO;
import static net.brusdeilins.jv_rel.rest.Transformer.TO_QUERY_DAO;
import static net.brusdeilins.jv_rel.rest.Transformer.TO_QUERY_DTO;
import static net.brusdeilins.jv_rel.rest.Transformer.TO_QUERY_INFO_DTO;
import static net.brusdeilins.jv_rel.rest.Transformer.TO_RELATION_DTO;
import static net.brusdeilins.jv_rel.rest.Transformer.TO_RELATION_TYPE_DTO;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.graph.api.GraphViewService;
import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.query.api.QueryService;
import net.brusdeilins.jv_rel.query.api.dao.JvrQueryDao;
import net.brusdeilins.jv_rel.rest.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.rest.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.rest.dto.JvrGraphInfoDto;
import net.brusdeilins.jv_rel.rest.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.rest.dto.JvrQueryExecutionDto;
import net.brusdeilins.jv_rel.rest.dto.JvrQueryInfoDto;
import net.brusdeilins.jv_rel.rest.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.rest.dto.JvrRelationTypeDto;

@RestController
@RequestMapping("/rest")
public class GraphRestService {

    @Autowired
    private GraphDatabase graphDatabase;

    @Autowired
    private GraphViewService graphViewService;

    @Autowired
    private QueryService queryService;

    @RequestMapping(path = "/relation-types", method = RequestMethod.GET)
    public Set<JvrRelationTypeDto> getRelationTypes() {
        return Sets.newHashSet(graphDatabase.getRelationTypes()).stream().map(TO_RELATION_TYPE_DTO)
                .collect(Collectors.toSet());
    }

    @RequestMapping(path = "/entities/toplevel", method = RequestMethod.GET)
    public Set<JvrEntityDto> getToplevel() {
        return graphDatabase.findTopLevelPackages().stream().map(TO_ENTITY_DTO).collect(Collectors.toSet());
    }

    @RequestMapping(path = "/entities/{id}/targets", method = RequestMethod.GET)
    public Set<JvrRelationDto> getTargets(@PathVariable("id") String entityId) {
        return graphDatabase.findTargetRelations(entityId).stream().map(TO_RELATION_DTO).collect(Collectors.toSet());
    }

    @RequestMapping(path = "/graphs", method = RequestMethod.GET)
    public Set<JvrGraphInfoDto> getGraphInfos() {
        Set<JvrGraphDao> graphs = Sets.newHashSet(graphViewService.getGraphs());
        return graphs.stream().map(TO_GRAPH_INFO_DTO).collect(Collectors.toSet());
    }

    @RequestMapping(path = "/queries", method = RequestMethod.GET)
    public Set<JvrQueryInfoDto> getQueryInfos() {
        Set<JvrQueryDao> queries = Sets.newHashSet(queryService.getQueries());
        return queries.stream().map(TO_QUERY_INFO_DTO).collect(Collectors.toSet());
    }

    @RequestMapping(path = "/queries/{id}", method = RequestMethod.GET)
    public JvrQueryDto getQuery(@PathVariable("id") String queryId) {
        JvrQueryDao query = queryService.getQuery(queryId);
        return TO_QUERY_DTO.apply(query);
    }

    @RequestMapping(path = "/graphs/{id}", method = RequestMethod.GET)
    public JvrGraphDto getGraph(@PathVariable("id") String graphId) {
        JvrGraphDao graphDao = graphViewService.getGraph(graphId);
        return TO_GRAPH_DTO.apply(graphDao);
    }

    @RequestMapping(path = "/queries", method = RequestMethod.PUT)
    public void saveQuery(@RequestBody JvrQueryDto query) {
        JvrQueryDao jq = TO_QUERY_DAO.apply(query);
        queryService.saveQuery(jq);
    }

    @RequestMapping(path = "/process-query", method = RequestMethod.POST)
    public JvrGraphDto processQuery(@RequestBody JvrQueryExecutionDto execution) {
        Set<JvrEntityDao> startNodes = Sets.newHashSet(graphDatabase.getEntities(execution.getEntities()));
        JvrGraphDao result = queryService.process(startNodes, execution.getQueryName());
        return TO_GRAPH_DTO.apply(result);
    }

}
