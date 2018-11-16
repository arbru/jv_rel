package net.brusdeilins.jv_rel.core.jpa;

import org.springframework.data.repository.CrudRepository;

import net.brusdeilins.jv_rel.core.impl.dao.JvrEntityTypeDao;

public interface JvrEntityTypeRepository extends CrudRepository<JvrEntityTypeDao, String> {

}
