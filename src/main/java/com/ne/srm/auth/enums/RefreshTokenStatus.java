package com.ne.srm.auth.enums;

import lombok.Getter;

/**
 * Refresh Token 状态枚举
 *
 * <p>用于管理 OAuth2 / JWT 刷新令牌的生命周期状态，支持：
 * <ul>
 *   <li>令牌有效性控制（有效/过期/撤销）</li>
 *   <li>安全策略（泄露检测与全局撤销）</li>
 *   <li>会话管理（用户登出、设备管理）</li>
 * </ul>
 *
 * <p>状态流转说明：
 * <pre>
 * ┌─────────┐  刷新(Refresh)   ┌─────────┐
 * │ ACTIVE  │ ──────────────→ │ REVOKED │（旧令牌被废弃）
 * └─────────┘                  └─────────┘
 *      │                            ↑
 *      │ 过期(Expire)               │ 主动登出(Logout)
 *      ↓                            │
 * ┌─────────┐  检测到重放攻击   ┌─────────────┐
 * │ EXPIRED │ ──────────────→ │ COMPROMISED │（连带撤销所有关联令牌）
 * └─────────┘                  └─────────────┘
 * </pre>
 *
 * @author YourName
 * @since 2026-08-01
 */
@Getter
public enum RefreshTokenStatus {

    /**
     * 有效（ACTIVE）
     *
     * <p>令牌处于正常工作状态，可用于换取新的 Access Token。
     *
     * <p>触发条件：
     * <ul>
     *   <li>用户登录成功后初次颁发</li>
     *   <li>刷新操作成功后颁发的新令牌</li>
     * </ul>
     *
     * <p>可进行的操作：
     * <ul>
     *   <li>访问受保护资源（通过 Access Token）</li>
     *   <li>刷新令牌（换取新的 Access Token 和 Refresh Token）</li>
     * </ul>
     */
    ACTIVE(0, "有效"),

    /**
     * 已轮换（ROTATED）
     *
     * <p>在 Refresh Token 轮转策略中，旧令牌被新令牌替换后的状态。
     * 这是 OAuth2 安全最佳实践的一部分，用于防止重放攻击。
     *
     * <p>触发条件：
     * <ul>
     *   <li>用户使用 Refresh Token 换取新的 Access Token 时</li>
     *   <li>系统自动生成新的 Refresh Token 并替换旧令牌</li>
     *   <li>一次性 Refresh Token 使用后立即轮换</li>
     * </ul>
     *
     * <p>安全说明：
     * <ul>
     *   <li>轮换后的旧令牌应立即失效，不能继续使用</li>
     *   <li>如果轮换后的旧令牌被再次使用，表明可能存在重放攻击</li>
     *   <li>建议配合父令牌 ID 追踪令牌链，形成完整的令牌血缘关系</li>
     * </ul>
     *
     * <p>与 REVOKED 的区别：
     * <ul>
     *   <li>REVOKED：主动撤销（用户登出、管理员操作）</li>
     *   <li>ROTATED：被动替换（正常刷新流程产生的令牌更新）</li>
     * </ul>
     *
     * <p>令牌链示例：
     * <pre>
     * token_v1 (ACTIVE) → refresh → token_v2 (ACTIVE), token_v1 (ROTATED)
     * token_v2 (ACTIVE) → refresh → token_v3 (ACTIVE), token_v2 (ROTATED)
     * </pre>
     *
     * <p>重放检测：
     * <pre>
     * 如果 ROTATED 状态的旧令牌再次被用于刷新请求：
     * → 视为重放攻击
     * → 将该令牌状态更新为 COMPROMISED
     * → 撤销该用户所有活跃令牌
     * </pre>
     */
    ROTATED(1, "已轮换"),

    /**
     * 已过期（EXPIRED）
     *
     * <p>令牌超过其有效期，无法再用于换取 Access Token。
     *
     * <p>触发条件：
     * <ul>
     *   <li>令牌自然过期（如 7 天或 90 天有效期结束）</li>
     *   <li>系统检查时发现超过过期时间</li>
     * </ul>
     *
     * <p>用户行为：
     * <ul>
     *   <li>必须重新登录（Re-authenticate）才能获取新令牌</li>
     *   <li>在过期前一定时间内（如 1 天），系统可提醒用户续期</li>
     * </ul>
     *
     * <p>注意：过期令牌应在数据库中保留一段时间（如 30 天）用于审计，
     * 之后可归档或物理删除。
     */
    EXPIRED(2, "已过期"),

