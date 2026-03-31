# 后端开发统一规范文档（专属版）
## 一、基础分页父类
### PageQueryParamDTO.java
```java
内有属性page 和limit 
```

## 二、前后端交互命名规范
1. 查询入参：统一 `XXXQueryDTO`，分页查询必须继承 `PageQueryParamDTO`
2. 返回出参：
    - 分页列表：`XXXPageRespDTO`
    - 单条详情/普通数据：`XXXRespDTO`
3. 严禁直接返回数据库DO实体给前端，所有对外响应必须封装DTO/RespDTO

## 三、通用分页返回封装
### PageRespDTO.java
```java
分页返回类型PageRespDTO<T>
可调用public static <T, P extends PageQueryParamDTO> PageRespDTO<T> build(IPage<T> resultPage, P queryDTO)
   或者public static <T, P extends PageQueryParamDTO> PageRespDTO<T> build(IPage<T> resultPage, P queryDTO)
   或者public static <T> PageRespDTO<T> of(long total, List<T> rows, int page, int limit)

```

## 四、全局统一接口返回体
### Result.java
```java
@Data
@Schema(name = "Result", description = "通用返回结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private int code;

    /**
     * 提示信息
     */
    @Schema(description = "提示信息")
    private String message;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 响应时间戳（格式：yyyy-MM-dd HH:mm:ss）
     */
    @Schema(description = "响应时间戳")
    private String timestamp;

    // 私有化构造方法，通过静态方法创建
    private Result() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private Result(int code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ========== 静态构建方法 ==========

    /**
     * 成功（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功（自定义提示信息+数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败（使用默认失败状态码）
     */
    public static <T> Result<T> fail() {
        return new Result<>(ResultCode.FAIL.getCode(), ResultCode.FAIL.getMessage(), null);
    }

    /**
     * 失败（自定义提示信息）
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.FAIL.getCode(), message, null);
    }

    /**
     * 失败（自定义状态码+提示信息）
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败（使用枚举状态码）
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败（使用枚举状态码+自定义提示）
     */
    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }
}
```

## 五、自定义业务异常
### BusinessException.java
```java
public class BusinessException extends RuntimeException {

    private int code;

    public int getCode() {
        return code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        int code = ResultCode.FAIL.getCode();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }
}
```

## 六、补充说明（后续拓展规范）
1. 所有Controller接口，外层必须包装 `Result<T>` 返回；
2. 分页接口必须使用 `PageRespDTO.build()` 组装分页数据；
3. 业务校验报错，统一抛出 `BusinessException`，禁止直接return错误信息；
4. 所有DTO、RespDTO必须添加完整Swagger `@Schema`注解；
5. 后续新增通用枚举、全局异常处理器、MyBatis-Plus配置，统一追加到此文档。

---
你直接复制保存为 `后端统一开发规范.md` 即可，后续我写代码会严格遵守这份文档。需要我把缺失的 `ResultCode` 枚举也补充进来吗？