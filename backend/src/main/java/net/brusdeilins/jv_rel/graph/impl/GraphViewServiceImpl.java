package net.brusdeilins.jv_rel.graph.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.brusdeilins.jv_rel.graph.api.GraphViewService;
import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.graph.impl.jpa.JvrGraphRepository;

@Component
public class GraphViewServiceImpl implements GraphViewService {

    @Autowired
    private JvrGraphRepository graphRepository;

    @Override
    public Iterable<JvrGraphDao> getGraphs() {
        return graphRepository.findAll();
    }

    @Override
    public JvrGraphDao getGraph(String id) {
        return graphRepository.findById(id).get();
    }

}
