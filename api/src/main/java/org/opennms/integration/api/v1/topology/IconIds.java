/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020-2020 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2020 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.v1.topology;

public interface IconIds {

    // Default Icons
    String Router = "router";
    String Switch = "switch";
    String Server = "server";
    String Database = "database";
    String Firewall = "firewall";
    String Default = "generic";
    String Generic = "generic";
    String Printer = "printer";
    String WifiAccess = "wifiAccess";
    String LinuxFileServer = "linux_file_server";
    String OpenNMSServer = "opennms_server";
    String Cloud = "cloud";

    // Atlas Icons (since 19.0.0)
    String MicrowaveBackhaul1 = "microwave_backhaul_1";
    String MicrowaveBackhaul2 = "microwave_backhaul_2";
    String Region1 = "region_1";
    String Region2 = "region_2";
    String Market1 = "market_1";
    String Market2 = "market_2";
    String Site1 = "site_1";
    String Site2 = "site_2";

    // BSM Icons
    String IpService = "IP_service";
    String BusinessService = "business_service";
    String ReductionKey = "reduction_key";

    // Sextant Icons
    String Situation = "situation";
    String Interface = "interface";

    // Vmware Icons
    interface Vmware {
        String Datacenter = "vmware-datacenter";
        String Cluster = "vmware-cluster";
        String Network = "vmware-network";
        String Datastore = "vmware-datastore";

        interface Hostsystem {
            String Unknown = "vmware-hostsystem-unknown";
            String On = "vmware-hostsystem-on";
            String Off = "vmware-hostsystem-off";
            String Standby = "vmware-hostsystem-standby";
        }

        interface Virtualmachine {
            String Unknown = "vmware-virtualmachine-unknown";
            String On = "vmware-virtualmachine-on";
            String Off = "vmware-virtualmachine-off";
            String Suspended = "vmware-virtualmachine-suspended";
        }
    }
}
