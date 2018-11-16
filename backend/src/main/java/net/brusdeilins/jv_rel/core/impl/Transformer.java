package net.brusdeilins.jv_rel.core.impl;

import java.util.function.Function;

import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityTypeDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationTypeDto;
import net.brusdeilins.jv_rel.core.impl.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.impl.dao.JvrEntityTypeDao;
import net.brusdeilins.jv_rel.core.impl.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.core.impl.dao.JvrRelationTypeDao;

public class Transformer {

    public static Function<JvrEntityDto, JvrEntityDao> TO_ENTITY_DAO = dto -> {
        JvrEntityDao dao = new JvrEntityDao();
        dao.setId(dto.getId());
        dao.setName(dto.getName());
        JvrEntityTypeDao entityTypeDao = new JvrEntityTypeDao();
        entityTypeDao.setId(dto.getType());
        dao.setType(entityTypeDao);
        return dao;
    };


    public static Function<JvrEntityDao, JvrEntityDto> TO_ENTITY_DTO = dao -> {
        JvrEntityDto dto = new JvrEntityDto();
        dto.setId(dao.getId());
        dto.setType(dao.getType().getId());
        dto.setName(dao.getName());
        dto.setQualifiedName(dao.getQualifiedName());
        return dto;
    };

    public static Function<JvrRelationTypeDao, JvrRelationTypeDto> TO_RELATION_TYPE_DTO = dao -> {
        JvrRelationTypeDto dto = new JvrRelationTypeDto();
        dto.setId(dao.getId());
        return dto;
    };

    public static Function<JvrRelationTypeDto, JvrRelationTypeDao> TO_RELATION_TYPE_DAO = dto -> {
        JvrRelationTypeDao dao = new JvrRelationTypeDao();
        dao.setId(dto.getId());
        return dao;
    };

    public static Function<JvrEntityTypeDao, JvrEntityTypeDto> TO_ENTITY_TYPE_DTO = dao -> {
        JvrEntityTypeDto dto = new JvrEntityTypeDto();
        dto.setId(dao.getId());
        return dto;
    };

    public static Function<String, JvrEntityTypeDao> TO_ENTITY_TYPE_DAO = dto -> {
        JvrEntityTypeDao dao = new JvrEntityTypeDao();
        dao.setId(dto);
        return dao;
    };

    public static Function<JvrRelationDao, JvrRelationDto> TO_RELATION_DTO = dao -> {
        JvrRelationDto dto = new JvrRelationDto();
        dto.setId(dao.getId());
        dto.setType(dao.getType().getId());
        dto.setSource(TO_ENTITY_DTO.apply(dao.getSource()));
        dto.setTarget(TO_ENTITY_DTO.apply(dao.getTarget()));
        return dto;
    };


}
