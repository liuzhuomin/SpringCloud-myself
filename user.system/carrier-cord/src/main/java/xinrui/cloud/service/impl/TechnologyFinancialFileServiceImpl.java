package xinrui.cloud.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import xinrui.cloud.domain.TechnologyFinancialFile;
import xinrui.cloud.service.TechnologyFinancialFileService;

@Service
public class TechnologyFinancialFileServiceImpl extends BaseServiceImpl<TechnologyFinancialFile> implements TechnologyFinancialFileService {
    @Override
    public TechnologyFinancialFile findByPath(String fileDownLoadPath) {
        return dao.findSingleCriteria(DetachedCriteria.forClass(TechnologyFinancialFile.class).add(Restrictions.eq("fileDownLoadPath", fileDownLoadPath)));
    }
}
