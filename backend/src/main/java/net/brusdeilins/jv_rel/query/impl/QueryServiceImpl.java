package net.brusdeilins.jv_rel.query.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.query.api.QueryService;
import net.brusdeilins.jv_rel.query.api.dao.JvrQueryDao;
import net.brusdeilins.jv_rel.query.impl.jpa.JvrQueryRepository;

@Component
public class QueryServiceImpl implements QueryService {

    @Autowired
    private JvrQueryRepository queryRepository;

    @Autowired
    private GraphDatabase graphDatabase;

    @Override
    public JvrGraphDao process(Set<JvrEntityDao> startNodes, String query) {
        JvrQueryDao queryDao = queryRepository.findById(query).get();
        QueryProcessor qp = new QueryProcessor(queryDao, graphDatabase);
        qp.process(startNodes);
        return qp.getGraph();
    }

    @Override
    public Iterable<JvrQueryDao> getQueries() {
        return queryRepository.findAll();
    }

    @Override
    public JvrQueryDao getQuery(String id) {
        return queryRepository.findById(id).get();
    }

    @Override
    public void saveQuery(JvrQueryDao query) {
        queryRepository.save(query);
    }

}
