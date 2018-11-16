package net.brusdeilins.jv_rel.core.jpa;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.brusdeilins.jv_rel.core.impl.dao.JvrEntityDao;

public interface JvrEntityRepository extends CrudRepository<JvrEntityDao, String> {
    @Query("select je from JvrEntityDao je where type_id = 'PACKAGE' and not exists(select jr from JvrRelationDao jr where target.id = je.id)")
    Set<JvrEntityDao> findTopLevelPackages();

}
