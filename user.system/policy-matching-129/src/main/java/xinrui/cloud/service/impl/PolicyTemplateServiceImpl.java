package xinrui.cloud.service.impl;

import xinrui.cloud.domain.Policy;
import xinrui.cloud.domain.PolicyActivity;
import xinrui.cloud.domain.PolicyGroup;
import xinrui.cloud.domain.PolicyTemplate;
import xinrui.cloud.domain.dto.PolicyDto;
import xinrui.cloud.domain.dto.PolicyTemplateDto;
import xinrui.cloud.service.ActivityService;
import xinrui.cloud.service.PolicyGroupServcice;
import xinrui.cloud.service.PolicyService;
import xinrui.cloud.service.PolicyTemplateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <B>Title:</B>PolicyTemplateServiceImpl.java</br>
 * <B>Description:</B> 政策模版相关</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
@Service
public class PolicyTemplateServiceImpl extends BaseServiceImpl<PolicyTemplate> implements PolicyTemplateService {

    @Autowired
    PolicyGroupServcice groupSerivce;

    @Autowired
    ActivityService activityService;

    @Autowired
    PolicyService policyService;

    @Override
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<PolicyDto> listCreatedPolicyId(String name, Long ignoreId) {
//		isAdministration='1' AND
//
//        AND p.policyGroup IS NOT NULL
        StringBuffer hql = new StringBuffer(
                "SELECT new xinrui.cloud.domain.dto.PolicyDto(p.id as id,p.shortTitle as title ) FROM xinrui.cloud.domain.Policy AS p WHERE status='P' AND p.policyGroup IS NOT NULL AND id not in (SELECT  policy.id AS id FROM xinrui.cloud.domain.PolicyTemplate WHERE policy IS NOT NULL)");
        if (ignoreId == null) {
            if (StringUtils.isNotBlank(name)) {
                hql.append(" and p.shortTitle like ?0");
                return dao.pageObjectByHql(hql.toString(), PolicyDto.class, "%" + name + "%");
            } else {
                return dao.pageObjectByHql(hql.toString(), PolicyDto.class);
            }
        } else {
            hql.append(" OR id=?0");
            return dao.pageObjectByHql(hql.toString(), PolicyDto.class, ignoreId);
        }
    }

    @Override
    public PolicyTemplateDto createTemplate(String unitSubject, String description, Long policyId) {
        Policy policy = policyService.findById(policyId);
        PolicyGroup policyGroup = policy.getPolicyGroup();
        Assert.notNull(policyGroup, "政策组为空");
        String mainName = policyGroup.getTitle();
        PolicyTemplate policyTemplate = new PolicyTemplate();
        policyTemplate.setMainName(mainName);
        policyTemplate.setUnitSubject(unitSubject);
        policyTemplate.setDescription(description);
        PolicyTemplate persistAndGet = persistAndGet(policyTemplate);
        policyTemplate.setPolicy(policy);
        policy.setPolicyTemplate(persistAndGet);
        init(policyTemplate);
        return PolicyTemplateDto.copy(persistAndGet);
    }

//    /**
//     * 根據政策id和獲取政策名稱(如果政策组对象或者政策组名称为空则抛出异常)
//     *
//     * @param policy 政策對象
//     * @return 最终获取到的政策组名称
//     */
//    public String getPolicyGroupName(Policy policy) {
//        String mainName = "";
//        PolicyGroup policyGroup = policy.getPolicyGroup();
//        if (policyGroup != null) {
//            mainName = policyGroup.getTitle();
//        } else {
//            throw new BootException("政策id为" + policy.getId() + "的政策组id为空");
//        }
//        if (StringUtils.isBlank(mainName)) {
//            throw new BootException("政策id为" + policy.getId() + "的政策组名称为空");
//        }
//        return mainName;
//    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, timeout = 2000, propagation = Propagation.REQUIRES_NEW)
    public PolicyTemplate init(PolicyTemplate tem) {
        PolicyActivity problemsEdit = new PolicyActivity();
        problemsEdit.setTemplate(tem);
        problemsEdit.setStart(true);
        problemsEdit.setMaxIndex(4);
        tem.setActivity(problemsEdit);
        return merge(tem);
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public boolean checkNotCreate(Long policyId) {
        List<Long> listObjByHql = dao.pageObjectByHql(
                "SELECT id FROM xinrui.cloud.domain.PolicyTemplate WHERE policy.id = ?0", Long.class, policyId);
        return CollectionUtils.isEmpty(listObjByHql);
    }

    @Override
    public PolicyTemplateDto editTemplate(PolicyTemplate temp, Long policyId) {
        Policy policy = policyService.findById(policyId);
        temp.getPolicy().setPolicyTemplate(null);
        temp.setPolicy(policy);
        PolicyGroup policyGroup = policy.getPolicyGroup();
        Assert.notNull(policyGroup, "政策组为空");
        String mainName = policyGroup.getTitle();
        temp.setMainName(mainName);
        return PolicyTemplateDto.copy(super.merge(temp));
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, timeout = 3000, propagation = Propagation.REQUIRES_NEW)
    public void removeAll(Long tempId) {
        PolicyTemplate findById = findById(tempId);
        findById.getActivity().setTemplate(null);
        findById.getPolicy().setPolicyTemplate(null);
        remove(findById);
    }

}
