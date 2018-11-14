package net.brusdeilins.jv_rel.core.api;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import net.brusdeilins.jv_rel.core.api.dao.JvrEntityDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrEntityTypeDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationDao;
import net.brusdeilins.jv_rel.core.api.dao.JvrRelationTypeDao;

@Service
public interface GraphDatabase {
    Iterable<JvrRelationTypeDao> getRelationTypes();

    JvrEntityTypeDao createEntityType(String entityType);

    JvrRelationTypeDao createRelationType(String relationType);

    JvrRelationTypeDao getRelationType(String relationType);

    JvrEntityDao createEntity(String id, String type, String name, String qualifiedName);

    JvrRelationDao createRelation(String id, JvrRelationTypeDao type, JvrEntityDao source, JvrEntityDao target);

    Optional<JvrEntityDao> getEntityById(String id);

    Set<JvrEntityDao> findTopLevelPackages();

    Set<JvrRelationDao> findTargetRelations(String sourceId);

    Set<JvrRelationDao> getRelations(String entityId);

    Iterable<JvrEntityDao> getEntities(Set<String> entityIds);

}
