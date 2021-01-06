/*
 * (c) 2021 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.reg;

import javassist.ClassPool;
import org.crossmobile.bridge.system.BaseUtils;
import org.crossmobile.bridge.system.JsonHelper;
import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.model.NType;
import org.crossmobile.plugin.objc.SelectorEmitter;
import org.crossmobile.plugin.objc.SelectorEmitterReverse;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.Streamer;
import org.crossmobile.plugin.utils.Texters;
import org.crossmobile.utils.CustomTypeClasses;
import org.crossmobile.utils.ReverseCodeCollection;

import java.io.IOException;
import java.util.*;

import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.utils.ReflectionUtils.getBareClass;

public final class ReverseCode {

    private final Map<String, Factory> entries = new TreeMap<>();
    private final ClassPool cp;
    private final Registry reg;

    public ReverseCode(ClassPool cp, Registry reg) {
        this.cp = cp;
        this.reg = reg;
    }

    public void produce() {
        for (NObject obj : reg.objects().retrieveAll())
            if (obj.needsOverrideBindings()) {
                String plugin = reg.plugins().getPlugin(obj.getType().getName());
                entries.computeIfAbsent(plugin, p -> new Factory()).attachObject(obj);
            }
    }

    public String toString(String plugin) {
        Factory reverse = entries.get(plugin);
        return reverse == null ? "{}" : reverse.toString();
    }

    public Iterable<String> getListOfClasses(String plugin) {
        Factory reverse = entries.get(plugin);
        return reverse == null ? Collections.emptyList() : reverse.codeCollection.getClassMethodCode().keySet();
    }

    private class Factory {

        private final ReverseCodeCollection codeCollection = new ReverseCodeCollection(cp);

        private void attachObject(final NObject obj) {
            NObject current = obj;
            Collection<NSelector> selectors = new TreeSet<>();
            while (current != null) {
                current.getSelectors().stream().filter(NSelector::needsOverrideBindings).forEach(selectors::add);
                current = reg.objects().retrieve(current.getType().getSuperclass());
            }
            for (Class<?> inter : obj.getType().getInterfaces()) {
                NObject interClass = reg.objects().retrieve(inter);
                if (interClass != null)
                    selectors.addAll(interClass.getSelectors());
            }

            for (NSelector sel : selectors)
                attachSelector(sel, obj, sel.getContainer().isProtocol() || sel.getContainer() == obj);
        }

        private void attachSelector(NSelector sel, NObject obj, boolean shouldEmitMethods) {
            try {
                String signature = Texters.getSelectorSignature(sel);
                Streamer reverseCode = Streamer.asString();
                Streamer superCode = Streamer.asString();
                Collection<String> reverseImports = Collections.emptyList();
                Collection<String> superImports = Collections.emptyList();
                if (shouldEmitMethods) {
                    new SelectorEmitterReverse(sel, reg).emitImplementation(reverseCode);
                    reverseImports = reg.imports().getReverseImports(obj.getType(), signature);
                    new SelectorEmitter(sel, reg, "super").emitImplementation(superCode);
                    superImports = getSuperImports(sel);
                }
                codeCollection.addFromSource(signature, obj.getType()
                        , reverseCode.toString(), superCode.toString()
                        , reverseImports, superImports);
            } catch (IOException ignored) {
            }
        }

        private Collection<String> getSuperImports(NSelector sel) {
            Collection<String> imports = new TreeSet<>();
            sel.getParams().stream().map(p -> getIncludeName(p.getNType())).filter(Objects::nonNull).forEach(imports::add);
            String returnName = getIncludeName(sel.getReturnType());
            if (returnName != null)
                imports.add(returnName);
            return imports;
        }

        private String getIncludeName(NType type) {
            Class<?> cls = getBareClass(type.getType());
            if (cls.isPrimitive() || CustomTypeClasses.VoidRef.class == cls)
                return null;
            return toObjC(cls);
        }

        @Override
        public String toString() {
            try {
                return JsonHelper.encode(codeCollection.getClassMethodCode(), true);
            } catch (Exception ex) {
                BaseUtils.throwException(ex);
                return "";
            }
        }
    }
}
