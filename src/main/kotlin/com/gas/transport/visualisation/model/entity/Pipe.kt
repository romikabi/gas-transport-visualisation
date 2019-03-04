package com.gas.transport.visualisation.model.entity

import javax.persistence.*

@Entity
@Table(name = "gtv_pipe")
class Pipe : AbstractEntity() {
    @Column(name = "capacity")
    var capacity: Double? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    var destination: Node? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    var source: Node? = null
}