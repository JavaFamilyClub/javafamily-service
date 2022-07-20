package club.javafamily.prom.controller;

import club.javafamily.nf.request.FeiShuCardNotifyRequest;
import club.javafamily.nf.request.FeiShuPostNotifyRequest;
import club.javafamily.nf.request.FeiShuTextNotifyRequest;
import club.javafamily.nf.request.tags.BaseTextTagContentItem;
import club.javafamily.nf.request.tags.LinkTagContentItem;
import club.javafamily.nf.service.FeiShuNotifyHandler;
import club.javafamily.prom.constant.SystemConstant;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Jack Li
 * @date 2022/7/17 下午4:30
 * @description
 */
@RestController
@Slf4j
public class AlertController {

   private final FeiShuNotifyHandler feiShuNotifyHandler;

   public AlertController(FeiShuNotifyHandler feiShuNotifyHandler) {
      this.feiShuNotifyHandler = feiShuNotifyHandler;
   }

   @PostMapping("/alert/text")
   public String alertText(@RequestBody String body) {
      final JSONObject json = JSONObject.parseObject(body);

      JSONObject jsonObject = json.getJSONObject(SystemConstant.ANNO_KEY);

      String text = parseContent(jsonObject, SystemConstant.CONTENT_TEMPLATE, SystemConstant.CONTENT_KEY);

      log.info("Request received, body is: \n\n{}\n\n, text is {} \n",
         json.toJSONString(), text);

      final FeiShuTextNotifyRequest request
         = FeiShuTextNotifyRequest.of(text);

      final String response = feiShuNotifyHandler.notify(request);

      log.info("\n alertText response is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/post")
   public String alertPost(@RequestBody String body) {
      final JSONObject json = JSONObject.parseObject(body);

      JSONObject jsonObject = json.getJSONObject(SystemConstant.ANNO_KEY);

      String title = parseContent(jsonObject, SystemConstant.TITLE_TEMPLATE, SystemConstant.TITLE_KEY);
      String content = parseContent(jsonObject, SystemConstant.CONTENT_TEMPLATE, SystemConstant.CONTENT_KEY);
      String btnLabel = parseContent(jsonObject, null, SystemConstant.BTN_LABEL_KEY);
      String btnLink = parseContent(jsonObject, null, SystemConstant.BTN_LINK_KEY);

      log.info("Request received, body is: \n\n{}\n\n, title is {}, content is {}, label is {}, link is {} \n",
              json.toJSONString(), title, content, btnLabel, btnLink);

      final FeiShuPostNotifyRequest request = FeiShuPostNotifyRequest.of(
         title,
         new BaseTextTagContentItem<>(content),
         new LinkTagContentItem(btnLabel, btnLink));

      String response = feiShuNotifyHandler.notify(request);

      log.info("\n alertPost response is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/card")
   public String alertCard(@RequestBody String body) {
      final JSONObject json = JSONObject.parseObject(body);

      JSONObject jsonObject = json.getJSONObject(SystemConstant.ANNO_KEY);

      String title = parseContent(jsonObject, SystemConstant.TITLE_TEMPLATE, SystemConstant.TITLE_KEY);
      String content = parseContent(jsonObject, SystemConstant.CONTENT_TEMPLATE, SystemConstant.CONTENT_KEY);
      String btnLabel = parseContent(jsonObject, null, SystemConstant.BTN_LABEL_KEY);
      String btnLink = parseContent(jsonObject, null, SystemConstant.BTN_LINK_KEY);

      log.info("Request received, body is: \n\n{}\n\n, title is {}, content is {}, label is {}, link is {} \n",
              json.toJSONString(), title, content, btnLabel, btnLink);

      final FeiShuCardNotifyRequest request = FeiShuCardNotifyRequest.of(
              title, content, btnLabel, btnLink);

      String response = feiShuNotifyHandler.notify(request);

      log.info("\n alertCard response is : {}\n", request);

      return response;
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

}

