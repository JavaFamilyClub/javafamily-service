package club.javafamily.prom.controller;

import club.javafamily.nf.request.FeiShuTextNotifyRequest;
import club.javafamily.nf.service.FeiShuNotifyHandler;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jack Li
 * @date 2022/7/17 下午4:30
 * @description
 */
@RestController
public class AlertController {

   private final FeiShuNotifyHandler feiShuNotifyHandler;

   public AlertController(FeiShuNotifyHandler feiShuNotifyHandler) {
      this.feiShuNotifyHandler = feiShuNotifyHandler;
   }

   @GetMapping("/alert/text")
   public String alertText(@RequestParam String text) {
      final FeiShuTextNotifyRequest request = FeiShuTextNotifyRequest.of(text);

      return feiShuNotifyHandler.notify(request);
   }

}

