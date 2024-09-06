import 'package:aigeniog/ui/welcome_screen/welcome_screen.dart';
import 'package:aigeniog/utils/constants/app_spacer_constants.dart';
import 'package:aigeniog/utils/extensions/screen_util_extension.dart';
import 'package:aigeniog/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

import '../../utils/constants/scale_factor.dart';

class Feature4Screen extends StatelessWidget {
  const Feature4Screen({super.key});

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
          backgroundColor: const Color(0xffd386f2),
          body: Padding(
            padding: const EdgeInsets.all(20),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Column(
                  children: [
                    AppSpacer.p20(),
                    Text(
                      'InstaScript',
                      style: context.displayMedium!.copyWith(color: Colors.white),
                      textScaler: TextScaler.linear(
                        ScaleSize.textScaleFactor(context),
                      ),
                    ),
                    AppSpacer.p20(),
                    SvgPicture.asset('assets/images/feature4.svg',height: context.height*0.3,fit: BoxFit.fitHeight,),
                    AppSpacer.p20(),
                    Text(
                      'Effortlessly generate real-time, tailored content with our AI Bot- your ultimate solution for your social media engagement . Make your  Gmail, subtitles or article article effortless and convenient. ',
                      style: context.displaySmall!.copyWith(color: Colors.white),
                      textScaler: TextScaler.linear(
                        ScaleSize.textScaleFactor(context),
                      ),
                    ),

                  ],
                ),


                Column(
                  children: [
                    SizedBox(
                      width: 105,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [

                          SvgPicture.asset('assets/icons/not_tick.svg'),
                          SvgPicture.asset('assets/icons/not_tick.svg'),
                          SvgPicture.asset('assets/icons/not_tick.svg'),
                          SvgPicture.asset('assets/icons/tick.svg'),


                        ],
                      ),
                    ),

                    const SizedBox(height: 25,),
                    GestureDetector(
                      onTap: (){
                        Navigator.push(context, MaterialPageRoute(builder: (context) => const WelcomeScreen(),));
                      },
                      child: SizedBox(
                        width: 90,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text('Let’s Go',style: context.displaySmall!.copyWith(fontWeight: FontWeight.w700,color: Colors.white),
                            ),

                            SvgPicture.asset('assets/icons/forword2.svg')

                          ],),
                      ),
                    )
                  ],
                ),



              ],
            ),
          )),
    );
  }
}
