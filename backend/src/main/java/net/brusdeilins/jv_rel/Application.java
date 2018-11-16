package net.brusdeilins.jv_rel;

import static net.brusdeilins.jv_rel.graph.impl.Transformer.TO_ENTITY_VIEW;
import static net.brusdeilins.jv_rel.graph.impl.Transformer.TO_RELATION_VIEW;

import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationTypeDto;
import net.brusdeilins.jv_rel.graph.api.GraphViewService;
import net.brusdeilins.jv_rel.graph.api.dto.JvrEntityViewDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrRelationViewDto;
import net.brusdeilins.jv_rel.graph.impl.jpa.JvrGraphRepository;
import net.brusdeilins.jv_rel.parser.Entity;
import net.brusdeilins.jv_rel.parser.EntityType;
import net.brusdeilins.jv_rel.parser.Graph;
import net.brusdeilins.jv_rel.parser.GraphBuilder;
import net.brusdeilins.jv_rel.parser.Relation;
import net.brusdeilins.jv_rel.parser.RelationType;
import net.brusdeilins.jv_rel.query.api.QueryService;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrRelationFilterDto;
import net.brusdeilins.jv_rel.query.impl.jpa.JvrQueryRepository;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(GraphDatabase graphDatabase, GraphViewService graphViewService, QueryService queryService, JvrGraphRepository jgr, QueryService queryProcessor,
            JvrQueryRepository jqr) {
        return (args) -> {

            for (EntityType entityType : EntityType.values()) {
                graphDatabase.createEntityType(entityType.name());
            }

            Map<RelationType, JvrRelationTypeDto> relationTypeMap = Maps.newHashMap();
            for (RelationType relationType : RelationType.values()) {
                JvrRelationTypeDto rtd = graphDatabase.createRelationType(relationType.name());
                relationTypeMap.put(relationType, rtd);
            }

            GraphBuilder gb = new net.brusdeilins.jv_rel.parser.GraphBuilder();
            Graph g = gb.build(Lists.newArrayList("target/classes"), ArrayListMultimap.create());
            Map<Entity, JvrEntityDto> entityMap = Maps.newHashMap();
            for (Entity entity : g.getEntities()) {
                JvrEntityDto entityDto = graphDatabase.createEntity(entity.getId(), entity.getType().name(),
                        entity.getName(), entity.getQName());
                entityMap.put(entity, entityDto);
            }

            Map<Relation, JvrRelationDto> relationMap = Maps.newHashMap();
            for (Relation relation : g.getRelations()) {
                JvrRelationDto relationDto = graphDatabase.createRelation(relation.getId(),
                        relationTypeMap.get(relation.getType()), entityMap.get(relation.getSource()),
                        entityMap.get(relation.getTarget()));
                relationMap.put(relation, relationDto);
            }

            JvrGraphDto jgd = new JvrGraphDto();
            jgd.setId("test_graph");
            jgd.setName("Test Graph");
            Set<JvrEntityViewDto> jevds = Sets.newHashSet();
            int cnt = 0;
            for (Entity entity : g.getEntities()) {
                JvrEntityViewDto jevd = TO_ENTITY_VIEW.apply(entityMap.get(entity));
                jevd.setX(cnt++);
                jevds.add(jevd);
            }
            jgd.setEntities(jevds);

            Set<JvrRelationViewDto> jrvds = Sets.newHashSet();
            for (Relation relation : g.getRelations()) {
                JvrRelationViewDto jevd = TO_RELATION_VIEW.apply(relationMap.get(relation));
                jevd.setX(cnt++);
                jrvds.add(jevd);
            }
            jgd.setRelations(jrvds);

            graphViewService.saveGraph(jgd);

            JvrQueryDto jq = new JvrQueryDto();
            jq.setName("test_query");
            jq.setMaxHops(10);
            JvrRelationFilterDto rf = new JvrRelationFilterDto();
            rf.setRelationType("HAS_PACKAGE");
            rf.setDirection("FORWARD");
            rf.setMaxHops(5);
            jq.setRelationFilter(Sets.newHashSet(rf));

            queryService.saveQuery(jq);

            Set<JvrEntityDto> toplevel = graphDatabase.findTopLevelPackages();
            JvrGraphDto graph = queryProcessor.process(toplevel, "test_query");
            graph.setId("result_graph");
            graph.setName("result_graph");
            graphViewService.saveGraph(graph);
        };
    }
}
