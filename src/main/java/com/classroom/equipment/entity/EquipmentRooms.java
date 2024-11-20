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
@Entity(name = "equipment_rooms")
public class EquipmentRooms {
    @Id
    @Column(name = "room_id")
    @JsonProperty("room_id")
    private Long roomId;

    @Column(name = "room_name", nullable = false)
    @JsonProperty("room_name")
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false)
    private Buildings building;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "staff_id")
    private Staff manager;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;
}
