package com.appmodz.executionmodule.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@lombok.Getter
@lombok.Setter
@lombok.ToString
@Table(name="AWS_Keys")
public class AwsKey implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="AWS_Key_Id")
    private long awsKeyId;

    @OneToMany(targetEntity=Stack.class, fetch = FetchType.EAGER)
    @JoinColumn(name="AWS_Key_Stacks")
    private List<Stack> stacks;

    @ManyToOne
    @JoinColumn(name="AWS_Keys_Uploader_User_Id")
    private User user;

    @Column(name="AWS_Region")
    private String awsRegion;

    @Column(name="AWS_Access_Key")
    private String awsAccessKey;

    @Column(name="AWS_Secret_Access_Key")
    private String awsSecretAccessKey;

    @Column(name="AWS_Keys_Created_On")
    @CreationTimestamp
    private Date awsKeysCreatedOn;

}