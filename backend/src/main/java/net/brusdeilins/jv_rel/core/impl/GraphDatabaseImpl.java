package net.brusdeilins.jv_rel.core.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.brusdeilins.jv_rel.core.api.GraphDatabase;
import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrEntityTypeDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationTypeDao;
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
    public Optional<JvrEntityDao> getEntityById(String id) {
        return entityRepository.findById(id);
    }

    @Override
    public Iterable<JvrRelationTypeDao> getRelationTypes() {
        return relationTypeRepository.findAll();
    }

    @Override
    public JvrEntityTypeDao createEntityType(String entityType) {
        JvrEntityTypeDao entityTypeDao = new JvrEntityTypeDao();
        entityTypeDao.setId(entityType);
        entityTypeRepository.save(entityTypeDao);
        return entityTypeDao;
    }

    @Override
    public JvrRelationTypeDao createRelationType(String relationType) {
        JvrRelationTypeDao relationTypeDao = new JvrRelationTypeDao();
        relationTypeDao.setId(relationType);
        relationTypeRepository.save(relationTypeDao);
        return relationTypeDao;
    }

    @Override
    public JvrRelationTypeDao getRelationType(String relationType) {
        return relationTypeRepository.findById(relationType).get();
    }

    @Override
    public JvrEntityDao createEntity(String id, String type, String name, String qualifiedName) {
        JvrEntityDao entity = new JvrEntityDao();
        entity.setId(id);
        entity.setType(entityTypeRepository.findById(type).get());
        entity.setName(name);
        entity.setQualifiedName(qualifiedName);
        entityRepository.save(entity);
        return entity;
    }

    @Override
    public JvrRelationDao createRelation(String id, JvrRelationTypeDao type, JvrEntityDao source, JvrEntityDao target) {
        JvrRelationDao relation = new JvrRelationDao();
        relation.setId(id);
        relation.setType(type);
        relation.setSource(source);
        relation.setTarget(target);
        relationRepository.save(relation);
        return relation;
    }

    @Override
    public Set<JvrEntityDao> findTopLevelPackages() {
        return entityRepository.findTopLevelPackages();
    }

    @Override
    public Set<JvrRelationDao> getRelations(String entityId) {
        return relationRepository.getRelations(entityId);
    }

    @Override
    public Set<JvrRelationDao> findTargetRelations(String sourceId) {
        return relationRepository.findTargetRelations(sourceId);
    }

    @Override
    public Iterable<JvrEntityDao> getEntities(Set<String> entityIds) {
        return entityRepository.findAllById(entityIds);
    }

}
