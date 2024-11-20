package com.example.SERVER.domain.entity.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company_detail")
public class CompanyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_detail_id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_picture_link")
    private String profilePictureLink;

    @Column(name = "date_established")
    private Date dateEstablished;

    @Column(name = "about_us")
    private String aboutUs;

    @Column(name = "team_size")
    private String teamSize;

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

}
