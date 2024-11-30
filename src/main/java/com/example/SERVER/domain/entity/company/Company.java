package com.example.SERVER.domain.entity.company;

import com.example.SERVER.domain.entity.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private int id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    private CompanyDetail companyDetail;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Job> jobs;
}
