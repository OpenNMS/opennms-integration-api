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

package ${package};

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.dao.EdgeDao;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.TopologyEdge;
import org.opennms.integration.api.v1.model.TopologyPort;
import org.opennms.integration.api.v1.model.TopologyProtocol;
import org.opennms.integration.api.v1.model.TopologySegment;
import ${package}.model.Link;
import ${package}.model.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopologyForwarder {
    private static final Logger LOG = LoggerFactory.getLogger(TopologyForwarder.class);

    private final ApiClient apiClient;
    private final EdgeDao edgeDao;

    public TopologyForwarder(ApiClient apiClient, EdgeDao edgeDao) {
        this.apiClient = Objects.requireNonNull(apiClient);
        this.edgeDao = Objects.requireNonNull(edgeDao);
    }

    public CompletableFuture<List<Topology>> forwardTopologies() {
        List<Topology> topologies = new LinkedList<>();
        List<CompletableFuture<?>> futures = new LinkedList<>();
        for (TopologyProtocol protocol : edgeDao.getProtocols()) {
            List<Link> links = new LinkedList<>();
            for (TopologyEdge edge : edgeDao.getEdges(protocol)) {
                Link link = new Link();
                edge.visitEndpoints(new TopologyEdge.EndpointVisitor() {
                    private String toLinkId(Node node) {
                        return "node-" + node.getId();
                    }

                    private String toLinkId(TopologyPort port) {
                        return "node-" + port.getNodeCriteria().getId() + "port-" + port.getIfName();
                    }

                    private String toLinkId(TopologySegment segment) {
                        return "segment-" + segment.getSegmentCriteria();
                    }

                    @Override
                    public void visitSource(Node node) {
                        link.setSource(toLinkId(node));
                    }

                    @Override
                    public void visitSource(TopologyPort port) {
                        link.setSource(toLinkId(port));
                    }

                    @Override
                    public void visitSource(TopologySegment segment) {
                        link.setSource(toLinkId(segment));
                    }

                    @Override
                    public void visitTarget(Node node) {
                        link.setTarget(toLinkId(node));
                    }

                    @Override
                    public void visitTarget(TopologyPort port) {
                        link.setTarget(toLinkId(port));
                    }

                    @Override
                    public void visitTarget(TopologySegment segment) {
                        link.setTarget(toLinkId(segment));
                    }
                });
                links.add(link);
            }
            LOG.info("Forwarding {} links for topology protocol: {}", links.size(), protocol);
            Topology topology = new Topology(protocol.name(), links);
            topologies.add(topology);
            futures.add(apiClient.forwardTopology(topology));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).thenApply(val -> topologies);
    }
}
