package net.brusdeilins.jv_rel.core.api;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import net.brusdeilins.jv_rel.core.api.dto.JvrEntityDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrEntityTypeDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationDto;
import net.brusdeilins.jv_rel.core.api.dto.JvrRelationTypeDto;

@Service
public interface GraphDatabase {
    Iterable<JvrRelationTypeDto> getRelationTypes();

    JvrEntityTypeDto createEntityType(String entityType);

    JvrRelationTypeDto createRelationType(String relationType);

    JvrRelationTypeDto getRelationType(String relationType);

    JvrEntityDto createEntity(String id, String type, String name, String qualifiedName);

    JvrRelationDto createRelation(String id, JvrRelationTypeDto type, JvrEntityDto source, JvrEntityDto target);

    Optional<JvrEntityDto> getEntityById(String id);

    Set<JvrEntityDto> findTopLevelPackages();

    Set<JvrRelationDto> findTargetRelations(String sourceId);

    Set<JvrRelationDto> getRelations(String entityId);

    Iterable<JvrEntityDto> getEntities(Set<String> entityIds);

}
