package net.brusdeilins.jv_rel.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class Functions {

    /*
     * public static final Function<String,List<Entity>> CLASSPATH_TO_ENTITY =
     * classPath -> { if (classPath.endsWith(".jar")) {
     * 
     * } };
     */

    public static final Function<String, Path> STRING_TO_PATH = classPath -> {
        return Paths.get(classPath);
    };

    public static final Function<InputStream, ClassNode> STREAM_TO_NODE = inputStream -> {
        try {
            ClassReader cr = new ClassReader(inputStream);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            return cn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    };

    public static final Function<Entity, Stream<Entity>> EXPLODE_METHODS = entity -> {
        return entity.explode();
    };

    public static final Function<ZipFile, List<InputStream>> ZIP_TO_STREAM = zipFile -> {
        List<InputStream> results = new ArrayList<InputStream>();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                try {
                    results.add(zipFile.getInputStream(entry));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    };

    public static final Function<InputStream, Entity> STREAM_TO_ENTITY = inputStream -> {
        try {
            ClassReader cr = new ClassReader(inputStream);
            ClassNode cn = new ClassNode(Opcodes.ASM6);
            cr.accept(cn, 0);
            return new Entity(cn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    };

    public static final Function<Path, List<InputStream>> PATH_TO_STREAM = path -> {
        final List<InputStream> ins = new ArrayList<InputStream>();
        try {
            if (path.toString().endsWith(".jar") || path.toString().endsWith(".war")) {
                ZipFile zipFile = new ZipFile(path.toString());
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class") && !entry.getName().equals("module-info.class")) {
                        try {
                            ins.add(zipFile.getInputStream(entry));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Files.walk(path).filter(Files::isRegularFile).forEach(item -> {
                    if (!item.toString().endsWith(".class")) {
                        return;
                    }
                    try {
                        ins.add(new FileInputStream(item.toFile()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ins;
    };

}
