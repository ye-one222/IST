import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:se_frontend/files/issueClass.dart';
import 'package:se_frontend/widgets/detail_box.dart';
import 'package:intl/intl.dart'; // 류: 날짜 때문에 추가
import 'package:se_frontend/files/commentClass.dart';

class IssueDetail extends StatefulWidget {
  final Issue issue;
  final int userId;

  const IssueDetail({
    super.key,
    required this.issue,
    required this.userId,
  });

  @override
  State<IssueDetail> createState() => _IssueDetailState();
}

class _IssueDetailState extends State<IssueDetail> {
  late IState _selectedState;
  late TextEditingController _assigneeController;
  late TextEditingController _commentController; // 류: 사용자 댓글 컨트롤러
  List<Comment> _comments = []; // 댓글 용

  @override
  void initState() {
    super.initState();
    _selectedState = widget.issue.state;
    _assigneeController =
        TextEditingController(text: widget.issue.assigneeNickname);
    _commentController = TextEditingController(); // 류: 댓글 부분
    _fetchComments(); // 류: 댓글 불러오기
  }

  @override
  void dispose() {
    _assigneeController.dispose();
    _commentController.dispose();
    super.dispose();
  }

  Future<void> _fetchComments() async {
    final url = 'http://localhost:8081/api/issue/${widget.issue.id}/comments';
    try {
      final response = await http.get(Uri.parse(url));
      print('###$response###${widget.issue.id}');
      if (response.statusCode == 201 || response.statusCode == 200) {
        final List<dynamic> commentsJson = json.decode(response.body);
        print(commentsJson);
        setState(() {
          _comments =
              commentsJson.map((json) => Comment.fromJson(json)).toList();
        });
      } else {
        throw Exception('Failed to load comments');
      }
    } catch (e) {
      print('Error fetching comments: $e');
    }
  }

  Future<void> _addComment() async {
    // 류: 댓글 달기
    if (_commentController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter a comment.')),
      );
      return;
    }

    const url = 'http://localhost:8081/api/comments/create'; // 수정된 URL
    final headers = {"Content-Type": "application/json"};
    final body = json.encode({
      "creater_id": widget.userId, // 여기에서 creator_id를 creater_id로 변경
      "description": _commentController.text,
      "issue_id": widget.issue.id
    });

