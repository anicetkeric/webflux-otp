import 'package:flutter/material.dart';
import 'package:flutter_otp/core/exceptions/route_exception.dart';
import 'package:flutter_otp/data/providers/auth_provider.dart';
import 'package:flutter_otp/data/repositories/auth_repository.dart';
import 'package:flutter_otp/logic/cubit/auth/auth_cubit.dart';
import 'package:flutter_otp/ui/screens/dashboard_screen.dart';
import 'package:flutter_otp/ui/screens/login_screen.dart';
import 'package:flutter_otp/ui/screens/otp_screen.dart';
import 'package:flutter_otp/ui/screens/splash_screen.dart';
import 'package:flutter_bloc/flutter_bloc.dart';



class AppRouter {
  const AppRouter._();

  static Route<dynamic> onGenerateRoute(RouteSettings settings) {

    AuthCubit restaurantCubit =  AuthCubit(repository: AuthRepository(provider: AuthProvider()));


    switch (settings.name) {
      case SplashScreen.routeName:
        return MaterialPageRoute(
          builder: (_) => const SplashScreen(),
        );
      case LoginScreen.routeName:
        return MaterialPageRoute(
          builder: (_) => BlocProvider(
            create: (BuildContext context) => restaurantCubit,
            child: const LoginScreen(),
          ),
        );
      case OtpScreen.routeName:
        return MaterialPageRoute(
          builder: (_) => BlocProvider(
            create: (BuildContext context) => restaurantCubit,
            child: const OtpScreen(userEmail: "anicetkouame@yahoo.fr")
          ),
        );
      case DashboardScreen.routeName:
        return MaterialPageRoute(
          builder: (_) => const DashboardScreen()
        );
      default:
        throw const RouteException('Route not found!');
    }
  }
}
