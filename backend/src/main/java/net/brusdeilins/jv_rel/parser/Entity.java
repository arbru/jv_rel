package net.brusdeilins.jv_rel.parser;

import static net.brusdeilins.jv_rel.parser.EntityType.CLASS;
import static net.brusdeilins.jv_rel.parser.EntityType.ENUM;
import static net.brusdeilins.jv_rel.parser.EntityType.INTERFACE;
import static net.brusdeilins.jv_rel.parser.EntityType.LAMBDA;
import static net.brusdeilins.jv_rel.parser.EntityType.METHOD;
import static net.brusdeilins.jv_rel.parser.EntityType.MODULE;
import static net.brusdeilins.jv_rel.parser.Md5Generator.md5;
import static net.brusdeilins.jv_rel.parser.RelationType.COMPOSITION;
import static net.brusdeilins.jv_rel.parser.RelationType.HAS_CLASS;
import static net.brusdeilins.jv_rel.parser.RelationType.HAS_METHOD;
import static net.brusdeilins.jv_rel.parser.RelationType.IMPLEMENTS;
import static net.brusdeilins.jv_rel.parser.RelationType.INHERITANCE;
import static net.brusdeilins.jv_rel.parser.RelationType.INNER_CLASS;
import static net.brusdeilins.jv_rel.parser.RelationType.METHOD_CALL;
import static net.brusdeilins.jv_rel.parser.RelationType.REFERENCE;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Entity {
    private final Object asmNode;
    private final EntityType type;
    private final String id;
    private final String name;
    private final String qname;
    private final String displayName;
    private List<String> annotations = Lists.newArrayList();

    public Entity(EntityType type, String qname, String name) {
        asmNode = null;
        this.type = type;
        this.name = name;
        this.qname = qname;
        this.id = md5(this.qname);
        this.displayName = qname.replaceAll("/", ".");
    }

    public Entity(ClassNode classNode) {
        this.asmNode = classNode;
        if (classNode.visibleAnnotations != null) {
            for (AnnotationNode annotation : classNode.visibleAnnotations) {
                Type t = Type.getType(annotation.desc);
                annotations.add(t.getInternalName());
            }
        }
        if (classNode.invisibleAnnotations != null) {
            for (AnnotationNode annotation : classNode.invisibleAnnotations) {
                Type t = Type.getType(annotation.desc);
                annotations.add(t.getInternalName());
            }
        }
        int lastSlash = classNode.name.lastIndexOf("/");
        if ((classNode.access & Opcodes.ACC_MODULE) > 0) {
            this.name = classNode.module.name;
            this.qname = classNode.module.name;
            this.displayName = classNode.module.name.replaceAll("/", ".");
        } else {
            this.name = classNode.name.substring(lastSlash + 1);
            this.qname = classNode.name;
            this.displayName = classNode.name.replaceAll("/", ".");
        }
        this.id = md5(this.qname);
        if ((classNode.access & Opcodes.ACC_INTERFACE) > 0) {
            this.type = INTERFACE;
        } else if ((classNode.access & Opcodes.ACC_ENUM) > 0) {
            this.type = ENUM;
        } else if ((classNode.access & Opcodes.ACC_MODULE) > 0) {
            this.type = MODULE;
        } else {
            this.type = CLASS;
        }
    }

    public Entity(MethodNode methodNode, ClassNode parentNode) {
        this.asmNode = methodNode;
        this.name = methodNode.name;
        this.qname = parentNode.name + "/" + methodNode.name + methodNode.desc;
        this.displayName = calculateDisplayName(methodNode, parentNode);
        this.id = md5(this.qname);
        if ((methodNode.access & Opcodes.ACC_SYNTHETIC) > 0) {
            this.type = LAMBDA;
        } else {
            this.type = METHOD;
        }
        if (methodNode.visibleAnnotations != null) {
            for (AnnotationNode annotation : methodNode.visibleAnnotations) {
                Type t = Type.getType(annotation.desc);
                annotations.add(t.getInternalName());
            }
        }
        if (methodNode.invisibleAnnotations != null) {
            for (AnnotationNode annotation : methodNode.invisibleAnnotations) {
                Type t = Type.getType(annotation.desc);
                annotations.add(t.getInternalName());
            }
        }
    }

    private String calculateDisplayName(MethodNode methodNode, ClassNode parentNode) {
        StringBuffer sig = new StringBuffer();
        Type type = Type.getType(methodNode.desc);
        Type returnType = type.getReturnType();
        sig.append(returnType.getClassName());
        sig.append(" ");
        sig.append(parentNode.name.replaceAll("/", "."));
        sig.append("::");
        sig.append(methodNode.name);
        sig.append("(");
        Type[] args = type.getArgumentTypes();
        for (int idx = 0; idx < args.length; idx++) {
            sig.append(args[idx].getClassName());
            if (idx < args.length - 1) {
                sig.append(",");
            }

        }
        sig.append(")");
        return sig.toString();
    }

    public EntityType getType() {
        return type;
    }

    public Stream<Entity> explode() {
        Set<Entity> entities = Sets.newHashSet();
        if (type == CLASS || type == INTERFACE || type == ENUM) {
            entities.add(this);
            ClassNode cn = (ClassNode) asmNode;
            for (MethodNode mn : cn.methods) {
                entities.add(new Entity(mn, cn));
            }
        }
        return entities.stream();
    }

    public Stream<Relation> resolveRelations(Map<String, Entity> lookup) {
        Set<Relation> rels = Sets.newHashSet();
        if (asmNode instanceof ClassNode) {
            ClassNode classNode = (ClassNode) asmNode;
            if (classNode.outerClass != null) {
                if (classNode.outerMethod != null) {
                    // class defined in a method
                    Entity e = lookup
                            .get(classNode.outerClass + "/" + classNode.outerMethod + classNode.outerMethodDesc);
                    if (e != null) {
                        rels.add(new Relation(INNER_CLASS, e, this));
                    }
                } else {
                    // class defined in class
                    Entity e = lookup.get(classNode.outerClass);
                    if (e != null) {
                        rels.add(new Relation(INNER_CLASS, e, this));
                    }
                }
            }
            if (classNode.innerClasses != null) {
                for (InnerClassNode innerClass : classNode.innerClasses) {
                    if (innerClass.name.equals(classNode.name)) {
                        Entity e = lookup.get(innerClass.outerName);
                        if (e != null) {
                            rels.add(new Relation(INNER_CLASS, e, this));
                        }
                    }
                }
            }
            for (FieldNode field : classNode.fields) {
                String desc = Type.getType(field.desc).getInternalName();
                Entity e = lookup.get(desc);
                if (e != null) {
                    if (this != e) {
                        rels.add(new Relation(COMPOSITION, this, e));
                    }
                }
                if (field.signature != null && !field.signature.equals("TT;")) {
                    SignatureReader sr = new SignatureReader(field.signature);
                    SignatureParser sp = new SignatureParser(Opcodes.ASM6);
                    sr.accept(sp);
                    for (String classType : sp.getClassTypes()) {
                        e = lookup.get(classType);
                        if (e != null) {
                            rels.add(new Relation(REFERENCE, this, e));
                        }
                    }
                }
            }
            for (MethodNode methodNode : classNode.methods) {
                String qname = classNode.name + "/" + methodNode.name + methodNode.desc;
                Entity me = lookup.get(qname);
                if (me != null) {
                    rels.add(new Relation(HAS_METHOD, this, me));
                }
            }
            Entity superClass = lookup.get(classNode.superName);
            if (superClass != null) {
                rels.add(new Relation(INHERITANCE, this, superClass));
            }
            for (String ifn : classNode.interfaces) {
                Entity ifNode = lookup.get(ifn);
                if (ifNode != null) {
                    rels.add(new Relation(IMPLEMENTS, this, ifNode));
                    for (MethodNode methodNode : classNode.methods) {
                        String decl = ifNode.getQName() + "/" + methodNode.name + methodNode.desc;
                        String impl = qname + "/" + methodNode.name + methodNode.desc;
                        Entity declEntity = lookup.get(decl);
                        Entity implEntity = lookup.get(impl);
                        if (declEntity != null && implEntity != null) {
                            rels.add(new Relation(METHOD_CALL, declEntity, implEntity));
                        }
                    }
                }
            }

            if ((classNode.access & Opcodes.ACC_MODULE) == 0) {
                if (classNode.module != null) {
                    Entity module = lookup.get(classNode.module.name);
                    if (module != null) {
                        rels.add(new Relation(HAS_CLASS, module, this));
                    }
                }
            }

        } else if (type == METHOD || type == LAMBDA) {
            MethodNode methodNode = (MethodNode) asmNode;

            if (methodNode.signature != null) {
                SignatureReader sr = new SignatureReader(methodNode.signature);
                SignatureParser sp = new SignatureParser(Opcodes.ASM6);
                sr.accept(sp);
                for (String classType : sp.getClassTypes()) {
                    Entity e = lookup.get(classType);
                    if (e != null) {
                        rels.add(new Relation(REFERENCE, this, e));
                    }
                }
            }

            Type mt = Type.getType(methodNode.desc);
            Entity returnType = lookup.get(mt.getReturnType().getInternalName());
            if (returnType != null) {
                rels.add(new Relation(REFERENCE, this, returnType));
            }

            for (Type argType : mt.getArgumentTypes()) {
                Entity argEntity = lookup.get(argType.getInternalName());
                if (argEntity != null) {
                    rels.add(new Relation(REFERENCE, this, argEntity));
                }
            }

            if (methodNode.localVariables != null) {
                for (LocalVariableNode lvn : methodNode.localVariables) {
                    String id = Type.getType(lvn.desc).getInternalName();
                    Entity le = lookup.get(id);
                    if (le != null && !lvn.name.equals("this")) {
                        rels.add(new Relation(REFERENCE, this, le));
                    }
                }
            }
            for (AbstractInsnNode ins : methodNode.instructions.toArray()) {
                if (ins.getType() == AbstractInsnNode.TYPE_INSN) {
                    TypeInsnNode ti = (TypeInsnNode) ins;
                    Entity tc = lookup.get(ti.desc);
                    if (tc != null) {
                        rels.add(new Relation(REFERENCE, this, tc));
                    }
                }
                if (ins.getType() == AbstractInsnNode.FIELD_INSN) {
                    FieldInsnNode fi = (FieldInsnNode) ins;
                    Entity fc = lookup.get(fi.owner);
                    if (fc != null) {
                        rels.add(new Relation(REFERENCE, this, fc));
                    }
                }
                if (ins.getType() == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode mi = (MethodInsnNode) ins;
                    Entity calleeMethod = lookup.get(mi.owner + "/" + mi.name + mi.desc);
                    if (calleeMethod != null) {
                        rels.add(new Relation(METHOD_CALL, this, calleeMethod));
                    }
                    // check arguments
                    Type ct = Type.getType(mi.desc);
                    for (Type argType : ct.getArgumentTypes()) {
                        Entity argEntity = lookup.get(argType.getInternalName());
                        if (argEntity != null) {
                            rels.add(new Relation(REFERENCE, this, argEntity));
                        }
                    }
                }
            }
            // exceptions
            for (String exceptionClass : methodNode.exceptions) {
                Entity e = lookup.get(exceptionClass);
                if (e != null) {
                    rels.add(new Relation(REFERENCE, this, e));
                }
            }
        }
        return rels.stream();
    }

    public String getName() {
        return name;
    }

    public String getQName() {
        return qname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public boolean hasAnnotation(String annotation) {
        return annotations.contains(annotation);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return type + " " + qname + " [" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((qname == null) ? 0 : qname.hashCode());
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
        Entity other = (Entity) obj;
        if (qname == null) {
            if (other.qname != null)
                return false;
        } else if (!qname.equals(other.qname))
            return false;
        return true;
    }

    public static String accessToString(int acc) {
        StringBuffer sb = new StringBuffer();
        if ((acc & Opcodes.ACC_ABSTRACT) > 0) {
            sb.append("ACC_ABSTRACT ");
        }
        if ((acc & Opcodes.ACC_ANNOTATION) > 0) {
            sb.append("ACC_ANNOTATION ");
        }
        if ((acc & Opcodes.ACC_BRIDGE) > 0) {
            sb.append("ACC_BRIDGE ");
        }
        if ((acc & Opcodes.ACC_DEPRECATED) > 0) {
            sb.append("ACC_DEPRECATED ");
        }
        if ((acc & Opcodes.ACC_ENUM) > 0) {
            sb.append("ACC_ENUM ");
        }
        if ((acc & Opcodes.ACC_FINAL) > 0) {
            sb.append("ACC_FINAL ");
        }
        if ((acc & Opcodes.ACC_INTERFACE) > 0) {
            sb.append("ACC_INTERFACE ");
        }
        if ((acc & Opcodes.ACC_MANDATED) > 0) {
            sb.append("ACC_MANDATED ");
        }
        if ((acc & Opcodes.ACC_MODULE) > 0) {
            sb.append("ACC_MODULE ");
        }
        if ((acc & Opcodes.ACC_NATIVE) > 0) {
            sb.append("ACC_NATIVE ");
        }
        if ((acc & Opcodes.ACC_OPEN) > 0) {
            sb.append("ACC_OPEN ");
        }
        if ((acc & Opcodes.ACC_PRIVATE) > 0) {
            sb.append("ACC_PRIVATE ");
        }
        if ((acc & Opcodes.ACC_PROTECTED) > 0) {
            sb.append("ACC_PROTECTED ");
        }
        if ((acc & Opcodes.ACC_PUBLIC) > 0) {
            sb.append("ACC_PUBLIC ");
        }
        if ((acc & Opcodes.ACC_STATIC) > 0) {
            sb.append("ACC_STATIC ");
        }
        if ((acc & Opcodes.ACC_STATIC_PHASE) > 0) {
            sb.append("ACC_STATIC_PHASE ");
        }
        if ((acc & Opcodes.ACC_STRICT) > 0) {
            sb.append("ACC_STRICT ");
        }
        if ((acc & Opcodes.ACC_SUPER) > 0) {
            sb.append("ACC_SUPER ");
        }
        if ((acc & Opcodes.ACC_SYNCHRONIZED) > 0) {
            sb.append("ACC_SYNCHRONIZED ");
        }
        if ((acc & Opcodes.ACC_SYNTHETIC) > 0) {
            sb.append("ACC_SYNTHETIC ");
        }
        if ((acc & Opcodes.ACC_TRANSIENT) > 0) {
            sb.append("ACC_TRANSIENT ");
        }
        if ((acc & Opcodes.ACC_TRANSITIVE) > 0) {
            sb.append("ACC_TRANSITIVE ");
        }
        if ((acc & Opcodes.ACC_VARARGS) > 0) {
            sb.append("ACC_VARARGS ");
        }
        if ((acc & Opcodes.ACC_VOLATILE) > 0) {
            sb.append("ACC_VOLATILE ");
        }
        return sb.toString();
    }
}