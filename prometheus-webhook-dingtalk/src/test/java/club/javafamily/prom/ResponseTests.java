package club.javafamily.prom;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.text.StrSubstitutor;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;
import org.springframework.validation.MessageCodeFormatter;

import java.text.MessageFormat;
import java.util.Map;

public class ResponseTests {

    String jsonStr = "{\"alerts\":[{\"generatorURL\":\"http://0f802a1c941a:9090/graph?g0.expr=%28node_filesystem_size_bytes+-+node_filesystem_avail_bytes%29+%2F+node_filesystem_size_bytes+%2A+100+%3E+90&g0.tab=1\",\"fingerprint\":\"8528be812e5bcd7b\",\"annotations\":{\"summary\":\"服务器磁盘使用率预警\",\"template\":\"预警: ${serviceName}(${instance}) 服务器磁盘设备使用率超过90%！(挂载点: ${mountpoint} 当前值: ${value}%), 详情可参考: ${link}\",\"instance\":\"192.168.100.192:9010\",\"link\":\"http://hxt.yijiweather.com/grafana/d/aka/duo-job-ji-cheng-fu-wu-qi-jian-kong\",\"description\":\"预警: 测试服务器(192.168.100.192:9010) 服务器磁盘设备使用率超过90%！(挂载点: /rootfs 当前值: 91.16306982230842%), 详情可参考: http://hxt.yijiweather.com/grafana/d/aka/duo-job-ji-cheng-fu-wu-qi-jian-kong\",\"serviceName\":\"测试服务器\",\"value\":\"91.16306982230842\",\"mountpoint\":\"/rootfs\"},\"startsAt\":\"2022-07-20T12:55:03.753Z\",\"endsAt\":\"0001-01-01T00:00:00Z\",\"status\":\"firing\",\"labels\":{\"severity\":\"critical\",\"instance\":\"192.168.100.192:9010\",\"alertname\":\"磁盘使用率预警\",\"job\":\"dev-node-exportor\",\"serviceId\":\"node-exporter\",\"serviceName\":\"测试服务器\",\"device\":\"/dev/vda1\",\"fstype\":\"xfs\",\"mountpoint\":\"/rootfs\"}}],\"commonLabels\":{\"severity\":\"critical\",\"instance\":\"192.168.100.192:9010\",\"alertname\":\"磁盘使用率预警\",\"job\":\"dev-node-exportor\",\"serviceId\":\"node-exporter\",\"serviceName\":\"测试服务器\",\"device\":\"/dev/vda1\",\"fstype\":\"xfs\",\"mountpoint\":\"/rootfs\"},\"externalURL\":\"http://64ee495a6a31:9093\",\"groupLabels\":{\"severity\":\"critical\",\"instance\":\"192.168.100.192:9010\",\"alertname\":\"磁盘使用率预警\",\"job\":\"dev-node-exportor\",\"serviceId\":\"node-exporter\",\"serviceName\":\"测试服务器\",\"device\":\"/dev/vda1\",\"fstype\":\"xfs\",\"mountpoint\":\"/rootfs\"},\"receiver\":\"webhook\",\"truncatedAlerts\":0,\"version\":\"4\",\"commonAnnotations\":{\"summary\":\"服务器磁盘使用率预警\",\"template\":\"预警: ${serviceName}(${instance}) 服务器磁盘设备使用率超过90%！(挂载点: ${mountpoint} 当前值: ${value}%), 详情可参考: ${link}\",\"instance\":\"192.168.100.192:9010\",\"link\":\"http://hxt.yijiweather.com/grafana/d/aka/duo-job-ji-cheng-fu-wu-qi-jian-kong\",\"description\":\"预警: 测试服务器(192.168.100.192:9010) 服务器磁盘设备使用率超过90%！(挂载点: /rootfs 当前值: 91.16306982230842%), 详情可参考: http://hxt.yijiweather.com/grafana/d/aka/duo-job-ji-cheng-fu-wu-qi-jian-kong\",\"serviceName\":\"测试服务器\",\"value\":\"91.16306982230842\",\"mountpoint\":\"/rootfs\"},\"groupKey\":\"{}:{alertname=\\\"磁盘使用率预警\\\", device=\\\"/dev/vda1\\\", fstype=\\\"xfs\\\", instance=\\\"192.168.100.192:9010\\\", job=\\\"dev-node-exportor\\\", mountpoint=\\\"/rootfs\\\", serviceId=\\\"node-exporter\\\", serviceName=\\\"测试服务器\\\", severity=\\\"critical\\\"}\",\"status\":\"firing\"}";

    @Test
    void testParse() {
        final JSONObject json = JSONObject.parseObject(jsonStr);

        System.out.println(json.getString(null));

        JSONObject jsonObject = json.getJSONObject("commonAnnotations");

        String template = jsonObject.getString("template");
        Map<String, Object> params = jsonObject.getInnerMap();

        String content = StringSubstitutor.replace(template, params);

        System.out.println(content);

        String noVars = StringSubstitutor.replace("测试没有变量", params);

        System.out.println(noVars);
    }

}
