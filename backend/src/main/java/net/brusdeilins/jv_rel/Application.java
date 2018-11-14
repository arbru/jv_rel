package net.brusdeilins.jv_rel;

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
import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationTypeDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrEntityViewDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrEntityViewId;
import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrRelationViewDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrRelationViewId;
import net.brusdeilins.jv_rel.graph.impl.jpa.JvrGraphRepository;
import net.brusdeilins.jv_rel.parser.Entity;
import net.brusdeilins.jv_rel.parser.EntityType;
import net.brusdeilins.jv_rel.parser.Graph;
import net.brusdeilins.jv_rel.parser.GraphBuilder;
import net.brusdeilins.jv_rel.parser.Relation;
import net.brusdeilins.jv_rel.parser.RelationType;
import net.brusdeilins.jv_rel.query.api.QueryService;
import net.brusdeilins.jv_rel.query.api.dao.JvrDirection;
import net.brusdeilins.jv_rel.query.api.dao.JvrQueryDao;
import net.brusdeilins.jv_rel.query.api.dao.JvrRelationFilterDao;
import net.brusdeilins.jv_rel.query.api.dao.JvrRelationFilterId;
import net.brusdeilins.jv_rel.query.impl.jpa.JvrQueryRepository;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(GraphDatabase graphDatabase, JvrGraphRepository jgr, QueryService queryProcessor,
            JvrQueryRepository jqr) {
        return (args) -> {

            for (EntityType entityType : EntityType.values()) {
                graphDatabase.createEntityType(entityType.name());
            }

            Map<RelationType, JvrRelationTypeDao> relationTypeMap = Maps.newHashMap();
            for (RelationType relationType : RelationType.values()) {
                JvrRelationTypeDao rtd = graphDatabase.createRelationType(relationType.name());
                relationTypeMap.put(relationType, rtd);
            }

            GraphBuilder gb = new net.brusdeilins.jv_rel.parser.GraphBuilder();
            Graph g = gb.build(Lists.newArrayList("target/classes"), ArrayListMultimap.create());
            Map<Entity, JvrEntityDao> entityMap = Maps.newHashMap();
            for (Entity entity : g.getEntities()) {
                JvrEntityDao entityDao = graphDatabase.createEntity(entity.getId(), entity.getType().name(),
                        entity.getName(), entity.getQName());
                entityMap.put(entity, entityDao);
            }

            Map<Relation, JvrRelationDao> relationMap = Maps.newHashMap();
            for (Relation relation : g.getRelations()) {
                JvrRelationDao relationDao = graphDatabase.createRelation(relation.getId(),
                        relationTypeMap.get(relation.getType()), entityMap.get(relation.getSource()),
                        entityMap.get(relation.getTarget()));
                relationMap.put(relation, relationDao);
            }

            JvrGraphDao jgd = new JvrGraphDao();
            jgd.setId("test_graph");
            jgd.setName("Test Graph");
            Set<JvrEntityViewDao> jevds = Sets.newHashSet();
            int cnt = 0;
            for (Entity entity : g.getEntities()) {
                JvrEntityDao jed = entityMap.get(entity);
                JvrEntityViewDao jevd = new JvrEntityViewDao();
                JvrEntityViewId jevi = new JvrEntityViewId();
                jevi.setGraph(jgd);
                jevi.setEntity(jed);
                jevd.setId(jevi);
                jevd.setX(cnt++);
                jevds.add(jevd);
            }
            jgd.setEntities(jevds);

            Set<JvrRelationViewDao> jrvds = Sets.newHashSet();
            for (Relation entity : g.getRelations()) {
                JvrRelationDao jed = relationMap.get(entity);
                JvrRelationViewDao jevd = new JvrRelationViewDao();
                JvrRelationViewId jevi = new JvrRelationViewId();
                jevi.setGraph(jgd);
                jevi.setRelation(jed);
                jevd.setId(jevi);
                jevd.setX(cnt++);
                jrvds.add(jevd);
            }
            jgd.setRelations(jrvds);

            jgr.save(jgd);

            JvrQueryDao jq = new JvrQueryDao();
            jq.setName("test_query");
            jq.setMaxHops(10);
            JvrRelationFilterDao rf = new JvrRelationFilterDao();
            JvrRelationFilterId rfid = new JvrRelationFilterId();
            rfid.setQuery(jq);
            rfid.setRelationType(relationTypeMap.get(RelationType.HAS_PACKAGE));
            rf.setId(rfid);
            rf.setDirection(JvrDirection.FORWARD);
            rf.setMaxHops(5);
            jq.setRelationFilter(Sets.newHashSet(rf));

            jqr.save(jq);

            Set<JvrEntityDao> toplevel = graphDatabase.findTopLevelPackages();
            JvrGraphDao graph = queryProcessor.process(toplevel, "test_query");
            graph.setId("result_graph");
            graph.setName("result_graph");
            jgr.save(graph);
        };
    }
}
