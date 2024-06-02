import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:se_frontend/files/issueClass.dart'; // 이슈 클래스 임포트
import 'widgets/issue_card.dart';
import 'issue_detail.dart';

// 이슈 리스트를 보고 검색 가능

class IssueListPage extends StatefulWidget {
  final int userId; // 유저 id

  const IssueListPage({
    super.key,
    required this.userId, 
  });

  @override
  IssueListPageState createState() => IssueListPageState();
}

class IssueListPageState extends State<IssueListPage> {
  List<Issue> issues = [];
  List<Issue> filteredIssues = [];
  String selectedStatus = 'All';

  @override
  void initState() {
    super.initState();
    _fetchIssues(); // 이슈 목록을 가져오는 함수 호출
  }

  Future<void> _fetchIssues() async {
    try {
      final response = await http.get(
        Uri.parse('http://localhost:8081/issue/my/${widget.userId}'),
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        // 성공하면
        final List<dynamic> issueJson = json.decode(response.body);
        setState(() {
          issues = issueJson
              .map((json) => Issue.fromJson(json))
              .toList(); // 이슈 리스트 생성
          filteredIssues = issues;
        });
      } else {
        throw Exception('Failed to load issues');
      }
    } catch (e) {
      print('Error fetching issues: $e');
    }
  }

  void _filterIssues(String query) {
    final filtered = issues.where((issue) {
      final matchesTitle =
          issue.title.toLowerCase().contains(query.toLowerCase());
      final matchesReporter = issue.reporter.toString().contains(query);
      final matchesAssignee =
          issue.assignee?.toString().contains(query) ?? false;
      final matchesStatus = selectedStatus == 'All' ||
          issue.state.toString().split('.').last == selectedStatus;
      return (matchesTitle || matchesReporter || matchesAssignee) &&
          matchesStatus;
    }).toList();

    setState(() {
      filteredIssues = filtered; // 이슈 검색 필터링 반영
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 30, horizontal: 150),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    decoration: InputDecoration(
                      labelText: '찾으시려는 이슈를 입력하세요!',
                      border: const OutlineInputBorder(),
                      enabledBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(30),
                      ),
                    ),
                    onChanged: _filterIssues,
                  ),
                ),
                const SizedBox(width: 20),
                DropdownButton<String>(
                  value: selectedStatus,
                  onChanged: (String? newValue) {
                    setState(() {
                      selectedStatus = newValue!;
                      _filterIssues('');
                    });
                  },
                  items: <String>[
                    'All',
                    'NEW',
                    'ASSIGNED',
                    'FIXED',
                    'RESOLVED',
                    'CLOSED',
                    'REOPEND'
                  ].map<DropdownMenuItem<String>>((String value) {
                    return DropdownMenuItem<String>(
                      value: value,
                      child: Text(value),
                    );
                  }).toList(),
                ),
              ],
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: filteredIssues.length,
              itemBuilder: (context, index) {
                final issue = filteredIssues[index];
                return GestureDetector(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => IssueDetail(
                            issue: issue, userId: widget.userId),
                      ),
                    );
                  },
                  child: IssueCard(
                    title: issue.title,
                    status: issue.state.toString().split('.').last,
                    reporter: issue.reporter.toString(),
                    assignee: issue.assignee?.toString() ?? 'Unassigned',
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
