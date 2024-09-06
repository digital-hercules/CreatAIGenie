import 'package:flutter/material.dart';

Color parseColorCode(String strColor) {
  return Color(int.parse(strColor.substring(1, 7), radix: 16) + 0xFF000000);
}


extension ColorUtils on BuildContext {
  ColorScheme get colorScheme => Theme.of(this).colorScheme;


}