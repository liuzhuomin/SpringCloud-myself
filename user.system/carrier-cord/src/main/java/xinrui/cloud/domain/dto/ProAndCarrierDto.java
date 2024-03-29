package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("关联载体的实体类")
public class ProAndCarrierDto {
    @ApiModelProperty(value = "载体名称", position = 1)
    private String brandName;
    @ApiModelProperty(value = "所属区域", position = 2)
    private String areasToWhichTheyBelong;
    @ApiModelProperty(value = "详细地址", position = 3)
    private String incubationBaseAddress;
    @ApiModelProperty(value = "租凭价格范围", position = 4)
    private String leasePriceRange;
    @ApiModelProperty(value = "联系人姓名", position = 5)
    private String contactsName;
    @ApiModelProperty(value = "联系人电话", position = 6)
    private String contactsPhone;
    @ApiModelProperty(value = "载体的Id", position = 7)
    private Long CarrierId;
    @ApiModelProperty(value = "添加载体排序的字段", position = 8)
    private int sort;
    @ApiModelProperty(value = "空置面积", position = 9)
    private String vacantArea;
}
