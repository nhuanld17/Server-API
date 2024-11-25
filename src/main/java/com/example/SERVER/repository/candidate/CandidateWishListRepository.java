package com.example.SERVER.repository.candidate;

import com.example.SERVER.domain.entity.candidate.CandidateWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateWishListRepository extends JpaRepository<CandidateWishList, Long> {

}
