package com.ne.srm.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@Table(name="sys_user_role")
public class UserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @EmbeddedId
    private UserRoleId id;


}
