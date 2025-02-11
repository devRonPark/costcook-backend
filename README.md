# 코스트쿡 백엔드 API

## 📌 프로젝트 개요
**코스트쿡**은 **예산 기반 맞춤형 레시피 추천 서비스**입니다.  
사용자가 **주간 식비 예산을 설정**하면 해당 예산에 맞는 레시피를 추천받을 수 있으며, 이를 바탕으로 효과적으로 식단을 계획할 수 있습니다.

이 저장소는 **코스트쿡의 백엔드 API 저장소**로, 프론트엔드 애플리케이션과 통신하여 서비스의 주요 기능을 제공합니다.


📄 **발표 자료(PPT)**: [코스트쿡 프로젝트 발표 자료](https://github.com/user-attachments/files/18744666/costcook_ppt.pdf)

## 🚀 주요 기능
- **사용자 인증**: OAuth 2.0을 활용한 카카오 및 구글 소셜 로그인 지원  
- **예산 맞춤 레시피 추천**: 사용자의 주간 예산을 기반으로 최적의 레시피 목록 제공  
- **레시피 리뷰 제공**: 레시피별 리뷰 및 평점 기능 제공  
- **예산 관리**: 예산 설정 및 소비 내역 추적 가능  
- **즐겨찾기 관리**: 레시피 즐겨찾기 및 비회원 즐겨찾기 동기화 지원  

## 🛠️ 기술 스택
### **Backend**
- **Framework**: Spring Boot  
- **Security & Auth**: Spring Security, OAuth 2.0, JWT  
- **Database**: MySQL, Spring Data JPA  
- **Utility Libraries**:
  - **Email**: Spring Boot Mail  
  - **Crawling**: Jsoup  
  - **Code Simplification**: Lombok  

### **Infrastructure**
- **Containerization**: Docker  
- **CI/CD**: GitHub Actions  
- **Hosting**: AWS (EC2), Nginx 

---

## 📂 프로젝트 구조  
```plaintext
📦 costcook-backend
 ┣ 📂 src
 ┃ ┣ 📂 main
 ┃ ┃ ┣ 📂 java/com/costcook
 ┃ ┃ ┃ ┣ 📂 config        # 애플리케이션 전반적인 설정(Spring Security, CORS)
 ┃ ┃ ┃ ┣ 📂 controller    # 클라이언트 요청을 처리하는 API 컨트롤러
 ┃ ┃ ┃ ┣ 📂 domain        # 핵심 도메인 로직 및 서비스 계층에서 사용하는 DTO 정의
 ┃ ┃ ┃ ┣ 📂 entity        # JPA 엔티티 정의 (데이터베이스 테이블 매핑)
 ┃ ┃ ┃ ┣ 📂 exceptions    # 커스텀 예외 클래스 및 전역 예외 처리 핸들러
 ┃ ┃ ┃ ┣ 📂 repository    # JPA를 활용한 데이터 액세스 계층 (Spring Data JPA 인터페이스)
 ┃ ┃ ┃ ┣ 📂 security      # 인증 및 인가 관련 로직 (JWT, OAuth2 인증 등)
 ┃ ┃ ┃ ┣ 📂 service       # 비즈니스 로직을 처리하는 서비스 계층
 ┃ ┃ ┃ ┗ 📂 util          # 공통적으로 사용되는 유틸리티 클래스 (파일 처리, 데이터 변환 등)
 ┗ 📂 test                  # 단위 테스트 및 통합 테스트 코드
```

## 🗂 ERD 및 백엔드 아키텍처  

### 👤 사용자 관련 ERD  
사용자 정보 및 인증과 관련된 ERD입니다.  

![사용자 관련 ERD](https://github.com/user-attachments/assets/6152a9d0-594c-4cb3-a8e5-7c0821c01fb0)

### 🍽 레시피 관련 ERD  
레시피 및 재료 관리와 관련된 ERD입니다.  

![레시피 관련 ERD](https://github.com/user-attachments/assets/af3374e1-654d-404d-90fe-4501ba3a6e8d)

### 🏗 백엔드 아키텍처  
서비스 전반의 백엔드 아키텍처 구성도입니다.

![백엔드 아키텍처](https://github.com/user-attachments/assets/628821d4-411e-462a-833a-8ff7560da5ba)


## 👥 팀원 소개

| 이름 | 역할 | 주요 담당 업무 |
|------|------|----------------------------------------------------------------|
| **박병찬** | 팀장, 백엔드 | 프로젝트 관리, ERD 설계, API 설계 및 명세서 문서화, 소셜 로그인 API 개발, 이메일 인증 및 인증번호 처리 API 개발, 사용자 정보 조회 및 수정 API 개발, 소셜 로그인 페이지, 이메일 인증 페이지, 마이 페이지 개발 |
| **권지훈** | 백엔드 | 관리자 회원가입 및 로그인 API 개발, 관리자 레시피 등록/수정/삭제 API 개발, 관리자 재료 등록/수정/삭제 API 개발, 관리자 리뷰 비공개 처리 API 개발, 관리자 대시보드 페이지 개발 |
| **유현준** | 백엔드, 프론트엔드 | 레시피 목록 조회, 검색, 상세 정보 조회 API 개발, 레시피 목록 및 상세정보 페이지 개발, 예산관리 페이지 개발, 주간 예산 사용 내역 상세 페이지 개발 |
| **한민혁** | 프론트엔드 | 프론트엔드 디자인, 레시피 데이터 가공, 사용자 리뷰 등록/수정/삭제 API 개발, 사용자 예산 설정/수정 API 개발, 레시피 추천 페이지 개발 |


## 🔥 주요 이슈 및 해결  

### 1. 복합키(Favorite 엔티티) 활용 및 조회 문제  
**이슈**  
- `Favorite` 엔티티에서 사용자 ID와 레시피 ID를 복합키로 사용하여 즐겨찾기 데이터를 관리하는 과정에서 조회 쿼리 처리 및 JPA 매핑 문제 발생.  

**해결**  
- `FavoriteId`를 `@Embeddable`로 정의하고 `@EmbeddedId`를 사용하여 매핑.  
- `@MapsId`를 활용하여 `User`, `Recipe`와의 관계를 설정.  
- 복합키 기반의 JPQL 쿼리를 작성하고, 페이지네이션을 적용하여 효율적으로 조회하도록 개선.  

🔗 **자세한 해결 방법:** [Notion 링크](https://www.notion.so/JPA-Favorite-1314b7e536fc80248400de2f240a02a9?pvs=4)  

---  

### 2. Docker 환경에서 Spring Boot 애플리케이션의 이미지 파일 접근 문제  
**이슈**  
- Spring Boot 애플리케이션에서 외부 이미지를 불러오는 과정에서 Docker 컨테이너 내부에서 파일 접근이 불가능한 문제 발생.  
- 컨테이너 내부 경로와 호스트 시스템 경로가 매핑되지 않아 `"No static resource found"` 오류 발생.  

**해결**  
- `docker-compose.yml`에서 볼륨 매핑을 설정하여 호스트의 이미지 폴더를 컨테이너 내부에서 접근 가능하도록 변경.  
- `WebMvcConfigurer`에서 `addResourceHandlers()` 메서드를 활용하여 정적 리소스 요청을 매핑.  
- 파일 권한 설정(`chmod 755`)을 통해 컨테이너가 파일을 읽을 수 있도록 조정.  
- 환경 변수를 활용하여 이미지 저장 경로를 동적으로 설정하고 유지보수성을 개선.  

🔗 **자세한 해결 방법:** [Notion 링크](https://www.notion.so/Docker-Spring-Boot-1384b7e536fc804897e2c6c9b70e4c81?pvs=4)  

--

### 3. 로그인 후 비회원 즐겨찾기 데이터 동기화

**이슈**  
비회원이 즐겨찾기에 추가한 레시피 데이터를 유지할 것인가?  
- 사용자가 로그인 후에도 **비회원 상태에서 추가한 즐겨찾기 데이터를 유지할 필요가 있는지** 고민이 필요함.  
- 로그인한 사용자의 **기존 즐겨찾기 목록과 중복될 가능성이 있음**.  
- **과거에 삭제한 즐겨찾기 데이터를 복원할 것인지 여부**를 고려해야 함.  

**해결**  
✔ 로그인 요청 시, **비회원 즐겨찾기 데이터(favoriteRecipeIds)를 함께 전송**하여 서버에서 동기화.  
✔ 이미 존재하는 즐겨찾기 데이터는 **중복 추가를 방지하고, 삭제된 경우 복원**.  
✔ 로그인한 사용자의 **기존 즐겨찾기 데이터와 병합**하여 충돌 없이 유지.  

🔗 **자세한 해결 방법:** [Notion 링크](https://www.notion.so/1106-1364b7e536fc8077a674e0037ce28a7e?pvs=4)  

---

## 📌 프로젝트 회고  

프로젝트를 진행하며 겪은 시행착오와 배운 점을 정리했습니다.  
[➡ 코스트쿡 프로젝트 회고 보기](https://www.notion.so/13a4b7e536fc80d4978af88c49827175?pvs=4)

