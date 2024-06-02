import 'package:flutter/material.dart';
import 'package:se_frontend/files/issueClass.dart';

class IssueCard extends StatelessWidget {
  final String title;
  final String status;
  final String assignee;
  final String reporter;

  final greybackground = const Color(0xffD9D9D9);

  const IssueCard({
    super.key,
    required this.title,
    required this.status,
    required this.assignee,
    required this.reporter,
  });

  factory IssueCard.fromIssue(Issue issue) {
    return IssueCard(
      title: issue.title,
      status: issue.state.toString().split('.').last,
      assignee:
          issue.assignee != null ? issue.assignee.toString() : 'Unassigned',
      reporter: issue.reporter.toString(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding:
          const EdgeInsets.symmetric(horizontal: 30, vertical: 10), // 여백을 8로 설정
      child: Container(
        width: double.infinity,
        decoration: BoxDecoration(
          color: greybackground,
          borderRadius: BorderRadius.circular(25),
        ),
        child: Padding(
          // 내부 패딩을 추가
          padding: const EdgeInsets.all(25.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Text(
                    title,
                    style: const TextStyle(
                      fontSize: 20, // 폰트 크기를 20으로 설정
                      fontWeight: FontWeight.bold,
                      color: Colors.black,
                    ),
                  ),
                  const SizedBox(
                    width: 10,
                  ),
                  Text(
                    status,
                    style: const TextStyle(
                      fontSize: 15,
                      color: Colors.black,
                    ),
                  ),
                ],
              ),
              const SizedBox(
                height: 10,
              ),
              Text(
                'Assignee: $assignee    |    Reporter: $reporter',
                style: const TextStyle(
                  fontSize: 15,
                  color: Colors.black,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
