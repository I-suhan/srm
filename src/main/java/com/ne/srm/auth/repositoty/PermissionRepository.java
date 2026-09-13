package com.ne.srm.auth.repositoty;

import com.ne.srm.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface PermissionRepository extends JpaRepository<Permission,Long> {


    Optional<Permission> findByCode(
            String code
    );

}
