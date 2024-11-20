package com.example.SERVER.repository.candidate;

import com.example.SERVER.domain.entity.candidate.LinkSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkSocialRepository extends JpaRepository<LinkSocial, Long> {

}
