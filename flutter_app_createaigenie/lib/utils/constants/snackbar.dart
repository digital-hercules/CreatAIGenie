
import 'package:create_ai_genie/utils/constants/textStyles.dart';
import 'package:flutter/material.dart';
import 'app_spacer_constants.dart';
// Define a function to create a custom SnackBar with dynamic text.
SnackBar customSnackBar(String message) {
  return SnackBar(
    closeIconColor: Colors.white,
    backgroundColor: const Color(0xffa30010),
    content: Row(
      children: [
        Image.asset('lib/icons/cancelIcon.png'),
        AppSpacer.p8(),
        Flexible(
          child: Text(
            message,
            style: bodyTextStyle(),
          ),
        ),
      ],
    ),
    showCloseIcon: true,
  );
}

