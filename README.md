# 简介

`Gradle` 是一个基于 `Apache Ant` 和 `Apache Maven` 概念的项目自动化构建开源工具。它使用一种基于 `Groovy` 的领域特定语言( `DSL` )来声明项目设置，也增加了基于 `Kotlin` 语言的 `kotlin-based DSL`，抛弃了基于 `XML` 的各种繁琐配置。

# 下载安装

下载地址：[`https://gradle.org/releases/`](https://gradle.org/releases/)

解压后放至个人工作目录。例如：`/Users/llnancy/workspace`。

# 配置环境变量

以 `MacOS` 系统为例，编辑 `.bash_profile` 文件，内容如下：

```text
GRADLE_HOME=/Users/llnancy/workspace/gradle-7.5.1
export GRADLE_HOME
export PATH=$GRADLE_HOME/bin:$PATH
```

执行命令 `source .bash_profile` 使之生效。

# Gradle 常用命令

- `gradle clean`：清空 `build` 目录。
- `gradle classes`：编译 `src` 目录代码。
- `gradle test`：编译测试代码。
- `gradle build`：构建工程。
- `gradle build -x test`：跳过测试构建工程。

# Groovy 语言

`Gradle` 的配置文件基于 `Groovy` 语言编写。

`Groovy` 是一种基于 `JVM`（`Java` 虚拟机）的敏捷开发语言，它结合了 `Python`、`Ruby` 和 `Smalltalk` 的许多强大的特性，`Groovy` 代码能够与 `Java` 代码很好地结合，也能用于扩展现有代码。由于其运行在 `JVM` 上的特性，`Groovy` 也可以使用其他非 `Java` 语言编写的库。

`Groovy` 语言的基础语法可前往 [`https://cloud.tencent.com/developer/doc/1309`](https://cloud.tencent.com/developer/doc/1309) 进行学习查看。

# 修改下载源

进入 `Gradle` 安装目录，在 `init.d` 目录下创建 `init.gradle` 文件，内容如下：

```text
allprojects {
    repositories {
        // mavenLocal();
        // 配置 maven 本地仓库地址
        maven { name 'local'; url '/Users/llnancy/workspace/mvn-repository' }
        maven { name 'aliyun'; url 'https://maven.aliyun.com/repository/public' }
        mavenCentral();
    }

    buildscript {
        repositories {
            maven { name 'local'; url '/Users/llnancy/workspace/mvn-repository' }
            maven { name 'aliyun'; url 'https://maven.aliyun.com/repository/public' }
        }
    }
}
```

# IDEA 配置

全局配置：

选择 `Build, Execution, Deployment -> Build Tools -> Gradle`，配置 `Gradle user home`，默认为 `/Users/llnancy/.gradle`，如果不想放在该目录，可以自行指定，但最后的文件名一定是 `.gradle`，例如：`/Users/llnancy/workspace/maven-repository/.gradle`。

配置完成后，所有 `gradle` 项目下载的依赖 `jar` 包都将放置在 `/Users/llnancy/workspace/maven-repository/.gradle/caches/modules-2/files-2.1` 目录下。

项目配置：

配置 `Use Gradle from` 为 `Specifield location`，选择本地 `gradle` 安装目录。之后该项目下载依赖 `jar` 包就会按照 `init.gradle` 中配置的 `repositories` 规则进行。

# IDEA 新建 Gradle 项目

## 无 src 目录问题

新建 `Gradle` 项目默认使用 `gradle-wrapper` 下载的 `Gradle`，请等待构建任务全部执行完成后，再将项目配置的 `Use Gradle from` 切换为 `Specifield location`。否则可能导致生成 `src` 目录的任务取消执行或执行失败。

# Gradle 项目单元测试

单元测试代码默认放置在 `src/test` 目录下，点击 `Tasks/verification/test` 可执行单元测试任务（或者在 `build.gradle` 文件所在目录下执行 `gradle test` 命令），执行完成后会在 `build/reports/tests/test` 目录下生成测试报告。

## Junit4

`build.gradle`:

```text
dependencies {
    testImplementation 'junit:junit:4.13.2'
}

test {
    useJUnit()
}
```

## Junit5

`build.gradle`:

```text
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
}

test {
    useJUnitPlatform()
}
```

## 指定或排除特定测试

指定仅执行某些包下的单元测试，或者排除某些包下的单元测试。

`build.gradle`:

```text
test {
    // Junit5
    useJUnitPlatform()
    enabled(true)
    include('com/sunchaser/template/gradle/test/**')
    exclude('com/sunchaser/template/gradle/test/**')
}
```

注意要用 `/` 分隔包名。

# dependencies

`Gradle`中的依赖分为直接依赖、项目依赖和本地 `jar` 依赖。

## 直接依赖

项目中直接引入的依赖称为直接依赖。分为四个部分：依赖类型、依赖组名、依赖名称和依赖版本号。

例如：

```text
dependencies {
    // 简写形式。groovy 语法中小括号也可省略。
    implementation('org.apache.logging.log4j:log4j:2.17.2')
    // 完整写法
    // 依赖类型：implementation
    // 依赖组名（group）：org.apache.logging.log4j
    // 依赖名称（name）：log4j
    // 依赖版本号（version）：2.17.2
    implementation(group: 'org.apache.logging.log4j', name: 'log4j', version: '2.17.2')
}
```

## 项目依赖

依赖工程中的某个子模块。首先需在项目根目录下的 `setting.gradle` 文件中 `include` 子模块，然后使用 `project` 引入。

例如依赖项目公共模块 `sunchaser-common`：

```text
dependencies {
    implementation(project(':sunchaser-common'))
}
```

> 冒号 `:` 表示相对路径，可省略。

## 本地 jar 依赖

依赖本地文件系统的某个 `jar` 包。可通过文件集合或文件树的方式指定。

例如依赖 `src/main/resources/lib` 目录下的 `commons-lang3-3.8.1.jar` 包。

文件集合方式：

```text
dependencies {
    implementation(files('src/main/resources/lib/commons-lang3-3.8.1.jar'))
}
```

文件树方式：

```text
dependencies {
    implementation(fileTree('dir': 'src/main/resources/lib', includes: ['*.jar'], excludes: []))
}
```

## 依赖类型

类似于 `maven` 的 `scope` 标签。`gradle` 中依赖类型有如下：

| 依赖类型             | 描述                                                                                                      |
| -------------------- | --------------------------------------------------------------------------------------------------------- |
| `compileOnly`        | 由 `java` 插件提供，曾短暂的叫 `provided`，新版本中改为了 `compileOnly`。适用于编译期需要但不需要打包的情况。 |
| `runtimeOnly`        | 由 `java` 插件提供，只在运行期有效，编译期不需要。例如 `mysql` 驱动包。取代老版本中被移除的 `runtime`。        |
| `implementation`     | 由 `java` 插件提供，针对源码（`src/main` 目录），在编译期和运行期都有效。取代老版本中被移除的 `compile`。     |
| `testCompileOnly`    | 由 `java` 插件提供，用于编译测试代码的依赖项，运行期不需要。                                                |
| `testRuntimeOnly`    | 由 `java` 插件提供，只在测试代码运行期需要，测试编译期不需要。取代老版本中被移除的 `testRuntime`。           |
| `testImplementation` | 由 `java` 插件提供，针对测试代码（`src/test` 目录）。取代老版本中被移除的 `testCompile`。                     |
| `provideCompile`     | 由 `war` 插件提供，仅在编译期和测试期需要，例如 `servlet-api.jar`。                                          |
| `api`                | 由 `java-library` 插件提供，支持依赖的传递，在编译期和运行期都有效。取代老版本中被移除的 `compile`。         |
| `compileOnlyApi`     | 由 `java-library` 插件提供，仅在编译期有效。                                                                |

### api 和 implementation 的区别

|          | api                                | implementation                   |
| -------- | ------------------------------------ | ---------------------------------- |
| 编译期   | 依赖具有传递性，会导致编译速度变慢。 | 不具有传递性，编译速度快。         |
| 运行期   | 运行期加载所有模块的 `class`。        | 运行期加载所有模块的 `class`。      |
| 应用场景 | 适用于多模块项目，避免重复依赖。     | 大多数情况下使用 `implementation`。 |

例如有 `ABC` 三个模块：

- `A implementation B; B implementation C`，则 `A` 不能使用 `C`。
- `A implementation B; B api C`，则 `A` 可以使用 `C`。

## 依赖冲突及解决方案

依赖冲突是指在编译过程中，如果某个依赖存在多个版本，`gradle` 应该选择哪个版本进行构建的问题。

### 默认解决方案

`gradle` 默认的解决方案是选用高版本的依赖（高版本通常会向下兼容）。

### 依赖排除后手动指定

类似于 `maven` 中的 `exclusion` 标签，使用 `exclude` 进行依赖排除，然后再手动指定。

例如：

```text
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web:2.7.4') {
        exclude(group: 'org.slf4j', module: 'slf4j-api')
    }
    implementation('org.slf4j:slf4j-api:2.0.1')
}
```

### 不允许依赖传递（一般不使用）

关闭依赖传递，全部手动指定。一般不使用这种方式。

```text
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web:2.7.4') {
        // 关闭依赖传递
        transitive(false)
    }
    implementation('org.slf4j:slf4j-api:2.0.1')
}
```

### 强制指定版本号

写法一：双叹号简写语法

```text
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web:2.7.4')
    implementation('org.slf4j:slf4j-api:2.0.1!!')
}
```

写法二：

```text
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web:2.7.4')
    implementation('org.slf4j:slf4j-api') {
        version {
            strictly '2.0.1'
        }
    }
}
```

### 动态版本号（一直使用最新版）

写法一：加号 `+`

```text
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web')
    implementation('org.slf4j:slf4j-api:+')
}
```

写法二：`latest.integration`

```text
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web')
    implementation('org.slf4j:slf4j-api:latest.integration')
}
```

一般不推荐使用。

### 配置发生依赖冲突后，立即构建失败

```text
// 依赖冲突时，立即构建失败
configurations.all {
    Configuration configuration -> {
        configuration.resolutionStrategy.failOnVersionConflict()
    }
}
```

# Gradle 插件

插件可以做的事情：

- 添加任务到 `task` 中帮助完成测试、编译及打包等过程。
- 添加依赖配置到项目中。例如 `java` 插件提供依赖类型。
- 给项目添加扩展属性和方法等。例如 `java` 插件可使用 `sourceCompatibility` 给项目设置 `JDK` 版本信息，类似于 `maven` 中的 `maven.compiler.source` 标签。
- 进行约定，例如 `java` 插件约定 `src/main/java` 目录为源代码目录。

## 插件分类

`Gradle` 插件分为：

- 脚本插件
- 二进制插件（对象插件）
  - 内部插件
  - 第三方插件
  - 自定义插件

### 脚本插件

脚本插件本质是一个脚本文件，可通过 `apply from:` 进行加载。脚本文件可以来源于本地或互联网。下面定义 `version.gradle` 脚本插件并进行使用。

`version.gradle`:

```text
ext {
    organization = 'SunChaser'
    jdk = [compileJdkVersion: JavaVersion.VERSION_1_8]
    spring = [version: '5.3.23']
}
```

`build.gradle` 中使用：

```text
apply from: 'version.gradle'

task taskVersion {
    doLast {
        println "organization is ${organization}, jdk verison is ${jdk.compileJdkVersion}, spring version is ${spring.version}."
    }
}
```

### 二进制插件（对象插件）

二进制插件实现了 `org.gradle.api.Plugin` 接口，每个插件都有一个唯一的 `plugin id`。包含内部插件、第三方插件和自定义插件。

#### 内部插件

例如 `java` 插件。使用方式有以下：

方式一：`plugins dsl`

```text
plugins {
    id 'java'
}
```

方式二：`apply` 方式

形式一：`apply`(`map` 具名参数)

`map` 的 `key` 为固定值 `plugin`，`value` 有三种写法：插件 `ID`、插件的全限定类名和插件的简类名。

写法一：插件 `ID`

```text
apply(plugin: 'java')
```

> `groovy` 语法中小括号也可省略：`apply plugin: 'java'`。

写法二：插件全限定类名

```text
apply plugin: org.gradle.api.plugins.JavaPlugin
```

写法三：插件简类名

被 `gradle` 默认导入的包下的插件可写简类名。例如 `org.gradle.api.plugins` 包就已经被导入过。

```text
apply plugin: JavaPlugin
```

形式二：`apply` 闭包

```text
apply {
    plugin 'java'
}
```

#### 第三方插件

非 `gradle` 官方提供的插件。可前往 [https://plugins.gradle.org](https://plugins.gradle.org) 进行查看。使用方式有以下两种：

方式一：`plugins dsl`

```text
plugins {
  id 'org.springframework.boot' version '2.7.4'
}
```

方式二（旧版）：

先配置 `maven` 仓库地址和类路径 `classpath`，再 `apply` 应用插件。

```text
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "org.springframework.boot:spring-boot-gradle-plugin:2.7.4"
  }
}

apply plugin: "org.springframework.boot"
```

注意旧版方式 `buildscript` 必须写在 `build.gradle` 文件顶部。且不能和 `plugins dsl` 形式混用。

#### 自定义插件

参考官方教程 [`https://docs.gradle.org/current/userguide/custom_plugins.html`](https://docs.gradle.org/current/userguide/custom_plugins.html)。

自定义插件仅能当前项目使用，有一定局限性，一般不推荐。

## buildSrc

`buildSrc` 是 `Gradle` 默认的插件目录，编译 `Gradle` 的时候会自动识别该目录，将其中的代码编译成插件。

# build.gradle 文件

`build.gradle` 是 `Gradle` 项目的构建脚本文件，支持 `Java` 和 `groovy` 等语言。每个 `build.gradle` 文件都对应一个 `org.gradle.api.Project` 类的实例，文件中的内容本质是调用 `Project` 类的方法。常用配置项有：

- `group`：项目组织。相当于 `maven` 中 `GAV` 坐标的 `<group>` 标签。
- `name`：项目名。相当于 `maven` 中 `GAV` 坐标的 `<artifact>` 标签。
- `version`：项目版本号。相当于 `maven` 中 `GAV` 坐标的 `<version>` 标签。
- `sourceCompatibility`：项目编译版本。相当于 `maven` 项目中的 `<maven.compiler.source>` 标签。
- `targetCompatibility`：项目 `class` 字节码版本。相当于`maven`项目中的 `<maven.compiler.target>` 标签。
- `compileJava.options.encoding`：源代码编码字符集。相当于 `maven` 项目中的 `<project.build.sourceEncoding>` 标签。
- `compileTestJava.options.encoding`：测试代码编码字符集。相当于 `maven` 项目中的 `<project.reporting.outputEncoding>` 标签。
- `ext`：自定义属性。
- `plugins`：新版插件应用方式。
- `task`：定义任务。
- `repositories`：`maven` 仓库信息。
- `dependencies`：项目依赖信息。
- `allprojects`：设置当前 `project` 及所有子 `project` 信息。
- `subprojects`：设置所有子 `project` 信息。
- `sourceSets`：设置项目源集。
- `publishing`：设置发布插件信息。
- `configurations`：设置项目依赖项配置。
- `artifacts`：设置项目已发布构件。

常用配置示例如下：

```text
import java.nio.charset.StandardCharsets

sourceCompatibility = JavaVersion.VERSION_1_8
targetCompatibility = JavaVersion.VERSION_1_8
compileJava.options.encoding = StandardCharsets.UTF_8
compileTestJava.options.encoding = StandardCharsets.UTF_8
```

# gradle.properties 文件

可用于配置系统属性、环境变量、项目属性和 `JVM` 参数等信息。常用配置如下：

```text
# 详细配置可查看 https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties
# 设置 gradle 运行时 JVM 参数，防止编译下载包时内存溢出。
org.gradle.jvmargs=-Xmx2g -Dfile.encoding=UTF-8
# 开启 gradle 缓存
org.gradle.caching=true
# 开启并行编译
org.gradle.parallel=true
# 开启新的孵化模式
org.gradle.configureondemand=true
# 开启守护进程
org.gradle.daemon=true
```

# publishing 项目打包发布

将某个模块打成 `jar` 包进行发布供他人进行使用。

## 引入插件

`java-library` 插件支持带 `source` 源码和 `JavaDoc` 文档的发布。

```text
plugins {
    id 'java'
    id 'java-library'
    id 'maven-publish'
}
```

## 编写发布代码

示例如下：

```text
javadoc.options.encoding = StandardCharsets.UTF_8

java {
    // 带 source 源码包
    withSourcesJar()
    // 带 JavaDoc 文档包
    withJavadocJar()
}

publishing {
    publications {
        // 定义 templateGradleLibrary 发布任务，注意名称中不能包含短横线
        templateGradleLibrary(MavenPublication) {
            // maven GAV 坐标信息
            groupId = 'com.sunchaser.template'
            artifactId = 'template-gradle'
            version = '0.0.1'
            // 发布 jar 包固定写法：from components.java
            from components.java
        }
    }
    repositories {
        // 发布至 maven 本地仓库。默认位于 USER_HOME/.m2/repository
        // mavenLocal()
        // 发布至其它 maven 仓库
        maven {
            // name：maven 仓库名称。可选。
            name = 'localRepo'
            // url：发布地址，可以是本地仓库或者 maven 私服。必选。
            url = layout.buildDirectory.dir('repo')
            // url = 'https://repo.example.com'
            // 根据版本号是否以 SNAPSHOT 结尾判断发布至快照版仓库还是正式版仓库
            def snapshotRepo = layout.buildDirectory.dir('repos/snapshots')
            def releaseRepo = layout.buildDirectory.dir('repos/releases')
            url = version.endsWith('SNAPSHOT') ? snapshotRepo : releaseRepo
            // 认证信息（用户名和密码）
            // credentials {
            //     username: 'SunChaser'
            //     password: 'SunChaser-Gradle'
            // }
        }
    }
}
```

注意，`publications` 中的 `templateGradleLibrary` 名称不能包含短横线`-`，否则会引发类似以下报错：

```text
> Cannot create a Publication named 'template' because this container does not support creating elements by name alone. Please specify which subtype of Publication to create. Known subtypes are: MavenPublication
```

## 进行发布

以发布任务 `templateGradleLibrary` 为例，常用的发布指令有：

- `generatePomFileForTemplateGradleLibraryPublication`：生成 `pom` 文件。
- `publishTemplateGradleLibraryPublicationToLocalRepoRepository`：发布 `jar` 包至指定 `maven` 仓库，如果未指定仓库 `name` 属性，默认为 `maven`。
- `publishTemplateGradleLibraryPublicationToMavenLocal`：发布 `jar` 包至 `maven` 本地仓库，默认路径为 `USER_HOME/.m2/repository`。
- `publish`：发布 `jar` 包至 `repositories` 中指定的所有仓库。
- `publishToMavenLocal`：发布所有任务至 `maven` 本地仓库。

可在 `IDEA` 右侧 `Gradle` 工具中（`Tasks/publishing`）查看并执行。

# Gradle 生命周期

# 创建 Spring Boot 项目

可在 [`https://start.spring.io`](https://start.spring.io/) 网站在线创建。或在 `IDEA` 中选择 `Spring Initializr` 新建。

# 其它

## 关于字符串的单双引号问题

`gradle` 文件中，普通字符串优先选用单引号；需要使用 `$` 符号引用变量的字符串使用双引号。

## 选择 Maven 还是 Gradle？

教学类型项目用 `Maven`。其它类型项目根据个人喜好，可挑选一个项目使用 `Gradle` 进行构建。
