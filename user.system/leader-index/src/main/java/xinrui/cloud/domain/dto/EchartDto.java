package xinrui.cloud.domain.dto;

/**
 * <B>Title:</B>EchartDto</br>
 * <B>Description:</B> 用于echart图展示数据用 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/10 16:51
 */
public class EchartDto {

   private String companyName;
   private int complete;
   private int incomplete;

    /**
     * 按照企业名称，完成数量，未完成数量创建echart图数据传输对象
     * @param companyName       企业名称
     * @param complete          完成数量
     * @param incomplete        未完成数量
     */
    public EchartDto(String companyName, int complete, int incomplete) {
        this.companyName = companyName;
        this.complete = complete;
        this.incomplete = incomplete;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(int incomplete) {
        this.incomplete = incomplete;
    }

}
