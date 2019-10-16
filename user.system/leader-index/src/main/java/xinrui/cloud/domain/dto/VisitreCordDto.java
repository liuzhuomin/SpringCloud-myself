package xinrui.cloud.domain.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.VisitreCord;
import xinrui.cloud.domain.VisitreCordType;
import xinrui.cloud.domain.VisitreLeaderShip;


/**
 * 走访记录数据传输对象
 *
 * @author liuliuliu
 */
@ApiModel("走访记录数据传输对象")
public class VisitreCordDto extends IdEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业的名称", position = 1)
    private String CompanyName;

    @ApiModelProperty(value = "上报走访的时间", position = 2)
    private String commitDate;

    @ApiModelProperty(value = "挂点领导", position = 3)
    private String gdld;

    @ApiModelProperty(value = "挂点领导id", position = 4)
    private Long gdldId;

    @ApiModelProperty(value = "* 在光明区项目情况", position = 5)
    private String inGmProjectState;

    @ApiModelProperty(value = "牵头单位", position = 6)
    private String qtdw;

    @ApiModelProperty(value = "所属街道", position = 7)
    private String ssjd;

    @ApiModelProperty(value = "走访领导名称", position = 8)
    private String leaderShip;

    @ApiModelProperty(value = "走访类型", position = 9)
    private String visitreCordType;

    @ApiModelProperty(value = "走访时间", position = 10)
    private String visitreDate;

    @ApiModelProperty(value = "对应诉求对象", position = 11)
    private List<DifficultCompanyDto> difficultCompany = new ArrayList<DifficultCompanyDto>();

    @ApiModelProperty(value = "走访后", position = 12)
    private List<DifficultCompanyDto> afterDifficultCompany = new ArrayList<DifficultCompanyDto>();

    @ApiModelProperty(value = "走访后计划完成时间", position = 12)
    private String afterPlanDate;

    @ApiModelProperty(value ="更新走访时间之前的走访时间", position = 13)
    private String lastUpdate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static VisitreCordDto copyFrom(VisitreCord byId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        VisitreCordDto result=new VisitreCordDto();
        BeanUtils.copyProperties(byId,result,"commitDate","leaderShip","visitreCordType","visitreDate","difficultCompany","afterPlanDate");
        Date commitDate = byId.getCommitDate();
        if(commitDate!=null){
            result.setCommitDate( simpleDateFormat.format(commitDate));
        }
        Date visitreDate = byId.getVisitreDate();
        if(visitreDate!=null){
            result.setVisitreDate( simpleDateFormat.format(visitreDate));
        }
        Date afterPlanDate = byId.getAfterPlanDate();
        if(afterPlanDate!=null){
            result.setAfterPlanDate(simpleDateFormat.format(afterPlanDate));
        }
        Date lastUpdate = byId.getLastUpdate();
        if(lastUpdate!=null){
            result.setLastUpdate(simpleDateFormat.format(lastUpdate));
        }
//        result.setCompanyName(byId.getCompany().getName());
        List<VisitreLeaderShip> leaderShip = byId.getLeaderShip();
        if(!CollectionUtils.isEmpty(leaderShip)){
            StringBuffer sbf=new StringBuffer();
            for(int i=0;i<leaderShip.size();i++){
                VisitreLeaderShip visitreLeaderShip = leaderShip.get(i);
                String realname = visitreLeaderShip.getVisitreLeader().getUser().getRealname();
                sbf.append(realname).append("、");
            }
            if(sbf.length()!=0){
                sbf.deleteCharAt(sbf.length()-1);
                result.setLeaderShip(sbf.toString());
            }
        }
        VisitreCordType visitreCordType = byId.getVisitreCordType();
        if(visitreCordType!=null){
            String title = visitreCordType.getTitle();
            if(!StringUtils.isEmpty(title)) {
                result.setVisitreCordType(title);
            }
        }
        return result;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getGdld() {
        return gdld;
    }

    public void setGdld(String gdld) {
        this.gdld = gdld;
    }

    public Long getGdldId() {
        return gdldId;
    }

    public void setGdldId(Long gdldId) {
        this.gdldId = gdldId;
    }

    public String getInGmProjectState() {
        return inGmProjectState;
    }

    public void setInGmProjectState(String inGmProjectState) {
        this.inGmProjectState = inGmProjectState;
    }

    public String getQtdw() {
        return qtdw;
    }

    public void setQtdw(String qtdw) {
        this.qtdw = qtdw;
    }

    public String getSsjd() {
        return ssjd;
    }

    public void setSsjd(String ssjd) {
        this.ssjd = ssjd;
    }

    public String getLeaderShip() {
        return leaderShip;
    }

    public void setLeaderShip(String leaderShip) {
        this.leaderShip = leaderShip;
    }

    public String getVisitreCordType() {
        return visitreCordType;
    }

    public void setVisitreCordType(String visitreCordType) {
        this.visitreCordType = visitreCordType;
    }

    public String getVisitreDate() {
        return visitreDate;
    }

    public void setVisitreDate(String visitreDate) {
        this.visitreDate = visitreDate;
    }

    public List<DifficultCompanyDto> getDifficultCompany() {
        return difficultCompany;
    }

    public void setDifficultCompany(List<DifficultCompanyDto> difficultCompany) {
        this.difficultCompany = difficultCompany;
    }

    public String getAfterPlanDate() {
        return afterPlanDate;
    }

    public void setAfterPlanDate(String afterPlanDate) {
        this.afterPlanDate = afterPlanDate;
    }

    public List<DifficultCompanyDto> getAfterDifficultCompany() {
        return afterDifficultCompany;
    }

    public void setAfterDifficultCompany(List<DifficultCompanyDto> afterDifficultCompany) {
        this.afterDifficultCompany = afterDifficultCompany;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
