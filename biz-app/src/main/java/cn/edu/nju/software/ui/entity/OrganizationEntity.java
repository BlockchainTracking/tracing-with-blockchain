package cn.edu.nju.software.ui.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Daniel
 * @since 2018/5/12 23:28
 */
//@Entity
//@Table(name = "organization", schema = "tracing", catalog = "")
public class OrganizationEntity {
    private int id;
    private String orgName;
    private Serializable orgType;
    private String blockchainOrgId;

    public void setOrgType(byte[] orgType) {
        this.orgType = orgType;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "org_name", nullable = true, length = 255)
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Basic
    @Column(name = "org_type", nullable = true)
    public Serializable getOrgType() {
        return orgType;
    }

    public void setOrgType(Serializable orgType) {
        this.orgType = orgType;
    }

    @Basic
    @Column(name = "blockchain_org_id", nullable = true, length = 255)
    public String getBlockchainOrgId() {
        return blockchainOrgId;
    }

    public void setBlockchainOrgId(String blockchainOrgId) {
        this.blockchainOrgId = blockchainOrgId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationEntity that = (OrganizationEntity) o;
        return id == that.id &&
                Objects.equals(orgName, that.orgName) &&
                Objects.equals(orgType, that.orgType) &&
                Objects.equals(blockchainOrgId, that.blockchainOrgId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, orgName, orgType, blockchainOrgId);
    }
}
