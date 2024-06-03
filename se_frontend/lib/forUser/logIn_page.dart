import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:se_frontend/forUser/signUp_page.dart';
import 'package:http/http.dart' as http;
import 'package:se_frontend/myDashBoard.dart';

class LoginPage extends StatelessWidget {
  LoginPage({super.key});
  final TextEditingController _userNameController =
      TextEditingController(); //사용자 닉네임 입력 컨트롤러
  final TextEditingController _passwordController =
      TextEditingController(); //사용자 비번 입력 컨트롤러

  void loginUser(BuildContext context) async {
    String nickname = _userNameController.text; //nickname 에 입력된 닉네임 가져옴
    String password = _passwordController.text; //password 변수에 입력된 비번 가져옴

    // 로그인 요청을 보낼 URL
    Uri url = Uri.parse('http://localhost:8081/user/login');

    // 요청 본문 (MAP)
    Map<String, dynamic> requestBody = {
      "nickname": nickname, //입력된 닉네임 전달
      "password": password, //입력된 비번 전달
    };

    try {
      //예외처리문
      final response = await http.post(
        url,
        body: jsonEncode(requestBody),
        headers: {'Content-Type': 'application/json'},
      );

      if (response.statusCode == 200) {
        //성공하면
        final responseBody = jsonDecode(response.body); //응답 온거 JSON 으로 디코딩

        if (responseBody != null && responseBody['user_id'] != null) {
          // 응답이 null 이 아니고, user id 존재하면
          int userId = int.parse(
              responseBody['user_id']); // user_id 추출 (백에서 확인된 유저 닉네임저장한곳)

          // 로그인 성공 시
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(
              builder: (context) =>
                  MyDashboard(userId: userId), //대시보드 이동할때 유저 닉네임 전달
            ),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('본문에 nickname 존재하지 않음')),
          );
        }
      } else {
        // 로그인 실패
        print(
            'Error: ${response.statusCode} ${response.reasonPhrase}'); //오류 상태코드, 이유를 콘솔에 출력시킴
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('로그인에 실패했습니다.')),
        );
      }
    } catch (e) {
      // 요청 실패
      print('Error during login: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('로그인에 오류가 발생했습니다. 다시 시도해주세요.')),
      );
    }
  }

//****************************************** UI 부분 ****************************************************** */
  @override
  Widget build(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width; // 넓이
    double screenHeight = MediaQuery.of(context).size.height; // 높이 가져옴

    // 화면 크기에 따라 폰트 크기와 패딩을 동적으로 설정
    double fontSize = screenWidth < 850 ? 18 : 18;
    double paddingSize = screenWidth < 850 ? 20 : 50;

    double formFieldWidth =
        screenWidth < 800 ? screenWidth * 0.8 : screenWidth * 0.3;

    return Scaffold(
      appBar: AppBar(
        title: const Row(
          children: [
            Text(
              '          RYU SOOJUNG         PARK SIYEON          JEONG BOWOON         JEONG YEWON         HYUN SOYOUNG',
              style: TextStyle(
                color: Color.fromARGB(255, 94, 94, 94),
                fontSize: 10,
              ),
            ),
            Spacer(), // 로고 오른쪽 이동
          ],
        ),
        backgroundColor: const Color.fromARGB(255, 255, 255, 255),
        toolbarHeight: 70,
        titleSpacing: 0,
      ), // 앱바 *************************************************
      backgroundColor: const Color(0xFFF1F1F1),
      body: SingleChildScrollView(
        // SingleChildScrollView 추가
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  SizedBox(
                    width: formFieldWidth, // 폼 필드 동적 조절
                    child: Container(
                      padding: EdgeInsets.all(paddingSize), // 패딩 동적 조절
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(20), // 박스 각도 추가
                      ),

                      // height: screenHeight * 0.7, //박스크기
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Text(
                            "Log In ITS",
                            style: TextStyle(
                                fontSize: fontSize * 1.4,
                                color: Colors.black,
                                fontWeight: FontWeight.bold),
                          ),
                          // 유저이름 *********************************
                          SizedBox(height: screenHeight * 0.06),
                          TextFormField(
                            controller: _userNameController,
                            decoration: const InputDecoration(
                              labelText: 'User Name',
                            ),
                            obscureText: false,
                          ),
                          // 비번 *************************************
                          SizedBox(height: screenHeight * 0.04),
                          TextFormField(
                            controller: _passwordController,
                            decoration:
                                const InputDecoration(labelText: 'Password'),
                            obscureText: true, // 비번 가리기
                          ),
                          // 로그인 버튼 *******************************
                          SizedBox(height: screenHeight * 0.07),
                          SizedBox(
                            width: formFieldWidth,
                            child: ElevatedButton(
                              onPressed: () {
                                loginUser(context);
                              },
                              style: ElevatedButton.styleFrom(
                                backgroundColor:
                                    const Color.fromARGB(255, 255, 156, 204),
                                fixedSize: const Size.fromHeight(50),
                              ),
                              child: Text(
                                'Login',
                                style: TextStyle(
                                    color: Colors.white,
                                    fontSize: fontSize,
                                    fontWeight: FontWeight.w200),
                              ),
                            ),
                          ),

                          // or 사진 *************************************
                          SizedBox(height: screenHeight * 0.02),
                          Image.asset(
                            'assets/or.png',
                            width: 300,
                            height: 40,
                          ),

                          // read policy***************************************
                          SizedBox(height: screenHeight * 0.02),
                          TextButton(
                              onPressed: () {},
                              style: ButtonStyle(
                                overlayColor:
                                    WidgetStateProperty.all(Colors.transparent),
                              ),
                              child: const Text(
                                "Click here to read ITS's policy",
                                style: TextStyle(
                                  decoration: TextDecoration.underline,
                                  color: Colors.black,
                                  fontWeight: FontWeight.w100,
                                ),
                              )),

                          // 회원가입 버튼 ******************************
                          SizedBox(height: screenHeight * 0.03),
                          SizedBox(
                            width: formFieldWidth,
                            child: OutlinedButton(
                              onPressed: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) => SignUpPage(),
                                  ),
                                );
                              },
                              style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.white,
                                  fixedSize: const Size.fromHeight(50),
                                  side: const BorderSide(
                                      color: Colors.black, width: 0.1),
                                  shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30))),
                              child: Text(
                                "don't have account? Sign Up",
                                style: TextStyle(
                                  color: const Color.fromARGB(255, 0, 0, 0),
                                  fontSize: fontSize * 0.7,
                                  fontWeight: FontWeight.w200,
                                  // decoration: TextDecoration.underline,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ), // 컨테이너 높이 동적 조절
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: screenHeight * 0.9), // 이미지 상단 간격 조정
            MediaQuery.of(context).size.width >= 850
                ? Expanded(
                    child: Center(
                      child: Visibility(
                        visible: MediaQuery.of(context).size.width >= 400,
                        child: Image.asset(
                          'assets/loginPic.png',
                          width: 650,
                          height: 650,
                        ),
                      ),
                    ),
                  )
                : const SizedBox(),
          ],
        ),
      ), // SingleChildScrollView 닫기
    );
  }
}
