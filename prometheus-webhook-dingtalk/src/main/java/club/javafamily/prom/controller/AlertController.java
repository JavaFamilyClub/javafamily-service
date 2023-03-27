package club.javafamily.prom.controller;

import club.javafamily.nf.properties.DingTalkProperties;
import club.javafamily.nf.request.DingTalkNotifyRequest;
import club.javafamily.nf.request.card.multi.DingTalkMultiBtnCardRequest;
import club.javafamily.nf.request.card.single.DingTalkSingleBtnCardRequest;
import club.javafamily.nf.request.link.DingTalkLinkRequest;
import club.javafamily.nf.request.markdown.DingTalkMarkDownRequest;
import club.javafamily.nf.request.tags.CardBtn;
import club.javafamily.nf.request.text.DingTalkTextNotifyRequest;
import club.javafamily.nf.service.DingTalkNotifyHandler;
import club.javafamily.prom.constant.AlertConstant;
import club.javafamily.prom.constant.SystemConstant;
import club.javafamily.prom.enums.AlertStatusEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Jack Li
 * @date 2022/7/17 下午4:30
 * @description
 */
@RestController
@Slf4j
public class AlertController {

   private final DingTalkNotifyHandler DingTalkNotifyHandler;
   private final RestTemplate restTemplate;

   public AlertController(DingTalkNotifyHandler DingTalkNotifyHandler,
                          RestTemplate restTemplate)
   {
      this.DingTalkNotifyHandler = DingTalkNotifyHandler;
      this.restTemplate = restTemplate;
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

      final boolean atAll = ObjectUtils.nullSafeEquals(Boolean.TRUE,
         parseTemplateOrDefault(jsonObject,
            AlertConstant.AT_ALL, null));

      final String atInfo = parseTemplateOrDefault(jsonObject,
         AlertConstant.AT_USER_PHONE, null);
      final String[] atUserPhones = StringUtils.hasText(atInfo) ? atInfo.split(",") : null;

      log.info("Request received, text is {}, atall is {}, atInfo: {}\n",
         text, atAll, atInfo);

      DingTalkNotifyRequest request = DingTalkTextNotifyRequest.of(
         text, atAll, atUserPhones);

      final String token = jsonObject.getString(AlertConstant.HANDLER_TOKEN_KEY);

      final String response = getDingTalkNotifyHandler(token).notify(request);

      log.info("\n alertText response is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/link")
   public String alertLink(@RequestBody String body) {
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
      String link = parseContent(jsonObject, null, AlertConstant.BTN_LINK_KEY);

      final String picUrl = parseTemplateOrDefault(jsonObject, AlertConstant.LINK_PIC_URL, null);

      log.info("Request received, title is {}, content is {}, link is {} pic url is: {}\n",
         title, content, link, picUrl);

      final DingTalkNotifyRequest request = DingTalkLinkRequest.of(
         title, content, link, picUrl);

      final String token = jsonObject.getString(AlertConstant.HANDLER_TOKEN_KEY);

      final String response = getDingTalkNotifyHandler(token).notify(request);

      log.info("\n alertPost response is : {}\n", request);

      return response;
   }


   @PostMapping("/alert/mk")
   public String alertMarkdown(@RequestBody String body) {
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

      final boolean atAll = ObjectUtils.nullSafeEquals(Boolean.TRUE,
         parseTemplateOrDefault(jsonObject,
            AlertConstant.AT_ALL, null));

      final String atInfo = parseTemplateOrDefault(jsonObject,
         AlertConstant.AT_USER_PHONE, null);
      final String[] atUserPhones = StringUtils.hasText(atInfo) ? atInfo.split(",") : null;

      log.info("Request received, title is {}, content is {}, atall is {}, atInfo: {}\n",
         title, content, atAll, atInfo);

      final DingTalkNotifyRequest request = DingTalkMarkDownRequest.of(
         title, content, atAll, atUserPhones);

      final String token = jsonObject.getString(AlertConstant.HANDLER_TOKEN_KEY);

      final String response = getDingTalkNotifyHandler(token).notify(request);

      log.info("\n alertPost response is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/card/single")
   public String alertCard(@RequestBody String body) {
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
      String btnLabel = parseContent(jsonObject, null, AlertConstant.BTN_LABEL_KEY);
      String btnLink = parseContent(jsonObject, null, AlertConstant.BTN_LINK_KEY);

      log.info("Request received, title result is {}, content is {}, label is {}, link is {} \n",
         title, content, btnLabel, btnLink);

      final DingTalkNotifyRequest request = DingTalkSingleBtnCardRequest.of(
         title,
         content,
         btnLabel,
         btnLink);

      final String token = jsonObject.getString(AlertConstant.HANDLER_TOKEN_KEY);

      final String response = getDingTalkNotifyHandler(token).notify(request);

      log.info("\n alertCard response is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/card/multi")
   public String alertMultiCard(@RequestBody String body) {
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
      String btnLabels = parseContent(jsonObject, null, AlertConstant.BTN_LABELS_KEY);
      String btnLinks = parseContent(jsonObject, null, AlertConstant.BTN_LINKS_KEY);

      log.info("Request received, title result is {}, content is {}, label is {}, link is {} \n",
         title, content, btnLabels, btnLinks);

      List<CardBtn> cardBtns = new ArrayList<>();

      if(StringUtils.hasText(btnLabels)) {
         String[] labels = btnLabels.split(AlertConstant.SPLIT_FLAG);
         final String[] links =
            btnLinks == null ? new String[0] : btnLinks.split(AlertConstant.SPLIT_FLAG);

         for (int i = 0; i < labels.length; i++) {
            if(i >= links.length) {
               break;
            }

            cardBtns.add(CardBtn.builder()
               .title(labels[i])
               .actionURL(links[i])
               .build());
         }
      }

      DingTalkNotifyRequest request = DingTalkMultiBtnCardRequest.of(
         title,
         content,
         cardBtns.toArray(new CardBtn[0]));

      final String token = jsonObject.getString(AlertConstant.HANDLER_TOKEN_KEY);

      final String response = getDingTalkNotifyHandler(token).notify(request);

      log.info("\n alertCard response is : {}\n", request);

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
    * @return DingTalkNotifyHandler
    */
   @NonNull
   private DingTalkNotifyHandler getDingTalkNotifyHandler(@Nullable String token) {
      if(ObjectUtils.isEmpty(token)) {
         return DingTalkNotifyHandler;
      }

      final DingTalkProperties properties = new DingTalkProperties();

      if(token.startsWith("http://") || token.startsWith("https://")) {
         properties.setHookUrl(token);
      }
      else {
         properties.setHookUrl(
            "https://open.feishu.cn/open-apis/bot/v2/hook/" + token);
      }

      return new DingTalkNotifyHandler(properties, restTemplate, null);
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

      if(template == null) {
         return null;
      }

      return StringSubstitutor.replace(template, params);
   }

}

