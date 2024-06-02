// TODO Implement this library

import 'package:flutter/material.dart';
import 'dart:math' as math;
import 'dart:convert';
import 'package:http/http.dart' as http;

// IssueData 클래스 정의
class IssueData {
  final int month;
  final int newCnt;
  final int assignedCnt;
  final int fixedCnt;
  final int resolvedCnt;
  final int closedCnt;
  final int reopenedCnt;

  IssueData({
    required this.month,
    required this.newCnt,
    required this.assignedCnt,
    required this.fixedCnt,
    required this.resolvedCnt,
    required this.closedCnt,
    required this.reopenedCnt,
  });

  factory IssueData.fromJson(Map<String, dynamic> json) {
    return IssueData(
      month: json['month'],
      newCnt: json['newCnt'],
      assignedCnt: json['assignedCnt'],
      fixedCnt: json['fixedCnt'],
      resolvedCnt: json['resolvedCnt'],
      closedCnt: json['closedCnt'],
      reopenedCnt: json['reopened'],
    );
  }
}

// 실제 API 호출을 통해 데이터를 반환하는 함수
Future<List<IssueData>> fetchIssueData(int projectId) async {
  final response = await http
      .get(Uri.parse('http://localhost:8081/project/analysis/$projectId'));

  if (response.statusCode == 200) {
    List<dynamic> data = jsonDecode(response.body);
    return data.map((item) => IssueData.fromJson(item)).toList();
  } else {
    throw Exception('Failed to load issue data');
  }
}

// IssueStatisticsScreen 위젯 정의
class IssueStatisticsScreen extends StatefulWidget {
  final int projectId;
  const IssueStatisticsScreen({super.key, required this.projectId});

  @override
  _IssueStatisticsScreenState createState() => _IssueStatisticsScreenState();
}

class _IssueStatisticsScreenState extends State<IssueStatisticsScreen> {
  late Future<List<IssueData>> futureIssueData;

  @override
  void initState() {
    super.initState();
    futureIssueData = fetchIssueData(widget.projectId); // 실제 API 호출
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Issue Statistics')),
      body: FutureBuilder<List<IssueData>>(
        future: futureIssueData,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('No data available'));
          }

          List<IssueData> data = snapshot.data!;

          return Padding(
            padding: const EdgeInsets.all(16.0),
            child: GridView.builder(
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                mainAxisSpacing: 16,
                crossAxisSpacing: 16,
                childAspectRatio: 1.0,
              ),
              itemCount: data.length,
              itemBuilder: (context, index) {
                return _buildDonutChart(data[index]);
              },
            ),
          );
        },
      ),
    );
  }

  Widget _buildDonutChart(IssueData issueData) {
    List<PieModel> pieData = [
      PieModel(count: issueData.newCnt, color: Colors.amber, label: 'New'),
      PieModel(
          count: issueData.assignedCnt, color: Colors.red, label: 'Assigned'),
      PieModel(count: issueData.fixedCnt, color: Colors.blue, label: 'Fixed'),
      PieModel(
          count: issueData.resolvedCnt, color: Colors.green, label: 'Resolved'),
      PieModel(
          count: issueData.closedCnt, color: Colors.purple, label: 'Closed'),
      PieModel(
          count: issueData.reopenedCnt,
          color: Colors.orange,
          label: 'Reopened'),
    ];

    int totalIssues = issueData.newCnt +
        issueData.assignedCnt +
        issueData.fixedCnt +
        issueData.resolvedCnt +
        issueData.closedCnt +
        issueData.reopenedCnt;

    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.all(5.0),
          child: Text(
            '${issueData.month}월',
            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
        ),
        const SizedBox(
          height: 20,
        ),
        SizedBox(
          height: 150,
          child: CustomPaint(
            size: const Size(150, 150),
            painter: DonutChart(pieData),
          ),
        ),
        const SizedBox(
          height: 20,
        ),
        Padding(
          padding: const EdgeInsets.all(10.0),
          child: Text(
            '이슈 총 $totalIssues개',
            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
        ),
        ...pieData.where((data) => data.count > 0).map(
              (data) => Padding(
                //count가 0보다 큰 경우만 표시
                padding: const EdgeInsets.symmetric(horizontal: 4.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                      width: 12,
                      height: 12,
                      color: data.color,
                    ),
                    const SizedBox(width: 8),
                    Text('${data.label} : ${data.count}개',
                        style: const TextStyle(fontSize: 14)),
                  ],
                ),
              ),
            ),
      ],
    );
  }
}

// 기존의 PieModel 및 DonutChart 클래스 정의
class PieModel {
  final int count;
  final Color color;
  final String label;

  PieModel({
    required this.count,
    required this.color,
    required this.label,
  });
}

class DonutChart extends CustomPainter {
  final List<PieModel> data;
  DonutChart(this.data);

  @override
  void paint(Canvas canvas, Size size) {
    double total = data.fold(0, (sum, item) => sum + item.count);
    double startAngle = -math.pi / 2;
    Paint paint = Paint()
      ..style = PaintingStyle.stroke
      ..strokeWidth = 50;

    for (var item in data) {
      final sweepAngle = (item.count / total) * 2 * math.pi;
      paint.color = item.color;
      canvas.drawArc(
        Rect.fromCircle(
            center: Offset(size.width / 2, size.height / 2),
            radius: size.width / 2),
        startAngle,
        sweepAngle,
        false,
        paint,
      );
      startAngle += sweepAngle;
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}
