import 'package:flutter/material.dart';
import 'package:se_frontend/forUser/logIn_page.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class SignUpPage extends StatelessWidget {
  SignUpPage({super.key});

  final TextEditingController _userNameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  void signUp(BuildContext context) async {
    String nickname = _userNameController.text;
    String password = _passwordController.text;

    if (nickname.isNotEmpty && password.isNotEmpty) {
      Uri url = Uri.parse('http://localhost:8081/user/signup');

      Map<String, dynamic> requestBody = {
        "nickname": nickname,
        "password": password,
      };

      try {
        print('Sending request to $url with body: $requestBody');
        final response = await http.post(
          url,
          body: jsonEncode(requestBody),
          headers: {'Content-Type': 'application/json'},
        );

        print('Response status: ${response.statusCode}');
        print('Response body: ${response.body}');

        if (response.statusCode == 200) {
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (context) => LoginPage()),
          );
        } else {
          String errorMessage = '회원가입에 실패했습니다.';
          if (response.statusCode == 409) {
            errorMessage = '이미 사용 중인 사용자 이름입니다. 다른 이름을 선택해주세요.';
          }
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(errorMessage)),
          );
        }
      } catch (e) {
        print('Error during sign up: $e');
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('회원가입 중 오류가 발생했습니다.')),
        );
      }
    } else {
      ScaffoldMessenger.of(context)
          .showSnackBar(const SnackBar(content: Text('모든 필수 정보를 입력해주세요.')));
    }
  }

  @override
  Widget build(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width;
    double screenHeight = MediaQuery.of(context).size.height;

    double fontSize = screenWidth < 850 ? 12 : 18;
    double paddingSize = screenWidth < 850 ? 13 : 50;
    double formFieldWidth =
        screenWidth < 700 ? screenWidth * 0.8 : screenWidth * 0.3;

    return Scaffold(
      appBar: AppBar(
        title: const Row(
          children: [
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
      ),
      backgroundColor: const Color.fromARGB(255, 255, 255, 255),
      body: SingleChildScrollView(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  SizedBox(
                    child: Container(
                      padding: EdgeInsets.only(
                        top: paddingSize * 2,
                        left: paddingSize * 3.5,
                        bottom: paddingSize * 3,
                        right: paddingSize * 3,
                      ),
                      decoration: const BoxDecoration(
                        color: Colors.white,
                      ),
                      height: screenHeight,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "SignUp IST",
                            style: TextStyle(
                                fontSize: fontSize * 1.5, color: Colors.black),
                          ),
                          Text(
                            "Sign Up ITS free to access",
                            style: TextStyle(
                                fontSize: fontSize * 0.7,
                                color: Colors.grey,
                                fontWeight: FontWeight.w100),
                          ),
                          SizedBox(height: screenHeight * 0.04),
                          TextFormField(
                            controller: _userNameController,
                            decoration: const InputDecoration(
                              labelText: 'User Name',
                            ),
                            obscureText: false,
                          ),
                          SizedBox(height: screenHeight * 0.04),
                          TextFormField(
                            controller: _passwordController,
                            decoration:
                                const InputDecoration(labelText: 'Password'),
                            obscureText: true,
                          ),
                          SizedBox(height: screenHeight * 0.04),
                          SizedBox(
                            width: formFieldWidth * 0.6,
                            child: ElevatedButton(
                              onPressed: () => signUp(context),
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
            if (screenWidth >= 1100)
              Expanded(
                child: Container(
                  padding: EdgeInsets.only(
                    top: paddingSize * 1.5,
                    bottom: paddingSize * 3,
                    right: paddingSize * 1,
                  ),
                  child: Image.asset(
                    'assets/signUp.png',
                    width: 500,
                    height: 600,
                  ),
                ),
              ),
          ],
        ),
      ),
    );
  }
}
