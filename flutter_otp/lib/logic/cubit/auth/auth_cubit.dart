
import 'package:flutter_bloc/flutter_bloc.dart';
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

      emit(AuthLoaded(auth: response ));
    } catch (e) {
      emit(AuthError(message: e.toString()));
    }
  }
  otpCheck(String code) async {
    try {
      emit(AuthLoading());
      final response = await repository.checkOtpCode(code);

      emit(AuthLoaded(auth: response ));
    } catch (e) {
      emit(AuthError(message: e.toString()));
    }
  }

  otpResend() async {
    try {
      emit(AuthLoading());

      final response = await repository.resendOtpCode();

      emit(AuthLoaded(auth: response ));
    } catch (e) {
      emit(AuthError(message: e.toString()));
    }
  }

}