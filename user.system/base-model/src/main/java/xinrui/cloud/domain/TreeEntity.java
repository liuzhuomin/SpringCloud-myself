package xinrui.cloud.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.Interfaces.Treeable;

/**
 * 
 * <B>Title:</B>TreeEntity.java</br>
 * <B>Description:</B> 树实体基类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 * 
 * @author liuliuliu
 *  2019年3月27日
 * @version 1.0
 */
@MappedSuperclass
@SuppressWarnings("serial")
@ApiModel(value = "树结构")
public class TreeEntity<E extends TreeEntity<E>> extends NameEntity implements Treeable<E> {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@ApiModelProperty(value = "父节点", position = 3)
	protected E parent;

	@OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL })
	@ApiModelProperty(value = "子节点集合", position = 4)
	protected List<E> children = new ArrayList<E>();

	@Transient
	@JsonIgnore
	private Boolean hasChild;

	public TreeEntity() {
	}

	public TreeEntity(String name) {
		super(name);
	}

	public TreeEntity(String name, String description) {
		super(name, description);
	}

	public TreeEntity(Long id, String name) {
		this.id=id;
		this.name=name;
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

	@JsonIgnore
	public boolean isRoot() {
		return this.getParent() == null;
	}

	@JsonIgnore
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public E addChild(E... entities) {
		for (E e : entities) {
			e.setParent((E) this);
			getChildren().add(e);
		}
		return (E) this;
	}

	@Override
    public String getName() {
		return name;
	}

	@Override
    public void setName(String name) {
		this.name = name;
	}

	@Override
    public String getDescription() {
		return description;
	}

	@Override
    public void setDescription(String description) {
		this.description = description;
	}

}
