# SRM 后端技术结构

## 1. 项目定位

本目录是基于 Spring Boot 3.5 的 Java 服务，当前监听端口为 `9090`。代码覆盖认证与用户、企业、询价/订单/合同等流程、商品、字典、文件、付款和通用状态能力；同时包含 Thymeleaf 页面资源。此文档按仓库当前代码整理，未将目录名视为已实现的业务承诺。

## 2. 技术栈与启动配置

- Java 工具链：21（`build.gradle`）；版本目录还声明 Java 17，构建实际配置以工具链 21 为准。
- Spring Boot Web、Security、Data JPA、Thymeleaf、Validation；MySQL、Redis。
- JWT（JJWT）及 Redis 分布式锁/缓存；Apache POI、PDFBox、Tika 和 ZXing 用于文档、表格、PDF 与条码相关能力；Hutool 为通用工具库。
- Gradle Wrapper：`./gradlew bootRun` 启动，`./gradlew test` 执行测试，`./gradlew build` 构建。

本地默认连接 `127.0.0.1:3306/srm` 和 `127.0.0.1:6379`，服务端口 `9090`。连接配置位于 `src/main/resources/application.properties`，安全/JWT/Redis 自定义属性位于 `application.yaml`。部署时应通过外部配置提供数据库、Redis 与 JWT 密钥；不要把仓库中的开发默认值作为生产凭据。JPA 当前配置 `ddl-auto=update`，会在应用启动时尝试更新表结构。

## 3. 代码分层与模块

根包 `com.ne.srm`，启动入口为 `SrmProjectApplication`。业务包通常按 `entity`、`repository`、`service.application`、`service.domain` 分层；当前若干服务类只有依赖注入、尚无业务方法，故以下同时区分“模型表达的业务”与“目前可见的已实现流程”。

| 包 | 职责 |
| --- | --- |
| `auth` | 登录/注册、JWT 认证过滤器、用户/角色/权限、刷新令牌和会话/设备管理。 |
| `company` | 企业、联系人、发票相关实体、仓储及企业领域服务；被合同及条款模型引用。 |
| `process` | 询价、询价历史、订单、合同、合同明细/条款、收货、流程状态模型及查询入口。详见下节。 |
| `product` | 商品主数据模型与按 SKU/中文名查询的仓储；与询价及合同明细关联。当前服务层尚无操作方法。 |
| `pay` | 付款记录及应用/领域服务；流程状态枚举中另有付款类型/状态。 |
| `file` | 文件元数据与业务附件关联，详见下节。 |
| `dict` | 字典类型及字典项，按字典编码维护/查询，详见下节。 |
| `state` | 通用状态相关能力（当前目录实现以源代码为准）。 |
| `core` | JPA/Redis 配置、Redis 服务、文件/Excel/文本/加密工具及属性。 |
| `common` | 通用常量、基础实体/仓储、统一结果结构和全局异常处理。 |
| `vo` | 跨域视图对象。 |

常见请求链路为 Controller → Service → Spring Data Repository → MySQL。认证请求由 Spring Security 链中的 JWT 过滤器解析；token 支持 Header 或 Cookie 解析器，具体模式由 `security.token-mode` 控制。

## 4. 重点业务域（process、product、dict、file）

### 4.1 采购流程主线：询价 → 订单 → 合同 → 收货

从实体关联看，核心业务主线如下。关系方向按 JPA 字段注解描述；基数只对代码中明确的 owning side 下结论。

```text
ProductEntity  ← InquiryEntity → InquiryHistory
                      ↑
                  OrderEntity
                      ↑
                  ContractEntity → ContractItem → ProductEntity
                      ├── ContractTerm
                      ├── buyerCompany / supplierCompany
                      ├── ContractStatusLog
                      └── GoodsReceipt
```

