package club.javafamily.prom.constant;

/**
 * @author Jack Li
 * @date 2022/6/6 下午22:03
 * @description
 */
public interface AlertConstant {

    /**
     * 多数据分隔符
     */
    String SPLIT_FLAG = "_,_";

    /**
     * 指定处理器 token 的 key
     */
    String HANDLER_TOKEN_KEY = "token";

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
     * btns 直接取值的 key
     */
    String BTN_LABELS_KEY = "btns";

    /**
     * button link 直接取值的 key
     */
    String BTN_LINK_KEY = "link";

    /**
     * button links 直接取值的 key
     */
    String BTN_LINKS_KEY = "links";

    /**
     * button pictures 直接取值的 key
     */
    String BTN_LINK_PICS_KEY = "linkPicUrls";

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

    /**
     * 需要 @ 的人员电话.
     */
    String AT_USER_PHONE = "atUserPhones";

    /**
     * 是否是 @ 所有人
     */
    String AT_ALL = "atAll";

    /**
     * link 通知的图片地址
     */
    String LINK_PIC_URL = "linkPicUrl";
}
