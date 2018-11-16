package net.brusdeilins.jv_rel.core.impl;

import static java.util.stream.Collectors.toSet;
import static net.brusdeilins.jv_rel.core.impl.Transformer.TO_ENTITY_DAO;
import static net.brusdeilins.jv_rel.core.impl.Transformer.TO_ENTITY_DTO;
import static net.brusdeilins.jv_rel.core.impl.Transformer.TO_ENTITY_TYPE_DTO;
import static net.brusdeilins.jv_rel.core.impl.Transformer.TO_RELATION_DTO;
import static net.brusdeilins.jv_rel.core.impl.Transformer.TO_RELATION_TYPE_DAO;
import static net.brusdeilins.jv_rel.core.impl.Transformer.TO_RELATION_TYPE_DTO;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityTypeDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationTypeDto;
import net.brusdeilins.jv_rel.core.impl.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.impl.dao.JvrEntityTypeDao;
import net.brusdeilins.jv_rel.core.impl.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.core.impl.dao.JvrRelationTypeDao;
import net.brusdeilins.jv_rel.core.jpa.JvrEntityRepository;
import net.brusdeilins.jv_rel.core.jpa.JvrEntityTypeRepository;
import net.brusdeilins.jv_rel.core.jpa.JvrRelationRepository;
import net.brusdeilins.jv_rel.core.jpa.JvrRelationTypeRepository;

@Component
public class GraphDatabaseImpl implements GraphDatabase {
    @Autowired
    private JvrEntityRepository entityRepository;

    @Autowired
    private JvrEntityTypeRepository entityTypeRepository;

    @Autowired
    private JvrRelationTypeRepository relationTypeRepository;

    @Autowired
    private JvrRelationRepository relationRepository;

    @Override
    public Optional<JvrEntityDto> getEntityById(String id) {
        Optional<JvrEntityDao> dao = entityRepository.findById(id);
        if (dao.isPresent()) {
            return Optional.ofNullable(TO_ENTITY_DTO.apply(dao.get()));
        }
        return Optional.empty();
    }

    @Override
    public Iterable<JvrRelationTypeDto> getRelationTypes() {
        Iterable<JvrRelationTypeDao> relationTypes = relationTypeRepository.findAll();
        return Sets.newHashSet(relationTypes).stream().map(TO_RELATION_TYPE_DTO)
        .collect(Collectors.toSet());
    }

    @Override
    public JvrEntityTypeDto createEntityType(String entityType) {
        JvrEntityTypeDao entityTypeDao = new JvrEntityTypeDao();
        entityTypeDao.setId(entityType);
        entityTypeRepository.save(entityTypeDao);
        return TO_ENTITY_TYPE_DTO.apply(entityTypeDao);
    }

    @Override
    public JvrRelationTypeDto createRelationType(String relationType) {
        JvrRelationTypeDao relationTypeDao = new JvrRelationTypeDao();
        relationTypeDao.setId(relationType);
        relationTypeRepository.save(relationTypeDao);
        return TO_RELATION_TYPE_DTO.apply(relationTypeDao);
    }

    @Override
    public JvrRelationTypeDto getRelationType(String relationType) {
        return TO_RELATION_TYPE_DTO.apply(relationTypeRepository.findById(relationType).get());
    }

    @Override
    public JvrEntityDto createEntity(String id, String type, String name, String qualifiedName) {
        JvrEntityDao entity = new JvrEntityDao();
        entity.setId(id);
        entity.setType(entityTypeRepository.findById(type).get());
        entity.setName(name);
        entity.setQualifiedName(qualifiedName);
        entityRepository.save(entity);
        return TO_ENTITY_DTO.apply(entity);
    }

    @Override
    public JvrRelationDto createRelation(String id, JvrRelationTypeDto type, JvrEntityDto source, JvrEntityDto target) {
        JvrRelationDao relation = new JvrRelationDao();
        relation.setId(id);
        relation.setType(TO_RELATION_TYPE_DAO.apply(type));
        relation.setSource(TO_ENTITY_DAO.apply(source));
        relation.setTarget(TO_ENTITY_DAO.apply(target));
        relationRepository.save(relation);
        return TO_RELATION_DTO.apply(relation);
    }

    @Override
    public Set<JvrEntityDto> findTopLevelPackages() {
        return entityRepository.findTopLevelPackages().stream().map(TO_ENTITY_DTO).collect(toSet());
    }

    @Override
    public Set<JvrRelationDto> getRelations(String entityId) {
        return relationRepository.getRelations(entityId).stream().map(TO_RELATION_DTO).collect(toSet());
    }

    @Override
    public Set<JvrRelationDto> findTargetRelations(String sourceId) {
        return relationRepository.findTargetRelations(sourceId).stream().map(TO_RELATION_DTO).collect(toSet());
    }

    @Override
    public Iterable<JvrEntityDto> getEntities(Set<String> entityIds) {
        return Sets.newHashSet(entityRepository.findAllById(entityIds)).stream().map(TO_ENTITY_DTO).collect(toSet());
    }

}
