package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;

/**
 * <B>Title:</B>PolicyParagraphDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/4/10 17:23
 */
@ApiModel("政策描述对象")
public class PolicyParagraphDto extends IdEntity {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "政策的描述（html格式）", position = 1)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
