
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../utils/constants/responive.dart';

class RoundButton extends StatelessWidget {
  final String text;
  VoidCallback voidCallBack;

  RoundButton({super.key, required this.text,required this.voidCallBack});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: voidCallBack,
      child: Container(
        width:Responsive.isDesktop(context) ?162:162.w,
        height: Responsive.isDesktop(context)? 60:60.h,
        decoration: BoxDecoration(
          border: Border.all(color: Colors.black,width: 1.5),
          borderRadius: BorderRadius.circular(20),
        ),
        child: Center(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              Text('Get Started',style:GoogleFonts.poppins(
                textStyle: const TextStyle(
                  fontFamily: 'Poppins',
                  fontWeight: FontWeight.w500,
                  fontSize: 18.0,
                  height: 1.333, // Corresponds to line height of 24px (24px / 18px)
                  color: Colors.black, // You can change the color according to your requirement
                  // If you want to align the text, you should do it in the container or widget holding the text
                ),),),
              const Icon(Icons.arrow_forward_ios_rounded,size: 20,)
            ],
          ),
        ),
      ),
    );
  }
}