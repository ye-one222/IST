class Comment {
  final int id;
  final int createrId;
  final String description;
  final DateTime createdDate;
  final int issueId;
  final String createrNickname;

  Comment({
    required this.id,
    required this.createrId,
    required this.description,
    required this.createdDate,
    required this.issueId,
    required this.createrNickname,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    final commentDto = json['commentDTO'] ?? json;
    return Comment(
      id: commentDto['id'] ?? 0, // 기본값을 0으로 설정
      createrId: commentDto['creater_id'] ?? 0,
      description: commentDto['description'] ?? '',
      createdDate: DateTime.parse(
          commentDto['created_date'] ?? DateTime.now().toIso8601String()),
      issueId: commentDto['issue_id'] ?? 0,
      createrNickname: json['creater_nickname'] ?? '',
    );
  }
}
