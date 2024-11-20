package com.example.SERVER.domain.entity.candidate;

import com.example.SERVER.domain.entity.company.Application;
import com.example.SERVER.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "tittle")
    private String title;

    @Column(name = "portfolio")
    private String portfolio;

    @Column(name = "resume_link")
    private String resumeLink;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    private CandidateDetail candidateDetail;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private LinkSocial linkSocial;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Application application;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private CandidateWishList candidateWishList;
}