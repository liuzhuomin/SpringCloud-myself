package xinrui.cloud.domain;

import javax.persistence.*;

/**
 * 走访记录和走访领导关联表
 * @author jihy
 *
 */
@Entity @Table(name = "qybf_visitre_leader_ship") public class VisitreLeaderShip extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 走访的id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visitreCord_id")
	private VisitreCord visitreCord;
	
	/**
	 * 走访关联的走访领导
	 */
	@OneToOne 
	private VisitreLeaders visitreLeader;
	
	/**
	 * 索引位置
	 */
	private Long orderIndex;

	

	public VisitreCord getVisitreCord() {
		return visitreCord;
	}

	public void setVisitreCord(VisitreCord visitreCord) {
		this.visitreCord = visitreCord;
	}

	public VisitreLeaders getVisitreLeader() {
		return visitreLeader;
	}

	public void setVisitreLeader(VisitreLeaders visitreLeader) {
		this.visitreLeader = visitreLeader;
	}

	public Long getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}
	
	
	

}
