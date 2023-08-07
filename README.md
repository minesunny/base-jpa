# base-jpa

### 1. com.minesunny.jpa.entity.BaseEntity

* 父类共同信息（没有写入id为共用字段，是因为id生成策略有多种，可自定义配置）
* 提供搜索 Specification的方法

### 2.com.minesunny.jpa.manager.SdtNodeMgr 组建树结构

### 3. com.minesunny.jpa.manager.MapMgr 关系表工具，主要优化n+1问题。

    *   该接口可能会影响hibernate entitymanager Context的缓存