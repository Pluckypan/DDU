## 输出依赖

```bash

# 输出目录 build/reports/project/dependencies/
$ ./gradlew app:htmlDependencyReport

# 输出目录 build/reports/dependency-graph
$ ./gradlew app:generateDependencyGraph

# 原始依赖报告
$ ./gradlew app:dependencies > d.txt

```