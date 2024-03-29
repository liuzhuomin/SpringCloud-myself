package xinrui.cloud.service.impl;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import xinrui.cloud.domain.Problem;
import xinrui.cloud.service.ProblemService;

/**
 * <B>Title:</B>ProblemServiceImpl.java</br>
 * <B>Description:</B> 问题编辑实现类 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月29日
 */
@Service
@PropertySource("classpath:policy.properties")
public class ProblemServiceImpl extends BaseServiceImpl<Problem> implements ProblemService {

}
