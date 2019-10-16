package xinrui.cloud.service.impl;

import xinrui.cloud.domain.OtherGroup;
import xinrui.cloud.dto.OtherGroupDto;
import org.springframework.stereotype.Service;
import xinrui.cloud.service.CompanyService;

import java.util.List;
import java.util.Map;


@Service
public class CompanyServiceImpl extends BaseServiceImpl<OtherGroup> implements CompanyService {


    @Override
    public OtherGroupDto findCompanyDtoById(Long id) {
        return OtherGroupDto.copyDetailedByPojo(findById(id));
    }

    @Override
    public List<OtherGroupDto> listCompanyDtoByName(String name) {
        return null;
    }

  @Override public Map<String, Object> getCompanyInfo(Long id) {
    String sql = "select t.id, t.name , t.logo, t.creator_id , t.industry_id, t.region_id, c.org_code from tu_company t \n"
        + "left join t_corporation c on c.other_id = t.id  where t.id =  " + id;
    return getQueryForToMap(sql);
  }
}
