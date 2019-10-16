package xinrui.cloud.service;

import java.util.List;
import java.util.Map;
import xinrui.cloud.dao.CompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jihy
 * @since 2019-06-25 11:27
 */
@Service  public class CompanyService {

  @Autowired private CompanyDao companyDao;

  public List<Map<String, Object>> getCompanyByName(String name){
      return companyDao.getCompanyByName(name);
  }
}
