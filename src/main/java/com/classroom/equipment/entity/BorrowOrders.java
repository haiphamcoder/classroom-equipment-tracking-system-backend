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
@Entity(name = "borrow_orders")
public class BorrowOrders {
    @Id
    @Column(name = "order_id")
    @JsonProperty("order_id")
    private Long orderId;

    @Column(name = "borrower_id", nullable = false)
    @JsonProperty("borrower_id")
    private Long borrowerId;

    @ManyToOne
    @JoinColumn(name = "borrower_id", referencedColumnName = "borrower_id")
    private Borrowers borrower;

    @Column(name = "equipment_id", nullable = false)
    @JsonProperty("equipment_id")
    private Long equipmentId;

    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id")
    private Equipment equipment;

    @Column(name = "staff_id", nullable = false)
    @JsonProperty("staff_id")
    private Long staffId;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    private Staff staff;

    @Column(name = "borrow_time", nullable = false)
    @JsonProperty("borrow_time")
    private LocalDateTime borrowTime;

    @Column(name = "return_deadline")
    @JsonProperty("return_deadline")
    private LocalDateTime returnDeadline;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;
}
