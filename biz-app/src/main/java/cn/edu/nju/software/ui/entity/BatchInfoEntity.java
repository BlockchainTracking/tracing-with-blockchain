package cn.edu.nju.software.ui.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

/**
 * @author Daniel
 * @since 2018/5/12 23:28
 */
@Entity
@Table(name = "batch_info", schema = "tracing", catalog = "")
public class BatchInfoEntity {
    private int id;
    private Integer batchNum;
    private Date date;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "batch_num", nullable = true)
    public Integer getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Integer batchNum) {
        this.batchNum = batchNum;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchInfoEntity that = (BatchInfoEntity) o;
        return id == that.id &&
                Objects.equals(batchNum, that.batchNum) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, batchNum, date);
    }
}
