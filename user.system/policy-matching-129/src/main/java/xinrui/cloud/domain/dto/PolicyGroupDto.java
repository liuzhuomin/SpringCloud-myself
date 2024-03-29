package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.PolicyGroup;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

@ApiModel("政策组数据传输对象")
public class PolicyGroupDto extends IdEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "政策组标题", position = 1)
    private String title;

    @ApiModelProperty(value = "图片地址", position = 2)
    private String imageUrl;

    @ApiModelProperty(value = "政策组的描述", position = 3)
    private String content;

    @ApiModelProperty(value = "最终获得的资助额度", position = 4)
    private String money;

    @ApiModelProperty(value = "通过选择政策获取到的所有需要的类目", position = 5)
    private List<PolicyTemplateSimpleDto> temps = Lists.newArrayList();

    @ApiModelProperty(value = "标识政策组的所有题目是否都为空装填", position = 6)
    private boolean allEmpty = false;

    /**
     * 按照政策组{@link PolicyGroup}对象生成一个不包含政策集合的政策组数据传输对象
     *
     * @param byId 政策组对象
     */
    public PolicyGroupDto(PolicyGroup byId) {
        BeanUtils.copyProperties(byId, this, "temps");
    }

    /**
     * 按照id,title，small_pic，content，生成一个政策组数据传输对象
     *
     * @param id        政策组的id
     * @param title     政策组的标题
     * @param small_pic 政策组的图片地址
     * @param content   政策组的描述
     */
    public PolicyGroupDto(long id, String title, String small_pic, String content) {
        this.id = id;
        this.title = title;
        this.imageUrl = small_pic;
        this.content = content;
    }

    /**
     * 按照id,title，small_pic生成一个政策组数据传输对象
     *
     * @param id        政策组的id
     * @param title     政策组的标题
     * @param small_pic 政策组的图片地址
     */
    public PolicyGroupDto(long id, String title, String small_pic) {
        this.id = id;
        this.title = title;
        this.imageUrl = small_pic;
    }

    /**
     * 按照id,title生成一个政策组数据传输对象
     *
     * @param id    政策组的id
     * @param title 政策组的标题
     */
    public PolicyGroupDto(Long id, String title) {
        this.id=id;
        this.title = title;
    }

    public PolicyGroupDto() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<PolicyTemplateSimpleDto> getTemps() {
        return temps;
    }

    public void setTemps(List<PolicyTemplateSimpleDto> temps) {
        this.temps = temps;
    }

    public boolean isAllEmpty() {
        return allEmpty;
    }

    public void setAllEmpty(boolean allEmpty) {
        this.allEmpty = allEmpty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolicyGroupDto that = (PolicyGroupDto) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, imageUrl, content);
    }


}
