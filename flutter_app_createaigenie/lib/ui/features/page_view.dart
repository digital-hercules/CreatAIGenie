import 'package:create_ai_genie/ui/features/feature1.dart';
import 'package:create_ai_genie/ui/features/feature2.dart';
import 'package:create_ai_genie/ui/features/feature3.dart';
import 'package:create_ai_genie/ui/features/feature4.dart';
import 'package:flutter/material.dart';


class PageViewFlutter extends StatelessWidget {
  const PageViewFlutter({super.key});

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: PageView(
          children: const [
            Feature1Screen(),
            Feature2Screen(),
            Feature3Screen(),
            Feature4Screen(),
          ],
        ),
      ),
    );
  }
}
