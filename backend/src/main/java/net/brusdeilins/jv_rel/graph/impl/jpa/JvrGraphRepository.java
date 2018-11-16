package net.brusdeilins.jv_rel.graph.impl.jpa;

import org.springframework.data.repository.CrudRepository;

import net.brusdeilins.jv_rel.graph.impl.dao.JvrGraphDao;

public interface JvrGraphRepository extends CrudRepository<JvrGraphDao, String> {
}
