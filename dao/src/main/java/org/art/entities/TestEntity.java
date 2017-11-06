package org.art.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Dummy entity for Hibernate quick start test
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class TestEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;
}
