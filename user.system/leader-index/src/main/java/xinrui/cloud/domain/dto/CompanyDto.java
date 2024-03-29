package xinrui.cloud.domain.dto;

import java.util.List;

import xinrui.cloud.domain.OtherGroup;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;

/**
 * <B>Title:</B>CompanyDto</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/6 9:35
 */
@ApiModel("企业简洁对象")
public class CompanyDto extends IdEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "企业的名称", position = 1)
    private String name;

    @ApiModelProperty(value = "牵头单位", position = 2)
    private String qtdw;

    @ApiModelProperty(value = "标识是否是关注企业", position = 4)
    private Boolean foucs;

    public CompanyDto(long aLong, String string) {
        this.id=aLong;
        this.name=string;
    }

    public static List<CompanyDto> copyFrom(List<OtherGroup> focusCompanies) {
        List<CompanyDto> result= Lists.newArrayList();
        if(!CollectionUtils.isEmpty(focusCompanies)){
            for (int i=0;i<focusCompanies.size();i++){
                OtherGroup otherGroup = focusCompanies.get(i);
                result.add(new CompanyDto(otherGroup.getId(),otherGroup.getName()));
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQtdw() {
        return qtdw;
    }

    public void setQtdw(String qtdw) {
        this.qtdw = qtdw;
    }

    public Boolean getFoucs() {
        return foucs;
    }

    public void setFoucs(Boolean foucs) {
        this.foucs = foucs;
    }
}
