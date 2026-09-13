package com.ne.srm.auth.repositoty;

import com.ne.srm.auth.entity.RolePermission;
import com.ne.srm.auth.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findByIdRoleId(Long roleId);


}
