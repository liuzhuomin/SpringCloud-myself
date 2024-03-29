/**
 * Copyright 2019 bejson.com
 */
package xinrui.cloud.domain.vto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xinrui.cloud.domain.TechnologyFinancial;

import java.io.Serializable;
import java.util.List;

@ApiModel("科技金融对象（接收）")
@Data
public class TechnologyFinancialVto implements Serializable {
    @ApiModelProperty(value = "科技金融产品名称", position = 1)
    private String name;
    @ApiModelProperty(value = "展示的图片", position = 2)
    private String viewIndexImage;
    @ApiModelProperty(value = "科技金融类别，只可能为 信用贷/抵押贷", position = 3)
    private String category;
    @ApiModelProperty(value = "说明的图片", position = 4)
    private List<ViewImagesVto> viewImages;
    @ApiModelProperty(value = "产品标语", position = 5)
    private String slogan;
    @ApiModelProperty(value = "产品简介信息", position = 6)
    private String information;
    @ApiModelProperty(value = "贷款期限对象", position = 7)
    private LoanTimeLimitVto loanTimeLimit;
    @ApiModelProperty(value = "申请结束时间", position = 8)
    private String applyEndDate;
    @ApiModelProperty(value = "贷款额度对象", position = 9)
    private LoanAmountVto loanAmount;
    @ApiModelProperty(value = "可抵押物件类型", position = 10)
    private List<TechnologyLoanTypeVto> technologyLoanTypes;
    @ApiModelProperty(value = "申请条件对象集合", position = 11)
    private List<TechnologyApplyConditionsVto> technologyApplyConditions;
    @ApiModelProperty(value = "问题对象", position = 12)
    private List<TechnologyUsualProblemsVto> technologyUsualProblems;
    @ApiModelProperty(value = "办理流程对象集合", position = 13)
    private List<TechnologyProcessesVto> technologyProcesses;

    @JsonIgnore
    /**
     * 科技产品的状态具体类型看{@link TechnologyStatus#values()}
     */
    private Integer status = TechnologyFinancial.TechnologyStatus.DRAFT.value();


}