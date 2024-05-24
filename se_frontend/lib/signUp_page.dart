import 'package:flutter/material.dart';
import 'package:se_frontend/login_page.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

// ignore: must_be_immutable
class SignUpPage extends StatelessWidget {
  //stateLessWidegt 확장
  SignUpPage({super.key});

  //컨트롤러, 변수 선언
  final TextEditingController _userNameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  String _selectedRole = '';

  // Sign Up 누르면 실행
  void signUp(BuildContext context) async {
    String userName = _userNameController.text; //사용자 입력사항 컨트롤러변수에 저장
    String password = _passwordController.text;

    if (userName.isNotEmpty && password.isNotEmpty && _selectedRole != '') {
      // 서버 요청 대기
      Uri url = Uri.parse('http://127.0.0.1:8000/primary_disease_prediction/');

      //요청 본문 JSON 형식으로
      Map<String, dynamic> requestBody = {
        'userName': userName,
        'password': password,
        'gender': _selectedRole,
      };

      try {
        //서버에 post 요청함
        final response = await http.post(
          url,
          body: jsonEncode(requestBody),
          headers: {'Content-Type': 'application/json'},
        );
        //회원가입 성공하면 로그인 페이지 이동
        if (response.statusCode == 200) {
          Navigator.pushReplacement(
            //화면 전환
            context, //이전 화면의 context전달해서 화면 교체
            MaterialPageRoute(builder: (context) => LoginPage()), //새로운 빌더함수 전달
          );
        } else {
          //회원가입 실패
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('회원가입에 실패했습니다.')),
          );
        }
      } catch (e) {
        print('Error during sign up: $e');
        if (e.toString().contains('Duplicate entry')) {
          // 이미 사용 중인 사용자 이름인 경우 에러 메시지 표시
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('이미 사용 중인 사용자 이름입니다. 다른 이름을 선택해주세요.')),
          );
        }
      }
    } else {
      //필수 정보 입력 안했을때
      ScaffoldMessenger.of(context)
          .showSnackBar(const SnackBar(content: Text('모든 필수 정보를 입력해주세요.')));
    }
  }

  @override
  Widget build(BuildContext context) {
    //현재 화면 넓이 높이 가져옴
    double screenWidth = MediaQuery.of(context).size.width;
    double screenHeight = MediaQuery.of(context).size.height;

    // 화면 크기에 따라 폰트 크기와 패딩을 동적으로 설정
    double fontSize = screenWidth < 850 ? 12 : 18;
    double paddingSize = screenWidth < 850 ? 13 : 50;
    double formFieldWidth =
        screenWidth < 700 ? screenWidth * 0.8 : screenWidth * 0.3;

    return Scaffold(
      appBar: AppBar(
        // 앱바 설정
        title: const Row(
          children: [
            // 앱바에 표시될 요소들

            SizedBox(width: 10),
            Text(
              '          RYU SOOJUNG         PARK SIYEON          JEONG BOWOON         JEONG YEWON         HYUN SOYOUNG',
              style: TextStyle(
                color: Color.fromARGB(255, 94, 94, 94),
                fontSize: 10,
              ),
            ),
            Spacer(),
          ],
        ),
        backgroundColor: const Color.fromARGB(255, 255, 255, 255),
        toolbarHeight: 70,
        titleSpacing: 0,
      ), // 앱바 *************************************************
      backgroundColor: const Color.fromARGB(255, 255, 255, 255),
      body: SingleChildScrollView(
        // SingleChildScrollView 추가
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center, //가운데 정렬
          crossAxisAlignment: CrossAxisAlignment.center, //세로 가운데 정렬
          children: [
            Expanded(
              // 화면 나머지 공간 차지
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  SizedBox(
                    //width: formFieldWidth, // 폼 필드 동적 조절
                    child: Container(
                      padding: EdgeInsets.only(
                        top: paddingSize * 2,
                        left: paddingSize * 3.5,
                        bottom: paddingSize * 3,
                        right: paddingSize * 3,
                      ), // 패딩 동적 조절
                      decoration: const BoxDecoration(
                        color: Colors.white,
                      ),
                      height: screenHeight, //* 0.7, //박스크기
                      //***************************************************
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "SignUp IST",
                            style: TextStyle(
                                fontSize: fontSize * 1.5, color: Colors.black),
                          ),
                          //*******************************************
                          Text(
                            "Sign Up ITS free to access",
                            style: TextStyle(
                                fontSize: fontSize * 0.7,
                                color: Colors.grey,
                                fontWeight: FontWeight.w100),
                          ),
                          //유저이름 *********************************
                          SizedBox(height: screenHeight * 0.04),
                          TextFormField(
                            controller: _userNameController, // 컨트롤러 설정
                            decoration: const InputDecoration(
                              labelText: 'User Name',
                            ),
                            obscureText: false,
                          ),

                          //비번 *************************************
                          SizedBox(height: screenHeight * 0.04),
                          TextFormField(
                            controller: _passwordController, // 컨트롤러 설정
                            decoration:
                                const InputDecoration(labelText: 'Password'),
                            obscureText: true, // 비번 가리기
                          ),

                          //롤***********************************
                          SizedBox(height: screenHeight * 0.04),
                          // 사용자가 선택한 성별을 저장하는 변수
                          DropdownButtonFormField<String>(
                            value: _selectedRole, // 사용자가 선택한 값
                            onChanged: (String? newValue) {
                              // 새로운 값 선택할때마다 호출
                              _selectedRole = newValue!; // 사용자가 선택한 롤 변수에 저장
                            },
                            items: <String>['', '관리자', '일반 사용자'] // 기본값 추가
                                .map<DropdownMenuItem<String>>((String value) {
                              return DropdownMenuItem<String>(
                                value: value,
                                child: Text(
                                  value,
                                  style: TextStyle(fontSize: fontSize * 0.7),
                                ),
                              );
                            }).toList(),
                            decoration: const InputDecoration(
                              labelText: 'ROLE',
                            ),
                          ),

                          // 회원가입 버튼 *******************************
                          SizedBox(height: screenHeight * 0.04),
                          SizedBox(
                            width: formFieldWidth * 0.6,
                            child: ElevatedButton(
                              onPressed: () => signUp(context), // signUp 함수 호출
                              style: ElevatedButton.styleFrom(
                                backgroundColor:
                                    const Color.fromARGB(255, 255, 156, 204),
                                fixedSize: const Size.fromHeight(50),
                              ),
                              child: Text(
                                'Sign Up',
                                style: TextStyle(
                                    color: Colors.white,
                                    fontSize: fontSize,
                                    fontWeight: FontWeight.w200),
                              ),
                            ),
                          ),

                          //read policy***************************************
                          SizedBox(height: screenHeight * 0.05),
                          TextButton(
                              onPressed: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) => LoginPage(),
                                  ),
                                );
                              },
                              style: ButtonStyle(
                                overlayColor: MaterialStateProperty.all(
                                    Colors.transparent),
                              ),
                              child: const Text(
                                "Already have an account? Login",
                                style: TextStyle(
                                  decoration: TextDecoration.underline,
                                  color: Colors.black,
                                  fontWeight: FontWeight.w100,
                                ),
                              )),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
            MediaQuery.of(context).size.width >= 1100
                ? Expanded(
                    child: Container(
                      padding: EdgeInsets.only(
                        top: paddingSize * 1.5,
                        //left: paddingSize * 3.5,
                        bottom: paddingSize * 3,
                        right: paddingSize * 1,
                      ),
                      child: Visibility(
                          visible: MediaQuery.of(context).size.width >= 00,
                          child: Image.asset(
                            'assets/signUp.png',
                            width: 500,
                            height: 600,
                          )),
                    ),
                  )
                : const SizedBox(),
          ],
        ),
      ), // SingleChildScrollView 닫기
    );
  }
}
