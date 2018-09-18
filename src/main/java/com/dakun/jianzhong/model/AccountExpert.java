package com.dakun.jianzhong.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "account_expert")
public class AccountExpert {
    @Id
    private Integer id;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "expert_name")
    private String expertName;

    @Column(name = "id_front")
    private String idFront;

    @Column(name = "id_back")
    private String idBack;

//    private String credential;

    private String skills;

    private String description;

    @Transient
    private List<AccountPictures> credentialpictures;//专家证书，多图

    public List<AccountPictures> getCredentialpictures() {
        return credentialpictures;
    }

    public void setCredentialpictures(List<AccountPictures> credentialpictures) {
        this.credentialpictures = credentialpictures;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return id_number
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return expert_name
     */
    public String getExpertName() {
        return expertName;
    }

    /**
     * @param expertName
     */
    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    /**
     * @return id_front
     */
    public String getIdFront() {
        return idFront;
    }

    /**
     * @param idFront
     */
    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    /**
     * @return id_back
     */
    public String getIdBack() {
        return idBack;
    }

    /**
     * @param idBack
     */
    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

//    /**
//     * @return credential
//     */
//    public String getCredential() {
//        return credential;
//    }
//
//    /**
//     * @param credential
//     */
//    public void setCredential(String credential) {
//        this.credential = credential;
//    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}