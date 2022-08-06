package club.javafamily.prom.constant;

/**
 * @author Jack Li
 * @date 2022/6/6 下午22:03
 * @description
 */
public interface SystemConstant {

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
}
