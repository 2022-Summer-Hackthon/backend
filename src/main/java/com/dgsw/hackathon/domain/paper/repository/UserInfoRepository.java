package com.dgsw.hackathon.domain.paper.repository;

import com.dgsw.hackathon.domain.paper.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

}
