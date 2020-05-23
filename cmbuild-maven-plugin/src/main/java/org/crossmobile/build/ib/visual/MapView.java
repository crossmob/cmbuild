/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.crossmobile.build.ib.visual;

import org.crossmobile.build.ib.Value;
import org.crossmobile.build.ib.Values;

public class MapView extends View {

    @Override
    protected void addSupported() {
        super.addSupported();
        addSupportedAttribute("maptype", new Value.Selections(new String[]{"standard", "satellite", "hyprid"}));
        addSupportedAttribute("showsUserLocation", Values.Boolean);
        addSupportedAttribute("zoomEnabled", Values.Boolean);
        addSupportedAttribute("scrollEnabled", Values.Boolean);
        addSupportedAttribute("rotateEnabled", Values.Boolean);
        addSupportedAttribute("pitchEnabled", Values.Boolean);

    }

    @Override
    public String getDefaultClassName() {
        return "crossmobile.ios.mapkit.MKMapView";
    }

    @Override
    public String toCode() {
        StringBuilder out = new StringBuilder(super.toCode());

        return out.toString();
    }

}
