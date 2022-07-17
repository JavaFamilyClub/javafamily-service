package club.javafamily.prom.controller;

import club.javafamily.nf.request.FeiShuPostNotifyRequest;
import club.javafamily.nf.request.FeiShuTextNotifyRequest;
import club.javafamily.nf.request.tags.BaseTextTagContentItem;
import club.javafamily.nf.request.tags.LinkTagContentItem;
import club.javafamily.nf.service.FeiShuNotifyHandler;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

      final String text = json.getJSONObject("commonAnnotations")
         .getString("description");

      log.info("Request received, body is: \n{}\n, text is {} \n",
         json.toJSONString(), text);

      final FeiShuTextNotifyRequest request
         = FeiShuTextNotifyRequest.of(text);

      final String response = feiShuNotifyHandler.notify(request);

      log.info("\nRequestResponse is : {}\n", request);

      return response;
   }

   @PostMapping("/alert/post")
   public String alertPost(@RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam("btnLabel") String btnLabel,
                           @RequestParam("btnLink") String btnLink)
   {
      final FeiShuPostNotifyRequest request = FeiShuPostNotifyRequest.of(
         title,
         new BaseTextTagContentItem<>(content),
         new LinkTagContentItem(btnLabel, btnLink));

      return feiShuNotifyHandler.notify(request);
   }

}

