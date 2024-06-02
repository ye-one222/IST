class Comment {
  final int id;
  final int creatorId;
  final String description;
  final DateTime createdDate;
  final int issueId;

  Comment({
    required this.id,
    required this.creatorId,
    required this.description,
    required this.createdDate,
    required this.issueId,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      id: json['id'],
      creatorId: json['creator_id'],
      description: json['description'],
      createdDate: DateTime.parse(json['created_date']),
      issueId: json['issue_id'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'creator_id': creatorId,
      'description': description,
      'created_date': createdDate.toIso8601String(),
      'issue_id': issueId,
    };
  }
}
