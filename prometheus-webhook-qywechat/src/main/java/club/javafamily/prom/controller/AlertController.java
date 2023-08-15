package club.javafamily.prom.controller;

import club.javafamily.nf.properties.QyWechatProperties;
import club.javafamily.nf.request.QyWechatMarkdownNotifyRequest;
import club.javafamily.nf.request.QyWechatNewsNotifyRequest;
import club.javafamily.nf.request.QyWechatTextNotifyRequest;
import club.javafamily.nf.service.InhibitPolicy;
import club.javafamily.nf.service.QyWechatNotifyHandler;
import club.javafamily.prom.conf.QyWechatConfigListener;
import club.javafamily.prom.constant.AlertConstant;
import club.javafamily.prom.constant.SystemConstant;
import club.javafamily.prom.enums.AlertStatusEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Jack Li
 * @date 2022/7/17 下午4:30
 * @description
 */
@RestController
@Slf4j
public class AlertController {

   private final QyWechatNotifyHandler notifyHandler;
   private final RestTemplate restTemplate;

   private final InhibitPolicy inhibitPolicy;

   public AlertController(QyWechatNotifyHandler notifyHandler,
                          RestTemplate restTemplate,
                          @Autowired(required = false) InhibitPolicy inhibitPolicy)
   {
      this.notifyHandler = notifyHandler;
      this.restTemplate = restTemplate;
      this.inhibitPolicy = inhibitPolicy;
   }

