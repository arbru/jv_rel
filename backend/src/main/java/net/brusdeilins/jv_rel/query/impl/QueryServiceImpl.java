package net.brusdeilins.jv_rel.query.impl;

import static java.util.stream.Collectors.toSet;
import static net.brusdeilins.jv_rel.query.impl.Transformer.TO_QUERY_DAO;
import static net.brusdeilins.jv_rel.query.impl.Transformer.TO_QUERY_DTO;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.query.api.QueryService;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.query.impl.jpa.JvrQueryRepository;

@Component
public class QueryServiceImpl implements QueryService {

    @Autowired
    private JvrQueryRepository queryRepository;

    @Autowired
    private GraphDatabase graphDatabase;

    @Override
    public JvrGraphDto process(Set<JvrEntityDto> startNodes, String query) {
        JvrQueryDto queryDao = TO_QUERY_DTO.apply(queryRepository.findById(query).get());
        QueryProcessor qp = new QueryProcessor(queryDao, graphDatabase);
        qp.process(startNodes);
        return qp.getGraph();
    }

    @Override
    public Iterable<JvrQueryDto> getQueries() {
        return Sets.newHashSet(queryRepository.findAll()).stream().map(TO_QUERY_DTO).collect(toSet());
    }

    @Override
    public JvrQueryDto getQuery(String id) {
        return TO_QUERY_DTO.apply(queryRepository.findById(id).get());
    }

    @Override
    public void saveQuery(JvrQueryDto query) {
        queryRepository.save(TO_QUERY_DAO.apply(query));
    }

}
