import 'package:flutter/material.dart';
import 'widgets/issue_card.dart'; // issue_card.dart 파일을 임포트합니다.

class Issue {
  final String title;
  final String status;
  final String reporter;
  final String assignee;
  final int commentCount;

  Issue({
    required this.title,
    required this.status,
    required this.reporter,
    required this.assignee,
    required this.commentCount,
  });
}

class IssueListPage extends StatefulWidget {
  const IssueListPage({super.key});

  @override
  IssueListPageState createState() => IssueListPageState();
}

class IssueListPageState extends State<IssueListPage> {
  List<Issue> issues = [
    // 현재 나의 이슈, 나중에 백에서 받아오기.
    Issue(
      title: 'Issue 1',
      status: 'Open',
      reporter: 'User1',
      assignee: 'User2',
      commentCount: 5,
    ),
    Issue(
      title: 'Issue 2',
      status: 'Closed',
      reporter: 'User3',
      assignee: 'User4',
      commentCount: 2,
    ),
    // Add more issues here
  ];

  List<Issue> filteredIssues = []; // 검색된 이슈

  @override
  void initState() {
    super.initState();
    filteredIssues = issues;
  }

  void _filterIssues(String query) {
    // 검색어 필터
    final filtered = issues.where((issue) {
      return issue.title.toLowerCase().contains(query.toLowerCase()) ||
          issue.status.toLowerCase().contains(query.toLowerCase()) ||
          issue.reporter.toLowerCase().contains(query.toLowerCase()) ||
          issue.assignee.toLowerCase().contains(query.toLowerCase());
    }).toList();

    setState(() {
      filteredIssues = filtered;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // appBar: AppBar(
      //   title: const Text('Issue Tracker'),
      // ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Padding(
              padding: const EdgeInsets.all(30.0),
              child: TextField(
                decoration: const InputDecoration(
                  labelText: '찾으시려는 이슈를 입력하세요!',
                  border: OutlineInputBorder(),
                ),
                onChanged: _filterIssues,
              ),
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: filteredIssues.length,
              itemBuilder: (context, index) {
                final issue = filteredIssues[index];
                return IssueCard(
                  title: issue.title,
                  status: issue.status,
                  reporter: issue.reporter,
                  assignee: issue.assignee,
                  commentCount: issue.commentCount,
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
