package org.crossmobile.plugin.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class JParameter {
    private final Class<?> type;
    private final Annotation[] declaredAnnotations;

    public JParameter(Class<?> type, Annotation... declaredAnnotations) {
        this.type = type;
        this.declaredAnnotations = declaredAnnotations;
    }

    public JParameter(Parameter param) {
        type = param.getType();
        declaredAnnotations = param.getDeclaredAnnotations();
    }

    public Class<?> getType() {
        return type;
    }

    public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
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
