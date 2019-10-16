package xinrui.cloud.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * id实现超类
 *
 * @author liuliuliu
 */
@MappedSuperclass
public class IdEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @SequenceGenerator(initialValue = 1, name = "id_sequence", sequenceName = "ID_SEQUENCE")
    @ApiModelProperty(value = "唯一主键", position = 0)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    public IdEntity() {
    }

    public IdEntity(Long id) {
        super();
        this.id = id;
    }

    public ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this).add("id", getId());
    }

}
