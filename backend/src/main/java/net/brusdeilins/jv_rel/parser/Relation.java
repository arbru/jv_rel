package net.brusdeilins.jv_rel.parser;

import static net.brusdeilins.jv_rel.parser.Md5Generator.md5;

public class Relation {
    private final String id;
    private final RelationType type;
    private final Entity source;
    private final Entity target;

    public Relation(RelationType type, Entity source, Entity target) {
        super();
        this.type = type;
        this.source = source;
        this.target = target;
        this.id = md5(this.type.toString() + "-" + this.source.getId() + "-" + this.target.getId());
    }

    public RelationType getType() {
        return type;
    }

    public Entity getSource() {
        return source;
    }

    public Entity getTarget() {
        return target;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Relation [id=" + id + ", type=" + type + ", source=" + source + ", target=" + target + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Relation other = (Relation) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}