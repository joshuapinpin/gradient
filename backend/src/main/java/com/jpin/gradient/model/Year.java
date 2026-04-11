package com.jpin.gradient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // only include id in equals and hashcode
public class Year {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "year", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // to avoid circular reference
    private List<Term> terms = new ArrayList<>();

    /**
     * Adds a term to this year and sets the year on the term.
     */
    public void addTerm(Term term){
        terms.add(term);
        term.setYear(this);
    }

    /**
     * Removes a term from this year and unsets the year on the term.
     */
    public void removeTerm(Term term){
        terms.remove(term);
        term.setYear(null);
    }
}
