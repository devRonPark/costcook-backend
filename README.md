# 코스트쿡 백엔드 API

## 📌 프로젝트 개요
**코스트쿡**은 **예산 기반 맞춤형 레시피 추천 서비스**입니다.  
사용자가 **주간 식비 예산을 설정**하면 해당 예산에 맞는 레시피를 추천받을 수 있으며, 이를 바탕으로 효과적으로 식단을 계획할 수 있습니다.

이 저장소는 **코스트쿡의 백엔드 API 저장소**로, 프론트엔드 애플리케이션과 통신하여 서비스의 주요 기능을 제공합니다.

## 🚀 주요 기능
- **사용자 인증**: OAuth 2.0을 활용한 카카오 및 구글 소셜 로그인 지원  
- **예산 맞춤 레시피 추천**: 사용자의 주간 예산을 기반으로 최적의 레시피 목록 제공  
- **레시피 리뷰 제공**: 레시피별 리뷰 및 평점 기능 제공  
- **예산 관리**: 예산 설정 및 소비 내역 추적 가능  
- **즐겨찾기 관리**: 레시피 즐겨찾기 및 비회원 즐겨찾기 동기화 지원  

## 🛠️ 기술 스택
### **Backend**
- **Framework**: Spring Boot  
- **Security**: Spring Security, OAuth 2.0, JWT  
- **Database**: MySQL, Spring Data JPA  
- **Libraries**:
  - **Authentication**: Spring Security, JWT (io.jsonwebtoken)  
  - **Web & API**: Spring Web  
  - **Email**: Spring Boot Mail  
  - **Crawling**: Jsoup  
  - **Utility**: Lombok  

### **Infrastructure**
- **Containerization**: Docker  
- **CI/CD**: GitHub Actions  
- **Hosting**: AWS (EC2) 

## 📂 프로젝트 구조

## 📌 주요 이슈 및 해결
### 1. 복합키를 가진 `Favorite` 엔티티의 즐겨찾기 조회 문제
- **문제**: `Favorite` 엔티티에서 사용자 ID와 레시피 ID를 복합키로 사용하면서, 특정 사용자의 즐겨찾기 목록을 조회하는 과정에서 JPQL 쿼리 작성이 까다로웠음.
- **해결**:  
  - `@EmbeddedId`를 활용하여 복합키(`FavoriteId`)를 설계.  
  - `@Query`를 통해 `fav.id.userId`를 명시적으로 지정하여 JPQL에서 복합키를 활용한 조회 구현.  
  - 페이징을 적용하여 한 번에 많은 데이터를 불러오는 문제 해결.  
- **상세 내용**: [복합키 엔티티 조회 문제 해결 (Notion)](### 1. 복합키를 가진 `Favorite` 엔티티의 즐겨찾기 조회 문제
- **문제**: `Favorite` 엔티티에서 사용자 ID와 레시피 ID를 복합키로 사용하면서, 특정 사용자의 즐겨찾기 목록을 조회하는 과정에서 JPQL 쿼리 작성이 까다로웠음.
- **해결**:  
  - `@EmbeddedId`를 활용하여 복합키(`FavoriteId`)를 설계.  
  - `@Query`를 통해 `fav.id.userId`를 명시적으로 지정하여 JPQL에서 복합키를 활용한 조회 구현.  
  - 페이징을 적용하여 한 번에 많은 데이터를 불러오는 문제 해결.  
- **상세 내용**: [복합키 엔티티 조회 문제 해결 (Notion)](https://www.notion.so/JPA-Favorite-1314b7e536fc80248400de2f240a02a9?pvs=4))
