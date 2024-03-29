package xinrui.cloud.filter;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author liuliuliu
 */
@Configuration("serviceInfoUtil")
public class ServiceInfoUtil implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private static EmbeddedServletContainerInitializedEvent event;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        ServiceInfoUtil.event = event;
    }

    public static int getPort() {
        assert event == null : "初始化EmbeddedServletContainerInitializedEvent失败!";
        int port = event.getEmbeddedServletContainer().getPort();
        Assert.state(port != -1, "端口号获取失败");
        return port;
    }
}
