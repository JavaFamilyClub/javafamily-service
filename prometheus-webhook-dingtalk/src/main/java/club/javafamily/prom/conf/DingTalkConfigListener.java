package club.javafamily.prom.conf;

import club.javafamily.nf.properties.DingTalkProperties;
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
public class DingTalkConfigListener implements ApplicationListener<ApplicationStartedEvent> {

   private final DingTalkProperties properties;

   public DingTalkConfigListener(DingTalkProperties properties) {
      this.properties = properties;
   }

   @Override
   public void onApplicationEvent(ApplicationStartedEvent event) {
      if(StringUtils.hasText(properties.getHookUrl())
         && !(properties.getHookUrl().startsWith("http://")
         || properties.getHookUrl().startsWith("https://")))
      {
         properties.setHookUrl("https://oapi.dingtalk.com/robot/send?access_token=" + properties.getHookUrl());
      }
   }
}
