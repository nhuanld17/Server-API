package com.example.SERVER.domain.entity.company;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    private Instant postAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Application> applications;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @PrePersist
    protected void handleBeforePersists() {
        postAt = Instant.now();
    }
    
    @Transient
    private boolean isActive;
    
    // Hàm kiếm tra bài đăng còn hiệu lực hay không
    public boolean isActive() {
        return getDaysUntilExpiration() > 0 ? true : false;
    }
    
    // Tính số ngày còn lại trước hạn ứng tuyển
    public long getDaysUntilExpiration() {
        
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationLocalDate = expirationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        
        return ChronoUnit.DAYS.between(currentDate, expirationLocalDate);
    }
    
    // Lấy số đơn ứng tuyển của 1 job
    public long getNumberOfApplications() {
        if (applications == null) {
            return 0;
        }
        
        return applications.size();
    }
}