   @PostMapping("/alert/text")
   public String alertText(@RequestBody String body) {
      JSONObject jsonObject = parseAnnotation(body);

      String text = parseContent(jsonObject,
         AlertConstant.CONTENT_TEMPLATE, AlertConstant.CONTENT_KEY);

      AlertStatusEnum status = getStatus(body);

      // 故障恢复 title
      if(status == AlertStatusEnum.RESOLVED) {
         jsonObject.put(AlertConstant.TITLE_RESULT, text);

         text = parseTemplateOrDefault(
            jsonObject, AlertConstant.RESOLVED_TITLE_TEMPLATE,
            AlertConstant.DEFAULT_RESOLVED_TITLE);
      }

      log.info("alertText Request received, text is {} \n", text);

      final QyWechatTextNotifyRequest request
         = QyWechatTextNotifyRequest.of(text);

      final String token = jsonObject.getString(AlertConstant.TOKEN_KEY);

      final String response = getQyWechatNotifyHandler(token).notify(request);

      log.info("\n alertText response is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/mk")
   public String alertMarkdown(@RequestBody String body) {
      JSONObject jsonObject = parseAnnotation(body);

      String text = parseContent(jsonObject,
              AlertConstant.CONTENT_TEMPLATE, AlertConstant.CONTENT_KEY);

      AlertStatusEnum status = getStatus(body);

      // 故障恢复 title
      if(status == AlertStatusEnum.RESOLVED) {
         jsonObject.put(AlertConstant.TITLE_RESULT, text);

         text = parseTemplateOrDefault(
                 jsonObject, AlertConstant.RESOLVED_TITLE_TEMPLATE,
                 AlertConstant.DEFAULT_RESOLVED_TITLE);
      }

      log.info("alertMarkdown Request received, markdown is {} \n", text);

      final QyWechatMarkdownNotifyRequest request
              = QyWechatMarkdownNotifyRequest.of(text);

      final String token = jsonObject.getString(AlertConstant.TOKEN_KEY);

      final String response = getQyWechatNotifyHandler(token).notify(request);

      log.info("\n alertMarkdown response is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/news")
   public String alertNews(@RequestBody String body) {
      JSONObject jsonObject = parseAnnotation(body);

      String title = parseContent(
         jsonObject, AlertConstant.TITLE_TEMPLATE, AlertConstant.TITLE_KEY);

      AlertStatusEnum status = getStatus(body);

      // 故障恢复 title
      if(status == AlertStatusEnum.RESOLVED) {
         jsonObject.put(AlertConstant.TITLE_RESULT, title);

         title = parseTemplateOrDefault(
            jsonObject, AlertConstant.RESOLVED_TITLE_TEMPLATE,
            AlertConstant.DEFAULT_RESOLVED_TITLE);
      }

      String content = parseContent(
         jsonObject, AlertConstant.CONTENT_TEMPLATE, AlertConstant.CONTENT_KEY);
      String picUrl = parseContent(jsonObject, null, AlertConstant.PIC_URL_KEY);
      String link = parseContent(jsonObject, null, AlertConstant.BTN_LINK_KEY);

      log.info("alertNews Request received, title result is {}, content is {}, pic url is {}, link is {} \n",
         title, content, picUrl, link);

      final QyWechatNewsNotifyRequest request = QyWechatNewsNotifyRequest.of(
              title, content, picUrl, link);

      final String token = jsonObject.getString(AlertConstant.TOKEN_KEY);

      final String response = getQyWechatNotifyHandler(token).notify(request);

      log.info("\n alertNews response is : {}\n", request);

      return response;
   }

   /**
    * 获取报警状态
    * @param body request body
    * @return status
    */
   public static AlertStatusEnum getStatus(String body) {
      try {
         JSONObject jsonObject = JSONObject.parseObject(body);

         if(jsonObject == null ||
            StringUtils.isEmpty(jsonObject.getString(SystemConstant.STATUS_KEY)))
         {
            return AlertStatusEnum.FIRING;
         }

         return EnumUtils.getEnum(AlertStatusEnum.class,
            jsonObject.getString(SystemConstant.STATUS_KEY).toUpperCase());
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return AlertStatusEnum.FIRING;
   }

   /**
    * 获取 Prometheus annotation 内容
    * @param body 请求体
    * @return commonAnnotations
    */
   private JSONObject parseAnnotation(String body) {
      final JSONObject json = JSONObject.parseObject(body);

      log.info("Request received, body is: \n\n{}\n\n",
         json.toJSONString());

      return json.getJSONObject(AlertConstant.ANNO_KEY);
   }

   /**
    * 获取飞书通知管理器
    * @param token 自定义 token
    * @return QyWechatNotifyHandler
    */
   @NonNull
   private QyWechatNotifyHandler getQyWechatNotifyHandler(@Nullable String token) {
      if(ObjectUtils.isEmpty(token)) {
         return notifyHandler;
      }

      final QyWechatProperties properties = new QyWechatProperties();
      properties.setHookUrl(token);

      QyWechatConfigListener.fixHookUrl(properties);

      return new QyWechatNotifyHandler(properties, restTemplate, inhibitPolicy);
   }

   /**
    * 格式化内容
    * @param jsonObject commonAnnotations
    * @param templateKey 模板 key
    * @param valueKey 值key
    * @return 格式化后的内容
    */
   private String parseContent(JSONObject jsonObject,
                               String templateKey,
                               String valueKey)
   {
      if(ObjectUtils.isEmpty(templateKey)
              || ObjectUtils.isEmpty(jsonObject.getString(templateKey)))
      {
         return jsonObject.getString(valueKey);
      }

      String template = jsonObject.getString(templateKey);

      Map<String, Object> params = jsonObject.getInnerMap();

      return StringSubstitutor.replace(template, params);
   }

   /**
    * 格式化内容
    * @param jsonObject commonAnnotations
    * @param templateKey 模板 key
    * @param defaultTemplate 默认模板
    * @return 格式化后的内容
    */
   private String parseTemplateOrDefault(JSONObject jsonObject,
                                         String templateKey,
                                         String defaultTemplate)
   {
      String template;

      if(ObjectUtils.isEmpty(templateKey)
         || ObjectUtils.isEmpty(jsonObject.getString(templateKey)))
      {
         template = defaultTemplate;
      }
      else {
         template = jsonObject.getString(templateKey);
      }

      Map<String, Object> params = jsonObject.getInnerMap();

      return StringSubstitutor.replace(template, params);
   }

}

