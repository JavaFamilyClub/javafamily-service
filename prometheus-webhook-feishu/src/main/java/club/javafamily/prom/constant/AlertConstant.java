package club.javafamily.prom.constant;

/**
 * @author Jack Li
 * @date 2022/6/6 下午22:03
 * @description
 */
public interface AlertConstant {

    /**
     * 指定飞书 token 的 key
     */
    String FEISHU_TOKEN_KEY = "token";

    /**
     * alert manager 推送过来包含 annotation 的 key
     */
    String ANNO_KEY = "commonAnnotations";

    /**
     * title 模板 key
     */
    String TITLE_TEMPLATE = "titleTemplate";

    /**
     * 内容模板 key
     */
    String CONTENT_TEMPLATE = "template";

    /**
     * title 直接取值的 key
     */
    String TITLE_KEY = "title";

    /**
     * content 直接取值的 key
     */
    String CONTENT_KEY = "content";

    /**
     * btn 直接取值的 key
     */
    String BTN_LABEL_KEY = "btn";

    /**
     * button link 直接取值的 key
     */
    String BTN_LINK_KEY = "link";

    /**
     * 通过 {@link #TITLE_TEMPLATE} 或者 {@link #TITLE_KEY} parse 后的最终 title
     */
    String TITLE_RESULT = "titleResult";

    /**
     * 故障恢复 title key
     */
    String RESOLVED_TITLE_TEMPLATE = "resolvedTitleTemplate";

    /**
     * 默认的故障恢复 title 模板
     */
    String DEFAULT_RESOLVED_TITLE = "故障恢复通知(${" + TITLE_RESULT + "})";
}
