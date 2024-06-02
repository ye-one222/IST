import 'package:flutter/material.dart'; // Flutter의 기본 위젯 라이브러리
import 'package:http/http.dart' as http; // HTTP 요청을 위한 라이브러리
import 'dart:convert'; // JSON 디코딩을 위한 라이브러리
import 'myDashBoard.dart'; // 대시보드 위젯 임포트

class CreateProject extends StatefulWidget {
  final int userId; // 유저 아이디를 저장 변수
  const CreateProject({super.key, required this.userId}); //생성자 추가

  @override
  _CreateProjectState createState() => _CreateProjectState(); // 상태 객체 생성
}

class _CreateProjectState extends State<CreateProject> {
  final TextEditingController _titleController =
      TextEditingController(); // 제목 입력 컨트롤러
  final TextEditingController _leaderController =
      TextEditingController(); // 리더 입력 컨트롤러
  int? _leaderId; // 리더 id를 저장할 변수 (user_id임 결국엔)
  bool _isLoading = false; // 로딩 상태 변수
  String? _errorMessage; // 에러 메시지를 저장할 변수

  // 리더 ID를 가져오는 비동기 함수
  Future<void> _fetchLeaderId(String nickname) async {
    try {
      final response = await http.get(
        Uri.parse('http://localhost:8081/user/$nickname'), // GET 요청 URL
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );
      if (response.statusCode == 200) {
        // 요청이 성공하면
        final int? userId = int.tryParse(response.body); // 응답 본문을 int로 변환
        setState(() {
          if (userId != null) {
            _leaderId = userId; // 성공하면 리더 id 에 user_id 저장 (int)
            _errorMessage = null; // 에러 메시지 초기화
          } else {
            _leaderId = null; // 실패하면 null로 저장
            _errorMessage = 'Invalid user ID format'; // 에러 메시지 설정
          }
        });
      } else {
        setState(() {
          _leaderId = null; // 실패하면 null로 저장
          _errorMessage = 'Leader not found'; // 에러 메시지 설정
        });
      }
    } catch (e) {
      setState(() {
        _leaderId = null; // 예외 발생 시 null로 저장
        _errorMessage = 'Error fetching leader ID: $e'; // 에러 메시지 설정
      });
    } finally {
      setState(() {
        _isLoading = false; // 로딩 상태 해제
      });
    }
  }

  // 프로젝트를 생성하는 비동기 함수
  Future<void> _createProject(
    String title,
    int leaderId,
  ) async {
    try {
      final response = await http.post(
        Uri.parse('http://localhost:8081/project/create'), // POST 요청 URL
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, dynamic>{
          'title': title, // 제목 전달
          'leader_id': leaderId, // 리더 ID 전달(user_id)
        }),
      );

      if (response.statusCode == 201 || response.statusCode == 200) {
        // 요청 성공하면 대시보드 이동
        jsonDecode(response.body); // 응답 본문을 JSON으로 디코드
        {
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(
                builder: (context) => MyDashboard(userId: widget.userId)),
          );
        }
      } else {
        setState(() {
          _errorMessage =
              'Failed to create project: ${response.statusCode} ${response.body}';
        });
      }
    } catch (e) {
      print('Error creating project: $e'); // 콘솔에 에러 메시지 출력
      setState(() {
        _errorMessage = 'Error creating project: $e'; // 에러 메시지 설정
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Create Project'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          child: Column(
            children: [
              const Text(
                'Title',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
              TextFormField(
                controller: _titleController,
                decoration: InputDecoration(
                  filled: true,
                  fillColor: Colors.grey,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12.0),
                    borderSide: BorderSide.none,
                  ),
                ),
                style: const TextStyle(color: Colors.white),
              ),
              const SizedBox(height: 20),
              const Text(
                'Leader Nickname',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
              TextFormField(
                controller: _leaderController,
                decoration: InputDecoration(
                  filled: true,
                  fillColor: Colors.grey,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12.0),
                    borderSide: BorderSide.none,
                  ),
                ),
                style: const TextStyle(color: Colors.white),
                onChanged: (value) async {
                  if (value.isNotEmpty) {
                    setState(() {
                      _isLoading = true; // 로딩 상태 설정
                    });
                    await _fetchLeaderId(value); // 리더 ID 가져오기
                    setState(() {
                      _isLoading = false; // 로딩 상태 해제
                    });
                  }
                },
              ),
              if (_isLoading) // 로딩 중일 때
                const CircularProgressIndicator() // 로딩 인디케이터 표시
              else if (_errorMessage != null) // 에러 메시지가 있으면
                Text(
                  _errorMessage!,
                  style: const TextStyle(color: Colors.red), // 에러 메시지 스타일 설정
                )
              else if (_leaderId != null) // 리더 ID가 있으면
                Text('Leader ID: $_leaderId'), // 리더 ID 표시
              const SizedBox(height: 20), // 여백 추가
              Align(
                alignment: Alignment.bottomCenter,
                child: SizedBox(
                  width: MediaQuery.of(context).size.width * 0.4,
                  height: 70,
                  child: ElevatedButton(
                    onPressed: _leaderId == null
                        ? null // 리더 ID가 없으면 버튼 비활성화
                        : () async {
                            await _createProject(
                                _titleController.text, _leaderId!); // 프로젝트 생성
                          },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromARGB(255, 255, 205, 220),
                      fixedSize: const Size.fromHeight(50),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                    ),
                    child: const Text(
                      'Click here to\nCreate Project',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        color: Color.fromARGB(255, 0, 0, 0),
                        fontSize: 14,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
