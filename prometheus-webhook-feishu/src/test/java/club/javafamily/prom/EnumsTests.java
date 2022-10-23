package club.javafamily.prom;

import club.javafamily.prom.controller.AlertController;
import club.javafamily.prom.enums.AlertStatusEnum;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Jack Li
 * @date 2022/10/23 下午12:25
 * @description
 */
public class EnumsTests {

   @ParameterizedTest
   @ValueSource(strings = { "resolved", "firing", "notExist" })
   void testParse(String statusStr) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("status", statusStr);

      AlertStatusEnum status = AlertController.getStatus(jsonObject);

      System.out.println(status);
   }

}
