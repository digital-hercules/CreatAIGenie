import 'package:create_ai_genie/utils/theme/custom_theme/text_theme.dart';
import 'package:create_ai_genie/utils/theme/custom_theme/textinput_theme.dart';
import 'package:flutter/material.dart';

class TAppTheme {
  TAppTheme._();

  static ThemeData lightTheme = ThemeData(
    useMaterial3: true,
    brightness: Brightness.light,
    textTheme: TTextTheme.lightTextTheme,
    scaffoldBackgroundColor: Colors.white,
    primaryColor: const Color(0xffB219F0),
    inputDecorationTheme: TInputTheme.lightInputDecoration,
  );

  static ThemeData darkTheme = ThemeData(
    useMaterial3: true,
    brightness: Brightness.dark,
    textTheme: TTextTheme.darkTextTheme,
    scaffoldBackgroundColor: Colors.black,
    primaryColor: const Color(0xffB219F0),
    inputDecorationTheme: TInputTheme.darkInputDecoration,
  );
}
