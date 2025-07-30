# 加密数据模糊查询示例

本项目 (`encrypt-data-query-demo`) 演示了在数据加密存储后，如何高效地实现模糊查询功能。主要探讨并实现了两种针对不同数据类型的加密查询方案。

## 核心问题

当敏感数据（如姓名、手机号）以密文形式存储在数据库中时，传统的 `LIKE '%keyword%'` 查询方式将失效。本项目旨在解决这一问题，提供既安全又高效的密文查询方案。

## 实现方案

针对不同类型的数据及其查询需求，本项目实现了两种主流的加密查询方案：

### 1. 部分匹配 + 哈希索引 (Part Match + Hash Index)

此方案适用于具有固定结构、且查询模式单一的场景，例如 **根据手机号后四位查询**。

- **服务接口**: `cn.clancy.service.PartMatchService`
- **实现逻辑**:
    1. **存储**:
        - 提取手机号的后四位。
        - 使用 **HMAC-SHA256** 算法对后四位进行哈希，生成一个固定长度的哈希值，并为其建立数据库索引。
        - 使用 **AES** 算法加密完整的手机号。
        - 将加密后的手机号和后四位的哈希值存入数据库。
    2. **查询**:
        - 计算查询条件（手机号后四位）的哈希值。
        - 利用索引快速从数据库中精确查找匹配该哈希值的记录。
        - 对查询到的数据进行 **二次验证**：解密完整的手机号，并校验其是否真的以后四位结尾，以过滤哈希碰撞的无效数据。

### 2. N-gram分词 + 哈希索引 (N-gram + Hash Index)

此方案适用于非结构化文本（如姓名、地址）的模糊查询。它将文本拆分为连续的片段（N-gram），并对这些片段进行索引。

- **服务接口**: `cn.clancy.service.NgramService`
- **实现逻辑**:
    1. **存储**:
        - 使用 **N-gram** 算法（本项目中 N=2，即 bigram）将用户名切分为多个连续的两个字符的片段。例如，“张三丰” -> {“张三”, “三丰”}。
        - 对每个分词片段进行 **HMAC-SHA256** 哈希。
        - 将所有分词的哈希值拼接成一个字符串，存储在特定字段中，并为此字段建立全文索引。
        - 使用 **AES** 加密完整的用户名。
        - 将加密后的用户名和分词哈希字符串存入数据库。
    2. **查询**:
        - 对查询关键词（例如 "三丰"）进行同样的分词和哈希处理。
        - 在数据库中查询包含所有关键词分词哈希的记录。
        - 对查询到的数据进行 **二次验证**：解密完整的用户名，并校验其是否真的包含查询关键词，以确保结果的准确性。

## 工具类

- **`EncryptUtil`**: 封装了加密和哈希算法。
    - `aesEncrypt(String)` / `aesDecrypt(String)`: 提供AES对称加密和解密功能。
    - `hmac(String)`: 提供HMAC-SHA256哈希功能。

> **注意**: 为了演示方便，本项目的AES和HMAC密钥均硬编码在代码中。在生产环境中，应使用更安全的密钥管理方案（如配置中心、KMS等）。

## 如何使用

项目通过 `JUnit` 测试用例展示了两个服务的使用方法。您可以直接运行测试来观察效果。

- **部分匹配查询测试**: `PartMatchServiceImplTest.java`
- **N-gram模糊查询测试**: `NgramServiceImplTest.java`

### 示例

**保存和查询手机号:**
```java
// 注入服务
PartMatchService partMatchService = new PartMatchServiceImpl();

// 保存
partMatchService.saveMobile("13812345678");

// 根据后四位查询
List<UserEntity> users = partMatchService.queryByMobileSuffix("5678");
// users.get(0).getMobile() 将返回 "1381234-5678" 的AES加密字符串
```

**保存和查询用户名:**
```java
// 注入服务
NgramService ngramService = new NgramServiceImpl();

// 保存
ngramService.saveUsername("ClancyLv");

// 模糊查询
List<UserEntity> users = ngramService.queryByUsername("ncyL");
// users.get(0).getUsername() 将返回 "ClancyLv" 的AES加密字符串
```

## 主要依赖

- **Spring Boot**: 基础框架。
- **Hutool**: 提供了便捷的加密 (`hutool-crypto`) 和核心 (`hutool-core`) 工具。
- **Lombok**: 简化Java代码。
