

import 'package:flutter_otp/data/domain/api_response/success_response.dart';
import 'package:flutter_otp/data/domain/model/login.dart';
import 'package:flutter_otp/data/providers/auth_provider.dart';

class AuthRepository {

  final AuthProvider provider;

  AuthRepository({required this.provider});

  Future<SuccessResponse> userLogin(Login login) async {
    final response = await provider.signIn(login);
    return SuccessResponse.fromJson(response);
  }
  Future<SuccessResponse> checkOtpCode(String code) async {
    final response = await provider.checkOtpCode(code);
    return SuccessResponse.fromJson(response);
  }
  Future<SuccessResponse> resendOtpCode() async {
    final response = await provider.resendOtpCode();
    return SuccessResponse.fromJson(response);
  }

}