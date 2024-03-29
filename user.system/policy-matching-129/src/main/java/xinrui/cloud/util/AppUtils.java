package xinrui.cloud.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import xinrui.cloud.BootException;
import xinrui.cloud.controller.PolicyTemplateController;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.PolicyActivity;
import xinrui.cloud.filter.ServiceInfoUtil;
import xinrui.cloud.service.ActivityService;
import xinrui.cloud.service.impl.BaseServiceImpl;

@Service
@DependsOn("serviceInfoUtil")
@PropertySource("classpath:policy.properties")
public class AppUtils extends BaseServiceImpl<IdEntity> {

    private Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);

    /**
     * 标识是否需要跳过从组件获取企业基本数据()
     */
    @Value("${table.ignore}")
    private boolean ignore;

    /**
     * 需要查找的标题类目
     */
    @Value("${table.titles}")
    private String titles;

    /**
     * 问题限制最大个数
     */
    @Value("${child.size.max}")
    public int maxSize = 5;

    /**
     * 待查找的这些组件的组件id
     */
    @Value("${table.componentIds}")
    public String componentIds;

    private ActivityService activityService;

    private static ServiceInfoUtil serviceInfoUtil;

    private List<Map<String, Object>> result = Lists.newArrayList();

    private static Logger logger = LoggerFactory.getLogger(PolicyTemplateController.class);

    public AppUtils(@Autowired ServiceInfoUtil serviceInfoUtil, @Autowired ActivityService activityService) {
        super();
        AppUtils.serviceInfoUtil = serviceInfoUtil;
        this.activityService = activityService;
    }

    @SuppressWarnings("static-access")
    public static String getRealUrl(String path) {
        try {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            InetAddress localHost = InetAddress.getLocalHost();
            String url = "http://" + localHost.getHostAddress() + ":" + ServiceInfoUtil.getPort() + "/" + path;
            return url;
        } catch (UnknownHostException e) {
            logger.warn("获取端口和path失败");
            logger.warn(e.getMessage());
        }
        return null;
    }


    /**
     * 处理环节，进行到下一步</br>
     * 将当前的环节流程步数+1如果到达了设置的最高流程则设置环节已经结束;</br>
     * 如果当前环节流程部署+1超过了最高环节步数则抛出异常;
     *
     * @param activity 环节流程对象
     */
    public synchronized void doProcess(PolicyActivity activity) {
        activity.setCurrentIndex(activity.getCurrentIndex() + 1);
        int currentIndex = activity.getCurrentIndex();
        int maxIndex = activity.getMaxIndex();
        if (currentIndex > maxIndex) {
            throw new BootException("当前环节操作超过了环节最大限制");
        }if (currentIndex == maxIndex) {
            activity.setEnd(true);
        }
        activityService.merge(activity);
    }

    List<String> idList = null;

    List<String> titleList = null;

    public List<Map<String, Object>> getResult() {
        if (CollectionUtils.isEmpty(idList)) {
            idList = DataUtil.split(componentIds);
        }
        if (CollectionUtils.isEmpty(titleList)) {
            List<String> split = DataUtil.split(titles);
            titleList = Lists.newArrayList();
            for (String title : split) {
                titleList.add(DataUtil.iso2Utf8(title));
            }
        }
        if (CollectionUtils.isEmpty(result)) {
            LOGGER.info("是否跳过从数据库查询企业基本信息:" + ignore);
            if (!ignore) {
                LOGGER.info("开始从组件库查找数据");
                long ago = System.currentTimeMillis();
                result.addAll(queryComponent(143508L, "2018年", false, true));
                result.addAll(queryComponent(143475L, "注册资本", true, false));
                result.addAll(queryComponent(143508L, "增长率（2017年-2018年）", true, false));
                List<Map<String, Object>> realyResult = Lists.newArrayList();
                for (Map<String, Object> map : result) {
                    Map<String, Object> realyMap = Maps.newHashMap();
                    Set<Map.Entry<String, Object>> entries = map.entrySet();
                    Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> next = iterator.next();
                        String key = next.getKey();
                        if ("id".equals(key)) {
                            Long l = Long.parseLong(next.getValue().toString());
                            if (l == null || StringUtils.isBlank(key)) {
                                continue;
                            }
                            realyMap.put(key, -l);
                        } else {
                            realyMap.put(key, next.getValue());
                        }

                    }
                    realyResult.add(realyMap);
                }
                result = realyResult;
                LOGGER.info("查找组件数据完成,耗时" + (System.currentTimeMillis() - ago) + "毫秒");
            }
        } else {
            LOGGER.info("当前result已缓存数据，跳过查找;");
        }
        return result;
    }

    private List<Map<String, Object>> queryComponent(Long component_id, String title, boolean needParent, boolean needChild) {
        List<Map<String, Object>> parentMap = dao.getJdbcTemplate().queryForList("select id,title as name from zhjf_component_item where component_id=? and title=?", component_id, title);
        if (CollectionUtils.isEmpty(parentMap) || parentMap.size() != 1) {
            return Lists.newArrayList();
        }
        LOGGER.warn("组件数据查找成功：" + "component_id = [" + component_id + "], title = [" + title + "]");
        Object value = parentMap.get(0).entrySet().iterator().next().getValue();
        long parentId = Long.parseLong(value.toString());
        String sql = "select id,title as name from zhjf_component_item where parent_id=:parentId and title in (:titles)";
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dao.getJdbcTemplate());
        Map<String, Object> param = Maps.newHashMap();
        param.put("parentId", parentId);
        param.put("titles", titleList);
        List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(sql, param);

        LOGGER.warn("sql：" + sql.replace(":parentId", parentId + "").replace(":titles", titleList.toString()));
        LOGGER.warn("子项数据查找成功，个数为：" + maps.size());
        if (needParent && needChild) {
            if (!CollectionUtils.isEmpty(maps)) {
                parentMap.addAll(maps);
            }
            return parentMap;
        } else if (needParent) {
            return parentMap;
        } else if (needChild) {
            return maps;
        } else {
            return Lists.newArrayList();
        }
    }

    public int getMaxSize() {
        return maxSize;
    }

}
