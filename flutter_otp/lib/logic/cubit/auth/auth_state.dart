import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';

@immutable
abstract class AuthState extends Equatable {
  @override
  List<Object> get props => [];
}

class AuthInitial extends AuthState {
}

class AuthLoading extends AuthState {
}

class AuthNetworkError extends AuthState {
  final String message;

  AuthNetworkError({
    required this.message,
  });

  @override
  List<Object> get props => [message];
}

class AuthError extends AuthState {
  final String message;

  AuthError({
    required this.message,
  });

  @override
  List<Object> get props => [message];
}

class AuthLoaded extends AuthState {
  final dynamic response;

  AuthLoaded({
    required this.response,
  });

  @override
  List<Object> get props => [response];
}
class AuthSubmit extends AuthState {
  final dynamic data;

  AuthSubmit({
    required this.data,
  });

  @override
  List<Object> get props => [data];
}

