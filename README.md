# 팀: 9조
## 팀원
### FE _ Flutter
- 류수정
- 현소영
### BE _ Spring Boot
- 정보운
- 정예원
- 박시연
<br/>

## 제출물
- 소스코드 및 실행 파일
<br/>

## 1. 설계
### 1-1. 전체 Class Diagram
![클래스 다이어그램](https://github.com/user-attachments/assets/fdface1d-6f1f-44e0-be87-87c55cf0f67d)
<br/>
### 1-2. 프로젝트 생성 시 Sequence Diagram
![3 프로젝트생성](https://github.com/user-attachments/assets/520dc9bc-a3df-4856-a083-f4cf15b5202c)
<br/>
### 1-3. Usecase Diagram
![유스케이스 다이어그램](https://github.com/user-attachments/assets/05bb0cc9-6e97-4304-9135-61f0051d6ddf)
<br/>
<br/>

## 2. 프로그램 사용방법
### 2-1. Web
1. Visual Studio Code에서 Flutter를 실행하고 IST 폴더 안의 se_frontend 폴더를 연다.<br/>
2. lib 폴더 안의 main 파일을 Chrome으로 실행한다. (오류 발생 시 터미널에 “flutter pub get” 입력)<br/>
3. MySQL을 실행하여 se_schema 데이터베이스를 생성하고, "user_se"라는 유저를 "1234"라는 비밀번호로 생성한다. 이후 se_schema 데이터베이스에 "/DB table 정보"에 첨부된 테이블을 생성한다.<br/>
4. IntelliJ를 실행하고 IST 폴더 안의 back-end 폴더를 연다. Setting에서 Gradle JVM이 Java 21인지 확인하고, Project Structure에서 SDK가 Java 21인지 확인한다.<br/>
5. 프론트에서 실행된 크롬의 주소를 복사한다 (예: http://localhost:00000/).<br/>
6. IntelliJ에서 back-end 폴더 내 src/main/java/utils 내의 issueTrackingApplication 파일을 열고, 복사한 호스트 주소를 입력한 후 재실행한다.<br/>
7. 프론트에서 번개 모양의 save and hot reload 버튼을 클릭한다.<br/>

### 2-1-1. Web 실행화면
![image](https://github.com/user-attachments/assets/393c3b05-1e10-4ed5-aac3-74cb65dc7fe4)
![image](https://github.com/user-attachments/assets/de133291-7be2-4805-ba00-88cf848a04c5)
![image](https://github.com/user-attachments/assets/d0d61763-d6c3-4783-92a7-226fc390e567)
![image](https://github.com/user-attachments/assets/2564dfdf-577b-4a08-bb4f-7c7a8d4fb1b3)
![image](https://github.com/user-attachments/assets/f0a2a726-de75-476e-95ff-cd811524b091)
<br/>

### 2-2. Console
1. Java 콘솔 프로그램은 HTTP 클라이언트를 사용하여 Spring Boot 애플리케이션에서 제공하는 RESTful API 엔드포인트에 요청을 보낸다.<br/>
2. Spring Boot 애플리케이션 설정 - 애플리케이션을 설정하고 엔드포인트를 통해 프로젝트 정보를 제공한다.<br/>
3. Java 콘솔 프로그램 작성 - Java 콘솔 프로그램을 작성한다.<br/>
4. 콘솔 프로그램 실행 - Spring Boot 애플리케이션이 로컬에서 실행 중일 때, 콘솔 프로그램이 해당 애플리케이션의 엔드포인트에 요청을 보내고 응답을 처리한다.<br/>
<br/>

## 3. 파일 목록
- 9조_발표_슬라이드.pptx (PPT 파일)<br/>
- 9조_프로젝트_문서.pdf (보고서 파일)<br/>
- IST.zip, se_schema.zip (모든 소스코드 파일)<br/>
<br/>

## 4. GitHub 주소
[SE-9/IST](https://github.com/SE-9/IST)
<br/>

## 5. 시연 영상 Youtube 링크
https://youtu.be/K-WY0s3xwYg


