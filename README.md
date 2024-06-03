# 팀: 9조
## 팀원
- 류수정
- 박시연
- 정보운
- 정예원
- 현소영

## 제출물
- 소스코드 및 실행 파일

## 프로그램 사용방법

### 2-1. Web
a. Visual Studio Code에서 Flutter를 실행하고 IST 폴더 안의 se_frontend 폴더를 연다.
b. lib 폴더 안의 main 파일을 Chrome으로 실행한다. (오류 발생 시 터미널에 “flutter pub get” 입력)
c. MySQL을 실행하여 se_schema 데이터베이스를 생성하고, "user_se"라는 유저를 "1234"라는 비밀번호로 생성한다. 이후 se_schema 데이터베이스에 "/DB table 정보"에 첨부된 테이블을 생성한다.
d. IntelliJ를 실행하고 IST 폴더 안의 back-end 폴더를 연다. Setting에서 Gradle JVM이 Java 21인지 확인하고, Project Structure에서 SDK가 Java 21인지 확인한다.
e. 프론트에서 실행된 크롬의 주소를 복사한다 (예: http://localhost:00000/).
f. IntelliJ에서 back-end 폴더 내 src/main/java/utils 내의 issueTrackingApplication 파일을 열고, 복사한 호스트 주소를 입력한 후 재실행한다.
g. 프론트에서 번개 모양의 save and hot reload 버튼을 클릭한다.

### 2-2. Console
a. Java 콘솔 프로그램은 HTTP 클라이언트를 사용하여 Spring Boot 애플리케이션에서 제공하는 RESTful API 엔드포인트에 요청을 보낸다.
b. Spring Boot 애플리케이션 설정 - 애플리케이션을 설정하고 엔드포인트를 통해 프로젝트 정보를 제공한다.
c. Java 콘솔 프로그램 작성 - Java 콘솔 프로그램을 작성한다.
d. 콘솔 프로그램 실행 - Spring Boot 애플리케이션이 로컬에서 실행 중일 때, 콘솔 프로그램이 해당 애플리케이션의 엔드포인트에 요청을 보내고 응답을 처리한다.

## 파일 목록
- 9조_발표_슬라이드.pptx (PPT 파일)
- 9조_프로젝트_문서.pdf (보고서 파일)
- IST.zip, se_schema.zip (모든 소스코드 파일)

## GitHub 주소
[SE-9/IST](https://github.com/SE-9/IST)

## 시연 영상 Youtube 링크
https://youtu.be/K-WY0s3xwYg


