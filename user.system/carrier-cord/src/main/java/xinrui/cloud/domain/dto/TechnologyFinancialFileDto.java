package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyFinancialFile;

import java.util.List;


@ApiModel("科技金融文件对象")
@Data
public class TechnologyFinancialFileDto extends IdEntity {

    @ApiModelProperty(value = "图片完整路径", position = 1)
    private String fullPath;

}
