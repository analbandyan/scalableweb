package com.waes.scalableweb.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "diff")
public class Diff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "offset", nullable = false)
    private Long offset;

    @ToString.Include
    @Column(name = "length", nullable = false)
    private Long length;

    @ManyToOne
    @JoinColumn(name = "diffsId", nullable = false)
    @ToString.Include
    private Diffs diffs;

}
