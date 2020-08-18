#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2020 The OpenNMS Group, Inc.
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

package ${package}.shell;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import ${package}.TopologyForwarder;
import ${package}.model.Topology;

@Command(scope = "opennms-${pluginId}", name = "push-topology", description = "Push the topology")
@Service
public class TopologyCommand implements Action {

    @Reference
    private TopologyForwarder forwarder;

    @Override
    public Object execute() {
        System.out.println("Forwarding topologies...");
        final CompletableFuture<List<Topology>> future = forwarder.forwardTopologies();
        future.whenComplete((topologies,ex) -> {
            if (ex == null) {
                System.out.printf("Successfully forwarded %d topologies.", topologies.size());
            } else {
                System.out.println("Error occurred forwarding topologies: " + ex);
                ex.printStackTrace();
            }
        });
        try {
            future.get();
        } catch (Exception e) {
            // pass
        }
        return null;
    }
}
