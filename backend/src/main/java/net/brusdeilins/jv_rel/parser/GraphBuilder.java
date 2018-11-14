package net.brusdeilins.jv_rel.parser;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static net.brusdeilins.jv_rel.parser.EntityType.CLASS;
import static net.brusdeilins.jv_rel.parser.EntityType.ENUM;
import static net.brusdeilins.jv_rel.parser.EntityType.INTERFACE;
import static net.brusdeilins.jv_rel.parser.EntityType.MODULE;
import static net.brusdeilins.jv_rel.parser.Functions.EXPLODE_METHODS;
import static net.brusdeilins.jv_rel.parser.Functions.PATH_TO_STREAM;
import static net.brusdeilins.jv_rel.parser.Functions.STREAM_TO_ENTITY;
import static net.brusdeilins.jv_rel.parser.Functions.STRING_TO_PATH;
import static net.brusdeilins.jv_rel.parser.RelationType.HAS_CLASS;
import static net.brusdeilins.jv_rel.parser.RelationType.HAS_PACKAGE;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GraphBuilder {
    /*
     * public static List<String> excludes = Lists.newArrayList(
     * "de/zalando/bm/backend/configuration/ApplicationConfig",
     * "de/zalando/bm/backend/configuration/impl/ApplicationConfigImpl",
     * "de/zalando/bm/backend/service/partner/PartnerService",
     * "de/zalando/bm/webservice/services/oauth/TokensManager",
     * "de/zalando/bm/backend/jobs/AbstractConfiguredBMJob",
     * "de/zalando/bm/backend/domain/partner/Partner",
     * "de/zalando/bm/backend/configuration/BmFeatureToggle",
     * "de/zalando/bm/backend/service/partner/PartnerManagerService",
     * "de/zalando/bm/backend/jobs/BmJobGroup",
     * "de/zalando/bm/backend/configuration/ServiceTimeoutConfig",
     * "de/zalando/bm/backend/persistence/events/EventSProcService",
     * "de/zalando/bm/backend/service/businesspartner/BusinessPartnerService",
     * "de/zalando/bm/backend/configuration/PegasusConfig",
     * "de/zalando/bm/backend/service/TranslationService",
     * "de/zalando/bm/webservice/services/domain/customer/ShippingDelayParameterWithPartner",
     * "de/zalando/bm/backend/domain/ArticleSizeType",
     * "de/zalando/bm/backend/domain/AttributeUnit",
     * "de/zalando/bm/backend/domain/ArticleType" );
     */

    public Graph build(List<String> archives, ArrayListMultimap<String, String> modules) {
        Map<String, Entity> entities = archives.stream().map(STRING_TO_PATH).map(PATH_TO_STREAM)
                .flatMap(l -> l.stream()).map(STREAM_TO_ENTITY)
                // .filter(e -> !excludes.contains(e.getQName()))
                .flatMap(EXPLODE_METHODS).collect(toMap(e -> e.getQName(), e -> e));

        // add modules
        if (modules != null) {
            for (String moduleName : modules.keySet()) {
                Entity m = new Entity(EntityType.MODULE, moduleName, moduleName);
                entities.put(moduleName, m);
            }
        }

        // entities.values().forEach(System.out::println);

        Set<Relation> relations = entities.values().stream().flatMap(e -> e.resolveRelations(entities))
                .collect(toSet());

        Set<Relation> pkgrels = createPackageStructure(entities);
        // pkgrels.forEach(System.out::println);
        relations.addAll(pkgrels);

        if (modules != null) {
            for (String moduleName : modules.keySet()) {
                Entity m = entities.get(moduleName);
                for (String en : modules.get(moduleName)) {
                    Entity e = entities.get(en);
                    if (e != null) {
                        Relation r = new Relation(RelationType.HAS_CLASS, m, e);
                        relations.add(r);
                    }
                }
            }
        }
        return new Graph("", Sets.newHashSet(entities.values()), relations);
    }

    private Set<Relation> createPackageStructure(Map<String, Entity> lookup) {
        Set<Entity> entities = lookup.values().stream()
                .filter(e -> e.getType() == CLASS || e.getType() == INTERFACE || e.getType() == ENUM).collect(toSet());

        Node root = new Node("", "");
        for (Entity entity : entities) {
            String[] items = entity.getQName().split("/");
            Node current = root;
            String currentPath = "";
            for (int idx = 0; idx < items.length - 1; idx++) {
                String item = items[idx];
                if (idx > 0) {
                    currentPath += "/";
                }
                currentPath += item;
                current = current.getChild(currentPath, item);
            }
        }

        for (Node node : root.getChilds()) {
            Entity entity = new Entity(EntityType.PACKAGE, node.qname, node.name);
            lookup.put(node.qname, entity);
        }

        Set<Relation> rels = Sets.newHashSet();

        for (Entity entity : entities) {
            if (!EnumSet.of(CLASS, INTERFACE, ENUM, MODULE).contains(entity.getType())) {
                continue;
            }
            if (!entity.getQName().contains("$") && !entity.getQName().equals("module-info")) {
                String qname = entity.getQName();
                int lastSlash = qname.lastIndexOf('/');
                String pkgName = qname.substring(0, lastSlash);
                Entity pkg = lookup.get(pkgName);
                rels.add(new Relation(HAS_CLASS, pkg, entity));
            }
        }

        for (Node node : root.getChilds()) {
            Entity parentEntity = new Entity(EntityType.PACKAGE, node.qname, node.name);
            for (Node child : node.childs.values()) {
                Entity childEntity = lookup.get(child.qname);
                rels.add(new Relation(HAS_PACKAGE, parentEntity, childEntity));
            }
        }

        return rels;
    }

    private class Node {
        public String name;
        public String qname;
        public Map<String, Node> childs = Maps.newHashMap();

        private class Test {
            public void test() {

            }

        }

        public Node(String qname, String name) {
            this.name = name;
            this.qname = qname;
        }

        @Override
        public String toString() {
            return qname;
        }

        public Node getChild(String qname, String name) {
            Node child = childs.get(name);
            if (child == null) {
                child = new Node(qname, name);
                childs.put(name, child);
            }
            return child;
        }

        public Set<Node> getChilds() {
            Set<Node> nodes = Sets.newHashSet();
            nodes.addAll(childs.values());
            for (Node child : childs.values()) {
                nodes.addAll(child.getChilds());
            }
            return nodes;
        }
    }

}