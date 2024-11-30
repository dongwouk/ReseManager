# 📑 API 명세서

## ✅ 사용자 관련 API

### 1) 사용자 생성
- **요청 경로**: `[POST] /member`
- **응답**: 사용자 ID, 이름, 상태 정보를 반환합니다.

### 2) 로그인
- **요청 경로**: `[POST] /login`
- **응답 (성공 시)**: 사용자 이름과 JWT 토큰을 반환합니다.
- **응답 (실패 시)**: 로그인 정보가 올바르지 않다는 메시지를 반환합니다.

## ✅ 매장 관련 API

### 1) 매장 등록
- **요청 경로**: `[POST] /store`
- **응답**: 매장의 ID, 소유자 ID, 이름, 위치, 설명을 반환합니다.

### 2) 매장 리스트 조회
- **요청 경로**: `[GET] /stores?page={currentPage}&size={pageSize}&sort=id,desc`
- **응답**: 총 매장 수, 페이지에 해당하는 매장 정보 목록을 반환합니다.

### 3) 매장 상세 조회
- **요청 경로**: `[GET] /store/15`
- **응답**: 해당 매장의 ID, 소유자 ID, 이름, 위치, 설명을 반환합니다.

### 4) 회원의 매장 조회
- **요청 경로**: `[GET] /store/member/1?page={currentPage}&size={pageSize}`
- **응답**: 총 매장 수, 회원이 소유한 매장의 정보 목록을 반환합니다.

### 5) 매장의 예약 정보 조회
- **요청 경로**: `[GET] /members/1/owned-stores/reservations?page={currentPage}&size={pageSize}`
- **응답**: 총 예약 수, 해당 매장의 예약 정보 목록을 반환합니다.

### 6) 매장 정보 수정
- **요청 경로**: `[PUT] /store/13`
- **응답**: 수정된 매장의 ID, 소유자 ID, 이름, 위치, 설명을 반환합니다.

### 7) 매장 삭제
- **요청 경로**: `[DELETE] /store/9`
- **결과**: 매장 정보가 성공적으로 삭제되었음을 반환합니다.
