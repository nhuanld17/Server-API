package com.example.SERVER.domain.entity.candidate;

import com.example.SERVER.domain.entity.company.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "candidate_wish_list")
public class CandidateWishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToMany
    @JoinTable(
        name = "wish_list",
        joinColumns = @JoinColumn(name = "wish_list_id"),
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> jobs;
}
