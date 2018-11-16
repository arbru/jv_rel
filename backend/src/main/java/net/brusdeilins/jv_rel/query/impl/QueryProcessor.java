package net.brusdeilins.jv_rel.query.impl;

import java.util.Set;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrEntityViewDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrRelationViewDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrDirection;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrRelationFilterDto;

public class QueryProcessor {
    private JvrQueryDto query;
    private JvrGraphDto graph;
    private Set<JvrEntityViewDto> visitedEntities = Sets.newHashSet();
    private Set<JvrRelationViewDto> visitedRelations = Sets.newHashSet();
    private GraphDatabase graphDatabase;

    public QueryProcessor(JvrQueryDto query, GraphDatabase graphDatabase) {
        this.query = query;
        this.graphDatabase = graphDatabase;
        this.graph = new JvrGraphDto();
        this.graph.setId("test_result");
    }

    public void process(Set<JvrEntityDto> nodes) {
        process(nodes, 0);
    }

    private void process(Set<JvrEntityDto> nodes, int hops) {
        if (hops >= query.getMaxHops()) {
            return;
        }
        Set<JvrEntityDto> resultNodes = Sets.newHashSet();
        for (JvrEntityDto node : nodes) {
            if (visitedEntities.contains(node)) {
                continue;
            }
            Set<JvrRelationDto> rels = graphDatabase.getRelations(node.getId());
            for (JvrRelationDto rel : rels) {
                if (visitedRelations.contains(rel)) {
                    continue;
                }
                JvrDirection dir = JvrDirection.FORWARD;
                JvrEntityDto otherNode = rel.getTarget();
                if (rel.getTarget().getId().equals(node.getId())) {
                    dir = JvrDirection.BACKWARD;
                    otherNode = rel.getSource();
                }

                for (JvrRelationFilterDto rf : query.getRelationFilter()) {
                    if (rf.getMaxHops() <= hops) {
                        continue;
                    }
                    if (rf.getRelationType().equals(rel.getType())) {
                        JvrDirection rfdir = JvrDirection.valueOf(rf.getDirection());
                        if (rfdir == dir || rfdir == JvrDirection.BOTH) {
                            resultNodes.add(otherNode);
                            JvrRelationViewDto relView = new JvrRelationViewDto();
                            relView.setId(rel.getId());
                            relView.setType(rel.getType());
                            relView.setSourceId(rel.getSource().getId());
                            relView.setTargetId(rel.getTarget().getId());
                            relView.setX(hops);
                            relView.setY(0);
                            visitedRelations.add(relView);
                            break;
                        }
                    }
                }
            }
            JvrEntityViewDto nodeView = new JvrEntityViewDto();
            nodeView.setId(node.getId());
            nodeView.setName(node.getName());
            nodeView.setQualifiedName(node.getQualifiedName());
            nodeView.setType(node.getType());
            nodeView.setX(hops);
            nodeView.setY(0);
            visitedEntities.add(nodeView);
        }
        if (resultNodes.size() == 0) {
            return;
        }
        process(resultNodes, hops + 1);
    }

    public JvrGraphDto getGraph() {
        this.graph.setEntities(visitedEntities);
        this.graph.setRelations(visitedRelations);
        return this.graph;
    }
}
