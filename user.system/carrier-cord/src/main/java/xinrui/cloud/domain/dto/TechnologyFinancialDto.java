package xinrui.cloud.domain.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.domain.*;

import java.util.*;

@ApiModel("科技金融核心对象")
@Data
public class TechnologyFinancialDto extends IdEntity {

    @ApiModelProperty(value = "科技金融产品名称", position = 1)
    private String name;

    @ApiModelProperty(value = "展示的图片", position = 2)
    private String viewIndexImage;

    @ApiModelProperty(value = "说明的图片", position = 3)
    private List<TechnologyFinancialFileDto> viewImages;

    @ApiModelProperty(value = "科技金融类别，只可能为 信用贷/抵押贷", position = 4)
    private String category;

    @ApiModelProperty(value = "产品标语", position = 5)
    private String slogan;

    @ApiModelProperty(value = "产品简介信息", position = 6)
    private String information;

    @ApiModelProperty(value = "贷款期限对象", position = 7)
    private TechnologyLoanDateDto loanTimeLimit;

    @ApiModelProperty(value = "贷款可抵押物件", position = 8)
    private List<TechnologyLoanTypeDto> technologyLoanTypes;

    @ApiModelProperty(value = "申请结束时间", position = 9)
    private String applyEndDate;

    @ApiModelProperty(value = "贷款额度对象", position = 10)
    private TechnologyLoanAmountDto loanAmount;

    @ApiModelProperty(value = "申请条件对象集合", position = 11)
    private List<TechnologyApplyConditionDto> technologyApplyConditions;

    @ApiModelProperty(value = "创建时间", position = 12)
    private String createDate;

    @ApiModelProperty(value = "问题对象", position = 13)
    private List<TechnologyUsualProblemDto> technologyUsualProblems;

    @ApiModelProperty(value = "办理流程对象集合", position = 14)
    private List<TechnologyProcessDto> technologyProcesses;

    @ApiModelProperty(value = "办理流程对象集合", position = 15)
    private String refuseReason;


    public static TechnologyFinancialDto copy(TechnologyFinancial technologyFinancial) {
        TechnologyFinancialDto dto = new TechnologyFinancialDto();
        BeanUtilsEnhance.copyPropertiesEnhance(technologyFinancial, dto);
        dto.setViewImages(BeanUtilsEnhance.copyList(technologyFinancial.getViewImages(), TechnologyFinancialFileDto.class));
        dto.setLoanTimeLimit(TechnologyLoanDateDto.copy(technologyFinancial.getLoanTimeLimit()));
        dto.setLoanAmount(TechnologyLoanAmountDto.copy(technologyFinancial.getLoanAmount()));
        dto.setTechnologyApplyConditions(BeanUtilsEnhance.copyList(technologyFinancial.getTechnologyApplyConditions(), TechnologyApplyConditionDto.class));
        dto.setTechnologyUsualProblems(BeanUtilsEnhance.copyList(technologyFinancial.getTechnologyUsualProblems(), TechnologyUsualProblemDto.class));
        dto.setTechnologyProcesses(BeanUtilsEnhance.copyList(technologyFinancial.getTechnologyProcesses(), TechnologyProcessDto.class));
        dto.setTechnologyLoanTypes(BeanUtilsEnhance.copyList(technologyFinancial.getTechnologyLoanTypes(), TechnologyLoanTypeDto.class));
        return dto;
    }

    public static List<TechnologyFinancialDto> copy(List<TechnologyFinancial> technologyFinancials) {
        List<TechnologyFinancialDto> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(technologyFinancials)) {
            for (TechnologyFinancial technologyFinancial : technologyFinancials) {
                result.add(copy(technologyFinancial));
            }
        }
        return result;
    }

}

