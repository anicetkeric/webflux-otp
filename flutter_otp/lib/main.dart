import 'package:flutter/material.dart';
import 'package:flutter_otp/core/app_router.dart';
import 'package:flutter_otp/core/app_theme.dart';
import 'package:flutter_otp/screens/splash_screen.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
        designSize: const Size(375, 812),
        minTextAdapt: true,
        splitScreenMode: true,
        builder: (child) {
          return MaterialApp(
              debugShowCheckedModeBanner: false,
              title: 'OTP',
              // You can use the library anywhere in the app even in theme
              theme: AppTheme.lightTheme,
              initialRoute: SplashScreen.routeName,
              onGenerateRoute: AppRouter.onGenerateRoute
          );
        }
    );
  }
}