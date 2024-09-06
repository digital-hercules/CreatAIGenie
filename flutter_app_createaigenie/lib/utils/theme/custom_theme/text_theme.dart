import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class TTextTheme {
  TTextTheme._();

  // Light theme text styles
  static TextTheme lightTextTheme = TextTheme(
    displayLarge: GoogleFonts.lora(
      textStyle: const TextStyle(
        color: Color(0xffB219F0),
        fontFamily: 'Lora',
        fontWeight: FontWeight.w600,
        fontSize: 25.0,
        height: 2.0, // Corresponds to line height of 50px (50px / 25px)
      ),
    ),
    displayMedium: GoogleFonts.poppins(
      textStyle: const TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: 25,
        height: 1.5, // 37.5px line height for 25px font size
        color: Colors.black,
      ),
    ),
    displaySmall: GoogleFonts.poppins(
      textStyle: const TextStyle(
        fontWeight: FontWeight.w500,
        fontSize: 16,
        color: Color(0xffB219F0),
      ),
    ),
  );

  // Dark theme text styles
  static TextTheme darkTextTheme = TextTheme(
    displayLarge: GoogleFonts.lora(
      textStyle: const TextStyle(
        color: Colors.white,
        fontFamily: 'Lora',
        fontWeight: FontWeight.w600,
        fontSize: 25.0,
        height: 2.0, // Corresponds to line height of 50px (50px / 25px)
      ),
    ),
    displayMedium: GoogleFonts.poppins(
      textStyle: const TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: 25,
        height: 1.5, // 37.5px line height for 25px font size
        color: Colors.white70, // Slightly lighter than pure white for better contrast
      ),
    ),
    displaySmall: GoogleFonts.poppins(
      textStyle: const TextStyle(
        fontWeight: FontWeight.w500,
        fontSize: 16,
        color: Colors.white70, // To ensure visibility on dark backgrounds
      ),
    ),
  );
}