- **询价（`InquiryEntity`）**：询价记录以 `inquiryNo` 分组查询，包含商品、实物图、目标价/出厂价/logo/运费、开票加点与类型、装柜字典项、询价数量、店铺联系人、箱规、状态码、负责人和版本。询价通过 `product_id` 关联 `ProductEntity`，通过 `container_load_type_id` 引用 `SysDictItem`。
- **询价历史（`InquiryHistory`）**：多条历史记录通过 `inquiry_id` 归属询价，保存价格、发票参数、数量、版本、修改者和时间，适合表达报价变更快照。当前已暴露的询价 API 只读：`GET /inquiry/batches` 返回询价编号，`GET /inquiry/getByBatch?num=...` 按编号查询记录；控制器直接调用 `InquiryDomainService`。
- **订单（`OrderEntity`）**：通过 `inquiry_id` 关联询价，保存订单批次号、采购数量、字符串状态码和创建时间。因此模型允许一个询价被多个订单记录引用；实际拆单/转单规则尚未在当前服务实现中体现。
- **合同（`ContractEntity`）**：通过 `order_id` 关联订单，并保存合同编号、买卖双方企业、合同类型/状态编码、版本、原合同 ID、修订原因、金额（数字及大写）、币种、签订日期、付款状态编码、条款及负责人。合同修订目前以 `parentContractId` 和 `revisionReason` 表达，没有 ORM 自关联映射。
- **合同明细（`ContractItem`）**：通过 `contract_id` 从属于合同，保留合同批次、商品引用及签约时商品名称/单位/说明/颜色等快照、交期、发票类型、单价、数量、箱规和金额。显式快照字段使合同资料可保留签约时文本。
- **合同条款（`ContractTerm`）**：保存条款正文、企业、版本、创建/更新时间及操作人；合同引用条款。实体提供默认条款创建、临时修改和版本更新方法，但当前 Contract 应用/领域服务没有公开用例方法。
- **收货（`GoodsReceipt`）**：模型含收货编号、合同引用、应收/实收数量、收货时间、仓库备注和状态码。实体以 `contract_id` 一对一映射，当前收货应用/领域服务没有公开流程方法。

流程枚举包括订单状态（进行中、关闭、重新询价、完成）、合同状态（内容确认、未盖章、已盖章回传、已出库、完成）、付款状态（申请预付款、已付预付款、申请尾款/全款、已结清）及付款类型（预付款、尾款、全款）。合同状态变更日志由 `ContractStatusLog` 保存合同、前后状态、操作人、备注和时间；`StateTransition` 保存业务类型及允许的 from/to 状态候选。当前 `WorkflowDomainService`/`WorkflowApplicationService` 仍仅注入仓储，未见实际状态转移校验或日志写入逻辑。

基础 CRUD 已由 `InquiryController` 和 `ProcurementController` 暴露；`ProcurementApplicationService` 将订单、合同、合同明细、条款及收货用例委托给 `ProcurementDomainService`。目前这些接口直接接收/返回 JPA 实体，尚未加入 DTO 转换、业务状态机约束、复杂流程事务编排或角色级授权。删除操作由数据库关联约束兜底，存在引用时可能失败。

### 4.2 商品主数据（`product`）

`ProductEntity` 保存 SKU、图片路径、多语言品名/描述（波兰语、英语、中文）、颜色及 CE、说明书、贴纸等布尔标记。`ProductRepository` 支持按 SKU 查找和中文名包含关键字的分页查询。询价与合同明细均引用商品；合同明细同时保存若干商品字段快照。`ProductController` 通过 Application/Domain Service 暴露列表、详情、新增、更新、删除接口（`/products`）。

### 4.3 字典（`dict`）

`SysDictType` 定义字典编码、名称、备注及创建时间；`SysDictItem` 保存字典编码、项编码/名称、排序、启用标记、备注及创建时间。两者以 `dictCode` 字段关联，实体上没有 JPA 关系映射。`DictController` 经应用/领域服务暴露类型与条目的 CRUD，条目支持按字典编码筛选及按字典编码/项编码精确查询，路径位于 `/dict/types` 和 `/dict/items`。询价装柜类型直接关联字典项。

### 4.4 文件与业务附件（`file`）