    /**
     * 已撤销（REVOKED）
     *
     * <p>被用户或管理员主动作废的令牌，立即失效且不可恢复。
     *
     * <p>触发条件：
     * <ul>
     *   <li>用户主动登出（Logout）</li>
     *   <li>用户修改密码（建议强制撤销所有活跃会话）</li>
     *   <li>管理员强制结束用户会话</li>
     *   <li>Refresh Token 轮转时，旧令牌被自动撤销</li>
     *   <li>用户账号被锁定或禁用</li>
     * </ul>
     *
     * <p>安全说明：
     * <ul>
     *   <li>撤销后，该令牌携带的 Access Token 也应立即失效</li>
     *   <li>建议结合 Redis 黑名单机制实时生效</li>
     * </ul>
     */
    REVOKED(3, "已撤销"),

    /**
     * 已泄露/已废弃（COMPROMISED）
     *
     * <p>令牌被标记为可能泄露，触发全局安全策略。
     *
     * <p>触发条件：
     * <ul>
     *   <li>检测到同一个 Refresh Token 被重复使用（重放攻击）</li>
     *   <li>系统安全扫描发现令牌异常</li>
     *   <li>用户端主动报告可疑活动</li>
     * </ul>
     *
     * <p>系统自动执行的应急响应：
     * <ul>
     *   <li>将该令牌立即失效</li>
     *   <li><b>强制撤销该用户所有活跃的 Refresh Token</b></li>
     *   <li>记录安全事件日志，触发告警</li>
     *   <li>强制该用户所有设备重新登录</li>
     * </ul>
     *
     * <p>这是 <b>Refresh Token 轮转策略</b>中的核心安全机制，
     * 可有效防止令牌被盗用后的横向扩散。
     */
    COMPROMISED(4, "已泄露"),

    /**
     * 已作废（INACTIVE）- 可选状态
     *
     * <p>一个过渡或聚合状态，代表令牌不再可用（包括 EXPIRED、REVOKED、COMPROMISED）。
     *
     * <p>用途：
     * <ul>
     *   <li>在业务判断时，简化"是否可用"的逻辑判断</li>
     *   <li>数据清理时，批量处理所有非活跃令牌</li>
     * </ul>
     *
     * <p>注意：此状态一般不直接存储在数据库中，而是作为查询时的虚拟状态使用。
     */
    INACTIVE(9, "已作废");  // code 值较大，避免与正式状态混淆

    /**
     * 状态码
     *
     * <p>对应数据库 SMALLINT 类型存储的值。
     * 为了适应无符号 SMALLINT，范围应在 0~65535 之间。
     */
    private final int code;

    /**
     * 状态描述
     *
     * <p>用于日志输出、前端展示或国际化（可扩展为 properties 配置）。
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param code 状态码（存入数据库的值）
     * @param description 状态描述
     */
    RefreshTokenStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据状态码获取枚举实例
     *
     * <p>用于从数据库读取 SMALLINT 值后转换为 Java 枚举对象。
     *
     * @param code 状态码
     * @return 对应的枚举实例
     * @throws IllegalArgumentException 如果状态码无效
     */
    public static RefreshTokenStatus fromCode(int code) {
        for (RefreshTokenStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的 Refresh Token 状态码: " + code);
    }

    /**
     * 判断当前令牌是否可用（即是否可用于换取 Access Token）
     *
     * <p>只有 ACTIVE 状态的令牌才是可用的。
     *
     * @return true 如果令牌可用，否则 false
     */
    public boolean isUsable() {
        return this == ACTIVE;
    }

    /**
     * 判断当前令牌是否已终止（不可恢复）
     *
     * <p>包括 REVOKED 和 COMPROMISED，但不包括 EXPIRED（因为过期可以重新登录）。
     *
     * @return true 如果令牌已永久终止，否则 false
     */
    public boolean isTerminated() {
        return this == REVOKED || this == COMPROMISED;
    }

    /**
     * 判断当前令牌是否需要触发全局安全策略
     *
     * <p>当令牌处于 COMPROMISED 状态时，需要撤销该用户所有活跃会话。
     *
     * @return true 如果需要触发全局安全响应，否则 false
     */
    public boolean requiresGlobalAction() {
        return this == COMPROMISED;
    }

    // ============ 以下是可选的便捷工具方法 ============

    /**
     * 安全地获取状态码，避免 NPE
     *
     * @param status 枚举实例（可能为 null）
     * @return 状态码，如果 status 为 null 则返回 0
     */
    public static int safeGetCode(RefreshTokenStatus status) {
        return status == null ? 0 : status.getCode();
    }

    /**
     * 安全地根据状态码获取枚举，处理未知码的情况
     *
     * @param code 状态码
     * @param defaultValue 默认值（当码无效时返回）
     * @return 对应的枚举实例或默认值
     */
    public static RefreshTokenStatus fromCodeOrDefault(int code, RefreshTokenStatus defaultValue) {
        try {
            return fromCode(code);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}
