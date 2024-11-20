package com.example.SERVER.domain.entity.candidate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "link_social")
public class LinkSocial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_id")
    private int id;

    @Column(name = "fb_link")
    private String fbLink;

    @Column(name = "twitter_link")
    private String twitter_link;

    @Column(name = "linked_link")
    private String linked_link;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

}
