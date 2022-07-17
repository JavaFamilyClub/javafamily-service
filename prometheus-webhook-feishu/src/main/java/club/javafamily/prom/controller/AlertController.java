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

   @GetMapping("/alert/text")
   public String alertText(@RequestBody String body) {
      log.info("Request received, body is: \n{}\n", body);
      final JSONObject json = JSONObject.parseObject(body);

      final FeiShuTextNotifyRequest request
         = FeiShuTextNotifyRequest.of(
            json.getJSONObject("commonAnnotations").getString("text"));

      return feiShuNotifyHandler.notify(request);
   }

   @GetMapping("/alert/post")
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

