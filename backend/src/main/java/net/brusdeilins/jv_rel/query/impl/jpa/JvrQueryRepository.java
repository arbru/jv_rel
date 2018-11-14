package net.brusdeilins.jv_rel.query.impl.jpa;

import org.springframework.data.repository.CrudRepository;

import net.brusdeilins.jv_rel.query.api.dao.JvrQueryDao;

public interface JvrQueryRepository extends CrudRepository<JvrQueryDao, String> {

}
