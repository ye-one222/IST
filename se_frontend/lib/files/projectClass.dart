import 'package:se_frontend/files/issueClass.dart';

class Project {
  final int id;
  final String title;
  final int leaderId;
  final String leaderNickname;
  final List<Issue> issues;
  final List<Member> members;

  Project({
    required this.id,
    required this.title,
    required this.leaderId,
    required this.leaderNickname,
    required this.issues,
    required this.members,
  });

  factory Project.fromJson(Map<String, dynamic> json) {
    final projectDTO = json['projectDTO'] as Map<String, dynamic>? ?? {};
    return Project(
      id: projectDTO['id'] ?? 0,
      title: projectDTO['title'] ?? '',
      leaderId: projectDTO['leader_id'] ?? 0,
      leaderNickname: json['leader_nickname'] ?? '',
      issues: (projectDTO['issues'] as List<dynamic>?)
              ?.map((i) => Issue.fromJson(i as Map<String, dynamic>))
              .toList() ??
          [],
      members: (projectDTO['members'] as List<dynamic>?)
              ?.map((m) => Member.fromJson(m as Map<String, dynamic>))
              .toList() ??
          [],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'leader_id': leaderId,
      'leader_nickname': leaderNickname,
      'issues': issues.map((i) => i.toJson()).toList(),
      'members': members.map((m) => m.toJson()).toList(),
    };
  }
}

class Member {
  final int userId;
  final String nickname;
  final String password;
  final List<Project> projects;

  Member({
    required this.userId,
    required this.nickname,
    required this.password,
    required this.projects,
  });

  factory Member.fromJson(Map<String, dynamic> json) {
    return Member(
      userId: json['user_id'] ?? 0,
      nickname: json['nickname'] ?? '',
      password: json['password'] ?? '',
      projects: (json['projects'] as List<dynamic>?)
              ?.map((p) => Project.fromJson(p as Map<String, dynamic>))
              .toList() ??
          [],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'user_id': userId,
      'nickname': nickname,
      'password': password,
      'projects': projects.map((p) => p.toJson()).toList(),
    };
  }
}
