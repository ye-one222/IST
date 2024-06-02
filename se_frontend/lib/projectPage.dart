import 'package:flutter/material.dart';
import 'package:se_frontend/box/issueBox.dart';
import 'package:se_frontend/files/issueClass.dart';
import 'package:se_frontend/files/projectClass.dart';
import 'package:se_frontend/issue_input_field.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:se_frontend/add_member.dart';
import 'package:se_frontend/statistic.dart';

class ProjectPage extends StatefulWidget {
  final Project project; // 현재 프로젝트 전달용
  final int userId; // 유저 아이디 전달용

  const ProjectPage({
    super.key,
    required this.project, // 플젝 정보 전달받음
    required this.userId,
  });

  @override
  _ProjectPageState createState() => _ProjectPageState();
}

class _ProjectPageState extends State<ProjectPage> {
  late Project _project; // 플젝 정보 저장 함수

  @override
  void initState() {
    // 여기서 fetchProject 호출해서 백에서 정보 가져옴
    super.initState();
    _project = widget.project; // 위젯에서 전달받은 플젝 정보 초기화
    _fetchProject(); // 서버에서 플젝 정보 가져오기
  }

  Future<void> _fetchProject() async {
    try {
      final response = await http.get(
        Uri.parse('http://localhost:8081/project/${_project.id}'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        setState(() {
          _project = Project.fromJson(json.decode(response.body));
        });
      } else {
        throw Exception('Failed to load project');
      }
    } catch (e) {
      print('Error fetching project: $e');
    }
  }

  Future<List<Issue>> fetchIssues() async {
    try {
      final response = await http.get(
        Uri.parse('http://localhost:8081/project/${_project.id}/issues'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> issueJson = json.decode(response.body);
        return issueJson.map((json) {
          final issueData = json['responseIssue'] as Map<String, dynamic>;
          return Issue.fromJson({
            ...issueData,
            'reporter_nickname': json['reporter_nickname'],
            'assignee_nickname': json['assignee_nickname'] ?? '',
          });
        }).toList();
      } else {
        throw Exception('Failed to load issues');
      }
    } catch (e) {
      throw Exception('Error fetching issues: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width; // 넓이

    ScrollController scrollController = ScrollController();

    // 화면 크기에 따라 폰트 크기와 패딩을 동적으로 설정
    double fontSize = screenWidth < 850 ? 18 : 18;
    double formFieldWidth =
        screenWidth < 800 ? screenWidth * 0.8 : screenWidth * 0.3;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color.fromARGB(255, 255, 255, 255),
        title: const Row(
          children: [
            Text(
              "MY PROJECT",
              style: TextStyle(fontSize: 25, fontWeight: FontWeight.w500),
            ),
          ],
        ),
        titleSpacing: 20,
      ),
      body: SingleChildScrollView(
        // 프로젝트 제목 표시
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'TITLE : ${_project.title}',
              style: const TextStyle(
                fontSize: 25,
                fontWeight: FontWeight.w900,
              ),
            ),
            const SizedBox(height: 20),
            // 리더란
            const Text(
              "Leader",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 10),
            Container(
              constraints: const BoxConstraints(minHeight: 50),
              width: double.infinity,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(15.0),
                color: const Color.fromARGB(255, 37, 37, 37),
              ),
              alignment: Alignment.center,
              child: Text(
                _project.leaderNickname,
                style: const TextStyle(
                  fontSize: 25,
                  color: Colors.white,
                  fontWeight: FontWeight.w900,
                ),
              ),
            ),
            // 멤버들 표시 **********************************************************
            const SizedBox(height: 20),
            const Text(
              "Members",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 10),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Wrap(
                  spacing: 8.0, // 멤버들 간의 가로 간격
                  children: _project.members.map((member) {
                    return Text(
                      member.nickname,
                      style: const TextStyle(fontSize: 18),
                    );
                  }).toList(),
                ),
              ],
            ),
            const SizedBox(height: 10), //******************* 맴버 추가 부분 */
            Align(
              alignment: Alignment.bottomCenter,
              child: SizedBox(
                width: formFieldWidth,
                height: 70,
                child: ElevatedButton(
                  onPressed: () async {
                    final result = await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => AddMember(
                          projectId: _project.id,
                          userId: widget.userId,
                        ),
                      ),
                    );
                    if (result == true) {
                      _fetchProject(); // 돌아온 후 프로젝트 데이터 새로 고침
                      Navigator.pop(context, true); // 변경된 내용 전달
                    }
                  },
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromARGB(255, 255, 205, 220),
                      fixedSize: const Size.fromHeight(50),
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(20))),
                  child: Text(
                    'Click here to\nadd Member',
                    style: TextStyle(
                        color: const Color.fromARGB(255, 0, 0, 0),
                        fontSize: fontSize * 0.8,
                        fontWeight: FontWeight.bold),
                  ),
                ),
              ),
            ),
            // 이슈 생성란 이동 버튼
            const SizedBox(height: 20),
            Align(
              alignment: Alignment.bottomCenter,
              child: SizedBox(
                width: formFieldWidth,
                height: 70,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => IssueInputField(
                          projectId: _project.id,
                          userId: widget.userId, // 플젝 아이디 전달
                        ),
                      ),
                    );
                  },
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromARGB(255, 255, 205, 220),
                      fixedSize: const Size.fromHeight(50),
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(20))),
                  child: Text(
                    'Click here to\ncreate Issue',
                    style: TextStyle(
                        color: const Color.fromARGB(255, 0, 0, 0),
                        fontSize: fontSize * 0.8,
                        fontWeight: FontWeight.bold),
                  ),
                ),
              ),
            ),
            const SizedBox(height: 20),
            Align(
                alignment: Alignment.bottomCenter,
                child: SizedBox(
                    width: formFieldWidth,
                    height: 70,
                    child: ElevatedButton(
                        onPressed: () async {
                          await Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) =>
                                  IssueStatisticsScreen(projectId: _project.id),
                            ),
                          );
                        },
                        style: ElevatedButton.styleFrom(
                            backgroundColor:
                                const Color.fromARGB(255, 255, 205, 220),
                            fixedSize: const Size.fromHeight(50),
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(20))),
                        child: Text(
                          'Statistics',
                          style: TextStyle(
                              color: const Color.fromARGB(255, 0, 0, 0),
                              fontSize: fontSize * 0.8,
                              fontWeight: FontWeight.bold),
                        )))),
            // 플젝에 대한 이슈 보기란
            const SizedBox(height: 15),
            const Text(
              "Current Issues",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 10),
            Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(15.0),
                color: const Color.fromARGB(255, 146, 146, 146),
              ),
              height: 230,
              width: double.infinity,
              child: FutureBuilder<List<Issue>>(
                future: fetchIssues(), // 이슈 목록 가져옴
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const Center(child: CircularProgressIndicator());
                  } else if (snapshot.hasError) {
                    return const Center(child: Text('Error loading issues'));
                  } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                    return const Center(child: Text('No issues found'));
                  }

                  final issues = snapshot.data!;
                  return Scrollbar(
                    controller: scrollController,
                    child: SingleChildScrollView(
                      controller: scrollController,
                      scrollDirection: Axis.horizontal,
                      child: Row(
                        children: issues.map((issue) {
                          return IssueBox(
                            issue: issue,
                            userId: widget.userId,
                            // 류: 여기 수정해야되는데 닉네임 부분 뺴기 너무 벅차서 일단 닉네임이라고 해둠
                          ); // 이슈 박스로 리턴
                        }).toList(),
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
