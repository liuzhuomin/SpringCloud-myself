package xinrui.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import xinrui.cloud.domain.Policy;
import xinrui.cloud.domain.PolicyTemplate;
import xinrui.cloud.domain.dto.PolicyDto;
import xinrui.cloud.domain.dto.PolicyGroupDto2;
import xinrui.cloud.service.PolicyService;
import xinrui.cloud.service.PolicyTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xinrui.cloud.domain.PolicyGroup;
import xinrui.cloud.service.PolicyGroupServcice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <B>Title:</B>PolicyGroupImpl.java</br>
 * <B>Description:</B> 政策组相关 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
@Service
public class PolicyGroupServiceImpl extends BaseServiceImpl<PolicyGroup> implements PolicyGroupServcice {

    private final static Logger LOGGER = LoggerFactory.getLogger(PolicyServiceImpl.class);

    @Autowired
    PolicyService policyService;

    @Autowired
    PolicyTemplateService policyTemplateService;

    @Override
    @Transactional
    public List<PolicyGroupDto2> editGroup(String groupJson) {
        List<PolicyGroupDto2> result = Lists.newArrayList();
        if (StringUtils.isEmpty(groupJson)) {
            removeByList(findAll());
            return result;
        }
        List<PolicyGroupDto2> parseArray = JSON.parseArray(groupJson, PolicyGroupDto2.class);
        if (CollectionUtils.isEmpty(parseArray)) {
            removeByList(findAll());
            return result;
        } else {
            detachedRefrence();
            List<Long> notRemoveIdList = Lists.newArrayList();
            for (int i = 0; i < parseArray.size(); i++) {
                if (i == 0) {
                    PolicyGroupDto2 policyGroupDto2 = parseArray.get(0);
                    List<PolicyDto> policies = policyGroupDto2.getPolicies();
                    for (PolicyDto o : policies) {
                        Policy byId = policyService.findById(o.getId());
                        byId.setPolicyGroup(null);
                        policyService.merge(byId);
                    }
                    continue;
                }
                PolicyGroupDto2 o = parseArray.get(i);
                Long id = o.getId();
                if (id == null) {
                    PolicyGroup policyGroup = new PolicyGroup();
                    BeanUtils.copyProperties(o, policyGroup, "id", "policies");
                    LOGGER.info(policyGroup == null ? "policy group is null" : policyGroup.toString());
                    notRemoveIdList.add(mergePolicy(o, merge(policyGroup)).getId());
                } else {
                    PolicyGroup policyGroup = findById(id);
                    BeanUtils.copyProperties(o, policyGroup, "id", "policies");
                    PolicyGroup policyGroup1 = mergePolicy(o, merge(policyGroup));
                    notRemoveIdList.add(policyGroup1.getId());
                }
            }
            //TODO 删除掉所有政策组id不在上面政策组id集合中的政策组
            List<PolicyGroup> objects = dao.listByHql("from xinrui.cloud.domain.PolicyGroup where id not in ?0", PolicyGroup.class, notRemoveIdList);
            LOGGER.info(objects.toString() + "");
            removeByList(objects);
        }
        return listGroup();
    }

    private void detachedRefrence() {
        List<PolicyGroup> all = findAll();
        for (PolicyGroup g : all) {
            g.getPolicies().clear();
            merge(g);
        }
//        List<Policy> all = policyService.findAll();
//        for (Policy p:all){
//            p.setPolicyGroup(null);
//            policyService.merge(p);
//        }
    }

    @Override
    public List<PolicyGroupDto2> listGroup() {
        List<PolicyGroup> all = findAll();
        PolicyGroup haveNotGroup = new PolicyGroup(0L, "未分组政策");
        haveNotGroup.setPolicies(policyService.listByHql("from xinrui.cloud.domain.Policy p where p.policyGroup is null"));
        all.add(0, haveNotGroup);
        return PolicyGroupDto2.copyFrom(all);
    }

    private PolicyGroup mergePolicy(PolicyGroupDto2 o, PolicyGroup merge) {
        List<PolicyDto> policies = o.getPolicies();
        for (PolicyDto policyDto : policies) {
            Policy byId = policyService.findById(policyDto.getId());
            byId.setPolicyGroup(merge);
            merge.getPolicies().add(byId);
        }
        return merge(merge);
    }

    private void removeByList(List<PolicyGroup> all) {
        for (PolicyGroup pd : all) {
            List<Policy> policies = pd.getPolicies();
            for (Policy o : policies) {
                o.setPolicyGroup(null);
                Policy mergePolicy = policyService.merge(o);
                PolicyTemplate policyTemplate = mergePolicy.getPolicyTemplate();
                policyTemplate.setPolicy(null);
                policyTemplateService.remove(policyTemplate);
                mergePolicy.setPolicyTemplate(null);
                policyService.merge(mergePolicy);
            }
            remove(pd);
        }
    }

}
