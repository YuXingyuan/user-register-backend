package com.interview.userregister.repository;

import com.interview.userregister.repository.dao.RegisterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRegisterRepository extends JpaRepository<RegisterUser, UUID> {

}
