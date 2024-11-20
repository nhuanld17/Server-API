package com.example.SERVER.repository.candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateWishListRepository extends JpaRepository<CandidateWishListRepository, Long> {

}
