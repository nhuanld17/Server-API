package com.example.SERVER.domain.entity.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "tags")
    private String tags;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "education")
    private String education;

    @Column(name = "experience")
    private String experience;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "job_level")
    private String jobLevel;

    @Column(name = "description")
    private String description;

    @Column(name = "responesibility")
    private String responesibility;

    @Column(name = "post_at")
    private String postAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Application> applications;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
