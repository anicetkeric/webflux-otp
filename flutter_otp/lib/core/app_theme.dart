import 'package:flutter/material.dart';
import 'package:flutter_otp/core/constants/color.dart';

class AppTheme {
  const AppTheme._();

  static final lightTheme = ThemeData(
      primarySwatch: Colors.blue,
      primaryColor: primary,
      visualDensity: VisualDensity.adaptivePlatformDensity,
      errorColor: Colors.red[400],
      indicatorColor: Colors.green[300],
      accentColor: secondary,
      splashColor: Colors.blueAccent.withAlpha(40)
  );
}
