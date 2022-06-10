import 'package:flutter/material.dart';
import 'package:flutter_otp/ui/router/app_router.dart';
import 'package:flutter_otp/core/app_theme.dart';
import 'package:flutter_otp/logic/debug/app_bloc_observer.dart';
import 'package:flutter_otp/ui/screens/splash_screen.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

void main() {
  BlocOverrides.runZoned(
        () => runApp(const MyApp()),
    blocObserver: AppBlocObserver(),
  );
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
