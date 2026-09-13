package com.ne.srm.auth.repositoty;

import com.ne.srm.auth.entity.UserRole;
import com.ne.srm.auth.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findByIdUserId(Long userId);


    void deleteByIdUserId(Long userId);

}
