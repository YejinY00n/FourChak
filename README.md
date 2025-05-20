# 🍽️ FourChak

## 팀원 및 역할
```

```

---

## 프로그램 설명 (FourChak)


---

## 주요 구현 기능
### 1. 회원가입/로그인

이메일 형식 아이디 + 영문 대소문자/숫자 조합 8자 이상 비밀번호로 회원가입
JWT를 활용한 인증 방식 구현
가입 시 USER(일반 사용자) / OWNER(가게 소유자) 역할 선택
회원 정보 수정 (이름, 전화번호) 및 비밀번호 변경 기능
회원 탈퇴 시 소프트 딜리트 처리

### 2. 가게 관리

OWNER 권한을 가진 사용자만 가게 등록 가능
가게 정보 (이름, 전화번호, 좌석 수) 관리
가게 조회 시 남은 좌석 수 실시간 확인 가능
가게 폐업 처리 (소프트 딜리트)
가게 검색 기능 (이름 기준 LIKE 검색)
인기 검색어 기능 (검색 횟수 기준 상위 10개)

### 3. 예약 시스템

사용자는 원하는 시간과 인원 수로 가게 예약 가능
현재 가용 좌석 수에 따라 예약 가능 여부 실시간 확인
예약 시간이 지나면 자동으로 만료 처리
사용자별 예약 내역 조회
가게별 예약 내역 조회 (가게 소유자만 가능)

### 4. 대기 시스템

예약이 불가능한 경우 대기 등록 가능
대기 번호 자동 발급 및 상태 관리
사용자별 대기 내역 조회
대기 상태 변경 (대기 중, 입장 완료, 취소)

### 5. 쿠폰 시스템

가게 소유자는 할인 쿠폰 발행 가능
쿠폰 수량 및 할인율 설정
가게별 발행된 쿠폰 조회

### 6. 캐싱 성능 최적화

검색 API에 Local Memory Cache 적용
Redis를 활용한 Remote Cache 구현
캐시 무효화 전략 적용
캐시 TTL 설정으로 데이터 일관성 유지

### 7. 보안 기능

Spring Security 기반 인증/인가 구현
JWT 토큰을 활용한 사용자 인증
비밀번호 암호화 저장
API 엔드포인트별 권한 설정

### 8. 부가 기능

인기 검색어 순위 제공
시간대별 예약 현황 조회
사용자 활동 이력 관리
만료된 예약 자동 정리 배치 작업

---

## 기술적 포인트
#### 1. JWT 기반 로그인 구현

#### 2. Cache 검색 기능 구현

---

## 기술 스택
- Java 17 
- Spring Boot
- MySQL
- Redis
- Cache
- Spring Data JPA
- Postman (API 테스트용)
- JWT 기반 로그인, 인증/인가
- IntelliJ IDEA
- Lombok, Jakarta Validation
- GitHub

---

### 변수 지정표
![image](https://github.com/user-attachments/assets/efac7732-9ea7-4b29-8535-9c1a416549da)

---

## 연관관계

<img width="634" alt="image" src="https://github.com/user-attachments/assets/ddafbca9-616e-42fa-b2b6-fc0f10d2cd60" />

---

## API 명세서

### User, Auth


### Store


### Reservation


### Waiting


### Coupon


### SearchKeyword


---

## ERD 작성

![FourChak](https://github.com/user-attachments/assets/091d09bb-1015-41f6-b308-c2796579ed20)

---

## SQL 작성

```
CREATE TABLE `store` (
	`ID`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`storename`	VARCHAR(10)	NULL,
	`number`	VARCHAR(20)	NULL,
	`seatCount`	INT	NULL,
	`deletedat`	DATETIME	NULL,
	`userId`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK
);

CREATE TABLE `baseentity` (
	`createdat`	DATETIME	NULL,
	`modifiedat`	DATETIME	NULL
);

CREATE TABLE `user` (
	`ID`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`email`	VARCHAR(30)	NULL,
	`username`	VARCHAR(20)	NULL,
	`phone`	VARCHAR(20)	NULL,
	`password`	VARCHAR(20)	NULL,
	`role`	ENUM	NULL
);

CREATE TABLE `Untitled2` (
	`PK`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`FK`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`FK,`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`field 1`	int	NULL,
	`field 2`	DateTime	NULL,
	`field 3`	Int	NULL,
	`field 4`	ENUM	NULL
);

CREATE TABLE `coupon` (
	`id`	BIGINT	NOT NULL,
	`ID`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`discount`	INTEGER	NULL,
	`count`	INTEGER	NULL
);

CREATE TABLE `searchkeyword` (
	`ID`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`keyword`	VARCHAR(20)	NULL,
	`count`	INT	NULL
);

CREATE TABLE `Untitled3` (
	`Key`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`PeopleNumber`	Int	NULL,
	`DateTime`	DateTime	NULL,
	`userId`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`storeId`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`Id2`	BIGINT	NOT NULL
);

CREATE TABLE `UserCoupon` (
	`Id`	BIGINT	NOT NULL,
	`couponId`	BIGINT	NOT NULL,
	`storeId`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`userId`	BIGINT(PK, Auto Increment)	NOT NULL	DEFAULT PK,
	`isUsed`	BOOLEAN	NULL
);

ALTER TABLE `store` ADD CONSTRAINT `PK_STORE` PRIMARY KEY (
	`ID`
);

ALTER TABLE `user` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`ID`
);

ALTER TABLE `Untitled2` ADD CONSTRAINT `PK_UNTITLED2` PRIMARY KEY (
	`PK`,
	`FK`,
	`FK,`
);

ALTER TABLE `coupon` ADD CONSTRAINT `PK_COUPON` PRIMARY KEY (
	`id`,
	`ID`
);

ALTER TABLE `searchkeyword` ADD CONSTRAINT `PK_SEARCHKEYWORD` PRIMARY KEY (
	`ID`
);

ALTER TABLE `Untitled3` ADD CONSTRAINT `PK_UNTITLED3` PRIMARY KEY (
	`Key`
);

ALTER TABLE `UserCoupon` ADD CONSTRAINT `PK_USERCOUPON` PRIMARY KEY (
	`Id`,
	`couponId`,
	`storeId`,
	`userId`
);

ALTER TABLE `Untitled2` ADD CONSTRAINT `FK_user_TO_Untitled2_1` FOREIGN KEY (
	`FK`
)
REFERENCES `user` (
	`ID`
);

ALTER TABLE `Untitled2` ADD CONSTRAINT `FK_store_TO_Untitled2_1` FOREIGN KEY (
	`FK,`
)
REFERENCES `store` (
	`ID`
);

ALTER TABLE `coupon` ADD CONSTRAINT `FK_store_TO_coupon_1` FOREIGN KEY (
	`ID`
)
REFERENCES `store` (
	`ID`
);

ALTER TABLE `UserCoupon` ADD CONSTRAINT `FK_coupon_TO_UserCoupon_1` FOREIGN KEY (
	`couponId`
)
REFERENCES `coupon` (
	`id`
);

ALTER TABLE `UserCoupon` ADD CONSTRAINT `FK_coupon_TO_UserCoupon_2` FOREIGN KEY (
	`storeId`
)
REFERENCES `coupon` (
	`ID`
);

ALTER TABLE `UserCoupon` ADD CONSTRAINT `FK_user_TO_UserCoupon_1` FOREIGN KEY (
	`userId`
)
REFERENCES `user` (
	`ID`
);
```

---

## 패키지 구조


