enum IPriority { BLOCKER, CRITICAL, MAJOR, MINOR, TRIVIAL }

enum IState { NEW, ASSIGNED, FIXED, RESOLVED, CLOSED, REOPENED }

class Issue {
  int id;
  String title;
  String description;
  int reporter;
  DateTime date;
  IPriority priority;
  int projectId;
  int? fixer;
  int? assignee;
  IState state;
  String reporterNickname;
  String assigneeNickname;

  Issue({
    required this.id,
    required this.title,
    required this.description,
    required this.reporter,
    required this.date,
    required this.priority,
    required this.projectId,
    this.fixer,
    this.assignee,
    required this.state,
    required this.reporterNickname,
    required this.assigneeNickname,
  });

  factory Issue.fromJson(Map<String, dynamic> json) {
    return Issue(
      id: json['id'] ?? 0,
      title: json['title'] ?? '',
      description: json['description'] ?? '',
      reporter: json['reporter_id'] ?? 0,
      date: DateTime.parse(json['date']),
      priority: _parsePriority(json['priority']),
      projectId: json['project_id'] ?? 0,
      fixer: json['fixer_id'],
      assignee: json['assignee_id'],
      state: _parseState(json['state']),
      reporterNickname: json['reporter_nickname'] ?? '',
      assigneeNickname: json['assignee_nickname'] ?? '',
    );
  }

  static IPriority _parsePriority(String? priority) {
    switch (priority?.toUpperCase()) {
      case 'BLOCKER':
        return IPriority.BLOCKER;
      case 'CRITICAL':
        return IPriority.CRITICAL;
      case 'MAJOR':
        return IPriority.MAJOR;
      case 'MINOR':
        return IPriority.MINOR;
      case 'TRIVIAL':
      default:
        return IPriority.TRIVIAL;
    }
  }

  static IState _parseState(String? state) {
    switch (state?.toUpperCase()) {
      case 'ASSIGNED':
        return IState.ASSIGNED;
      case 'FIXED':
        return IState.FIXED;
      case 'RESOLVED':
        return IState.RESOLVED;
      case 'CLOSED':
        return IState.CLOSED;
      case 'REOPENED':
        return IState.REOPENED;
      case 'NEW':
      default:
        return IState.NEW;
    }
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'reporter_id': reporter,
      'date': date.toIso8601String(),
      'priority': priority.toString().split('.').last,
      'project_id': projectId,
      'fixer_id': fixer,
      'assignee_id': assignee,
      'state': state.toString().split('.').last,
      'reporter_nickname': reporterNickname,
      'assignee_nickname': assigneeNickname,
    };
  }
}
