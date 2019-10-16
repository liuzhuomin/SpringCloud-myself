package xinrui.cloud.domain.vto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import xinrui.cloud.domain.IdEntity;

/**
 * <B>Title:</B>OtherGroupDto</br>
 * <B>Description:</B>组织数据传输对象可为企业也可为组织对象，通过type进行判断</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * 2019/6/26 20:22
 */
@ApiModel("组织数据传输对象")
@Getter
@Setter
public class OtherGroupVto extends IdEntity {

    @ApiModelProperty(value = "标识", position = 2)
    private String tag;

    @ApiModelProperty(value = "是否是系统导入:系统导入1", position = 3)
    private Integer isImport;

    @ApiModelProperty(value = "英文名称", position = 6)
    private String engName;

    @ApiModelProperty(value = "办公地址", position = 12)
    private String officeAddr;

    @ApiModelProperty(value = "公司简称", position = 13)
    private String companyIntro;

    @ApiModelProperty(value = "组织类型", position = 15)
    private String groupType;

    @ApiModelProperty(value = "组织名称", position = 16)
    private String name;


    public OtherGroupVto(String name) {
        this.name=name;
        this.tag="bank";
        this.isImport=0;
        this.engName="bank";
        this.groupType="bank";
    }
}
