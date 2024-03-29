package xinrui.cloud.domain;

/**
 * 组织机构类别
 *
 * @author tzz
 *
 */
public final class GroupType {
    public static final String GROUP = "group";
    // public static final String COMMON = "common";
    /**
     * 政府
     */
    public static final String ORGANIZATION = "organization";
    /**
     * 企业
     */
    public static final String OTHER = "other";
    /**
     * 企业部门
     */
    public static final String BRANCH = "other";
    /**
     * 社团组织
     */
    public static final String ASSOCIATION = "association";
    /**
     * 事业单位
     */
    public static final String INTERMEDIARY = "intermediary";
    /**
     * 民办非企业
     */
    public static final String PERSONAL = "personal";

    /**
     * 银行
     */
    public static final String BANK = "bank";

    private GroupType() {
    }
}
