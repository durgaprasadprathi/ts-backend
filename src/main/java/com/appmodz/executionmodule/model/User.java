package com.appmodz.executionmodule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@lombok.Getter
@lombok.Setter
@lombok.ToString
@NoArgsConstructor
@Table(name="Users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="User_Id")
    private long userId;

    @Column(name="User_First_Name")
    private String userFirstName;

    @Column(name="User_Last_Name")
    private String userLastName;

    @Column(name="Username")
    private String userName;

    @JsonIgnore
    @Column(name="User_Password_Hash")
    private String userPasswordHash;

    @ManyToOne
    @JoinColumn(name="User_Org_Id")
    Organization userOrganization;

    @OneToOne
    @JoinColumn(name="User_Role_Id")
    Role userRole;

    @Column(name="User_Last_Login")
    private Date userLastLogin;

    @Column(name="User_Created_On")
    @CreationTimestamp
    private Date userCreatedOn;

    @Column(name="User_Updated_On")
    @UpdateTimestamp
    private Date userUpdatedOn;

    public User(User user) {
        this.userId = user.userId;
        this.userName = user.userName;
        this.userCreatedOn = user.userCreatedOn;
        this.userUpdatedOn = user.userUpdatedOn;
        this.userFirstName = user.userFirstName;
        this.userLastName = user.userLastName;
        this.userLastLogin = user.userLastLogin;
        this.userOrganization = user.userOrganization;
        this.userPasswordHash = user.userPasswordHash;
        this.userRole = user.userRole;
    }
}
