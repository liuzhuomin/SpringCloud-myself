package xinrui.cloud.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.google.common.base.MoreObjects.ToStringHelper;

import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.Interfaces.Nameable;

/**
 * Name实体基类.
 * 
 * @author lsh
 */
@MappedSuperclass
@SuppressWarnings("serial")
public class NameEntity extends IdEntity implements Nameable {

	/**
	 * 名称.
	 */
	@Column(length = 500)
	@ApiModelProperty(value = "名称", position = 1)
	protected String name;

	/**
	 * 描述.
	 */
	@Column(length = 2000)
	@ApiModelProperty(value = "描述", position = 2)
	protected String description;

	public NameEntity() {
	}

	public NameEntity(String name) {
		this.name = name;
	}

	public NameEntity(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public ToStringHelper toStringHelper() {
		return super.toStringHelper().add("name", name).add("description", description);
	}

}
