package xinrui.cloud.util;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/7 10:32
 */
public class JudgeFactory {

    public static <T extends JudgeInterface> JudgeInterface newInstance(Class<T> judge) throws IllegalAccessException, InstantiationException {
        return judge.newInstance();
    }

}