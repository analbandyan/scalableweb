package com.waes.scalableweb.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "diffs")
public class Diffs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    //    @NaturalId
    @Column(name = "natural_id", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String naturalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "diff_status")
    private DiffStatus diffStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_status")
    private CalculationStatus calculationStatus;

    @OneToMany(mappedBy = "diffs", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Diff> diffs;

    public void setDiffs(Set<Diff> diffs) {
        diffs.forEach(diff -> diff.setDiffs(this));
        this.diffs = diffs;
    }


}
