/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.qapi.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.api.VersionInfo;

import java.util.List;

/**
 * The QEmu version and capability information.
 *
 * This information is sent to the client on connection.
 *
 * @author shevek
 */
public class QApiGreeting extends QApiObject {

    public static class QMPVersion extends QApiObject {

        @JsonProperty
        public VersionInfo version;
        @JsonProperty
        public List<Object> capabilities;
    }
    @JsonProperty
    public QMPVersion QMP;
}
