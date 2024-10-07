import 'package:create_ai_genie/ui/user_selection/user_selection.dart';
import 'package:create_ai_genie/utils/extensions/screen_util_extension.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../utils/constants/app_spacer_constants.dart';
import '../../utils/constants/scale_factor.dart';
import '../common_widgets/round_button.dart';


class WelcomeScreen extends StatelessWidget {
  const WelcomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
      ),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.only(left: 20,right: 20,bottom: 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(
                    width: double.infinity,
                  ),
                  Text(
                    'Creat AI Genie',
                    style: context.displayMedium,
                    textScaler: TextScaler.linear(
                      ScaleSize.textScaleFactor(context),
                    ),
                    maxLines: 1,
                  ),
                  Image.asset(
                    'assets/images/welcomeImage.png',
                    width: context.width*0.8,
                    height: context.height*0.3,
                    fit: BoxFit.fitHeight,
                  ),
                   AppSpacer.p15(),
                  SizedBox(
                      width: 260.w,
                      // height: 99.h,
                      //  color: Colors.red,
                      child: Column(
                        children: [
                          FittedBox(
                              fit: BoxFit.scaleDown,
                              child: Center(
                                  child: Text('Welcome to',
                                      textScaler: TextScaler.linear(
                                        ScaleSize.textScaleFactor(context),
                                      ),

                                      textAlign: TextAlign.center,
                                      style: context.displayLarge!.copyWith(fontSize: 35, height: 50.0.h /
                                          35.0.sp, // Line height calculation
                                      ))

                                   )),
                          Center(
                            child: FittedBox(
                              fit: BoxFit.scaleDown,
                              child: Text.rich(
                                textScaler: TextScaler.linear(
                                  ScaleSize.textScaleFactor(context),
                                ),
                                TextSpan(
                                  children: [
                                    TextSpan(
                                        text: 'Creat',
                                        style:context.displayLarge!.copyWith(fontSize: 40,fontWeight:FontWeight.w700, height: 50.0.h /
                                            35.0.sp, )
                                       ),
                                    TextSpan(
                                      text: 'AI',
                                      style:context.displayLarge!.copyWith(fontSize: 40,fontWeight:FontWeight.w700, height: 50.0.h /
                                          35.0.sp, color: Colors.black)
                                    ),
                                    TextSpan(
                                        text: 'Genie',
                                        style: GoogleFonts.lora(
                                          textStyle: TextStyle(
                                            color: const Color(0xffB219F0),
                                            fontFamily: 'Lora',
                                            fontWeight: FontWeight.w700,
                                            fontSize: 40.0,
                                            height: 50.0.h /
                                                35.0.sp, // Line height calculation
                                          ),
                                        )),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ],
                      )),
                  AppSpacer.p12(),
                  Text(
                      'Meet AIGenie, your personal AI language model& discover the benefits of using AI Genie for language tasks',
                    style: context.displaySmall,
                    textScaler: TextScaler.linear(
                    ScaleSize.textScaleFactor(context),
                  ) ,
                    maxLines: 4,),




                ],
              ),

              RoundButton(text: 'Get Started', voidCallBack: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) => const UserSelection(),));

              },),
            ],
          ),
        ),
      ),
    );
  }
}


