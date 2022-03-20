/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.model;

import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.crossmobile.plugin.reg.TypeRegistry.getJavaBoxed;
import static org.crossmobile.plugin.reg.TypeRegistry.isBlockParameterSupported;
import static org.crossmobile.utils.CollectionUtils.forEach;
import static org.crossmobile.utils.NamingUtils.execSignature;
import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.utils.ReflectionUtils.appearsInParent;

public class NSelector extends NParsable implements Comparable<NSelector> {

    /* Function info */
    private NType returnType;
    private String name;
    private final List<NParam> params = new ArrayList<>();
    private Executable java;
    private Class javaReturn;
    private boolean constructor;
    private boolean fakeConstructor;
    private MethodType methodType = MethodType.SELECTOR;

    /* Selector info */
    private NObject container;
    private NProperty property;
    private boolean asStatic;
    private NStructField structRef;
    private int sinceIosMajor = 0;
    private int sinceIosMinor = 0;

    /* Function info */
    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public boolean isConstructor() {
        return constructor;
    }

    public void setReturnType(NType returnType) {
        this.returnType = returnType;
    }

    public NType getReturnType() {
        return returnType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getObjCSignature() {
        StringBuilder out = new StringBuilder();
        out.append(name);
        forEach(params)
                .onFront(n -> out.append(n.getName()).append(":"))
                .go();
        return out.toString();
    }

    private String getJavaSignature() {
        return execSignature(java, false);
    }

    public void addParam(NParam param) {
        if (param.getNType().getType().equals(void.class))
            return;
        if (param.getVarname().isEmpty())
            param.setVarname("_unnamed_parameter_" + params.size());
        params.add(param);
        param.setContainer(this);
    }

    public List<NParam> getParams() {
        return params;
    }

    public Collection<String> getDependencies() {
        return getDependencies(false);
    }

    private Collection<String> getDependencies(boolean forceBoxed) {
        Set<String> deps = new HashSet<>();
        addType(deps, javaReturn, returnType.getType(), forceBoxed);
        for (NParam param : params) {
            addType(deps, param.getJavaType() == null ? param.getNType().getType() : param.getJavaType(), param.getNType().getType(), forceBoxed);
            if (param.getNType().getBlock() != null)
                deps.addAll(param.getNType().getBlock().getDependencies(true));
        }
        return deps;
    }

    private void addType(Set<String> deps, Class<?> javaType, Class<?> nativeType, boolean forceBoxed) {
        if (javaType.equals(void.class))
            return;
        if (forceBoxed)
            javaType = getJavaBoxed(nativeType);
        if (!javaType.isPrimitive() && !javaType.isArray())
            deps.add(toObjC(javaType));
    }

    public void setJavaExecutable(Executable javaMethod, Class<?> javaReturn) {
        this.java = javaMethod;
        this.javaReturn = javaReturn;
    }

    public Executable getJavaExecutable() {
        return java;
    }

    public Class getJavaReturn() {
        return javaReturn;
    }

    public void setMethodType(MethodType methodType) {
        Objects.requireNonNull(methodType);
        this.methodType = methodType;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    /* Selector info */
    public void setContainer(NObject container) {
        this.container = container;
    }

    public NObject getContainer() {
        return container;
    }

    public void setStatic(boolean asStatic) {
        this.asStatic = asStatic;
    }

    public boolean isStatic() {
        return asStatic;
    }

    public boolean needsOverrideBindings() {
        return !asStatic && !isConstructor() && !Modifier.isFinal(java.getModifiers()) && !Modifier.isStatic(java.getModifiers())
                && isBlockParameterSupported(this);
    }

    public void setProperty(NProperty property) {
        this.property = property;
    }

    public NProperty getProperty() {
        return property;
    }

    public boolean isSetter() {
        return property != null && getName().startsWith("set");
    }

    public boolean isGetter() {
        return property != null && !getName().startsWith("set");
    }

    public boolean setSinceIos(String sinceIos) {
        if (sinceIos.isEmpty())
            return true;
        String[] parts = sinceIos.split("\\.");
        if (parts.length > 2 || parts.length == 0) {
            return false;
        }
        try {
            this.sinceIosMinor = parts.length == 1 ? 0 : Integer.parseInt(parts[1]);
            this.sinceIosMajor = Integer.parseInt(parts[0]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean requireIosVersion() {
        return sinceIosMajor > 0;
    }

    public int getSinceIosMajor() {
        return sinceIosMajor;
    }

    public int getSinceIosMinor() {
        return sinceIosMinor;
    }

    @Override
    public int compareTo(NSelector other) {
        if (Modifier.isStatic(this.java.getModifiers()) != Modifier.isStatic(other.java.getModifiers()))
            return Modifier.isStatic(this.java.getModifiers()) ? -1 : 1;
        if (this.isConstructor() != other.isConstructor())
            return this.isConstructor() ? -1 : 1;
        if (this.property != null && other.property != null) {
            if (this.property.getName().equals(other.property.getName())) {
                if (this.isSetter() == other.isSetter())
                    return getJavaSignature().compareTo(other.getJavaSignature());
                return this.isSetter() ? -1 : 1;
            }
            return this.property.getName().compareTo(other.property.getName());
        } else if (this.property != null)
            return -1;
        else if (other.property != null)
            return 1;
        return getJavaSignature().compareTo(other.getJavaSignature());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.getJavaExecutable());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NSelector other = (NSelector) obj;
        if (!this.java.getName().equals(other.java.getName()))
            return false;
        return getJavaSignature().equals(other.getJavaSignature());
    }

    public String getFamily() {
        return isGetter() ? "getter" : (isSetter() ? "setter" : (isConstructor() ? "constructor" : methodType.name().toLowerCase()));
    }

    public boolean isInherited() {
        return appearsInParent(java) != java.getDeclaringClass();
    }

    public void removeLastParam() {
        if (!params.isEmpty())
            params.remove(params.size() - 1);
    }

    public void setFakeConstructor(boolean fakeConstructor) {
        this.fakeConstructor = fakeConstructor;
    }

    /**
     * A static method that works as constructor: usually this appears ONLY in NSString
     */
    public boolean isFakeConstructor() {
        return fakeConstructor;
    }

    @Override
    public String toString() {
        return getJavaSignature();
    }

    /**
     * In case this is a struct reference method, set here the corresponding references
     *
     * @param structRef
     */
    public void setStructRef(NStructField structRef) {
        this.structRef = structRef;
    }

    public NStructField getStructRef() {
        return structRef;
    }
}
