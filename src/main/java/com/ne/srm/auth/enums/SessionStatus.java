package com.ne.srm.auth.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 用户登录 Session 状态

 * Session 表示一次用户登录行为，而不是单个 JWT Token。

 * 生命周期通常：

 * ACTIVE
 *    |
 *    +----> EXPIRED
 *    |
 *    +----> REVOKED
 *    |
 *    +----> SUSPENDED
 *
 */
@Getter
public enum SessionStatus {

    /**
     * 活跃状态

     * 表示：
     * - 用户正常登录
     * - refresh token 有效
     * - 允许刷新 access token
     * - 允许正常访问业务资源

     * 常见产生方式：
     * - 用户登录成功
     * - 用户重新认证成功
     * - 被风控解除冻结
     */
    ACTIVE("ACTIVE", "正常活跃"),


    /**
     * 已过期状态
     * 表示：
     * - Session 达到了自然过期时间
     * - refresh token 已超过有效期
     * - 不再允许刷新 token

     * 特点：
     * - 一般不是用户主动操作
     * - 通常不需要额外安全处理
     * 示例：
     * refresh_token 有效期 30 天，
     * 30 天后自动进入 EXPIRED。
     */
    EXPIRED("EXPIRED", "已过期"),


    /**
     * 已撤销状态
     * 表示：
     * Session 被主动失效。

     * 常见场景：
     * - 用户主动退出登录
     * - 修改密码后踢出所有设备
     * - 管理员强制下线
     * - 安全风险检测

     * 与 EXPIRED 区别：
     * EXPIRED 是自然失效；
     * REVOKED 是人为或系统主动失效。
     */
    REVOKED("REVOKED", "已撤销"),


    /**
     * 用户主动退出状态
     * 表示：
     * 用户明确点击了退出登录。
     * 与 REVOKED 类似，但是语义更加精确：

     * REVOKED:
     *   系统、安全策略、管理员导致失效

     * LOGGED_OUT:
     *   用户主动退出

     * 小型系统通常可以不用该状态，
     * 统一归为 REVOKED。
     */
    LOGGED_OUT("LOGGED_OUT", "用户退出"),


    /**
     * 暂停状态

     * 表示：
     * Session 暂时不能使用，但未来可能恢复。

     * 常见场景：
     * - 异常登录检测
     * - 风控审核中
     * - 需要用户二次验证

     * 与 REVOKED 区别：

     * REVOKED:
     *   不可恢复，需要重新登录

     * SUSPENDED:
     *   临时冻结，可以恢复
     */
    SUSPENDED("SUSPENDED", "已暂停");


    private final String code;

    private final String description;


    SessionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }


    /**
     * 根据数据库值转换枚举
     */
    public static SessionStatus fromCode(String code) {

        for (SessionStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "未知会话状态： " + code
        );
    }

    public boolean isActive() {
        return Objects.equals(ACTIVE,this);
    }
}
