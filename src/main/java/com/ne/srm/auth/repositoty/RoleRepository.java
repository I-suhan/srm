package com.ne.srm.auth.repositoty;

import com.ne.srm.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface RoleRepository
        extends JpaRepository<Role,Long> {


    Optional<Role> findByCode(
            String code
    );

}