package net.brusdeilins.jv_rel.core.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.brusdeilins.jv_rel.core.api.dao.JvrRelationDao;

public interface JvrRelationRepository extends CrudRepository<JvrRelationDao, String> {
    List<JvrRelationDao> findBySourceIdOrTargetId(String sourceId, String targetId);

    @Query("select jr from JvrRelationDao jr where jr.source.id = ?1")
    Set<JvrRelationDao> findTargetRelations(String sourceId);

    @Query("select jr from JvrRelationDao jr where jr.source.id = ?1 or jr.target.id = ?1")
    Set<JvrRelationDao> getRelations(String entityId);

}
