/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.objc;

import org.crossmobile.plugin.model.NObject;
import org.crossmobile.plugin.model.NStructField;
import org.crossmobile.plugin.reg.ObjectRegistry;
import org.crossmobile.plugin.reg.Registry;
import org.crossmobile.plugin.utils.Streamer;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.crossmobile.utils.NamingUtils.toObjC;
import static org.crossmobile.plugin.utils.Texters.toObjCType;
import static org.crossmobile.utils.NamingUtils.getClassNameSimple;

public class StructConstructorParser {

    public static void helperMethods(NObject nobj, Registry reg, Streamer out) throws IOException {
        String structName = getClassNameSimple(nobj.getType());
        Collection<NStructField> primaryFields = nobj.getStructFields().stream().filter(n -> !n.type.isPrimitive()).collect(Collectors.toList());

        if (!primaryFields.isEmpty()) {
            // Custom alloc, to allocate object space
            out.append("+ (id) alloc\n{\n").tab();
            out.append(toObjCType(nobj.getType())).append(" obj = [super alloc];\n");
            for (NStructField f : primaryFields)
                out.append("obj->").append(f.objc_name).append(" = ").append("[").append(toObjC(f.type)).append(" alloc];\n");
            out.append("return obj;\n").untab().append("}\n\n");

            // Custom deallocator
            out.append("- (void) dealloc\n{\n").tab();
            for (NStructField f : primaryFields)
                out.append("[").append(f.objc_name).append(" release];\n");
            out.append("[super dealloc];\n").untab().append("}\n\n");
        }

        // Initialize from native
        out.append("- (instancetype) initWith").append(structName).append(":(").append(structName).append(") other\n{\n").tab();
        out.append("self = [super init];\n");
        emitConverters(nobj, "self->", "other.", true, reg, out);
        out.append("return self;\n").untab();
        out.append("}\n\n");

        // From native to wrapped
        out.append("- (void) set").append(structName).append(":(").append(structName).append(") other\n{\n").tab();
        emitConverters(nobj, "self->", "other.", true, reg, out);
        out.untab().append("}\n\n");

        // From wrapped to native
        out.append("- (").append(structName).append(") get").append(structName).append("\n{\n").tab();
        out.append(structName).append(" result;\n");
        emitConverters(nobj, "self->", "result.", false, reg, out);
        out.append("return result;\n").untab();
        out.append("}\n\n");
    }

    private static void emitConverters(NObject nobj, String self, String other, boolean toCurrent, Registry reg, Streamer out) throws IOException {
        for (NStructField f : nobj.getStructFields())
            if (f.type.isPrimitive()) {
                String selfRef = self + f.objc_name;
                String otherRef = other + f.name;
                if (toCurrent)
                    out.append(selfRef).append(" = ").append(otherRef).append(";\n");
                else
                    out.append(otherRef).append(" = ").append(selfRef).append(";\n");
            } else
                emitConverters(reg.objects().retrieve(f.type), self + f.objc_name + "->", other + f.name + ".", toCurrent, reg, out);
    }
}
