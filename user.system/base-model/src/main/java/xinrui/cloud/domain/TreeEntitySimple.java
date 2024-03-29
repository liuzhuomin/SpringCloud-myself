package xinrui.cloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.Interfaces.Treeable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <B>Title:</B>TreeEntity.java</br>
 * <B>Description:</B> 树实体基类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月27日
 */
@MappedSuperclass
@SuppressWarnings("serial")
@ApiModel(value = "树结构")
public class TreeEntitySimple<E extends TreeEntitySimple<E>> extends IdEntity implements Treeable<E> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ApiModelProperty(value = "父节点", position = 3)
    protected E parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    @ApiModelProperty(value = "子节点集合", position = 4)
    protected List<E> children = new ArrayList<E>();

    @Transient
    @JsonIgnore
    private Boolean hasChild;

    public TreeEntitySimple() {
    }


    @Override
    public E getParent() {
        return parent;
    }

    @Override
    @JsonIgnore
    public void setParent(E parent) {
        this.parent = parent;
    }

    @Override
    public List<E> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<E> children) {
        this.children = children;
    }

    @Override
    public Boolean getHasChild() {
        return hasChild == null ? !getChildren().isEmpty() : hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }


    @SuppressWarnings("unchecked")
    public E addChild(E... entities) {
        for (E e : entities) {
            e.setParent((E) this);
            getChildren().add(e);
        }
        return (E) this;
    }


}
