package xinrui.cloud.dto;

import org.hibernate.criterion.DetachedCriteria;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("分页统一对象")
public class PageDto<T> {
	
	/**
	 * 总条数
	 */
	@ApiModelProperty(value = "总条数", position = 0)
	private int totalPage;
	
	/**
	 * 起始页
	 */
	@JsonIgnore
	private int firstResult;
	 
	/**
	 * 最大页
	 */
	@JsonIgnore
	private int maxResult;
	
	/**
	 * 结果对象
	 */
	@ApiModelProperty(value = "结果对象", position = 1)
	private T t;
	
	/**
	 * 离线查询条件
	 */
	@JsonIgnore
	private DetachedCriteria criteria ;


	public PageDto() {
	}

	public PageDto(int totalPage) {
		super();
		this.totalPage = totalPage;
	}

	public PageDto(int firstResult, int maxResult, DetachedCriteria criteria) {
		super();
		this.firstResult=firstResult;
		this.maxResult=maxResult;
		this.criteria=criteria;
	}

	public PageDto(int totalPage, int firstResult, int maxResult, T t) {
		super();
		this.totalPage = totalPage;
		this.firstResult = firstResult;
		this.maxResult = maxResult;
		this.t = t;
	}

	public PageDto(int firstResult, int maxResult) {
		super();
		this.firstResult = firstResult;
		this.maxResult = maxResult;
	}

	public PageDto(int totalPage, T t) {
		this.totalPage=totalPage;
		this.t=t;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public DetachedCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(DetachedCriteria criteria) {
		this.criteria = criteria;
	}


	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

}
