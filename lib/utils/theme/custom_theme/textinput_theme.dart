import 'package:flutter/material.dart';

class TInputTheme {
  TInputTheme._();

  // Light input decoration theme
  static InputDecorationTheme lightInputDecoration = InputDecorationTheme(
    filled: true,
    fillColor: Colors.white,
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(8.0),
      borderSide: const BorderSide(color: Color(0xffB219F0)),
    ),
    enabledBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(8.0),
      borderSide: const BorderSide(color: Color(0xffB219F0)),
    ),
    focusedBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(8.0),
      borderSide: const BorderSide(color: Colors.blue),
    ),
    labelStyle: const TextStyle(color: Colors.black),
  );

  // Dark input decoration theme
  static InputDecorationTheme darkInputDecoration = InputDecorationTheme(
    filled: true,
    fillColor: Colors.black,
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(8.0),
      borderSide: const BorderSide(color: Color(0xffB219F0)),
    ),
    enabledBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(8.0),
      borderSide: const BorderSide(color: Color(0xffB219F0)),
    ),
    focusedBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(8.0),
      borderSide: const BorderSide(color: Colors.blue),
    ),
    labelStyle: const TextStyle(color: Colors.white70),
  );
}
