# TossBeneface

인텔리제이 실행시 vm option으로

```Bash
-Djasypt.password=sakncksjallkasdkl#$@^#*asdsiajodias2737
```

추가해야 동작함

(*jasypt : api key등 양방향 암호화 해주는 라이브러리)



### 회원가입 요청
POST http://localhost:8080/api/join
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123",
  "memberName": "Test User",
  "phoneNumber": "010-000-0000",
  "role": "USER",
  "gender": "FEMALE"
}


###
### 로그인 요청
POST http://localhost:8080/api/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
