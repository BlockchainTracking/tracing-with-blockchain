package cn.edu.nju.software.ui.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Daniel
 * @since 2018/5/12 23:28
 */
@Entity
@Table(name = "item", schema = "tracing", catalog = "")
public class ItemEntity {
    private int id;
    private String itemBlockchainId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "item_blockchain_id", nullable = true, length = 255)
    public String getItemBlockchainId() {
        return itemBlockchainId;
    }

    public void setItemBlockchainId(String itemBlockchainId) {
        this.itemBlockchainId = itemBlockchainId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity that = (ItemEntity) o;
        return id == that.id &&
                Objects.equals(itemBlockchainId, that.itemBlockchainId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, itemBlockchainId);
    }
}
