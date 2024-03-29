package xinrui.cloud.domain.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.Instructions;

/**
 * <B>Title:</B>Instructions</br>
 * <B>Description:</B> 批示对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/5 15:06
 */
@ApiModel("批示对象")
public class InstructionsDto extends  IdEntity{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "批示的文本", position =1)
    private String context;

    @ApiModelProperty(value = "批示时间", position =2)
    private String date;

    @ApiModelProperty(value = "领导名称", position =3)
    private String leaderName;

    public static List<InstructionsDto> copyFrom(List<Instructions> difficultInstructions) {
        List<InstructionsDto> result= Lists.newArrayList();
        if(!CollectionUtils.isEmpty(difficultInstructions)){
            for (Instructions bean:difficultInstructions){
                result.add(InstructionsDto.copyFrom(bean));
            }
        }
        return result;
    }

    public static InstructionsDto copyFrom(Instructions instructions){
        InstructionsDto dto=new InstructionsDto();
        BeanUtils.copyProperties(instructions,dto,"date");
        Date date = instructions.getDate();
        if(date!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(date);
            dto.setDate(format);
        }
        return dto;
    }


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
