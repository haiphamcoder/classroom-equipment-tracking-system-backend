package com.classroom.equipment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity(name = "return_records")
public class ReturnRecords {
    @Id
    @Column(name = "record_id")
    @JsonProperty("record_id")
    private Long recordId;

    @Column(name = "order_id", nullable = false)
    @JsonProperty("order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private BorrowOrders order;

    @Column(name = "staff_id", nullable = false)
    @JsonProperty("staff_id")
    private Long staffId;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    private Staff staff;

    @Column(name = "return_time", nullable = false)
    @JsonProperty("return_time")
    private LocalDateTime returnTime;

    @Column(name = "equipment_status", nullable = false)
    @JsonProperty("equipment_status")
    private int equipmentStatus;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;
}
