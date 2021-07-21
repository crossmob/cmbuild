/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.utils;

import org.crossmobile.plugin.model.NParam;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.utils.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Collectors {

    public static List<NParam> getJoinedParams(NSelector sel) {
        List<NParam> types = new ArrayList<>();
        for (NParam param : sel.getParams())
            if (param.getAffiliation() == null)
                types.add(param);
        return types;
    }

    public static List<Class<?>> getListOfTypes(Collection<NParam> params) {
        List<Class<?>> classes = new ArrayList<>();
        for (NParam param : params)
            classes.add(param.getNType().getType());
        return classes;
    }

    public static List<Method> getAbstractMethods(Class<?> c) {
        List<Method> methods = new ArrayList<>();
        for (Method m : c.getDeclaredMethods())
            if (Modifier.isAbstract(m.getModifiers()))
                methods.add(m);
        return methods;
    }

    public static Collection<Field> getNonPrimitivePublicFields(Class<?> c) {
        Collection<Field> fields = new ArrayList<>();
        for (Field f : c.getFields())
            if (!f.getType().isPrimitive())
                fields.add(f);
        return fields;
    }

    public static NParam findParamNamed(List<NParam> parameters, String varname, String errorContext) {
        if (varname.isEmpty())
            return null;
        for (NParam param : parameters)
            if (param.getVarname().equals(varname))
                return param;
        Log.error("Unable to locate parameter named " + varname + " in " + errorContext);
        return null;
    }

    public static List<String> findMostPopularNames(Map<String, Integer> names) {
        List<String> pop = new ArrayList<>();
        int last = -1;
        for (String key : names.keySet()) {
            int current = names.get(key);
            if (current == last)
                pop.add(key);
            else if (current > last) {
                last = current;
                pop.clear();
                pop.add(key);
            }
        }
        return pop;
    }
}
