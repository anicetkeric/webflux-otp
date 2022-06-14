
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_otp/core/exceptions/api_exceptions.dart';
import 'package:flutter_otp/data/domain/model/login.dart';
import 'package:flutter_otp/data/repositories/auth_repository.dart';

import 'auth_state.dart';


class AuthCubit extends Cubit<AuthState> {
  final AuthRepository repository;

  AuthCubit({required this.repository}) : super(AuthInitial());

  userLogin(Login login) async {
    try {
      emit(AuthLoading());
      final response = await repository.userLogin(login);

      emit(AuthSuccess(response: response ));
    } on NetworkStatusException catch(e) {
      emit(AuthNetworkError(message: e.toString()));
    } catch (e) {
      emit(AuthError(message: e.toString()));
    }
  }
  otpCheck(String code) async {
    try {
      emit(AuthLoading());
      final response = await repository.checkOtpCode(code);

      emit(AuthSuccess(response: response ));

    } on NetworkStatusException catch(e) {
      emit(AuthNetworkError(message: e.toString()));
    } catch (e) {
      emit(AuthError(message: e.toString()));
    }
  }

  otpResend() async {
    try {
      emit(AuthLoading());

      final response = await repository.resendOtpCode();

      emit(AuthSubmit(data: response ));

    } on NetworkStatusException catch(e) {
      emit(AuthNetworkError(message: e.toString()));
    } catch (e) {
      emit(AuthError(message: e.toString()));
    }
  }

}
