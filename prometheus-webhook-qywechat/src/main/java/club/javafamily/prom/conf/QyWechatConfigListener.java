package club.javafamily.prom.conf;

import club.javafamily.nf.properties.QyWechatProperties;
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
public class QyWechatConfigListener implements ApplicationListener<ApplicationStartedEvent> {

   private final QyWechatProperties properties;

   public QyWechatConfigListener(QyWechatProperties properties) {
      this.properties = properties;
   }

   @Override
   public void onApplicationEvent(ApplicationStartedEvent event) {
      fixHookUrl(properties);
   }

   public static void fixHookUrl(QyWechatProperties properties) {
      if(StringUtils.hasText(properties.getHookUrl())
              && !(properties.getHookUrl().startsWith("http://")
              || properties.getHookUrl().startsWith("https://")))
      {
         properties.setHookUrl(Tool.appendPath(
                 "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=",
                 properties.getHookUrl()));
      }
   }
}
