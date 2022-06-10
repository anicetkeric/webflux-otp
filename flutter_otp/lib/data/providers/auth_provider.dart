
import 'dart:convert';
import 'dart:io';
import 'package:flutter_otp/core/exceptions/api_exceptions.dart';
import 'package:http/http.dart' as http;
import 'dart:developer' as developer;
import 'package:flutter_otp/core/constants/app_constants.dart';
import 'package:flutter_otp/data/domain/model/login.dart';


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

class ApiClient {

  Future<dynamic> get(Uri url) async {
    dynamic responseJson;
    try {
      final response = await http.get(url);
      responseJson = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api get received!');
    return responseJson;
  }

  Future<dynamic> post(Uri url, dynamic body) async {
    dynamic responseJson;
    try {
      final response = await http.post(url, headers: {'Content-Type':'application/json'} , body: jsonEncode(body));
      responseJson = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api post data!');
    return responseJson;
  }
}


dynamic _returnResponse(http.Response response) {
  switch (response.statusCode) {
    case 200:
      var responseJson = jsonDecode(utf8.decode(response.bodyBytes));
      return responseJson;
    case 201:
      var responseJson = jsonDecode(utf8.decode(response.bodyBytes));
      return responseJson;
    case 400:
      throw BadRequestException(response.body.toString());
    case 401:
    case 403:
      throw UnauthorisedException(response.body.toString());
    case 404:
      throw NotFoundDataException(response.body.toString());
    case 500:
      throw InternalErrorException(response.body.toString());
    default:
      return FetchDataException(
          'Error occurred while Communication with Server with StatusCode : ${response.statusCode}');
  }
}