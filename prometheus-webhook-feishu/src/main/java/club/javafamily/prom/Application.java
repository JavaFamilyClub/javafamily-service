package club.javafamily.prom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * @author Jack Li
 * @date 2022/7/17 下午4:15
 * @description
 */
@SpringBootApplication
public class Application {
   public static void main(String[] args) {
      TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

      SpringApplication.run(Application.class, args);

      System.out.println("飞书 AlertManager 启动成功!");
   }
}
