package xinrui.cloud.fallback;

import org.springframework.stereotype.Component;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.service.feign.CompanyServiceFeign;

import java.util.List;
import java.util.Map;

/**
 * <B>Title:</B>CompanyServiceFeignFallBack</br>
 * <B>Description:</B> 企业信息接口降级处理</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/3 10:12
 */
@Component
public class CompanyServiceFeignFallBack implements CompanyServiceFeign {

    @Override
    public List<OtherGroupDto> listCompanyDtoByName(String name) {
        return null;
    }

    @Override
    public OtherGroupDto findCompanyDtoById(Long id) {
        return null;
    }

  @Override public Map<String, Object> findCompanyMapById(Long id) {
    return null;
  }
}
