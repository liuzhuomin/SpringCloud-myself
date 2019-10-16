package xinrui.cloud.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xinrui.cloud.domain.Organization;
import xinrui.cloud.domain.OtherGroup;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.service.OrganizationService;
import xinrui.cloud.vto.OtherGroupVto;

import java.util.List;

@Service
public class OrganizationServiceImpl extends BaseServiceImpl<Organization> implements OrganizationService {
    @Override
    public OtherGroupDto findByCompanyName(String name) {
        return OtherGroupDto.copy(dao.findSingleByProperty(OtherGroup.class, "name", name));
    }

    @Override
    public OtherGroupDto findByCompanyId(Long id) {
        return OtherGroupDto.copy(dao.findSingleByProperty(OtherGroup.class, "id", id));
    }

    @Override
    public OtherGroupDto createByVto(OtherGroupVto vto) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(vto, organization);
        return OtherGroupDto.copy(merge(organization));
    }

    @Override
    public List<OtherGroupDto> listByType(String type) {
        DetachedCriteria groupType = DetachedCriteria.forClass(Organization.class).add(Restrictions.eq("groupType", type));
        return OtherGroupDto.copy(listBydCriteria(groupType));
    }
}