    try {
      final response = await http.post(
        Uri.parse(url),
        headers: headers,
        body: body,
      );

      if (response.statusCode == 200) {
        final newComment = Comment.fromJson(json.decode(response.body));
        setState(() {
          _comments.add(newComment);
          _commentController.clear();
        });
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Comment added successfully.')),
        );
      } else {
        print('Failed to add comment: ${response.statusCode} ${response.body}');
        throw Exception('Failed to add comment');
      }
    } catch (e) {
      print('Error adding comment: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
            content: Text('Failed to add comment. Please try again.')),
      );
    }
  }

  // nickname로부터 ID 받아오기
  Future<int?> _fetchUserId(String nickname) async {
    final url = 'http://localhost:8081/user/$nickname'; // URL 설정
    final response = await http.get(
      Uri.parse(url),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if (response.statusCode == 200) {
      return int.tryParse(response.body);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
            content: Text(
                'Failed to find user. Please check the nickname and try again')),
      );
      return null;
    }
  }

  // 이슈 배정 (new -> assigned)
  Future<void> _assignUser() async {
    if (_assigneeController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter a user for the assignee.')),
      );
      return;
    }

    final assigneeId =
        await _fetchUserId(_assigneeController.text); // 닉네임으로부터 바꾼 id

    final url =
        'http://localhost:8081/issue/${widget.issue.id}/update/${widget.userId}'; // 로그인한 사용자 id를 넘겨줘야 함.
    final headers = {"Content-Type": "application/json"};
    const oldState = 'new';
    const newState = 'assigned';

    Map<String, dynamic> body = {
      "oldState": oldState,
      "newState": newState,
      "assignee_id": assigneeId,
    };

    final response = await http.patch(
      Uri.parse(url),
      headers: headers,
      body: json.encode(body),
    );

    switch (response.statusCode) {
      case 200:
        setState(() {
          widget.issue.state = IState.ASSIGNED;
          widget.issue.assignee = assigneeId;
          _selectedState = IState.ASSIGNED;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text(
                  'User assigned successfully. Status updated to assigned.')),
        );
        break;
      case 401:
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text(
                  'The specified assignee is not part of the project team.')),
        );
        break;
      case 403:
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text('You do not have permission to assign users.')),
        );
        break;
      default:
        print('${response.statusCode}\n\n');
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text('Failed to assign user. Please try again.')),
        );
    }
  }

  // 나머지 이슈 상태 변경
  Future<void> _updateState() async {
    if (_selectedState == IState.ASSIGNED && widget.issue.state == IState.NEW) {
      await _assignUser();
      return;
    }

    final url =
        'http://localhost:8081/issue/${widget.issue.id}/update/${widget.userId}'; // URL 설정
    final headers = {"Content-Type": "application/json"};
    final oldState =
        widget.issue.state.toString().split('.').last.toLowerCase();
    final newState = _selectedState.toString().split('.').last.toLowerCase();

    Map<String, dynamic> body = {
      "oldState": oldState,
      "newState": newState,
    };

    final response = await http.patch(
      Uri.parse(url),
      headers: headers,
      body: json.encode(body),
    );

    switch (response.statusCode) {
      case 200:
        setState(() {
          widget.issue.state = _selectedState;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
              content: Text(
                  'Status updated to ${_selectedState.toString().split('.').last}')),
        );
        break;
      case 403:
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('You do not have permission.')),
        );
        break;
      default:
        print('${response.statusCode}\n\n');
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text('Failed to update status. Please try again.')),
        );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("ISSUE OVERALL VIEW"),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              DetailBox(
                item: 'Title:',
                content: widget.issue.title,
              ),
              DetailBox(
                item: 'Description',
                content: widget.issue.description,
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      const Text(
                        'Status',
                        style: TextStyle(
                          fontSize: 25,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(
                        width: 15,
                      ),
                      DropdownButton<IState>(
                        value: _selectedState,
                        onChanged: (IState? newValue) {
                          // new에서 assigned로 변경할 수 없도록 필터링
                          if (widget.issue.state == IState.NEW &&
                              newValue == IState.ASSIGNED) {
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(
                                  content: Text(
                                      'Cannot change status from new to assigned here.')),
                            );
                            return;
                          }
                          setState(() {
                            _selectedState = newValue!;
                          });
                        },
                        items: IState.values
                            .map<DropdownMenuItem<IState>>((IState value) {
                          return DropdownMenuItem<IState>(
                            value: value,
                            child: Text(value.toString().split('.').last),
                          );
                        }).toList(),
                      ),
                    ],
                  ),
                ],
              ),
              const SizedBox(height: 10),
              ElevatedButton(
                // 이슈 변경 버튼
                onPressed: _updateState,
                child: const Text('Update Status'),
              ),
              const SizedBox(height: 25),
              DetailBox(
                item: 'Priority',
                content: widget.issue.priority.toString().split('.').last,
              ),
              DetailBox(
                item: 'Reporter',
                content: widget.issue.reporterNickname,
              ),
              const Text(
                'Assignee(Fixer)',
                style: TextStyle(
                  fontSize: 25,
                  fontWeight: FontWeight.bold,
                ),
              ),
              TextField(
                controller: _assigneeController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(height: 10),
              ElevatedButton(
                // 배정 버튼
                onPressed: _assignUser,
                child: const Text('Assign User'),
              ),
              // ************** 댓글 부분
              const SizedBox(height: 25),
              const Text(
                'Comments',
                style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              Container(
                decoration: BoxDecoration(
                  color: Colors.grey[200],
                  borderRadius: BorderRadius.all(Radius.circular(10)),
                ),
                constraints: const BoxConstraints(maxHeight: 200),
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: _comments.length,
                  itemBuilder: (context, index) {
                    final comment = _comments[index];
                    return ListTile(
                      title: Text(comment.description),
                      subtitle: Row(
                        children: [
                          Text(DateFormat('yyyy-MM-dd – kk:mm')
                              .format(comment.createdDate)),
                          const SizedBox(width: 10),
                          Text(comment.createrNickname, // 댓글 작성자의 닉네임 표시
                              style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  color: Colors.black54)),
                        ],
                      ),
                    );
                  },
                ),
              ),
              const SizedBox(height: 10),
              TextField(
                controller: _commentController,
                decoration: const InputDecoration(
                  labelText: 'Add a comment',
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(height: 10),
              ElevatedButton(
                // 댓글 추가 버튼
                onPressed: _addComment,
                child: const Text('Add Comment'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
