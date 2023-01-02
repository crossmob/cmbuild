/*
 * (c) 2023 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc;

import org.crossmobile.plugin.model.NSelector;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.utils.FileUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReverseImportRegistry {
    private final Map<String, Map<Class<?>, HandlerData>> registry = new HashMap<>();   // plugin, object, ...
    private final Registry reg;

    public ReverseImportRegistry(Registry reg) {
        this.reg = reg;
    }

    public Collection<String> getReverseImports(Class<?> containerObject, String execSignature) {
        String plugin = reg.plugins().getPlugin(containerObject.getName());
        HandlerData handlerData = registry.getOrDefault(plugin, Collections.emptyMap()).getOrDefault(containerObject, null);
        return handlerData == null ? Collections.emptyList() : handlerData.getImports(execSignature);
    }

    public String requestRandomClass(Class<?> containerObject, String execSignature, NSelector block) {
        String plugin = reg.plugins().getPlugin(containerObject.getName());
        HandlerData objectRegistry = registry.computeIfAbsent(plugin, p -> new HashMap<>()).computeIfAbsent(containerObject, c -> new HandlerData());
        Collection<ReverseBlockHandler> execRegistry = objectRegistry.methodImports.computeIfAbsent(execSignature, e -> new ArrayList<>());
        String className = "CM_Block_Reverse_" + plugin + "_" + objectRegistry.counter.getAndIncrement();
        execRegistry.add(new ReverseBlockHandler(className, containerObject, block));
        return className;
    }

    public void saveTo(Function<String, File> targetFinder) {
        for (String plugin : registry.keySet()) {
            File target = targetFinder.apply(plugin);
            Map<Class<?>, HandlerData> pluginData = registry.get(plugin);
            for (Class<?> containerObject : pluginData.keySet()) {
                HandlerData objectData = pluginData.get(containerObject);
                for (String methodSignature : objectData.methodImports.keySet())
                    for (ReverseBlockHandler blockData : objectData.methodImports.get(methodSignature)) {
                        FileUtils.writeIfDiffers(new File(target, blockData.className + ".h"), blockData.importData);
                        FileUtils.writeIfDiffers(new File(target, blockData.className + ".m"), blockData.bodyData);
                    }
            }
        }
    }

    private static class HandlerData {
        private final Map<String, Collection<ReverseBlockHandler>> methodImports = new HashMap<>();
        private AtomicInteger counter = new AtomicInteger();

        private Collection<String> getImports(String methodSignature) {
            return methodImports.getOrDefault(methodSignature, Collections.emptyList()).stream().map(a -> a.className).collect(Collectors.toList());
        }
    }
}
