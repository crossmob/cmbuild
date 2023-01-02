/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.model;

import org.crossmobile.bridge.ann.AssociationType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class NParam {

    private NSelector container;
    private NType type;
    private String name = "";
    private String varname;
    private AssociationType assoc = AssociationType.DEFAULT;
    private boolean shouldNotBeNull;
    private boolean transferName = false;
    /**
     * Use this parameter to bundle parameters together, like when SEL+target is
     * used
     */
    private NParamAffiliation affiliation;
    private JParameter java;
    private StaticMappingType staticMapping = StaticMappingType.NONE;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }

    public String getVarname() {
        return varname;
    }

    public void setNType(NType type) {
        this.type = type;
    }

    public NType getNType() {
        return type;
    }

    public void setContainer(NSelector container) {
        this.container = container;
    }

    public NSelector getContainer() {
        return container;
    }

    public void affiliateTo(NParam strongParam, NParamAffiliation.Type type) {
        this.affiliation = new NParamAffiliation(strongParam, type);
    }

    public NParamAffiliation getAffiliation() {
        return affiliation;
    }

    public void setJavaParameter(Parameter java) {
        this.java = new JParameter(java);
    }

    public void setJavaParameter(Class<?> type, Annotation... declaredAnnotations) {
        this.java = new JParameter(type, declaredAnnotations);
    }

    public Class<?> getJavaType() {
        return java == null ? null : java.getType();
    }

    public <T extends Annotation> T getJavaDeclaredAnnotation(Class<T> annotation) {
        return java == null ? null : java.getDeclaredAnnotation(annotation);
    }

    public void setStaticMapping(StaticMappingType staticMapping) {
        Objects.requireNonNull(staticMapping);
        this.staticMapping = staticMapping;
    }

    public StaticMappingType getStaticMapping() {
        return staticMapping;
    }

    public void setAssociation(AssociationType associationType) {
        this.assoc = associationType;
    }

    public AssociationType getAssociation() {
        return assoc;
    }

    public void setShouldNotBeNull(boolean shouldNotBeNull) {
        this.shouldNotBeNull = shouldNotBeNull;
    }

    public boolean shouldNotBeNull() {
        return shouldNotBeNull;
    }

    public void setTransferName(boolean transferName) {
        this.transferName = transferName;
    }

    public boolean isTransferName() {
        return transferName;
    }

    @Override
    public String toString() {
        return name + ":" + varname;
    }

    private static final class JParameter {
        private final Class<?> type;
        private final Annotation[] declaredAnnotations;

        private JParameter(Class<?> type, Annotation... declaredAnnotations) {
            this.type = type;
            this.declaredAnnotations = declaredAnnotations;
        }

        private JParameter(Parameter param) {
            type = param.getType();
            declaredAnnotations = param.getDeclaredAnnotations();
        }

        private Class<?> getType() {
            return type;
        }

        private <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
            return getAnnotation(annotationClass);
        }

        private <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            if (declaredAnnotations != null)
                for (Annotation ann : declaredAnnotations)
                    if (annotationClass.isAssignableFrom(ann.getClass()))
                        return annotationClass.cast(ann);
            return null;
        }
    }
}
