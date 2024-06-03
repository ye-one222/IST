import 'package:flutter/material.dart';
import 'package:se_frontend/projectPage.dart';
import 'package:se_frontend/files/projectClass.dart';

class ProjectBox extends StatelessWidget {
  final Project project;
  final int userId; //유저 아이디 플젝 페이지에 전달

  const ProjectBox({
    super.key,
    required this.project,
    required this.userId,
  });

  @override
  Widget build(BuildContext context) {
    print('Building ProjectBox for project: ${project.title}'); // 디버깅 메시지
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => ProjectPage(project: project, userId: userId),
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
              project.title,
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),
            Text('Leader: ${project.leaderNickname}'),
            //const SizedBox(height: 10),
            // ...project.members.map((member) {
            // return Text('Member: ${member.nickname}');
            // }).toList(),
          ],
        ),
      ),
    );
  }
}
