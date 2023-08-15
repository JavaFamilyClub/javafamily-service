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

   String body = "{\"alerts\":[{\"generatorURL\":\"http://0f802a1c941a:9090/graph?g0.expr=%28node_filesystem_size_bytes+-+node_filesystem_avail_bytes%29+%2F+node_filesystem_size_bytes+%2A+100+%3E+90&g0.tab=1\",\"fingerprint\":\"27d14486afb8e614\",\"annotations\":{\"template\":\"**${serviceName}**(${instance}) 服务器磁盘设备使用率超过 **90%**, 请及时处理！\\n挂载点: ${mountpoint}\\n当前值: ${value}%!\",\"instance\":\"192.168.100.192:9010\",\"link\":\"http://hxt.yijiweather.com/grafana/d/aka/duo-job-ji-cheng-fu-wu-qi-jian-kong\",\"serviceName\":\"测试服务器\",\"title\":\"磁盘使用率预警\",\"btn\":\"点击查看详情 :玫瑰:\",\"value\":\"90.86792536842535\",\"mountpoint\":\"/etc/resolv.conf\",\"token\":\"\"},\"startsAt\":\"2022-10-23T05:45:48.753Z\",\"endsAt\":\"2022-10-23T05:50:33.753Z\",\"status\":\"resolved\",\"labels\":{\"severity\":\"critical\",\"instance\":\"192.168.100.192:9010\",\"alertname\":\"磁盘使用率预警\",\"job\":\"dev-node-exportor\",\"serviceId\":\"node-exporter\",\"serviceName\":\"测试服务器\",\"device\":\"/dev/vdb\",\"fstype\":\"xfs\",\"mountpoint\":\"/etc/resolv.conf\"}}],\"commonLabels\":{\"severity\":\"critical\",\"instance\":\"192.168.100.192:9010\",\"alertname\":\"磁盘使用率预警\",\"job\":\"dev-node-exportor\",\"serviceId\":\"node-exporter\",\"serviceName\":\"测试服务器\",\"device\":\"/dev/vdb\",\"fstype\":\"xfs\",\"mountpoint\":\"/etc/resolv.conf\"},\"externalURL\":\"http://64ee495a6a31:9093\",\"groupLabels\":{\"severity\":\"critical\",\"instance\":\"192.168.100.192:9010\",\"alertname\":\"磁盘使用率预警\",\"job\":\"dev-node-exportor\",\"serviceId\":\"node-exporter\",\"serviceName\":\"测试服务器\",\"device\":\"/dev/vdb\",\"fstype\":\"xfs\",\"mountpoint\":\"/etc/resolv.conf\"},\"receiver\":\"webhook\",\"truncatedAlerts\":0,\"version\":\"4\",\"commonAnnotations\":{\"template\":\"**${serviceName}**(${instance}) 服务器磁盘设备使用率超过 **90%**, 请及时处理！\\n挂载点: ${mountpoint}\\n当前值: ${value}%!\",\"instance\":\"192.168.100.192:9010\",\"link\":\"http://hxt.yijiweather.com/grafana/d/aka/duo-job-ji-cheng-fu-wu-qi-jian-kong\",\"serviceName\":\"测试服务器\",\"title\":\"磁盘使用率预警\",\"btn\":\"点击查看详情 :玫瑰:\",\"value\":\"90.86792536842535\",\"mountpoint\":\"/etc/resolv.conf\",\"token\":\"\"},\"groupKey\":\"{}:{alertname=\\\"磁盘使用率预警\\\", device=\\\"/dev/vdb\\\", fstype=\\\"xfs\\\", instance=\\\"192.168.100.192:9010\\\", job=\\\"dev-node-exportor\\\", mountpoint=\\\"/etc/resolv.conf\\\", serviceId=\\\"node-exporter\\\", serviceName=\\\"测试服务器\\\", severity=\\\"critical\\\"}\",\"status\":\"resolved\"}\n";

   @ParameterizedTest
   @ValueSource(strings = { "resolved", "firing", "notExist" })
   void testParse(String statusStr) {
      JSONObject jsonObject = JSONObject.parseObject(body);
      jsonObject.put("status", statusStr);

      AlertStatusEnum status = AlertController.getStatus(jsonObject.toJSONString());

      System.out.println(status);
   }

}
