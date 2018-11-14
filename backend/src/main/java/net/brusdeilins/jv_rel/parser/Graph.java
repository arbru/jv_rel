package net.brusdeilins.jv_rel.parser;

import java.util.Set;

public class Graph {
    private final Set<Entity> entities;
    private final Set<Relation> relations;

    public Graph(String name, Set<Entity> entities, Set<Relation> relations) {
        this.entities = entities;
        this.relations = relations;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public Set<Relation> getRelations() {
        return relations;
    }

}
