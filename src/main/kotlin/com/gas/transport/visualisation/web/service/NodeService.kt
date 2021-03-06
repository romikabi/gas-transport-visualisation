package com.gas.transport.visualisation.web.service

import com.gas.transport.visualisation.model.dao.NodeDao
import com.gas.transport.visualisation.model.dao.criteria.*
import com.gas.transport.visualisation.model.entity.Node
import com.gas.transport.visualisation.web.dto.NodeAvailableFiltersDto
import com.gas.transport.visualisation.web.dto.NodeDto
import com.gas.transport.visualisation.web.dto.filter.NodeFilterDto
import com.gas.transport.visualisation.web.dto.toNodeDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class NodeService {

    @Autowired
    lateinit var nodeDao: NodeDao

    @Autowired
    lateinit var nodeCustomDao: NodeCustomDao

    fun getAllUnpreparedNodes(): MutableIterable<Node> = nodeDao.findAll()

    fun getAllNodes() = getAllUnpreparedNodes().map { node -> node.toNodeDto() }

    fun getUnpreparedFilteredNodes(filter: NodeFilterDto): List<Node> {
        var specification = hasNodeYear(filter.year).and(hasNodeSnapshot(filter.snapshotId))
        if (filter.region != null) {
            var regionSpecification: Specification<Node>? = null
            filter.region.forEach { region ->
                regionSpecification = if (regionSpecification == null)
                    hasRegion(region)
                else
                    regionSpecification!!.or(hasRegion(region))
            }
            specification = specification.and(regionSpecification!!)
        }

        if (filter.type != null) {
            var typeSpecification: Specification<Node>? = null
            filter.type.forEach { type ->
                typeSpecification = if (typeSpecification == null)
                    hasNodeType(type)
                else
                    typeSpecification!!.or(hasNodeType(type))
            }
            specification = specification.and(typeSpecification!!)
        }
        return nodeCustomDao.findAll(specification)
    }

    fun getFilteredNodes(filter: NodeFilterDto): List<NodeDto> = getUnpreparedFilteredNodes(filter).map { node -> node.toNodeDto() }

    fun availableFilters(): NodeAvailableFiltersDto {
        val nodes = getAllUnpreparedNodes()
        val snapshots = nodes.mapNotNull { node -> node.snapshotId }.distinct()
        val regions = nodes.mapNotNull { node -> node.region }.distinct()
        val years = nodes.mapNotNull { node -> node.year }.distinct()
        val types = nodes.mapNotNull { node -> node.type }.distinct()
        return NodeAvailableFiltersDto(snapshots, regions, years, types)
    }
}