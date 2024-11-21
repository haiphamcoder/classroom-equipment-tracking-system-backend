package com.classroom.equipment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity(name = "equipment")
public class Equipment {
    @Id
    @Column(name = "equipment_id")
    @JsonProperty("equipment_id")
    private Long equipmentId;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private EquipmentRoom room;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;

    @Column(name = "quantity", nullable = false)
    @JsonProperty("quantity")
    private int quantity;
}
