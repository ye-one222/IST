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
    final commentDto = json['commentDTO'];
    return Comment(
      id: commentDto['id'],
      createrId: commentDto['creater_id'],
      description: commentDto['description'],
      createdDate: DateTime.parse(commentDto['created_date']),
      issueId: commentDto['issue_id'],
      createrNickname: json['creater_nickname'],
    );
  }
}
