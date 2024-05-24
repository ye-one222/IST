import 'package:flutter/material.dart';

class IssueCard extends StatelessWidget {
  final String title, status, assignee, reporter;
  final int commentCount;

  final greybackground = const Color(0xffD9D9D9);

  const IssueCard({
    super.key,
    required this.title,
    required this.status,
    required this.assignee,
    required this.reporter,
    required this.commentCount,
  });

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
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Expanded(
                    child: Text(
                      'Assignee: $assignee    |    Reporter: $reporter',
                      style: const TextStyle(
                        fontSize: 15,
                        color: Colors.black,
                      ),
                    ),
                  ),
                  Text(
                    'Comments: $commentCount',
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.w700,
                      color: Colors.black,
                    ),
                  )
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
