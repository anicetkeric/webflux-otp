


import 'package:flutter_otp/core/constants/app_constants.dart';
import 'package:flutter_otp/data/domain/model/login.dart';

import 'api_client.dart';

class AuthProvider {

  static ApiClient apiClient = ApiClient();

  Future<dynamic> signIn(Login login) async {
    return await apiClient.post(Uri.parse(baseApi + loginUrl), login.toJson());
  }

  Future<dynamic> checkOtpCode(String code) async {
    return await apiClient.get(Uri.parse("${baseApi + otpCheckUrl}/$code"));
  }

  Future<dynamic> resendOtpCode() async {
    return await apiClient.get(Uri.parse(baseApi + otpResendUrl));
  }

}