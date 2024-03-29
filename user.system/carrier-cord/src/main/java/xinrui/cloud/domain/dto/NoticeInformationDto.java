package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.NoticeInformation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.Lob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@ApiModel(description = "通知公告表")
public class NoticeInformationDto extends IdEntity {

    @ApiModelProperty(value = "用户id", position = 0)
    private String userId;
    @ApiModelProperty(value = "标题", position = 1)
    private String title;
    @ApiModelProperty(value = "内容", position = 2)
    @Lob
    private String content;
    @ApiModelProperty(value = "状态（0：草稿箱；1：发布通知）", position = 3)
    private String status;
    @ApiModelProperty(value = "开始时间", position = 4)
    private String beginTime;
    @ApiModelProperty(value = "结束时间", position = 5)
    private String endTime;
    @ApiModelProperty(value = "阅读量", position = 6)
    private int readNum;
    @ApiModelProperty(value = "通知类型", position = 7)
    private String type;
    @ApiModelProperty(value = "创建时间", position = 8)
    private String createTime;
    @ApiModelProperty(value = "封面图片地址", position = 9)
    private String imgPath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NoticeInformationDto(Long id, String title, String content, String createTime) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    public static List<NoticeInformationDto> copyList(List<NoticeInformation> findAll) {
        if (CollectionUtils.isEmpty(findAll)) {
            return null;
        }
        List<NoticeInformationDto> results = Lists.newArrayList();
        for (NoticeInformation noticeInformation : findAll) {
            results.add(copy(noticeInformation));
        }
        return results;
    }

    public NoticeInformationDto() {
    }

    public static NoticeInformationDto copy(NoticeInformation notice) {
        if (notice == null) {
            return null;
        }
        NoticeInformationDto noticeInformationDto = new NoticeInformationDto();
        BeanUtils.copyProperties(notice, noticeInformationDto, "createTime");
        Date createTime = notice.getCreateTime();
        if (createTime != null) {
            noticeInformationDto.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        }
        return noticeInformationDto;
//        return new NoticeInformationDto(notice.getId(), notice.getTitle(), notice.getCreateTime());
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
