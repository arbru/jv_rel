package net.brusdeilins.jv_rel.query.impl;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.impl.dao.JvrRelationTypeDao;
import net.brusdeilins.jv_rel.query.api.dto.JvrDirection;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrRelationFilterDto;
import net.brusdeilins.jv_rel.query.impl.dao.JvrQueryDao;
import net.brusdeilins.jv_rel.query.impl.dao.JvrRelationFilterDao;
import net.brusdeilins.jv_rel.query.impl.dao.JvrRelationFilterId;


public class Transformer {
    public static Function<JvrRelationFilterDao, JvrRelationFilterDto> TO_RELATION_FILTER_DTO = dao -> {
        JvrRelationFilterDto dto = new JvrRelationFilterDto();
        dto.setDirection(dao.getDirection().name());
        dto.setMaxHops(dao.getMaxHops());
        dto.setRelationType(dao.getId().getRelationType().getId());
        return dto;
    };

    public static Function<JvrQueryDao, JvrQueryDto> TO_QUERY_DTO = dao -> {
        JvrQueryDto dto = new JvrQueryDto();
        dto.setName(dao.getName());
        dto.setMaxHops(dao.getMaxHops());
        dto.setRelationFilter(dao.getRelationFilter().stream().map(TO_RELATION_FILTER_DTO).collect(toSet()));
        return dto;
    };

    public static Function<JvrQueryDto, JvrQueryDao> TO_QUERY_DAO = dto -> {
        JvrQueryDao dao = new JvrQueryDao();
        dao.setName(dto.getName());
        dao.setMaxHops(dto.getMaxHops());
        // javaQueryRepository.save(jq);
        Set<JvrRelationFilterDao> filter = Sets.newHashSet();
        for (JvrRelationFilterDto rfd : dto.getRelationFilter()) {
            JvrRelationFilterDao rf = new JvrRelationFilterDao();
            JvrRelationFilterId rfid = new JvrRelationFilterId();
            rfid.setQuery(dao);
            JvrRelationTypeDao relationType = new JvrRelationTypeDao();
            relationType.setId(rfd.getRelationType());
            rfid.setRelationType(relationType);
            rf.setId(rfid);
            rf.setMaxHops(rfd.getMaxHops());
            rf.setDirection(JvrDirection.valueOf(rfd.getDirection()));
            // javaRelationFilterRepository.save(rf);
            filter.add(rf);
        }
        dao.setRelationFilter(filter);
        return dao;
    };

}
