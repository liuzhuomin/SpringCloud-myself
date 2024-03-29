package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;

/**
 * <B>Title:</B>CompanyInfoDto</br>
 * <B>Description:</B>企业简略信息</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/12 16:36
 */
@ApiModel("企业简略信息对象")
public class CompanyInfoDto extends IdEntity {

    @ApiModelProperty(value = "企业名称", position = 1)
    private String name;

    @ApiModelProperty(value = "企业地址", position = 2)
    private String address;

    public CompanyInfoDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CompanyInfoDto(long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
