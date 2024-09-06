import 'package:aigeniog/utils/constants/responive.dart';
import 'package:aigeniog/utils/extensions/screen_util_extension.dart';
import 'package:aigeniog/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';

import '../../../utils/constants/scale_factor.dart';

class LowerContainer extends StatelessWidget {

  final String upperText;
  final String lowerText;
  final  String image;
  final Color color;

  const LowerContainer({super.key, required this.upperText,required this.lowerText,required this.image,required this.color});
  @override
  Widget build(BuildContext context) {
    return Container(
      width:Responsive.isTablet(context)? context.width*0.46:context.width*0.43,

      padding: const EdgeInsets.all(10),

      decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(10),
          color: color,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(upperText,style: context.displayMedium!.copyWith(fontSize: 15,fontWeight: FontWeight.w600,color: Colors.white,),textScaler: TextScaler.linear(
          ScaleSize.textScaleFactor(context),)),

          const SizedBox(height: 10),
          Text(lowerText,style: context.displaySmall!.copyWith(fontSize: 9,fontWeight: FontWeight.w500,color: Colors.white),textScaler: TextScaler.linear(
            ScaleSize.textScaleFactor(context),)),
          const SizedBox(height: 10),
          Align(
              alignment: Alignment.centerRight,
              child: Image.asset(image)),

        ],
      ),


    );
  }
}
