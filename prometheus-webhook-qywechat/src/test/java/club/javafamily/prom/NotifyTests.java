package club.javafamily.prom;

import club.javafamily.prom.controller.AlertController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Jack Li
 * @date 2022/10/23 下午1:57
 * @description
 */
@SpringBootTest
public class NotifyTests {

   @Autowired
   private AlertController controller;

   String body = "{\"alerts\":[{\"generatorURL\":\"/prometheus/graph?g0.expr=up+%3D%3D+0&g0.tab=1\",\"fingerprint\":\"f9a231550eb9e1b7\",\"annotations\":{\"template\":\"节点 **${serviceName}**(${instance}) 断联, 请及时处理!\",\"instance\":\"172.16.2.21:9100\",\"link\":\"http://124.223.75.119:92/prometheus/targets\",\"pic\":\"https://www.haogebiji.com/wp-content/uploads/2021/04/2021043009064875-480x300.png\",\"serviceName\":\"Docker 运行环境服务器\",\"title\":\"节点宕机报警\",\"token\":\"7ae71e2e-15bb-4e8b-9283-86410004f060\"},\"startsAt\":\"2023-08-16T02:32:12.133Z\",\"endsAt\":\"0001-01-01T00:00:00Z\",\"status\":\"firing\",\"labels\":{\"severity\":\"emergency\",\"instance\":\"172.16.2.21:9100\",\"alertname\":\"实例存活报警\",\"job\":\"sd-nodes\",\"serviceId\":\"FileServiceDiscoveryNodeExporter\",\"serviceName\":\"Docker 运行环境服务器\",\"type\":\"File\",\"token\":\"7ae71e2e-15bb-4e8b-9283-86410004f060\"}}],\"commonLabels\":{\"severity\":\"emergency\",\"instance\":\"172.16.2.21:9100\",\"alertname\":\"实例存活报警\",\"job\":\"sd-nodes\",\"serviceId\":\"FileServiceDiscoveryNodeExporter\",\"serviceName\":\"Docker 运行环境服务器\",\"type\":\"File\",\"token\":\"7ae71e2e-15bb-4e8b-9283-86410004f060\"},\"externalURL\":\"http://1c85f9e2a64e:9093\",\"groupLabels\":{\"severity\":\"emergency\",\"instance\":\"172.16.2.21:9100\",\"alertname\":\"实例存活报警\",\"job\":\"sd-nodes\",\"serviceId\":\"FileServiceDiscoveryNodeExporter\",\"serviceName\":\"Docker 运行环境服务器\",\"type\":\"File\",\"token\":\"7ae71e2e-15bb-4e8b-9283-86410004f060\"},\"receiver\":\"web\\\\.hook\",\"truncatedAlerts\":0,\"version\":\"4\",\"commonAnnotations\":{\"template\":\"节点 **${serviceName}**(${instance}) 断联, 请及时处理!\",\"instance\":\"172.16.2.21:9100\",\"link\":\"http://124.223.75.119:92/prometheus/targets\",\"pic\":\"https://www.haogebiji.com/wp-content/uploads/2021/04/2021043009064875-480x300.png\",\"serviceName\":\"Docker 运行环境服务器\",\"title\":\"节点宕机报警\",\"token\":\"7ae71e2e-15bb-4e8b-9283-86410004f060\"},\"groupKey\":\"{}:{alertname=\\\"实例存活报警\\\", instance=\\\"172.16.2.21:9100\\\", job=\\\"sd-nodes\\\", serviceId=\\\"FileServiceDiscoveryNodeExporter\\\", serviceName=\\\"Docker 运行环境服务器\\\", severity=\\\"emergency\\\", token=\\\"7ae71e2e-15bb-4e8b-9283-86410004f060\\\", type=\\\"File\\\"}\",\"status\":\"firing\"}\n";

   @Test
   void testNotify() {
      final String s = controller.alertNews(body);
      System.out.println(s);
   }


}
