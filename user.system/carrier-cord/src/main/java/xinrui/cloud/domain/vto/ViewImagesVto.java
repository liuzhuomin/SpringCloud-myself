/**
 * Copyright 2019 bejson.com
 */
package xinrui.cloud.domain.vto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("科技金融文件对象(接收)")
public class ViewImagesVto implements Serializable {

    @ApiModelProperty(value = "上传后获得的完整下载路径", position = 1)
    private String fullPath;
    public ViewImagesVto(String fullPath) {
        this.fullPath = fullPath;
    }
    public ViewImagesVto() {
    }
}