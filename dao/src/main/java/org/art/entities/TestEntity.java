package org.art.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
    @GeneratedValue(generator = "ID_GENERATOR")
    @GenericGenerator(
            name = "ID_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @Parameter(
                            name = "sequence_name",
                            value = "TEST_SEQUENCE"
                    )
            }
    )
    @Column(name = "TEST_ENTITY_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;
}
