import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

TextStyle headingTextStyle() {
  return TextStyle(
      fontFamily: 'DM Sans',
      fontSize: 30.sp,
      fontWeight: FontWeight.w700,
      height: 1.2, // Equivalent to line-height: 36px; in CSS
      letterSpacing: 0.0, // Equivalent to letter-spacing: 0em; in CSS

      color: Colors.white,

  );
}

TextStyle alertTextStyle(){
  return TextStyle(
      fontWeight: FontWeight.bold,
      color:  const Color(0xFFFD7E14),fontSize: 14.sp);
}


TextStyle bodyTextStyle() {
  return TextStyle(
      fontFamily: 'DM Sans',
      fontSize: 16.sp,
      fontWeight: FontWeight.w500,
      height: 1.1875, // Equivalent to line-height: 19px; in CSS
      letterSpacing: 0.0, // Equivalent to letter-spacing: 0em; in CSS
     color: Colors.white,
  );
}

TextStyle subBodyTextStyle() {
  return TextStyle(color: const Color(0xFFADB5BD),
    fontFamily: 'DM Sans',
    fontSize: 15.0.sp,
    fontWeight: FontWeight.w400,
    height: 1.2, // You can adjust this value for line-height
    letterSpacing: 0.0, // No letter spacing
  );
}

Color iconColor(context) {
  return Theme.of(context).colorScheme.secondary;
}
