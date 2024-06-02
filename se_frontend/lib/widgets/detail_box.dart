import 'package:flutter/material.dart';

class DetailBox extends StatelessWidget {
  final String item;
  final String content;

  final greybackground = const Color(0xff3E3E3E);

  const DetailBox({
    super.key,
    required this.item,
    required this.content,
  });

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start, // Text를 왼쪽 정렬
        children: [
          Text(
            item,
            style: const TextStyle(
              fontSize: 25,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 10), // Text와 Container 사이의 간격
          Container(
            width: double.infinity, // Container의 너비를 화면의 너비로 설정
            decoration: BoxDecoration(
              color: greybackground,
              borderRadius: BorderRadius.circular(15.0), // 원하는 둥근 모서리 반경 설정
            ),
            padding: const EdgeInsets.all(20.0), // 내용과의 간격을 위한 패딩 설정 (선택 사항)
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start, // 내용물을 왼쪽 정렬
              children: [
                Text(
                  content,
                  style: const TextStyle(
                    fontSize: 20,
                    color: Colors.white,
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 25),
        ],
      ),
    );
  }
}
