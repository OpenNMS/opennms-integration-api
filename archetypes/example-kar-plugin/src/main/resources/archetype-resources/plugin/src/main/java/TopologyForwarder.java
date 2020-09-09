#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
