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
@Entity(name = "staff")
public class Staff {
    @Id
    @Column(name = "staff_id")
    @JsonProperty("staff_id")
    private Long staffId;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false)
    @JsonProperty("building_id")
    private Buildings buildingId;

    @Column(name = "email", nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(name = "phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name = "admin")
    @JsonProperty("admin")
    private boolean admin;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;
}
