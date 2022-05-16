import 'package:flutter/material.dart';
import 'package:flutter_otp/screens/login_screen.dart';
import 'package:flutter_otp/screens/otp_screen.dart';
import 'package:flutter_otp/screens/splash_screen.dart';

import 'exceptions/route_exception.dart';


class AppRouter {
  const AppRouter._();


  static Route<dynamic> onGenerateRoute(RouteSettings settings) {

    switch (settings.name) {
      case SplashScreen.routeName:
        return MaterialPageRoute(
          builder: (_) => const SplashScreen(),
        );
      case LoginScreen.routeName:
        return MaterialPageRoute(
          builder: (_) => const LoginScreen(),
        );
      case OtpScreen.routeName:
        return MaterialPageRoute(
          builder: (_) => const OtpScreen(userEmail: "anicetkouame@yahoo.fr"),
        );
      default:
        throw const RouteException('Route not found!');
    }
  }
}
