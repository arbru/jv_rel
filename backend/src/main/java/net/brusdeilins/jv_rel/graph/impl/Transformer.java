package net.brusdeilins.jv_rel.graph.impl;

import static java.util.stream.Collectors.toSet;

import java.util.function.Function;

import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.core.impl.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.impl.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.graph.api.dto.JvrEntityViewDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrRelationViewDto;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrEntityViewDao;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrGraphDao;
import net.brusdeilins.jv_rel.graph.impl.dao.JvrRelationViewDao;

public class Transformer {

    public static Function<JvrEntityDto, JvrEntityViewDto> TO_ENTITY_VIEW = entityDto -> {
        JvrEntityViewDto entityViewDto = new JvrEntityViewDto();
        entityViewDto.setId(entityDto.getId());
        entityViewDto.setType(entityDto.getType());
        entityViewDto.setName(entityDto.getName());
        entityViewDto.setQualifiedName(entityDto.getQualifiedName());
        return entityViewDto; 
    };
    public static Function<JvrRelationDto, JvrRelationViewDto> TO_RELATION_VIEW = relationDto -> {
        JvrRelationViewDto relationViewDto = new JvrRelationViewDto();
        relationViewDto.setId(relationDto.getId());
        relationViewDto.setType(relationDto.getType());
        relationViewDto.setSourceId(relationDto.getSource().getId());
        relationViewDto.setTargetId(relationDto.getTarget().getId());
        return relationViewDto; 
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
        dto.setEntities(dao.getEntities().stream().map(TO_ENTITY_VIEW_DTO).collect(toSet()));
        dto.setRelations(dao.getRelations().stream().map(TO_RELATION_VIEW_DTO).collect(toSet()));
        return dto;
    };

}
