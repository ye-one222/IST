import 'package:flutter/material.dart';
import 'package:se_frontend/files/issueClass.dart';
import 'package:se_frontend/issue_detail.dart';

class IssueBox extends StatelessWidget {
  final Issue issue;
  final int userId;

  const IssueBox({
    super.key,
    required this.issue,
    required this.userId,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      // 누르면 이슈 상세 페이지로 이동하게 제스처
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => IssueDetail(
              issue: issue,
              userId: userId,
            ),
          ),
        );
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
              issue.title,
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
            Text('상태: ${issue.state.toString().split('.').last}',
                style: const TextStyle(
                  fontSize: 14,
                  color: Colors.grey,
                )),
            const SizedBox(height: 10),
            Text(
                '담당자: ${issue.assigneeNickname.isNotEmpty ? issue.assigneeNickname : 'Unassigned'}',
                style: const TextStyle(
                  fontSize: 14,
                )),
            Text('보고자: ${issue.reporterNickname}',
                style: const TextStyle(
                  fontSize: 14,
                )),
            const SizedBox(height: 10),
            Text('우선순위: ${issue.priority.toString().split('.').last}',
                style: const TextStyle(
                  fontSize: 14,
                  color: Colors.grey,
                )),
            Text('프로젝트 ID: ${issue.projectId}',
                style: const TextStyle(
                  fontSize: 14,
                  color: Colors.grey,
                )),
          ],
        ),
      ),
    );
  }
}
