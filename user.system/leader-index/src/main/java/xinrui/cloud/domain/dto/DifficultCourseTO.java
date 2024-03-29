package xinrui.cloud.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("诉求历程")
public class DifficultCourseTO {
	@ApiModelProperty(value = "诉求id", position = 1)
	private Long difficultCompanyId;
	@ApiModelProperty(value = "诉求回复对象id", position = 2)
	private Long difficultTransferId;
	@ApiModelProperty(value = "办理人员", position = 3)
	private String transactorName;
	@ApiModelProperty(value = "办理意见", position = 4)
	private String opinions;
	@ApiModelProperty(value = "接收时间", position = 5)
	private String acceptDate;
	@ApiModelProperty(value = "开始时间", position = 6)
	private String startDate;
	@ApiModelProperty(value = "办理时长", position = 7)
	private String acceptHandling;
	@ApiModelProperty(value = "是否显示催办", position = 8)
	private boolean isShowUrge;
	
	public boolean getIsShowUrge() {
		return isShowUrge;
	}
	public void setShowUrge(boolean isShowUrge) {
		this.isShowUrge = isShowUrge;
	}
	public Long getDifficultCompanyId() {
		return difficultCompanyId;
	}
	public void setDifficultCompanyId(Long difficultCompanyId) {
		this.difficultCompanyId = difficultCompanyId;
	}
	public Long getDifficultTransferId() {
		return difficultTransferId;
	}
	public void setDifficultTransferId(Long difficultTransferId) {
		this.difficultTransferId = difficultTransferId;
	}
	public String getTransactorName() {
		return transactorName;
	}
	public void setTransactorName(String transactorName) {
		this.transactorName = transactorName;
	}
	public String getOpinions() {
		return opinions;
	}
	public void setOpinions(String opinions) {
		this.opinions = opinions;
	}
	public String getAcceptDate() {
		return acceptDate;
	}
	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getAcceptHandling() {
		return acceptHandling;
	}
	public void setAcceptHandling(String acceptHandling) {
		this.acceptHandling = acceptHandling;
	}
	
	
}
