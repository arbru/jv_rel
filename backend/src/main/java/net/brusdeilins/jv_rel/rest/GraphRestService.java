package net.brusdeilins.jv_rel.rest;

import static java.util.stream.Collectors.toSet;
import static net.brusdeilins.jv_rel.rest.Transformer.GRAPH_TO_GRAPH_INFO_DTO;
import static net.brusdeilins.jv_rel.rest.Transformer.QUERY_TO_QUERY_INFO_DTO;

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
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationTypeDto;
import net.brusdeilins.jv_rel.graph.api.GraphViewService;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphInfoDto;
import net.brusdeilins.jv_rel.query.api.QueryService;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryExecutionDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryInfoDto;


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
        return Sets.newHashSet(graphDatabase.getRelationTypes());
    }

    @RequestMapping(path = "/entities/toplevel", method = RequestMethod.GET)
    public Set<JvrEntityDto> getToplevel() {
        return graphDatabase.findTopLevelPackages();
    }

    @RequestMapping(path = "/entities/{id}/targets", method = RequestMethod.GET)
    public Set<JvrRelationDto> getTargets(@PathVariable("id") String entityId) {
        return graphDatabase.findTargetRelations(entityId);
    }

    @RequestMapping(path = "/graphs", method = RequestMethod.GET)
    public Set<JvrGraphInfoDto> getGraphInfos() {
        return Sets.newHashSet(graphViewService.getGraphs()).stream().map(GRAPH_TO_GRAPH_INFO_DTO).collect(toSet());
    }

    @RequestMapping(path = "/queries", method = RequestMethod.GET)
    public Set<JvrQueryInfoDto> getQueryInfos() {
        Set<JvrQueryDto> queries = Sets.newHashSet(queryService.getQueries());
        return queries.stream().map(QUERY_TO_QUERY_INFO_DTO).collect(Collectors.toSet());
    }

    @RequestMapping(path = "/queries/{id}", method = RequestMethod.GET)
    public JvrQueryDto getQuery(@PathVariable("id") String queryId) {
        JvrQueryDto query = queryService.getQuery(queryId);
        return query;
    }

    @RequestMapping(path = "/graphs/{id}", method = RequestMethod.GET)
    public JvrGraphDto getGraph(@PathVariable("id") String graphId) {
        JvrGraphDto graphDto = graphViewService.getGraph(graphId);
        return graphDto;
    }

    @RequestMapping(path = "/queries", method = RequestMethod.PUT)
    public void saveQuery(@RequestBody JvrQueryDto query) {
        queryService.saveQuery(query);
    }

    @RequestMapping(path = "/process-query", method = RequestMethod.POST)
    public JvrGraphDto processQuery(@RequestBody JvrQueryExecutionDto execution) {
        Set<JvrEntityDto> startNodes = Sets.newHashSet(graphDatabase.getEntities(execution.getEntities()));
        JvrGraphDto result = queryService.process(startNodes, execution.getQueryName());
        return result;
    }

}
