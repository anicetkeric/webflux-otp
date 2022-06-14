
import 'dart:convert';
import 'dart:io';
import 'package:flutter_otp/core/exceptions/api_exceptions.dart';
import 'package:http/http.dart' as http;
import 'dart:developer' as developer;
import 'package:flutter_otp/core/constants/app_constants.dart';
import 'package:flutter_otp/data/domain/model/login.dart';
import 'package:shared_preferences/shared_preferences.dart';


class AuthProvider {

  static ApiClient apiClient = ApiClient();

  Future<dynamic> signIn(Login login) async {
    return await apiClient.post(Uri.parse(baseApi + loginUrl), login.toJson(), {'Content-Type':'application/json', 'Accept': 'application/json'});
  }

  Future<dynamic> checkOtpCode(String code) async {
    String? token = await getToken();
    return await apiClient.get(Uri.parse("${baseApi + otpCheckUrl}/$code"), {'Content-Type':'application/json', 'Accept': 'application/json', 'Authorization': 'Bearer $token'});
  }

  Future<dynamic> resendOtpCode() async {
    String? token = await getToken();
    return await apiClient.get(Uri.parse(baseApi + otpResendUrl), {'Content-Type':'application/json', 'Accept': 'application/json', 'Authorization': 'Bearer $token'});
  }

}

class ApiClient {


  Future<dynamic> get(Uri url, Map<String, String>? headers) async {
    dynamic responseJson;
    try {
      final response = await http.get(url, headers: headers);
      responseJson = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api get received!');
    return responseJson;
  }

  Future<dynamic> post(Uri url, dynamic body, Map<String, String>? headers) async {
    dynamic responseJson;
    try {
      final response = await http.post(url, headers: headers , body: jsonEncode(body));
      responseJson = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api post data!');
    return responseJson;
  }
}

Future<String?> getToken() async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  return prefs.getString(tokenKey);
}

dynamic _returnResponse(http.Response response) {
  var responseJson = jsonDecode(utf8.decode(response.bodyBytes));
  var errorResp = responseJson["message"] ?? response.body.toString();
  switch (response.statusCode) {
    case 200:
      return responseJson;
    case 201:
      return responseJson;
    case 400:
      throw BadRequestException(response.body.toString());
    case 401:
    case 403:
      throw UnauthorisedException(errorResp);
    case 404:
      throw NotFoundDataException(errorResp);
    case 500:
      throw InternalErrorException(errorResp);
    default:
      return FetchDataException(
          'Error occurred while Communication with Server with StatusCode : ${response.statusCode}');
  }
}