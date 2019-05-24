### ProtoBuf Support

### 定义 `.proto`

```
syntax = "proto2";

package engineer.echo;

option java_package = "engineer.echo.proto";
option java_outer_classname = "UserEntity";

message User {
    required string name = 1; //姓名
    required int32 id = 2; //id
    optional string email = 3; //邮件

    // 电话类型
    enum PhoneType {
        MOBILE = 0;
        HOME = 1;
        WORK = 2;
    }

    // 电话号码
    message PhoneNumber {
        required string number = 1;
        optional PhoneType type = 2 [default = HOME];
    }

    // 可以有多个电话号码
    repeated PhoneNumber phone = 4;
}
```
### 赋值
```
UserEntity.User.newBuilder().apply {
            email = "plucky@echo.engineer"
            id = 1
            name = "Plucky"
            val phone1 = UserEntity.User.PhoneNumber.newBuilder()
                .setNumber("10086")
                .setType(UserEntity.User.PhoneType.HOME)
                .build()
            val phone2 = UserEntity.User.PhoneNumber.newBuilder()
                .setNumber("10010")
                .setType(UserEntity.User.PhoneType.WORK)
                .build()
            addPhone(phone1)
            addPhone(phone2)
        }.build().also {
            mBinding.user = it.toString()
        }
```