`SysFile` 保存物理文件元数据：文件名、路径、大小、MIME 类型、MD5、创建时间和创建人。`BusinessAttachment` 通过 `file_id` 多对一关联文件，并用 `businessTypeCode + businessId` 多态指向业务对象；类型注释列出合同、发票、商品、付款和询价。附件还带分类码、类型码、备注及创建/更新时间。该多态业务引用没有外键实体映射，需由应用逻辑保证业务对象与附件类型一致。

文件/附件的 Application Service、Domain Service 和 Repository 已支撑 `/files` 下的元数据 CRUD，以及 `/files/attachments` 下的附件 CRUD 和按业务类型/业务 ID 查询。当前接口只维护数据库元数据与关联记录，不负责 multipart 上传、文件下载、物理删除或 MD5 去重；实际存储介质与路径策略仍未定义。

## 5. 其他业务模块概览

| 包 | 职责（依目录及类名归纳） |
| --- | --- |
| `pay` | 付款记录及应用/领域服务。 |
| `company` | 企业、联系人、发票相关实体、仓储及企业领域服务。 |
| `state` | 通用状态相关能力。 |
| `core` | JPA/Redis 配置、Redis 服务、文件/Excel/文本/加密工具及属性。 |
| `common` | 通用常量、基础实体/仓储、统一结果结构和全局异常处理。 |
| `vo` | 跨域视图对象。 |

## 6. 已观察到的 HTTP 入口

| 路径 | 方法 | 用途 |
| --- | --- | --- |
| `/inquiry` | GET / POST | 询价列表及新增 |
| `/inquiry/{id}` | GET / PUT / DELETE | 询价详情、更新、删除 |
| `/auth/me` | GET | 当前登录用户 |
| `/auth/login`, `/auth/logout`, `/auth/register`, `/auth/refresh` | POST | 登录生命周期 |
| `/auth/password`, `/auth/profile` | PUT | 修改密码、资料 |
| `/devices/me` | GET | 当前用户设备/会话信息 |
| `/users` | GET / POST | 用户列表与创建 |
| `/users/{id}/disable`, `/users/{id}/enable` | PUT | 禁用/启用用户 |
| `/users/{id}` | DELETE | 删除用户 |
| `/inquiry/batches`, `/inquiry/getByBatch` | GET | 询价批次查询 |
| `/orders`, `/contracts`, `/contract-items`, `/contract-terms`, `/receipts` | GET / POST | 订单/合同相关资料的列表及新增；可选关联过滤见 Controller 参数 |
| 上述流程资源 `/{id}` | GET / PUT / DELETE | 详情、更新、删除 |
| `/products`, `/dict/types`, `/dict/items`, `/files`, `/files/attachments` | GET / POST / PUT / DELETE | 商品、字典、文件元数据和业务附件基础维护 |
| `/`, `/login`, `/register`, `/password`, `/devices` | GET | Thymeleaf 页面路由 |

这是从控制器映射注解提取的入口摘要；权限要求、请求/响应字段和完整业务语义请以控制器、DTO、Security 配置为准。

## 7. 持久化与资源

- JPA 实体位于各业务模块的 `entity` 包，仓储位于 `repository` 或现有拼写 `repositoty` 包。
- `prj.sql`、`auth.sql` 提供数据库初始化/业务 SQL 参考；应用另启用 Hibernate 自动 DDL 更新，二者使用时需确认与当前实体模型的一致性。
- `src/main/resources/templates` 放置登录、注册、设备及首页等 Thymeleaf 模板；静态 CSS/JS 在 `static`。
- 测试源集为 `src/test/java`，测试框架为 Spring Boot Test/JUnit Platform。

## 8. 维护提示

新增业务接口时沿用所属模块的 Controller/Service/Repository/Entity 分层；请求与响应优先使用 DTO/VO，输入校验使用 Jakarta Validation。跨模块共享的数据结构放在 `common`，基础设施适配放在 `core`。涉及权限时同步检查 URL 规则、方法级授权、角色权限数据及异常响应；涉及 schema 时同步审阅 SQL 脚本与 Hibernate DDL 行为。
