import 'package:flutter/material.dart';
import 'package:se_frontend/forUser/logIn_page.dart';
import 'package:se_frontend/forUser/signUp_page.dart';

class DesktopLayout extends StatelessWidget {
  const DesktopLayout({super.key});

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
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // 왼쪽부분
        Expanded(
          child: Container(
            padding: const EdgeInsets.only(left: 20.0),
            child: const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center, //정렬
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Image(
                    image: AssetImage('assets/logo.png'),
                    width: 600,
                    height: 200,
                  ),
                  Padding(
                    padding: EdgeInsets.only(left: 65.0),
                    child: Text(
                      'Software engineering team project \nIssue Tracking System will help you manage your project! ',
                      style: TextStyle(
                          fontSize: 15,
                          color: Colors.white,
                          fontWeight: FontWeight.w100),
                      textAlign: TextAlign.left,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
        // 오른쪽 부분
        Expanded(
          child: Container(
            padding: const EdgeInsets.all(20.0),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  SizedBox(height: 50),
                  ElevatedButton(
                    onPressed: () => _onLogInPressed(context),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromARGB(255, 255, 156, 204),
                      minimumSize: const Size(120, 50),
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      foregroundColor: Colors.white,
                    ),
                    child: const Text('Login'),
                  ),
                  const SizedBox(height: 10),
                  ElevatedButton(
                    onPressed: () => _onSignUpPressed(context),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Color.fromARGB(255, 255, 156, 204),
                      minimumSize: const Size(120, 50),
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      foregroundColor: Colors.white,
                    ),
                    child: const Text('Sign Up'),
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }
}
