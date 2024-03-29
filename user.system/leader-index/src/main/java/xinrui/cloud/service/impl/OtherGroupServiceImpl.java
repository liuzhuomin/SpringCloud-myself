package xinrui.cloud.service.impl;

import com.google.common.collect.Lists;
import xinrui.cloud.domain.OtherGroup;
import xinrui.cloud.domain.User;
import xinrui.cloud.domain.dto.CompanyDto;
import xinrui.cloud.service.OtherGroupService;
import xinrui.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>Title:</B>OtherGroupServiceImpl</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/10 16:46
 */
@Service
public class OtherGroupServiceImpl extends BaseServiceImpl<OtherGroup> implements OtherGroupService {

    @Autowired
    UserService userService;

    @Autowired
    OtherGroupService otherGroupService;

    @Override
    public List<CompanyDto> searchNames(String name, Long leaderId) {
        List<CompanyDto> companyDtos = otherGroupService.listCompany(leaderId);
        List<Long> ids =Lists.newArrayList();
        for(CompanyDto dto:companyDtos){
            ids.add(dto.getId());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("mainName","%"+name+"%");
        params.put("ids",ids);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(dao.getJdbcTemplate());
        return namedParameterJdbcTemplate.query("select id,name from tu_company where name like :mainName and id not in (:ids) ",params,getRse());
//        return dao.getJdbcTemplate().query("select id,name from tu_company where name like ? and id not in (select corp_id from qy_base_corp_extension_leader where leader_id = ?)", getRse(), "%" + name + "%", leaderId);
    }

    private ResultSetExtractor<List<CompanyDto>> getRse() {
        return new ResultSetExtractor<List<CompanyDto>>() {
            @Override
            public List<CompanyDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<CompanyDto> result = Lists.newArrayList();
                while (rs.next()) {
                    result.add(new CompanyDto(rs.getLong(1), rs.getString(2)));
                }
                return result;
            }
        };
    }

    @Override
    @Transactional
    public List<CompanyDto> listCompany(Long leaderId) {
        List<CompanyDto> query = dao.getJdbcTemplate().query("select id,corp_name from  qy_base_corp_essence where id in( " +
                "select DISTINCT corp_id from qy_base_corp_extension_leader where leader_id=?)", getRse(), leaderId);
        User byId = userService.findById(leaderId);
        for (CompanyDto dto : query) {
            dto.setFoucs(false);
        }
        List<CompanyDto> companyDtos = CompanyDto.copyFrom(byId.getFocusCompanies());
        for (CompanyDto dto : companyDtos) {
            dto.setFoucs(true);
        }
        query.addAll(companyDtos);
        if (!CollectionUtils.isEmpty(query)) {
            for (int i = 0; i < query.size(); i++) {
                CompanyDto companyDto = query.get(i);
                Long id = companyDto.getId();
                String orgName = dao.getJdbcTemplate().query("select alias_name from tu_organization where id=(select manage_org from qy_base_corp_extension_manage_org where corp_id=?)", new ResultSetExtractor<String>() {
                    @Override
                    public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return rs.getString(1);
                        }
                        return null;
                    }
                }, id);
                if (!StringUtils.isEmpty(orgName)) {
                    companyDto.setQtdw(orgName);
                }
            }
        }
        return query;
    }

    @Override
    public void deleteFocusCompany(Long leaderId, Long companyId) {
        User user = userService.findById(leaderId);
        Assert.notNull(user, "用户找不到");
        OtherGroup company = findById(companyId);
        Assert.notNull(company, "企业找不到");
        company.setFucosLeader(null);
        user.getFocusCompanies().remove(company);
        userService.merge(user);
    }
}
