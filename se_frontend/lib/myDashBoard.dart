import 'package:flutter/material.dart';
import 'package:se_frontend/issue_list.dart';
import 'package:se_frontend/project.dart';

class MyDashboard extends StatelessWidget {
  const MyDashboard({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        // 세로 배치
        children: [
          // 제목, 검색창
          AppBar(
            title: Row(
              children: <Widget>[
                const Text(
                  'MY DASHBOARD',
                  style: TextStyle(
                    color: Colors.black,
                    fontWeight: FontWeight.w900,
                  ),
                ),
                const SizedBox(width: 30),
                Expanded(
                  child: TextField(
                    decoration: InputDecoration(
                      hintText: ' 찾으시는 이슈를 검색해보세요! ',
                      hintStyle: const TextStyle(fontSize: 12), // 힌트 텍스트
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(40.0),
                        borderSide: BorderSide.none,
                      ),
                      filled: true,
                      fillColor: const Color.fromARGB(255, 241, 241, 241),
                      contentPadding: const EdgeInsets.symmetric(
                          horizontal: 20.0, vertical: 0),
                      suffixIcon: const Icon(Icons.search), // 검색 아이콘
                    ),
                  ),
                ),
              ],
            ),
          ),
          // 시작 부분
          Expanded(
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(15.0),
              child: Column(
                // 자식 왼쪽 정렬
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  // 내 프로젝트 상자 ***************************
                  const SizedBox(height: 10),
                  const Text('MY PROJECTS',
                      style: TextStyle(
                        fontSize: 22,
                        fontWeight: FontWeight.w500,
                      )),
                  const SizedBox(height: 10),
                  Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(15.0),
                      color: Color.fromARGB(255, 146, 146, 146),
                    ),
                    // 흰색 박스
                    height: 230,
                    width: double.infinity,
                    child: Scrollbar(
                      //스크롤바 추가
                      child: SingleChildScrollView(
                        scrollDirection: Axis.horizontal, // 수평 스크롤 가능
                        child: Row(
                          children: List.generate(
                            20,
                            // 많이 생성해봄
                            (index) => ProjectBox(
                              title: 'Project ${index + 1}', // 제목 설정
                              description:
                                  'Description of project ${index + 1}', // 설명 저장
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  //내 이슈들 상자*******************************
                  const Text(
                    'MY ISSUES',
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(15.0),
                      color: Color.fromARGB(255, 146, 146, 146),
                    ),
                    height: 230,
                    width: double.infinity,
                    child: Scrollbar(
                      //스크롤바 추가
                      child: SingleChildScrollView(
                        scrollDirection: Axis.horizontal,
                        child: Row(
                          children: List.generate(
                            9,
                            (index) => IssueBox(
                              title: 'Issue ${index + 1}',
                              assignee: 'assignee ${index + 1}',
                              reporter: 'reporter ${index + 1}',
                              status: 'new',
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
          // 소영언니가 만든 부분으로 이동
          Align(
            alignment: Alignment.bottomCenter,
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const IssueListPage(),
                    ),
                  );
                },
                child: const Text('소영언니 이거 누르면됩니당'),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// 박스로 플젝표현
class ProjectBox extends StatelessWidget {
  final String title; // 제목 저장 변수
  final String description; // 설명 저장 변수

  const ProjectBox({
    required this.title,
    required this.description,
  }); // 생성자로 title과 description을 받아옴

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
        // 누르면 프로젝트 페이지로 이동하게 제스쳐
        onTap: () {
          Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => ProjectPage(
                  title: title,
                  description: description,
                ),
              ));
        },
        child: Container(
          width: 250,
          height: 150,
          padding: const EdgeInsets.all(16.0),
          margin: const EdgeInsets.only(right: 16.0),
          decoration: BoxDecoration(
            color: const Color.fromARGB(255, 250, 219, 234),
            borderRadius: BorderRadius.circular(15.0),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Text(
                title,
                style:
                    const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              Text(description),
            ],
          ),
        ));
  }
}

//박스로 이슈 표현
class IssueBox extends StatelessWidget {
  final String title; // 제목 저장 변수

  final String assignee;
  final String reporter;
  final String status;

  const IssueBox({
    required this.title,
    required this.assignee,
    required this.reporter,
    required this.status,
  }); // 생성자로 title과 description을 받아옴

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 250,
      height: 150,
      padding: const EdgeInsets.all(16.0),
      margin: const EdgeInsets.only(right: 16.0),
      decoration: BoxDecoration(
        color: const Color.fromARGB(255, 250, 219, 234),
        borderRadius: BorderRadius.circular(15.0),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            title,
            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          Text(status),
          const SizedBox(height: 10),
          Text(assignee),
          Text(reporter),
        ],
      ),
    );
  }
}
