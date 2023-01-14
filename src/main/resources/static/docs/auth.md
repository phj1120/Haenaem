# 인증

## 회원 가입

POST /api/auth/sign-up

### 요청

* BODY

```
{
    "authId" : "phj123",
    "password" : "password",
    "name" : "parkh",
    "email" : "hyeonj1998@naver.com"
}
```

### 응답

* 회원 가입에 성공 한 경우

```
HTTP STATUS : 200

{
    "data": {
        "id": 1,
        "name": "parkh"
    },
    "error": null
}
```

* 요청 한 이메일, 아이디로 가입 된 사용자가 있는 경우

```
HTTP STATUS : 400

{
    "data": null,
    "error": "이미 존재 하는 값"
}
```

## 로그인

POST /api/auth/sign-in

### 요청

* BODY

```
{
    "authId" : "parkh",
    "password" : "password"
}
```

### 응답

* 로그인에 성공한 경우

```
HTTP STATUS : 200

{
    "data": {
        "id": 1,
        "name": "parkh",
        "authId": "phj123",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2Njk4MDYxMDAsInN1YiI6InBoajEyMyIsImV4cCI6MTY2OTg5MjUwMH0.9HAmOX6BM4ZcH6k6KBi56cGsmOIAxM-ROKVri1ggOuoez-Wkk98eDSiZSAH52_eHgWYp70vwDZD8AbvWV0fX-Q"
    },
    "error": null
}
```

* 존재하지 않는 아이디일 경우

```
HTTP STATUS : 400

{
    "data": null,
    "error": "존재 하지 않는 아이디"
}
```

* 비밀번호가 일치하지 않을 경우

```
HTTP STATUS : 400

{
    "data": null,
    "error": "비밀번호 불일치"
}
```

## 회원 탈퇴

POST /api/auth/withdrawal

### 요청

* BODY

```
{
    "authId" : "parkh",
    "password" : "password"
}
```

### 응답

* 정상적으로 회원 탈퇴 된 경우

```
HTTP STATUS : 200

{
    "data": {
        "status": true
    },
    "error": null
}
```

* 존재하지 않는 아이디일 경우

```
HTTP STATUS : 400

{
    "data": null,
    "error": "존재 하지 않는 아이디"
}
```

* 비밀번호 틀릴 경우

```
HTTP STATUS : 400

{
    "data": null,
    "error": "비밀번호 불일치"
}
```

## 사용자 ID 사용 가능 여부 확인

GET /api/auth/check/auth-id/{authId}

ex)api/auth/check/auth-id/parkh

### 요청

URL 에 사용자 아이디(문자열) 넘겨 줌

### 응답

* 사용 가능한 아이디일 경우

```
HTTP STATUS : 200

{
    "data": {
        "usable": true
    },
    "error": null
}
```

* 사용 불가능한 아이디일 경우

```
HTTP STATUS : 200

{
    "data": {
        "usable": false
    },
    "error": null
}
```

## 사용자 Email 사용 가능 여부 확인

GET /api/auth/check/email/{email}

ex)}api/auth/check/email/hyeonj1998@naver.com

### 요청

URL 에 사용자 이메일 넘겨 줌

### 응답

* 사용 가능한 이메일일 경우

```
HTTP STATUS : 200

{
    "data": {
        "usable": true
    },
    "error": null
}
```

* 사용 불가능한 이메일 경우

```
HTTP STATUS : 200

{
    "data": {
        "usable": false
    },
    "error": null
}
```

## 인증 번호 전송

POST /api/auth/send/auth-key

### 요청

* BODY

회원 가입 일 경우 SIGN_UP / 비밀번호 찾기일 경우 FORGET_PASSWORD

```
{
    "authId": "parkh",
    "email": "hyeonj1998@naver.com",
    "type": "SIGN_UP" // SIGN_UP, FORGET_PASSWORD
}
```

### 응답

* 정상적으로 인증번호가 전송 된 경우

```
HTTP STATUS : 200

{
    "data": {
        "state": true
    },
    "error": null
}
```

* 이미 존재하는 이메일로 회원 가입 요청을 보냈을 경우

```
HTTP STATUS : 400

{
    "data": null,
    "error": "이미 존재 하는 이메일"
}
```

## 인증 번호 확인

GET /api/auth/check/auth-code

ex)}api/auth/check/email/hyeonj1998@naver.com

### 요청

* BODY

회원 가입 일 경우 SIGN_UP / 비밀번호 찾기일 경우 FORGET_PASSWORD

```
{
    "authId":"parkh",
    "email":"hyeonj1998@naver.com",
    "authCode":"e09d93",
    "type":"SIGN_UP" // SIGN_UP, FORGET_PASSWORD
}
```

### 응답

* 인증 번호가 정상적으로 확인 된 경우

회원가입의 경우 같은 타입의 요청, 10분 이내의 요청 일 경우 정상적인 인증 코드로 판단

```
HTTP STATUS : 200

{
    "data": {
        "usable": true
    },
    "error": null
}
```

* 사용 불가능한 이메일 경우

```
HTTP STATUS : 200

{
    "data": {
        "usable": false
    },
    "error": null
}
```

## 비밀번호 찾기 (비밀번호 변경)

POST /api/auth/reset/password

### 요청

사용자 아이디(authId) 가 아닌 숫자로 된 사용자 번호(userId)

```
{
    "userId":1,
    "authCode":"ad6045",
    "newPassword":"hi"
}
```

### 응답

* 정상적으로 변경 됐을 경우

```
{
    "data": {
        "userId": 1
    },
    "error": null
}
```

* 인증 번호가 틀렸을 경우

```
{
    "data": null,
    "error": "올바르지 않은 접근 권한"
}
```