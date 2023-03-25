package club.javafamily.prom.conf;

import club.javafamily.nf.properties.FeiShuProperties;
import club.javafamily.utils.Tool;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Jack Li
 * @date 2023/3/25 下午11:34
 * @description
 */
@Component
public class FeiShuConfigListener implements ApplicationListener<ApplicationStartedEvent> {

   private final FeiShuProperties properties;

   public FeiShuConfigListener(FeiShuProperties properties) {
      this.properties = properties;
   }

   @Override
   public void onApplicationEvent(ApplicationStartedEvent event) {
      if(StringUtils.hasText(properties.getHookUrl())
         && !(properties.getHookUrl().startsWith("http://")
         || properties.getHookUrl().startsWith("https://")))
      {
         properties.setHookUrl(Tool.appendPath("https://open.feishu.cn/open-apis/bot/v2/hook/",
            properties.getHookUrl()));
      }
   }
}
