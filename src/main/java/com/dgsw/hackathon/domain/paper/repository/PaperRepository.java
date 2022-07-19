package com.dgsw.hackathon.domain.paper.repository;

import com.dgsw.hackathon.domain.paper.entity.Paper;
import org.springframework.data.repository.CrudRepository;

public interface PaperRepository extends CrudRepository<Paper, Long> {

}
