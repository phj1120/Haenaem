본인 정보만 수정 가능하도록 JWT 에 있는 정보 기반으로
챌린지 조회, 신청, 취소 / 포인트 충전, 포인트 조회 가능하도록 수정

### 챌린지 조회
{{domain}}/api/challenger/challenge?status={status}

```
status
    RECRUITING 모집 중
    CHALLENGING 진행 중
    FINISHED 완료
```

### 챌린지 신청
{{domain}}/api/challenger/challenge/{challengeId}/request

### 챌린지 신청 취소
{{domain}}/api/challenger/challenge/{challengeId}/cancel

### 포인트 충전
{{domain}}/api/point/charge/{pointAmount}

### 포인트 조회
{{domain}}/api/point/user