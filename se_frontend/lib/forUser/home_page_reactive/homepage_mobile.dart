import 'package:flutter/material.dart';
import 'package:se_frontend/forUser/logIn_page.dart';
import 'package:se_frontend/forUser/SignUp_page.dart';

class MobileLayout extends StatelessWidget {
  const MobileLayout({Key? key}) : super(key: key);

  void _onLogInPressed(BuildContext context) {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => LoginPage()),
    );
  }

  void _onSignUpPressed(BuildContext context) {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => SignUpPage()));
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Image(
          image: AssetImage('assets/logo.png'),
          width: 400,
          height: 200,
        ),
        const Text(
          'Software engineering team project \nIssue Tracking System will help you manage your project! ',
          style: TextStyle(
              fontSize: 12, color: Colors.white, fontWeight: FontWeight.w100),
          textAlign: TextAlign.center,
        ),
        const SizedBox(height: 30),
        ElevatedButton(
            onPressed: () => _onLogInPressed(context),
            style: ElevatedButton.styleFrom(
              backgroundColor: const Color.fromARGB(255, 255, 156, 204),
              minimumSize: const Size(120, 50),
              padding: const EdgeInsets.symmetric(horizontal: 20),
              foregroundColor: Colors.white, // 텍스트 색상 설정
            ),
            child: const Text('Login')),
        const SizedBox(height: 10),
        ElevatedButton(
          onPressed: () => _onSignUpPressed,
          style: ElevatedButton.styleFrom(
            backgroundColor: const Color.fromARGB(255, 255, 156, 204),
            minimumSize: const Size(120, 50),
            padding: const EdgeInsets.symmetric(horizontal: 20),
            foregroundColor: Colors.white, // 텍스트 색상 설정
          ),
          child: const Text('Sign Up'),
        )
      ],
    );
  }
}
