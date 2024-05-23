import 'package:flutter/material.dart';
import 'package:se_frontend/issue_list.dart';

class MyDashboard extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        // 세로 배치
        children: [
          // 앱바ㅑ*****************************************************
          Container(
            color: Color.fromARGB(255, 255, 255, 255),
            padding: EdgeInsets.symmetric(horizontal: 50), //좌우 패딩추가
            height: 70,
            child: const Row(
              // 로우 배치
              children: [
                SizedBox(width: 10),
                Expanded(
                  child: Text(
                    'RYU SOOJUNG         PARK SIYEON          JEONG BOWOON         JEONG YEWON         HYUN SOYOUNG',
                    style: TextStyle(
                      color: Color.fromARGB(255, 94, 94, 94),
                      fontSize: 10,
                    ),
                    textAlign: TextAlign.start,
                  ),
                ),
              ],
            ),
          ),
          // 제목, 검색창********************************************8
          AppBar(
            title: Row(
              children: <Widget>[
                const Text(
                  'MY DASHBOARD',
                  style: TextStyle(
                      color: Colors.black, fontWeight: FontWeight.w900),
                ),
                const SizedBox(width: 20),
                Expanded(
                  child: TextField(
                    decoration: InputDecoration(
                      hintText: '찾으시는 이슈를 검색해보세요! ', //힌트 텍스트
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(20.0),
                        borderSide: BorderSide.none,
                      ),
                      filled: true,
                      fillColor: Color.fromARGB(255, 241, 241, 241),
                      contentPadding:
                          EdgeInsets.symmetric(horizontal: 20.0, vertical: 0),
                      suffixIcon: Icon(Icons.search), //검색 아이콘
                    ),
                  ),
                ),
              ],
            ),
          ),
          // 시작 부분
          Expanded(
            child: SingleChildScrollView(
              padding: EdgeInsets.all(15.0),
              child: Column(
                // 자식 왼쪽 정렬
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    'My Projects',
                    style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 10),
                  SingleChildScrollView(
                    scrollDirection: Axis.horizontal, //수평 스크롤 ㄱㄴ
                    child: Row(
                      children: List.generate(
                          5, //일단 5개 생성함..
                          (index) => ProjectBox(
                              title:
                                  'Project ${index + 1}', //제목 설정인데 일단 그냥 +1 로 함
                              description:
                                  'Description of project ${index + 1}')), // 설명 저장
                    ),
                  ),
                  SizedBox(height: 20),
                  Text(
                    'My Issues',
                    style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 10),
                  SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: Row(
                      children: List.generate(
                          5,
                          (index) => ProjectBox(
                              title: 'Issue ${index + 1}',
                              description:
                                  'Description of issue ${index + 1}')),
                    ),
                  ),
                ],
              ),
            ),
          ), // 소영언니가 만든 부분으로 이동
          Align(
            alignment: Alignment.bottomCenter,
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => IssueListPage(), // 여기서 올바르게 수정
                    ),
                  );
                },
                child: Text('소영언니 이거 누르면됩니당'),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// 박스로 플젝, 이슈 표현하는거
class ProjectBox extends StatelessWidget {
  final String title; //제목 저장 변수
  final String description; //설명 저장변수

  ProjectBox(
      {required this.title,
      required this.description}); // 생성자로 title과 description을 받아옴

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 200,
      padding: EdgeInsets.all(16.0),
      margin: EdgeInsets.only(right: 16.0),
      decoration: BoxDecoration(
        color: Color.fromARGB(255, 250, 219, 234),
        borderRadius: BorderRadius.circular(8.0),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            title,
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          SizedBox(height: 10),
          Text(description),
        ],
      ),
    );
  }
}
