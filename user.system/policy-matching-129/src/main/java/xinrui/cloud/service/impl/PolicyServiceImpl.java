package xinrui.cloud.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.*;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.enums.ProblemStatus;
import xinrui.cloud.service.PolicyService;
import xinrui.cloud.service.PolicyTemplateService;
import xinrui.cloud.service.ProblemService;
import xinrui.cloud.util.DataUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <B>Title:</B>PolicyServiceImpl.java</br>
 * <B>Description:</B> 政策相关 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
@Service
public class PolicyServiceImpl extends BaseServiceImpl<Policy> implements PolicyService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PolicyServiceImpl.class);

    @Autowired
    private PolicyTemplateService templateService;

    @Autowired
    private ProblemService problemService;

    @Override
    public List<PolicyGroupDto> listGroup(String uniSubject) {
        StringBuffer sql = new StringBuffer("SELECT g.id AS id,g.title AS title,g.small_pic AS small_pic,g.content AS content\n" +
                " FROM policy_match_group g LEFT JOIN zhjf_policy p ON g.id = p.policy_match_group_ship_id " +
                " LEFT JOIN policy_template e ON p.id = e.policy_id " +
                " LEFT JOIN policy_activity a ON a.template_id = e.id WHERE e.id IS NOT NULL AND a.end = true");
        ResultSetExtractor<List<PolicyGroupDto>> resultSetExtractor = new ResultSetExtractor<List<PolicyGroupDto>>() {
            @Override
            public List<PolicyGroupDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<PolicyGroupDto> groups = Lists.newArrayList();
                while (rs.next()) {
                    groups.add(new PolicyGroupDto(rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("small_pic"), rs.getString("content")));
                }
                return groups;
            }
        };
        if (StringUtils.isNotBlank(uniSubject)) {
            sql.append(" and e.unitSubject LIKE ? GROUP BY id");
            return dao.getJdbcTemplate().query(sql.toString(), resultSetExtractor, "%" + uniSubject + "%");
        } else {
            sql.append(" GROUP BY id");
            LOGGER.info("sql :[" + sql + " ]");
            List<PolicyGroupDto> query = dao.getJdbcTemplate().query(sql.toString(), resultSetExtractor);
            LOGGER.info("数组集合:" + query == null ? "" : query.toString());
            return query;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, timeout = 2000)
    public synchronized MatchtemplateDto createTemplate(final List<Long> groupIdList, String uniSubject) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("ids", groupIdList);
        params.put("uniSubject", "%" + uniSubject + "%");
        NamedParameterJdbcTemplate givenParamJdbcTemp = new NamedParameterJdbcTemplate(dao.getJdbcTemplate());
        List<Long> ids = givenParamJdbcTemp.query("SELECT t.id FROM policy_template t LEFT JOIN policy_activity a ON t.id = a.template_id\n" +
                " WHERE a.end = TRUE AND  t.unitSubject like :uniSubject  AND t.policy_id IN (SELECT p.id AS id FROM zhjf_policy p\n" +
                "\tLEFT JOIN policy_match_group g ON p.policy_match_group_ship_id = g.id WHERE g.id IN (:ids))", params, new ResultSetExtractor<List<Long>>() {
            @Override
            public List<Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Long> result = Lists.newArrayList();
                while (rs.next()) {
                    result.add(rs.getLong("id"));
                }
                return result;
            }
        });
        Assert.notEmpty(ids, "模板未找到!");
        LOGGER.info("查找到的政策id为[" + ids + "]");
        final long ago = System.currentTimeMillis();
        LOGGER.info("[开始创建模板]");
        final MatchtemplateDto matchTemplate = new MatchtemplateDto();
        Map<Long, List<PolicyTemplateSimpleDto>> maps = Maps.newHashMap();
        for (Long id : ids) {
            PolicyTemplate byId = templateService.findById(id);
            if (byId == null) {     //如果该模板不存在（因为之前已经是查询的环节完整的模板，所以不需要再次判断环节是否走完，）
                continue;
            }
            Policy policy = byId.getPolicy();
            Long groupId = policy.getPolicyGroup().getId();

            PolicyTemplateSimpleDto policyTemplateSimpleDto = new PolicyTemplateSimpleDto();
            PolicyDto policyDto = PolicyDto.copy(policy);
            policyDto.setDescription(byId.getDescription());
            policyTemplateSimpleDto.setPolicyDto(policyDto);
            policyTemplateSimpleDto.setProblems(ProblemSimpleDto.copyList(byId.getActivity().getProblems()));

            createPolicyTemplateDto(maps, groupId, policyTemplateSimpleDto);
        }
        add2GroupByMap(matchTemplate, maps);
        LOGGER.info("[结束创建模板，耗时为" + ((System.currentTimeMillis() - ago)) + "毫秒]\r\n\r\n");
        return matchTemplate;
    }

    /**
     * 生成{@link PolicyTemplateDto}对象，并且置入map；
     * 如果包含此政策组groupId的key不存在，创建一个key为groupId并且value为{@code  List<PolicyTemplateSimpleDto>}类型的keyvalue对
     *
     * @param maps
     * @param groupId
     * @param policyTemplateSimpleDto
     */
    private void createPolicyTemplateDto(Map<Long, List<PolicyTemplateSimpleDto>> maps, Long groupId, PolicyTemplateSimpleDto policyTemplateSimpleDto) {
        if (maps.containsKey(groupId)) {
            maps.get(groupId).add(policyTemplateSimpleDto);
        } else {
            List<PolicyTemplateSimpleDto> objects = Lists.newArrayList();
            objects.add(policyTemplateSimpleDto);
            maps.put(groupId, objects);
        }
    }

    /**
     * 通过map集合将maps里的所有政策组添加到dto中;</br>
     * 1、判断政策的所有问题遍及项是否全部为空，并且将其设置到{@link PolicyDto#setAllEmpty(boolean)}中
     * 2、判断政策组的所有政策的所有问题是否都空选项，并且将其设置到{@link PolicyGroupDto#setAllEmpty(boolean)}中
     * 3、判断所有的政策组里所有的政策的所有的问题编辑对象的状态是否为空选项，并且将其设置到{@link MatchtemplateDto#setAllEmpty(boolean)}中
     *
     * @param matchTemplate 政策匹配题目模板对象
     * @param maps          key为政策组id，value为当前政策组应该对应的所有政策对象集合
     */
    @Transactional
    public void add2GroupByMap(MatchtemplateDto matchTemplate, Map<Long, List<PolicyTemplateSimpleDto>> maps) {
        Set<Map.Entry<Long, List<PolicyTemplateSimpleDto>>> entries = maps.entrySet();
        boolean finalEmpty = true;
        for (Map.Entry<Long, List<PolicyTemplateSimpleDto>> entry : entries) {
            boolean groupEmpty = true;
            PolicyGroup byId = dao.getById(PolicyGroup.class, entry.getKey());
            PolicyGroupDto dto = new PolicyGroupDto(byId);
            List<PolicyTemplateSimpleDto> value = entry.getValue();
            for (PolicyTemplateSimpleDto simpleDto : value) {
                boolean policyEmpty = true;
                List<ProblemSimpleDto> problems = simpleDto.getProblems();
                for (ProblemSimpleDto pb : problems) {
                    if (pb.getStatus() != ProblemStatus.EMPTY.getStatus()) {
                        policyEmpty = false;
                        finalEmpty = false;
                        groupEmpty = false;
                        break;
                    }
                }
                simpleDto.getPolicyDto().setAllEmpty(policyEmpty);
            }
            dto.setTemps(value);
            dto.setAllEmpty(groupEmpty);
            matchTemplate.getGroups().add(dto);
        }
        matchTemplate.setAllEmpty(finalEmpty);
    }

    @Override
    public List<PolicyGroupDto> listAllGroup() {
        return dao.pageObjectByHql("select new xinrui.cloud.domain.dto.PolicyGroupDto(id,title) from xinrui.cloud.domain.PolicyGroup", PolicyGroupDto.class);
    }

    @Override
    public PageDto<List<PolicyDto>> listPolicyByGroupId(Long groupId, int currentPage, int pageSize) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Policy.class);
        if (groupId != null) {
            detachedCriteria.createAlias("policyGroup", "g").add(Restrictions.eq("g.id", groupId));
        }
        PageDto<List<Policy>> pg = new PageDto<>(--currentPage * pageSize, pageSize, detachedCriteria);
        dao.pageByCriteria(pg);
        return new PageDto<List<PolicyDto>>(pg.getTotalPage(), PolicyDto.copyList(pg.getT()));
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, timeout = 2000)
    public synchronized MatchResultDto match(MatchtemplateDto dto, Long companyId) {
        LOGGER.info("\r\n\r\n|==============================开始匹配，当前企业id为:{}==============================|", companyId);
        long ago = System.currentTimeMillis();
        MatchResultDto result = new MatchResultDto();   //匹配结果对象
        Double finalMoney = new Double(0);       //匹配最终获取的资助额度
        List<PolicyGroupDto> groups = dto.getGroups();  //政策组数据传输对象集合
        List<PolicyTemplateSimpleDto> policies = Lists.newArrayList();  //政策模板简单数据传输对象集合，里面一对一包含着政策对象
        List<DoubleMyself> policyMoneys = Lists.newArrayList();         //装载了所有的资助额度处理内部处理对象
        for (PolicyGroupDto group : groups) {
            Double groupMoney = null;
            List<PolicyTemplateSimpleDto> temps = group.getTemps();
            for (PolicyTemplateSimpleDto simple : temps) {
                LOGGER.info("\r\n======================================================================================");
                LOGGER.info("==============[一个政策:{}]================", simple.getPolicyDto().getShortTitle());
                LOGGER.info("======================================================================================\r\n");
                List<ProblemSimpleDto> problems = simple.getProblems();//获取到前端传输过来的json对象
                Double tempMoney;                               //最终政策获取到的总额度
                for (ProblemSimpleDto simpleDto : problems) {
                    LOGGER.info("[[一个问题编辑:{}]]\r\n", simpleDto.getId());
                    if (canContinue(simpleDto)) {
                        continue;
                    }
                    Problem problem = problemService.findById(simpleDto.getId());       //根据id查找问题编辑对象
                    if (problem == null) {
                        LOGGER.warn("当前问题编辑对象被删除或者被操作中，跳过!");
                        continue;
                    }
                    if (addToMoneyList(policyMoneys, problem)) {
                        continue;
                    }
                    boolean finished = false;
                    List<ProblemLimit> problemLimits = problem.getProblemLimits();      //获取所有的问题限制对象
                    //遍历当前所有的问题限制对象(外层，外层是控制内层真正公式的对象，即问题对应的5项问题限制)，获取符合条件的问题限制对象以便下一步操作
                    for (ProblemLimit limit : problemLimits) {                  //每一层问题限制如果符合则进行sql运算，然后进行触发结果和触发结构运算，最后累加资助金额
                        if (finished) {
                            break;
                        }
                        ProblemTrigger trigger = limit.getTrigger();             //在环节终结的情况下，依然存在某些限制项是没有完成的，逻辑是跳过匹配这些项
                        if (trigger == null) {
                            LOGGER.warn("当前问题编辑的触发结构未曾配置，请查看政府端配置problem:[" + problem + "]");
                            continue;
                        }
                        ProblemTriggerResult triggerResult = trigger.getTriggerResult();
                        if (triggerResult == null) {
                            LOGGER.warn("当前问题编辑的触发结构修饰未曾配置，请查看政府端配置;problem:[" + problem + "]");
                            continue;
                        }
                        StringBuffer finalLogicInner = new StringBuffer();
                        add2FinalLogicInner(limit, finalLogicInner);
                        boolean canContinue = StringUtils.isBlank(finalLogicInner.toString());  //默认问题限制的公式为空，则可以进行跳过
                        if (canContinue) {
                            LOGGER.info("当前问题限制的公式为空,默认为通过问题限制。");
                        }
                        // 外层的问题限制针对于整个问题编辑项，而外部的问题限制集合上的每一个则对应了触发结构
                        if (canContinue || judgeProblemModel(limit.getVariables(), finalLogicInner, companyId, simpleDto)) {  //可进行触发结构运算
                            Boolean query = null;
                            if (!canContinue) {
                                query = getLogicResultOfLimit(finalLogicInner);
                            }
                            LOGGER.info("当前问题限制公式的运算结果为:" + query);
                            if (canContinue || query != null && query) {
                                if (add2FinalLogicInner(trigger, finalLogicInner, simpleDto)) {       //找到选中项，生成原公式
                                    LOGGER.info("触发结构公式生成成功!\r\n");
                                    simpleDto.setCanContinue(true);
                                    if (judgeProblemModel(new ProblemLimitInner(trigger.getVariables()), finalLogicInner, companyId, simpleDto)) {
                                        LOGGER.info("触发结构的公式替换成功\r\n");
                                        Double logicResultOfTrigger = getLogicResultOfTrigger(finalLogicInner, simpleDto);
                                        if (logicResultOfTrigger == null && !simpleDto.isCanContinue()) {
                                            continue;
                                        }
                                        LOGGER.info("当前触发结果公式的运算结果为:" + logicResultOfTrigger);
                                        String logic = getTriggerLogic(triggerResult, simpleDto);
                                        LOGGER.info("触发结果修饰的公式为:" + logic);
                                        DoubleMyself doubleMyself = policyMoneys.get(policyMoneys.size() - 1);  //获取最新的一个，也就是当前的这个DoubleMyself对象
                                        if (doubleMyself != null) {
                                            doubleMyself.setTruthMoney(logicResultOfTrigger);
                                            doubleMyself.setLogicMoney(parseDouble(logic));
                                        } else {
                                            LOGGER.error("自定义内部资助额处理对象为空!");
                                            LOGGER.info(policyMoneys.toString());
                                        }
                                        finished = true;
                                        LOGGER.info("\r\n==当前问题编辑对应的资助额获取完毕!==\r\n");
                                    }
                                }
                            }
                        }
                    }
                }
                if ((tempMoney = getPolicyMoney(policyMoneys)) != null) {
                    groupMoney = initDouble(groupMoney);
                    tempMoney = DataUtil.str2Double(tempMoney);
                    groupMoney += tempMoney;
                    if (tempMoney > 0) {
                        PolicyDto copy = PolicyDto.copy(findById(simple.getPolicyDto().getId()));
                        copy.setMoney(DataUtil.double2Str(tempMoney));
                        setPolicyParagraph(copy);
                        policies.add(new PolicyTemplateSimpleDto(copy));
                    }
                }
                policyMoneys.clear();
            }
            if (groupMoney != null) {
                finalMoney += groupMoney;
                if (groupMoney > 0) {
                    PolicyGroupDto policyGroupDto = new PolicyGroupDto(dao.getById(PolicyGroup.class, group.getId()));
                    policyGroupDto.getTemps().addAll(policies);
                    policyGroupDto.setMoney(DataUtil.double2Str(groupMoney));
                    result.getGroups().add(policyGroupDto);
                }
            }
            policies.clear();
        }
        setFinalMoney(result, finalMoney);
        LOGGER.info("==============================匹配完成，最终耗时为:" + (System.currentTimeMillis() - ago) + "毫秒==============================\n\n\n\n\n\n");
        return result;
    }

    /**
     * @param policyMoneys
     * @param problem
     * @return
     */
    private boolean addToMoneyList(List<DoubleMyself> policyMoneys, Problem problem) {
        if (problem.getStatus() == ProblemStatus.SINGLE.getStatus()) {
            LOGGER.info("当前模板的类型为[独选]");
            String name = problem.getName();
            Double aDouble = parseDouble(name);
            if (problem.isAmplitude()) {
                LOGGER.info("当前独选的作用为:[对当前政策所有模板进行幅度上涨]");
                policyMoneys.add(new DoubleMyself(true, false, aDouble));
            } else {
                boolean last = problem.getLast();
                LOGGER.info("当前独选的作用为:[{}]", last ? "针对上一个模板进行幅度上涨" : "仅仅获取资助额");
                if (last) {
                    policyMoneys.add(new DoubleMyself(true, aDouble));
                } else {
                    policyMoneys.add(new DoubleMyself());
                    return false;
                }
            }
            return true;
        } else {
            LOGGER.info("当前模板的类型为[非独选]");
            policyMoneys.add(new DoubleMyself());
        }
        return false;
    }

    /**
     * Set Policy Paragraph with {@link PolicyDto}
     *
     * @param policyDto {@link PolicyDto} must not be null;
     */
    private void setPolicyParagraph(PolicyDto policyDto) {
        Assert.notNull(policyDto, "政策传输对象不能为空");
        String query = dao.getJdbcTemplate().query("select content from zhjf_policy_paragraph where policy_id =?", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getString(1);
                }
                return null;
            }
        }, policyDto.getId());
        policyDto.setDescription(query);

    }

    /**
     * <p/>根据自定义{@link DoubleMyself}对象获取政策最终匹配到的金额
     *
     * <ul>1、区分所有幅度上涨的对象（独选）和非幅度上涨的对象（非独选）；
     * 如果是独选：
     * <li>1.1、独选如果是仅仅针对上一个问题编辑项的，则调用{@link DoubleMyself#setLastIndex(int)}函数，设置上一个{@link DoubleMyself}对象的索引。
     * <li>1.2、如果不是仅仅针对上一个问题编辑项的，则直接添加如独选集合中。
     * 如果是非独选:直接添加到非独选集合中。</ul>
     *
     * <ul>2、将获取到所有幅度上涨对象集合遍历:
     * <li>2.1、如果是仅仅针对上一个问题编辑项的，判断触发结果的资助额度{@link DoubleMyself#getTruthMoney()}是否大于{@link DoubleMyself#getLogicMoney()},
     * 再将上一个问题编辑项获取到的最高限制额度{@link DoubleMyself#getLogicMoney()}设置成幅度上涨后的资助金额;
     * <li>2.2、将所有独选的上涨幅度额度叠加</ul>
     *
     * <ul>3.遍历所有非幅度上涨对象集合，并且根据叠加额度进行幅度上涨；</ul>
     *
     * @param policyMoneys 当前政策获取的所有资助额度处理对象
     * @return 最终获取到的额度
     */
    private Double getPolicyMoney(List<DoubleMyself> policyMoneys) {
        Double tempMoney = null;
        if (!CollectionUtils.isEmpty(policyMoneys)) {
            tempMoney = new Double(0D);
            Double singleMoney = null;
            DoubleMyself oneUpMoneyDoubleMyself = null;
            List<DoubleMyself> notSingleList = Lists.newArrayList();
            for (int i = 0; i < policyMoneys.size(); i++) {
                DoubleMyself doubleMyself = policyMoneys.get(i);
                if (doubleMyself.isSingle) {
                    if (doubleMyself.isOnlySingle) {
                        if (doubleMyself.isLast()) {
                            oneUpMoneyDoubleMyself = policyMoneys.get(i - 1);
                        }
                    }
                    singleMoney = doubleMyself.getPercentage();
                } else {
                    notSingleList.add(doubleMyself);
                }
            }
            if (oneUpMoneyDoubleMyself == null) {
                for (DoubleMyself doubleMyself : notSingleList) {
                    tempMoney += upMoney(singleMoney, doubleMyself);
                }
            } else {
                for (int i = 0; i < policyMoneys.size(); i++) {
                    DoubleMyself doubleMyself = policyMoneys.get(i);
                    if (doubleMyself == oneUpMoneyDoubleMyself) {
                        tempMoney += upMoney(singleMoney, doubleMyself);
                    } else {
                        tempMoney += upMoney(null, doubleMyself);
                    }
                }
            }
            LOGGER.info("=====当前非单选的卡片集合为:{}", notSingleList.toString());
            LOGGER.info("=====当前独选的上涨百分比为:{}", singleMoney);
        }
        return tempMoney;
    }


    /**
     * 将资助额度进行幅度上涨
     *
     * @param singleMoney 幅度资助金额，可以为空
     * @param money       需要涨幅的原本金额
     * @return 涨幅后的金额
     */
    private Double upMoney(Double singleMoney, DoubleMyself money) {
        Double resultMoney = new Double(0D);
        Double truthMoney = money.getTruthMoney();
        Double logicMoney = money.getLogicMoney();
        boolean singleMoneyGreaterZero = greaterZeroAndNotNull(singleMoney);
        boolean truthMoneyGreaterZero = greaterZeroAndNotNull(truthMoney);
        boolean logicMoneyGreaterZero = greaterZeroAndNotNull(logicMoney);
        if (truthMoneyGreaterZero && singleMoneyGreaterZero) {
            truthMoney *= (singleMoney / 100);
        }
        if (logicMoneyGreaterZero && singleMoneyGreaterZero) {
            logicMoney *= (singleMoney / 100);
        }
        if (truthMoneyGreaterZero && logicMoneyGreaterZero) {
            if (truthMoney >= logicMoney) {
                resultMoney += logicMoney;
                LOGGER.info("当前获取的实际金额为最高限制的值:{}", logicMoney);
            } else {
                resultMoney += truthMoney;
                LOGGER.info("当前获取的实际金额为触发结果的值:{}", truthMoney);
            }
        } else if (truthMoneyGreaterZero) {
            resultMoney += truthMoney;
            LOGGER.info("当前获取的实际金额为触发结果的值:{}", truthMoney);
        }/* else if (logicMoneyGreaterZero) {
            resultMoney += truthMoney;
            LOGGER.info("当前获取的实际金额为最高限制的值:{}", truthMoney);
        }*/
        return resultMoney;
    }

    /**
     * double不为null并且大于0
     *
     * @param doubleVar {@link Double}
     * @return 匹配返回true，否则返回false
     */
    private boolean greaterZeroAndNotNull(Double doubleVar) {
        return doubleVar != null && doubleVar > 0D;
    }

    /**
     * <ul>以前的幅度上涨
     * <li/>当实际触发超过最高限制，按照百分比上涨最高限制的值。
     * <li/>当上涨后的金额低于最高限制，则按照当前上涨后的实际金额为资助金额。
     * <li/>当上涨后的金额高于最高限制，取最高限制。
     * </ul>
     *
     * @param tempMoney
     * @param singleMoney
     * @param singleMoneyNotNull
     * @param truthMoney
     * @param logicMoney
     * @return
     */
    @Deprecated
    private Double addMoney(Double tempMoney, Double singleMoney, boolean singleMoneyNotNull, Double truthMoney, Double logicMoney) {
        boolean logicMoneyGreaterZero = logicMoney != null && logicMoney > 0;
        if (singleMoneyNotNull && logicMoneyGreaterZero) {
            logicMoney *= (singleMoney / 100);
            LOGGER.info("当前公式生成的值为:" + logicMoney);
        }
        if (truthMoney == null || truthMoney <= 0) {
            LOGGER.info("当前触发结果获取到的资助额度为空，或者获取到的资助额度是负数或者0 ；truthMoney: {}", truthMoney);
            return tempMoney;
        } else {
            LOGGER.info("进入百分比上涨");
            if (logicMoneyGreaterZero) {
                LOGGER.info("触发结果金额为:{}", truthMoney);
                LOGGER.info("触发结果修饰的金额为:{}", logicMoney);
                if (truthMoney < logicMoney) {
                    tempMoney += truthMoney;
                } else {
                    tempMoney += logicMoney;
                }
            } else {
                LOGGER.info("上涨公式为空，跳过!");
                tempMoney += truthMoney;
            }
        }
        return tempMoney;
    }

    /**
     * 根据百分比，触发结果修饰的资助额度和处罚结果的资助额度将触发结果修饰获取幅度上涨之后的触发结果修饰资助额度
     *
     * @param percentage
     * @param logicMoney
     * @param truthMoney
     * @return
     */
    private Double getPercentageValue(Double percentage, Double logicMoney, Double truthMoney) {
        if (truthMoney == null || logicMoney == null) {
            LOGGER.warn("当前获取到的触发结果或者触发结果修饰的资助额度为空");
            return null;
        } else {
            if (truthMoney > logicMoney) {
                logicMoney *= (percentage / 100);
            }
        }
        return logicMoney;
    }

    /**
     * 将str解析成Double类型的对象，如果解析失败会输出一段日志，最终返回一个不为空的Double对象
     *
     * @param str 数字字符串
     * @return 解析后的Double对象
     */
    private Double parseDouble(String str) {
        Double db = new Double(0);
        if (StringUtils.isBlank(str) || str == null || "null".equals(str)) {
            return db;
        }
        try {
            db = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            LOGGER.info("解析成double类型失败");
        }
        return db;
    }

    /**
     * 初始化传入的Double对象，如果传入的对象为空，重新创建一个Double类型的对象，反之直接返回当前对象
     *
     * @param groupMoney 如果为空new一个，否则返回当前的
     * @return 最终得到一个不为空的Double对象
     */
    private Double initDouble(Double groupMoney) {
        if (groupMoney == null) {
            groupMoney = new Double(0);
        }
        return groupMoney;
    }

    /**
     * 设置最终获得的资助，如果最终获得资助小于0，则将所有的政策组集合清空
     *
     * @param result     匹配结果数据传输对象
     * @param finalMoney 最终得到的金额
     */
    private void setFinalMoney(MatchResultDto result, Double finalMoney) {
        if (finalMoney < 0) {
            result.setMoney(DataUtil.double2Str(0D));
            result.getGroups().clear();
            LOGGER.warn("最终得到的资助额度为负数，请查看政府端公式配置！");
        } else {
            result.setMoney(DataUtil.double2Str(finalMoney));
            LOGGER.info("最终得到的资助额度为:[" + finalMoney + "]");
        }
    }


    /**
     * 根据问题编辑简单格式数据传输对象内的index，{@link ProblemSimpleDto#getIndex()}实际上里面的值是在程序前面获取到的index，获取到指定的最高限制的公式
     *
     * @param triggerResult 问题触发结构修饰对象
     * @param simpleDto     问题编辑简单类型数据传输对象
     * @return 最终获取到的最高限制
     */
    private String getTriggerLogic(ProblemTriggerResult triggerResult, ProblemSimpleDto simpleDto) {
        boolean single = isSingle(simpleDto);
        StringBuffer buffer = new StringBuffer();
        if (single) {
            ProblemModelDto problemModelDto = new ProblemModelDto();
            problemModelDto.setIndex(simpleDto.getIndex());
            if (findByIndex(triggerResult, buffer, problemModelDto)) {
                return null;
            }
        } else {
            return triggerResult.getLogic();
        }
        return buffer.toString();
    }

    /**
     * 判断是否是独选并且没有选中任何项({@link ProblemSimpleDto#getValue()}里的value为false或者为空代表没有选中，反之代表选中
     *
     * @param simpleDto 问题编辑简单类型数据传输对象
     * @return 是独选并且没选中返回true，不是独选或者是独选并且未选中false
     */
    private boolean canContinue(ProblemSimpleDto simpleDto) {
        if (simpleDto.getStatus() == ProblemStatus.SINGLE.getStatus()) {
            String value = simpleDto.getValue();
            if (StringUtils.isBlank(value) || "false".equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接问题限制的公式，将左边的公式和连接符号和右边的公式再连接上and or
     *
     * @param limit           问题限制对象
     * @param finalLogicInner 公式字符串缓冲对象
     */
    @Override
    public void add2FinalLogicInner(ProblemLimit limit, StringBuffer finalLogicInner) {
        LOGGER.info("--开始拼接问题限制的公式--");
        List<ProblemLimit> children = limit.getChildren();
        for (ProblemLimit limitChild : children) {
            finalLogicInner.append(limitChild.getLogicalFormLeft()).append(limitChild.getConnector()).append(limitChild.getLogicalFormRight()).append(" ").append(limitChild.getLogic()).append(" ");
        }
        LOGGER.info("--拼接完毕，公式为:[" + finalLogicInner.toString() + "]--\r\n");
    }

    public List<CompanyInfoDto> queryCompanyByName(String sql, String name) {
        return dao.getJdbcTemplate().query(sql, new ResultSetExtractor<List<CompanyInfoDto>>() {
            @Override
            public List<CompanyInfoDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<CompanyInfoDto> result = Lists.newArrayList();
                while (rs.next()) {
                    CompanyInfoDto companyInfoDto = new CompanyInfoDto(rs.getLong(1),
                            rs.getString(2));
//                    try {
//                        int addressIndex = rs.findColumn("address");
//                        if (addressIndex > 0) {
//                            companyInfoDto.setAddress(rs.getString(addressIndex));
//                        }
//                    } catch (SQLException e) {
//
//                    }
                    result.add(companyInfoDto);
                }
                return result;
            }
        }, name);
    }

    @Override
    public List<CompanyInfoDto> findInfoByName(String name) {
        List<CompanyInfoDto> result = queryCompanyByName("select id,name from tu_company where name like ? ", "%" + name + "%");
        result.addAll(queryCompanyByName("select id,companyName from policy_other_company where companyName like ?", "%" + name + "%"));
        return result;
    }

    @Override
    public CompanyInfoDto addCompany(String companyName, String concatName, String concatPhone) {
        List<CompanyInfoDto> result = queryCompanyByName("select id,name from tu_company where name = ? limit 1", companyName);
        result.addAll(queryCompanyByName("select id,companyName from policy_other_company where companyName = ? limit 1", companyName));
        if (!CollectionUtils.isEmpty(result)) {
            throw new BootException("企业已经存在!");
        }
        OtherCompany merge = dao.merge(OtherCompany.class, new OtherCompany(companyName, concatName, concatPhone));
        return new CompanyInfoDto(merge.getId(), merge.getCompanyName());
    }


    /**
     * 获取触发器的最终公式
     *
     * @param trigger         触发器对象
     * @param finalLogicInner 公式字符串缓冲对象
     */
    private boolean add2FinalLogicInner(ProblemTrigger trigger, StringBuffer finalLogicInner, ProblemSimpleDto simpleDto) {
        finalLogicInner.setLength(0);
        String value = simpleDto.getValue();
        if (StringUtils.isBlank(value) && simpleDto.getStatus() != ProblemStatus.EMPTY.getStatus()) {
            return false;
        }
        boolean isSingle = isSingle(simpleDto);
        ProblemModelDto result = null;
        if (!isSingle) {
            LOGGER.info("选中的不是单选项");
            String logic = trigger.getLogic();
            if (StringUtils.isBlank(logic)) {
                LOGGER.info("当前填入的公式为空，公式作废!");
                return false;
            }
            finalLogicInner.append(logic);
        } else {
            LOGGER.info("选中的为单选项");
            List<ProblemModelDto> child = simpleDto.getChild();
            if (CollectionUtils.isEmpty(child)) {
                LOGGER.info("单选项没有一级选项");
                return false;
            }
            ProblemModelDto firstChooseObject = getFirstChooseObject(simpleDto);
            if (firstChooseObject == null) {
                LOGGER.info("单选项没有选中一级选项");
                return false;
            }
            List<ProblemModelDto> firstChildren = child.get(0).getChildren();
            if (CollectionUtils.isEmpty(firstChildren) || simpleDto.getChildStatus() == ProblemStatus.TEXT.getStatus()) {
                LOGGER.info("一级选项单选项或者是二级填空项");
                result = firstChooseObject;
            } else {
                LOGGER.info("二级选项为单选项!");
                String chooseChildId = simpleDto.getChooseChildId();
                if (StringUtils.isBlank(chooseChildId) || (result = getProblemModelDto(firstChooseObject, chooseChildId)) == null) {
                    LOGGER.info("未曾找到选中的单选二级选项");
                    return false;
                }
            }
        }
        if (finalLogicInner.length() == 0) {
            //通过层级寻找公式
            try {
                if (findByIndex(trigger, finalLogicInner, result)) {
                    return false;
                }
            } catch (Exception e) {
                LOGGER.error("当前选中的下标不存在配置的公式，跳过生成！");
                return false;
            }
            simpleDto.setIndex(result.getIndex());    //将标表示层级的字符串放入simpleDto的描述中
        }
        LOGGER.info("最终生成的公式为:" + finalLogicInner.toString());
        return true;
    }

    /**
     * 通过index寻找与前端传送过来一样的结构的后端触发结构内部对象的公式，index的格式为0,1...或者0-1,0-2......
     *
     * @param trigger         触发结构抽象对象
     * @param finalLogicInner 公式字符串缓冲对象
     * @param result          最终选中的来自前端的问题编辑的子选项数据传输对象
     * @return 如果未曾找到返回true，找到了对象并且成功替换公式返回false
     */
    private boolean findByIndex(ProblemTriggerFather trigger, StringBuffer finalLogicInner, ProblemModelDto result) {
        String index = result.getIndex();
        String logTwo = null;
        if (index.contains("-")) {
            String[] split = index.split("-");
            String first = split[0];
            String second = split[1];
            try {
                ProblemTriggerInner problemTriggerInner = trigger.getChild().get(Integer.parseInt(first)).getChildren().get(Integer.parseInt(second));
                if (problemTriggerInner == null) {
                    return true;
                }
                logTwo = problemTriggerInner.getLogicTwo();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ProblemTriggerInner problemTriggerInner = trigger.getChild().get(Integer.parseInt(index));
                if (problemTriggerInner == null) {
                    return true;
                }
                logTwo = problemTriggerInner.getLogicTwo();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        finalLogicInner.append(logTwo);
        return false;
    }

    /**
     * 获取选中的一级项
     *
     * @param simpleDto 前端传送json解析而成的问题编辑简单类型传输对象
     * @return 最终选中的二级选项，如果未找到返回null
     */
    public ProblemModelDto getFirstChooseObject(ProblemSimpleDto simpleDto) {
        String value = simpleDto.getValue();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        List<ProblemModelDto> child = simpleDto.getChild();
        if (!CollectionUtils.isEmpty(child)) {
            for (int i = 0; i < child.size(); i++) {
                ProblemModelDto dto = child.get(i);
                if (value.equals(dto.getId().toString())) {          //代表匹配到选中的一级项了
                    dto.setIndex(i + "");
                    return dto;
                }
            }
        }
        return null;
    }

    /**
     * 获取选中的二级项
     *
     * @param simpleDto 前端传送json解析而成的问题编辑简单类型传输对象
     * @return 最终选中的二级选项，如果未找到返回null
     */
    @Deprecated
    public ProblemModelDto getSecondChooseObject(ProblemSimpleDto simpleDto) {
        ProblemModelDto dto = getFirstChooseObject(simpleDto);
        if (dto != null) {
            String value = dto.getValue();
            if (StringUtils.isBlank(value)) {
                return null;
            }
            ProblemModelDto dto2 = getProblemModelDto(dto, value);
            if (dto2 != null) {
                return dto2;
            }
        }
        return null;
    }

    /**
     * 通过指定id与value比较，对dto的子集的每一个元素的id做匹配，如果匹配到了则返回当前model，否则返回null
     *
     * @param dto   一级选项
     * @param value 希望被匹配到的id
     * @return 最终匹配到的对象
     */
    private ProblemModelDto getProblemModelDto(ProblemModelDto dto, String value) {
        List<ProblemModelDto> children = dto.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            for (int i = 0; i < children.size(); i++) {
                ProblemModelDto dto2 = children.get(i);
                if (value.equals(dto2.getId().toString())) { //代表匹配到了选中的二级选项
                    dto2.setIndex(dto.getIndex() + "-" + i);
                    return dto2;
                }
            }
        }
        return null;
    }


    /**
     * 根据公式生成的sql获取最终的问题限制的结果
     *
     * @param finalLogicInner sql公式
     * @return true或者false或者null
     */
    private Boolean getLogicResultOfLimit(StringBuffer finalLogicInner) {
        Boolean query = null;
        try {
            query = dao.getJdbcTemplate().query(finalLogicInner.toString(), new ResultSetExtractor<Boolean>() {

                @Override
                public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        return rs.getBoolean(1);
                    }
                    return null;
                }
            });
        } catch (DataAccessException e) {
            LOGGER.warn("问题限制公式sql查询出错!");
        }
        return query;
    }

    /**
     * 根据trigger公式生成的sql获取最终的结果
     *
     * @param finalLogicInner sql公式
     * @return Double或者null
     */
    private Double getLogicResultOfTrigger(StringBuffer finalLogicInner, ProblemSimpleDto dto) {
        Double query = null;
        if (dto.isCanContinue()) {
            try {
                if (StringUtils.isNotBlank(finalLogicInner.toString())) {
                    query = Double.parseDouble(finalLogicInner.toString());
                } else {
                    LOGGER.info("触发结构的公式为空!");
                    return null;
                }
            } catch (Exception e) {
                LOGGER.info("解析为DOUBLE出错!");
                return null;
            }
        }
        try {
            query = dao.getJdbcTemplate().query(finalLogicInner.toString(), new ResultSetExtractor<Double>() {

                @Override
                public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        return rs.getDouble(1);
                    }
                    return null;
                }
            });
        } catch (DataAccessException e) {
            LOGGER.warn("触发结构公式sql查询出错!");
        }
        return query;
    }

    /**
     * 判断当前公式是否有合法变量填充，如果无返回false，否则返回tru;</br>
     * 并且按照给出的变量和公式将公式替换成填写的变量;</br>
     * 1、如果是问题限制调用,一旦公式不为空，并且变量为空，则返回false，相当于公式作废;</br>
     * 2、如果是触发结构调用，则需要判断，如果当前的变量集合和公式都为空则可以跳过，代表配置的公式仅仅是一个自己写死的数字，或者公式，不包含变量</br>
     *
     * @param singleVariable  控制变量的父节点
     * @param finalLogicInner 最终生成的公式
     * @param companyId       企业id
     * @param simpleDto       问题编辑简单类型数据传输对象
     * @return 最终判断的结果
     */
    public boolean judgeProblemModel(ProblemLimitInner singleVariable, StringBuffer finalLogicInner, Long companyId, ProblemSimpleDto simpleDto) {

        LOGGER.info("当前的问题编辑id为:[" + simpleDto.getId() + "]");
        LOGGER.info("当前获取的变量父节点的id:[" + singleVariable.getId() + "]");

        //获取当前题目的所有变量对象，分为两类，负整数的代表是组件库内的id，正整数的代表是填空类型题目二级项或者填空类型题目的id
        List<ProblemLimitInner> variableChildren = singleVariable.getChildren();
        List<ProblemLimitInner> baseInfos = Lists.newArrayList();
        List<ProblemLimitInner> variables = Lists.newArrayList();
        for (ProblemLimitInner inner : variableChildren) {
            Long variableId = inner.getVariableId();
            if (variableId == null) {
                continue;
            }
            if (variableId < 0) {
                baseInfos.add(inner);
            } else {
                variables.add(inner);
            }
        }
        LOGGER.info("当前的值为:{}", finalLogicInner.toString());
        LOGGER.info("公式是否为空{}", "null".equals(finalLogicInner.toString()) + "");

        if (!simpleDto.isCanContinue()) {
            if (CollectionUtils.isEmpty(variables) && CollectionUtils.isEmpty(baseInfos) && StringUtils.isNotBlank(finalLogicInner.toString()) && !"null".equals(finalLogicInner.toString())) {
                LOGGER.warn("当前待区分的变量为空，并且当前的公式并不为空，公式作废!");
                return false;
            }
        }

        boolean canContinue = CollectionUtils.isEmpty(variables) && CollectionUtils.isEmpty(baseInfos) && simpleDto.isCanContinue();
        if (canContinue) {
            LOGGER.info("当前变量为空，并且是触发结构那块，所以跳过;");
            return true;
        }

        LOGGER.info("\r\n区分两种变量对象类型完毕:\r\n\t来自于组件库的变量有:{}\r\n\t来自于填空或者单选填空的变量有:{}", baseInfos, variables);

        //通过组件库查询企业基本信息
        if (queryComponentData(finalLogicInner, companyId, baseInfos)) {
            return false;
        }

        LOGGER.info("当前选项的一级项的状态是:" + simpleDto.getStatus());
        LOGGER.info("当前选项的二级项的状态是:" + simpleDto.getChildStatus());

        //当前填空填写的变量
        if (queryProblemLimitInner(finalLogicInner, simpleDto, variables)) {
            return false;
        }
        replaceSpecialStr(finalLogicInner);
        LOGGER.info("最终生成的公式是:" + finalLogicInner.toString() + "\r\n");

        String s = finalLogicInner.toString();
        finalLogicInner.setLength(0);
        finalLogicInner.append("select ").append(s);
        simpleDto.setCanContinue(false);
        LOGGER.info("--结束替换公式--\r\n");
        return true;
    }

    /**
     * 查找再政府端填写的变量的对象的值并且替换到当前公式中去，分别是单选，独选，填空和空等选项；
     * 函数返回true代表公式作废，函数返回false代表公式正常替换成功
     *
     * @param finalLogicInner 公式字符串缓冲对象
     * @param simpleDto       问题编辑简单类型传输对象
     * @param variables       当前来自于政府端编写的政策匹配的匹配规则对象
     * @return 如果公式替换成功返回false，否则返回true
     */
    private boolean queryProblemLimitInner(StringBuffer finalLogicInner, ProblemSimpleDto simpleDto, List<ProblemLimitInner> variables) {
        if (finalLogicInner.length() != 0 && StringUtils.isNotBlank(finalLogicInner.toString())) {  //公式不为空才进入，否则直接跳过此步
            boolean isSingle = isSingle(simpleDto);
            if (!isSingle) {        //因为是经过筛选过的，所以非单选的只能是填空填写的变量，并且vid必定是probvlem的id否则公式作废
                LOGGER.info("[非单选]");
                for (ProblemLimitInner inner : variables) {
                    Long variableId = inner.getVariableId();
                    if (variableId.intValue() != simpleDto.getId().intValue()) {
                        LOGGER.info("当前的变量id不等于当前问题编辑的id，则公式作废！");
                        return true;
                    } else {
                        String value = simpleDto.getValue();
                        if (StringUtils.isBlank(value)) {      //一旦匹配上了id但是填写的值为空也作废公式
                            LOGGER.info("当前变量的值为空，公式作废！");
                            return true;
                        }
                        replaceBuffer(finalLogicInner, inner, value);
                    }
                }
            } else {
                LOGGER.info("[单选]");
                ProblemModelDto firstChooseObject = getFirstChooseObject(simpleDto);
                if (firstChooseObject == null) {
                    LOGGER.info("单选未曾选中任何选项!");
                    return true;
                }
                List<ProblemModelDto> children = firstChooseObject.getChildren();
                for (ProblemModelDto dto : children) {
                    Long id = dto.getId();
                    if (id == null) {
                        LOGGER.warn("数据错误，二级项id为null");
                    } else {
                        String value = dto.getValue();
                        for (ProblemLimitInner inner : variables) {
                            Long variableId = inner.getVariableId();
                            if (variableId == null) {
                                continue;
                            }
                            if (id.intValue() == variableId.intValue()) {
                                if (StringUtils.isNotBlank(value)) {
                                    replaceBuffer(finalLogicInner, inner, value);
                                } else {
                                    LOGGER.info("当前二级填空项的值为空，公式作废!");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 将来自于组件库的所有变量遍历并且替换到公式中，如果当前的变量为空或者变量不存在，则返回true，代表公式作废，否则返回false；
     *
     * @param finalLogicInner 公式字符串缓冲对象
     * @param companyId       企业id
     * @param baseInfos       装载着企业基本信息的变量集合
     * @return 如果查找到了变量并且替换成功，则返回false，否则返回true
     */
    private boolean queryComponentData(StringBuffer finalLogicInner, Long companyId, List<ProblemLimitInner> baseInfos) {
        for (ProblemLimitInner inner : baseInfos) {
            LOGGER.info("[组件项id =]" + Math.abs(inner.getVariableId()) + "[ 企业id=]" + companyId);
            String currentVb = queryForComponent(Math.abs(inner.getVariableId()), companyId);
            if (StringUtils.isBlank(currentVb)) {       //从组件库替换变量
                LOGGER.info("当前公式的内容为:[" + finalLogicInner.toString() + "] 而当前遍历的变量的名称为:" + inner.getName());
                LOGGER.info("当前企业未曾填写组件信息，组件字段名称为:" + inner.getName());
                LOGGER.info("当前公式是否包含当前遍历的此变量名称:" + finalLogicInner.toString().contains(inner.getName()));
                if (finalLogicInner.toString().contains(inner.getName())) { //忽略当前基本信息，如果公式内包含此对象则此限制不通过
                    LOGGER.warn("当前公式包含企业未曾填写的基本信息，公式作废!");
                    return true;
                }
            } else {
                replaceBuffer(finalLogicInner, inner, currentVb);
            }
        }
        return false;
    }

    /**
     * 最终将特殊的大于等于和小于等于符号替换成sql可用的字符
     *
     * @param finalLogicInner 公式字符串缓冲对象
     */
    private void replaceSpecialStr(StringBuffer finalLogicInner) {
        String replace = finalLogicInner.toString().replace("≦", "<=").replace("≧", ">=");
        finalLogicInner.setLength(0);
        finalLogicInner.append(replace);
    }

    /**
     * 判断当前问题编辑简单类型对象是否单选类型
     *
     * @param simpleDto 问题编辑简单类型对象
     * @return 如果当前的问题编辑的status为单选，返回true，否则返回false
     */
    private boolean isSingle(ProblemSimpleDto simpleDto) {
        return simpleDto.getStatus() == ProblemStatus.SINGLE_RADIO.getStatus();
    }

    /**
     * 将装载着公式内容的Stringbuffer对象地字符串内容，按照变量名替换成当前查找到的值
     *
     * @param finalLogicInner 公式字符串缓冲对象
     * @param inner           装着变量的对象
     * @param currentVb       当前的变量
     */
    private void replaceBuffer(StringBuffer finalLogicInner, ProblemLimitInner inner, String currentVb) {
        LOGGER.info("\r\n[进入替换公式函数]\r\n\t当前的公式函数为:" + finalLogicInner + "\r\n\t当前的内部变量对象名称为:" + inner.getName() + "\r\n\t+当前查找的变量的值为:" + currentVb);
        LOGGER.info("替换前的公式为:" + finalLogicInner.toString());
        String replace = finalLogicInner.toString().replace(inner.getName(), currentVb);//将当前变量关联
        finalLogicInner.setLength(0);
        finalLogicInner.append(replace);
        LOGGER.info("替换后的公式为:" + finalLogicInner.toString());
    }

    /**
     * 根据组件项id和企业id获取到当前企业填写此项的值
     *
     * @param itemId    组件项id
     * @param companyId 企业id
     * @return 最终获取的值，如果没有返回null
     */
    private String queryForComponent(Long itemId, Long companyId) {
        if (companyId == null) {
            return null;
        }
        Object objBySql = dao.getObjBySql("select value from zhjf_component_data where item_id=? and company_id=?", itemId, companyId);
        if (objBySql == null) {
            return null;
        }
        return objBySql.toString();
    }

    /**
     * 内部计算资助金额用的类
     */
    class DoubleMyself {
        /**
         * 触发结果资助额度
         */
        Double truthMoney;
        /**
         * 触发结果修饰资助额度
         */
        Double logicMoney;
        /**
         * 独选的时候的上涨百分比
         */
        Double percentage;
        /**
         * 标识是否是独选，true为是，false为否
         */
        boolean isSingle = false;
        /**
         * 标识是否是否是针对上一个问题编辑项，true为是，false为否
         */
        boolean last = false;
        /**
         * 上一个问题遍及项的索引，默认为-1
         */
        int lastIndex = -1;

        /**
         * 仅仅是独选（非幅度上涨）
         */
        boolean isOnlySingle = false;

        /**
         * 生成一个独选的{@link DoubleMyself}对象，仅仅针对上一个资助的。
         *
         * @param last       是否是针对上一个问题编辑项，true为是，false为否
         * @param percentage 独选上涨的幅度 120 100 50 这种
         */
        public DoubleMyself(boolean last, Double percentage) {
            this.percentage = percentage;
            this.isSingle = true;
            this.isOnlySingle = true;
            this.last = last;
        }

        public DoubleMyself() {
        }

        public DoubleMyself(boolean isOnlySingle) {
            this.isOnlySingle = isOnlySingle;
        }

        public DoubleMyself(boolean isSingle, boolean isOnlySingle, Double percentage) {
            this.isSingle = isSingle;
            this.isOnlySingle = isOnlySingle;
            this.percentage = percentage;
        }

        public Double getTruthMoney() {
            return truthMoney;
        }

        public void setTruthMoney(Double truthMoney) {
            this.truthMoney = truthMoney;
        }

        public Double getLogicMoney() {
            return logicMoney;
        }

        public void setLogicMoney(Double logicMoney) {
            this.logicMoney = logicMoney;
        }

        public boolean isSingle() {
            return isSingle;
        }

        public void setSingle(boolean single) {
            isSingle = single;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public int getLastIndex() {
            return lastIndex;
        }

        public void setLastIndex(int lastIndex) {
            this.lastIndex = lastIndex;
        }

        public boolean isOnlySingle() {
            return isOnlySingle;
        }

        public void setOnlySingle(boolean onlySingle) {
            isOnlySingle = onlySingle;
        }

        @Override
        public String toString() {
            return "DoubleMyself{" +
                    "truthMoney=" + truthMoney +
                    ", logicMoney=" + logicMoney +
                    ", percentage=" + percentage +
                    ", isSingle=" + isSingle +
                    ", last=" + last +
                    ", lastIndex=" + lastIndex +
                    '}';
        }
    }

}
