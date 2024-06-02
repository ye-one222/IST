class Comment {
  final int id;
  final int createrId; // 여기서 변수 이름이 creator_id가 아닌 creater_id인지 확인
  final String description;
  final DateTime createdDate;
  final int issueId;

  Comment({
    required this.id,
    required this.createrId,
    required this.description,
    required this.createdDate,
    required this.issueId,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      id: json['id'] as int,
      createrId: json['creater_id'] as int,
      description: json['description'] as String,
      createdDate: DateTime.parse(json['created_date'] as String),
      issueId: json['issue_id'] as int,
    );
  }
}
