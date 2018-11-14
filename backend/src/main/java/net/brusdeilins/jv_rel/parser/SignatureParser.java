package net.brusdeilins.jv_rel.parser;

import java.util.Set;

import org.objectweb.asm.signature.SignatureVisitor;

import com.google.common.collect.Sets;

public class SignatureParser extends SignatureVisitor {
    private Set<String> classTypes = Sets.newHashSet();

    public SignatureParser(int api) {
        super(api);
    }

    public Set<String> getClassTypes() {
        return classTypes;
    }

    @Override
    public void visitFormalTypeParameter(String name) {
        super.visitFormalTypeParameter(name);
    }

    @Override
    public SignatureVisitor visitClassBound() {
        return super.visitClassBound();
    }

    @Override
    public SignatureVisitor visitInterfaceBound() {
        return super.visitInterfaceBound();
    }

    @Override
    public SignatureVisitor visitSuperclass() {
        return super.visitSuperclass();
    }

    @Override
    public SignatureVisitor visitInterface() {
        return super.visitInterface();
    }

    @Override
    public SignatureVisitor visitParameterType() {
        return super.visitParameterType();
    }

    @Override
    public SignatureVisitor visitReturnType() {
        return super.visitReturnType();
    }

    @Override
    public SignatureVisitor visitExceptionType() {
        return super.visitExceptionType();
    }

    @Override
    public void visitBaseType(char descriptor) {
        super.visitBaseType(descriptor);
    }

    @Override
    public void visitTypeVariable(String name) {
        super.visitTypeVariable(name);
    }

    @Override
    public SignatureVisitor visitArrayType() {
        return super.visitArrayType();
    }

    @Override
    public void visitClassType(String name) {
        classTypes.add(name);
        super.visitClassType(name);
    }

    @Override
    public void visitInnerClassType(String name) {
        classTypes.add(name);
        super.visitInnerClassType(name);
    }

    @Override
    public void visitTypeArgument() {
        super.visitTypeArgument();
    }

    @Override
    public SignatureVisitor visitTypeArgument(char wildcard) {
        return super.visitTypeArgument(wildcard);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

}
