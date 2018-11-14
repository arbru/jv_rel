package net.brusdeilins.jv_rel.query.impl;

import java.util.Set;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrEntityViewDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrEntityViewId;
import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrRelationViewDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrRelationViewId;
import net.brusdeilins.jv_rel.query.api.dao.JvrDirection;
import net.brusdeilins.jv_rel.query.api.dao.JvrQueryDao;
import net.brusdeilins.jv_rel.query.api.dao.JvrRelationFilterDao;

public class QueryProcessor {
    private JvrQueryDao query;
    private JvrGraphDao graph;
    private Set<JvrEntityViewDao> visitedEntities = Sets.newHashSet();
    private Set<JvrRelationViewDao> visitedRelations = Sets.newHashSet();
    private GraphDatabase graphDatabase;

    public QueryProcessor(JvrQueryDao query, GraphDatabase graphDatabase) {
        this.query = query;
        this.graphDatabase = graphDatabase;
        this.graph = new JvrGraphDao();
        this.graph.setId("test_result");
    }

    public void process(Set<JvrEntityDao> nodes) {
        process(nodes, 0);
    }

    private void process(Set<JvrEntityDao> nodes, int hops) {
        if (hops >= query.getMaxHops()) {
            return;
        }
        Set<JvrEntityDao> resultNodes = Sets.newHashSet();
        for (JvrEntityDao node : nodes) {
            if (visitedEntities.contains(node)) {
                continue;
            }
            Set<JvrRelationDao> rels = graphDatabase.getRelations(node.getId());
            for (JvrRelationDao rel : rels) {
                if (visitedRelations.contains(rel)) {
                    continue;
                }
                JvrDirection dir = JvrDirection.FORWARD;
                JvrEntityDao otherNode = rel.getTarget();
                if (rel.getTarget().getId().equals(node.getId())) {
                    dir = JvrDirection.BACKWARD;
                    otherNode = rel.getSource();
                }

                for (JvrRelationFilterDao rf : query.getRelationFilter()) {
                    if (rf.getMaxHops() <= hops) {
                        continue;
                    }
                    if (rf.getId().getRelationType().getId().equals(rel.getType().getId())) {
                        JvrDirection rfdir = rf.getDirection();
                        if (rfdir == dir || rfdir == JvrDirection.BOTH) {
                            resultNodes.add(otherNode);
                            JvrRelationViewDao relView = new JvrRelationViewDao();
                            JvrRelationViewId relViewId = new JvrRelationViewId();
                            if (graph == null || rel == null) {
                                throw new RuntimeException("something is NULL");
                            }
                            relViewId.setGraph(graph);
                            relViewId.setRelation(rel);
                            relView.setId(relViewId);
                            relView.setX(hops);
                            relView.setY(0);
                            visitedRelations.add(relView);
                            break;
                        }
                    }
                }
            }
            JvrEntityViewDao nodeView = new JvrEntityViewDao();
            JvrEntityViewId nodeViewId = new JvrEntityViewId();
            if (graph == null || node == null) {
                throw new RuntimeException("something is NULL");
            }
            nodeViewId.setGraph(graph);
            nodeViewId.setEntity(node);
            nodeView.setId(nodeViewId);
            nodeView.setX(hops);
            nodeView.setY(0);
            visitedEntities.add(nodeView);
        }
        if (resultNodes.size() == 0) {
            return;
        }
        process(resultNodes, hops + 1);
    }

    public JvrGraphDao getGraph() {
        this.graph.setEntities(visitedEntities);
        this.graph.setRelations(visitedRelations);
        return this.graph;
    }
}
