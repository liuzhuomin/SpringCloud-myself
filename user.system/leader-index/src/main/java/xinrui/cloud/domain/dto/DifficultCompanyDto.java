package xinrui.cloud.domain.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.DifficultCompany;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.Instructions;

/**
 * <B>Title:</B>DifficultCompanyDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 16:07
 */
@ApiModel("诉求详细信息对象")
public class DifficultCompanyDto extends IdEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "企业名称", position = 1)
    private String companyName;

    @ApiModelProperty(value = "问题类型", position = 3)
    private String diffcultType;

    @ApiModelProperty(value = "上报时间", position = 4)
    private String commitDate;

    @ApiModelProperty(value = "诉求标题", position = 5)
    private String diffcultTitle;

    @ApiModelProperty(value = "问题描述", position = 6)
    private String mainProblems;

    @ApiModelProperty(value = " 0 待办 1 在办(主办或协办) 2 办结", position = 7)
    private int status;

    @ApiModelProperty(value = "是否跟踪,true代表当前诉求件被领导跟踪了，false代表没有被跟踪", position = 8)
    private boolean flow = false;

    @ApiModelProperty(value = "是否批示，true代表", position = 9)
    private boolean instructions = false;

    @ApiModelProperty(value = "办理意见对象", position = 10)
    private DifficultHistoryDto difficultHistoryDto;

    @ApiModelProperty(value = "批示对象集合", position = 11)
    private List<InstructionsDto> instructionsDtos = Lists.newArrayList();

    @ApiModelProperty(value = "办理时间限制", position = 12)
    private long processDate;

    @ApiModelProperty(value = "现场解决情况", position = 13)
    private String xcjjqk;

    @ApiModelProperty(value = "针对走访记录的状态 走访前 0 | 走访后 1 | 取消走访 2", position = 13)
    private int visitreCordStatus;

    private final static Logger LOOGER = LoggerFactory.getLogger(DifficultCompanyDto.class);

    public static DifficultCompanyDto copyFrom(DifficultCompany byId) {
        DifficultCompanyDto dto = new DifficultCompanyDto();
        if (byId != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BeanUtils.copyProperties(byId, dto, "diffcultType", "commitDate", "processDate", "flow");
            dto.setDiffcultType(byId.getDiffcultType().getName());
            dto.setCommitDate(simpleDateFormat.format(byId.getCommitDate()));
            LOOGER.info("flow:" + byId.getFlow());
            if (byId.getFlow() != null) {
                dto.setFlow(byId.getFlow().booleanValue());
            }
            Date processDate = byId.getProcessDate();
            if (processDate != null) {
                dto.setProcessDate(processDate.getTime());
            }
            List<Instructions> difficultInstructions = byId.getDifficultInstructions();
            dto.setInstructions(!CollectionUtils.isEmpty(difficultInstructions));
            dto.setInstructionsDtos(InstructionsDto.copyFrom(difficultInstructions));
        }
        return dto;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDiffcultType() {
        return diffcultType;
    }

    public void setDiffcultType(String diffcultType) {
        this.diffcultType = diffcultType;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getDiffcultTitle() {
        return diffcultTitle;
    }

    public void setDiffcultTitle(String diffcultTitle) {
        this.diffcultTitle = diffcultTitle;
    }

    public String getMainProblems() {
        return mainProblems;
    }

    public void setMainProblems(String mainProblems) {
        this.mainProblems = mainProblems;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFlow() {
        return flow;
    }

    public void setFlow(boolean flow) {
        this.flow = flow;
    }

    public boolean isInstructions() {
        return instructions;
    }

    public void setInstructions(boolean instructions) {
        this.instructions = instructions;
    }

    public List<InstructionsDto> getInstructionsDtos() {
        return instructionsDtos;
    }

    public void setInstructionsDtos(List<InstructionsDto> instructionsDtos) {
        this.instructionsDtos = instructionsDtos;
    }

    public DifficultHistoryDto getDifficultHistoryDto() {
        return difficultHistoryDto;
    }

    public void setDifficultHistoryDto(DifficultHistoryDto difficultHistoryDto) {
        this.difficultHistoryDto = difficultHistoryDto;
    }

    public long getProcessDate() {
        return processDate;
    }

    public void setProcessDate(long processDate) {
        this.processDate = processDate;
    }

    public static Logger getLOOGER() {
        return LOOGER;
    }

    public String getXcjjqk() {
        return xcjjqk;
    }

    public void setXcjjqk(String xcjjqk) {
        this.xcjjqk = xcjjqk;
    }

    public int getVisitreCordStatus() {
        return visitreCordStatus;
    }

    public void setVisitreCordStatus(int visitreCordStatus) {
        this.visitreCordStatus = visitreCordStatus;
    }


}