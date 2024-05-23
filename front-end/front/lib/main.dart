import 'package:flutter/material.dart';
import 'issue_list.dart'; // issue_list.dart 파일을 임포트합니다.

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Issue Tracker',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const IssueListPage(),
    );
  }
}
