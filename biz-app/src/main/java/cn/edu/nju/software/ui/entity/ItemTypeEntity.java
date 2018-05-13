package cn.edu.nju.software.ui.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Daniel
 * @since 2018/5/12 23:28
 */
//@Entity
//@Table(name = "item_type", schema = "tracing", catalog = "")
public class ItemTypeEntity {
    private int id;
    private String typeName;
    private String typeClass;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type_name", nullable = true, length = 255)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "type_class", nullable = true, length = 255)
    public String getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemTypeEntity that = (ItemTypeEntity) o;
        return id == that.id &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(typeClass, that.typeClass);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, typeName, typeClass);
    }
}
