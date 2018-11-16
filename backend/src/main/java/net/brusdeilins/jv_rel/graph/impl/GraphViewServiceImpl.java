package net.brusdeilins.jv_rel.graph.impl;

import static java.util.stream.Collectors.toSet;
import static net.brusdeilins.jv_rel.graph.impl.Transformer.TO_GRAPH_DTO;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.jpa.JvrEntityRepository;
import net.brusdeilins.jv_rel.core.jpa.JvrRelationRepository;
import net.brusdeilins.jv_rel.graph.api.GraphViewService;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrEntityViewDao;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrEntityViewId;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrRelationViewDao;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrRelationViewId;
import net.brusdeilins.jv_rel.graph.impl.jpa.JvrGraphRepository;


@Component
public class GraphViewServiceImpl implements GraphViewService {

    @Autowired
    private JvrGraphRepository graphRepository;

    @Autowired
    private JvrEntityRepository entityRepository;

    @Autowired
    private JvrRelationRepository relationRepository;

    @Override
    @Transactional
    public Iterable<JvrGraphDto> getGraphs() {
        return Sets.newHashSet(graphRepository.findAll()).stream().map(TO_GRAPH_DTO).collect(toSet());
    }

    @Override
    @Transactional
    public JvrGraphDto getGraph(String id) {
        return TO_GRAPH_DTO.apply(graphRepository.findById(id).get());
    }

    @Override
    public void saveGraph(JvrGraphDto graph) {
        JvrGraphDao dao = new JvrGraphDao();
        dao.setId(graph.getId());
        dao.setName(graph.getName());
        dao.setEntities(graph.getEntities().stream().map(entityViewDto -> {
            JvrEntityViewDao entityViewDao = new JvrEntityViewDao();
            JvrEntityViewId viewId = new JvrEntityViewId();
            viewId.setGraph(dao);
            viewId.setEntity(entityRepository.findById(entityViewDto.getId()).get());
            entityViewDao.setId(viewId);
            entityViewDao.setX(entityViewDto.getX());
            entityViewDao.setY(entityViewDto.getY());
            return entityViewDao;
        }).collect(toSet()));
        dao.setRelations(graph.getRelations().stream().map(relationViewDto -> {
            JvrRelationViewDao relationViewDao = new JvrRelationViewDao();
            JvrRelationViewId viewId = new JvrRelationViewId();
            viewId.setGraph(dao);
            viewId.setRelation(relationRepository.findById(relationViewDto.getId()).get());
            relationViewDao.setId(viewId);
            relationViewDao.setX(relationViewDto.getX());
            relationViewDao.setY(relationViewDto.getY());
            return relationViewDao;
        }).collect(toSet()));
        graphRepository.save(dao);
    }

}
