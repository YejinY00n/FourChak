# 🍽️ FourChak
![ChatGPT Image 2025년 5월 20일 오후 01_00_28](https://github.com/user-attachments/assets/d365281a-f0b0-4553-9d68-2bc884982083)

## 팀원 및 역할
![image](https://github.com/user-attachments/assets/7c8e9803-fd33-4b72-a417-beaa9806a7f0)

---

## 프로그램 설명 (FourChak)

FourChak은 4조가 만든 좌석에 착하고 앉는 예약 프로그램으로 FourChak입니다.
FourChak은 사용자가 원하는 식당을 검색하고 실시간으로 예약 할 수 있도록 도와주는 식당 예약 플랫폼입니다. 실시간 예약 시스템을 통해 빠르고 편리한 외식 예약을 할 수 있게 구현하였습니다.

---

## 주요 구현 기능
### 1. 회원가입/로그인

- 이메일 형식 아이디 + 영문 대소문자/숫자 조합 8자 이상 비밀번호로 회원가입
- JWT를 활용한 인증 방식 구현
- 가입 시 USER(일반 사용자) / OWNER(가게 소유자) 역할 선택
- 회원 정보 수정 (이름, 전화번호) 및 비밀번호 변경 기능
- 회원 탈퇴 시 소프트 딜리트 처리

### 2. 가게 관리

- OWNER 권한을 가진 사용자만 가게 등록 가능
- 가게 정보 (이름, 전화번호, 좌석 수) 관리
- 가게 조회 시 남은 좌석 수 실시간 확인 가능
- 가게 폐업 처리 (소프트 딜리트)
- 가게 검색 기능 (이름 기준 LIKE 검색)
- 인기 검색어 기능 (검색 횟수 기준 상위 10개)

### 3. 예약 시스템

- 사용자는 원하는 시간과 인원 수로 가게 예약 가능
- 현재 가용 좌석 수에 따라 예약 가능 여부 실시간 확인
- 예약 시간이 지나면 자동으로 만료 처리
- 사용자별 예약 내역 조회
- 가게별 예약 내역 조회 (가게 소유자만 가능)

### 4. 대기 시스템

- 예약이 불가능한 경우 대기 등록 가능
- 대기 번호 자동 발급 및 상태 관리
- 사용자별 대기 내역 조회
- 대기 상태 변경 (대기 중, 입장 완료, 취소)

### 5. 쿠폰 시스템

- 가게 소유자는 할인 쿠폰 발행 가능
- 쿠폰 수량 및 할인율 설정
- 가게별 발행된 쿠폰 조회

### 6. 캐싱 성능 최적화

- 검색 API에 Local Memory Cache 적용
- Redis를 활용한 Remote Cache 구현
- 캐시 무효화 전략 적용
- 캐시 TTL 설정으로 데이터 일관성 유지

### 7. 보안 기능

- Spring Security 기반 인증/인가 구현
- JWT 토큰을 활용한 사용자 인증
- 비밀번호 암호화 저장
- API 엔드포인트별 권한 설정

### 8. 부가 기능

- 인기 검색어 순위 제공
- 시간대별 예약 현황 조회
- 사용자 활동 이력 관리
- 만료된 예약 자동 정리 배치 작업

---

## 기술적 포인트
### 1. JWT 기반 로그인 구현
- Spring Security와 JWT(Json Web Token)를 활용한 인증 시스템 구현

- Stateless한 구조로 세션 서버 부담 없이 사용자 인증 처리

- 사용자 로그인 시 JWT 발급, 이후 모든 요청에 헤더를 통해 인증 처리

### 2. Cache 검색 기능 구현

#### Redis 캐시 적용 배경
- 검색 특성: 동일 키워드 반복 검색 빈번
- DB 부하: LIKE 쿼리의 높은 비용
- 사용자 경험: 빠른 검색 응답 필요

#### 캐시전략
```
// 검색 결과 캐싱 (5분 TTL)
@Cacheable(value = "storeSearch", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
public Page<StoreResponseDto> searchStoreWithCache(String keyword, Pageable pageable)

// 인기 검색어 캐싱 (30분 TTL)
@Cacheable(value = "popularKeywords")
public List<PopularKeywordResponseDto> getPopularKeywords()
```

### 3. 동시성 제어

#### 문제 상황
- 예약 요청이 동시에 집중될 경우 데이터 정합성 유지 필요
- 예: 동일 시간대에 같은 테이블에 대한 중복 예약 시도

#### 해결 방법
- Redisson 분산 락 적용
	- Redisson을 사용하여 락 획득/해제의 원자성 보장
	- Lettuce보다 코드 간결하고 안정적인 락 처리 가능
- 트랜잭션보다 락이 먼저 해제되는 문제 예방
	- 트랜잭션 격리 수준을 READ_COMMITTED로 설정하여 데이터 정합성 강화


### 4. 인덱싱

#### 개념 요약
- 인덱스는 DB에서 데이터를 더 빠르게 조회할 수 있도록 돕는 저장 방식
- MySQL에서는 기본적으로 B+Tree 인덱스 사용

#### 적용방식
- Entity 내에서 @Id 또는 @Column(unique = true) 어노테이션 활용
- 자주 검색되거나 정렬에 사용되는 컬럼에 인덱스를 적용하여 쿼리 성능 개선

### 5. CI/CD
#### 목적
- 코드 변경 시 자동으로 빌드 및 배포되도록 하여 개발 효율성과 배포 안정성 향상
- 수동 배포로 인한 실수 및 반복 작업 최소화

#### Jenkins 기반 CI/CD 파이프라인 구축
- Jenkins가 프로젝트 변경 된 사항을 감지하여 ./gradlew build를 통해 .jar 파일 생성 (자동 빌드화)
#### 서버 배포 (CD) -> 할 예정
- 빌드된 .jar 파일을 AWS EC2 서버에 전송 (scp)
- 기존 애플리케이션 종료 후 새 버전 실행 (pkill, nohup java -jar)
- 필요한 경우 Nginx 또는 도메인 설정 연동

---

## 기술 스택
<img width="717" alt="image" src="https://github.com/user-attachments/assets/6887a134-2ce6-4fc6-9f55-297de2ac872c" />

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
![4조 - 초기 변수 저장](https://github.com/user-attachments/assets/bc583a01-e52c-4961-8d06-b67b9ba242f8)

---

## 연관관계

<img width="634" alt="image" src="https://github.com/user-attachments/assets/ddafbca9-616e-42fa-b2b6-fc0f10d2cd60" />

---

## API 명세서

### User, Auth
![KakaoTalk_Photo_2025-05-23-11-20-46](https://github.com/user-attachments/assets/f9948de3-6b16-4363-a56c-66bd0d5a556f)

### Store
![KakaoTalk_Photo_2025-05-23-11-21-09](https://github.com/user-attachments/assets/30e743e3-6cb0-4922-9c40-eb54d9cf6e6e)

### Reservation
![KakaoTalk_Photo_2025-05-23-11-21-24](https://github.com/user-attachments/assets/d79a71bf-c3a6-4c83-bd8e-0878c3370db9)

### Waiting
![KakaoTalk_Photo_2025-05-23-11-22-22](https://github.com/user-attachments/assets/1fa65bd1-9851-4dbd-90f2-b4ab937aebb0)


### Coupon
![KakaoTalk_Photo_2025-05-23-11-22-12](https://github.com/user-attachments/assets/5f24ea79-7377-42c3-b28a-6e31b1803ed2)


### SearchKeyword
![KakaoTalk_Photo_2025-05-23-11-22-39](https://github.com/user-attachments/assets/23f04b07-6e83-4131-844d-a7d84671779c)


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
![image](https://github.com/user-attachments/assets/d3c7ba9b-c6c0-451d-9076-f7f81a76af42)

---

## 검색 API 캐시 적용

### 문제 상황

- 실시간 검색의 특성: 사용자들이 같은 키워드로 반복 검색 (예: "치킨", "피자")
- DB 부하 증가: 매번 LIKE 쿼리로 전체 테이블 스캔
- 응답 지연: 복잡한 검색 쿼리로 인한 성능 저하
- 동시 요청: 많은 사용자가 동일한 인기 검색어로 동시 검색

### 캐시 적용 이유

#### 1. 검색 결과 캐싱 (storeSearch)
```
@Cacheable(value = "storeSearch", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
```
- 반복 검색 최적화: "치킨" 검색 시 5분간 DB 조회 없이 즉시 응답
- 페이지네이션 고려: 키워드 + 페이지 정보로 세밀한 캐시 키 설정
- DB 부하 감소: LIKE 쿼리 실행 횟수 대폭 감소

#### 2. 인기 검색어 캐싱(popularKeywords)
```
@Cacheable(value = "popularKeywords")
```
- 자주 조회되는 데이터: 인기 검색어는 모든 사용자가 확인하는 공통 데이터
- 집계 쿼리 최적화: COUNT 기반 정렬 쿼리는 비용이 높음
- 실시간성 vs 성능: 30분 주기 업데이트로 충분한 실시간성 확보

#### 3. API 버전 분리

- v1 API: 캐시 미적용 (비교 및 테스트용)
- v2 API: 캐시 적용 (실제 서비스용)

### 성능 비교 분석
![](https://velog.velcdn.com/images/todok0317/post/5c621b66-a4d1-4870-a5ee-f08c1ae028c6/image.png)
![](https://velog.velcdn.com/images/todok0317/post/ef8fa490-1d79-4e1e-9efb-e2310cff9b6f/image.png)

- v1 검색 (캐시 미사용)
응답 시간: 107ms
데이터 크기: 2.14 KB
결과: 가게 ID 46, 가게명 "고향떡볶이집 7호점"

- v2 검색 (캐시 사용)
응답 시간: 75ms
데이터 크기: 2.15 KB
결과: 가게 ID 16, 가게명 "지인떡볶이집 13호점"

#### 성능 향상 결과
캐시를 사용한 v2가 약 30% 더 빠른 성능을 보여주고 있습니다:
- 시간 단축: 107ms → 75ms (32ms 감소)
- 성능 향상률: 약 29.9%





