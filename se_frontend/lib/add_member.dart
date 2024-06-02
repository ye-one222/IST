import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:se_frontend/myDashBoard.dart';

final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

class AddMember extends StatefulWidget {
  final int projectId;
  final int userId;

  const AddMember({
    Key? key,
    required this.projectId,
    required this.userId,
  }) : super(key: key);

  @override
  _AddMemberState createState() => _AddMemberState();
}

class _AddMemberState extends State<AddMember> {
  final TextEditingController _nicknameController = TextEditingController();
  bool _isLoading = false;
  String? _errorMessage;

  Future<void> _checkAndAddMember() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    final nickname = _nicknameController.text;

    try {
      // 닉네임으로 사용자 ID를 가져오는 API 호출
      final response = await http.get(
        Uri.parse('http://localhost:8081/user/$nickname'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        final int? memberId =
            int.tryParse(response.body); //받아온 먐버정보 memberId라고 지칭

        if (memberId != null) {
          // 프로젝트에 멤버를 추가하는 API 호출
          final addMemberResponse = await http.post(
            Uri.parse(
                'http://localhost:8081/project/${widget.projectId}/invite/$memberId'),
            headers: <String, String>{
              'Content-Type': 'application/json; charset=UTF-8',
            },
          );
          if (addMemberResponse.statusCode == 200) {
            Navigator.pushAndRemoveUntil(
              context,
              MaterialPageRoute(
                builder: (context) => MyDashboard(userId: widget.userId),
              ),
              (Route<dynamic> route) => false,
            ); // 성공적으로 추가되면 대시보드로 이동
          } else {
            setState(() {
              _errorMessage =
                  'Failed to add member: ${addMemberResponse.statusCode} ${addMemberResponse.body}';
            });
          }
        } else {
          setState(() {
            _errorMessage = 'Invalid user ID format';
          });
        }
      } else {
        setState(() {
          _errorMessage = 'User not found';
        });
      }
    } catch (e) {
      setState(() {
        _errorMessage = 'Error adding member: $e';
      });
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add Member')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              const Text(
                'Nickname',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
              TextFormField(
                controller: _nicknameController,
                decoration: InputDecoration(
                  filled: true,
                  fillColor: Colors.grey,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12.0),
                    borderSide: BorderSide.none,
                  ),
                ),
                style: const TextStyle(color: Colors.white),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter a nickname';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20),
              if (_errorMessage != null)
                Text(
                  _errorMessage!,
                  style: const TextStyle(color: Colors.red),
                ),
              const SizedBox(height: 40),
              Align(
                  alignment: Alignment.bottomCenter,
                  child: SizedBox(
                      width: MediaQuery.of(context).size.width * 0.4,
                      height: 70,
                      child: ElevatedButton(
                          onPressed: _checkAndAddMember,
                          style: ElevatedButton.styleFrom(
                              backgroundColor:
                                  const Color.fromARGB(255, 255, 205, 220),
                              fixedSize: const Size.fromHeight(50),
                              shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(20))),
                          child: const Text(
                            'Click here to\nadd Member',
                            textAlign: TextAlign.center,
                            style: TextStyle(
                                color: Color.fromARGB(255, 0, 0, 0),
                                fontSize: 14,
                                fontWeight: FontWeight.bold),
                          )))),
            ],
          ),
        ),
      ),
    );
  }
}
