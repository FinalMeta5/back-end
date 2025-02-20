# 🚖 HIFIVE - 택시 및 차량 공유 플랫폼
![LOGO](https://github.com/user-attachments/assets/ccbbdba7-5d66-4160-8676-318f41c1c039)

**HIFIVE**는 택시 및 차량 공유를 통해 **비용 절감과 편리한 이동을 제공하는 매칭 예약 서비스**입니다.  
실시간 위치 기반 검색과 안전한 인증 시스템을 갖춘 플랫폼입니다.

## 🎥 시연 영상
🔗 [부르릉 시연 영상 보러가기](https://youtu.be/sqpIHMiNvq8)
---

## 📌 프로젝트 개요
- **프로젝트명:** HIFIVE (하이파이브)  
- **개발 기간:** 2024.02.03 ~ 2024.02.18  
- **팀원 구성:** 2조  HIFIVE (하이파이브) 5명
- **주요 기능:**  
  - 🚕 **택시 및 차량 공유 서비스**  
  - 📍 **실시간 위치 기반 검색**  
  - 🔒 **안전한 사용자 인증 시스템** 
  - 💳 **크레딧 기반 결제 시스템**
  - 💳 **리워드 시스템** (추후 개선)
  - 💬 **실시간 채팅 및 후기 시스템**  (추후 개선)

---

## ⚙️ 기술 스택
![STACK](https://github.com/user-attachments/assets/206ad0ea-7d10-475a-a7ba-4998a7f4a012)

### 🔹 **Frontend**
- `Vue 3` + `Vite`
- `Vue Router`
- `Axios` / `Chart.js`
- `Bootstrap 5`

### 🔹 **Backend**
- `Spring Boot 3.4.1` / `Java 17`
- `Spring Security` + `JWT`
- `JPA (Hibernate)` + `MyBatis`
- `Redis` / `AWS S3`
- `Swagger` / `JUnit`

### 🔹 **Database**
- `Oracle 11g`
- `ERDCloud`를 활용한 데이터 모델링

### 🔹 **DevOps & 협업 도구**
- `GitHub Actions` (CI/CD 자동화)
- `Docker` + `Nginx` + `AWS EC2`
- `Notion` / `Slack` / `Figma` (UI/UX 설계)

---

## 📌 주요 기능 소개

### 🚗 **차량 공유 서비스**
- **차량 동승자 모집**
- **카카오 지도 API**를 활용한 출발지/도착지 설정
- **실시간 위치 기반 차량 검색 및 예약**
- **JWT 인증을 통한 보안 강화**
- **차량 등록 시 유효성 검사 및 중복 방지** : 과거 시점 운행 일시 기입 금지 확인
- **참가자 리뷰 및 별점 시스템 적용**

### 🔄 **차량 정보 관리**
- 차량 등록 / 수정 / 삭제 기능 제공
- **VERIFIED 상태값으로 인증된 차량만 서비스 등록 가능**
- 차량 삭제 시 **참여자 없는 경우만 삭제 가능**

### 🚖 **택시 공유 서비스**
- **택시 동승자 모집**
- **카카오 지도 API**를 활용한 출발지/도착지 설정
- **실시간 위치 기반 차량 검색 및 예약**
- **JWT 인증을 통한 보안 강화**
- **택시 공유 서비스 등록 시 유효성 검사 및 중복 방지**

### 📢 **실시간 알림 시스템**
- **WebSocket & STOMP 프로토콜**을 활용한 실시간 알림 기능
- **Redis Pub/Sub**을 사용한 다중 서버 환경에서도 안정적인 메시징 지원

### 💳 **결제 시스템**
- **크레딧 기반 결제 기능**
- **탑승 인원 기준 크레딧 지급 예정**
- **크레딧을 활용한 리워드 시스템 추가 예정**

### ⚙️ **관리자 시스템**
- **회원 관리**
- **공유 서비스 관리**
- **결제 내역 조회 및 엑셀 파일 추출 기능**
- **플랫폼 내 다양한 통계** : 남녀 비율, 신규 가입자 수, 연령대 비율, 회원 별점 순으로 확인
- **차량 등록 신청 승인 및 반려**

---

## 📐 **ERD 및 데이터 구조**
> 📌 `ERDCloud`를 활용하여 **총 11개 테이블, 106개 컬럼**으로 설계  

![ERD](https://github.com/user-attachments/assets/5069ee8b-8afb-4556-9117-ff078b0e8953)

- `MEMBER` - 회원 관리 테이블  
- `CAR_REGISTRATION` - 차량 등록 정보  
- `CAR_SHARE_REGISTRATION` -  차량 공유 등록
- `CAR_SHARE_JOIN` - 차량 공유 참가
- `TAXI_SHARE` - 택시 공유
- `TAXI_SHARE_JOIN` - 택시 공유 참가
- `PAYMENT` - 크레딧 결제
- `CREDIT` - 크레딧
- `MEMBER_CREDIT ` - 회원 크레딧 내역
- `NOTIFICATION` - 실시간 알림 기록
- `REVIEW` - 리뷰

---

## 🔥 **시스템 아키텍처**
> Spring Boot 기반의 REST API 및 비동기 데이터 처리
<img width="481" alt="Image" src="https://github.com/user-attachments/assets/87d43d1d-1e1c-4f25-b43c-f62b229fb08d" />
