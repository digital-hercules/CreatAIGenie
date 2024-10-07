import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';

import '../../../utils/constants/app_spacer_constants.dart';
import '../../../utils/constants/scale_factor.dart';


class NotificationScreen extends StatelessWidget {
  const NotificationScreen({super.key});



  @override
  Widget build(BuildContext context) {
    return  Scaffold(
      appBar: AppBar(
        leading: GestureDetector(
          onTap: (){Navigator.pop(context);},
          child: const Icon(
            Icons.arrow_back_ios_rounded,
            color: Color(0xffB219F0),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.only(
          left: 20,
          right: 20,
        ),
        child: CustomScrollView(
          slivers: [
            SliverFillRemaining(
                hasScrollBody: false,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const SizedBox(
                          width: double.infinity,
                        ),
                        Text(
                          'Notification',
                          style: context.displayMedium,
                          textScaler: TextScaler.linear(
                            ScaleSize.textScaleFactor(context),
                          ),
                          maxLines: 1,
                        ),
                        AppSpacer.p24(),
                        Container(

                          child: Center(
                            child: ListTile(

                              tileColor: const Color(0xfff9f9f9),
                              hoverColor: const Color(0xfff9f9f9),
                              leading: Image.asset('assets/icons/check.png'),
                              title: const Text('your code #1234 has\nbeen approve'),


                            ),
                          ),
                        ),
                        AppSpacer.p20(),

                        Container(

                          child: Center(
                            child: ListTile(

                              tileColor: const Color(0xfff9f9f9),
                              hoverColor: const Color(0xfff9f9f9),
                              leading: Image.asset('assets/icons/check.png'),
                              title: const Text('your code #1234 has\nbeen approve'),


                            ),
                          ),
                        ),
                        AppSpacer.p20(),
                        Container(

                          child: Center(
                            child: ListTile(

                              tileColor: const Color(0xfff9f9f9),
                              hoverColor: const Color(0xfff9f9f9),
                              leading: Image.asset('assets/icons/check.png'),
                              title: const Text('your code #1234 has\nbeen approve'),


                            ),
                          ),
                        ),

                      ],
                    ),

                  ],
                ))
          ],
        ),
      ),
    );
  }
}
