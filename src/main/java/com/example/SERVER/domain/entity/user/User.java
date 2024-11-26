package com.example.SERVER.domain.entity.user;

import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.company.Company;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;
	
	@Column(columnDefinition = "MEDIUMTEXT")
	private String refreshToken;

	@Column(name = "create_at", updatable = false)
	private Instant createAt;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference // Tránh lỗi vòng lặp vô hạn khi trả ra JSON
	private Candidate candidate; // @JsonManagedReference phải để ở bảng cha

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Company company;

	@PrePersist
	public void handleBeforePersist() {
		this.createAt = Instant.now();
	}
}