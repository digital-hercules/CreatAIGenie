import 'package:flutter/material.dart';





extension ScreenUtils on BuildContext {
  double get heightWithoutSafeArea => (MediaQuery.of(this).size.height-MediaQuery.of(this).viewPadding.top-MediaQuery.of(this).viewPadding.bottom);
  double get heightWithoutSafeAreaAndAppbar => (MediaQuery.of(this).size.height-MediaQuery.of(this).viewPadding.top-MediaQuery.of(this).viewPadding.bottom) -AppBar().preferredSize.height;
  double get height => MediaQuery.of(this).size.height;
  double get width => MediaQuery.of(this).size.width;
  bool get isPatriot=>MediaQuery.of(this).orientation == Orientation.portrait;


}
