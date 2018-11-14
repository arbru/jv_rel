package net.brusdeilins.jv_rel.rest;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationTypeDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrEntityViewDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.graph.api.dao.JvrRelationViewDao;
import net.brusdeilins.jv_rel.query.api.dao.JvrDirection;
import net.brusdeilins.jv_rel.query.api.dao.JvrQueryDao;
import net.brusdeilins.jv_rel.query.api.dao.JvrRelationFilterDao;
import net.brusdeilins.jv_rel.query.api.dao.JvrRelationFilterId;
import net.brusdeilins.jv_rel.rest.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.rest.dto.JvrEntityViewDto;
import net.brusdeilins.jv_rel.rest.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.rest.dto.JvrGraphInfoDto;
import net.brusdeilins.jv_rel.rest.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.rest.dto.JvrQueryInfoDto;
import net.brusdeilins.jv_rel.rest.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.rest.dto.JvrRelationFilterDto;
import net.brusdeilins.jv_rel.rest.dto.JvrRelationTypeDto;
import net.brusdeilins.jv_rel.rest.dto.JvrRelationViewDto;

public class Transformer {
    public static Function<JvrEntityDao, JvrEntityDto> TO_ENTITY_DTO = dao -> {
        JvrEntityDto dto = new JvrEntityDto();
        dto.setId(dao.getId());
        dto.setType(dao.getType().getId());
        dto.setName(dao.getName());
        dto.setQualifiedName(dao.getQualifiedName());
        return dto;
    };
    public static Function<JvrRelationDao, JvrRelationDto> TO_RELATION_DTO = dao -> {
        JvrRelationDto dto = new JvrRelationDto();
        dto.setId(dao.getId());
        dto.setType(dao.getType().getId());
        dto.setSource(TO_ENTITY_DTO.apply(dao.getSource()));
        dto.setTarget(TO_ENTITY_DTO.apply(dao.getTarget()));
        return dto;
    };

    public static Function<JvrGraphDao, JvrGraphInfoDto> TO_GRAPH_INFO_DTO = dao -> {
        JvrGraphInfoDto dto = new JvrGraphInfoDto();
        dto.setId(dao.getId());
        dto.setName(dao.getName());
        return dto;
    };

    public static Function<JvrQueryDao, JvrQueryInfoDto> TO_QUERY_INFO_DTO = dao -> {
        JvrQueryInfoDto dto = new JvrQueryInfoDto();
        dto.setName(dao.getName());
        return dto;
    };

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
        dto.setRelationFilter(dao.getRelationFilter().stream().map(TO_RELATION_FILTER_DTO).collect(Collectors.toSet()));
        return dto;
    };

    public static Function<JvrEntityViewDao, JvrEntityViewDto> TO_ENTITY_VIEW_DTO = dao -> {
        JvrEntityViewDto dto = new JvrEntityViewDto();
        JvrEntityDao entityDao = dao.getId().getEntity();
        dto.setId(entityDao.getId());
        dto.setType(entityDao.getType().getId());
        dto.setName(entityDao.getName());
        dto.setQualifiedName(entityDao.getQualifiedName());
        return dto;
    };

    public static Function<JvrRelationViewDao, JvrRelationViewDto> TO_RELATION_VIEW_DTO = dao -> {
        JvrRelationViewDto dto = new JvrRelationViewDto();
        JvrRelationDao relationDao = dao.getId().getRelation();
        dto.setId(relationDao.getId());
        dto.setType(relationDao.getType().getId());
        dto.setSourceId(relationDao.getSource().getId());
        dto.setTargetId(relationDao.getTarget().getId());
        dto.setX(dao.getX());
        dto.setY(dao.getY());
        return dto;
    };

    public static Function<JvrGraphDao, JvrGraphDto> TO_GRAPH_DTO = dao -> {
        JvrGraphDto dto = new JvrGraphDto();
        dto.setId(dao.getId());
        dto.setName(dao.getName());
        dto.setEntities(dao.getEntities().stream().map(TO_ENTITY_VIEW_DTO).collect(Collectors.toSet()));
        dto.setRelations(dao.getRelations().stream().map(TO_RELATION_VIEW_DTO).collect(Collectors.toSet()));
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

    public static Function<JvrRelationTypeDao, JvrRelationTypeDto> TO_RELATION_TYPE_DTO = dao -> {
        JvrRelationTypeDto dto = new JvrRelationTypeDto();
        dto.setId(dao.getId());
        return dto;
    };
}
