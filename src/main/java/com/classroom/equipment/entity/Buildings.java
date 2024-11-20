package com.classroom.equipment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity(name = "buildings")
public class Buildings {
    @Id
    @Column(name = "building_id")
    @JsonProperty("building_id")
    private Long buildingId;

    @Column(name = "building_name", nullable = false)
    @JsonProperty("building_name")
    private String buildingName;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;
}
