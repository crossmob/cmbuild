/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.plugin.model;

import org.crossmobile.bridge.ann.AssociationType;

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
    private Parameter java;
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
        this.java = java;
    }

    public Parameter getJavaParameter() {
        return java;
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

}
